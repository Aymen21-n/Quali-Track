package com.qualitrack.utilisateurs.service;

import com.qualitrack.utilisateurs.entite.User;
import com.qualitrack.utilisateurs.exception.RessourceIntrouvableException;
import com.qualitrack.utilisateurs.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) { this.repository = repository; }

    public List<User> getAll() { return repository.findAll(); }
    public User getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RessourceIntrouvableException("User not found: " + id));
    }
    public User save(User user) { return repository.saveAndFlush(user); }
    public User modifier(Long id, User updated) {
        User existing = getById(id);
        existing.setNom(updated.getNom());
        existing.setEmail(updated.getEmail());
        existing.setRole(updated.getRole());
        existing.setActif(updated.isActif());
        return repository.saveAndFlush(existing);
    }
    public void supprimer(Long id) { repository.delete(getById(id)); }
}
