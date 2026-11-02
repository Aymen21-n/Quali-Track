package com.qualitrack.atelier.mapper;

import com.qualitrack.atelier.dto.MachineDto;
import com.qualitrack.atelier.entite.Machine;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MachineMapper {
    Machine toEntity(MachineDto dto);
    MachineDto toDto(Machine entity);
}
