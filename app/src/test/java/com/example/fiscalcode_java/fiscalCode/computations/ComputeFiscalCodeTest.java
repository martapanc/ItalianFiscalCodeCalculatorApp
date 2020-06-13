package com.example.fiscalcode_java.fiscalCode.computations;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ComputeFiscalCodeTest {

    @Test
    public void computeSurname() {
        assertEquals("PNC", ComputeFiscalCode.computeSurname("Pancaldi"));
        assertEquals("MRN", ComputeFiscalCode.computeSurname("Morini"));
    }

    @Test
    public void computeName() {
        assertEquals("MRT", ComputeFiscalCode.computeName("Marta"));
        assertEquals("MRT", ComputeFiscalCode.computeName("marta"));
        assertEquals("MRT", ComputeFiscalCode.computeName("Umberto"));
        assertEquals("LBT", ComputeFiscalCode.computeName("Elisabetta"));
        assertEquals("MRA", ComputeFiscalCode.computeName("Maria"));
        assertEquals("RTI", ComputeFiscalCode.computeName("Rita"));
    }

    @Test
    public void computeDateOfBirth() {
        assertEquals("95L52", ComputeFiscalCode.computeDateOfBirth("12/07/1995", "f"));
        assertEquals("59C52", ComputeFiscalCode.computeDateOfBirth("12/03/1959", "f"));
        assertEquals("40H63", ComputeFiscalCode.computeDateOfBirth("23/06/1940", "f"));
        assertEquals("80A01", ComputeFiscalCode.computeDateOfBirth("01/01/1980", "m"));
    }

    @Test
    public void computeControlChar() throws InterruptedException {
        assertEquals("R", ComputeFiscalCode.computeControlChar("PNCMRT95L52E253"));
        assertEquals("N", ComputeFiscalCode.computeControlChar("MRNLBT59C52H223"));
    }
}