package com.mimi.mimigroup.api;

import com.mimi.mimigroup.model.DM_Customer_Distance;
import java.util.List;

public interface SyncFindCustomerCallback {
    public void onSyncStart();
    public void onSyncSuccess(List<DM_Customer_Distance> lstSel);
    public void onSyncFailer(Exception e);
}
