package com.codegym.mapper;

import com.codegym.dto.request.RegisterRequest;
import com.codegym.dto.response.UserResponse;
import com.codegym.entity.Role;
import com.codegym.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) // Không encode password ở đây
    @Mapping(target = "role", source = "role")
    @Mapping(target = "avatarUrl", constant = "default.png")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "username", source = "request.username")
    User toEntity(RegisterRequest request, Role role);

    default UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .phone(user.getPhone())
                .role(Long.parseLong(user.getRole().getName()))
                .build();
    }
}
