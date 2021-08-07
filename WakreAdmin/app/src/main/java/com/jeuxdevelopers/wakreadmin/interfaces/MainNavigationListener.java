package com.jeuxdevelopers.wakreadmin.interfaces;

import com.jeuxdevelopers.wakreadmin.models.TransactionModel;
import com.jeuxdevelopers.wakreadmin.models.UserModel;

public interface MainNavigationListener {
    void fromTransactionsIncomeFragmentToTransactionDetailFragment(TransactionModel model);
    void fromAppUsersFragmentToUserDetailFragment(UserModel model);
}
