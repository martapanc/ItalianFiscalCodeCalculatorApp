package com.example.fiscalcode_java.fiscalcode.computations;

import com.example.fiscalcode_java.exception.FiscalCodeExtractionException;
import com.example.fiscalcode_java.fiscalcode.constants.DateFormat;
import com.example.fiscalcode_java.fiscalcode.models.Country;
import com.example.fiscalcode_java.fiscalcode.models.FiscalCodeData;
import com.example.fiscalcode_java.fiscalcode.models.Gender;
import com.example.fiscalcode_java.fiscalcode.models.Town;

import java.util.List;
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
                throw new FiscalCodeExtractionException("err_gender_day");
            }
        } else {
            throw new FiscalCodeExtractionException("err_gender_parse");
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
            throw new FiscalCodeExtractionException("err_dob_format");
        }
    }

    public static String extractPlaceOfBirth(String pobInput, List<Town> townList, List<Country> countryList) throws FiscalCodeExtractionException {
        for (Town town : townList) {
            if (pobInput.equals(town.getCadastral_code())) {
                return town.getName() + " (" + town.getProvince() + ")";
            }
        }
        for (Country country : countryList) {
            if (pobInput.equals(country.getCadastral_code())) {
                return country.getName();
            }
        }
        throw new FiscalCodeExtractionException("err_pob_not_found");
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

    public static FiscalCodeData extractData(String inputFiscalCode, List<Town> townList, List<Country> countryList) throws FiscalCodeExtractionException {
        inputFiscalCode = inputFiscalCode.toUpperCase();
        String inputLastName = inputFiscalCode.substring(0, 3);
        String inputFirstName = inputFiscalCode.substring(3, 6);
        String inputGender = inputFiscalCode.substring(9, 11);
        String inputDateOfBirth = inputFiscalCode.substring(6, 11);
        String inputPlaceOfBirth = inputFiscalCode.substring(11, 15);
        final Gender gender = extractGender(inputGender);

        return FiscalCodeData.builder()
                .firstNameCode(inputFirstName)
                .lastNameCode(inputLastName)
                .gender(gender)
                .dateOfBirth(extractDateOfBirth(inputDateOfBirth, gender))
                .placeOfBirth(extractPlaceOfBirth(inputPlaceOfBirth, townList, countryList))
                .build();
    }
}
