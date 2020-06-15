package com.example.fiscalcode_java.fiscalCode.computations;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class FunctionChecksTest {

    private int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    @Test
    public void testIsAllLetters() {
        assertTrue(FunctionChecks.isAllLetters("A"));
        assertTrue(FunctionChecks.isAllLetters("a"));
        assertTrue(FunctionChecks.isAllLetters("AbCd"));
        assertTrue(FunctionChecks.isAllLetters("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
        assertFalse(FunctionChecks.isAllLetters(""));
        assertFalse(FunctionChecks.isAllLetters("al1"));
        assertFalse(FunctionChecks.isAllLetters("aa-aa"));
        assertFalse(FunctionChecks.isAllLetters("1"));
        assertFalse(FunctionChecks.isAllLetters("a1"));
        assertFalse(FunctionChecks.isAllLetters("a!"));
        assertFalse(FunctionChecks.isAllLetters("AAAAAAAAAAA@AAAA"));
    }

    @Test
    public void testIsAllDigits() {
        assertTrue(FunctionChecks.isAllDigits("1"));
        assertTrue(FunctionChecks.isAllDigits("1234567890"));
        assertFalse(FunctionChecks.isAllDigits(""));
        assertFalse(FunctionChecks.isAllDigits("1.2"));
        assertFalse(FunctionChecks.isAllDigits("3r"));
        assertFalse(FunctionChecks.isAllDigits("3!"));
        assertFalse(FunctionChecks.isAllDigits("1,2"));
    }

    @Test
    public void testHowManyLettersOfType() {
        assertEquals(3, FunctionChecks.howManyLettersOfType("MARTA", FunctionChecks.CONSONANTS));
        assertEquals(2, FunctionChecks.howManyLettersOfType("MARTA", FunctionChecks.VOWELS));
        assertEquals(0, FunctionChecks.howManyLettersOfType("MARTA", FunctionChecks.NUMBERS));
        assertEquals(5, FunctionChecks.howManyLettersOfType("MARTA", FunctionChecks.ALPHABET));
        assertEquals(7, FunctionChecks.howManyLettersOfType("Helloworld", FunctionChecks.CONSONANTS));
        assertEquals(3, FunctionChecks.howManyLettersOfType("Helloworld", FunctionChecks.VOWELS));
        assertEquals(10, FunctionChecks.howManyLettersOfType("Helloworld", FunctionChecks.ALPHABET));
        assertEquals(0, FunctionChecks.howManyLettersOfType("Helloworld", FunctionChecks.NUMBERS));
    }

    @Test
    public void testIsYearValid() {
        assertTrue(FunctionChecks.isYearValid("1900"));
        assertTrue(FunctionChecks.isYearValid("1901"));
        assertTrue(FunctionChecks.isYearValid("1999"));
        assertTrue(FunctionChecks.isYearValid("2019"));
        assertTrue(FunctionChecks.isYearValid(Integer.toString(currentYear)));
        assertFalse(FunctionChecks.isYearValid(Integer.toString(currentYear + 1)));
        assertFalse(FunctionChecks.isYearValid("1899"));
        assertFalse(FunctionChecks.isYearValid("19o0"));
    }

    @Test
    public void testIsDateValid() {
        assertTrue(FunctionChecks.isDateValid("12", "12", "2019"));
        assertTrue(FunctionChecks.isDateValid("01", "01", "1900"));
        Calendar cal = Calendar.getInstance();
        assertTrue(FunctionChecks.isDateValid(cal));
        assertFalse(FunctionChecks.isDateValid("01", "01", "1899"));
        assertFalse(FunctionChecks.isDateValid("01", "01", "1899"));
        assertFalse(FunctionChecks.isDateValid("29", "02", "2001"));
        cal.add(1, Calendar.DAY_OF_MONTH);
        assertFalse(FunctionChecks.isDateValid(cal));
    }

    @Test
    public void testReplaceSpecialChars() {
        assertEquals("AE", FunctionChecks.replaceSpecialChars("Ä"));
        assertEquals(FunctionChecks.replaceSpecialChars("nicolò"), "NICOLO");
        assertEquals(FunctionChecks.replaceSpecialChars("Maria Antonietta"), "MARIAANTONIETTA");
        assertEquals(FunctionChecks.replaceSpecialChars("de' medici"), "DEMEDICI");
        assertEquals(FunctionChecks.replaceSpecialChars("françois"), "FRANCOIS");
        assertEquals(FunctionChecks.replaceSpecialChars("müllerstraße"), "MUELLERSTRASSE");
    }
}