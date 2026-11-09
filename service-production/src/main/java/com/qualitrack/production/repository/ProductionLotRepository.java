package com.qualitrack.production.repository;

import com.qualitrack.production.entite.ProductionLot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionLotRepository extends JpaRepository<ProductionLot, Long> {
}

