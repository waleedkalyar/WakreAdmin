package com.jeuxdevelopers.wakreadmin.fragments.others;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jeuxdevelopers.wakreadmin.R;
import com.jeuxdevelopers.wakreadmin.databinding.FragmentUserDetailBinding;
import com.jeuxdevelopers.wakreadmin.dialogs.AddAmountDialog;
import com.jeuxdevelopers.wakreadmin.dialogs.UpdateAccountStateDialog;
import com.jeuxdevelopers.wakreadmin.dialogs.WaitingDialog;
import com.jeuxdevelopers.wakreadmin.models.UserModel;
import com.jeuxdevelopers.wakreadmin.utils.Constants;
import com.jeuxdevelopers.wakreadmin.utils.MyPreferences;


public class UserDetailFragment extends Fragment {
    private Context context;
    private FragmentUserDetailBinding binding;
    private UserModel model;

    public UserDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserDetailBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        context = requireContext();
        model = (UserModel) getArguments().getSerializable(Constants.USER_MODEL);
        setProfileViews(model);
        setClickListeners();
    }

    private void setClickListeners() {
        binding.btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        binding.rlAmount.setOnClickListener(v -> {
            AddAmountDialog amountDialog = new AddAmountDialog(context, model,Constants.CUSTOMER);
            amountDialog.setOnAmountUpdateListener(m -> {
                this.model = m;
                setProfileViews(model);
            });
            amountDialog.show();
        });

        binding.rlAccountState.setOnClickListener(v -> {
            UpdateAccountStateDialog stateDialog = new UpdateAccountStateDialog(context, model);
            stateDialog.setOnStateUpdateListener(m -> {
                this.model = m;
                setProfileViews(model);
            });
            stateDialog.show();
        });
    }

    @SuppressLint("DefaultLocale")
    private void setProfileViews(UserModel model) {
        Glide.with(context).load(model.getProfileImageUrl())
                .into(binding.cvProfile);
        binding.tvName.setText(model.getName());
        binding.tvPhone.setText(model.getPhone());
        if (model.getShopName() == null || model.getShopName().isEmpty()) {
            binding.tvShopName.setText("");
        } else {
            binding.tvShopName.setText(model.getShopName());
        }
        binding.tvAmount.setText(String.format("%.2f$", model.getAmount()));
        binding.tvAccountType.setText(model.getUserType().name());
        binding.tvAccountState.setText(model.getAccountState().name());
        binding.tvAddress.setText(model.getAddress());

        switch (model.getAccountState()) {
            case Active:
                binding.tvAccountState.setTextColor(getResources().getColor(R.color.colorGreen));
                setTextViewDrawableColor(binding.tvAccountState, getResources().getColor(R.color.colorGreen));
                break;
            case Pending:
                binding.tvAccountState.setTextColor(getResources().getColor(R.color.colorGold));
                setTextViewDrawableColor(binding.tvAccountState, getResources().getColor(R.color.colorGold));
                break;
            case Declined:
                binding.tvAccountState.setTextColor(getResources().getColor(R.color.colorGreyDark));
                setTextViewDrawableColor(binding.tvAccountState, getResources().getColor(R.color.colorGreyDark));
                break;
            case Suspended:
                binding.tvAccountState.setTextColor(getResources().getColor(R.color.colorRed));
                setTextViewDrawableColor(binding.tvAccountState, getResources().getColor(R.color.colorRed));
                break;
        }
    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }
        }
    }


}