package com.qualitrack.atelier.service;

import com.qualitrack.atelier.entite.Machine;
import com.qualitrack.atelier.entite.StatutMachine;
import com.qualitrack.atelier.exception.RessourceIntrouvableException;
import com.qualitrack.atelier.repository.MachineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MachineService {
    private final MachineRepository repository;

    public MachineService(MachineRepository repository) { this.repository = repository; }

    public List<Machine> getAll() { return repository.findAll(); }
    public Machine getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RessourceIntrouvableException("Machine not found: " + id));
    }
    public Machine save(Machine machine) { return repository.saveAndFlush(machine); }
    public Machine modifier(Long id, Machine updated) {
        Machine existing = getById(id);
        existing.setNom(updated.getNom());
        existing.setType(updated.getType());
        existing.setCadenceHeure(updated.getCadenceHeure());
        existing.setStatut(updated.getStatut());
        return repository.saveAndFlush(existing);
    }
    @Transactional
    public Machine reserveMachine(Long id, String statut) {
        Machine machine = getById(id);
        if (machine.getStatut() != null && "RESERVEE".equalsIgnoreCase(machine.getStatut().name())) {
            throw new IllegalArgumentException("Machine déjà occupée");
        }
        if (statut == null || statut.isBlank()) {
            throw new IllegalArgumentException("Statut invalide");
        }
        machine.setStatut(StatutMachine.valueOf(statut.toUpperCase()));
        return repository.saveAndFlush(machine);
    }
    public void supprimer(Long id) { repository.delete(getById(id)); }
}