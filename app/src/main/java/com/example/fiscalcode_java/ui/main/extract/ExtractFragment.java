package com.example.fiscalcode_java.ui.main.extract;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.fiscalcode_java.R;
import com.example.fiscalcode_java.exception.FiscalCodeExtractionException;
import com.example.fiscalcode_java.fiscalcode.computations.ExtractDataFromFiscalCode;
import com.example.fiscalcode_java.fiscalcode.computations.ValidateInputFields;
import com.example.fiscalcode_java.fiscalcode.models.Country;
import com.example.fiscalcode_java.fiscalcode.models.FiscalCodeData;
import com.example.fiscalcode_java.fiscalcode.models.InputField;
import com.example.fiscalcode_java.fiscalcode.models.Town;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import lombok.SneakyThrows;

import static com.example.fiscalcode_java.fiscalcode.constants.ErrorMap.getErrorMap;

public class ExtractFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SneakyThrows
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Locale.setDefault(Locale.ITALY);
        View root = inflater.inflate(R.layout.fragment_extract, container, false);
        final Context context = Objects.requireNonNull(getContext());
        ExtractViewModel viewModel = ViewModelProviders.of(requireActivity()).get(ExtractViewModel.class);

        EditText fiscalCodeEditText = root.findViewById(R.id.fiscalCodeInputEditText);
        fiscalCodeEditText.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        List<Town> townList = viewModel.getTownList(context);
        List<Country> countryList = viewModel.getCountryList(context);

        Button computeButton = root.findViewById(R.id.extract_data_button);
        computeButton.setOnClickListener(getOnClickListener(townList, countryList));

        ImageButton resetButton = root.findViewById(R.id.extract_reset_button);
        resetButton.setOnClickListener(view -> {
            fiscalCodeEditText.setText("");
            fiscalCodeEditText.setError(null);
        });
        return root;
    }

    private View.OnClickListener getOnClickListener(List<Town> townList, List<Country> countryList) {
        return view -> {
            EditText fiscalCodeEditText = Objects.requireNonNull(getActivity()).findViewById(R.id.fiscalCodeInputEditText);
            String fiscalCodeInput = fiscalCodeEditText.getText().toString();

            if (!fiscalCodeInput.isEmpty()) {
                if (ValidateInputFields.isFieldValid(fiscalCodeInput, InputField.FISCAL_CODE, null)) {
                    try {
                        FiscalCodeData fiscalCodeData = ExtractDataFromFiscalCode.extractData(fiscalCodeInput, townList, countryList);
                        showFiscalCodeData(Objects.requireNonNull(getActivity()), fiscalCodeData);
                    } catch (FiscalCodeExtractionException e) {
                        int errorMessageId;
                        try {
                            errorMessageId = getErrorMap().get(e.getMessage());
                        } catch (NullPointerException npe) {
                            errorMessageId = R.string.err_unknown;
                        }
                        Toast.makeText(getActivity().getApplicationContext(), errorMessageId, Toast.LENGTH_LONG).show();
                    }
                } else {
                    fiscalCodeEditText.setError(getString(R.string.invalid_fiscal_code_input_error));
                }
            } else {
                fiscalCodeEditText.setError(getString(R.string.empty_field_error));
                fiscalCodeEditText.requestFocus();
            }
        };
    }

    private void showFiscalCodeData(FragmentActivity activity, FiscalCodeData fiscalCodeData) {
        TextView firstNameText = activity.findViewById(R.id.first_name_text);
        firstNameText.setText(fiscalCodeData.getFirstNameCode());

        TextView lastNameText = activity.findViewById(R.id.last_name_text);
        lastNameText.setText(fiscalCodeData.getLastNameCode());

        TextView genderText = activity.findViewById(R.id.gender_text);
        genderText.setText(fiscalCodeData.getGender().toString());

        TextView dobText = activity.findViewById(R.id.dob_text);
        dobText.setText(fiscalCodeData.getDateOfBirth());

        TextView pobText = activity.findViewById(R.id.pob_text);
        pobText.setText(fiscalCodeData.getPlaceOfBirth());
    }
}
