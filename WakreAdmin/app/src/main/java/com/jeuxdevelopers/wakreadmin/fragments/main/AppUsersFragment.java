package com.jeuxdevelopers.wakreadmin.fragments.main;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.jeuxdevelopers.wakreadmin.adapters.UsersListAdapter;
import com.jeuxdevelopers.wakreadmin.databinding.FragmentAppUsersBinding;
import com.jeuxdevelopers.wakreadmin.dialogs.WaitingDialog;
import com.jeuxdevelopers.wakreadmin.interfaces.MainNavigationListener;
import com.jeuxdevelopers.wakreadmin.models.UserModel;
import com.jeuxdevelopers.wakreadmin.utils.Constants;
import com.jeuxdevelopers.wakreadmin.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class AppUsersFragment extends Fragment implements UsersListAdapter.UserSelectListener {
    private Context context;
    private FragmentAppUsersBinding binding;
    private List<UserModel> list;
    private UsersListAdapter adapter;
    private MainNavigationListener navigationListener;
    private CollectionReference mUsersCollectionRef;
    private WaitingDialog waitingDialog;

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
        navigationListener = (MainNavigationListener) context;
        mUsersCollectionRef = FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION);
        waitingDialog = new WaitingDialog(context);
        setRecycler();
        setSearch();
        setRecyclerListener();


    }

    private void setRecyclerListener() {
        waitingDialog.show("Fetching Users");
        mUsersCollectionRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                waitingDialog.dismiss();
                Utils.showShortToast(context, error.getMessage());
            } else {
                waitingDialog.dismiss();
                if (value != null) {
                    for (DocumentChange documentChange : value.getDocumentChanges()) {

                        int oldIndex = documentChange.getOldIndex();
                        int newIndex = documentChange.getNewIndex();

                        switch (documentChange.getType()) {
                            case ADDED:
                                UserModel model = documentChange.getDocument().toObject(UserModel.class);
                                list.add(newIndex, model);
                                adapter.notifyItemInserted(newIndex);
                                break;
                            case MODIFIED:
                                UserModel modified = documentChange.getDocument().toObject(UserModel.class);
                                list.set(oldIndex, modified);
                                adapter.notifyItemChanged(newIndex);
                                break;
                        }
                    }
                }
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

    private void setRecycler() {
        list = new ArrayList<>();
        adapter = new UsersListAdapter(context, list, this);
        binding.recyclerUsers.setAdapter(adapter);
    }

    @Override
    public void onUserClick(UserModel model) {
        navigationListener.fromAppUsersFragmentToUserDetailFragment(model);
    }
}