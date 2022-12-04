package com.crmservice.crmservice.infrastructure.drivers.responses.dtos;

import com.crmservice.crmservice.domain.enums.DocumentType;

public record DocumentResponseDTO(DocumentType type, String number) {
}
