package com.crmservice.crmservice.infrastructure.drivers.responses.dtos;

import com.crmservice.crmservice.domain.entities.User;

import java.util.List;

public record GetAllUsersResponseDTO(List<GetAllUsersUserResponseDTO> users) {
    public static GetAllUsersResponseDTO from(List<User> getAllUsersResponse) {
        List<GetAllUsersUserResponseDTO> users = getAllUsersResponse
                .stream()
                .map(user -> GetAllUsersUserResponseDTO.from(user))
                .toList();
        return new GetAllUsersResponseDTO(users);
    }
}

