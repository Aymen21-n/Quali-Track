package com.qualitrack.utilisateurs.mapper;

import com.qualitrack.utilisateurs.dto.UserDto;
import com.qualitrack.utilisateurs.entite.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User toEntity(UserDto dto);
    UserDto toDto(User entity);
}
