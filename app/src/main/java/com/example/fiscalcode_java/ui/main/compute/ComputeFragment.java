package com.example.fiscalcode_java.ui.main.compute;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.*;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.fiscalcode_java.R;
import com.example.fiscalcode_java.exception.FiscalCodeComputationException;
import com.example.fiscalcode_java.fiscalcode.computations.ComputeFiscalCode;
import com.example.fiscalcode_java.fiscalcode.computations.ValidateInputFields;
import com.example.fiscalcode_java.fiscalcode.models.Town;
import com.example.fiscalcode_java.fiscalcode.models.Country;
import com.example.fiscalcode_java.fiscalcode.models.InputField;
import com.example.fiscalcode_java.fiscalcode.utils.ReadTownList;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import lombok.SneakyThrows;

import static com.example.fiscalcode_java.fiscalcode.constants.DateFormat.DD_MM_YYYY;

public class ComputeFragment extends Fragment {

    //TODO: implement localisation

    private int year, month, day;
    private Calendar calendar = initCalendar();

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
        Context context = Objects.requireNonNull(getContext());
        ComputeViewModel model = ViewModelProviders.of(requireActivity()).get(ComputeViewModel.class);

        setupGenderRadioButtons(root);
        setupDateOfBirth(root);

        String[] placesOfBirth = model.getPlaceList(context);
        ArrayAdapter<String> pobArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, placesOfBirth);
        setupPlaceOfBirth(root, pobArrayAdapter);

        Button computeButton = root.findViewById(R.id.compute_button);
        computeButton.setOnClickListener(validateFieldsAndCompute(placesOfBirth));
        return root;
    }

    private void setupGenderRadioButtons(View root) {
        final RadioButton maleRadioButton = root.findViewById(R.id.maleRadioButton);
        final RadioButton femaleRadioButton = root.findViewById(R.id.femaleRadioButton);
        maleRadioButton.setOnCheckedChangeListener(getRadioButtonListener(maleRadioButton));
        femaleRadioButton.setOnCheckedChangeListener(getRadioButtonListener(femaleRadioButton));
    }

    private void setupPlaceOfBirth(View root, ArrayAdapter<String> pobArrayAdapter) {
        AutoCompleteTextView autoCompletePoBTextView = root.findViewById(R.id.pob_autocompleteTextView);
        autoCompletePoBTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hideVirtualKeyboard(adapterView);
            }
        });
        autoCompletePoBTextView.setAdapter(pobArrayAdapter);
    }

    private void setupDateOfBirth(View root) {
        final EditText dateOfBirth = root.findViewById(R.id.dateOfBirth_editText);
        dateOfBirth.setRawInputType(InputType.TYPE_NULL);
        dateOfBirth.setOnClickListener(dobOnClickListener());
        dateOfBirth.addTextChangedListener(getDobWatcher(dateOfBirth));
    }

    public View.OnClickListener validateFieldsAndCompute(final String[] placesOfBirth) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allFieldsValid = true;
                FragmentActivity activity = Objects.requireNonNull(getActivity());

                EditText firstNameEditText = activity.findViewById(R.id.first_name);
                EditText lastNameEditText = activity.findViewById(R.id.last_name);
                RadioButton maleRadioButton = activity.findViewById(R.id.maleRadioButton);
                RadioButton femaleRadioButton = activity.findViewById(R.id.femaleRadioButton);
                EditText dobEditText = activity.findViewById(R.id.dateOfBirth_editText);
                AutoCompleteTextView pobTextView = activity.findViewById(R.id.pob_autocompleteTextView);

                allFieldsValid = validateField(InputField.FIRST_NAME, firstNameEditText, allFieldsValid, placesOfBirth);
                allFieldsValid = validateField(InputField.LAST_NAME, lastNameEditText, allFieldsValid, placesOfBirth);
                allFieldsValid = validateField(femaleRadioButton, maleRadioButton, allFieldsValid);
                allFieldsValid = validateField(InputField.DATE_OF_BIRTH, dobEditText, allFieldsValid, placesOfBirth);
                allFieldsValid = validateField(InputField.PLACE_OF_BIRTH, pobTextView, allFieldsValid, placesOfBirth);

                if (allFieldsValid) {
                    String firstName = firstNameEditText.getText().toString();
                    String lastName = lastNameEditText.getText().toString();
                    String gender = maleRadioButton.isChecked() ? "m" : "f";
                    String dob = dobEditText.getText().toString();
                    String pob = pobTextView.getText().toString();

                    try {
                        List<Town> towns = ReadTownList.readTowns(activity.getAssets().open(ComputeViewModel.TOWNS_FILE));
                        List<Country> countries = ReadTownList.readCountries(activity.getAssets().open(ComputeViewModel.COUNTRIES_FILE));
                        String fiscalCode = ComputeFiscalCode.compute(firstName, lastName, dob, gender, pob, towns, countries);
                        hideVirtualKeyboard(view);

                        TextView outputTextView = activity.findViewById(R.id.fiscalCodeOutput);
                        outputTextView.setPadding(10, 5, 10, 5);
                        outputTextView.setText(fiscalCode);
                    } catch (IOException | InterruptedException | FiscalCodeComputationException e) {
                        Toast.makeText(activity.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
    }

    private View.OnClickListener dobOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(calendar.getTime());

                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);

                final TextView dateTextView = getActivity().findViewById(R.id.dateOfBirth_editText);
                FragmentActivity fragmentActivity = ComputeFragment.this.getActivity();
                DatePickerDialog datePickerDialog = new DatePickerDialog(fragmentActivity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YYYY, Locale.ITALY);
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

    private TextWatcher getDobWatcher(final EditText dateOfBirth) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @SneakyThrows
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YYYY, Locale.ITALY);
                calendar.setTime(Objects.requireNonNull(sdf.parse(charSequence.toString())));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                dateOfBirth.setError(null);
            }
        };
    }

    private CompoundButton.OnCheckedChangeListener getRadioButtonListener(final RadioButton maleRadioButton) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                maleRadioButton.setError(null);
            }
        };
    }

    private Calendar initCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -30);
        return cal;
    }

    private void hideVirtualKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private boolean validateField(InputField type, EditText editText, boolean allFieldsValid, String[] places) {
        editText.setError(null);
        String input = editText.getText().toString();
        if (input.equals("")) {
            allFieldsValid = false;
            editText.setError(getString(R.string.empty_field_error));
            editText.requestFocus();
        } else if (!ValidateInputFields.isFieldValid(input, type, places)) {
            allFieldsValid = false;
            editText.setError(getString(R.string.invalid_input_error));
            editText.requestFocus();
        }
        return allFieldsValid;
    }

    private boolean validateField(RadioButton femaleRadioButton, RadioButton maleRadioButton, boolean allFieldsValid) {
        maleRadioButton.setError(null);
        femaleRadioButton.setError(null);
        if (!maleRadioButton.isChecked() && !femaleRadioButton.isChecked()) {
            allFieldsValid = false;
            femaleRadioButton.setError(getString(R.string.empty_field_error));
            femaleRadioButton.requestFocus();
        }
        return allFieldsValid;
    }
}
