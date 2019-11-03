package com.mimi.mimigroup.api;

public interface APINetCallBack {
    public void onHttpStart();
    public void onHttpSuccess(String ResPonseRs);
    public void onHttpFailer(Exception e);
}
