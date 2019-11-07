package com.mimi.mimigroup.ui.utility;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.SM_ReportTech;
import com.mimi.mimigroup.model.SM_ReportTechActivity;
import com.mimi.mimigroup.model.SM_ReportTechCompetitor;
import com.mimi.mimigroup.model.SM_ReportTechDisease;
import com.mimi.mimigroup.model.SM_ReportTechMarket;
import com.mimi.mimigroup.ui.adapter.FragmentPagerTabAdapter;

import java.util.List;

import butterknife.BindView;

public class ReportTechFormActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    FragmentPagerTabAdapter adapter;

    @BindView(R.id.btnReportTechDetailAdd)
    FloatingActionButton btnReportTechDetailAdd;
    @BindView(R.id.btnReportTechDetailDel)
    FloatingActionButton btnReportTechDetailDel;

    public String mReportTechID="";
    private String mPar_Symbol;
    private String mAction="";
    private DBGimsHelper mDB;
    private boolean isSaved=false;

    SM_ReportTech oReportTech;
    List<SM_ReportTechMarket> oReportTechMarket;
    List<SM_ReportTechDisease> oReportTechDisease;
    List<SM_ReportTechCompetitor> oReportTechCompetitor;
    List<SM_ReportTechActivity> oReportTechActivity;

    ReportTechFormItemFragment ReportTechFragment;

    public String getAction(){return this.mAction;}

    public SM_ReportTech getoReportTech() {
        return oReportTech;
    }

    public List<SM_ReportTechMarket> getoReportTechMarket() {
        return oReportTechMarket;
    }

    public List<SM_ReportTechDisease> getoReportTechDisease() {
        return oReportTechDisease;
    }

    public List<SM_ReportTechCompetitor> getoReportTechCompetitor() {
        return oReportTechCompetitor;
    }

    public List<SM_ReportTechActivity> getoReportTechActivity() {
        return oReportTechActivity;
    }
}
