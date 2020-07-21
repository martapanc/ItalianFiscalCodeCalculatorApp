package com.example.fiscalcode_java.ui.main.extract;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import com.example.fiscalcode_java.fiscalcode.computations.ExtractDataFromFiscalCodeHelper;
import com.example.fiscalcode_java.fiscalcode.computations.ValidateInputFields;
import com.example.fiscalcode_java.fiscalcode.models.Country;
import com.example.fiscalcode_java.fiscalcode.models.FiscalCodeData;
import com.example.fiscalcode_java.fiscalcode.models.Gender;
import com.example.fiscalcode_java.fiscalcode.models.InputField;
import com.example.fiscalcode_java.fiscalcode.models.Town;
import com.google.android.material.snackbar.Snackbar;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import lombok.SneakyThrows;

import static com.example.fiscalcode_java.fiscalcode.constants.ErrorMapConstants.getErrorMap;

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

        EditText fiscalCodeEditText = root.findViewById(R.id.ext_fiscalCodeInput_input);
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

        setupSpeedDial(root);

        return root;
    }

    private View.OnClickListener getOnClickListener(List<Town> townList, List<Country> countryList) {
        return view -> {
            EditText fiscalCodeEditText = Objects.requireNonNull(getActivity()).findViewById(R.id.ext_fiscalCodeInput_input);
            String fiscalCodeInput = fiscalCodeEditText.getText().toString();

            if (!fiscalCodeInput.isEmpty()) {
                if (ValidateInputFields.isFieldValid(fiscalCodeInput, InputField.FISCAL_CODE, null)) {
                    try {
                        //TODO: offer to edit first and last name
                        FiscalCodeData fiscalCodeData = ExtractDataFromFiscalCodeHelper.extractData(fiscalCodeInput, townList, countryList);
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
        TextView firstNameText = activity.findViewById(R.id.ext_first_name_text);
        firstNameText.setText(fiscalCodeData.getFirstNameCode());

        TextView lastNameText = activity.findViewById(R.id.ext_last_name_text);
        lastNameText.setText(fiscalCodeData.getLastNameCode());

        TextView genderText = activity.findViewById(R.id.ext_gender_text);
        genderText.setText(fiscalCodeData.getGender().toString());

        TextView dobText = activity.findViewById(R.id.ext_dob_text);
        dobText.setText(fiscalCodeData.getDateOfBirth());

        TextView pobText = activity.findViewById(R.id.ext_pob_text);
        pobText.setText(fiscalCodeData.getPlaceOfBirth());
    }

    private void setupSpeedDial(View root) {
        SpeedDialView speedDialView = root.findViewById(R.id.ext_speedDial);

        final int color = root.getResources().getColor(R.color.colorAccent, null);
        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_copy, R.drawable.ic_content_copy_24px)
                .setFabBackgroundColor(color)
                .create());
        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_send, R.drawable.ic_share_24px)
                .setFabBackgroundColor(color)
                .create());

        speedDialView.setOnActionSelectedListener(actionItem -> {
            switch (actionItem.getId()) {
                case R.id.fab_copy:
                    copyFunction(root);
                    break;
                case R.id.fab_send:
                    shareFunction(root);
                    break;
                default:
                    return false;
            }
            return false;
        });
    }

    private FiscalCodeData getData(View root) {
        EditText fiscalCodeInput = root.findViewById(R.id.ext_fiscalCodeInput_input);
        TextView firstName = root.findViewById(R.id.ext_first_name_text);
        TextView lastName = root.findViewById(R.id.ext_last_name_text);
        TextView gender = root.findViewById(R.id.ext_gender_text);
        TextView dob = root.findViewById(R.id.ext_dob_text);
        TextView pob = root.findViewById(R.id.ext_pob_text);
        return FiscalCodeData.builder()
                .fiscalCode(fiscalCodeInput.getText().toString())
                .firstNameCode(firstName.getText().toString())
                .lastNameCode(lastName.getText().toString())
                .gender(gender.getText().toString().equals("F") ? Gender.F : Gender.M)
                .dateOfBirth(dob.getText().toString())
                .placeOfBirth(pob.getText().toString())
                .build();
    }

    private void copyFunction(View root) {
        FiscalCodeData fiscalCodeData = getData(root);
        String message;
        final Resources resources = getContext().getResources();
        if (fiscalCodeData.areFieldsNotEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Data", fiscalCodeData.formatData(resources));
            clipboard.setPrimaryClip(clipData);
            message = resources.getString(R.string.data_copied_to_clipboard);
        } else {
            message = resources.getString(R.string.nothing_to_copy);
        }
        Snackbar.make(root, message, Snackbar.LENGTH_LONG)
                .setAction("action", null)
                .show();
    }

    private void shareFunction(View root) {
        FiscalCodeData fiscalCodeData = getData(root);
        final Resources resources = getContext().getResources();
        if (fiscalCodeData.areFieldsNotEmpty()) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, fiscalCodeData.formatData(resources));
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            getContext().startActivity(shareIntent);
        } else {
            Snackbar.make(root, resources.getString(R.string.nothing_to_copy), Snackbar.LENGTH_LONG)
                    .setAction("action", null)
                    .show();
        }
    }
}
