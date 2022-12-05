package com.crmservice.crmservice.infrastructure.drivers.requesthandlers;

import java.util.Optional;

public abstract class InputValueEvaluator {

    private InputValueEvaluator() {
    }

    private static final String VALID_NAME_REGEX = "[a-zA-Z0-9\s]{3,30}";
    private static final String VALID_SURNAME_REGEX = VALID_NAME_REGEX;
    private static final String VALID_DOCUMENT_NUMBER_REGEX = "[a-zA-Z0-9\s]{6,20}";

    public static void evaluateCustomerName(String name) throws IllegalArgumentException {
        Optional.ofNullable(name)
                .map(notNullName -> notNullName.matches(VALID_NAME_REGEX))
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer name: " + name));
    }

    public static void evaluateCustomerSurname(String surname) throws IllegalArgumentException {
        Optional.ofNullable(surname)
                .map(notNullName -> notNullName.matches(VALID_SURNAME_REGEX))
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer surname: " + surname));
    }

    public static void evaluateCustomerDocumentNumber(String number) throws IllegalArgumentException {
        Optional.ofNullable(number)
                .map(notNullName -> notNullName.matches(VALID_DOCUMENT_NUMBER_REGEX))
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer document number: " + number));
    }
}
