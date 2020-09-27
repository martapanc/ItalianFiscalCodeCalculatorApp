package com.pancaldim.fiscalcode_app.ui.main.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pancaldim.fiscalcode_app.R;
import com.pancaldim.fiscalcode_app.fiscalcode.constants.TableConstants;
import com.pancaldim.fiscalcode_app.fiscalcode.constants.TableConstants_it;

import static com.pancaldim.fiscalcode_app.fiscalcode.constants.DateFormatAndLocaleConstants.getLanguage;

public class AboutFragment extends Fragment {

    private static final String UTF_8 = "UTF-8";
    private static final String TEXT_HTML = "text/html";
    private final String LANGUAGE = getLanguage();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        setupImagesAndTables(root);
        return root;
    }

    private void setupImagesAndTables(View root) {
        ImageView fcCardImageView = root.findViewById(R.id.fc_card);
        fcCardImageView.setImageResource("EN".equalsIgnoreCase(LANGUAGE) ? R.drawable.fc_card_en : R.drawable.fc_card_it);

        ImageView fcPartsImageView = root.findViewById(R.id.fc_parts);
        fcPartsImageView.setImageResource("EN".equalsIgnoreCase(LANGUAGE) ? R.drawable.fc_parts_en : R.drawable.fc_parts_it);

        WebView dobWebView = root.findViewById(R.id.table_months);
        dobWebView.loadDataWithBaseURL(null, getDoBTable(), TEXT_HTML, UTF_8, null);

        WebView oddWebView = root.findViewById(R.id.control_table_odd);
        oddWebView.loadDataWithBaseURL(null, getOddTable(), TEXT_HTML, UTF_8, null);

        WebView evenWebView = root.findViewById(R.id.control_table_even);
        evenWebView.loadDataWithBaseURL(null, getEvenTable(), TEXT_HTML, UTF_8, null);

        WebView controlWebView = root.findViewById(R.id.control_table);
        controlWebView.loadDataWithBaseURL(null, getControlTable(), TEXT_HTML, UTF_8, null);

        WebView omoWebView = root.findViewById(R.id.omo_table);
        omoWebView.loadDataWithBaseURL(null, getOmoTable(), TEXT_HTML, UTF_8, null);
    }

    private String getDoBTable() {
        return ("EN".equalsIgnoreCase(LANGUAGE) ? TableConstants.TABLE_DOB : TableConstants_it.TABLE_DOB);
    }

    private String getOddTable() {
        return ("EN".equalsIgnoreCase(LANGUAGE) ? TableConstants.TABLE_ODD : TableConstants_it.TABLE_ODD);
    }

    private String getEvenTable() {
        return ("EN".equalsIgnoreCase(LANGUAGE) ? TableConstants.TABLE_EVEN : TableConstants_it.TABLE_EVEN);
    }

    private String getControlTable() {
        return ("EN".equalsIgnoreCase(LANGUAGE) ? TableConstants.TABLE_CONTROL : TableConstants_it.TABLE_CONTROL);
    }

    private String getOmoTable() {
        return ("EN".equalsIgnoreCase(LANGUAGE) ? TableConstants.TABLE_OMO : TableConstants_it.TABLE_OMO);
    }
}
