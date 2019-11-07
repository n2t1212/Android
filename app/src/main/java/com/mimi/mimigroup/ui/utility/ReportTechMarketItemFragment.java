package com.mimi.mimigroup.ui.utility;

import android.support.v7.widget.RecyclerView;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.base.BaseFragment;
import com.mimi.mimigroup.ui.custom.CustomBoldEditText;

import butterknife.BindView;

public class ReportTechMarketItemFragment extends BaseFragment {

    @BindView(R.id.rvReportTechMarketList)
    RecyclerView rvReportTechMarketList;

    @BindView(R.id.tv)
    CustomBoldEditText tvAmountDetail;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_report_tech_market_form;
    }
}
