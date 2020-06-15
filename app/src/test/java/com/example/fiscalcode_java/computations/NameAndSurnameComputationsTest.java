package com.example.fiscalcode_java.computations;

import com.example.fiscalcode_java.fiscalcode.computations.NameAndSurnameComputations;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NameAndSurnameComputationsTest {

    @Test
    public void testPickFirstThreeConsonants() {
        assertEquals("MRT", NameAndSurnameComputations.pickFirstThreeConsonants("MARTA"));
        assertEquals("PNC", NameAndSurnameComputations.pickFirstThreeConsonants("PANCALDI"));
    }

    @Test
    public void testPickFirstTwoConsonantsAndFirstVowel() {
        assertEquals("RTI", NameAndSurnameComputations.pickFirstTwoConsonantsAndFirstVowel("RITA"));
        assertEquals("MRA", NameAndSurnameComputations.pickFirstTwoConsonantsAndFirstVowel("MARIA"));
    }

    @Test
    public void testPickFirstConsonantAndFirstTwoVowels() {
        assertEquals("LOI", NameAndSurnameComputations.pickFirstConsonantAndFirstTwoVowels("OLI"));
        assertEquals("PAA", NameAndSurnameComputations.pickFirstConsonantAndFirstTwoVowels("PANCALDI"));
    }

    @Test
    public void testPickFirstThreeVowels() {
        assertEquals ( "AOI", NameAndSurnameComputations.pickFirstThreeVowels ( "PAOLINI" ) );
        assertEquals ( "AAI", NameAndSurnameComputations.pickFirstThreeVowels ( "PANCALDI" ) );
    }

    @Test
    public void testPickFirstThirdAndFourthConsonant() {
        assertEquals("LBT", NameAndSurnameComputations.pickFirstAndThirdAndFourthConsonant("ELISABETTA"));
        assertEquals("MRT", NameAndSurnameComputations.pickFirstAndThirdAndFourthConsonant("UMBERTO"));
    }
}
