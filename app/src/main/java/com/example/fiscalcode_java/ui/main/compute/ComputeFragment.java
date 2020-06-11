package com.example.fiscalcode_java.ui.main.compute;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.fiscalcode_java.R;
import com.example.fiscalcode_java.fiscalCode.computations.ComputeFiscalCode;
import com.example.fiscalcode_java.fiscalCode.utils.ReadTownList;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import lombok.SneakyThrows;

public class ComputeFragment extends Fragment {

    public static final String TOWNS_FILE = "comuni.json";
    private int year, month, day;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SneakyThrows
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Locale.setDefault(Locale.ITALY);
        View root = inflater.inflate(R.layout.fragment_compute, container, false);

        EditText dateOfBirth = root.findViewById(R.id.dateOfBirth_editText);
        dateOfBirth.setRawInputType(InputType.TYPE_NULL);
        dateOfBirth.setOnClickListener(dobOnClickListener());

        AutoCompleteTextView autoCompleteTextView = root.findViewById(R.id.pob_autocompleteTextView);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hideVirtualKeyboard(adapterView);
            }
        });

        String[] towns = ReadTownList.readTownNameList(getContext().getAssets().open(TOWNS_FILE));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, towns);
        autoCompleteTextView.setAdapter(arrayAdapter);

        Button computeButton = root.findViewById(R.id.compute_button);
        computeButton.setOnClickListener(validateFieldsAndCompute());
        return root;
    }

    private View.OnClickListener validateFieldsAndCompute() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allFieldsValid = true;
                EditText firstNameEditText = getActivity().findViewById(R.id.first_name);
                EditText lastNameEditText = getActivity().findViewById(R.id.last_name);
                RadioButton maleRadioButton = getActivity().findViewById(R.id.maleRadioButton);
                RadioButton femaleRadioButton = getActivity().findViewById(R.id.femaleRadioButton);
                EditText dobEditText = getActivity().findViewById(R.id.dateOfBirth_editText);
                AutoCompleteTextView pobTextView = getActivity().findViewById(R.id.pob_autocompleteTextView);

                allFieldsValid = validateField(allFieldsValid, firstNameEditText);
                allFieldsValid = validateField(allFieldsValid, lastNameEditText);

                maleRadioButton.setError(null);
                femaleRadioButton.setError(null);
                if (!maleRadioButton.isChecked() && !femaleRadioButton.isChecked()) {
                    allFieldsValid = false;
                    femaleRadioButton.setError(getString(R.string.empty_field_error));
                    femaleRadioButton.requestFocus();
                }

                dobEditText.setError(null);
                allFieldsValid = validateField(allFieldsValid, dobEditText);

                pobTextView.setError(null);
                allFieldsValid = validateField(allFieldsValid, pobTextView);

                if (allFieldsValid) {
                    String firstName = firstNameEditText.getText().toString();
                    String lastName = lastNameEditText.getText().toString();
                    String gender = maleRadioButton.isChecked() ? "m" : "f";
                    String dob = dobEditText.getText().toString();
                    String pob = pobTextView.getText().toString();

                    try {
                        String fiscalCode = ComputeFiscalCode.compute(firstName, lastName, dob, gender, pob, ReadTownList.read(getActivity().getAssets().open(TOWNS_FILE)));
                        hideVirtualKeyboard(view);

                        TextView outputTextView = getActivity().findViewById(R.id.fiscalCodeOutput);
                        outputTextView.setPadding(10, 5, 10, 5);
                        outputTextView.setText(fiscalCode);
                    } catch (IOException | InterruptedException e) {
                        Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
    }

    private void hideVirtualKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private boolean validateField(boolean allFieldsValid, EditText firstNameEditText) {
        if (firstNameEditText.getText().toString().equals("")) {
            allFieldsValid = false;
            firstNameEditText.setError(getString(R.string.empty_field_error));
            firstNameEditText.requestFocus();
        }
        return allFieldsValid;
    }

    private View.OnClickListener dobOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();

                cal.add(Calendar.YEAR, -30);

                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);

                final TextView dateTextView = getActivity().findViewById(R.id.dateOfBirth_editText);
                FragmentActivity fragmentActivity = ComputeFragment.this.getActivity();
                DatePickerDialog datePickerDialog = new DatePickerDialog(fragmentActivity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
                        Calendar c = Calendar.getInstance();
                        c.set(y, m, d);
                        dateTextView.setText(sdf.format(c.getTime()));
                    }
                }, year, month, day);

                cal.add(Calendar.YEAR, -90);

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                datePickerDialog.setTitle(getString(R.string.dob_label));
                datePickerDialog.show();
            }
        };
    }
}
