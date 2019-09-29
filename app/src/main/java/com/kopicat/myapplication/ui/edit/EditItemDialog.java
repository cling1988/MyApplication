package com.kopicat.myapplication.ui.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kopicat.myapplication.entity.ProductViewModel;
import com.kopicat.myapplication.R;
import com.kopicat.myapplication.entity.Product;

public class EditItemDialog extends DialogFragment {

    private TextInputLayout mOpeningEditLayout;
    private TextInputLayout mBalanceEditLayout;
    private TextInputEditText mOpeningEdit;
    private TextInputEditText mBalanceEdit;
    private ProductViewModel viewModel;


//    public static EditItemDialog newInstance(Product item) {
//        EditItemDialog frag = new EditItemDialog();
//        Bundle args = new Bundle();
//        args.putString("title", title);
//        frag.setArguments(args);
//        return frag;
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);

        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_edit_item_dialog, null);

        mOpeningEditLayout = dialogView.findViewById(R.id.opening_edit_layout);
        mBalanceEditLayout = dialogView.findViewById(R.id.balance_edit_layout);
        mOpeningEdit = dialogView.findViewById(R.id.opening_edit);
        mBalanceEdit = dialogView.findViewById(R.id.balance_edit);
        showKeyboardFrom(mOpeningEdit);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(viewModel.getSelectedProduct().getValue().name);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setPositiveButton(getString(R.string.save_button), null);
        dialogBuilder.setNegativeButton(getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        final AlertDialog dialog = dialogBuilder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (isEditTextFilled(mOpeningEdit, mOpeningEditLayout, getString(R.string.error_msg_is_empty))
                                && isEditTextFilled(mBalanceEdit, mBalanceEditLayout, getString(R.string.error_msg_is_empty))) {

                            Double open = convertTextFilled(mOpeningEdit, mOpeningEditLayout, getString(R.string.error_msg_not_valid));
                            Double balance = convertTextFilled(mBalanceEdit, mBalanceEditLayout, getString(R.string.error_msg_not_valid));
                            if (null != open && null != balance) {
                                hideKeyboardFrom();
                                viewModel.updateProductList(new Product(viewModel.getSelectedProduct().getValue().
                                        id,viewModel.getSelectedProduct().getValue().name,open,balance));
                                dialog.dismiss();
                                Toast.makeText(getContext(), "Update the list", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        return dialog;
    }

    private Boolean isEditTextFilled(TextInputEditText editText, TextInputLayout textInputLayout, String message) {

        if (TextUtils.isEmpty(editText.getText())) {
            textInputLayout.setError(message);
            editText.requestFocus();
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private Double convertTextFilled(TextInputEditText editText, TextInputLayout textInputLayout, String message) {

        try {
            return Double.parseDouble(editText.getText().toString());
        } catch (NumberFormatException e) {
            textInputLayout.setError(message);
        }
        return null;
    }
    private void showKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.RESULT_UNCHANGED_SHOWN);
//        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

    }
    private void hideKeyboardFrom() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
}
