package com.example.fiscalcode_java.ui.main.extract;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
                        FragmentActivity activity = Objects.requireNonNull(getActivity());

                        showFiscalCodeData(activity, fiscalCodeData);
                    } catch (FiscalCodeExtractionException e) {
                        Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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
        TextView firstNameLabel = activity.findViewById(R.id.first_name_label);
        firstNameLabel.setVisibility(View.VISIBLE);
        TextView firstNameText = activity.findViewById(R.id.first_name_text);
        firstNameText.setText(fiscalCodeData.getFirstNameCode());

        TextView lastNameLabel = activity.findViewById(R.id.last_name_label);
        lastNameLabel.setVisibility(View.VISIBLE);
        TextView lastNameText = activity.findViewById(R.id.last_name_text);
        lastNameText.setText(fiscalCodeData.getLastNameCode());

        TextView genderLabel = activity.findViewById(R.id.gender_label);
        genderLabel.setVisibility(View.VISIBLE);
        TextView genderText = activity.findViewById(R.id.gender_text);
        genderText.setText(fiscalCodeData.getGender().toString());

        TextView dobLabel = activity.findViewById(R.id.dob_label);
        dobLabel.setVisibility(View.VISIBLE);
        TextView dobText = activity.findViewById(R.id.dob_text);
        dobText.setText(fiscalCodeData.getDateOfBirth());

        TextView pobLabel = activity.findViewById(R.id.pob_label);
        pobLabel.setVisibility(View.VISIBLE);
        TextView pobText = activity.findViewById(R.id.pob_text);
        pobText.setText(fiscalCodeData.getPlaceOfBirth());
    }
}
