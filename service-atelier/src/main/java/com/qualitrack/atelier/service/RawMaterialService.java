package com.qualitrack.atelier.service;

import com.qualitrack.atelier.entite.RawMaterial;
import com.qualitrack.atelier.exception.RessourceIntrouvableException;
import com.qualitrack.atelier.repository.RawMaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RawMaterialService {
    private final RawMaterialRepository repository;

    public RawMaterialService(RawMaterialRepository repository) { this.repository = repository; }

    public List<RawMaterial> getAll() { return repository.findAll(); }
    public RawMaterial getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RessourceIntrouvableException("RawMaterial not found: " + id));
    }
    public RawMaterial save(RawMaterial material) { return repository.saveAndFlush(material); }
    public RawMaterial modifier(Long id, RawMaterial updated) {
        RawMaterial existing = getById(id);
        existing.setNom(updated.getNom());
        existing.setFournisseur(updated.getFournisseur());
        existing.setStock(updated.getStock());
        existing.setUnite(updated.getUnite());
        return repository.saveAndFlush(existing);
    }
    @Transactional
    public RawMaterial decrementStock(Long id, Integer quantite) {
        RawMaterial material = getById(id);
        if (quantite == null || quantite <= 0) {
            throw new IllegalArgumentException("Quantité invalide");
        }
        if (material.getStock() < quantite) {
            throw new IllegalArgumentException("Stock insuffisant");
        }
        material.setStock(material.getStock() - quantite);
        return repository.saveAndFlush(material);
    }
    public void supprimer(Long id) { repository.delete(getById(id)); }
}