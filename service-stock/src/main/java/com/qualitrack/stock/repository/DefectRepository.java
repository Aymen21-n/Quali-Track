package com.qualitrack.stock.repository;

import com.qualitrack.stock.entity.Defect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefectRepository extends JpaRepository<Defect, Long> {
}

