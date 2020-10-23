package com.pancaldim.fiscalcode_app.fiscalcode.models;

public enum Gender {
    M, F;

    public static Gender getGender(String genderString) {
        return "m".equalsIgnoreCase(genderString) ? M : F;
    }
}