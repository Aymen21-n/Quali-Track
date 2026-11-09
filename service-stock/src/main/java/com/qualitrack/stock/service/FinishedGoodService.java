package com.qualitrack.stock.service;

import com.qualitrack.stock.dto.FinishedGoodDetailDto;
import com.qualitrack.stock.entity.FinishedGood;
import com.qualitrack.stock.exception.RessourceIntrouvableException;
import com.qualitrack.stock.feignClient.ProductionClient;
import com.qualitrack.stock.feignClient.ProductionLotRequest;
import com.qualitrack.stock.repository.FinishedGoodRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FinishedGoodService {
    private final FinishedGoodRepository repository;
    private final ProductionClient productionClient;

    public FinishedGoodService(FinishedGoodRepository repository, ProductionClient productionClient) {
        this.repository = repository;
        this.productionClient = productionClient;
    }

    public List<FinishedGood> getAll() {
        return repository.findAll();
    }

    public FinishedGood getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("FinishedGood not found: " + id));
    }

    @Transactional(readOnly = true)
    public FinishedGoodDetailDto getDetail(Long id) {
        FinishedGood entity = getById(id);
        ProductionLotRequest lot = productionClient.getLotById(entity.getLotId());
        FinishedGoodDetailDto dto = new FinishedGoodDetailDto();
        dto.setId(entity.getId());
        dto.setLotId(entity.getLotId());
        dto.setLotStatut(lot.getStatut());
        dto.setDesignation(entity.getDesignation());
        dto.setQuantite(entity.getQuantite());
        dto.setDateEntree(entity.getDateEntree());
        return dto;
    }

    public FinishedGood save(FinishedGood entity) {
        if (entity.getLotId() == null) {
            throw new IllegalArgumentException("Lot invalide");
        }
        ProductionLotRequest lot = productionClient.getLotById(entity.getLotId());
        if (lot == null || lot.getStatut() == null || !"VALIDE".equalsIgnoreCase(lot.getStatut())) {
            throw new IllegalArgumentException("Le lot associé doit être VALIDE");
        }
        return repository.saveAndFlush(entity);
    }

    public FinishedGood modifier(Long id, FinishedGood updated) {
        FinishedGood existing = getById(id);
        existing.setLotId(updated.getLotId());
        existing.setDesignation(updated.getDesignation());
        existing.setQuantite(updated.getQuantite());
        existing.setDateEntree(updated.getDateEntree());
        return repository.saveAndFlush(existing);
    }

    public void supprimer(Long id) {
        repository.delete(getById(id));
    }
}
