package com.example.fiscalcode_java.computations;

import com.example.fiscalcode_java.fiscalCode.computations.NameAndSurnameComputations;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NameAndSurnameComputationsTest {

    @Test
    public void TestPickFirstThreeConsonants() {
        assertEquals("MRT", NameAndSurnameComputations.pickFirstThreeConsonants("MARTA"));
        assertEquals("PNC", NameAndSurnameComputations.pickFirstThreeConsonants("PANCALDI"));
    }

    @Test
    public void TestPickFirstTwoConsonantsAndFirstVowel() {
        assertEquals("RTI", NameAndSurnameComputations.pickFirstTwoConsonantsAndFirstVowel("RITA"));
        assertEquals("MRA", NameAndSurnameComputations.pickFirstTwoConsonantsAndFirstVowel("MARIA"));
    }

    @Test
    public void TestPickFirstConsonantAndFirstTwoVowels() {
        assertEquals("LOI", NameAndSurnameComputations.pickFirstConsonantAndFirstTwoVowels("OLI"));
        assertEquals("PAA", NameAndSurnameComputations.pickFirstConsonantAndFirstTwoVowels("PANCALDI"));
    }

    @Test
    public void TestPickFirstThreeVowels() {
        assertEquals ( "AOI", NameAndSurnameComputations.pickFirstThreeVowels ( "PAOLINI" ) );
        assertEquals ( "AAI", NameAndSurnameComputations.pickFirstThreeVowels ( "PANCALDI" ) );
    }

    @Test
    public void TestPickFirstThirdAndFourthConsonant() {
        assertEquals("LBT", NameAndSurnameComputations.pickFirstThirdAndFourthConsonant("ELISABETTA"));
        assertEquals("MRT", NameAndSurnameComputations.pickFirstThirdAndFourthConsonant("UMBERTO"));
    }
}
