package com.qualitrack.production.mapper;

import com.qualitrack.production.dto.QualityCheckDto;
import com.qualitrack.production.entite.QualityCheck;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QualityCheckMapper {
    @Mapping(target = "lot", ignore = true)
    QualityCheck toEntity(QualityCheckDto dto);

    @Mapping(source = "lot.id", target = "lotId")
    QualityCheckDto toDto(QualityCheck entity);
}

