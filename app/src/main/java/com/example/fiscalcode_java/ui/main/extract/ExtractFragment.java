package com.example.fiscalcode_java.ui.main.extract;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fiscalcode_java.R;
import com.example.fiscalcode_java.fiscalcode.computations.ValidateInputFields;
import com.example.fiscalcode_java.fiscalcode.models.InputField;

import java.util.Locale;
import java.util.Objects;

public class ExtractFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Locale.setDefault(Locale.ITALY);
        View root = inflater.inflate(R.layout.fragment_extract, container, false);

        Button computeButton = root.findViewById(R.id.compute_button);
        computeButton.setOnClickListener(view -> {
            EditText fiscalCodeEditText = Objects.requireNonNull(getActivity()).findViewById(R.id.fiscalCodeInputEditText);
            String fiscalCodeInput = fiscalCodeEditText.getText().toString();
            if (!fiscalCodeInput.isEmpty()) {
                if (ValidateInputFields.isFieldValid(fiscalCodeInput, InputField.FISCAL_CODE, null)) {

                } else {
                    fiscalCodeEditText.setError(getString(R.string.invalid_fiscal_code_input_error));
                }
            } else {
                fiscalCodeEditText.setError(getString(R.string.empty_field_error));
                fiscalCodeEditText.requestFocus();
            }
        });
        return root;
    }
}
