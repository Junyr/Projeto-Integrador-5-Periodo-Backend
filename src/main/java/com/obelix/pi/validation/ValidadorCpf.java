package com.obelix.pi.validation;

import java.util.regex.Pattern;

public class ValidadorCpf {
    private static final Pattern CPF_FORMAT = Pattern.compile(RegexPatterns.CPF_FORMAT);

    /**
     * Validates CPF format and digits.
     * @param cpf formatted CPF (e.g. "123.456.789-09")
     * @return true if valid
     */
    public static boolean isValid(String cpf) {
        if (cpf == null) return false;
        String s = cpf.trim();
        if (!CPF_FORMAT.matcher(s).matches()) return false;

        // remove non-digits
        String digits = s.replaceAll("\\D", "");
        if (digits.length() != 11) return false;

        // Reject same-digit CPFs (e.g. 00000000000)
        if (digits.chars().distinct().count() == 1) return false;

        try {
            int d1 = calcDigit(digits, 9, 10);
            int d2 = calcDigit(digits, 10, 11);
            int v1 = Character.getNumericValue(digits.charAt(9));
            int v2 = Character.getNumericValue(digits.charAt(10));
            return d1 == v1 && d2 == v2;
        } catch (Exception e) {
            return false;
        }
    }

    private static int calcDigit(String digits, int length, int weightStart) {
        int sum = 0;
        int weight = weightStart;
        for (int i = 0; i < length; i++) {
            sum += Character.getNumericValue(digits.charAt(i)) * weight--;
        }
        int mod = (sum * 10) % 11;
        return (mod == 10) ? 0 : mod;
    }
}

