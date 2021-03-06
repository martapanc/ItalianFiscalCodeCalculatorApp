package com.pancaldim.fiscalcode_app.ui.main.compute;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.snackbar.Snackbar;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.pancaldim.fiscalcode_app.R;
import com.pancaldim.fiscalcode_app.ShowBarcodeActivity;
import com.pancaldim.fiscalcode_app.exception.FiscalCodeComputationException;
import com.pancaldim.fiscalcode_app.fiscalcode.models.Country;
import com.pancaldim.fiscalcode_app.fiscalcode.models.FiscalCodeData;
import com.pancaldim.fiscalcode_app.fiscalcode.models.Gender;
import com.pancaldim.fiscalcode_app.fiscalcode.models.InputField;
import com.pancaldim.fiscalcode_app.fiscalcode.models.Town;
import com.pancaldim.fiscalcode_app.fiscalcode.utils.FragmentHelper;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static com.pancaldim.fiscalcode_app.ShowBarcodeActivity.FISCAL_CODE_DATA_EXTRA_KEY;
import static com.pancaldim.fiscalcode_app.fiscalcode.computations.ComputeFiscalCodeHelper.compute;
import static com.pancaldim.fiscalcode_app.fiscalcode.constants.ErrorMapConstants.getErrorMap;
import static com.pancaldim.fiscalcode_app.fiscalcode.utils.FragmentHelper.initCalendar;
import static com.pancaldim.fiscalcode_app.fiscalcode.utils.FragmentHelper.setupDateOfBirth;
import static com.pancaldim.fiscalcode_app.fiscalcode.utils.FragmentHelper.setupGenderRadioButtons;
import static com.pancaldim.fiscalcode_app.fiscalcode.utils.FragmentHelper.setupPlaceOfBirth;
import static com.pancaldim.fiscalcode_app.fiscalcode.utils.ReadTownListHelper.openFile;
import static com.pancaldim.fiscalcode_app.fiscalcode.utils.ReadTownListHelper.readCountriesWithLanguageSupport;
import static com.pancaldim.fiscalcode_app.fiscalcode.utils.ReadTownListHelper.readTowns;
import static com.pancaldim.fiscalcode_app.ui.main.compute.ComputeViewModel.COUNTRIES_FILE;
import static com.pancaldim.fiscalcode_app.ui.main.compute.ComputeViewModel.COUNTRIES_FILE_IT;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ComputeFragment extends Fragment {

    private static final Calendar computeCalendar = initCalendar();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SneakyThrows
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_compute, container, false);
        Context context = requireContext();
        ComputeViewModel model = new ViewModelProvider(requireActivity()).get(ComputeViewModel.class);

        setupGenderRadioButtons(root, R.id.com_maleRadioButton, R.id.com_femaleRadioButton);
        setupDateOfBirth(root, computeCalendar, R.id.com_dob_input);

        String[] placesOfBirth = new String[0];
        try {
            placesOfBirth = model.getPlaceList(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> pobArrayAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, placesOfBirth);
        setupPlaceOfBirth(root, pobArrayAdapter, R.id.com_pob_input);

        Button computeButton = root.findViewById(R.id.compute_button);
        computeButton.setOnClickListener(validateFieldsAndCompute(placesOfBirth));

        ImageButton resetButton = root.findViewById(R.id.compute_reset_button);
        resetButton.setOnClickListener(getResetListener());

        setupPobInfoDialog(root);
        setupSpeedDial(root);

        return root;
    }

    public View.OnClickListener validateFieldsAndCompute(final String[] placesOfBirth) {
        return view -> {
            FragmentActivity activity = requireActivity();

            EditText firstNameEditText = activity.findViewById(R.id.com_first_name_input);
            EditText lastNameEditText = activity.findViewById(R.id.com_last_name_input);
            RadioButton maleRadioButton = activity.findViewById(R.id.com_maleRadioButton);
            RadioButton femaleRadioButton = activity.findViewById(R.id.com_femaleRadioButton);
            TextView dobEditText = activity.findViewById(R.id.com_dob_input);
            AutoCompleteTextView pobTextView = activity.findViewById(R.id.com_pob_input);

            // Validate all fields (eager-loading) and display error if not valid on each field
            boolean allFieldsValid = InputField.FIRST_NAME.validateField(firstNameEditText, placesOfBirth, this)
                    & InputField.LAST_NAME.validateField(lastNameEditText, placesOfBirth, this)
                    & InputField.validateField(this, femaleRadioButton, maleRadioButton)
                    & InputField.DATE_OF_BIRTH.validateField(dobEditText, this)
                    & InputField.PLACE_OF_BIRTH.validateField(pobTextView, placesOfBirth, this);

            if (allFieldsValid) {
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String gender = maleRadioButton.isChecked() ? "m" : "f";
                String dob = dobEditText.getText().toString();
                String pob = pobTextView.getText().toString();

                try {
                    List<Town> towns = readTowns(openFile(activity, ComputeViewModel.TOWNS_FILE));
                    List<Country> countries = readCountriesWithLanguageSupport(openFile(activity, COUNTRIES_FILE), openFile(activity, COUNTRIES_FILE_IT));
                    String fiscalCode = compute(firstName, lastName, dob, gender, pob, towns, countries);

                    FragmentHelper.hideVirtualKeyboard(view);

                    FiscalCodeData fiscalCodeData = FiscalCodeData.builder()
                            .firstNameCode(firstName)
                            .lastNameCode(lastName)
                            .gender(Gender.getGender(gender))
                            .dateOfBirth(dob)
                            .placeOfBirth(pob)
                            .fiscalCode(fiscalCode)
                            .build();

                    displayOutputAndBarcodeButton(activity, fiscalCodeData);
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

            EditText firstName = activity.findViewById(R.id.com_first_name_input);
            firstName.setText(EMPTY);
            firstName.setError(null);
            EditText lastName = activity.findViewById(R.id.com_last_name_input);
            lastName.setText(EMPTY);
            lastName.setError(null);
            RadioGroup radioGroup = activity.findViewById(R.id.com_radioGroup);
            radioGroup.clearCheck();
            RadioButton maleRadio = activity.findViewById(R.id.com_maleRadioButton);
            maleRadio.setError(null);
            RadioButton femaleRadio = activity.findViewById(R.id.com_femaleRadioButton);
            femaleRadio.setError(null);
            TextView dob = activity.findViewById(R.id.com_dob_input);
            dob.setText(EMPTY);
            dob.setError(null);
            AutoCompleteTextView pob = activity.findViewById(R.id.com_pob_input);
            pob.setText(EMPTY);
            pob.setError(null);
            TextView fiscalCodeOutput = activity.findViewById(R.id.com_fiscalCodeOutput);
            fiscalCodeOutput.setText(EMPTY);
            fiscalCodeOutput.setPadding(0, 0, 0, 0);
            Button showBarcodeOutput = activity.findViewById(R.id.com_show_barcode_button);
            showBarcodeOutput.setVisibility(View.INVISIBLE);
        };
    }

    public void setupPobInfoDialog(View root) {
        ImageView pobInfoButton = root.findViewById(R.id.com_pob_info);
        pobInfoButton.setOnClickListener(view -> {
            Dialog dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.view_pob_info);
            Button closeDialog = dialog.findViewById(R.id.pob_info_close);
            closeDialog.setOnClickListener(v -> dialog.dismiss());
            dialog.show();
        });
    }

    public void displayOutputAndBarcodeButton(FragmentActivity activity, FiscalCodeData fiscalCodeData) {
        TextView outputTextView = activity.findViewById(R.id.com_fiscalCodeOutput);
        outputTextView.setPadding(10, 5, 10, 5);
        outputTextView.setText(fiscalCodeData.getFiscalCode());
        outputTextView.setOnClickListener(view -> copyFunction(view, fiscalCodeData.getFiscalCode()));

        Button showBarcodeButton = activity.findViewById(R.id.com_show_barcode_button);
        showBarcodeButton.setVisibility(View.VISIBLE);
        showBarcodeButton.setOnClickListener(view -> {
            Intent showBarcodeIntent = new Intent(view.getContext(), ShowBarcodeActivity.class);
            showBarcodeIntent.putExtra(FISCAL_CODE_DATA_EXTRA_KEY, fiscalCodeData);
            startActivityForResult(showBarcodeIntent, 0);
        });
    }

    private void setupSpeedDial(View root) {
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

    private void copyFunction(View root, CharSequence fiscalCode) {
        String message;
        final Context context = getContext();
        if (!fiscalCode.toString().isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) Objects.requireNonNull(context).getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Fiscal code", fiscalCode);
            Objects.requireNonNull(clipboard).setPrimaryClip(clipData);
            message = String.format(context.getResources().getString(R.string.copied_to_clipboard), fiscalCode);
        } else {
            message = Objects.requireNonNull(context).getResources().getString(R.string.nothing_to_copy);
        }
        Snackbar.make(root, message, Snackbar.LENGTH_LONG)
                .setAction("action", null)
                .show();
    }

    private void shareFunction(View root, CharSequence fiscalCode) {
        if (!fiscalCode.toString().isEmpty()) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, fiscalCode);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            requireContext().startActivity(shareIntent);
        } else {
            Snackbar.make(root, requireContext().getResources().getString(R.string.nothing_to_copy), Snackbar.LENGTH_LONG)
                    .setAction("action", null)
                    .show();
        }
    }
}
