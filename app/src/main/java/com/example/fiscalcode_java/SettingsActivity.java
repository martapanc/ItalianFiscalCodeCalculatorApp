package com.example.fiscalcode_java;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.fiscalcode_java.fiscalcode.utils.FirebaseHelper;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 16908332) {
            finish();
            overridePendingTransition(R.anim.nothing, R.anim.exit);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            setupInfoPreference();
            setupFeedbackPreference();
            setupLegalPreference(findPreference("privacy"), R.layout.view_privacy);
            setupLegalPreference(findPreference("tcs"), R.layout.view_terms);

            return super.onCreateView(inflater, container, savedInstanceState);
        }

        private void setupLegalPreference(Preference legalPreference, int view) {
            legalPreference.setOnPreferenceClickListener(preference -> {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(view);
                dialog.show();
                return true;
            });
        }

        public void setupInfoPreference() {
            Preference infoPreference = findPreference("info");
            infoPreference.setOnPreferenceClickListener(preference -> {
                Dialog infoDialog = new Dialog(getContext());
                infoDialog.setContentView(R.layout.view_info);

                // Make the link clickable
                TextView t2 = infoDialog.findViewById(R.id.info_dev);
                t2.setMovementMethod(LinkMovementMethod.getInstance());

                infoDialog.show();
                return true;
            });
        }

        public void setupFeedbackPreference() {
            Preference feedbackPreference = findPreference("feedback");
            feedbackPreference.setOnPreferenceClickListener(preference -> {
                Dialog feedbackDialog = setupFeedbackDialog();
                setupFeedbackViewButtons(feedbackDialog);
                return true;
            });
        }

        public Dialog setupFeedbackDialog() {
            Dialog feedbackDialog = new Dialog(getContext());
            InsetDrawable inset = new InsetDrawable(new ColorDrawable(Color.WHITE), 25);
            feedbackDialog.getWindow().setBackgroundDrawable(inset);
            feedbackDialog.setContentView(R.layout.view_feedback);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                TextView feedbackSubtitle = feedbackDialog.findViewById(R.id.feedback_subtitle);
                feedbackSubtitle.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            }

            feedbackDialog.show();
            return feedbackDialog;
        }

        public void setupFeedbackViewButtons(Dialog feedbackDialog) {
            Button cancel = feedbackDialog.findViewById(R.id.feedback_cancel_button);
            cancel.setOnClickListener(view -> feedbackDialog.dismiss());
            TextInputLayout textInput = feedbackDialog.findViewById(R.id.feedback_input);

            Button send = feedbackDialog.findViewById(R.id.feedback_send_button);
            send.setOnClickListener(view -> {
                textInput.setError(null);
                String text = textInput.getEditText().getText().toString();
                if (validateFields(textInput, text)) {
                    performAnonymousSignIn(feedbackDialog, text);
                }
            });
        }

        public boolean validateFields(TextInputLayout textInput, String text) {
            boolean validFields = true;
            if (text.isEmpty()) {
                validFields = false;
                textInput.setError(getResources().getString(R.string.empty_field_error));
            }
            return validFields;
        }

        public void performAnonymousSignIn(Dialog feedbackDialog, String text) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInAnonymously().addOnCompleteListener(getActivity(), task -> {
                if (task.isSuccessful()) {
                    System.out.println("signInAnonymously:success");
                    sendEmail(text);
                    feedbackDialog.dismiss();
                } else {
                    Toast.makeText(this.getActivity().getApplicationContext(), this.getResources().getString(R.string.email_sent_successfully), Toast.LENGTH_LONG).show();
                }
            });
        }

        private void sendEmail(String text) {
            final Context context = this.getActivity().getApplicationContext();
            Toast.makeText(context, this.getResources().getString(R.string.email_sent_successfully), Toast.LENGTH_LONG).show();

            // TODO: add real instance id
            String instanceId = "1234567";

            FirebaseHelper firebaseHelper = new FirebaseHelper();
            firebaseHelper.addMessage(text, instanceId);
        }
    }
}