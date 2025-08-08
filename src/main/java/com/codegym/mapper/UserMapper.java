package com.codegym.mapper;

import com.codegym.dto.request.RegisterRequest;
import com.codegym.dto.response.UserDTO;
import com.codegym.entity.Role;
import com.codegym.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", source = "role")
    @Mapping(target = "avatarUrl", constant = "default.png")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "username", source = "request.username")
    User toEntity(RegisterRequest request, Role role);

    default UserDTO toResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .fullName(user.getFullName())
                .address(user.getAddress())
                .avatarUrl(user.getAvatarUrl())
                .active(user.isActive())
                .roleName(user.getRole() != null ? user.getRole().getName() : null)
                .build();
    }
}
