package com.jeuxdevelopers.wakreadmin.fragments.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jeuxdevelopers.wakreadmin.activities.MainActivity;
import com.jeuxdevelopers.wakreadmin.databinding.FragmentLoginBinding;
import com.jeuxdevelopers.wakreadmin.models.AdminModel;
import com.jeuxdevelopers.wakreadmin.utils.Constants;
import com.jeuxdevelopers.wakreadmin.utils.MyPreferences;
import com.jeuxdevelopers.wakreadmin.utils.Utils;

import java.util.Objects;


public class LoginFragment extends Fragment {
    private Context context;
    private FragmentLoginBinding binding;
    private FirebaseAuth auth;
    private CollectionReference adminColRef;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        context = requireContext();
        auth = FirebaseAuth.getInstance();
        adminColRef = FirebaseFirestore.getInstance().collection(Constants.ADMIN_COLLECTION);
        setClickListeners();
    }

    private void setClickListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.inputEmail.getEditText().getText().toString();
            String password = binding.inputPassword.getEditText().getText().toString();

            if (email.isEmpty()) {
                Utils.showShortToast(context, "Please enter your email.");
            } else if (!email.contains("@")) {
                Utils.showShortToast(context, "Please enter valid email");
            } else if (password.isEmpty()) {
                binding.inputEmail.setError(null);
                binding.inputPassword.setError("Please enter password");
            } else {
                binding.inputEmail.setError(null);
                binding.inputPassword.setError(null);

                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                    adminColRef.document(authResult.getUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
                        AdminModel model = documentSnapshot.toObject(AdminModel.class);
                        new MyPreferences(context).saveCurrentUser(model);
                        startActivity(new Intent(context, MainActivity.class));
                        requireActivity().finishAffinity();
                    }).addOnFailureListener(e -> {
                        Utils.showShortToast(context, e.getLocalizedMessage());
                    });
                }).addOnFailureListener(e -> {
                    Utils.showShortToast(context, e.getLocalizedMessage());
                });
            }

        });

    }
}