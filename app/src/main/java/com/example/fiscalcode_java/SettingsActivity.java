package com.example.fiscalcode_java;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.textfield.TextInputLayout;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Preference feedbackPreference = findPreference("feedback");
            feedbackPreference.setOnPreferenceClickListener(preference -> {
                Dialog feedbackDialog = setupFeedbackDialog();
                setupFeedbackViewButtons(feedbackDialog);
                return true;
            });

            return super.onCreateView(inflater, container, savedInstanceState);
        }

        public Dialog setupFeedbackDialog() {
            Dialog feedbackDialog = new Dialog(getContext());
            InsetDrawable inset = new InsetDrawable(new ColorDrawable(Color.WHITE), 25);
            feedbackDialog.getWindow().setBackgroundDrawable(inset);
            feedbackDialog.setContentView(R.layout.view_feedback);
            feedbackDialog.show();
            return feedbackDialog;
        }

        public void setupFeedbackViewButtons(Dialog feedbackDialog) {
            Button cancel = feedbackDialog.findViewById(R.id.feedback_cancel_button);
            cancel.setOnClickListener(view -> feedbackDialog.dismiss());

            Button send = feedbackDialog.findViewById(R.id.feedback_send_button);
            send.setOnClickListener(view -> {
                TextInputLayout emailInput = feedbackDialog.findViewById(R.id.email_input);
                String email = emailInput.getEditText().getText().toString();

                TextInputLayout textInput = feedbackDialog.findViewById(R.id.feedback_input);
                String text = textInput.getEditText().getText().toString();
                sendEmail(email, text);
            });
        }

        private void sendEmail(String email, String text) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
            i.putExtra(Intent.EXTRA_SUBJECT, "Test email");
            i.putExtra(Intent.EXTRA_TEXT   , text);
            try {
                startActivity(Intent.createChooser(i, "Sending mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this.getActivity().getApplicationContext(), "Could not send email", Toast.LENGTH_LONG).show();
            }
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
}