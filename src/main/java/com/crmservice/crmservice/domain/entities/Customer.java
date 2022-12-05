package com.crmservice.crmservice.domain.entities;

import com.crmservice.crmservice.domain.enums.CustomerState;

import java.time.Instant;

public class Customer {
    private final String id;
    private final String name;
    private final String surname;
    private final Document document;
    private final Instant createdAt;
    private final String createdBy;
    private final Instant lastUpdatedAt;
    private final String lastUpdatedBy;
    private final String photoUrl;
    private final CustomerState state;

    public Customer(String id, String name, String surname, Document document, Instant createdAt, String createdBy, Instant lastUpdatedAt, String lastUpdatedBy, String photoUrl, CustomerState state) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.document = document;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.lastUpdatedAt = lastUpdatedAt;
        this.lastUpdatedBy = lastUpdatedBy;
        this.photoUrl = photoUrl;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Document getDocument() {
        return document;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Instant getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public CustomerState getState() {
        return state;
    }
}
