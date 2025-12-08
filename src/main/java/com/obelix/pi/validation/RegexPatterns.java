package com.obelix.pi.validation;


/**
 * Central place for regular expression patterns used by validators.
 */
public final class RegexPatterns {
    private RegexPatterns() {}


    public static final String PLACA_MERCOSUL = "^[A-Z]{3}[0-9][A-Z][0-9]{2}$";

    // CPF formato: 000.000.000-00
    public static final String CPF_FORMAT = "^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$";

    // CNPJ formato: 00.000.000/0000-00
    public static final String CNPJ_FORMAT = "^\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2}$";
}