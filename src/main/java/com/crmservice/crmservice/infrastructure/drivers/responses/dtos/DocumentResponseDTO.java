package com.crmservice.crmservice.infrastructure.drivers.responses.dtos;

import com.crmservice.crmservice.domain.entities.Document;
import com.crmservice.crmservice.domain.enums.DocumentType;

public record DocumentResponseDTO(DocumentType type, String number) {
    public static DocumentResponseDTO from(Document document) {
        return new DocumentResponseDTO(document.type(), document.number());
    }
}
