package com.qualitrack.stock.mapper;

import com.qualitrack.stock.dto.DefectDto;
import com.qualitrack.stock.entity.Defect;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DefectMapper {
    Defect toEntity(DefectDto dto);
    DefectDto toDto(Defect entity);
}

