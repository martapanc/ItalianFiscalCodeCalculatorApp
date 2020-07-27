package com.example.fiscalcode_java.fiscalcode.computations;

import com.example.fiscalcode_java.exception.FiscalCodeComputationException;
import com.example.fiscalcode_java.fiscalcode.models.Country;
import com.example.fiscalcode_java.fiscalcode.models.Town;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.fiscalcode_java.fiscalcode.computations.FunctionCheckHelper.CONSONANTS;
import static com.example.fiscalcode_java.fiscalcode.computations.FunctionCheckHelper.howManyLettersOfType;
import static com.example.fiscalcode_java.fiscalcode.computations.FunctionCheckHelper.isAllLetters;
import static com.example.fiscalcode_java.fiscalcode.computations.FunctionCheckHelper.isDateValid;
import static com.example.fiscalcode_java.fiscalcode.computations.FunctionCheckHelper.isTwoLettersAndVowelBeforeConsonant;
import static com.example.fiscalcode_java.fiscalcode.computations.FunctionCheckHelper.isYearValid;
import static com.example.fiscalcode_java.fiscalcode.computations.FunctionCheckHelper.replaceSpecialChars;
import static com.example.fiscalcode_java.fiscalcode.computations.FunctionCheckHelper.swapIfVowelBeforeConsonant;
import static com.example.fiscalcode_java.fiscalcode.computations.NameAndSurnameComputations.pickFirstAndThirdAndFourthConsonant;
import static com.example.fiscalcode_java.fiscalcode.computations.NameAndSurnameComputations.pickFirstConsonantAndFirstTwoVowels;
import static com.example.fiscalcode_java.fiscalcode.computations.NameAndSurnameComputations.pickFirstThreeConsonants;
import static com.example.fiscalcode_java.fiscalcode.computations.NameAndSurnameComputations.pickFirstThreeVowels;
import static com.example.fiscalcode_java.fiscalcode.computations.NameAndSurnameComputations.pickFirstTwoConsonantsAndFirstVowel;
import static com.example.fiscalcode_java.fiscalcode.constants.DateFormatConstants.getMonthCodeMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;


public class ComputeFiscalCodeHelper {

    public static String computeLastName(String input) throws FiscalCodeComputationException {
        input = replaceSpecialChars(input.trim());

        if (isAllLetters(input)) {
            StringBuilder result = new StringBuilder();
            input = input.toUpperCase();

            if (input.length() < 3) {
                if (isTwoLettersAndVowelBeforeConsonant(input)) {
                    result = swapIfVowelBeforeConsonant(input);
                } else {
                    result = new StringBuilder(input);
                    while (result.length() < 3) {
                        result.append("X");
                    }
                }
            } else {
                switch (howManyLettersOfType(input, CONSONANTS)) {
                    case 0:
                        result.append(pickFirstThreeVowels(input));
                        break;
                    case 1:
                        result.append(pickFirstConsonantAndFirstTwoVowels(input));
                        break;
                    case 2:
                        result.append(pickFirstTwoConsonantsAndFirstVowel(input));
                        break;
                    default:
                        result.append(pickFirstThreeConsonants(input));
                }
            }
            return result.toString();
        } else {
            throw new FiscalCodeComputationException("err_last_name");
        }
    }

    public static String computeFirstName(String inputName) throws FiscalCodeComputationException {
        inputName = replaceSpecialChars(inputName.trim());
        if (isAllLetters(inputName)) {
            StringBuilder result = new StringBuilder();
            inputName = inputName.toUpperCase();

            if (inputName.length() < 3) {
                if (isTwoLettersAndVowelBeforeConsonant(inputName)) {
                    result = swapIfVowelBeforeConsonant(inputName);
                } else {
                    result = new StringBuilder(inputName);
                    while (result.length() < 3) {
                        result.append("X");
                    }
                }
            } else {
                switch (howManyLettersOfType(inputName, CONSONANTS)) {
                    case 0:
                        result.append(pickFirstThreeVowels(inputName));
                        break;
                    case 1:
                        result.append(pickFirstConsonantAndFirstTwoVowels(inputName));
                        break;
                    case 2:
                        result.append(pickFirstTwoConsonantsAndFirstVowel(inputName));
                        break;
                    case 3:
                        result.append(pickFirstThreeConsonants(inputName));
                        break;
                    default:
                        result.append(pickFirstAndThirdAndFourthConsonant(inputName));
                }
            }
            return result.toString();

        } else {
            throw new FiscalCodeComputationException("err_first_name");
        }
    }

    public static String computeDateOfBirth(String dateString, String gender) throws FiscalCodeComputationException {
        //TODO: min year should be a static variable for the whole app
        String[] dateValues = dateString.split("/");
        String dayString = dateValues[0];
        String monthString = dateValues[1];
        String yearString = dateValues[2];

        if (isYearValid(yearString)) {
            if (isDateValid(dayString, monthString, yearString)) {
                String result = EMPTY;
                try {
                    int day = Integer.parseInt(dayString);
                    int month = Integer.parseInt(monthString);
                    int year = Integer.parseInt(yearString);

                    // get the last 2 digits of the year
                    if (year % 100 >= 10) {
                        result += (year % 100);
                    } else {
                        result = result + 0 + (year % 100);
                    }

                    // get the letter corresponding to the month
                    result += getMonthCodeMap().get(month);

                    if ("f".equals(gender)) {
                        result += (day + 40);
                    } else if ("m".equals(gender)) {
                        result += (day <= 10 ? "0" + day : day);
                    }
                    return result;
                } catch (NumberFormatException e) {
                    throw new FiscalCodeComputationException("err_date_parse");
                }
            } else {
                throw new FiscalCodeComputationException("err_date_invalid");
            }
        } else {
            throw new FiscalCodeComputationException("err_date_range");
        }
    }

    public static String getPlaceCode(List<Town> towns, List<Country> countries, String selectedPlace) {
        String placeCode = EMPTY;
        String placeWithoutAreaCode = selectedPlace.substring(0, selectedPlace.length() - 5);
        for (Town town : towns) {
            if (placeWithoutAreaCode.equals(town.getName())) {
                placeCode = town.getCadastral_code();
                break;
            }
        }
        if (EMPTY.equals(placeCode)) {
            for (Country country : countries) {
                if (placeWithoutAreaCode.equals(country.getName())) {
                    placeCode = country.getCadastral_code();
                    break;
                }
            }
        }
        return placeCode;
    }

    public static String computeControlChar(String incompleteFiscalCode) throws InterruptedException, FiscalCodeComputationException {
        Map<Integer, String> controlCharMap = new HashMap<>();
        controlCharMap.put(0, "A");
        controlCharMap.put(1, "B");
        controlCharMap.put(2, "C");
        controlCharMap.put(3, "D");
        controlCharMap.put(4, "E");
        controlCharMap.put(5, "F");
        controlCharMap.put(6, "G");
        controlCharMap.put(7, "H");
        controlCharMap.put(8, "I");
        controlCharMap.put(9, "J");
        controlCharMap.put(10, "K");
        controlCharMap.put(11, "L");
        controlCharMap.put(12, "M");
        controlCharMap.put(13, "N");
        controlCharMap.put(14, "O");
        controlCharMap.put(15, "P");
        controlCharMap.put(16, "Q");
        controlCharMap.put(17, "R");
        controlCharMap.put(18, "S");
        controlCharMap.put(19, "T");
        controlCharMap.put(20, "U");
        controlCharMap.put(21, "V");
        controlCharMap.put(22, "W");
        controlCharMap.put(23, "X");
        controlCharMap.put(24, "Y");
        controlCharMap.put(25, "Z");

        int evenSum = 0, oddSum = 0;
        incompleteFiscalCode = incompleteFiscalCode.toUpperCase();
        if (incompleteFiscalCode.length() == 15) {
            OddThread ot = new OddThread(incompleteFiscalCode, oddSum);
            Thread t1 = new Thread(ot);
            t1.start();
            t1.join();
            oddSum = ot.getOddSum();

            EvenThread et = new EvenThread(incompleteFiscalCode, evenSum);
            Thread t2 = new Thread(et);
            t2.start();
            t2.join();
            evenSum = et.getEvenSum();

            // The remainder of the division is the control character
            int sum = (oddSum + evenSum) % 26;
            return controlCharMap.get(sum);
        } else {
            throw new FiscalCodeComputationException("err_fc_char");
        }
    }

    public static String compute(String firstName, String lastName, String dateOfBirth, String gender, String townOfBirth, List<Town> towns, List<Country> countries) throws InterruptedException, FiscalCodeComputationException {
        StringBuilder fiscalCode = new StringBuilder();
        fiscalCode.append(computeLastName(lastName));
        fiscalCode.append(computeFirstName(firstName));
        fiscalCode.append(computeDateOfBirth(dateOfBirth, gender));
        fiscalCode.append(getPlaceCode(towns, countries, townOfBirth));
        fiscalCode.append(computeControlChar(fiscalCode.toString()));
        return fiscalCode.toString();
    }
}
