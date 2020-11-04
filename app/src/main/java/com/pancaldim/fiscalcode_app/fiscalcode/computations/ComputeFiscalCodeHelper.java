package com.pancaldim.fiscalcode_app.fiscalcode.computations;

import com.pancaldim.fiscalcode_app.exception.FiscalCodeComputationException;
import com.pancaldim.fiscalcode_app.fiscalcode.models.Country;
import com.pancaldim.fiscalcode_app.fiscalcode.models.Gender;
import com.pancaldim.fiscalcode_app.fiscalcode.models.Town;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pancaldim.fiscalcode_app.fiscalcode.computations.FunctionCheckHelper.CONSONANTS;
import static com.pancaldim.fiscalcode_app.fiscalcode.computations.FunctionCheckHelper.howManyLettersOfType;
import static com.pancaldim.fiscalcode_app.fiscalcode.computations.FunctionCheckHelper.isAllLetters;
import static com.pancaldim.fiscalcode_app.fiscalcode.computations.FunctionCheckHelper.isDateValid;
import static com.pancaldim.fiscalcode_app.fiscalcode.computations.FunctionCheckHelper.isTwoLettersAndVowelBeforeConsonant;
import static com.pancaldim.fiscalcode_app.fiscalcode.computations.FunctionCheckHelper.isYearValid;
import static com.pancaldim.fiscalcode_app.fiscalcode.computations.FunctionCheckHelper.replaceSpecialChars;
import static com.pancaldim.fiscalcode_app.fiscalcode.computations.FunctionCheckHelper.swapIfVowelBeforeConsonant;
import static com.pancaldim.fiscalcode_app.fiscalcode.computations.NameAndSurnameComputations.pickFirstAndThirdAndFourthConsonant;
import static com.pancaldim.fiscalcode_app.fiscalcode.computations.NameAndSurnameComputations.pickFirstConsonantAndFirstTwoVowels;
import static com.pancaldim.fiscalcode_app.fiscalcode.computations.NameAndSurnameComputations.pickFirstThreeConsonants;
import static com.pancaldim.fiscalcode_app.fiscalcode.computations.NameAndSurnameComputations.pickFirstThreeVowels;
import static com.pancaldim.fiscalcode_app.fiscalcode.computations.NameAndSurnameComputations.pickFirstTwoConsonantsAndFirstVowel;
import static com.pancaldim.fiscalcode_app.fiscalcode.constants.DateFormatAndLocaleConstants.getMonthCodeMap;
import static com.pancaldim.fiscalcode_app.fiscalcode.constants.FiscalCodeConstants.getControlCharMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;


public class ComputeFiscalCodeHelper {

    public static String computeLastName(String inputName) throws FiscalCodeComputationException {
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
                    default:
                        result.append(pickFirstThreeConsonants(inputName));
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

    //TODO: min year should be a static variable for the whole app
    public static String computeDateOfBirth(String dateString, String genderString) throws FiscalCodeComputationException {
        String[] dateValues = dateString.split("/");
        final String dayString = dateValues[0];
        final String monthString = dateValues[1];
        final String yearString = dateValues[2];
        final Gender gender = Gender.getGender(genderString);

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


                    if (Gender.F.equals(gender)) {
                        result += (day + 40);
                    } else if (Gender.M.equals(gender)) {
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
        int evenSum = 0;
        int oddSum = 0;
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
            return getControlCharMap().get(sum);
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
