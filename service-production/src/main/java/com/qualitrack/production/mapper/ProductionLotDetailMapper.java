package com.qualitrack.production.mapper;

import com.qualitrack.production.dto.ProductionLotDtoFull;
import com.qualitrack.production.entite.ProductionLot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = ProductionMaterialDetailMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductionLotDetailMapper {
    @Mapping(target = "machineNom", ignore = true)
    @Mapping(target = "machineReference", ignore = true)
    @Mapping(target = "controleurNom", ignore = true)
    @Mapping(target = "controleurEmail", ignore = true)
    @Mapping(target = "lignes", ignore = true)
    ProductionLotDtoFull toDto(ProductionLot entity);
}

