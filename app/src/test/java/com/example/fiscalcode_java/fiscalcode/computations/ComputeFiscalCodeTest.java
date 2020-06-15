package com.example.fiscalcode_java.fiscalcode.computations;

import com.example.fiscalcode_java.exception.FiscalCodeComputationException;

import org.junit.Test;

import static org.junit.Assert.*;

public class ComputeFiscalCodeTest {

    @Test
    public void computeSurname() throws FiscalCodeComputationException {
        assertEquals("PNC", ComputeFiscalCode.computeLastName("Pancaldi"));
        assertEquals("MRN", ComputeFiscalCode.computeLastName("Morini"));
    }

    @Test
    public void computeName() throws FiscalCodeComputationException {
        assertEquals("MRT", ComputeFiscalCode.computeFirstName("Marta"));
        assertEquals("MRT", ComputeFiscalCode.computeFirstName("marta"));
        assertEquals("MRT", ComputeFiscalCode.computeFirstName("Umberto"));
        assertEquals("LBT", ComputeFiscalCode.computeFirstName("Elisabetta"));
        assertEquals("MRA", ComputeFiscalCode.computeFirstName("Maria"));
        assertEquals("RTI", ComputeFiscalCode.computeFirstName("Rita"));
    }

    @Test
    public void computeDateOfBirth() throws FiscalCodeComputationException {
        assertEquals("95L52", ComputeFiscalCode.computeDateOfBirth("12/07/1995", "f"));
        assertEquals("59C52", ComputeFiscalCode.computeDateOfBirth("12/03/1959", "f"));
        assertEquals("40H63", ComputeFiscalCode.computeDateOfBirth("23/06/1940", "f"));
        assertEquals("80A01", ComputeFiscalCode.computeDateOfBirth("01/01/1980", "m"));
    }

    @Test
    public void computeControlChar() throws InterruptedException, FiscalCodeComputationException {
        assertEquals("R", ComputeFiscalCode.computeControlChar("PNCMRT95L52E253"));
        assertEquals("N", ComputeFiscalCode.computeControlChar("MRNLBT59C52H223"));
    }
}