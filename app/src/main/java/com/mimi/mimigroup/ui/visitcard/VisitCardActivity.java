package com.mimi.mimigroup.ui.visitcard;

import android.os.Bundle;
import com.mimi.mimigroup.R;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.ui.adapter.VisitCardAdapter;

public class VisitCardActivity extends BaseActivity{

    private VisitCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitcard);
    }
}
