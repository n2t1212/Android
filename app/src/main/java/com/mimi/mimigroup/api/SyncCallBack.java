package com.mimi.mimigroup.api;

public interface SyncCallBack {
    public void onSyncStart();
    public void onSyncSuccess(String ResPonseRs);
    public void onSyncFailer(Exception e);
}
