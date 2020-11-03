package com.pancaldim.fiscalcode_app.ui.main.listener;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.view.View;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;
import com.pancaldim.fiscalcode_app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.pancaldim.fiscalcode_app.fiscalcode.constants.DateFormatAndLocaleConstants.DD_MM_YYYY;
import static com.pancaldim.fiscalcode_app.fiscalcode.constants.DateFormatAndLocaleConstants.minYear;

public class DateOfBirthOnClickListener implements View.OnClickListener {

    private final Calendar calendar;
    private final int dobInputId;

    public DateOfBirthOnClickListener(Calendar calendar, int dobInputId) {
        this.calendar = calendar;
        this.dobInputId = dobInputId;
    }

    @Override
    public void onClick(View view) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(calendar.getTime());

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.build();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            view.getContext(),
            R.style.SpinnerDatePickerStyle,
            getOnDateSetListener(view.findViewById(dobInputId)),
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        );

        cal.set(minYear, 0, 1);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        datePickerDialog.setTitle(view.getContext().getString(R.string.dob_label));
        datePickerDialog.show();
    }

    public DatePickerDialog.OnDateSetListener getOnDateSetListener(TextView dateTextView) {
        return (datePicker, year, month, day) -> {
            SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YYYY, Locale.ITALY);
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            dateTextView.setText(sdf.format(cal.getTime()));
        };
    }
}
