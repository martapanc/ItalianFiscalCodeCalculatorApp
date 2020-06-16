package com.example.fiscalcode_java.ui.main.compute;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.fiscalcode_java.R;
import com.example.fiscalcode_java.exception.FiscalCodeComputationException;
import com.example.fiscalcode_java.fiscalcode.computations.ComputeFiscalCode;
import com.example.fiscalcode_java.fiscalcode.models.Town;
import com.example.fiscalcode_java.fiscalcode.models.Country;
import com.example.fiscalcode_java.fiscalcode.models.InputField;
import com.example.fiscalcode_java.fiscalcode.utils.FragmentHelper;
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

        FragmentHelper.setupGenderRadioButtons(root);
        setupDateOfBirth(this, root);

        String[] placesOfBirth = model.getPlaceList(context);
        ArrayAdapter<String> pobArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, placesOfBirth);
        FragmentHelper.setupPlaceOfBirth(root, pobArrayAdapter);

        Button computeButton = root.findViewById(R.id.compute_button);
        computeButton.setOnClickListener(validateFieldsAndCompute(placesOfBirth));
        return root;
    }

    public static void setupDateOfBirth(ComputeFragment computeFragment, View root) {
        final EditText dateOfBirth = root.findViewById(R.id.dateOfBirth_editText);
        dateOfBirth.setRawInputType(InputType.TYPE_NULL);
        dateOfBirth.setOnClickListener(computeFragment.dobOnClickListener());
        dateOfBirth.addTextChangedListener(computeFragment.getDobWatcher(dateOfBirth));
    }

    public View.OnClickListener validateFieldsAndCompute(final String[] placesOfBirth) {
        return view -> {
            boolean allFieldsValid = true;
            FragmentActivity activity = Objects.requireNonNull(getActivity());

            EditText firstNameEditText = activity.findViewById(R.id.first_name);
            EditText lastNameEditText = activity.findViewById(R.id.last_name);
            RadioButton maleRadioButton = activity.findViewById(R.id.maleRadioButton);
            RadioButton femaleRadioButton = activity.findViewById(R.id.femaleRadioButton);
            EditText dobEditText = activity.findViewById(R.id.dateOfBirth_editText);
            AutoCompleteTextView pobTextView = activity.findViewById(R.id.pob_autocompleteTextView);

            allFieldsValid = InputField.FIRST_NAME.validateField(firstNameEditText, allFieldsValid, placesOfBirth, this);
            allFieldsValid = InputField.LAST_NAME.validateField(lastNameEditText, allFieldsValid, placesOfBirth, this);
            allFieldsValid = InputField.validateField(this, femaleRadioButton, maleRadioButton, allFieldsValid);
            allFieldsValid = InputField.DATE_OF_BIRTH.validateField(dobEditText, allFieldsValid, placesOfBirth, this);
            allFieldsValid = InputField.PLACE_OF_BIRTH.validateField(pobTextView, allFieldsValid, placesOfBirth, this);

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
                    FragmentHelper.hideVirtualKeyboard(view);

                    TextView outputTextView = activity.findViewById(R.id.fiscalCodeOutput);
                    outputTextView.setPadding(10, 5, 10, 5);
                    outputTextView.setText(fiscalCode);
                } catch (IOException | InterruptedException | FiscalCodeComputationException e) {
                    Toast.makeText(activity.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private View.OnClickListener dobOnClickListener() {
        return view -> {
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
                //TODO: handle exceptions like this
                if (charSequence != null && !charSequence.toString().isEmpty()) {
                    calendar.setTime(Objects.requireNonNull(sdf.parse(charSequence.toString())));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                dateOfBirth.setError(null);
            }
        };
    }

    private Calendar initCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -30);
        return cal;
    }
}
