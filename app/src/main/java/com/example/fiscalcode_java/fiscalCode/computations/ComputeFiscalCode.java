package com.example.fiscalcode_java.fiscalCode.computations;

import com.example.fiscalcode_java.fiscalCode.models.Country;
import com.example.fiscalcode_java.fiscalCode.models.Town;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.fiscalcode_java.fiscalCode.computations.FunctionChecks.*;
import static com.example.fiscalcode_java.fiscalCode.computations.NameAndSurnameComputations.*;

public class ComputeFiscalCode {

    //TODO: add unit tests

    public static String computeSurname(String input) {
        String error = "0";
        input = replaceSpecialChars(input);

        if (isAllLetters(input)) {
            StringBuilder result = new StringBuilder();
            input = input.toUpperCase();

            if (input.length() < 3) {
                result = new StringBuilder(input);
                while (result.length() < 3) {
                    result.append("X");
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
            return error;
            // TODO: exception handling
        }
    }

    public static String computeName(String inputName) {
        String error = "0";
        inputName = replaceSpecialChars(inputName);
        if (isAllLetters(inputName)) {
            StringBuilder result = new StringBuilder();
            inputName = inputName.toUpperCase();

            if (inputName.length() < 3) {
                result = new StringBuilder(inputName);
                while (result.length() < 3)
                    result.append("X");
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
            return error;
            // TODO: exception handling
        }
    }

    public static String computeDateOfBirth(String dateString, String gender) {
        String yearError = "0", dateError = "0";

        String[] dateValues = dateString.split("/");
        String dayString = dateValues[0];
        String monthString = dateValues[1];
        String yearString = dateValues[2];

        Map<Integer, String> monthMap = new HashMap<>();
        monthMap.put(1, "A");
        monthMap.put(2, "B");
        monthMap.put(3, "C");
        monthMap.put(4, "D");
        monthMap.put(5, "E");
        monthMap.put(6, "H");
        monthMap.put(7, "L");
        monthMap.put(8, "M");
        monthMap.put(9, "P");
        monthMap.put(10, "R");
        monthMap.put(11, "S");
        monthMap.put(12, "T");

        if (isYearValid(yearString)) {
            if (isDateValid(dayString, monthString, yearString)) {
                String result = "";
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
                    result += monthMap.get(month);

                    switch (gender) {
                        case "f": {
                            result += (day + 40);
                            break;
                        }
                        case "m": {
                            result += (day <= 10 ? "0" + day : day);
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Check numeric input.");
                }
                return result;
            } else {
                return dateError;
            }
        } else {
            String message =
                    "Please insert a numeric value between 1900 and "
                            + Calendar.getInstance().get(Calendar.YEAR)
                            + " in \"Year\" field.";
            return yearError;
        }
    }

    public static String computeTownOfBirth(String townString) throws IOException {
        List<Town> townList = new ArrayList<>();
        String townCode = "0";
        townString = townString.toUpperCase();
        try (BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(new File("assets/comuni.json"))))) {
            String line = read.readLine();
            String[] town;
            while (line != null) {
                town = line.split(";");
                townList.add(new Town(town[0], town[1], town[2]));
                line = read.readLine();
            }
            int i = 0;
            while (i < townList.size()) {
                if (townString.equals(townList.get(i).getName())) {
                    townCode = townList.get(i).getCadastral_code();
                    break;
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            // TODO: exception handling
        }

        if (townCode.equals("0")) {
        }
        return townCode;
    }

    public static String getPlaceCode(List<Town> towns, List<Country> countries, String selectedPlace) {
        String placeCode = "";
        String placeWithoutAreaCode = selectedPlace.substring(0, selectedPlace.length()-5);
        for (Town town : towns) {
            if (placeWithoutAreaCode.equals(town.getName())) {
                placeCode = town.getCadastral_code();
                break;
            }
        }
        if ("".equals(placeCode)) {
            for (Country country : countries) {
                if (placeWithoutAreaCode.equals(country.getName())) {
                    placeCode = country.getCadastral_code();
                    break;
                }
            }
        }
        return placeCode;
    }

    public static String computeControlChar(String incompleteFiscalCode) throws InterruptedException {

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

        String control = "";
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
            control = controlCharMap.get(sum);
        }
        return control;
    }

    public static String compute(String firstName, String lastName, String dateOfBirth, String gender, String townOfBirth, List<Town> towns, List<Country> countries) throws InterruptedException {
        StringBuilder fiscalCode = new StringBuilder();
        fiscalCode.append(computeSurname(lastName));
        fiscalCode.append(computeName(firstName));
        fiscalCode.append(computeDateOfBirth(dateOfBirth, gender));
        fiscalCode.append(getPlaceCode(towns, countries, townOfBirth));
        fiscalCode.append(computeControlChar(fiscalCode.toString()));
        return fiscalCode.toString();
    }
}
