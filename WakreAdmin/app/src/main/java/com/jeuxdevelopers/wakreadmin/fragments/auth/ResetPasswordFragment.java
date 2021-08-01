package com.jeuxdevelopers.wakreadmin.fragments.auth;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeuxdevelopers.wakreadmin.databinding.FragmentResetPasswordBinding;


public class ResetPasswordFragment extends Fragment {
    private Context context;
    private FragmentResetPasswordBinding binding;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        context = requireContext();
        setClickListeners();
    }

    private void setClickListeners() {
        binding.btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

    }
}