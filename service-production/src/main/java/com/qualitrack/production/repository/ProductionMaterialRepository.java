package com.qualitrack.production.repository;

import com.qualitrack.production.entite.ProductionMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionMaterialRepository extends JpaRepository<ProductionMaterial, Long> {
}

