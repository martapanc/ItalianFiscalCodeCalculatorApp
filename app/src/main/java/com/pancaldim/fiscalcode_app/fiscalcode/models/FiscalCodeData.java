package com.pancaldim.fiscalcode_app.fiscalcode.models;

import android.content.res.Resources;

import com.pancaldim.fiscalcode_app.R;

import java.text.MessageFormat;

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
    private String fiscalCode;

    public boolean areFieldsNotEmpty() {
        return !getFirstNameCode().isEmpty() && !getLastNameCode().isEmpty()
                && (!getGender().equals(Gender.F) || !getGender().equals(Gender.M))
                && !getDateOfBirth().isEmpty() && !getPlaceOfBirth().isEmpty()
                && !getFiscalCode().isEmpty();
    }

    public String formatData(Resources res) {
        return MessageFormat.format(
                "{0}: {1}\n{2}: {3}\n{4}: {5}\n{6}: {7}\n{8}: {9}\n{10}: {11}",
                res.getString(R.string.fiscal_code_label), getFiscalCode(),
                res.getString(R.string.first_name_label), getFirstNameCode(),
                res.getString(R.string.last_name_label), getLastNameCode(),
                res.getString(R.string.gender_label), getGender(),
                res.getString(R.string.dob_label), getDateOfBirth(),
                res.getString(R.string.pob_label), getPlaceOfBirth()
        );
    }
}
