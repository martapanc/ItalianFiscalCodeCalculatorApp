package com.example.fiscalcode_java;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.fiscalcode_java.fiscalcode.utils.EmailSender;
import com.example.fiscalcode_java.fiscalcode.utils.FirebaseHelper;
import com.example.fiscalcode_java.fiscalcode.utils.GmailSender;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDateTime;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static com.example.fiscalcode_java.fiscalcode.utils.EmailSender.EMAIL;
import static com.example.fiscalcode_java.fiscalcode.utils.EmailSender.KEY;


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

            TextInputLayout textInput = feedbackDialog.findViewById(R.id.feedback_input);

            Button send = feedbackDialog.findViewById(R.id.feedback_send_button);
            send.setOnClickListener(view -> {
                textInput.setError(null);

                String text = textInput.getEditText().getText().toString();

                if (validateFields(textInput, text)) {
                    sendEmail(text);
                    feedbackDialog.dismiss();
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

        private void sendEmail(String text) {
            final Context context = this.getActivity().getApplicationContext();
            Toast.makeText(context, this.getResources().getString(R.string.email_sent_successfully), Toast.LENGTH_LONG).show();

            String instanceId = "1234567";

            FirebaseHelper firebaseHelper = new FirebaseHelper();
            firebaseHelper.addMessage(text, instanceId);
//            EmailSender emailSender = new EmailSender(context, email, "[User feedback]", text);
//            emailSender.execute();

//            Properties properties = new Properties();
//            properties.put("mail.smtp.host", "smtp.gmail.com");
//            properties.put("mail.smtp.socketFactory.port", "465");
//            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//            properties.put("mail.smtp.auth", "true");
//            properties.put("mail.smtp.port", "465");
//
//            //Creating a new session
//            try {
//                GmailSender sender = new GmailSender(EMAIL, KEY);
//                sender.sendMail("This is Subject",
//                        text,
//                        email,
//                        email);
//            } catch (Exception e) {
//                Log.e("SendMail", e.getMessage(), e);
//            }
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