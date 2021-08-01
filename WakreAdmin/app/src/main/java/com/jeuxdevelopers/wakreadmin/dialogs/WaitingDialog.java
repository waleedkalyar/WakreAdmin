package com.jeuxdevelopers.wakreadmin.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;


import com.jeuxdevelopers.wakreadmin.R;
import com.jeuxdevelopers.wakreadmin.databinding.DialogWaitingBinding;

import java.util.Objects;

public class WaitingDialog extends Dialog {
    private DialogWaitingBinding binding;
    private Context context;
    private String status;

    public WaitingDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_waiting, null, false);
        setContentView(binding.getRoot());
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        init();
    }

    private void init() {
        if (status!=null){
        binding.tvStatusLoading.setText(status);
        }
    }

    public void show(String status) {
        this.status = status;
        show();
    }

    public void setStatusTextView(String status){
        binding.tvStatusLoading.setText(status);
    }
}
