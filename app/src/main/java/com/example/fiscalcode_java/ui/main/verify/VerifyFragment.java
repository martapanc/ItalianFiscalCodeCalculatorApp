package com.example.fiscalcode_java.ui.main.verify;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.fiscalcode_java.R;
import com.example.fiscalcode_java.exception.FiscalCodeComputationException;
import com.example.fiscalcode_java.fiscalcode.computations.ComputeFiscalCode;
import com.example.fiscalcode_java.fiscalcode.models.Country;
import com.example.fiscalcode_java.fiscalcode.models.Town;
import com.example.fiscalcode_java.fiscalcode.utils.ReadTownList;
import com.example.fiscalcode_java.ui.main.compute.ComputeViewModel;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import lombok.SneakyThrows;

import static com.example.fiscalcode_java.fiscalcode.models.InputField.DATE_OF_BIRTH;
import static com.example.fiscalcode_java.fiscalcode.models.InputField.FIRST_NAME;
import static com.example.fiscalcode_java.fiscalcode.models.InputField.FISCAL_CODE;
import static com.example.fiscalcode_java.fiscalcode.models.InputField.LAST_NAME;
import static com.example.fiscalcode_java.fiscalcode.models.InputField.PLACE_OF_BIRTH;
import static com.example.fiscalcode_java.fiscalcode.models.InputField.validateField;
import static com.example.fiscalcode_java.fiscalcode.utils.FragmentHelper.hideVirtualKeyboard;
import static com.example.fiscalcode_java.fiscalcode.utils.FragmentHelper.initCalendar;
import static com.example.fiscalcode_java.fiscalcode.utils.FragmentHelper.setupDateOfBirth;
import static com.example.fiscalcode_java.fiscalcode.utils.FragmentHelper.setupFiscalCode;
import static com.example.fiscalcode_java.fiscalcode.utils.FragmentHelper.setupGenderRadioButtons;
import static com.example.fiscalcode_java.fiscalcode.utils.FragmentHelper.setupPlaceOfBirth;

public class VerifyFragment extends Fragment {

    private static Calendar verifyCalendar = initCalendar();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SneakyThrows
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_verify, container, false);
        Context context = Objects.requireNonNull(getContext());
        ComputeViewModel model = ViewModelProviders.of(requireActivity()).get(ComputeViewModel.class);

        setupGenderRadioButtons(root);
        setupDateOfBirth(root, verifyCalendar);
        setupFiscalCode(root);

        String[] placesOfBirth = model.getPlaceList(context);
        ArrayAdapter<String> pobArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, placesOfBirth);
        setupPlaceOfBirth(root, pobArrayAdapter, false);

        Button extractButton = root.findViewById(R.id.verify_button);
        extractButton.setOnClickListener(validateFieldsAndCompute(placesOfBirth));
        return root;
    }

    private View.OnClickListener validateFieldsAndCompute(String[] placesOfBirth) {
        return view -> {
            boolean allFieldsValid = true;
            FragmentActivity activity = Objects.requireNonNull(getActivity());

            EditText firstNameEditText = activity.findViewById(R.id.first_name);
            EditText lastNameEditText = activity.findViewById(R.id.last_name);
            RadioButton maleRadioButton = activity.findViewById(R.id.maleRadioButton);
            RadioButton femaleRadioButton = activity.findViewById(R.id.femaleRadioButton);
            EditText dobEditText = activity.findViewById(R.id.dateOfBirth_editText);
            AutoCompleteTextView pobTextView = activity.findViewById(R.id.pob_autocompleteTextView);
            EditText fiscalCodeEditText = activity.findViewById(R.id.fiscalCodeInputEditText);

            allFieldsValid = FIRST_NAME.validateField(firstNameEditText, allFieldsValid, placesOfBirth, this);
            allFieldsValid = LAST_NAME.validateField(lastNameEditText, allFieldsValid, placesOfBirth, this);
            allFieldsValid = validateField(this, femaleRadioButton, maleRadioButton, allFieldsValid);
            allFieldsValid = DATE_OF_BIRTH.validateField(dobEditText, allFieldsValid, placesOfBirth, this);
            allFieldsValid = PLACE_OF_BIRTH.validateField(pobTextView, allFieldsValid, placesOfBirth, this);
            allFieldsValid = FISCAL_CODE.validateField(fiscalCodeEditText, allFieldsValid, placesOfBirth, this);

            if (allFieldsValid) {
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String gender = maleRadioButton.isChecked() ? "m" : "f";
                String dob = dobEditText.getText().toString();
                String pob = pobTextView.getText().toString();
                String fiscalCode = fiscalCodeEditText.getText().toString();

                try {
                    List<Town> towns = ReadTownList.readTowns(activity.getAssets().open(ComputeViewModel.TOWNS_FILE));
                    List<Country> countries = ReadTownList.readCountries(activity.getAssets().open(ComputeViewModel.COUNTRIES_FILE));
                    String computedFiscalCode = ComputeFiscalCode.compute(firstName, lastName, dob, gender, pob, towns, countries);

                    hideVirtualKeyboard(view);
                    TextView outputTextView = activity.findViewById(R.id.fiscalCodeOutput);
                    outputTextView.setPadding(10, 5, 10, 5);

                    if (computedFiscalCode.equals(fiscalCode)) {
                        outputTextView.setText(R.string.fiscal_code_is_correct);
                    } else {
                        outputTextView.setText(R.string.fiscal_code_is_not_correct);
                    }
                } catch (IOException | InterruptedException | FiscalCodeComputationException e) {
                    Toast.makeText(activity.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        };
    }
}
