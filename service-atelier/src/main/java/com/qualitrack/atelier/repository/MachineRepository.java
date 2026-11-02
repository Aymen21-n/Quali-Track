package com.qualitrack.atelier.repository;

import com.qualitrack.atelier.entite.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineRepository extends JpaRepository<Machine, Long> {
}
