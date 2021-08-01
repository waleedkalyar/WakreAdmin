package com.jeuxdevelopers.wakreadmin.fragments.main;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeuxdevelopers.wakreadmin.R;
import com.jeuxdevelopers.wakreadmin.databinding.FragmentAdminProfileBinding;


public class AdminProfileFragment extends Fragment {
    private Context context;
    private FragmentAdminProfileBinding binding;

    public AdminProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminProfileBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        context = requireContext();
    }
}