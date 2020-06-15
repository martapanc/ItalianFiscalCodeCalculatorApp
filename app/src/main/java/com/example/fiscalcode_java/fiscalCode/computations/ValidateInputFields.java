package com.example.fiscalcode_java.fiscalCode.computations;

import com.example.fiscalcode_java.fiscalCode.constants.DateFormat;
import com.example.fiscalcode_java.fiscalCode.models.InputField;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.fiscalcode_java.fiscalCode.computations.FunctionChecks.isAllLetters;
import static com.example.fiscalcode_java.fiscalCode.computations.FunctionChecks.isDateValid;
import static com.example.fiscalcode_java.fiscalCode.computations.FunctionChecks.replaceSpecialChars;

public class ValidateInputFields {

    public static boolean isFieldValid(String input, InputField type, String[] places) {
        switch (type) {
            case FIRST_NAME:
            case LAST_NAME:
                return validateName(input);
            case DATE_OF_BIRTH:
                return validateDateOfBirth(input);
            case PLACE_OF_BIRTH:
                return validatePlaceOfBirth(input, places);
        }
        return false;
    }

    private static boolean validateName(String input) {
        input = replaceSpecialChars(input).trim();
        return isAllLetters(input);
    }

    private static boolean validateDateOfBirth(String input) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DD_MM_YYYY, Locale.ITALY);
        try {
            Date date = sdf.parse(input);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return isDateValid(cal);
        } catch (ParseException e) {
            return false;
        }
    }

    private static boolean validatePlaceOfBirth(String input, String[] places) {
        for (String place : places) {
            if (place.equals(input)) {
                return true;
            }
        }
        return false;
    }
}
