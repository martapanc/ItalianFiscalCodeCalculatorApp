package com.pancaldim.fiscalcode_app.fiscalcode.computations;

import org.junit.Test;

import java.util.Calendar;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class FunctionCheckHelperTest {

    private final int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    @Test
    public void testIsAllLetters() {
        assertTrue(FunctionCheckHelper.isAllLetters("A"));
        assertTrue(FunctionCheckHelper.isAllLetters("a"));
        assertTrue(FunctionCheckHelper.isAllLetters("AbCd"));
        assertTrue(FunctionCheckHelper.isAllLetters("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
        assertFalse(FunctionCheckHelper.isAllLetters(EMPTY));
        assertFalse(FunctionCheckHelper.isAllLetters("al1"));
        assertFalse(FunctionCheckHelper.isAllLetters("aa-aa"));
        assertFalse(FunctionCheckHelper.isAllLetters("1"));
        assertFalse(FunctionCheckHelper.isAllLetters("a1"));
        assertFalse(FunctionCheckHelper.isAllLetters("a!"));
        assertFalse(FunctionCheckHelper.isAllLetters("AAAAAAAAAAA@AAAA"));
    }

    @Test
    public void testIsAllDigits() {
        assertTrue(FunctionCheckHelper.isAllDigits("1"));
        assertTrue(FunctionCheckHelper.isAllDigits("1234567890"));
        assertFalse(FunctionCheckHelper.isAllDigits(EMPTY));
        assertFalse(FunctionCheckHelper.isAllDigits("1.2"));
        assertFalse(FunctionCheckHelper.isAllDigits("3r"));
        assertFalse(FunctionCheckHelper.isAllDigits("3!"));
        assertFalse(FunctionCheckHelper.isAllDigits("1,2"));
    }

    @Test
    public void testHowManyLettersOfType() {
        assertEquals(3, FunctionCheckHelper.howManyLettersOfType("MARTA", FunctionCheckHelper.CONSONANTS));
        assertEquals(2, FunctionCheckHelper.howManyLettersOfType("MARTA", FunctionCheckHelper.VOWELS));
        assertEquals(0, FunctionCheckHelper.howManyLettersOfType("MARTA", FunctionCheckHelper.NUMBERS));
        assertEquals(5, FunctionCheckHelper.howManyLettersOfType("MARTA", FunctionCheckHelper.ALPHABET));
        assertEquals(7, FunctionCheckHelper.howManyLettersOfType("Helloworld", FunctionCheckHelper.CONSONANTS));
        assertEquals(3, FunctionCheckHelper.howManyLettersOfType("Helloworld", FunctionCheckHelper.VOWELS));
        assertEquals(10, FunctionCheckHelper.howManyLettersOfType("Helloworld", FunctionCheckHelper.ALPHABET));
        assertEquals(0, FunctionCheckHelper.howManyLettersOfType("Helloworld", FunctionCheckHelper.NUMBERS));
    }

    @Test
    public void testIsYearValid() {
        assertTrue(FunctionCheckHelper.isYearValid("1900"));
        assertTrue(FunctionCheckHelper.isYearValid("1901"));
        assertTrue(FunctionCheckHelper.isYearValid("1999"));
        assertTrue(FunctionCheckHelper.isYearValid("2019"));
        assertTrue(FunctionCheckHelper.isYearValid(Integer.toString(currentYear)));
        assertFalse(FunctionCheckHelper.isYearValid(Integer.toString(currentYear + 1)));
        assertFalse(FunctionCheckHelper.isYearValid("1899"));
        assertFalse(FunctionCheckHelper.isYearValid("19o0"));
    }

    @Test
    public void testIsDateValid() {
        assertTrue(FunctionCheckHelper.isDateValid("12", "12", "2019"));
        assertTrue(FunctionCheckHelper.isDateValid("01", "01", "1900"));
        Calendar cal = Calendar.getInstance();
        assertTrue(FunctionCheckHelper.isDateValid(cal));
        assertFalse(FunctionCheckHelper.isDateValid("01", "01", "1899"));
        assertFalse(FunctionCheckHelper.isDateValid("01", "01", "1899"));
        assertFalse(FunctionCheckHelper.isDateValid("29", "02", "2001"));
        cal.add(1, Calendar.DAY_OF_MONTH);
        assertFalse(FunctionCheckHelper.isDateValid(cal));
    }

    @Test
    public void testReplaceSpecialChars() {
        assertEquals("AE", FunctionCheckHelper.replaceSpecialChars("Ä"));
        assertEquals(FunctionCheckHelper.replaceSpecialChars("nicolò"), "NICOLO");
        assertEquals(FunctionCheckHelper.replaceSpecialChars("Maria Antonietta"), "MARIAANTONIETTA");
        assertEquals(FunctionCheckHelper.replaceSpecialChars("de' medici"), "DEMEDICI");
        assertEquals(FunctionCheckHelper.replaceSpecialChars("françois"), "FRANCOIS");
        assertEquals(FunctionCheckHelper.replaceSpecialChars("müllerstraße"), "MUELLERSTRASSE");
    }

    @Test
    public void testIsFiscalCodeValid() {
        assertTrue(FunctionCheckHelper.isFiscalCodeValid("PNCMRT95L52E253R"));
        assertTrue(FunctionCheckHelper.isFiscalCodeValid("pncmrt95L52E253R"));
        assertFalse(FunctionCheckHelper.isFiscalCodeValid("PNCMRT95K52E253R"));
        assertFalse(FunctionCheckHelper.isFiscalCodeValid("PNCMRT95K52E2534R"));
        assertFalse(FunctionCheckHelper.isFiscalCodeValid("PNCMRT95K52E24R"));
        assertFalse(FunctionCheckHelper.isFiscalCodeValid("PNCMRT9lK52E247R"));
    }

    @Test
    public void isTwoLettersAndVowelBeforeConsonant() {
        assertTrue(FunctionCheckHelper.isTwoLettersAndVowelBeforeConsonant("AL"));
        assertTrue(FunctionCheckHelper.isTwoLettersAndVowelBeforeConsonant("ED"));
        assertFalse(FunctionCheckHelper.isTwoLettersAndVowelBeforeConsonant("LI"));
        assertFalse(FunctionCheckHelper.isTwoLettersAndVowelBeforeConsonant("NG"));
        assertFalse(FunctionCheckHelper.isTwoLettersAndVowelBeforeConsonant("AE"));
        assertFalse(FunctionCheckHelper.isTwoLettersAndVowelBeforeConsonant("ALI"));
        assertFalse(FunctionCheckHelper.isTwoLettersAndVowelBeforeConsonant("A"));
    }
}