package com.crmservice.crmservice.infrastructure.drivers.requesthandlers;

import java.util.Optional;

public abstract class InputValueEvaluator {

    private InputValueEvaluator() {
    }

    private static final String VALID_NAME_REGEX = "[a-zA-Z0-9\s]{3,30}";
    private static final String VALID_SURNAME_REGEX = VALID_NAME_REGEX;
    private static final String VALID_DOCUMENT_NUMBER_REGEX = "[a-zA-Z0-9\s]{6,20}";
    private static final String VALID_CUSTOMER_ID_REGEX = "[A-Z]{2,8}:" + VALID_DOCUMENT_NUMBER_REGEX;

    public static boolean isValidCustomerName(String name) {
        return Optional.ofNullable(name)
                .map(notNullName -> notNullName.matches(VALID_NAME_REGEX))
                .orElse(false);
    }

    public static boolean isValidCustomerSurname(String surname) {
        return Optional.ofNullable(surname)
                .map(notNullName -> notNullName.matches(VALID_SURNAME_REGEX))
                .orElse(false);
    }

    public static boolean isValidCustomerDocumentNumber(String number) {
        return Optional.ofNullable(number)
                .map(notNullName -> notNullName.matches(VALID_DOCUMENT_NUMBER_REGEX))
                .orElse(false);
    }

    public static boolean isValidCustomerId(String customerId) {
        return Optional.ofNullable(customerId)
                .map(notNullName -> notNullName.matches(VALID_CUSTOMER_ID_REGEX))
                .orElse(false);
    }
}
