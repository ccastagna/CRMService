package com.crmservice.crmservice.domain.services.credentialsvalidators;

class CredentialConstants {

    static final String USERNAME_WITH_254_NON_SPECIAL_CHARACTERS = "Secure password01 Secure password01 Secure password01" +
            "Secure password01 Secure password01 Secure password01 Secure password01 Secure password01 Secure password01" +
            "Secure password01 Secure password01 Secure password01 Secure password01 Secure password01 Secu";

    static final String USERNAME_WITH_SPECIAL_CHARACTERS = "Secure@password";

    static final String NAME_WITH_255_NON_SPECIAL_CHARACTERS = "Secure password01 Secure password01 Secure password01" +
            "Secure password01 Secure password01 Secure password01 Secure password01 Secure password01 Secure password" +
            "01 Secure password01 Secure password01 Secure password01 Secure password01 Secure password01 Secu";

    static final String STRONG_PASSWORD = "@SecUre.P4$5";

    static final String PASSWORD_WITH_11_CHARACTERS = "SecUre.P4$5";

    static final String PASSWORD_WITHOUT_UPPERCASE_LETTERS = "extr3m3!y@secure.p4$5w0rd";

    static final String PASSWORD_WITHOUT_LOWERCASE_LETTERS = "EXTR3M3!Y@SECURE.P4$5W0RD";

    static final String PASSWORD_WITHOUT_SPECIAL_CHARACTERS = "ExTr3m3lySecUreP4s5w0rd";

    static final String PASSWORD_WITHOUT_NUMBERS = "ExTreme!y@SecUre.Pa$sword";

}
