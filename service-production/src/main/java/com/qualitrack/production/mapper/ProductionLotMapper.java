package com.qualitrack.production.mapper;

import com.qualitrack.production.dto.ProductionLotDto;
import com.qualitrack.production.entite.ProductionLot;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = ProductionMaterialMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductionLotMapper {
    ProductionLot toEntity(ProductionLotDto dto);
    ProductionLotDto toDto(ProductionLot entity);
}

