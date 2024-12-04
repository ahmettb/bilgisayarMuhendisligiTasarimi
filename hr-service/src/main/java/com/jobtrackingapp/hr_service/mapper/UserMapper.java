package com.jobtrackingapp.hr_service.mapper;

import com.jobtrackingapp.hr_service.dto.UserDTO;
import com.jobtrackingapp.hr_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "role.role", target = "role")
    UserDTO toDTO(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDTO userDTO);

    void updateEntity(UserDTO userDTO, @MappingTarget User user);
}
