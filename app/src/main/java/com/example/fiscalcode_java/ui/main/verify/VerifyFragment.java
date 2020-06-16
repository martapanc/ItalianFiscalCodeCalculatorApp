package com.example.fiscalcode_java.ui.main.verify;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.fiscalcode_java.R;
import com.example.fiscalcode_java.ui.main.compute.ComputeViewModel;

import java.util.Objects;

import lombok.SneakyThrows;

public class VerifyFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SneakyThrows
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_verify, container, false);
        Context context = Objects.requireNonNull(getContext());
        ComputeViewModel model = ViewModelProviders.of(requireActivity()).get(ComputeViewModel.class);

        String[] placesOfBirth = model.getPlaceList(context);
        ArrayAdapter<String> pobArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, placesOfBirth);

        Button extractButton = root.findViewById(R.id.extract_data_button);
        extractButton.setOnClickListener(validateFieldsAndCompute(placesOfBirth));
        return root;
    }

    private View.OnClickListener validateFieldsAndCompute(String[] placesOfBirth) {
        return view -> {};
    }
}
