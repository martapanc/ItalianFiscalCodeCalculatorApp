package com.pancaldim.fiscalcode.fiscalcode.models;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.pancaldim.fiscalcode.R;
import com.pancaldim.fiscalcode.fiscalcode.computations.ValidateInputFields;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public enum InputField {

    FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, PLACE_OF_BIRTH, FISCAL_CODE;

    public static boolean validateField(Fragment computeFragment, RadioButton femaleRadioButton, RadioButton maleRadioButton) {
        boolean allFieldsValid = true;
        maleRadioButton.setError(null);
        femaleRadioButton.setError(null);
        if (!maleRadioButton.isChecked() && !femaleRadioButton.isChecked()) {
            allFieldsValid = false;
            femaleRadioButton.setError(computeFragment.getString(R.string.empty_field_error));
            femaleRadioButton.requestFocus();
        }
        return allFieldsValid;
    }

    public boolean validateField(EditText editText, String[] places, Fragment computeFragment) {
        editText.setError(null);
        String input = editText.getText().toString();
        boolean allFieldsValid = true;
        if (EMPTY.equals(input)) {
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

    public boolean validateField(TextView textView, Fragment computeFragment) {
        boolean allFieldsValid = true;
        textView.setError(null);
        String input = textView.getText().toString();
        if (EMPTY.equals(input)) {
            allFieldsValid = false;
            textView.setError(computeFragment.getString(R.string.empty_field_error));
            textView.requestFocus();
        } else if (!ValidateInputFields.isFieldValid(input, this, null)) {
            allFieldsValid = false;
            textView.setError(computeFragment.getString(R.string.invalid_input_error));
            textView.requestFocus();
        }
        return allFieldsValid;
    }
}
