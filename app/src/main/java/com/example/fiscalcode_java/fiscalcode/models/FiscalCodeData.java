package com.example.fiscalcode_java.fiscalcode.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FiscalCodeData {

    private String firstNameCode;
    private String lastNameCode;
    private Gender gender;
    private String dateOfBirth;
    private String placeOfBirth;
}
