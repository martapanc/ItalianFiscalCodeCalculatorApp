package com.example.fiscalcode_java.fiscalcode.computations;

import com.example.fiscalcode_java.exception.FiscalCodeComputationException;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ComputeFiscalCodeHelperTest {

    @Test
    public void computeSurname() throws FiscalCodeComputationException {
        assertEquals("PNC", ComputeFiscalCodeHelper.computeLastName("Pancaldi"));
        assertEquals("MRN", ComputeFiscalCodeHelper.computeLastName("Morini"));
        assertEquals("RSS", ComputeFiscalCodeHelper.computeLastName("Rossi"));
        assertEquals("RSO", ComputeFiscalCodeHelper.computeLastName("Rosi"));
        assertEquals("HUX", ComputeFiscalCodeHelper.computeLastName("Hu"));
        assertEquals("LAX", ComputeFiscalCodeHelper.computeLastName("Al"));
        assertEquals("AXX", ComputeFiscalCodeHelper.computeLastName("A"));
        assertEquals("NXX", ComputeFiscalCodeHelper.computeLastName("N"));
    }

    @Test
    public void computeName() throws FiscalCodeComputationException {
        assertEquals("MRT", ComputeFiscalCodeHelper.computeFirstName("Marta"));
        assertEquals("MRT", ComputeFiscalCodeHelper.computeFirstName("marta"));
        assertEquals("MRT", ComputeFiscalCodeHelper.computeFirstName("Umberto"));
        assertEquals("LBT", ComputeFiscalCodeHelper.computeFirstName("Elisabetta"));
        assertEquals("MRA", ComputeFiscalCodeHelper.computeFirstName("Maria"));
        assertEquals("RTI", ComputeFiscalCodeHelper.computeFirstName("Rita"));
        assertEquals("DEX", ComputeFiscalCodeHelper.computeFirstName("Ed"));
        assertEquals("LAX", ComputeFiscalCodeHelper.computeFirstName("Al"));
        assertEquals("LIX", ComputeFiscalCodeHelper.computeFirstName("Li"));
        assertEquals("JAX", ComputeFiscalCodeHelper.computeFirstName("AJ"));
        assertEquals("JXA", ComputeFiscalCodeHelper.computeFirstName("JAx"));
        assertEquals("EXX", ComputeFiscalCodeHelper.computeFirstName("E"));
    }

    @Test
    public void computeDateOfBirth() throws FiscalCodeComputationException {
        assertEquals("95L52", ComputeFiscalCodeHelper.computeDateOfBirth("12/07/1995", "f"));
        assertEquals("59C52", ComputeFiscalCodeHelper.computeDateOfBirth("12/03/1959", "f"));
        assertEquals("40H63", ComputeFiscalCodeHelper.computeDateOfBirth("23/06/1940", "f"));
        assertEquals("80A01", ComputeFiscalCodeHelper.computeDateOfBirth("01/01/1980", "m"));
    }

    @Test
    public void computeControlChar() throws InterruptedException, FiscalCodeComputationException {
        assertEquals("R", ComputeFiscalCodeHelper.computeControlChar("PNCMRT95L52E253"));
        assertEquals("N", ComputeFiscalCodeHelper.computeControlChar("MRNLBT59C52H223"));
    }
}