package com.obelix.pi.validation;

import java.util.regex.Pattern;

/**
 * Validator for vehicle plates (Mercosul formato).
 */
public class ValidadorPlacaVeiculo {

    private static final Pattern PLACA = Pattern.compile(RegexPatterns.PLACA_MERCOSUL);


    public static boolean isValid(String plate) {
        if (plate == null) return false;
        return PLACA.matcher(plate.trim().toUpperCase()).matches();
    }
}