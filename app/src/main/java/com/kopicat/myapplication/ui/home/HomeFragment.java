package com.kopicat.myapplication.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.kopicat.myapplication.R;
import com.kopicat.myapplication.entity.ProductViewModel;

public class HomeFragment extends Fragment {

    private ProductViewModel viewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel =
                ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_branch);
        viewModel.getBranchName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.i("Home","Refresh text view");
                textView.setText(s);
            }
        });

        final EditText editText = root.findViewById(R.id.edit_branch);

        Button saveBtn = root.findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                viewModel.updateBranchName(s);
                textView.setText(s);
                editText.getText().clear();
                hideKeyboardFrom();
            }
        });

        return root;
    }

    private void hideKeyboardFrom() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
}