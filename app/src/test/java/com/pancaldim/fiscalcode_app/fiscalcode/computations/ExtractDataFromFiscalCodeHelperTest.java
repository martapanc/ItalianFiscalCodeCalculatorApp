package com.pancaldim.fiscalcode_app.fiscalcode.computations;

import com.pancaldim.fiscalcode_app.exception.FiscalCodeExtractionException;
import com.pancaldim.fiscalcode_app.fiscalcode.models.Gender;

import org.junit.Test;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExtractDataFromFiscalCodeHelperTest {

    @Test
    public void testExtractDateOfBirth() throws FiscalCodeExtractionException {
        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCodeHelper.extractDateOfBirth(EMPTY, Gender.F),
                "Date of birth input has a wrong format");
        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCodeHelper.extractDateOfBirth("95L5S", Gender.F));
        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCodeHelper.extractDateOfBirth("95K52", Gender.F));
        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCodeHelper.extractDateOfBirth("95L15S", Gender.F));

        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCodeHelper.extractDateOfBirth("95L12", Gender.F),
                "Day of birth is out of range: 12 for F");
        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCodeHelper.extractDateOfBirth("95L44", Gender.M),
                "Day of birth is out of range: 44 for M");

        assertEquals("12/07/95", ExtractDataFromFiscalCodeHelper.extractDateOfBirth("95L52", Gender.F));
        assertEquals("31/01/81", ExtractDataFromFiscalCodeHelper.extractDateOfBirth("81A31", Gender.M));
        assertEquals("04/10/10", ExtractDataFromFiscalCodeHelper.extractDateOfBirth("10R04", Gender.M));
        assertEquals("11/09/00", ExtractDataFromFiscalCodeHelper.extractDateOfBirth("00P11", Gender.M));
        assertEquals("18/06/03", ExtractDataFromFiscalCodeHelper.extractDateOfBirth("03H58", Gender.F));
    }

    @Test
    public void testExtractGender() throws FiscalCodeExtractionException {
        assertEquals(Gender.F, ExtractDataFromFiscalCodeHelper.extractGender("41"));
        assertEquals(Gender.F, ExtractDataFromFiscalCodeHelper.extractGender("71"));
        assertEquals(Gender.M, ExtractDataFromFiscalCodeHelper.extractGender("1"));
        assertEquals(Gender.M, ExtractDataFromFiscalCodeHelper.extractGender("31"));

        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCodeHelper.extractGender("0"),
                "Day out of range for gender");
        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCodeHelper.extractGender("72"));
        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCodeHelper.extractGender(EMPTY),
                "Error parsing gender");
        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCodeHelper.extractGender("A"));
        assertThrows(FiscalCodeExtractionException.class, () -> ExtractDataFromFiscalCodeHelper.extractGender("-1"));
    }
}