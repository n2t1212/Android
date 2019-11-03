package com.mimi.mimigroup.ui.setting;

import com.mimi.mimigroup.base.BaseView;
import com.mimi.mimigroup.model.DM_Customer;

import java.util.List;

public interface CustomerView extends BaseView {
    void onGetCustomerList(List<DM_Customer> customerList);
}
