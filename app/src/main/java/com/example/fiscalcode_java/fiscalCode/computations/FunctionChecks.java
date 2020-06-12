package com.example.fiscalcode_java.fiscalCode.computations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionChecks {

    public static final String CONSONANTS = "[B-DF-HJ-NP-TV-Z]+";
    public static final String VOWELS = "[AEIOU]+";

    public static boolean isAllLetters(String string) {
        return Pattern.matches("[a-zA-Z]+", string);
    }

    public static boolean isAllDigits(String string) {
        return Pattern.matches("[0-9]+", string);
    }

    public static int howManyLettersOfType(String inputString, String letterType) {
        StringBuilder match = new StringBuilder();
        if (isAllLetters(inputString)) {
            inputString = inputString.toUpperCase();
            Pattern pattern = Pattern.compile(letterType);
            Matcher matcher = pattern.matcher(inputString);
            while (matcher.find()) {
                match.append(matcher.group());
            }
        }
        return match.length();
    }

    public static boolean isYearValid(String yearString) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int year;
        if (isAllDigits(yearString)) {
            year = Integer.parseInt(yearString);
            return (year >= 1900 && year <= currentYear);
        }
        return false;
    }

    public static boolean isDateValid(String day, String month, String year) {
        // Check whether a date is valid or not (e.g. 29/02/2001)
        if (isYearValid(year)) {
            String dateToCheck = day + "-" + month + "-" + year;
            Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy", Locale.ITALY);
            sdf.setLenient(false);
            try {
                sdf.parse(dateToCheck);
                Date date = sdf.parse(dateToCheck);
                Date current = Calendar.getInstance().getTime();
                if (current.before(date)) // You cannot calculate a fiscal code if the birthday is after the current day
                    return false;
            } catch (ParseException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static String replaceSpecialChars(String input) {
        //If the name or surname include stressed letters (à,è,ì...) or other special characters (ä,ç,ß,...),
        // this replaces them with corresponding letter (e.g. à = a)
        return input
                .toUpperCase()
                .replaceAll("[ÀÁÂÃÅĀ]", "A")
                .replaceAll("[ÄÆ]", "AE")
                .replaceAll("[ÈÉÊËĘĖĒ]", "E")
                .replaceAll("[ÌÍÎÏĮĪ]", "I")
                .replaceAll("[ÒÓÔÕOŌ]", "O")
                .replaceAll("[ÖŒØ]", "OE")
                .replaceAll("[ÙÚÛŪ]", "U")
                .replaceAll("[Ü]", "UE")
                .replaceAll("[ŚŠ]", "S")
                .replaceAll("ß", "SS")
                .replaceAll("[ÇĆČ]", "C")
                .replaceAll(" ", "")
                .replaceAll("-", "")
                .replaceAll("'", "");
    }
}
