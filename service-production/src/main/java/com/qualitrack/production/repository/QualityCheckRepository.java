package com.qualitrack.production.repository;

import com.qualitrack.production.entite.QualityCheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualityCheckRepository extends JpaRepository<QualityCheck, Long> {
}

