package com.example.fiscalcode_java.fiscalcode.models;

import android.widget.EditText;
import android.widget.RadioButton;

import com.example.fiscalcode_java.R;
import com.example.fiscalcode_java.fiscalcode.computations.ValidateInputFields;
import com.example.fiscalcode_java.ui.main.compute.ComputeFragment;

public enum InputField {

    FIRST_NAME, LAST_NAME, GENDER, DATE_OF_BIRTH, PLACE_OF_BIRTH, FISCAL_CODE;

    public static boolean validateField(ComputeFragment computeFragment, RadioButton femaleRadioButton, RadioButton maleRadioButton, boolean allFieldsValid) {
        maleRadioButton.setError(null);
        femaleRadioButton.setError(null);
        if (!maleRadioButton.isChecked() && !femaleRadioButton.isChecked()) {
            allFieldsValid = false;
            femaleRadioButton.setError(computeFragment.getString(R.string.empty_field_error));
            femaleRadioButton.requestFocus();
        }
        return allFieldsValid;
    }

    public boolean validateField(EditText editText, boolean allFieldsValid, String[] places, ComputeFragment computeFragment) {
        editText.setError(null);
        String input = editText.getText().toString();
        if (input.equals("")) {
            allFieldsValid = false;
            editText.setError(computeFragment.getString(R.string.empty_field_error));
            editText.requestFocus();
        } else if (!ValidateInputFields.isFieldValid(input, this, places)) {
            allFieldsValid = false;
            editText.setError(computeFragment.getString(R.string.invalid_input_error));
            editText.requestFocus();
        }
        return allFieldsValid;
    }
}
