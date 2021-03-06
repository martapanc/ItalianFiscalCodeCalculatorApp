package com.pancaldim.fiscalcode_app.ui.main.listener;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.pancaldim.fiscalcode_app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.pancaldim.fiscalcode_app.fiscalcode.constants.DateFormatAndLocaleConstants.DD_MM_YYYY;
import static com.pancaldim.fiscalcode_app.fiscalcode.constants.DateFormatAndLocaleConstants.minYear;

public class DateOfBirthOnClickListener implements View.OnClickListener {

    private static final int YEAR_CLICKABLE_INDEX = 0;
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

        FragmentActivity fragmentActivity = (FragmentActivity) view.getContext();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        final TextView dateTextView = view.findViewById(dobInputId);
        DatePickerDialog datePickerDialog = new DatePickerDialog(fragmentActivity,
                (datePicker, y, m, d) -> {
                    SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YYYY, Locale.ITALY);
                    Calendar c = Calendar.getInstance();
                    c.set(y, m, d);
                    dateTextView.setText(sdf.format(c.getTime()));
                }, year, month, day);

        cal.set(minYear, 0, 1);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        datePickerDialog.getDatePicker().getTouchables().get(YEAR_CLICKABLE_INDEX).performClick();
        datePickerDialog.setTitle(view.getContext().getString(R.string.dob_label));
        datePickerDialog.show();
    }
}
