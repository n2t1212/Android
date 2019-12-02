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
import com.mimi.mimigroup.api.APINetCallBack;
import com.mimi.mimigroup.api.SyncPost;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.db.DBGimsHelper;
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
import okhttp3.FormBody;
import okhttp3.RequestBody;

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

    ReportSaleRepFormItemFragment ReportSaleRepFragment;
    ReportSaleRepMarketItemFragment ReportSaleRepMarketFragment;
    ReportSaleRepDiseaseItemFragment ReportSaleRepDiseaseFragment;
    ReportSaleRepSeasonItemFragment ReportSaleRepSeasonFragment;
    ReportSaleRepActivityItemFragment ReportSaleRepActivityFragment;
    ReportSaleRepTaskItemFragment ReportSaleRepTaskFragment;

    List<SM_ReportSaleRepMarket> oReportSaleRepMarket;
    List<SM_ReportSaleRepDisease> oReportSaleRepDisease;
    List<SM_ReportSaleRepSeason> oReportSaleRepSeason;
    List<SM_ReportSaleRepActivitie> oReportSaleRepActivity;
    List<SM_ReportSaleRepActivitie> oReportSaleRepTask;

    public String mReportSaleRepID="";
    private String mPar_Symbol;
    private String mAction="";
    private DBGimsHelper mDB;
    private boolean isSaved=false;
    SM_ReportSaleRep oReportSaleRep;

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
            oReportSaleRep.setIsStatus(1);
            oReportSaleRep.setPost(false);

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
            oReportSaleRep.setIsStatus(0);
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

                        ReportSaleRepTaskFragment = new ReportSaleRepTaskItemFragment();
                        adapter.addFragment(ReportSaleRepTaskFragment, "Hoạt Động Trong Tuần");

                        ReportSaleRepActivityFragment = new ReportSaleRepActivityItemFragment();
                        adapter.addFragment(ReportSaleRepActivityFragment, "Hoạt Động Tuần Tới");

                        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                            @Override
                            public void onTabSelected(TabLayout.Tab tab) {
                                if(tab.getPosition()==0){
                                    btnReportSaleRepDetailAdd.setVisibility(View.INVISIBLE);
                                    btnReportSaleRepDetailDel.setVisibility(View.INVISIBLE);
                                }else {
                                    checkSaveWhenAddOrEdit();
                                    btnReportSaleRepDetailAdd.setVisibility(View.VISIBLE);
                                    btnReportSaleRepDetailDel.setVisibility(View.INVISIBLE);
                                    btnReportSaleRepDetailAdd.setTag("ADD");
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

    private void checkSaveWhenAddOrEdit(){
        if(btnReportSaleRepDetailAdd.getTag() != null && btnReportSaleRepDetailAdd.getTag().equals("SAVE")){
            final Dialog oDlg=new Dialog(this);
            oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            oDlg.setContentView(R.layout.dialog_yesno);
            oDlg.setTitle("");
            CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
            dlgTitle.setText("THÔNG BÁO");
            CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
            dlgContent.setText("Dữ liệu chưa cập nhật. Bạn có muốn lưu ?");
            CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
            CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);
            final Fragment currentFragment = adapter.getItem(viewPager.getCurrentItem());
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isSaved = false;
                    if(currentFragment instanceof ReportSaleRepMarketItemFragment){
                        isSaved = ((ReportSaleRepMarketItemFragment) currentFragment).onSaveReportSaleRepMarket();
                    }else if(currentFragment instanceof ReportSaleRepDiseaseItemFragment){
                        isSaved = ((ReportSaleRepDiseaseItemFragment) currentFragment).onSaveReportSaleRepDisease();
                    }else if(currentFragment instanceof ReportSaleRepSeasonItemFragment){
                        isSaved = ((ReportSaleRepSeasonItemFragment) currentFragment).onSaveReportSaleRepSeason();
                    } else if(currentFragment instanceof ReportSaleRepTaskItemFragment){
                        isSaved = ((ReportSaleRepTaskItemFragment) currentFragment).onSaveReportSaleRepActivity();
                    } else if(currentFragment instanceof ReportSaleRepActivityItemFragment){
                        isSaved = ((ReportSaleRepActivityItemFragment) currentFragment).onSaveReportSaleRepActivity();
                    }

                    if(isSaved){
                        btnReportSaleRepDetailAdd.setTag("ADD");
                        btnReportSaleRepDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_add));
                    } else {
                        Toast.makeText(ReportSaleRepFormActivity.this, "Dữ liệu không hợp lệ nên thông tin không lưu được..", Toast.LENGTH_SHORT).show();
                        if(currentFragment instanceof ReportSaleRepMarketItemFragment){
                            ((ReportSaleRepMarketItemFragment) currentFragment).cancelSaveData();
                        }else if(currentFragment instanceof ReportSaleRepDiseaseItemFragment){
                            ((ReportSaleRepDiseaseItemFragment) currentFragment).cancelSaveData();
                        }else if(currentFragment instanceof ReportSaleRepSeasonItemFragment){
                            ((ReportSaleRepSeasonItemFragment) currentFragment).cancelSaveData();
                        } else if(currentFragment instanceof ReportSaleRepTaskItemFragment){
                            ((ReportSaleRepTaskItemFragment) currentFragment).cancelSaveData();
                        } else if(currentFragment instanceof ReportSaleRepActivityItemFragment){
                            ((ReportSaleRepActivityItemFragment) currentFragment).cancelSaveData();
                        }
                        btnReportSaleRepDetailAdd.setTag("ADD");
                        btnReportSaleRepDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_add));
                    }

                    oDlg.dismiss();
                }
            });
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentFragment instanceof ReportSaleRepMarketItemFragment){
                        ((ReportSaleRepMarketItemFragment) currentFragment).cancelSaveData();
                    }else if(currentFragment instanceof ReportSaleRepDiseaseItemFragment){
                        ((ReportSaleRepDiseaseItemFragment) currentFragment).cancelSaveData();
                    }else if(currentFragment instanceof ReportSaleRepSeasonItemFragment){
                        ((ReportSaleRepSeasonItemFragment) currentFragment).cancelSaveData();
                    } else if(currentFragment instanceof ReportSaleRepTaskItemFragment){
                        ((ReportSaleRepTaskItemFragment) currentFragment).cancelSaveData();
                    } else if(currentFragment instanceof ReportSaleRepActivityItemFragment){
                        ((ReportSaleRepActivityItemFragment) currentFragment).cancelSaveData();
                    }
                    btnReportSaleRepDetailAdd.setTag("ADD");
                    btnReportSaleRepDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_add));
                    oDlg.dismiss();
                    return;
                }
            });
            oDlg.show();
        }
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

    public String getmReportSaleRepID() {
        return mReportSaleRepID;
    }

    public String getmPar_Symbol() {
        return mPar_Symbol;
    }

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
        }
        if(ReportSaleRepTaskFragment != null){
            oReportSaleRepTask = ReportSaleRepTaskFragment.getListReportSaleRepTask();
        }
        if(ReportSaleRepActivityFragment != null){
            oReportSaleRepActivity = ReportSaleRepActivityFragment.getListReportSaleRepActivity();
        }
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
            dlgContent.setText("Dữ liệu báo cáo sale chưa cập nhật. Bạn có muốn bỏ qua ?");
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
            dlgContent.setText("Dữ liệu chưa cập nhật. Bạn có muốn bỏ qua ?");
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

        else if(currentFragment instanceof ReportSaleRepTaskItemFragment){
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
        } else if(currentFragment instanceof ReportSaleRepActivityItemFragment){
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
        }
    }

    @OnClick(R.id.btnReportSaleRepDetailDel)
    public void onReportSaleRepMarketDel(){
        final Fragment currentFragment = adapter.getItem(viewPager.getCurrentItem());
        if(currentFragment instanceof ReportSaleRepMarketItemFragment){
            ((ReportSaleRepMarketItemFragment) currentFragment).onDeletedReportSaleRepMarket();
        }else if(currentFragment instanceof ReportSaleRepDiseaseItemFragment){
            ((ReportSaleRepDiseaseItemFragment) currentFragment).onDeletedReportSaleRepDisease();
        }
        else if(currentFragment instanceof ReportSaleRepSeasonItemFragment){
            ((ReportSaleRepSeasonItemFragment) currentFragment).onDeletedReportSaleRepSeason();
        } else if(currentFragment instanceof ReportSaleRepActivityItemFragment){
            ((ReportSaleRepActivityItemFragment) currentFragment).onDeletedReportSaleRepActivity();
        } else if(currentFragment instanceof ReportSaleRepTaskItemFragment){
            ((ReportSaleRepTaskItemFragment) currentFragment).onDeletedReportSaleRepActivity();
        }
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

            //market report
            if(oReportSaleRepMarket != null && oReportSaleRepMarket.size() >0){
                for (SM_ReportSaleRepMarket oDetail : oReportSaleRepMarket) {
                    mDB.addReportSaleRepMarket(oDetail);
                }
            }
            //disease report
            if(oReportSaleRepDisease != null && oReportSaleRepDisease.size() >0){
                for (SM_ReportSaleRepDisease oDetail : oReportSaleRepDisease) {
                    mDB.addReportSaleRepDisease(oDetail);
                }
            }
            //season report
            if(oReportSaleRepSeason != null && oReportSaleRepSeason.size() > 0){
                for (SM_ReportSaleRepSeason oDetail : oReportSaleRepSeason) {
                    mDB.addReportSaleRepSeason(oDetail);
                }
            }

            //task
            if(oReportSaleRepTask != null && oReportSaleRepTask.size() > 0){
                for (SM_ReportSaleRepActivitie oDetail : oReportSaleRepTask) {
                    mDB.addReportSaleRepActivity(oDetail);
                }
            }

            //activity next week
            if(oReportSaleRepActivity != null && oReportSaleRepActivity.size() >0){
                for (SM_ReportSaleRepActivitie oDetail : oReportSaleRepActivity) {
                    mDB.addReportSaleRepActivity(oDetail);
                }
            }
        }

        Toast.makeText(this, "Ghi báo cáo thành công", Toast.LENGTH_SHORT).show();
        setResult(2001);
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
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập báo cáo sale..", Toast.LENGTH_SHORT).show();
            return;
        }
        if(oReportSaleRepMarket==null || oReportSaleRepMarket.size()<=0){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập báo cáo thị trường..", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oReportSaleRep.getReportCode().isEmpty() || oReportSaleRepMarket.size()<=0) {
            Toast.makeText(this, "Không tìm thấy số báo cáo sale hoặc chưa nhập báo cáo thị trường", Toast.LENGTH_SHORT).show();
        }
        else{
            mDB.addReportSaleRep(oReportSaleRep);
            if(mDB.getSizeReportSaleRep(oReportSaleRep.getReportSaleId())>0) {
                for (SM_ReportSaleRepMarket oDetail : oReportSaleRepMarket) {
                    mDB.addReportSaleRepMarket(oDetail);
                }
                onPostReportSaleRep(oReportSaleRep, oReportSaleRepMarket, oReportSaleRepDisease, oReportSaleRepSeason, oReportSaleRepTask,oReportSaleRepActivity);
            }else{
                Toast.makeText(this, "Không thể ghi báo cáo sale..", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        isSaved=true;
    }

    //POST ORDER
    private String getSaleMarketData(List<SM_ReportSaleRepMarket> markets){
        String mSaleDetail="";
        try{
            if(markets!=null){
                for (SM_ReportSaleRepMarket market : markets) {
                    String mRow="";
                    if(market.getMarketId()!=null && !market.getMarketId().isEmpty()){
                        mRow=market.getMarketId()+"#";
                        if(market.getReportSaleId()!=null && !market.getReportSaleId().isEmpty()){
                            mRow+=market.getReportSaleId()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(market.getCustomerId()!=null && !market.getCustomerId().isEmpty()){
                            mRow+=market.getCustomerId()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(market.getCompanyName()!=null && !market.getCompanyName().isEmpty()){
                            mRow+=market.getCompanyName()+"#";
                        }else{
                            mRow+="0"+"#";
                        }
                        if(market.getProductCode()!=null && !market.getProductCode().isEmpty()){
                            mRow+=market.getProductCode().toString()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(market.getNotes()!=null && !market.getNotes().isEmpty()){
                            mRow+=market.getNotes()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(market.getPrice()!=null){
                            mRow+=market.getPrice().toString()+"#";
                        }else{
                            mRow+="0"+ "#";
                        }
                        mRow+="|";
                        mSaleDetail+=mRow;
                    }
                }
            }

        }catch (Exception ex){}
        return  mSaleDetail;
    }

    private String getSaleDiseaseData(List<SM_ReportSaleRepDisease> diseases){
        String mSaleDetail="";
        try{
            if(diseases!=null){
                for (SM_ReportSaleRepDisease disease : diseases) {
                    String mRow="";
                    if(disease.getDiseaseId()!=null && !disease.getDiseaseId().isEmpty()){
                        mRow=disease.getDiseaseId()+"#";
                        if(disease.getReportSaleId()!=null && !disease.getReportSaleId().isEmpty()){
                            mRow+=disease.getReportSaleId()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(disease.getTreeCode()!=null && !disease.getTreeCode().isEmpty()){
                            mRow+=disease.getTreeCode()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(disease.getTitle()!=null && !disease.getTitle().isEmpty()){
                            mRow+=disease.getTitle()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(disease.getArceage()!=null){
                            mRow+=disease.getArceage().toString()+"#";
                        }else{
                            mRow+="0"+"#";
                        }
                        if(disease.getNotes()!=null && !disease.getNotes().isEmpty()){
                            mRow+=disease.getNotes()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        mRow+="|";
                        mSaleDetail+=mRow;
                    }
                }
            }

        }catch (Exception ex){}
        return  mSaleDetail;
    }

    private String getSaleSeasonData(List<SM_ReportSaleRepSeason> seasons){
        String mSaleDetail="";
        try{
            if(seasons!=null){
                for (SM_ReportSaleRepSeason season : seasons) {
                    String mRow="";
                    if(season.getSeasonId()!=null && !season.getSeasonId().isEmpty()){
                        mRow=season.getSeasonId()+"#";
                        if(season.getReportSaleId()!=null && !season.getReportSaleId().isEmpty()){
                            mRow+=season.getReportSaleId()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(season.getTreeCode()!=null && !season.getTreeCode().isEmpty()){
                            mRow+=season.getTreeCode()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(season.getSeasonCode()!=null && !season.getSeasonCode().isEmpty()){
                            mRow+=season.getSeasonCode()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(season.getTitle()!=null && !season.getTitle().isEmpty()){
                            mRow+=season.getTitle()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(season.getAcreage()!=null){
                            mRow+=season.getAcreage().toString()+"#";
                        }else{
                            mRow+="0"+"#";
                        }
                        if(season.getNotes()!=null && !season.getNotes().isEmpty()){
                            mRow+=season.getNotes()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        mRow+="|";
                        mSaleDetail+=mRow;
                    }
                }
            }

        }catch (Exception ex){}
        return  mSaleDetail;
    }

    private String getSaleActivityData(List<SM_ReportSaleRepActivitie> tasks, List<SM_ReportSaleRepActivitie> activities){
        String mSaleDetail="";
        try{
            if(tasks != null){
                tasks = new ArrayList<>();
            }
            if(activities != null && activities.size() > 0){
                for(SM_ReportSaleRepActivitie o: activities){
                    tasks.add(o);
                }
            }
            if(tasks!=null){
                for (SM_ReportSaleRepActivitie activitie : tasks) {
                    String mRow="";
                    if(activitie.getActivitieId()!=null && !activitie.getActivitieId().isEmpty()){
                        mRow=activitie.getActivitieId()+"#";
                        if(activitie.getReportSaleId()!=null && !activitie.getReportSaleId().isEmpty()){
                            mRow+=activitie.getReportSaleId()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(activitie.getIsType()!=null && !activitie.getIsType().isEmpty()){
                            mRow+=activitie.getIsType()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(activitie.getWorkDay()!=null && !activitie.getWorkDay().isEmpty()){
                            mRow+=activitie.getWorkDay()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(activitie.getTitle()!=null && !activitie.getTitle().isEmpty()){
                            mRow+=activitie.getTitle()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(activitie.getPlace()!=null && !activitie.getPlace().isEmpty()){
                            mRow+=activitie.getPlace()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(activitie.getNotes()!=null && !activitie.getNotes().isEmpty()){
                            mRow+=activitie.getNotes()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        mRow+="|";
                        mSaleDetail+=mRow;
                    }
                }
            }

        }catch (Exception ex){}
        return  mSaleDetail;
    }

    private void onPostReportSaleRep(final SM_ReportSaleRep sale, List<SM_ReportSaleRepMarket> markets, List<SM_ReportSaleRepDisease> diseases,
                                     List<SM_ReportSaleRepSeason> seasons, List<SM_ReportSaleRepActivitie> tasks, List<SM_ReportSaleRepActivitie> activities){
        try{
            if (APINet.isNetworkAvailable(ReportSaleRepFormActivity.this)==false){
                Toast.makeText(ReportSaleRepFormActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
                return;
            }

            final String Imei=AppUtils.getImeil(this);
            final String ImeiSim=AppUtils.getImeilsim(this);
            final String mDataReportSaleMarket=getSaleMarketData(markets);
            final String mDataReportSaleDisease=getSaleDiseaseData(diseases);
            final String mDataReportSaleSeason=getSaleSeasonData(seasons);
            final String mDataReportSaleActivity=getSaleActivityData(tasks,activities);


            if(ImeiSim.isEmpty()){
                Toast.makeText(this,"Không đọc được số IMEI từ thiết bị cho việc đồng bộ. Kiểm tra Sim của bạn",Toast.LENGTH_LONG).show();
                return;
            }
            if(sale==null){
                Toast.makeText(this,"Không tìm thấy dữ liệu báo cáo sale.",Toast.LENGTH_LONG).show();
                return;
            }
            if(sale.getReportSaleId()==null || sale.getReportSaleId().isEmpty()){
                Toast.makeText(this,"Không tìm thấy mã báo cáo",Toast.LENGTH_SHORT).show();
                return;
            }

            final String mUrlPostSale=AppSetting.getInstance().URL_PostReportSale();
            try {
                if (sale.getReportSaleId() == null || sale.getReportSaleId().isEmpty()) {
                    sale.setReportSaleId("");
                }
                if (sale.getReportCode() == null || sale.getReportCode().isEmpty()) {
                    sale.setReportCode("");
                }
                if (sale.getReportName() == null || sale.getReportName().isEmpty()) {
                    sale.setReportName("");
                }
                if (sale.getReportDay() == null && sale.getReportDay().isEmpty()) {
                    sale.setReportDay("");
                }
                if (sale.getLongtitude() == null) {
                    sale.setLongtitude(0f);
                }

                if (sale.getLatitude() == null || sale.getLatitude().toString().isEmpty()) {
                    sale.setLatitude(0f);
                }
                if (sale.getLocationAddress() == null || sale.getLocationAddress().toString().isEmpty()) {
                    sale.setLocationAddress("N/A");
                }
                if(sale.getReceiverList()==null && sale.getReceiverList().isEmpty()){
                    sale.setReceiverList("");
                }
                if(sale.getNotes()==null && sale.getNotes().isEmpty()){
                    sale.setNotes("");
                }
                if(sale.getIsStatus()==null){
                    sale.setIsStatus(1);
                }
            }catch(Exception ex){
                Toast.makeText(ReportSaleRepFormActivity.this, "Không tìm thấy dữ liệu đã quét.." + ex.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            RequestBody DataBody = new FormBody.Builder()
                    .add("imei", Imei)
                    .add("imeisim", ImeiSim)
                    .add("reportcode",sale.getReportCode())
                    .add("reportday", sale.getReportDay())
                    .add("reportname", sale.getReportName())
                    .add("customerid", "")
                    .add("longitude", Float.toString(sale.getLongtitude()))
                    .add("latitude", Float.toString(sale.getLatitude()))
                    .add("locationaddress", sale.getLocationAddress())
                    .add("receiverlist", sale.getReceiverList())
                    .add("notes",sale.getNotes())
                    .add("isstatus", Integer.toString(sale.getIsStatus()))
                    .add("reportsalerepmarket", mDataReportSaleMarket)
                    .add("reportsalerepdisease",mDataReportSaleDisease)
                    .add("reportsalerepseason",mDataReportSaleSeason)
                    .add("reportsalerepactivity",mDataReportSaleActivity)
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
                                Toast.makeText(ReportSaleRepFormActivity.this, "Đồng  bộ thành công.", Toast.LENGTH_LONG).show();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                sale.setPostDay(sdf.format(new Date()));
                                sale.setPost(true);
                                sale.setIsStatus(2);
                                mDB.editReportSale(sale);
                                setResult(2001);
                                finish();
                            }
                            else if(ResPonseRs.contains("SYNC_REG") || ResPonseRs.contains("SYNC_NOT_REG")){
                                Toast.makeText(ReportSaleRepFormActivity.this, "Thiết bị chưa được đăng ký hoặc chưa xác thực từ Server.", Toast.LENGTH_LONG).show();
                            }else if(ResPonseRs.contains("SYNC_ACTIVE")) {
                                Toast.makeText(ReportSaleRepFormActivity.this, "Thiết bị chưa kích hoạt...", Toast.LENGTH_LONG).show();
                            }else if(ResPonseRs.contains("SYNC_APPROVE") || ResPonseRs.contains("SYNC_APPROVE")){
                                Toast.makeText(ReportSaleRepFormActivity.this, "Đơn hàng đang được xử lý. Bạn không thể gửi điều chỉnh.", Toast.LENGTH_LONG).show();
                            }else if (ResPonseRs.contains("SYNC_BODY_NULL")) {
                                Toast.makeText(ReportSaleRepFormActivity.this, "Tham số gửi lên BODY=NULL", Toast.LENGTH_LONG).show();
                            } else if (ResPonseRs.contains("SYNC_ORDERID_NULL")) {
                                Toast.makeText(ReportSaleRepFormActivity.this, "Mã số SALEID=NULL", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(ReportSaleRepFormActivity.this  , "Không nhận được trang thải trả về.", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception ex){ }
                    // finish();
                }

                @Override
                public void onHttpFailer(Exception e) {
                    dismissProgressDialog();
                    Toast.makeText(ReportSaleRepFormActivity.this,"Không thể đồng bộ:"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            },mUrlPostSale,"POST_REPORT_SALE",DataBody).execute();


        }catch (Exception ex){
            Toast.makeText(ReportSaleRepFormActivity.this,"Không thể đồng bộ:"+ex.getMessage(),Toast.LENGTH_LONG).show();
            dismissProgressDialog();
        }
    }
}
