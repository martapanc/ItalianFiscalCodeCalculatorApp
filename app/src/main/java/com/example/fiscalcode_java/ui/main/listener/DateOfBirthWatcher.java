package com.example.fiscalcode_java.ui.main.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import lombok.SneakyThrows;

import static com.example.fiscalcode_java.fiscalcode.constants.DateFormat.DD_MM_YYYY;

public class DateOfBirthWatcher implements TextWatcher {

    private Calendar calendar;
    private EditText dateOfBirth;

    public DateOfBirthWatcher(Calendar calendar, EditText dateOfBirth) {
        this.calendar = calendar;
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @SneakyThrows
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YYYY, Locale.ITALY);
        //TODO: handle exceptions like this
        if (charSequence != null && !charSequence.toString().isEmpty()) {
            calendar.setTime(Objects.requireNonNull(sdf.parse(charSequence.toString())));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        dateOfBirth.setError(null);
    }
}