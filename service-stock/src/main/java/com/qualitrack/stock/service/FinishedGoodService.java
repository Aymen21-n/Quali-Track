package com.qualitrack.stock.service;

import com.qualitrack.stock.entity.FinishedGood;
import com.qualitrack.stock.exception.RessourceIntrouvableException;
import com.qualitrack.stock.repository.FinishedGoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinishedGoodService {
    private final FinishedGoodRepository repository;

    public FinishedGoodService(FinishedGoodRepository repository) {
        this.repository = repository;
    }

    public List<FinishedGood> getAll() {
        return repository.findAll();
    }

    public FinishedGood getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("FinishedGood not found: " + id));
    }

    public FinishedGood save(FinishedGood entity) {
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

