package com.pancaldim.fiscalcode_app.fiscalcode.utils;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pancaldim.fiscalcode_app.ui.main.listener.DateOfBirthOnClickListener;
import com.pancaldim.fiscalcode_app.ui.main.listener.DateOfBirthWatcher;

import java.util.Calendar;
import java.util.Objects;

public class FragmentHelper {

    public static void hideVirtualKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputManager).hideSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static CompoundButton.OnCheckedChangeListener getRadioButtonListener(final RadioButton maleRadioButton) {
        return (compoundButton, b) -> maleRadioButton.setError(null);
    }

    public static Calendar initCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -30);
        return cal;
    }

    public static void setupGenderRadioButtons(View root, int maleRadioId, int femaleRadioId) {
        final RadioButton maleRadioButton = root.findViewById(maleRadioId);
        final RadioButton femaleRadioButton = root.findViewById(femaleRadioId);
        maleRadioButton.setOnClickListener(view -> hideVirtualKeyboard(root));
        femaleRadioButton.setOnClickListener(view -> hideVirtualKeyboard(root));

        maleRadioButton.setOnCheckedChangeListener(getRadioButtonListener(maleRadioButton));
        femaleRadioButton.setOnCheckedChangeListener(getRadioButtonListener(femaleRadioButton));
    }

    public static void setupPlaceOfBirth(View root, ArrayAdapter<String> pobArrayAdapter, int pobInputId) {
        AutoCompleteTextView autoCompletePoBTextView = root.findViewById(pobInputId);
        autoCompletePoBTextView.setOnItemClickListener((adapterView, view, i, l) -> hideVirtualKeyboard(adapterView));
        autoCompletePoBTextView.setAdapter(pobArrayAdapter);
    }

    public static void setupDateOfBirth(View root, Calendar calendar, int dobInputId) {
        final TextView dateOfBirth = root.findViewById(dobInputId);
        dateOfBirth.setRawInputType(InputType.TYPE_NULL);
        dateOfBirth.setOnClickListener(new DateOfBirthOnClickListener(calendar, dobInputId));
        dateOfBirth.addTextChangedListener(new DateOfBirthWatcher(calendar, dateOfBirth));
    }
}
