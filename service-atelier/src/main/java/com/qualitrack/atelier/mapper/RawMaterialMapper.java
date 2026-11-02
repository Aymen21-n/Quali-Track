package com.qualitrack.atelier.mapper;

import com.qualitrack.atelier.dto.RawMaterialDto;
import com.qualitrack.atelier.entite.RawMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RawMaterialMapper {
    RawMaterial toEntity(RawMaterialDto dto);
    RawMaterialDto toDto(RawMaterial entity);
}
