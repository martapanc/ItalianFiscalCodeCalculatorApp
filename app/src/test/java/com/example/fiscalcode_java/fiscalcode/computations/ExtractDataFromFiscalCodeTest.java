package com.example.fiscalcode_java.fiscalcode.computations;

import com.example.fiscalcode_java.exception.FiscalCodeExtractionException;
import com.example.fiscalcode_java.fiscalcode.models.Gender;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExtractDataFromFiscalCodeTest {

    @Test
    public void testExtractDateOfBirth() throws FiscalCodeExtractionException {
        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCode.extractDateOfBirth("", Gender.F),
                "Date of birth input has a wrong format");
        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCode.extractDateOfBirth("95L5S", Gender.F));
        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCode.extractDateOfBirth("95K52", Gender.F));
        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCode.extractDateOfBirth("95L15S", Gender.F));

        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCode.extractDateOfBirth("95L12", Gender.F),
                "Day of birth is out of range: 12 for F");
        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCode.extractDateOfBirth("95L44", Gender.M),
                "Day of birth is out of range: 44 for M");

        assertEquals("12/07/95", ExtractDataFromFiscalCode.extractDateOfBirth("95L52", Gender.F));
        assertEquals("31/01/81", ExtractDataFromFiscalCode.extractDateOfBirth("81A31", Gender.M));
        assertEquals("04/10/10", ExtractDataFromFiscalCode.extractDateOfBirth("10R04", Gender.M));
        assertEquals("11/09/00", ExtractDataFromFiscalCode.extractDateOfBirth("00P11", Gender.M));
        assertEquals("18/06/03", ExtractDataFromFiscalCode.extractDateOfBirth("03H58", Gender.F));
    }

    @Test
    public void testExtractGender() throws FiscalCodeExtractionException {
        assertEquals(Gender.F, ExtractDataFromFiscalCode.extractGender("41"));
        assertEquals(Gender.F, ExtractDataFromFiscalCode.extractGender("71"));
        assertEquals(Gender.M, ExtractDataFromFiscalCode.extractGender("1"));
        assertEquals(Gender.M, ExtractDataFromFiscalCode.extractGender("31"));

        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCode.extractGender("0"),
                "Day out of range for gender");
        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCode.extractGender("72"));
        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCode.extractGender(""),
                "Error parsing gender");
        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCode.extractGender("A"));
        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCode.extractGender("-1"));
    }
}