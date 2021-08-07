package com.jeuxdevelopers.wakreadmin.fragments.main;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jeuxdevelopers.wakreadmin.R;
import com.jeuxdevelopers.wakreadmin.adapters.VerificationsAdapter;
import com.jeuxdevelopers.wakreadmin.databinding.FragmentAccountsVerificationBinding;
import com.jeuxdevelopers.wakreadmin.enums.AccountState;
import com.jeuxdevelopers.wakreadmin.models.UserModel;
import com.jeuxdevelopers.wakreadmin.utils.Constants;
import com.jeuxdevelopers.wakreadmin.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class AccountsVerificationFragment extends Fragment {
    private Context context;
    private FragmentAccountsVerificationBinding binding;
    private CollectionReference verificationsCollectionRef;

    private List<UserModel> list;
    private VerificationsAdapter adapter;

    public AccountsVerificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountsVerificationBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        context = requireContext();
        verificationsCollectionRef = FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION);
        setRecycler();
        setRecyclerListener();
        setSearch();
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

    private void setRecyclerListener() {
        verificationsCollectionRef
                .whereEqualTo(Constants.ACCOUNT_STATE, AccountState.Pending)
                .addSnapshotListener((value, error) -> {
                    list.clear();
                    if (error != null) {
                        Utils.showShortToast(context, "Something went wrong!");
                    } else {
                        if (value != null) {
                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                list.add(snapshot.toObject(UserModel.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void setRecycler() {
        list = new ArrayList<>();
        adapter = new VerificationsAdapter(context, list, (state, model) -> {
            verificationsCollectionRef.document(model.getuId()).update(Constants.ACCOUNT_STATE, state);
        });
        binding.recyclerVerifications.setAdapter(adapter);
    }
}