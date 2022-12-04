package com.crmservice.crmservice.infrastructure.drivers.requests.dtos;

import com.crmservice.crmservice.domain.entities.Document;
import com.crmservice.crmservice.domain.enums.DocumentType;

public record DocumentRequestDTO(DocumentType type, String number) {
    public Document ToEntity() {
        return new Document(type, number);
    }
}
