package com.crmservice.crmservice.infrastructure.drivens.repositorydtos;

import com.crmservice.crmservice.domain.entities.Document;
import com.crmservice.crmservice.domain.enums.DocumentType;

public record DocumentRepositoryDTO(DocumentType type, String number) {
    public static DocumentRepositoryDTO from(Document document) {
        return new DocumentRepositoryDTO(document.type(), document.number());
    }

    public Document toEntity() {
        return new Document(type, number);
    }
}
