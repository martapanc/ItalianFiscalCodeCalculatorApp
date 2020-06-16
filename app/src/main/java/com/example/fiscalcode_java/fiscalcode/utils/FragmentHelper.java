package com.example.fiscalcode_java.fiscalcode.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.example.fiscalcode_java.R;

public class FragmentHelper {

    public static void setupPlaceOfBirth(View root, ArrayAdapter<String> pobArrayAdapter) {
        AutoCompleteTextView autoCompletePoBTextView = root.findViewById(R.id.pob_autocompleteTextView);
        autoCompletePoBTextView.setOnItemClickListener((adapterView, view, i, l) -> hideVirtualKeyboard(adapterView));
        autoCompletePoBTextView.setAdapter(pobArrayAdapter);
    }

    public static void hideVirtualKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void setupGenderRadioButtons(View root) {
        final RadioButton maleRadioButton = root.findViewById(R.id.maleRadioButton);
        final RadioButton femaleRadioButton = root.findViewById(R.id.femaleRadioButton);
        maleRadioButton.setOnCheckedChangeListener(getRadioButtonListener(maleRadioButton));
        femaleRadioButton.setOnCheckedChangeListener(getRadioButtonListener(femaleRadioButton));
    }

    private static CompoundButton.OnCheckedChangeListener getRadioButtonListener(final RadioButton maleRadioButton) {
        return (compoundButton, b) -> maleRadioButton.setError(null);
    }
}
