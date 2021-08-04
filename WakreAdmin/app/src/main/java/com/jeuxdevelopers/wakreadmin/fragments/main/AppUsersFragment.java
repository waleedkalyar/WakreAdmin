package com.jeuxdevelopers.wakreadmin.fragments.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.jeuxdevelopers.wakreadmin.databinding.FragmentAppUsersBinding;


public class AppUsersFragment extends Fragment {
    private Context context;
    private FragmentAppUsersBinding binding;

    public AppUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAppUsersBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        context = requireContext();
    }
}