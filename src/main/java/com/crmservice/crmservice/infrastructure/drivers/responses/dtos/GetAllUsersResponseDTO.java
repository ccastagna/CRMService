package com.crmservice.crmservice.infrastructure.drivers.responses.dtos;

import java.util.List;

public record GetAllUsersResponseDTO(List<GetAllUsersUserResponseDTO> users) {
}

