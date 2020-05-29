package com.example.fiscalcode_java.fiscalCode.computations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameAndSurnameComputations {

    private static final String VOWEL_PATTERN = "[AEIOU]";
    private static final String CONSONANT_PATTERN = "[B-DF-HJ-NP-TV-Z]";
    private static final Pattern COMPILE_WOVELS = Pattern.compile(VOWEL_PATTERN);
    private static final Pattern COMPILE_CONSONANTS = Pattern.compile(CONSONANT_PATTERN);

    static String pickFirstThreeConsonants(String input) {
        Matcher m = COMPILE_CONSONANTS.matcher(input);
        StringBuilder result = new StringBuilder();
        int cont = 1;
        while (m.find() && cont <= 3) {
            result.append(m.group());
            cont++;
        }
        return result.toString();
    }

    static String pickFirstTwoConsonantsAndFirstVowel(String input) {
        Matcher m = COMPILE_CONSONANTS.matcher(input);
        StringBuilder result = new StringBuilder();
        int cont = 1;
        while (m.find() && cont <= 2) {
            result.append(m.group());
            cont++;
        }
        m = COMPILE_WOVELS.matcher(input);
        cont = 1;
        while (m.find() && cont <= 1) {
            result.append(m.group());
            cont++;
        }
        return result.toString();
    }

    static String pickFirstConsonantAndFirstTwoVowels(String input) {
        Matcher m = COMPILE_CONSONANTS.matcher(input);
        StringBuilder result = new StringBuilder();
        int cont = 1;
        while (m.find() && cont <= 1) {
            result.append(m.group());
            cont++;
        }
        m = COMPILE_WOVELS.matcher(input);
        cont = 1;
        while (m.find() && cont <= 2) {
            result.append(m.group());
            cont++;
        }
        return result.toString();
    }

    static String pickFirstThreeVowels(String input) {
        Matcher m = COMPILE_WOVELS.matcher(input);
        StringBuilder result = new StringBuilder();
        int cont = 1;
        while (m.find() && cont <= 3) {
            result.append(m.group());
            cont++;
        }
        return result.toString();
    }

    static String pickFirstThirdAndFourthConsonant(String inputName) {
        Matcher m = COMPILE_CONSONANTS.matcher(inputName);
        StringBuilder result = new StringBuilder();
        int cont = 1;
        while (m.find() && cont <= 4) {
            if (cont != 2) {
                result.append(m.group());
            }
            cont++;
        }
        return result.toString();
    }
}