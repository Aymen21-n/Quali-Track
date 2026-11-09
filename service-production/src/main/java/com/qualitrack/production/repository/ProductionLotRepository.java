package com.qualitrack.production.repository;

import com.qualitrack.production.entite.ProductionLot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductionLotRepository extends JpaRepository<ProductionLot, Long> {
    Optional<ProductionLot> findProductionLotByIdempotencyKey(String idempotencyKey);
}