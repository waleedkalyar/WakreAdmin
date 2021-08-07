package com.jeuxdevelopers.wakreadmin.fragments.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.jeuxdevelopers.wakreadmin.R;
import com.jeuxdevelopers.wakreadmin.activities.AuthenticationActivity;
import com.jeuxdevelopers.wakreadmin.adapters.TransactionsListAdapter;
import com.jeuxdevelopers.wakreadmin.databinding.FragmentTransactionsIncomeBinding;
import com.jeuxdevelopers.wakreadmin.dialogs.AddAmountDialog;
import com.jeuxdevelopers.wakreadmin.interfaces.MainNavigationListener;
import com.jeuxdevelopers.wakreadmin.models.AdminModel;
import com.jeuxdevelopers.wakreadmin.models.TransactionModel;
import com.jeuxdevelopers.wakreadmin.models.UserModel;
import com.jeuxdevelopers.wakreadmin.utils.Constants;
import com.jeuxdevelopers.wakreadmin.utils.MyPreferences;

import java.util.ArrayList;
import java.util.List;


public class TransactionsIncomeFragment extends Fragment implements TransactionsListAdapter.TransactionClickListener {
    private static final String TAG = "TransactionsIncomeFragment";
    private Context context;
    private FragmentTransactionsIncomeBinding binding;
    private MainNavigationListener navigationListener;

    private TransactionsListAdapter adapter;
    private List<TransactionModel> list;

    private CollectionReference mTransactionsRef;

    public TransactionsIncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransactionsIncomeBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        context = requireContext();
        navigationListener = (MainNavigationListener) context;
        mTransactionsRef = FirebaseFirestore.getInstance().collection(Constants.TRANSACTIONS_COLLECTION);
        setRecycler();
        setTransactionsListener();
        setSearch();
        setAdminListener();
        setClickListeners();
    }

    private void setClickListeners() {
        binding.btnLogout.setOnClickListener(v -> {
            new MyPreferences(context).saveCurrentUser(null);
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(context, AuthenticationActivity.class));
            requireActivity().finishAffinity();
        });

        binding.btnAddAmount.setOnClickListener(v -> {
            AddAmountDialog dialog = new AddAmountDialog(context, new UserModel(), Constants.ADMIN_COLLECTION);
            dialog.setOnAmountUpdateListener(m -> {
                setTransactionsListener();
                setAdminListener();
            });
            dialog.show();

        });
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void setAdminListener() {
        FirebaseFirestore.getInstance().collection(Constants.ADMIN_COLLECTION)
                .document(new MyPreferences(context).getCurrentUser().getId())
                .addSnapshotListener((value, error) -> {
                    if (error == null && value != null) {
                        AdminModel model = value.toObject(AdminModel.class);
                        binding.tvAmount.setText(String.format("%.2f", model.getAmount()) + "$");
                        new MyPreferences(context).saveCurrentUser(model);
                        binding.tvDeposits.setText(String.format("%.2f$", new MyPreferences(context).getCurrentUser().getDeposits()));
                    }
                });
    }

    private void setSearch() {
        binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    adapter.getFilter().filter(s);
                } else {
                    adapter.setData(list);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @SuppressLint({"LongLogTag", "DefaultLocale"})
    private void setTransactionsListener() {
        mTransactionsRef
                // .whereArrayContains(Constants.TRANSACTION_USERS, preferences.getUId())
                .orderBy(Constants.TRANSACTION_DATE, Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Utils.showShortSnackBar(binding.getRoot(), error.getMessage());
                        Log.d(TAG, error.getLocalizedMessage());
                    } else {
                        if (value != null) {
                            double spends = 0.0, taxes = 0.0, deposits = 0.0;
                            for (DocumentChange documentChange : value.getDocumentChanges()) {

                                int oldIndex = documentChange.getOldIndex();
                                int newIndex = documentChange.getNewIndex();

                                switch (documentChange.getType()) {
                                    case ADDED:
                                        TransactionModel newModel = documentChange.getDocument().toObject(TransactionModel.class);
                                        taxes += newModel.getTax();
                                        if (newModel.getSender().getProfileImage().equals("ADMIN")) {
                                            spends += newModel.getAmount();
                                        }
                                        list.add(newIndex, newModel);
                                        adapter.notifyItemInserted(newIndex);
                                        break;
                                    case MODIFIED:
                                        TransactionModel modifiedModel = documentChange.getDocument().toObject(TransactionModel.class);
                                        list.set(oldIndex, modifiedModel);
                                        adapter.notifyItemChanged(newIndex);
                                        break;
                                }

                                deposits = new MyPreferences(context).getCurrentUser().getAmount() - taxes;
                                binding.tvSpends.setText(String.format("%.2f$", spends));
                                binding.tvDeposits.setText(String.format("%.2f$", new MyPreferences(context).getCurrentUser().getDeposits()));
                                binding.tvTaxes.setText(String.format("%.2f$", taxes));
                            }
                        }
//                        else {
//                            binding.noMessageLayout.setVisibility(View.VISIBLE);
//                            binding.hasMessagesLayout.setVisibility(View.GONE);
//                        }
                    }

                });
    }


    private void setRecycler() {
        list = new ArrayList<>();
        adapter = new TransactionsListAdapter(list, context, this);
        binding.recyclerTransactions.setAdapter(adapter);
    }

    @Override
    public void onTransactionClick(TransactionModel model) {
        navigationListener.fromTransactionsIncomeFragmentToTransactionDetailFragment(model);
    }

}