package com.example.fiscalcode_java.ui.main.compute;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.fiscalcode_java.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import lombok.SneakyThrows;

public class ComputeFragment extends Fragment {



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ComputeViewModel computeViewModel = ViewModelProviders.of((Fragment) this).get(ComputeViewModel.class);
    }

    @SneakyThrows
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_compute, container, false);
//        TextView textView = root.findViewById(R.id.compute_section_label);

        Locale.setDefault(Locale.ITALY);

        EditText dateOfBirth = root.findViewById(R.id.dateOfBirth_editText);
        dateOfBirth.setRawInputType(InputType.TYPE_NULL);
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();

                FragmentActivity fragmentActivity = ComputeFragment.this.getActivity();
                DatePickerDialog datePickerDialog = new DatePickerDialog(fragmentActivity);
            }
        });

        JSONObject jsonObject = new JSONObject("{}");

        AutoCompleteTextView autoCompleteTextView = root.findViewById(R.id.pob_autocompleteTextView);

        String[] COUNTRIES = new String[]{"Belgium", "France", "Italy", "Italian", "Ithaca", "Germany", "Spain"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, COUNTRIES);
        return root;
    }
}
