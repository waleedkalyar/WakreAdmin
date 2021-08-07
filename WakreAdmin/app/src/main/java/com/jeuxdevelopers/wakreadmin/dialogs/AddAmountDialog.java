package com.jeuxdevelopers.wakreadmin.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jeuxdevelopers.wakreadmin.R;
import com.jeuxdevelopers.wakreadmin.databinding.DialogAddAmountBinding;
import com.jeuxdevelopers.wakreadmin.enums.TransactionState;
import com.jeuxdevelopers.wakreadmin.enums.TransactionType;
import com.jeuxdevelopers.wakreadmin.models.TransactionModel;
import com.jeuxdevelopers.wakreadmin.models.TransactionUser;
import com.jeuxdevelopers.wakreadmin.models.UserModel;
import com.jeuxdevelopers.wakreadmin.utils.Constants;
import com.jeuxdevelopers.wakreadmin.utils.MyPreferences;
import com.jeuxdevelopers.wakreadmin.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class AddAmountDialog extends Dialog {
    private final String type;
    private Context context;
    private DialogAddAmountBinding binding;
    private UserModel model;
    private CollectionReference mTransactionsRef;
    private AmountUpdateListener listener;

    private MyPreferences preferences;

    public AddAmountDialog(@NonNull Context context, UserModel model, String type) {
        super(context);
        this.context = context;
        this.model = model;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_add_amount, null, false);
        setContentView(binding.getRoot());
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        init();
    }

    private void init() {
        preferences = new MyPreferences(context);
        mTransactionsRef = FirebaseFirestore.getInstance().collection(Constants.TRANSACTIONS_COLLECTION);
        setClickListeners();
    }

    private void setClickListeners() {
        binding.btnBack.setOnClickListener(v -> dismiss());
        binding.btnCancel.setOnClickListener(v -> dismiss());

        binding.btnDone.setOnClickListener(v -> {
            String strAmount = binding.inputAmount.getEditText().getText().toString();
            if (strAmount.isEmpty()) {
                binding.inputAmount.setError("Please enter amount first");
            } else {
                double amount = Double.parseDouble(strAmount);
                if (amount <= 0) {
                    binding.inputAmount.setError("Please enter valid amount");
                } else {
                    binding.inputAmount.setError(null);
                    if (type.equals(Constants.CUSTOMER)) {
                        if (preferences.getCurrentUser().getAmount() < amount) {
                            Utils.showShortToast(context, "Your amount is low!");
                        } else {
                            binding.pbLoading.setVisibility(View.VISIBLE);
                            binding.linButtons.setVisibility(View.INVISIBLE);
                            addTransactionFromAdmin(amount);
                        }

                    } else {
                        binding.pbLoading.setVisibility(View.VISIBLE);
                        binding.linButtons.setVisibility(View.INVISIBLE);
                        double totalAmount = preferences.getCurrentUser().getAmount() + amount;
                        FirebaseFirestore.getInstance().collection(Constants.ADMIN_COLLECTION)
                                .document(preferences.getCurrentUser().getId()).update(Constants.USER_AMOUNT, totalAmount).addOnSuccessListener(aVoid -> {
                            FirebaseFirestore.getInstance().collection(Constants.ADMIN_COLLECTION)
                                    .document(preferences.getCurrentUser().getId()).update(Constants.ADMIN_DEPOSIT, preferences.getCurrentUser().getDeposits() + amount)
                                    .addOnSuccessListener(aVoid1 -> {
                                        binding.pbLoading.setVisibility(View.INVISIBLE);
                                        binding.linButtons.setVisibility(View.VISIBLE);
                                        model.setAmount(totalAmount);
                                        listener.onAmountUpdate(model);
                                        dismiss();
                                    }).addOnFailureListener(e -> {
                                binding.pbLoading.setVisibility(View.INVISIBLE);
                                binding.linButtons.setVisibility(View.VISIBLE);
                                Utils.showShortToast(context, e.getLocalizedMessage());
                            });

                        }).addOnFailureListener(e -> {
                            binding.pbLoading.setVisibility(View.INVISIBLE);
                            binding.linButtons.setVisibility(View.VISIBLE);
                            Utils.showShortToast(context, e.getLocalizedMessage());
                        });
                    }
                    // addTransactionFromAdmin(amount);
                    //Just add Amount

                }
            }
        });
    }

    private void addTransactionFromAdmin(double amount) {
        String id = mTransactionsRef.document().getId();
        TransactionUser sender = new TransactionUser(preferences.getUId(), preferences.getCurrentUser().getName(), "ADMIN", preferences.getCurrentUser().getEmail());
        TransactionUser receiver = new TransactionUser(model.getuId(), model.getName(), model.getProfileImageUrl(), model.getPhone());
        TransactionModel transaction = new TransactionModel(id, amount, sender, receiver, TransactionType.DIRECT_TRANSFER, TransactionState.Completed, System.currentTimeMillis());
        transaction.setNotes("From Admin to user.");
        transaction.setTax(0.0);
        List<String> users = new ArrayList<>();
        users.add(preferences.getUId());
        users.add(model.getuId());
        transaction.setUsers(users);


        mTransactionsRef.document(id).set(transaction).addOnSuccessListener(aVoid -> {
            FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                    .document(model.getuId())
                    .update(Constants.ACCOUNT_AMOUNT, (model.getAmount() + amount))
                    .addOnSuccessListener(aVoid1 -> {
                        FirebaseFirestore.getInstance().collection(Constants.ADMIN_COLLECTION)
                                .document(preferences.getCurrentUser().getId()).update(Constants.ACCOUNT_AMOUNT, preferences.getCurrentUser().getAmount() - amount)
                                .addOnSuccessListener(aVoid2 -> {
                                    model.setAmount(model.getAmount() + amount);
                                    listener.onAmountUpdate(model);
                                    dismiss();
                                }).addOnFailureListener(e -> {
                            Log.d(TAG, "setClickListeners: Error => " + e.getLocalizedMessage());
                            binding.pbLoading.setVisibility(View.INVISIBLE);
                            binding.linButtons.setVisibility(View.VISIBLE);
                            Utils.showShortToast(context, e.getLocalizedMessage());
                        });
                    }).addOnFailureListener(e -> {
                Log.d(TAG, "setClickListeners: Error => " + e.getLocalizedMessage());
                binding.pbLoading.setVisibility(View.INVISIBLE);
                binding.linButtons.setVisibility(View.VISIBLE);
                Utils.showShortToast(context, e.getLocalizedMessage());
            });
        }).addOnFailureListener(e -> {
            Log.d(TAG, "setClickListeners: Error => " + e.getLocalizedMessage());
            binding.pbLoading.setVisibility(View.INVISIBLE);
            binding.linButtons.setVisibility(View.VISIBLE);
            Utils.showShortToast(context, e.getLocalizedMessage());
        });
    }


    public void setOnAmountUpdateListener(AmountUpdateListener listener) {
        this.listener = listener;
    }

    public interface AmountUpdateListener {
        void onAmountUpdate(UserModel model);
    }
}
