package com.qualitrack.stock.mapper;

import com.qualitrack.stock.dto.FinishedGoodDto;
import com.qualitrack.stock.entity.FinishedGood;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FinishedGoodMapper {
    FinishedGood toEntity(FinishedGoodDto dto);
    FinishedGoodDto toDto(FinishedGood entity);
}

