package com.qualitrack.production.service;

import com.qualitrack.production.entite.QualityCheck;
import com.qualitrack.production.exception.RessourceIntrouvableException;
import com.qualitrack.production.repository.QualityCheckRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QualityCheckService {
    private final QualityCheckRepository repository;

    public QualityCheckService(QualityCheckRepository repository) {
        this.repository = repository;
    }

    public List<QualityCheck> getAll() {
        return repository.findAll();
    }

    public QualityCheck getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("QualityCheck not found: " + id));
    }

    public QualityCheck save(QualityCheck entity) {
        return repository.saveAndFlush(entity);
    }

    public QualityCheck modifier(Long id, QualityCheck updated) {
        QualityCheck existing = getById(id);
        existing.setLot(updated.getLot());
        existing.setControleurId(updated.getControleurId());
        existing.setDateControle(updated.getDateControle());
        existing.setResultat(updated.getResultat());
        existing.setCommentaire(updated.getCommentaire());
        return repository.saveAndFlush(existing);
    }

    public void supprimer(Long id) {
        repository.delete(getById(id));
    }
}

