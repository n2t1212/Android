package com.mimi.mimigroup.ui.utility;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.api.APINet;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.DM_Customer_Search;
import com.mimi.mimigroup.model.DM_Tree;
import com.mimi.mimigroup.model.SM_ReportSaleRep;
import com.mimi.mimigroup.model.SM_ReportSaleRepActivitie;
import com.mimi.mimigroup.model.SM_ReportSaleRepDisease;
import com.mimi.mimigroup.model.SM_ReportSaleRepMarket;
import com.mimi.mimigroup.model.SM_ReportSaleRepSeason;
import com.mimi.mimigroup.ui.adapter.FragmentPagerTabAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.utils.AppUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ReportSaleRepFormActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    FragmentPagerTabAdapter adapter;

    @BindView(R.id.btnReportSaleRepDetailAdd)
    FloatingActionButton btnReportSaleRepDetailAdd;
    @BindView(R.id.btnReportSaleRepDetailDel)
    FloatingActionButton btnReportSaleRepDetailDel;

    public String getmReportSaleRepID() {
        return mReportSaleRepID;
    }

    public String mReportSaleRepID="";

    public String getmPar_Symbol() {
        return mPar_Symbol;
    }

    private String mPar_Symbol;
    private String mAction="";
    private DBGimsHelper mDB;
    private boolean isSaved=false;

    SM_ReportSaleRep oReportSaleRep;
    List<SM_ReportSaleRepMarket> oReportSaleRepMarket;
    List<SM_ReportSaleRepDisease> oReportSaleRepDisease;
    List<SM_ReportSaleRepSeason> oReportSaleRepSeason;
    List<SM_ReportSaleRepActivitie> oReportSaleRepActivity;
    List<SM_ReportSaleRepActivitie> oReportSaleRepTask;

    ReportSaleRepFormItemFragment ReportSaleRepFragment;
    ReportSaleRepMarketItemFragment ReportSaleRepMarketFragment;
    ReportSaleRepDiseaseItemFragment ReportSaleRepDiseaseFragment;
    ReportSaleRepSeasonItemFragment ReportSaleRepSeasonFragment;
    ReportSaleRepActivityItemFragment ReportSaleRepActivityFragment;
    ReportSaleRepTaskItemFragment ReportSaleRepTaskFragment;

    public String getAction(){return this.mAction;}

    public SM_ReportSaleRep getoReportSaleRep() {
        return oReportSaleRep;
    }

    public List<SM_ReportSaleRepMarket> getoReportSaleRepMarket() {
        return oReportSaleRepMarket;
    }

    public List<SM_ReportSaleRepDisease> getoReportSaleRepDisease() {
        return oReportSaleRepDisease;
    }

    public List<SM_ReportSaleRepActivitie> getoReportSaleRepActivity() {
        return oReportSaleRepActivity;
    }

    public List<SM_ReportSaleRepSeason> getoReportSaleRepSeason() {
        return oReportSaleRepSeason;
    }

    public List<SM_ReportSaleRepActivitie> getoReportSaleRepTask() {
        return oReportSaleRepTask;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_sale_form);
        mDB=DBGimsHelper.getInstance(this);

        adapter = new FragmentPagerTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        mReportSaleRepID = getIntent().getStringExtra("ReportSaleRepID");
        mPar_Symbol  = getIntent().getStringExtra("PAR_SYMBOL");
        mAction=getIntent().getAction().toString();
        if(mAction=="EDIT"){
            oReportSaleRep = mDB.getReportSaleRepById(mReportSaleRepID);
            oReportSaleRepMarket = mDB.getAllReportSaleRepMarket(mReportSaleRepID);
            oReportSaleRepDisease = mDB.getAllReportSaleRepDisease(mReportSaleRepID);
            oReportSaleRepSeason = mDB.getAllReportSaleRepSeason(mReportSaleRepID);
            oReportSaleRepTask = mDB.getAllReportSaleRepActivity(mReportSaleRepID, 0); // Cong viec
            oReportSaleRepActivity = mDB.getAllReportSaleRepActivity(mReportSaleRepID, 1); // Hoat dong tuan toi
        }else{
            oReportSaleRep = new SM_ReportSaleRep();
            oReportSaleRepMarket = new ArrayList<>();
            oReportSaleRepDisease = new ArrayList<>();
            oReportSaleRepSeason = new ArrayList<>();
            oReportSaleRepTask = new ArrayList<>();
            oReportSaleRepActivity = new ArrayList<>();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String mReportDate = sdf.format(new Date());
            String mRequestDate= AppUtils.DateAdd(mReportDate, 3, "yyyy-MM-dd");

            SimpleDateFormat sdfOrderCode = new SimpleDateFormat("ddMMyy");
            String mOrderCodeDMY = sdfOrderCode.format(new Date());
            SimpleDateFormat sdfhhMMss = new SimpleDateFormat("hhmmss");
            String mReportCodeHMS = sdfhhMMss.format(new Date());
            String mReportCode=mPar_Symbol+'-'+mReportCodeHMS+'/'+mOrderCodeDMY;

            oReportSaleRep.setReportSaleId(mReportSaleRepID);
            oReportSaleRep.setReportCode(mReportCode);
            oReportSaleRep.setReportDay(mReportDate);
            oReportSaleRep.setIsStatus("0");
            oReportSaleRep.setPost(false);
        }

        btnReportSaleRepDetailAdd.setVisibility(View.GONE);
        btnReportSaleRepDetailDel.setVisibility(View.GONE);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        ReportSaleRepFragment = new ReportSaleRepFormItemFragment();
                        adapter.addFragment(ReportSaleRepFragment, "Báo Cáo Sale");

                        ReportSaleRepMarketFragment = new ReportSaleRepMarketItemFragment();
                        adapter.addFragment(ReportSaleRepMarketFragment, "Thị Trường");

                        ReportSaleRepDiseaseFragment = new ReportSaleRepDiseaseItemFragment();
                        adapter.addFragment(ReportSaleRepDiseaseFragment, "Dịch Bệnh");

                        ReportSaleRepSeasonFragment = new ReportSaleRepSeasonItemFragment();
                        adapter.addFragment(ReportSaleRepSeasonFragment, "Mùa Vụ");
                        /*

                        ReportSaleRepTaskFragment = new ReportSaleRepTaskItemFragment();
                        adapter.addFragment(ReportSaleRepTaskFragment, "Công việc");

                        ReportSaleRepActivityFragment = new ReportSaleRepActivityItemFragment();
                        adapter.addFragment(ReportSaleRepActivityFragment, "HĐ Tuần Tới");*/

                        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                            @Override
                            public void onTabSelected(TabLayout.Tab tab) {
                                switch (tab.getPosition()){
                                    case 0:
                                        btnReportSaleRepDetailAdd.setVisibility(View.INVISIBLE);
                                        btnReportSaleRepDetailDel.setVisibility(View.INVISIBLE);
                                        break;
                                    case 1:
                                        btnReportSaleRepDetailAdd.setVisibility(View.VISIBLE);
                                        btnReportSaleRepDetailDel.setVisibility(View.INVISIBLE);
                                        btnReportSaleRepDetailAdd.setTag("ADD");
                                        break;
                                    case 2:
                                        btnReportSaleRepDetailAdd.setVisibility(View.VISIBLE);
                                        btnReportSaleRepDetailDel.setVisibility(View.INVISIBLE);
                                        btnReportSaleRepDetailAdd.setTag("ADD");
                                        break;
                                    case 3:
                                        btnReportSaleRepDetailAdd.setVisibility(View.VISIBLE);
                                        btnReportSaleRepDetailDel.setVisibility(View.INVISIBLE);
                                        btnReportSaleRepDetailAdd.setTag("ADD");
                                        break;
                                    case 4:
                                        btnReportSaleRepDetailAdd.setVisibility(View.VISIBLE);
                                        btnReportSaleRepDetailDel.setVisibility(View.INVISIBLE);
                                        btnReportSaleRepDetailAdd.setTag("ADD");
                                        break;
                                    case 5:
                                        btnReportSaleRepDetailAdd.setVisibility(View.VISIBLE);
                                        btnReportSaleRepDetailDel.setVisibility(View.INVISIBLE);
                                        btnReportSaleRepDetailAdd.setTag("ADD");
                                        break;
                                }
                            }
                            @Override
                            public void onTabUnselected(TabLayout.Tab tab) {}

                            @Override
                            public void onTabReselected(TabLayout.Tab tab) {}
                        });

                    }
                },300);
    }

    public List<DM_Tree> getListTree(){
        List<DM_Tree> lstTree=new ArrayList<DM_Tree>();
        try{
            lstTree=mDB.getAllTree();
        }catch (Exception ex){}
        return  lstTree;
    }

    /*[TRANSFER DATA FOR FRAMGMENT]*/
    public void setVisibleDetailDelete(boolean isVisible){
        try {
            if (isVisible) {
                btnReportSaleRepDetailDel.setVisibility(View.VISIBLE);
            } else {
                btnReportSaleRepDetailDel.setVisibility(View.INVISIBLE);
            }
        }catch (Exception ex){}
    }
    public void setButtonEditStatus(boolean isEdit){
        if(isEdit){
            btnReportSaleRepDetailAdd.setTag("EDIT");
            btnReportSaleRepDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_edit));
        }else{
            btnReportSaleRepDetailAdd.setTag("ADD");
            btnReportSaleRepDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_add));
        }
    }
    private void ReceiveDataFragment(){
        //SAVE DETAIL
        try{
            if(btnReportSaleRepDetailAdd.getTag()!=null && btnReportSaleRepDetailAdd.getTag().toString().equalsIgnoreCase("SAVE")) {
                onReportSaleRepDetailAdd();

            }
        }catch (Exception ex){}

        if(ReportSaleRepFragment !=null) {
            oReportSaleRep = ReportSaleRepFragment.getSMReportSaleRep();
        }

        if(ReportSaleRepMarketFragment!=null) {
            oReportSaleRepMarket = ReportSaleRepMarketFragment.getListReportSaleRepMarket();
        }
        if(ReportSaleRepDiseaseFragment != null) {
            oReportSaleRepDisease = ReportSaleRepDiseaseFragment.getListReportSaleRepDisease();
        }
        if(ReportSaleRepSeasonFragment!=null) {
            oReportSaleRepSeason = ReportSaleRepSeasonFragment.getListReportSaleRepSeason();
        }/*
        if(ReportSaleRepActivityThisWeekFragment != null){
            oReportSaleRepActivityThisWeek = ReportSaleRepActivityThisWeekFragment.getListReportSaleRepActivityThisWeek();
        }
        if(ReportSaleRepActivityNextWeekFragment != null){
            oReportSaleRepActivityNextWeek = ReportSaleRepActivityNextWeekFragment.getListReportSaleRepActivityNextWeek();
        }*/
    }

    @Override
    public void onBackPressed() {
        if (!isSaved) {
            final Dialog oDlg=new Dialog(this);
            oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            oDlg.setContentView(R.layout.dialog_yesno);
            oDlg.setTitle("");
            CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
            dlgTitle.setText("THÔNG BÁO");
            CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
            dlgContent.setText("Dữ liệu báo cáo kỹ thuật chưa cập nhật. Bạn có muốn bỏ qua ?");
            CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
            CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onExcuteBackPress();
                    oDlg.dismiss();
                }
            });
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oDlg.dismiss();
                    return;
                }
            });
            oDlg.show();

        } else {
            super.onBackPressed();
        }
    }

    private void onExcuteBackPress(){
        super.onBackPressed();
    }

    @OnClick(R.id.ivBack)
    public void onBack(){
        if (!isSaved) {
            final Dialog oDlg=new Dialog(this);
            oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            oDlg.setContentView(R.layout.dialog_yesno);
            oDlg.setTitle("");
            CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
            dlgTitle.setText("THÔNG BÁO");
            CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
            dlgContent.setText("Dữ liệu báo cáo kỹ thuật chưa cập nhật. Bạn có muốn bỏ qua ?");
            CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
            CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onExcuteBackPress();
                    oDlg.dismiss();
                }
            });
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oDlg.dismiss();
                    return;
                }
            });
            oDlg.show();

            return;
        } else {
            super.onBackPressed();
        }

        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @OnClick(R.id.btnReportSaleRepDetailAdd)
    public void onReportSaleRepDetailAdd(){
        final Fragment currentFragment = adapter.getItem(viewPager.getCurrentItem());

        if(currentFragment instanceof ReportSaleRepMarketItemFragment){
            if(btnReportSaleRepDetailAdd.getTag()!=null && btnReportSaleRepDetailAdd.getTag().toString().equalsIgnoreCase("SAVE")){
                if(((ReportSaleRepMarketItemFragment) currentFragment).onSaveReportSaleRepMarket()) {
                    btnReportSaleRepDetailAdd.setTag("ADD");
                    btnReportSaleRepDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_add));
                }
            }else if(btnReportSaleRepDetailAdd.getTag()!=null && btnReportSaleRepDetailAdd.getTag().toString().equalsIgnoreCase("EDIT")){
                //CHI HIỂN THỊ BOX ĐỂ CẬP NHẬT
                ((ReportSaleRepMarketItemFragment) currentFragment).onAddReportSaleRepMarket(false);
                btnReportSaleRepDetailAdd.setTag("SAVE");
                btnReportSaleRepDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }else {
                //HIỂN THỊ BOX MỚI ĐỂ THÊM
                ((ReportSaleRepMarketItemFragment) currentFragment).onAddReportSaleRepMarket(true);
                btnReportSaleRepDetailAdd.setTag("SAVE");
                btnReportSaleRepDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }
        }else if(currentFragment instanceof ReportSaleRepDiseaseItemFragment){
            if(btnReportSaleRepDetailAdd.getTag()!=null && btnReportSaleRepDetailAdd.getTag().toString().equalsIgnoreCase("SAVE")){
                if(((ReportSaleRepDiseaseItemFragment) currentFragment).onSaveReportSaleRepDisease()) {
                    btnReportSaleRepDetailAdd.setTag("ADD");
                    btnReportSaleRepDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_add));
                }
            }else if(btnReportSaleRepDetailAdd.getTag()!=null && btnReportSaleRepDetailAdd.getTag().toString().equalsIgnoreCase("EDIT")){
                //CHI HIỂN THỊ BOX ĐỂ CẬP NHẬT
                ((ReportSaleRepDiseaseItemFragment) currentFragment).onAddReportSaleRepDisease(false);
                btnReportSaleRepDetailAdd.setTag("SAVE");
                btnReportSaleRepDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }else {
                //HIỂN THỊ BOX MỚI ĐỂ THÊM
                ((ReportSaleRepDiseaseItemFragment) currentFragment).onAddReportSaleRepDisease(true);
                btnReportSaleRepDetailAdd.setTag("SAVE");
                btnReportSaleRepDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }
        }

        else if(currentFragment instanceof ReportSaleRepSeasonItemFragment){
            if(btnReportSaleRepDetailAdd.getTag()!=null && btnReportSaleRepDetailAdd.getTag().toString().equalsIgnoreCase("SAVE")){
                if(((ReportSaleRepSeasonItemFragment) currentFragment).onSaveReportSaleRepSeason()) {
                    btnReportSaleRepDetailAdd.setTag("ADD");
                    btnReportSaleRepDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_add));
                }
            }else if(btnReportSaleRepDetailAdd.getTag()!=null && btnReportSaleRepDetailAdd.getTag().toString().equalsIgnoreCase("EDIT")){
                //CHI HIỂN THỊ BOX ĐỂ CẬP NHẬT
                ((ReportSaleRepSeasonItemFragment) currentFragment).onAddReportSaleRepSeason(false);
                btnReportSaleRepDetailAdd.setTag("SAVE");
                btnReportSaleRepDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }else {
                //HIỂN THỊ BOX MỚI ĐỂ THÊM
                ((ReportSaleRepSeasonItemFragment) currentFragment).onAddReportSaleRepSeason(true);
                btnReportSaleRepDetailAdd.setTag("SAVE");
                btnReportSaleRepDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }
        }

        /*else if(currentFragment instanceof ReportSaleRepActivityItemFragment){
            if(btnReportSaleRepDetailAdd.getTag()!=null && btnReportSaleRepDetailAdd.getTag().toString().equalsIgnoreCase("SAVE")){
                if(((ReportSaleRepActivityItemFragment) currentFragment).onSaveReportSaleRepActivity()) {
                    btnReportSaleRepDetailAdd.setTag("ADD");
                    btnReportSaleRepDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_add));
                }
            }else if(btnReportSaleRepDetailAdd.getTag()!=null && btnReportSaleRepDetailAdd.getTag().toString().equalsIgnoreCase("EDIT")){
                //CHI HIỂN THỊ BOX ĐỂ CẬP NHẬT
                ((ReportSaleRepActivityItemFragment) currentFragment).onAddReportSaleRepActivity(false);
                btnReportSaleRepDetailAdd.setTag("SAVE");
                btnReportSaleRepDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }else {
                //HIỂN THỊ BOX MỚI ĐỂ THÊM
                ((ReportSaleRepActivityItemFragment) currentFragment).onAddReportSaleRepActivity(true);
                btnReportSaleRepDetailAdd.setTag("SAVE");
                btnReportSaleRepDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }
        } else if(currentFragment instanceof ReportSaleRepTaskItemFragment){
            if(btnReportSaleRepDetailAdd.getTag()!=null && btnReportSaleRepDetailAdd.getTag().toString().equalsIgnoreCase("SAVE")){
                if(((ReportSaleRepTaskItemFragment) currentFragment).onSaveReportSaleRepActivity()) {
                    btnReportSaleRepDetailAdd.setTag("ADD");
                    btnReportSaleRepDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_add));
                }
            }else if(btnReportSaleRepDetailAdd.getTag()!=null && btnReportSaleRepDetailAdd.getTag().toString().equalsIgnoreCase("EDIT")){
                //CHI HIỂN THỊ BOX ĐỂ CẬP NHẬT
                ((ReportSaleRepTaskItemFragment) currentFragment).onAddReportSaleRepActivity(false);
                btnReportSaleRepDetailAdd.setTag("SAVE");
                btnReportSaleRepDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }else {
                //HIỂN THỊ BOX MỚI ĐỂ THÊM
                ((ReportSaleRepTaskItemFragment) currentFragment).onAddReportSaleRepActivity(true);
                btnReportSaleRepDetailAdd.setTag("SAVE");
                btnReportSaleRepDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }
        }*/
    }

    @OnClick(R.id.btnReportSaleRepDetailDel)
    public void onReportSaleRepMarketDel(){
        final Fragment currentFragment = adapter.getItem(viewPager.getCurrentItem());
        if(currentFragment instanceof ReportSaleRepMarketItemFragment){
            ((ReportSaleRepMarketItemFragment) currentFragment).onDeletedReportSaleRepMarket();
        }else if(currentFragment instanceof ReportSaleRepDiseaseItemFragment){
            ((ReportSaleRepDiseaseItemFragment) currentFragment).onDeletedReportSaleRepDisease();
        }
        /*else if(currentFragment instanceof ReportSaleRepSeasonItemFragment){
            ((ReportSaleRepSeasonItemFragment) currentFragment).onDeletedReportSaleRepCompetitor();
        } else if(currentFragment instanceof ReportSaleRepActivityItemFragment){
            ((ReportSaleRepActivityItemFragment) currentFragment).onDeletedReportSaleRepActivity();
        } else if(currentFragment instanceof ReportSaleRepTaskItemFragment){
            ((ReportSaleRepTaskItemFragment) currentFragment).onDeletedReportSaleRepActivity();
        }*/
    }

    @OnClick(R.id.btnSaveReportSaleRep)
    public void onSaveOnly(){
        ReceiveDataFragment();
        if(oReportSaleRep==null || oReportSaleRep.getReportSaleId() == null || oReportSaleRep.getReportSaleId().isEmpty()){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập báo cáo sale..", Toast.LENGTH_SHORT).show();
            return;
        }

        mDB.addReportSaleRep(oReportSaleRep);

        if(mDB.getSizeReportSaleRep(oReportSaleRep.getReportSaleId())>0) {
            mDB.delReportSaleDetail(oReportSaleRep.getReportSaleId());
            // Save market report
            if(oReportSaleRepMarket != null && oReportSaleRepMarket.size() >0){
                for (SM_ReportSaleRepMarket oDetail : oReportSaleRepMarket) {
                    mDB.addReportSaleRepMarket(oDetail);
                }
            }
            // Save disease report
            if(oReportSaleRepDisease != null && oReportSaleRepDisease.size() >0){
                for (SM_ReportSaleRepDisease oDetail : oReportSaleRepDisease) {
                    mDB.addReportSaleRepDisease(oDetail);
                }
            }
            // Save season report
            if(oReportSaleRepSeason != null && oReportSaleRepSeason.size() > 0){
                for (SM_ReportSaleRepSeason oDetail : oReportSaleRepSeason) {
                    mDB.addReportSaleRepSeason(oDetail);
                }
            }

            // Save task
            if(oReportSaleRepTask != null && oReportSaleRepTask.size() > 0){
                for (SM_ReportSaleRepActivitie oDetail : oReportSaleRepTask) {
                    mDB.addReportSaleRepActivity(oDetail);
                }
            }

            // Save activity next week
            if(oReportSaleRepActivity != null && oReportSaleRepActivity.size() >0){
                for (SM_ReportSaleRepActivitie oDetail : oReportSaleRepActivity) {
                    mDB.addReportSaleRepActivity(oDetail);
                }
            }
        }

        Toast.makeText(this, "Ghi báo cáo sale thành công", Toast.LENGTH_SHORT).show();
        finish();
        isSaved=true;
    }


    @OnClick(R.id.btnPostReportSaleRep)
    public void onPostReportSaleRep(){
        ReceiveDataFragment();

        if (APINet.isNetworkAvailable(ReportSaleRepFormActivity.this)==false){
            Toast.makeText(ReportSaleRepFormActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
            return;
        }
        if(oReportSaleRep==null || oReportSaleRep.getReportSaleId().isEmpty()){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập báo cáo kỹ thuật..", Toast.LENGTH_SHORT).show();
            return;
        }
        if(oReportSaleRepMarket==null || oReportSaleRepMarket.size()<=0){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập báo cáo thị trường..", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oReportSaleRep.getReportCode().isEmpty() || oReportSaleRepMarket.size()<=0) {
            Toast.makeText(this, "Không tìm thấy số báo cáo kỹ thuật hoặc chưa nhập báo cáo thị trường", Toast.LENGTH_SHORT).show();
        }
        else{
            mDB.addReportSaleRep(oReportSaleRep);
            if(mDB.getSizeReportSaleRep(oReportSaleRep.getReportSaleId())>0) {
                for (SM_ReportSaleRepMarket oDetail : oReportSaleRepMarket) {
                    mDB.addReportSaleRepMarket(oDetail);
                }
                //onPostReportSaleRep(oReportSaleRep, oReportSaleRepMarket);
            }else{
                Toast.makeText(this, "Không thể ghi báo cáo kỹ thuật..", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        isSaved=true;
    }

    //POST ORDER
    /*private String getOrderDetailPost(List<SM_OrderDetail> lstOrderDetail){
        String mOrderDetail="";
        try{
            if(lstOrderDetail!=null){
                for (SM_OrderDetail oOdt : lstOrderDetail) {
                    String mRow="";
                    if(oOdt.getProductID()!=null && !oOdt.getProductID().isEmpty()){
                        mRow=oOdt.getProductID()+"#";
                        if(oOdt.getProductCode()!=null && !oOdt.getProductCode().isEmpty()){
                            mRow+=oOdt.getProductCode()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getUnit()!=null && !oOdt.getUnit().isEmpty()){
                            mRow+=oOdt.getUnit()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getAmount()!=null){
                            mRow+=oOdt.getAmount().toString()+"#";
                        }else{
                            mRow+="0"+"#";
                        }
                        if(oOdt.getAmountBox()!=null){
                            mRow+=oOdt.getAmountBox().toString()+"#";
                        }else{
                            mRow+="0"+"#";
                        }
                        if(oOdt.getPrice()!=null){
                            mRow+=oOdt.getPrice().toString()+"#";
                        }else{
                            mRow+="0"+"#";
                        }
                        if(oOdt.getNotes()!=null){
                            mRow+=oOdt.getNotes().toString()+"#";
                        }else{
                            mRow+="#";
                        }
                        if(oOdt.getNotes2()!=null){
                            mRow+=oOdt.getNotes2().toString()+"#";
                        }else{
                            mRow+="#";
                        }
                        mRow+="|";
                        mOrderDetail+=mRow;
                    }
                }
            }

        }catch (Exception ex){}
        return  mOrderDetail;
    }

    private void onPostOrder(final SM_Order oOd,final List<SM_OrderDetail> lstOrderDetail){
        try{
            if (APINet.isNetworkAvailable(OrderFormActivity.this)==false){
                Toast.makeText(OrderFormActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
                return;
            }

            final String Imei=AppUtils.getImeil(this);
            final String ImeiSim=AppUtils.getImeilsim(this);
            final String mDataOrderDetail=getOrderDetailPost(lstOrderDetail);

            if(ImeiSim.isEmpty()){
                Toast.makeText(this,"Không đọc được số IMEI từ thiết bị cho việc đồng bộ. Kiểm tra Sim của bạn",Toast.LENGTH_LONG).show();
                return;
            }
            if(oOd==null){
                Toast.makeText(this,"Không tìm thấy dữ liệu đơn hàng.",Toast.LENGTH_LONG).show();
                return;
            }
            if(oOd.getOrderID()==null || oOd.getOrderCode().isEmpty()){
                Toast.makeText(this,"Không tìm thấy mã đơn hàng",Toast.LENGTH_SHORT).show();
                return;
            }else if(oOd.getCustomerID()==null || oOd.getCustomerID().isEmpty()){
                Toast.makeText(this,"Bạn chưa chọn khách hàng cho đơn hàng này",Toast.LENGTH_SHORT).show();
                return;
            }

            final String mUrlPostOrder=AppSetting.getInstance().URL_PostOrder();
            try {
                if (oOd.getOrderCode() == null || oOd.getOrderCode().isEmpty()) {
                    oOd.setOrderCode("");
                }
                if (oOd.getRequestDate() == null || oOd.getRequestDate().isEmpty()) {
                    oOd.setRequestDate("");
                }
                if (oOd.getOrderDate() == null || oOd.getOrderDate().isEmpty()) {
                    oOd.setOrderDate("");
                }
                if (oOd.getMaxDebt() == null ) {
                    oOd.setMaxDebt(0);
                }
                if (oOd.getOrderNotes() == null || oOd.getOrderNotes().isEmpty()) {
                    oOd.setOrderNotes("");
                }

                if (oOd.getLatitude() == null || oOd.getLatitude().toString().isEmpty()) {
                    oOd.setLatitude(0.0);
                }
                if (oOd.getLongitude() == null || oOd.getLongitude().toString().isEmpty()) {
                    oOd.setLongitude(0.0);
                }
                if (oOd.getLocationAddress() == null || oOd.getLocationAddress().toString().isEmpty()) {
                    oOd.setLocationAddress("N/A");
                }
                if(oOd.getOrderStatus()==null){
                    oOd.setOrderStatus(1);
                }
            }catch(Exception ex){
                Toast.makeText(OrderFormActivity.this, "Không tìm thấy dữ liệu đã quét.." + ex.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            RequestBody DataBody = new FormBody.Builder()
                    .add("imei", Imei)
                    .add("imeisim", ImeiSim)
                    .add("customerid",oOd.getCustomerID())
                    .add("orderid", oOd.getOrderID())
                    .add("ordercode", oOd.getOrderCode())
                    .add("orderdate", oOd.getOrderDate())
                    .add("requestdate", oOd.getRequestDate())
                    .add("orderstatus", Integer.toString(oOd.getOrderStatus()))
                    .add("maxdebit", Integer.toString( oOd.getMaxDebt()))
                    .add("longitude", Double.toString(oOd.getLongitude()))
                    .add("latitude", Double.toString(oOd.getLatitude()))
                    .add("locationaddress", oOd.getLocationAddress())
                    .add("notes", oOd.getOrderNotes())
                    .add("orderdetail",mDataOrderDetail)
                    .build();
            new SyncPost(new APINetCallBack() {
                @Override
                public void onHttpStart() {
                    showProgressDialog("Đang đồng bộ dữ liệu về Server.");
                }
                @Override
                public void onHttpSuccess(String ResPonseRs) {
                    try {
                        dismissProgressDialog();
                        if (ResPonseRs!=null && !ResPonseRs.isEmpty()) {
                            if (ResPonseRs.contains("SYNC_OK")) {
                                Toast.makeText(OrderFormActivity.this, "Đồng  bộ thành công.", Toast.LENGTH_LONG).show();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                oOd.setPostDay(sdf.format(new Date()));
                                oOd.setPost(true);
                                oOd.setOrderStatus(2);
                                mDB.editSMOrder(oOd);
                                finish();
                            }
                            else if(ResPonseRs.contains("SYNC_REG") || ResPonseRs.contains("SYNC_NOT_REG")){
                                Toast.makeText(OrderFormActivity.this, "Thiết bị chưa được đăng ký hoặc chưa xác thực từ Server.", Toast.LENGTH_LONG).show();
                            }else if(ResPonseRs.contains("SYNC_ACTIVE")) {
                                Toast.makeText(OrderFormActivity.this, "Thiết bị chưa kích hoạt...", Toast.LENGTH_LONG).show();
                            }else if(ResPonseRs.contains("SYNC_APPROVE") || ResPonseRs.contains("SYNC_APPROVE")){
                                Toast.makeText(OrderFormActivity.this, "Đơn hàng đang được xử lý. Bạn không thể gửi điều chỉnh.", Toast.LENGTH_LONG).show();
                            }else if (ResPonseRs.contains("SYNC_BODY_NULL")) {
                                Toast.makeText(OrderFormActivity.this, "Tham số gửi lên BODY=NULL", Toast.LENGTH_LONG).show();
                            } else if (ResPonseRs.contains("SYNC_ORDERID_NULL")) {
                                Toast.makeText(OrderFormActivity.this, "Mã số ORDERID=NULL", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(OrderFormActivity.this  , "Không nhận được trang thải trả về.", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception ex){ }
                    // finish();
                }

                @Override
                public void onHttpFailer(Exception e) {
                    dismissProgressDialog();
                    Toast.makeText(OrderFormActivity.this,"Không thể đồng bộ:"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            },mUrlPostOrder,"POST_ORDER",DataBody).execute();


        }catch (Exception ex){
            Toast.makeText(OrderFormActivity.this,"Không thể đồng bộ:"+ex.getMessage(),Toast.LENGTH_LONG).show();
            dismissProgressDialog();
        }
    }*/
}
