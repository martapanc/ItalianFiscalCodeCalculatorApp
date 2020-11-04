package com.pancaldim.fiscalcode_app.fiscalcode.computations;

import com.pancaldim.fiscalcode_app.fiscalcode.constants.DateFormatAndLocaleConstants;
import com.pancaldim.fiscalcode_app.fiscalcode.models.InputField;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.pancaldim.fiscalcode_app.fiscalcode.computations.FunctionCheckHelper.isAllLetters;
import static com.pancaldim.fiscalcode_app.fiscalcode.computations.FunctionCheckHelper.isDateValid;
import static com.pancaldim.fiscalcode_app.fiscalcode.computations.FunctionCheckHelper.isFiscalCodeValid;
import static com.pancaldim.fiscalcode_app.fiscalcode.computations.FunctionCheckHelper.replaceSpecialChars;

public class ValidateInputFields {

    public static boolean isFieldValid(String inputField, InputField type, String[] places) {
        switch (type) {
            case FIRST_NAME:
            case LAST_NAME:
                return validateName(inputField);
            case DATE_OF_BIRTH:
                return validateDateOfBirth(inputField);
            case PLACE_OF_BIRTH:
                return validatePlaceOfBirth(inputField, places);
            case FISCAL_CODE:
                return validateFiscalCode(inputField);
            default:
                return false;
        }
    }

    private static boolean validateFiscalCode(String inputFiscalCode) {
        return isFiscalCodeValid(inputFiscalCode);
    }

    private static boolean validateName(String inputName) {
        inputName = replaceSpecialChars(inputName).trim();
        return isAllLetters(inputName);
    }

    private static boolean validateDateOfBirth(String inputDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormatAndLocaleConstants.DD_MM_YYYY, Locale.ITALY);
        try {
            Date date = sdf.parse(inputDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(Objects.requireNonNull(date));
            return isDateValid(cal);
        } catch (ParseException e) {
            return false;
        }
    }

    private static boolean validatePlaceOfBirth(String inputPlace, String[] places) {
        for (String place : places) {
            if (place.equals(inputPlace)) {
                return true;
            }
        }
        return false;
    }
}
