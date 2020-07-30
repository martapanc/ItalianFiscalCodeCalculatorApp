package com.pancaldim.fiscalcode.fiscalcode.computations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameAndSurnameComputations {

    private static final String VOWEL_PATTERN = "[AEIOU]";
    private static final String CONSONANT_PATTERN = "[B-DF-HJ-NP-TV-Z]";
    private static final Pattern COMPILE_WOVELS = Pattern.compile(VOWEL_PATTERN);
    private static final Pattern COMPILE_CONSONANTS = Pattern.compile(CONSONANT_PATTERN);

    public static String pickFirstThreeConsonants(String input) {
        Matcher matcher = COMPILE_CONSONANTS.matcher(input);
        StringBuilder result = new StringBuilder();
        int count = 1;
        while (matcher.find() && count <= 3) {
            result.append(matcher.group());
            count++;
        }
        return result.toString();
    }

    public static String pickFirstTwoConsonantsAndFirstVowel(String input) {
        Matcher matcher = COMPILE_CONSONANTS.matcher(input);
        StringBuilder result = new StringBuilder();
        int count = 1;
        while (matcher.find() && count <= 2) {
            result.append(matcher.group());
            count++;
        }
        matcher = COMPILE_WOVELS.matcher(input);
        count = 1;
        while (matcher.find() && count <= 1) {
            result.append(matcher.group());
            count++;
        }
        return result.toString();
    }

    public static String pickFirstConsonantAndFirstTwoVowels(String input) {
        Matcher matcher = COMPILE_CONSONANTS.matcher(input);
        StringBuilder result = new StringBuilder();
        int count = 1;
        while (matcher.find() && count <= 1) {
            result.append(matcher.group());
            count++;
        }
        matcher = COMPILE_WOVELS.matcher(input);
        count = 1;
        while (matcher.find() && count <= 2) {
            result.append(matcher.group());
            count++;
        }
        return result.toString();
    }

    public static String pickFirstThreeVowels(String input) {
        Matcher matcher = COMPILE_WOVELS.matcher(input);
        StringBuilder result = new StringBuilder();
        int count = 1;
        while (matcher.find() && count <= 3) {
            result.append(matcher.group());
            count++;
        }
        return result.toString();
    }

    public static String pickFirstAndThirdAndFourthConsonant(String inputName) {
        Matcher matcher = COMPILE_CONSONANTS.matcher(inputName);
        StringBuilder result = new StringBuilder();
        int count = 1;
        while (matcher.find() && count <= 4) {
            if (count != 2) {
                result.append(matcher.group());
            }
            count++;
        }
        return result.toString();
    }
}