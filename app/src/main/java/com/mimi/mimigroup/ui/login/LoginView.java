package com.mimi.mimigroup.ui.login;

import com.mimi.mimigroup.base.BaseView;

public interface LoginView extends BaseView {
    void onLoginSuccess();
    void onLoginFail(String msg);
}
