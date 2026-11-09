package com.qualitrack.production.service;

import com.qualitrack.production.dto.ProductionLotDtoFull;
import com.qualitrack.production.dto.ProductionMaterialDtoFull;
import com.qualitrack.production.entite.ProductionLot;
import com.qualitrack.production.entite.ProductionMaterial;
import com.qualitrack.production.entite.StatutLot;
import com.qualitrack.production.exception.RessourceIntrouvableException;
import com.qualitrack.production.exception.StatutTransitionException;
import com.qualitrack.production.feignClient.*;
import com.qualitrack.production.mapper.ProductionLotDetailMapper;
import com.qualitrack.production.mapper.ProductionMaterialDetailMapper;
import com.qualitrack.production.repository.ProductionLotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductionLotService {
    private final ProductionLotRepository repository;
    private final AtelierClient atelierClient;
    private final UtilisateursClient utilisateursClient;
    private final RawMaterialClient rawMaterialClient;
    private final ProductionLotDetailMapper detailMapper;
    private final ProductionMaterialDetailMapper materialDetailMapper;

    private static final Map<StatutLot, Set<StatutLot>> VALID_TRANSITIONS = Map.of(
            StatutLot.EN_ATTENTE_RESSOURCES, Set.of(StatutLot.EN_PRODUCTION, StatutLot.ANNULE),
            StatutLot.EN_PRODUCTION, Set.of(StatutLot.EN_CONTROLE, StatutLot.ANNULE),
            StatutLot.EN_CONTROLE, Set.of(StatutLot.VALIDE, StatutLot.REBUTE),
            StatutLot.VALIDE, Set.of(),
            StatutLot.REBUTE, Set.of(),
            StatutLot.ANNULE, Set.of()
    );

    public ProductionLotService(ProductionLotRepository repository,
                                AtelierClient atelierClient,
                                UtilisateursClient utilisateursClient,
                                RawMaterialClient rawMaterialClient,
                                ProductionLotDetailMapper detailMapper,
                                ProductionMaterialDetailMapper materialDetailMapper) {
        this.repository = repository;
        this.atelierClient = atelierClient;
        this.utilisateursClient = utilisateursClient;
        this.rawMaterialClient = rawMaterialClient;
        this.detailMapper = detailMapper;
        this.materialDetailMapper = materialDetailMapper;
    }

    public List<ProductionLot> getAll() {
        return repository.findAll();
    }

    public ProductionLot getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("ProductionLot not found: " + id));
    }

    public ProductionLot save(ProductionLot lot) {
        if (lot.getLignes() != null) {
            for (ProductionMaterial ligne : lot.getLignes()) {
                ligne.setLot(lot);
            }
        }
        return repository.saveAndFlush(lot);
    }

    public ProductionLot modifier(Long id, ProductionLot updated) {
        ProductionLot existing = getById(id);

        existing.setIdempotencyKey(updated.getIdempotencyKey());
        existing.setMachineId(updated.getMachineId());
        existing.setControleurId(updated.getControleurId());
        existing.setDateLot(updated.getDateLot());
        existing.setStatut(updated.getStatut());
        existing.setCoutTotal(updated.getCoutTotal());

        List<ProductionMaterial> lignes = updated.getLignes() == null ? new ArrayList<>() : updated.getLignes();
        for (ProductionMaterial ligne : lignes) {
            ligne.setLot(existing);
        }
        existing.setLignes(lignes);

        return repository.saveAndFlush(existing);
    }

    public void supprimer(Long id) {
        repository.delete(getById(id));
    }

    public ProductionLot changerStatut(Long id, StatutLot nouveauStatut) {
        ProductionLot lot = getById(id);
        StatutLot actuel = lot.getStatut();

        if (!VALID_TRANSITIONS.getOrDefault(actuel, Set.of()).contains(nouveauStatut)) {
            throw new StatutTransitionException("Transition invalide: " + actuel + " -> " + nouveauStatut);
        }
        lot.setStatut(nouveauStatut);
        return repository.saveAndFlush(lot);
    }

    @Transactional
    public ProductionLot createLot(ProductionLot lot, String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank() || idempotencyKey.length() > 255) {
            throw new IllegalArgumentException("Clé d’idempotence invalide");
        }
        if (repository.findProductionLotByIdempotencyKey(idempotencyKey).isPresent()) {
            throw new IllegalArgumentException("Opération déjà traitée");
        }

        if (lot.getMachineId() == null) {
            throw new IllegalArgumentException("Machine indisponible");
        }
        if (lot.getControleurId() == null) {
            throw new IllegalArgumentException("Contrôleur invalide");
        }

        MachineDto machine = atelierClient.getMachineById(lot.getMachineId());
        if (!"DISPONIBLE".equalsIgnoreCase(machine.getStatut())) {
            throw new IllegalArgumentException("Machine indisponible");
        }

        UserDto controleur = utilisateursClient.getUserById(lot.getControleurId());
        if (controleur.getActif() == null || !controleur.getActif()) {
            throw new IllegalArgumentException("Contrôleur invalide");
        }
        if (controleur.getRole() != null && !"qualite".equalsIgnoreCase(controleur.getRole())) {
            throw new IllegalArgumentException("Contrôleur invalide");
        }

        List<ProductionMaterial> lignes = lot.getLignes() == null ? new ArrayList<>() : lot.getLignes();
        for (ProductionMaterial ligne : lignes) {
            RawMaterialDto rawMaterial = rawMaterialClient.getRawMaterialById(ligne.getRawMaterialId());
            if (rawMaterial.getStock() == null || rawMaterial.getStock() < ligne.getQuantite()) {
                throw new IllegalArgumentException("Stock insuffisant pour la matière " + rawMaterial.getNom());
            }
        }

        atelierClient.reserveMachine(lot.getMachineId(), new MachineReservationRequest("RESERVEE"));
        for (ProductionMaterial ligne : lignes) {
            rawMaterialClient.decrementStock(ligne.getRawMaterialId(), new RawMaterialStockUpdateRequest(ligne.getQuantite()));
        }

        lot.setIdempotencyKey(idempotencyKey);
        lot.setDateLot(new Date());
        lot.setStatut(StatutLot.EN_PRODUCTION);
        for (ProductionMaterial ligne : lignes) {
            ligne.setLot(lot);
        }
        return repository.saveAndFlush(lot);
    }

    @Transactional(readOnly = true)
    public ProductionLotDtoFull getDetail(Long id) {
        ProductionLot lot = getById(id);
        ProductionLotDtoFull dto = detailMapper.toDto(lot);

        MachineDto machine = atelierClient.getMachineById(lot.getMachineId());
        dto.setMachineNom(machine.getNom());
        dto.setMachineReference(machine.getType());

        UserDto user = utilisateursClient.getUserById(lot.getControleurId());
        dto.setControleurNom(user.getNom());
        dto.setControleurEmail(user.getEmail());

        if (lot.getLignes() != null) {
            List<ProductionMaterialDtoFull> items = new ArrayList<>();
            for (ProductionMaterial line : lot.getLignes()) {
                RawMaterialDto rawMaterial = rawMaterialClient.getRawMaterialById(line.getRawMaterialId());
                ProductionMaterialDtoFull item = materialDetailMapper.toDto(line);
                item.setRawMaterialNom(rawMaterial.getNom());
                item.setRawMaterialUnite(rawMaterial.getUnite());
                items.add(item);
            }
            dto.setLignes(items);
        }

        return dto;
    }
}