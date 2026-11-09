package com.qualitrack.stock.service;

import com.qualitrack.stock.entity.Defect;
import com.qualitrack.stock.exception.RessourceIntrouvableException;
import com.qualitrack.stock.repository.DefectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefectService {
    private final DefectRepository repository;

    public DefectService(DefectRepository repository) {
        this.repository = repository;
    }

    public List<Defect> getAll() {
        return repository.findAll();
    }

    public Defect getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Defect not found: " + id));
    }

    public Defect save(Defect entity) {
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

