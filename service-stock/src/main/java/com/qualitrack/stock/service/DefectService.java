package com.qualitrack.stock.service;

import com.qualitrack.stock.entity.Defect;
import com.qualitrack.stock.exception.RessourceIntrouvableException;
import com.qualitrack.stock.feignClient.ProductionClient;
import com.qualitrack.stock.feignClient.ProductionLotRequest;
import com.qualitrack.stock.repository.DefectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefectService {
    private final DefectRepository repository;
    private final ProductionClient productionClient;

    public DefectService(DefectRepository repository, ProductionClient productionClient) {
        this.repository = repository;
        this.productionClient = productionClient;
    }

    public List<Defect> getAll() {
        return repository.findAll();
    }

    public Defect getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Defect not found: " + id));
    }

    public Defect save(Defect entity) {
        if (entity.getLotId() == null) {
            throw new IllegalArgumentException("Lot invalide");
        }
        ProductionLotRequest lot = productionClient.getLotById(entity.getLotId());
        if (lot == null || lot.getStatut() == null || (!"EN_CONTROLE".equalsIgnoreCase(lot.getStatut()) && !"REBUTE".equalsIgnoreCase(lot.getStatut()))) {
            throw new IllegalArgumentException("Le lot associé doit être EN_CONTROLE ou REBUTE");
        }
        return repository.saveAndFlush(entity);
    }

    public Defect modifier(Long id, Defect updated) {
        Defect existing = getById(id);
        existing.setLotId(updated.getLotId());
        existing.setType(updated.getType());
        existing.setDescription(updated.getDescription());
        existing.setGravite(updated.getGravite());
        existing.setDateDetection(updated.getDateDetection());
        return repository.saveAndFlush(existing);
    }

    public void supprimer(Long id) {
        repository.delete(getById(id));
    }
}
