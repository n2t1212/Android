package com.mimi.mimigroup.ui.setting;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseFragment;

public class NCCFragment extends BaseFragment {
    @Override
    protected int getLayoutResourceId() {
        if(AppSetting.getInstance().isAndroidVersion5()) {
            return R.layout.fragment_ncc;
        }else{
            return R.layout.fragment_ncc_21;
        }
    }
}
