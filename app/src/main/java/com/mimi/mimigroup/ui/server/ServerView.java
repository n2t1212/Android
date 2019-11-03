package com.mimi.mimigroup.ui.server;

import com.mimi.mimigroup.base.BaseView;

public interface ServerView extends BaseView {
    void onCheckSuccess(String ip, String port);
    void onCheckFail(String msg);
}
