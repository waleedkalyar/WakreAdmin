package com.jeuxdevelopers.wakreadmin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.View;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.jeuxdevelopers.wakreadmin.R;
import com.jeuxdevelopers.wakreadmin.databinding.ActivityMainBinding;
import com.jeuxdevelopers.wakreadmin.interfaces.MainNavigationListener;
import com.jeuxdevelopers.wakreadmin.models.TransactionModel;
import com.jeuxdevelopers.wakreadmin.models.UserModel;
import com.jeuxdevelopers.wakreadmin.utils.Constants;

public class MainActivity extends AppCompatActivity implements MainNavigationListener {
    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        navController = Navigation.findNavController(this, R.id.main_host_fragment);
        setUpBottomNav();
        setBottomNavListeners();
    }


    private void setBottomNavListeners() {
        binding.bottomNav.setOnClickMenuListener(model -> {
            switch (model.getId()) {
                case Constants.BOTTOM_VERIFICATIONS:
                    navController.popBackStack();
                    navController.navigate(R.id.accountsVerificationFragment);
                    break;
                case Constants.BOTTOM_TRANSACTION_INCOME:
                    navController.popBackStack();
                    navController.navigate(R.id.transactionsIncomeFragment);
                    break;
                case Constants.BOTTOM_APP_USERS:
                    navController.popBackStack();
                    navController.navigate(R.id.appUsersFragment);
                    break;
//                case Constants.BOTTOM_PROFILE:
//                    navController.popBackStack();
//                    navController.navigate(R.id.merchantProfileFragment);
//                    break;
                default:
                    break;
            }

            return null;
        });
    }

    private void setUpBottomNav() {
        binding.bottomNav.add(new MeowBottomNavigation.Model(Constants.BOTTOM_VERIFICATIONS, R.drawable.ic_users_verification));
        binding.bottomNav.add(new MeowBottomNavigation.Model(Constants.BOTTOM_TRANSACTION_INCOME, R.drawable.ic_income));
        binding.bottomNav.add(new MeowBottomNavigation.Model(Constants.BOTTOM_APP_USERS, R.drawable.ic_users));
        // binding.bottomNav.add(new MeowBottomNavigation.Model(Constants.BOTTOM_PROFILE, R.drawable.ic_profile));

        binding.bottomNav.show(Constants.BOTTOM_TRANSACTION_INCOME, true);
        navController.popBackStack();
        navController.navigate(R.id.transactionsIncomeFragment);
    }


    @Override
    public void fromTransactionsIncomeFragmentToTransactionDetailFragment(TransactionModel model) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.TRANSACTION_MODEL, model);
        navController.navigate(R.id.action_transactionsIncomeFragment_to_transactionDetailFragment, bundle);
        binding.bottomNav.setVisibility(View.GONE);
    }

    @Override
    public void fromAppUsersFragmentToUserDetailFragment(UserModel model) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.USER_MODEL, model);
        navController.navigate(R.id.action_appUsersFragment_to_userDetailFragment, bundle);
        binding.bottomNav.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        binding.bottomNav.setVisibility(View.VISIBLE);
    }
}
