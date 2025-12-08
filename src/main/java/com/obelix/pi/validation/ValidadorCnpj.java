package com.obelix.pi.validation;

import java.util.regex.Pattern;

public class ValidadorCnpj {
    private static final Pattern CNPJ_FORMAT = Pattern.compile(RegexPatterns.CNPJ_FORMAT);

    public static boolean isValid(String cnpj) {
        if (cnpj == null) return false;
        String s = cnpj.trim();
        if (!CNPJ_FORMAT.matcher(s).matches()) return false;

        String digits = s.replaceAll("\\D", "");
        if (digits.length() != 14) return false;

        // Reject same-digit CNPJs
        if (digits.chars().distinct().count() == 1) return false;

        try {
            int d1 = calcDigit(digits, 12, new int[]{5,4,3,2,9,8,7,6,5,4,3,2});
            int d2 = calcDigit(digits, 13, new int[]{6,5,4,3,2,9,8,7,6,5,4,3,2});
            int v1 = Character.getNumericValue(digits.charAt(12));
            int v2 = Character.getNumericValue(digits.charAt(13));
            return d1 == v1 && d2 == v2;
        } catch (Exception e) {
            return false;
        }
    }

    private static int calcDigit(String digits, int length, int[] weights) {
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += Character.getNumericValue(digits.charAt(i)) * weights[i];
        }
        int mod = sum % 11;
        return (mod < 2) ? 0 : (11 - mod);
    }
}
