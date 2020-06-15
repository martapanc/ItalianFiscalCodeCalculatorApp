package com.example.fiscalcode_java.fiscalcode.computations;

import com.example.fiscalcode_java.fiscalcode.constants.DateFormat;
import com.example.fiscalcode_java.fiscalcode.models.InputField;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.fiscalcode_java.fiscalcode.computations.FunctionCheckHelper.isAllLetters;
import static com.example.fiscalcode_java.fiscalcode.computations.FunctionCheckHelper.isDateValid;
import static com.example.fiscalcode_java.fiscalcode.computations.FunctionCheckHelper.isFiscalCodeValid;
import static com.example.fiscalcode_java.fiscalcode.computations.FunctionCheckHelper.replaceSpecialChars;

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
            case FISCAL_CODE:
                return validateFiscalCode(input);
        }
        return false;
    }

    private static boolean validateFiscalCode(String input) {
        return isFiscalCodeValid(input);
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
