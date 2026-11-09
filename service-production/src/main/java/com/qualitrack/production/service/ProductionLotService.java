package com.qualitrack.production.service;

import com.qualitrack.production.entite.ProductionLot;
import com.qualitrack.production.entite.ProductionMaterial;
import com.qualitrack.production.entite.StatutLot;
import com.qualitrack.production.exception.RessourceIntrouvableException;
import com.qualitrack.production.exception.StatutTransitionException;
import com.qualitrack.production.repository.ProductionLotRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProductionLotService {
    private final ProductionLotRepository repository;

    private static final Map<StatutLot, Set<StatutLot>> VALID_TRANSITIONS = Map.of(
            StatutLot.EN_ATTENTE_RESSOURCES, Set.of(StatutLot.EN_PRODUCTION, StatutLot.ANNULE),
            StatutLot.EN_PRODUCTION, Set.of(StatutLot.EN_CONTROLE, StatutLot.ANNULE),
            StatutLot.EN_CONTROLE, Set.of(StatutLot.VALIDE, StatutLot.REBUTE),
            StatutLot.VALIDE, Set.of(),
            StatutLot.REBUTE, Set.of(),
            StatutLot.ANNULE, Set.of()
    );

    public ProductionLotService(ProductionLotRepository repository) {
        this.repository = repository;
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
}

