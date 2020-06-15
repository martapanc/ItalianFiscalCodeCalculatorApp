package com.example.fiscalcode_java.fiscalcode.computations;

import com.example.fiscalcode_java.exception.FiscalCodeExtractionException;
import com.example.fiscalcode_java.fiscalcode.constants.DateFormat;
import com.example.fiscalcode_java.fiscalcode.models.Gender;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ExtractDataFromFiscalCode {

    private final static String DOB_PATTERN = "[\\d]{2}[A-EHLMPRST][\\d]{2}";
    private final static String DAY_PATTERN = "[\\d]{1,2}";

    public static Gender extractGender(String dayInput) throws FiscalCodeExtractionException {
        if (Pattern.matches(DAY_PATTERN, dayInput)) {
            int day = Integer.parseInt(dayInput);
            if (day >= 1 && day <= 31) {
                return Gender.M;
            } else if (day >= 41 && day <= 71) {
                return Gender.F;
            } else {
                throw new FiscalCodeExtractionException("Day out of range for gender");
            }
        } else {
            throw new FiscalCodeExtractionException("Error parsing gender");
        }
    }

    public static String extractDateOfBirth(String dobInput, Gender gender) throws FiscalCodeExtractionException {
        if (Pattern.matches(DOB_PATTERN, dobInput)) {
            String yearInput = dobInput.substring(0, 2);
            String monthInput = dobInput.substring(2, 3);
            String dayInput = dobInput.substring(3, 5);

            Map<String, Integer> monthCodeInverseMap = DateFormat.getMonthCodeMap().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
            final Integer month = monthCodeInverseMap.get(monthInput);

            if (!validateDayBasedOnGender(dayInput, gender)) {
                throw new FiscalCodeExtractionException("Day of birth is out of range: " + dayInput + " for " + gender);
            }
            int day = Integer.parseInt(dayInput) - (gender.equals(Gender.F) ? 40 : 0);

            return prependZero(day) + "/" + prependZero(month) + "/" + yearInput;
        } else {
            throw new FiscalCodeExtractionException("Date of birth input has a wrong format");
        }
    }

    public static String extractPlaceOfBirth(String pobInput, String[] places) {
        return "";
    }

    private static String prependZero(Integer value) {
        return (value < 10 ? "0" : "") + value;
    }

    private static boolean validateDayBasedOnGender(String dayString, Gender gender) {
        int day = Integer.parseInt(dayString);
        if (gender.equals(Gender.F)) {
            return day >= 41 && day <= 71;
        } else {
            return day >= 1 && day <= 31;
        }
    }
}
