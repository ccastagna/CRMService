package com.crmservice.crmservice.domain.entities;

import com.crmservice.crmservice.domain.enums.DocumentType;

public record Document(DocumentType type, String number) {

}
