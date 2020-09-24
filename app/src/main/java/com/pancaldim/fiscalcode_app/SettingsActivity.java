package com.pancaldim.fiscalcode_app;

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

import com.pancaldim.fiscalcode_app.fiscalcode.utils.FirebaseHelper;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

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
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
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

        private static final String PRIVACY = "privacy";
        private static final String TCS = "tcs";
        private static final String INFO = "info";
        private static final String FEEDBACK = "feedback";

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            setupInfoPreference();
            setupFeedbackPreference();
            setupLegalPreference(Objects.requireNonNull(findPreference(PRIVACY)), R.layout.view_privacy);
            setupLegalPreference(Objects.requireNonNull(findPreference(TCS)), R.layout.view_terms);

            return super.onCreateView(inflater, container, savedInstanceState);
        }

        private void setupLegalPreference(Preference legalPreference, int view) {
            legalPreference.setOnPreferenceClickListener(preference -> {
                Dialog dialog = new Dialog(requireContext());
                dialog.setContentView(view);

                // Make Links clickable
                if (view == R.layout.view_terms) {
                    TextView t2 = dialog.findViewById(R.id.terms_4);
                    t2.setMovementMethod(LinkMovementMethod.getInstance());
                } else {
                    TextView t2 = dialog.findViewById(R.id.terms_link);
                    t2.setMovementMethod(LinkMovementMethod.getInstance());
                }

                dialog.show();
                return true;
            });
        }

        private void setupInfoPreference() {
            Preference infoPreference = findPreference(INFO);
            Objects.requireNonNull(infoPreference).setOnPreferenceClickListener(preference -> {
                Dialog infoDialog = new Dialog(requireContext());
                infoDialog.setContentView(R.layout.view_info);

                // Make the link clickable
                TextView t2 = infoDialog.findViewById(R.id.info_dev);
                t2.setMovementMethod(LinkMovementMethod.getInstance());

                infoDialog.show();
                return true;
            });
        }

        private void setupFeedbackPreference() {
            Preference feedbackPreference = findPreference(FEEDBACK);
            Objects.requireNonNull(feedbackPreference).setOnPreferenceClickListener(preference -> {
                Dialog feedbackDialog = setupFeedbackDialog();
                setupFeedbackViewButtons(feedbackDialog);
                return true;
            });
        }

        private Dialog setupFeedbackDialog() {
            Dialog feedbackDialog = new Dialog(requireContext());
            InsetDrawable inset = new InsetDrawable(new ColorDrawable(Color.WHITE), 25);
            Objects.requireNonNull(feedbackDialog.getWindow()).setBackgroundDrawable(inset);
            feedbackDialog.setContentView(R.layout.view_feedback);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                TextView feedbackSubtitle = feedbackDialog.findViewById(R.id.feedback_subtitle);
                feedbackSubtitle.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            }

            feedbackDialog.show();
            return feedbackDialog;
        }

        private void setupFeedbackViewButtons(Dialog feedbackDialog) {
            Button cancel = feedbackDialog.findViewById(R.id.feedback_cancel_button);
            cancel.setOnClickListener(view -> feedbackDialog.dismiss());
            TextInputLayout textInput = feedbackDialog.findViewById(R.id.feedback_input);

            Button send = feedbackDialog.findViewById(R.id.feedback_send_button);
            send.setOnClickListener(view -> {
                textInput.setError(null);
                String text = Objects.requireNonNull(textInput.getEditText()).getText().toString();
                if (validateFields(textInput, text)) {
                    performAnonymousSignIn(feedbackDialog, text);
                }
            });
        }

        private boolean validateFields(TextInputLayout textInput, String text) {
            if (text.isEmpty()) {
                textInput.setError(getResources().getString(R.string.empty_field_error));
                return false;
            }
            return true;
        }

        private void performAnonymousSignIn(Dialog feedbackDialog, String text) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInAnonymously().addOnCompleteListener(requireActivity(), task -> {
                if (task.isSuccessful()) {
                    System.out.println("signInAnonymously:success");
                    sendEmail(text, mAuth.getUid());
                } else {
                    Toast.makeText(requireActivity().getApplicationContext(), this.getResources().getString(R.string.email_sent_successfully), Toast.LENGTH_LONG).show();
                }
                feedbackDialog.dismiss();
            });
        }

        private void sendEmail(String text, String uid) {
            final Context context = this.requireActivity().getApplicationContext();
            Toast.makeText(context, this.getResources().getString(R.string.email_sent_successfully), Toast.LENGTH_LONG).show();
            new FirebaseHelper(context).addMessage(text, uid);
        }
    }
}