package com.qualitrack.production.mapper;

import com.qualitrack.production.dto.ProductionMaterialDtoFull;
import com.qualitrack.production.entite.ProductionMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductionMaterialDetailMapper {
    @Mapping(target = "rawMaterialNom", ignore = true)
    @Mapping(target = "rawMaterialUnite", ignore = true)
    ProductionMaterialDtoFull toDto(ProductionMaterial entity);
}

