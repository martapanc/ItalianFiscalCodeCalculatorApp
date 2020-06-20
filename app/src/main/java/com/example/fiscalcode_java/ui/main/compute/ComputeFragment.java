package com.example.fiscalcode_java.ui.main.compute;

import android.content.Context;
import android.os.Bundle;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import lombok.SneakyThrows;

import static com.example.fiscalcode_java.fiscalcode.utils.FragmentHelper.initCalendar;
import static com.example.fiscalcode_java.fiscalcode.utils.FragmentHelper.setupDateOfBirth;
import static com.example.fiscalcode_java.fiscalcode.utils.FragmentHelper.setupGenderRadioButtons;
import static com.example.fiscalcode_java.fiscalcode.utils.FragmentHelper.setupPlaceOfBirth;

public class ComputeFragment extends Fragment {

    //TODO: implement localisation
    //TODO: Info button

    private static Calendar computeCalendar = initCalendar();

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
        setupDateOfBirth(root, computeCalendar);

        String[] placesOfBirth = model.getPlaceList(context);
        ArrayAdapter<String> pobArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, placesOfBirth);
        setupPlaceOfBirth(root, pobArrayAdapter);

        Button computeButton = root.findViewById(R.id.compute_button);
        computeButton.setOnClickListener(validateFieldsAndCompute(placesOfBirth));

        ImageButton resetButton = root.findViewById(R.id.compute_reset_button);
        resetButton.setOnClickListener(getResetListener());
        return root;
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

    public View.OnClickListener getResetListener() {
        return view -> {
            FragmentActivity activity = Objects.requireNonNull(getActivity());

            EditText firstName = activity.findViewById(R.id.first_name);
            firstName.setText("");
            firstName.setError(null);
            EditText lastName = activity.findViewById(R.id.last_name);
            lastName.setText("");
            lastName.setError(null);
            RadioButton maleRadio = activity.findViewById(R.id.maleRadioButton);
            maleRadio.setChecked(false);
            maleRadio.setError(null);
            RadioButton femaleRadio = activity.findViewById(R.id.femaleRadioButton);
            femaleRadio.setChecked(false);
            femaleRadio.setError(null);
            EditText dob = activity.findViewById(R.id.dateOfBirth_editText);
            dob.setText("");
            dob.setError(null);
            AutoCompleteTextView pob = activity.findViewById(R.id.pob_autocompleteTextView);
            pob.setText("");
            pob.setError(null);
        };
    }
}
