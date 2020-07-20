package com.example.fiscalcode_java.ui.main.compute;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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
import com.example.fiscalcode_java.fiscalcode.computations.ComputeFiscalCodeHelper;
import com.example.fiscalcode_java.fiscalcode.models.Town;
import com.example.fiscalcode_java.fiscalcode.models.Country;
import com.example.fiscalcode_java.fiscalcode.models.InputField;
import com.example.fiscalcode_java.fiscalcode.utils.FragmentHelper;
import com.example.fiscalcode_java.fiscalcode.utils.ReadTownListHelper;
import com.google.android.material.snackbar.Snackbar;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import lombok.SneakyThrows;

import static com.example.fiscalcode_java.fiscalcode.constants.ErrorMapConstants.getErrorMap;
import static com.example.fiscalcode_java.fiscalcode.utils.FragmentHelper.initCalendar;
import static com.example.fiscalcode_java.fiscalcode.utils.FragmentHelper.setupDateOfBirth;
import static com.example.fiscalcode_java.fiscalcode.utils.FragmentHelper.setupGenderRadioButtons;
import static com.example.fiscalcode_java.fiscalcode.utils.FragmentHelper.setupPlaceOfBirth;

public class ComputeFragment extends Fragment {

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

        setupGenderRadioButtons(root, R.id.com_maleRadioButton, R.id.com_femaleRadioButton);
        setupDateOfBirth(root, computeCalendar, R.id.com_dob_input);
        setupSpeedDial(root);

        String[] placesOfBirth = model.getPlaceList(context);
        ArrayAdapter<String> pobArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, placesOfBirth);
        setupPlaceOfBirth(root, pobArrayAdapter, R.id.com_pob_input);

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

            EditText firstNameEditText = activity.findViewById(R.id.com_first_name_input);
            EditText lastNameEditText = activity.findViewById(R.id.com_last_name_input);
            RadioButton maleRadioButton = activity.findViewById(R.id.com_maleRadioButton);
            RadioButton femaleRadioButton = activity.findViewById(R.id.com_femaleRadioButton);
            TextView dobEditText = activity.findViewById(R.id.com_dob_input);
            AutoCompleteTextView pobTextView = activity.findViewById(R.id.com_pob_input);

            allFieldsValid = InputField.FIRST_NAME.validateField(firstNameEditText, allFieldsValid, placesOfBirth, this);
            allFieldsValid = InputField.LAST_NAME.validateField(lastNameEditText, allFieldsValid, placesOfBirth, this);
            allFieldsValid = InputField.validateField(this, femaleRadioButton, maleRadioButton, allFieldsValid);
            allFieldsValid = InputField.DATE_OF_BIRTH.validateField(dobEditText, allFieldsValid, this);
            allFieldsValid = InputField.PLACE_OF_BIRTH.validateField(pobTextView, allFieldsValid, placesOfBirth, this);

            if (allFieldsValid) {
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String gender = maleRadioButton.isChecked() ? "m" : "f";
                String dob = dobEditText.getText().toString();
                String pob = pobTextView.getText().toString();

                try {
                    List<Town> towns = ReadTownListHelper.readTowns(activity.getAssets().open(ComputeViewModel.TOWNS_FILE));
                    List<Country> countries = ReadTownListHelper.readCountries(activity.getAssets().open(ComputeViewModel.COUNTRIES_FILE));
                    String fiscalCode = ComputeFiscalCodeHelper.compute(firstName, lastName, dob, gender, pob, towns, countries);
                    FragmentHelper.hideVirtualKeyboard(view);

                    TextView outputTextView = activity.findViewById(R.id.com_fiscalCodeOutput);
                    outputTextView.setPadding(10, 5, 10, 5);
                    outputTextView.setText(fiscalCode);
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
            FragmentActivity activity = Objects.requireNonNull(getActivity());

            EditText firstName = activity.findViewById(R.id.com_first_name_input);
            firstName.setText("");
            firstName.setError(null);
            EditText lastName = activity.findViewById(R.id.com_last_name_input);
            lastName.setText("");
            lastName.setError(null);
            RadioGroup radioGroup = activity.findViewById(R.id.com_radioGroup);
            radioGroup.clearCheck();
            RadioButton maleRadio = activity.findViewById(R.id.com_maleRadioButton);
            maleRadio.setError(null);
            RadioButton femaleRadio = activity.findViewById(R.id.com_femaleRadioButton);
            femaleRadio.setError(null);
            TextView dob = activity.findViewById(R.id.com_dob_input);
            dob.setText("");
            dob.setError(null);
            AutoCompleteTextView pob = activity.findViewById(R.id.com_pob_input);
            pob.setText("");
            pob.setError(null);
            TextView fiscalCodeOutput = activity.findViewById(R.id.com_fiscalCodeOutput);
            fiscalCodeOutput.setText("");
            fiscalCodeOutput.setPadding(0, 0, 0, 0);
        };
    }

    public void setupSpeedDial(View root) {
        SpeedDialView speedDialView = root.findViewById(R.id.speedDial);
        final int color = root.getResources().getColor(R.color.colorAccent, null);
        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_copy, R.drawable.ic_content_copy_24px)
                .setFabBackgroundColor(color)
                .create());
        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_send, R.drawable.ic_share_24px)
                .setFabBackgroundColor(color)
                .create());

        TextView fiscalCode = root.findViewById(R.id.com_fiscalCodeOutput);

        speedDialView.setOnActionSelectedListener(actionItem -> {
            switch (actionItem.getId()) {
                case R.id.fab_copy:
                    copyFunction(root, fiscalCode.getText());
                    break;
                case R.id.fab_send:
                    shareFunction(root, fiscalCode.getText());
                    break;
                default:
                    return false;
            }
            return false;
        });
    }

    public void copyFunction(View root, CharSequence fiscalCode) {
        String message;
        final Context context = getContext();
        if (!fiscalCode.toString().isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Fiscal code", fiscalCode);
            clipboard.setPrimaryClip(clipData);
            message = String.format(context.getResources().getString(R.string.copied_to_clipboard), fiscalCode);
        } else {
            message = context.getResources().getString(R.string.nothing_to_copy);
        }
        Snackbar.make(root, message, Snackbar.LENGTH_LONG)
                .setAction("action", null)
                .show();
    }

    public void shareFunction(View root, CharSequence fiscalCode) {
        if (!fiscalCode.toString().isEmpty()) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, fiscalCode);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            getContext().startActivity(shareIntent);
        } else {
            Snackbar.make(root, getContext().getResources().getString(R.string.nothing_to_copy), Snackbar.LENGTH_LONG)
                    .setAction("action", null)
                    .show();
        }
    }
}
