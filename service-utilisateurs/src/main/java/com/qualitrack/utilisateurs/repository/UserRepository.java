package com.qualitrack.utilisateurs.repository;

import com.qualitrack.utilisateurs.entite.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
