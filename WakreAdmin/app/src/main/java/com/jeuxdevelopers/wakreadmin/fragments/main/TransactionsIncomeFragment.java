package com.jeuxdevelopers.wakreadmin.fragments.main;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeuxdevelopers.wakreadmin.R;
import com.jeuxdevelopers.wakreadmin.databinding.FragmentTransactionsIncomeBinding;


public class TransactionsIncomeFragment extends Fragment {
    private Context context;
    private FragmentTransactionsIncomeBinding binding;

    public TransactionsIncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransactionsIncomeBinding.inflate(inflater,container,false);
        init();
        return binding.getRoot();
    }

    private void init() {
        context = requireContext();
    }
}