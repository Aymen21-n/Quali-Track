package com.qualitrack.production.mapper;

import com.qualitrack.production.dto.ProductionMaterialDto;
import com.qualitrack.production.entite.ProductionMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductionMaterialMapper {
    ProductionMaterial toEntity(ProductionMaterialDto dto);
    ProductionMaterialDto toDto(ProductionMaterial entity);
}

