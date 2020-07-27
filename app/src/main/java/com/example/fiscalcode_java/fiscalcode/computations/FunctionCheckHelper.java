package com.example.fiscalcode_java.fiscalcode.computations;

import com.example.fiscalcode_java.fiscalcode.constants.DateFormatConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class FunctionCheckHelper {

    public static final String CONSONANTS = "[B-DF-HJ-NP-TV-Z]+";
    public static final String VOWELS = "[AEIOU]+";
    public static final String ALPHABET = "[a-zA-Z]+";
    public static final String NUMBERS = "[0-9]+";
    public static final String FISCAL_CODE = "[A-Z]{6}\\d{2}[A-EHLMPRST]\\d{2}[A-Z]\\d{3}[A-Z]";

    public static boolean isAllLetters(String string) {
        return Pattern.matches(ALPHABET, string.toUpperCase());
    }

    public static boolean isAllDigits(String string) {
        return Pattern.matches(NUMBERS, string);
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
            String dateToCheck = day + "/" + month + "/" + year;
            Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormatConstants.DD_MM_YYYY, Locale.ITALY);
            sdf.setLenient(false);
            try {
                sdf.parse(dateToCheck);
                Date date = sdf.parse(dateToCheck);
                Date current = Calendar.getInstance().getTime();
                // You cannot calculate a fiscal code if the birthday is after the current day
                return !current.before(date);
            } catch (ParseException e) {
                return false;
            }
        }
        return false;
    }

    public static boolean isDateValid(Calendar calendar) {
        return isDateValid(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)),
                Integer.toString(calendar.get(Calendar.MONTH) + 1),
                Integer.toString(calendar.get(Calendar.YEAR)));
    }

    public static boolean isFiscalCodeValid(String fiscalCode) {
        return Pattern.matches(FISCAL_CODE, fiscalCode.toUpperCase());
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
                .replaceAll("[ÑŃ]", "N")
                .replaceAll("[ÇĆČ]", "C")
                .replaceAll("[ŽŹŻ]", "C")
                .replaceAll("ß", "SS")
                .replaceAll("Ł", "L")
                .replaceAll("Ÿ", "Y")
                .replaceAll(" ", EMPTY)
                .replaceAll("-", EMPTY)
                .replaceAll("[.']", EMPTY);
    }

    public static boolean isTwoLettersAndVowelBeforeConsonant(String input) {
        if (input.length() != 2) {
            return false;
        }
        return Pattern.matches(VOWELS, String.valueOf(input.charAt(0)))
                && Pattern.matches(CONSONANTS, String.valueOf(input.charAt(1)));
    }

    // Consonants are added before vowels. E.g. Ed -> DEX
    public static StringBuilder swapIfVowelBeforeConsonant(String input) {
        StringBuilder result = new StringBuilder();
        result.append(input.charAt(1));
        result.append(input.charAt(0));
        result.append("X");
        return result;
    }
}
