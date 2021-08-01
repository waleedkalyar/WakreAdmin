package com.jeuxdevelopers.wakreadmin.fragments.others;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.jeuxdevelopers.wakreadmin.databinding.FragmentTransactionDetailBinding;
import com.jeuxdevelopers.wakreadmin.models.TransactionModel;
import com.jeuxdevelopers.wakreadmin.utils.Constants;
import com.jeuxdevelopers.wakreadmin.utils.MyPreferences;
import com.jeuxdevelopers.wakreadmin.utils.Utils;


public class TransactionDetailFragment extends Fragment {
    private static final String TAG = "TransactionDetailFragment";
    private FragmentTransactionDetailBinding binding;
    private Context context;

    private TransactionModel model;

    private String type;

    private MyPreferences preferences;

    public TransactionDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransactionDetailBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        context = requireContext();
        preferences = new MyPreferences(context);
        assert getArguments() != null;
        type = getArguments().getString(Constants.TYPE);


        setViews();
        setClickListeners();
    }

    @SuppressLint({"SetTextI18n", "LongLogTag", "DefaultLocale"})
    private void setViews() {
        binding.tvAmount.setText(model.getAmount() + "$");
        if (model.getReceiver() != null) {
            binding.tvCustomerName.setText(model.getReceiver().getName());
            binding.tvCustomerPhone.setText(model.getReceiver().getPhone());
        } else {
            binding.tvCustomerName.setText("Unknown");
            binding.tvCustomerPhone.setText("Unknown");
        }

        binding.tvId.setText(model.getTransactionId());
        if (model.getType() != null) {
            String type = model.getType().name();
            type = type.replace("_", " ");
            binding.tvType.setText(type);
        }

        if (type == null) {
//            Log.d(TAG, "setViews: Type is null open bt merchant");
//            if (model.getType() == TransactionType.DIRECT_TRANSFER && preferences.getCurrentUser().getUserType() == UserType.Merchant) {
//                Log.d(TAG, "setViews: Set outgoing");
//                binding.tvType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_upward, 0, 0, 0);
//            } else {
//                Log.d(TAG, "setViews: set incoming");
//                binding.tvType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_downward, 0, 0, 0);
//            }
//        } else {
//            if (model.getType() == TransactionType.DIRECT_TRANSFER && preferences.getCurrentUser().getUserType() == UserType.Customer) {
//                binding.tvType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_downward, 0, 0, 0);
//            } else {
//                binding.tvType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_upward, 0, 0, 0);
//            }
//        }

            // set arrow with color
//        if (model.getType() == TransactionType.GENERATE_QR && preferences.getCurrentUser().getUserType() == UserType.Customer) {
//            binding.tvType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_upward, 0, 0, 0);
//        } else {
//            binding.tvType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_downward, 0, 0, 0);
//        }

            binding.tvState.setText(model.getState().name());
            binding.tvDate.setText(Utils.getDateFromMillies(model.getDate()));
            Log.d(TAG, "setViews: Five Percent => " + Utils.get20PercentOfAmount(model.getAmount()));
            binding.tvTax.setText("(20%) " + String.format("%.2f$", Utils.get20PercentOfAmount(model.getAmount())));

        }
    }

    private void setClickListeners() {
        binding.btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
    }
}