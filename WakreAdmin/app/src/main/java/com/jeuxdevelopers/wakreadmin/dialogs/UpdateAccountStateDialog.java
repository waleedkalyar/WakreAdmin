package com.jeuxdevelopers.wakreadmin.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jeuxdevelopers.wakreadmin.R;
import com.jeuxdevelopers.wakreadmin.databinding.DialogUpdateAccountStateBinding;
import com.jeuxdevelopers.wakreadmin.enums.AccountState;
import com.jeuxdevelopers.wakreadmin.models.UserModel;
import com.jeuxdevelopers.wakreadmin.utils.Constants;
import com.jeuxdevelopers.wakreadmin.utils.Utils;

import java.util.Objects;

public class UpdateAccountStateDialog extends Dialog {
    private Context context;
    private DialogUpdateAccountStateBinding binding;
    private UserModel model;
    private DocumentReference mUserRef;
    private StateUpdateListener listener;

    public UpdateAccountStateDialog(@NonNull Context context, UserModel model) {
        super(context);
        this.context = context;
        this.model = model;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_update_account_state, null, false);
        setContentView(binding.getRoot());
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        init();
    }

    private void init() {
        mUserRef = FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                .document(model.getuId());
        setAutoAdapter();
        setClickListeners();
    }

    private void setAutoAdapter() {
        String[] type = new String[]{AccountState.Pending.name(), AccountState.Active.name(), AccountState.Declined.name(), AccountState.Suspended.name()};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.dropdown_menu_popup_item, type);
        binding.autoComplete.setAdapter(adapter);
    }

    private void setClickListeners() {
        binding.btnBack.setOnClickListener(v -> dismiss());
        binding.btnCancel.setOnClickListener(v -> dismiss());

        binding.btnDone.setOnClickListener(v -> {
            String state = binding.autoComplete.getText().toString();
            if (state.isEmpty()) {
                binding.inputState.setError("Please select some type");
            } else if (contains(state)) {
                AccountState accountState = AccountState.valueOf(state);
                binding.inputState.setError(null);
                mUserRef.update(Constants.ACCOUNT_STATE, accountState).addOnSuccessListener(aVoid -> {
                    model.setAccountState(accountState);
                    listener.onStateUpdate(model);
                    dismiss();
                }).addOnFailureListener(e -> {
                    Utils.showShortToast(context, e.getLocalizedMessage());
                });
            } else {
                binding.inputState.setError("Please select valid state.");
            }

        });
    }

    public static boolean contains(String test) {

        for (AccountState c : AccountState.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }

        return false;
    }

    public void setOnStateUpdateListener(StateUpdateListener listener) {
        this.listener = listener;
    }

    public interface StateUpdateListener {
        void onStateUpdate(UserModel model);
    }
}
