package com.pancaldim.fiscalcode.ui.main.verify;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.pancaldim.fiscalcode.R;
import com.pancaldim.fiscalcode.exception.FiscalCodeComputationException;
import com.pancaldim.fiscalcode.fiscalcode.computations.ComputeFiscalCodeHelper;
import com.pancaldim.fiscalcode.fiscalcode.models.Country;
import com.pancaldim.fiscalcode.fiscalcode.models.Town;
import com.pancaldim.fiscalcode.fiscalcode.utils.ReadTownListHelper;
import com.pancaldim.fiscalcode.ui.main.compute.ComputeViewModel;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import lombok.SneakyThrows;

import static com.pancaldim.fiscalcode.fiscalcode.constants.ErrorMapConstants.getErrorMap;
import static com.pancaldim.fiscalcode.fiscalcode.models.InputField.DATE_OF_BIRTH;
import static com.pancaldim.fiscalcode.fiscalcode.models.InputField.FIRST_NAME;
import static com.pancaldim.fiscalcode.fiscalcode.models.InputField.FISCAL_CODE;
import static com.pancaldim.fiscalcode.fiscalcode.models.InputField.LAST_NAME;
import static com.pancaldim.fiscalcode.fiscalcode.models.InputField.PLACE_OF_BIRTH;
import static com.pancaldim.fiscalcode.fiscalcode.models.InputField.validateField;
import static com.pancaldim.fiscalcode.fiscalcode.utils.FragmentHelper.hideVirtualKeyboard;
import static com.pancaldim.fiscalcode.fiscalcode.utils.FragmentHelper.initCalendar;
import static com.pancaldim.fiscalcode.fiscalcode.utils.FragmentHelper.setupDateOfBirth;
import static com.pancaldim.fiscalcode.fiscalcode.utils.FragmentHelper.setupGenderRadioButtons;
import static com.pancaldim.fiscalcode.fiscalcode.utils.FragmentHelper.setupPlaceOfBirth;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class VerifyFragment extends Fragment {

    private static final Calendar verifyCalendar = initCalendar();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SneakyThrows
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_verify, container, false);
        Context context = requireContext();
        ComputeViewModel model = new ViewModelProvider(requireActivity()).get(ComputeViewModel.class);

        setupGenderRadioButtons(root, R.id.ver_maleRadioButton, R.id.ver_femaleRadioButton);
        setupDateOfBirth(root, verifyCalendar, R.id.ver_dob_input);

        EditText fiscalCodeEditText = root.findViewById(R.id.ver_fiscalCodeInput_input);
        fiscalCodeEditText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        String[] placesOfBirth = model.getPlaceList(context);
        ArrayAdapter<String> pobArrayAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, placesOfBirth);
        setupPlaceOfBirth(root, pobArrayAdapter, R.id.ver_pob_input);

        Button extractButton = root.findViewById(R.id.verify_button);
        extractButton.setOnClickListener(validateFieldsAndCompute(placesOfBirth));

        ImageButton resetButton = root.findViewById(R.id.verify_reset_button);
        resetButton.setOnClickListener(getResetListener());
        return root;
    }

    private View.OnClickListener validateFieldsAndCompute(String[] placesOfBirth) {
        return view -> {
            FragmentActivity activity = requireActivity();

            EditText firstNameEditText = activity.findViewById(R.id.ver_first_name_input);
            EditText lastNameEditText = activity.findViewById(R.id.ver_last_name_input);
            RadioButton maleRadioButton = activity.findViewById(R.id.ver_maleRadioButton);
            RadioButton femaleRadioButton = activity.findViewById(R.id.ver_femaleRadioButton);
            TextView dobEditText = activity.findViewById(R.id.ver_dob_input);
            AutoCompleteTextView pobTextView = activity.findViewById(R.id.ver_pob_input);
            EditText fiscalCodeEditText = activity.findViewById(R.id.ver_fiscalCodeInput_input);

            boolean allFieldsValid = FIRST_NAME.validateField(firstNameEditText, placesOfBirth, this)
                    & LAST_NAME.validateField(lastNameEditText, placesOfBirth, this)
                    & validateField(this, femaleRadioButton, maleRadioButton)
                    & DATE_OF_BIRTH.validateField(dobEditText, this)
                    & PLACE_OF_BIRTH.validateField(pobTextView, placesOfBirth, this)
                    & FISCAL_CODE.validateField(fiscalCodeEditText, placesOfBirth, this);

            if (allFieldsValid) {
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String gender = maleRadioButton.isChecked() ? "m" : "f";
                String dob = dobEditText.getText().toString();
                String pob = pobTextView.getText().toString();
                String fiscalCode = fiscalCodeEditText.getText().toString();

                try {
                    List<Town> towns = ReadTownListHelper.readTowns(activity.getAssets().open(ComputeViewModel.TOWNS_FILE));
                    List<Country> countries = ReadTownListHelper.readCountries(activity.getAssets().open(ComputeViewModel.COUNTRIES_FILE));
                    String computedFiscalCode = ComputeFiscalCodeHelper.compute(firstName, lastName, dob, gender, pob, towns, countries);

                    hideVirtualKeyboard(view);
                    TextView outputTextView = activity.findViewById(R.id.ver_fiscalCodeOutput);
                    outputTextView.setPadding(10, 5, 10, 5);

                    if (computedFiscalCode.equals(fiscalCode.toUpperCase())) {
                        outputTextView.setText(R.string.fiscal_code_is_correct);
                        outputTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_outline_24px, 0, 0, 0);
                        outputTextView.setCompoundDrawablePadding(activity.getResources().getDimensionPixelOffset(R.dimen.small_padding));
                    } else {
                        outputTextView.setText(R.string.fiscal_code_is_not_correct);
                        outputTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error_outline_24px, 0, 0, 0);
                        outputTextView.setCompoundDrawablePadding(activity.getResources().getDimensionPixelOffset(R.dimen.small_padding));
                        outputTextView.setTextSize(18);
                    }
                } catch (IOException | InterruptedException | FiscalCodeComputationException e) {
                    int errorMessageId;
                    try {
                        errorMessageId = getErrorMap().get(e.getMessage());
                    } catch (NullPointerException npe) {
                        errorMessageId = R.string.err_unknown;
                    }
                    Toast.makeText(activity.getApplicationContext(), errorMessageId, Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    public View.OnClickListener getResetListener() {
        return view -> {
            FragmentActivity activity = requireActivity();

            EditText fiscalCode = activity.findViewById(R.id.ver_fiscalCodeInput_input);
            fiscalCode.setText(EMPTY);
            fiscalCode.setError(null);
            EditText firstName = activity.findViewById(R.id.ver_first_name_input);
            firstName.setText(EMPTY);
            firstName.setError(null);
            EditText lastName = activity.findViewById(R.id.ver_last_name_input);
            lastName.setText(EMPTY);
            lastName.setError(null);
            RadioGroup radioGroup = activity.findViewById(R.id.ver_radioGroup);
            radioGroup.clearCheck();
            RadioButton maleRadio = activity.findViewById(R.id.ver_maleRadioButton);
            maleRadio.setError(null);
            RadioButton femaleRadio = activity.findViewById(R.id.ver_femaleRadioButton);
            femaleRadio.setError(null);
            TextView dob = activity.findViewById(R.id.ver_dob_input);
            dob.setText(EMPTY);
            dob.setError(null);
            AutoCompleteTextView pob = activity.findViewById(R.id.ver_pob_input);
            pob.setText(EMPTY);
            pob.setError(null);
        };
    }
}
