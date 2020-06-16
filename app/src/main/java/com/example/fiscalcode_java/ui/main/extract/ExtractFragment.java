package com.example.fiscalcode_java.ui.main.extract;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
                        Toast.makeText(getActivity().getApplicationContext(), fiscalCodeData.toString(), Toast.LENGTH_LONG).show();
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
}
