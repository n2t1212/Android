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
import com.mimi.mimigroup.model.SM_PlanSale;
import com.mimi.mimigroup.model.SM_PlanSaleDetail;
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

public class PlanSaleFormActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    FragmentPagerTabAdapter adapter;

    @BindView(R.id.btnPlanSaleDetailAdd)
    FloatingActionButton btnPlanSaleDetailAdd;
    @BindView(R.id.btnPlanSaleDetailDel)
    FloatingActionButton btnPlanSaleDetailDel;

    public String getmPlanSaleID() {
        return mPlanSaleID;
    }

    public String mPlanSaleID="";

    public String getmPar_Symbol() {
        return mPar_Symbol;
    }

    private String mPar_Symbol;
    private String mAction="";
    private DBGimsHelper mDB;
    private boolean isSaved=false;

    SM_PlanSale oPlanSale;
    List<SM_PlanSaleDetail> oPlanSaleDetail;

    PlanSaleFormItemFragment PlanSaleFragment;
    PlanSaleDetailItemFragment PlanSaleDetailFragment;

    public String getAction(){return this.mAction;}

    public SM_PlanSale getoPlanSale() {
        return oPlanSale;
    }

    public List<SM_PlanSaleDetail> getoPlanSaleDetail() {
        return oPlanSaleDetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_sale_form);
        mDB=DBGimsHelper.getInstance(this);

        adapter = new FragmentPagerTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        mPlanSaleID = getIntent().getStringExtra("PlanID");
        mPar_Symbol  = getIntent().getStringExtra("PAR_SYMBOL");
        mAction=getIntent().getAction().toString();
        if(mAction=="EDIT"){
            oPlanSale = mDB.getPlanSaleById(mPlanSaleID);
            oPlanSaleDetail = mDB.getAllPlanSaleDetail(mPlanSaleID);
        }else{
            oPlanSale = new SM_PlanSale();
            oPlanSaleDetail = new ArrayList<>();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String mReportDate = sdf.format(new Date());
            String mRequestDate= AppUtils.DateAdd(mReportDate, 3, "yyyy-MM-dd");

            SimpleDateFormat sdfOrderCode = new SimpleDateFormat("ddMMyy");
            String mOrderCodeDMY = sdfOrderCode.format(new Date());
            SimpleDateFormat sdfhhMMss = new SimpleDateFormat("hhmmss");
            String mReportCodeHMS = sdfhhMMss.format(new Date());
            String mReportCode=mPar_Symbol+'-'+mReportCodeHMS+'/'+mOrderCodeDMY;

            oPlanSale.setPlanId(mPlanSaleID);
            oPlanSale.setPlanCode(mReportCode);
            oPlanSale.setPlanDay(mReportDate);
            oPlanSale.setIsStatus("0");
            oPlanSale.setPost(false);
        }

        btnPlanSaleDetailAdd.setVisibility(View.GONE);
        btnPlanSaleDetailDel.setVisibility(View.GONE);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        PlanSaleFragment = new PlanSaleFormItemFragment();
                        adapter.addFragment(PlanSaleFragment, "Kế hoạch bán hàng");

                        PlanSaleDetailFragment = new PlanSaleDetailItemFragment();
                        adapter.addFragment(PlanSaleDetailFragment, "Kế hoạch chi tiết");

                        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                            @Override
                            public void onTabSelected(TabLayout.Tab tab) {
                                switch (tab.getPosition()){
                                    case 0:
                                        btnPlanSaleDetailAdd.setVisibility(View.INVISIBLE);
                                        btnPlanSaleDetailDel.setVisibility(View.INVISIBLE);
                                        break;
                                    case 1:
                                        btnPlanSaleDetailAdd.setVisibility(View.VISIBLE);
                                        btnPlanSaleDetailDel.setVisibility(View.INVISIBLE);
                                        btnPlanSaleDetailAdd.setTag("ADD");
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
                btnPlanSaleDetailDel.setVisibility(View.VISIBLE);
            } else {
                btnPlanSaleDetailDel.setVisibility(View.INVISIBLE);
            }
        }catch (Exception ex){}
    }
    public void setButtonEditStatus(boolean isEdit){
        if(isEdit){
            btnPlanSaleDetailAdd.setTag("EDIT");
            btnPlanSaleDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_edit));
        }else{
            btnPlanSaleDetailAdd.setTag("ADD");
            btnPlanSaleDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_add));
        }
    }
    private void ReceiveDataFragment(){
        //SAVE DETAIL
        try{
            if(btnPlanSaleDetailAdd.getTag()!=null && btnPlanSaleDetailAdd.getTag().toString().equalsIgnoreCase("SAVE")) {
                onPlanSaleDetailAdd();

            }
            if(PlanSaleFragment !=null) {
                oPlanSale = PlanSaleFragment.getSMPlanSale();
            }
            if(PlanSaleDetailFragment!=null) {
                oPlanSaleDetail = PlanSaleDetailFragment.getListPlanSaleDetail();
            }

        }catch (Exception ex){}


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
            dlgContent.setText("Dữ liệu kế hoạch bán hàng chưa cập nhật. Bạn có muốn bỏ qua ?");
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
            dlgContent.setText("Dữ liệu kế hoạch bán hàng chưa cập nhật. Bạn có muốn bỏ qua ?");
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

    @OnClick(R.id.btnPlanSaleDetailAdd)
    public void onPlanSaleDetailAdd(){
        final Fragment currentFragment = adapter.getItem(viewPager.getCurrentItem());

        if(currentFragment instanceof PlanSaleDetailItemFragment) {
            if (btnPlanSaleDetailAdd.getTag() != null && btnPlanSaleDetailAdd.getTag().toString().equalsIgnoreCase("SAVE")) {
                if (((PlanSaleDetailItemFragment) currentFragment).onSavePlanSaleDetail()) {
                    btnPlanSaleDetailAdd.setTag("ADD");
                    btnPlanSaleDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_add));
                }
            } else if (btnPlanSaleDetailAdd.getTag() != null && btnPlanSaleDetailAdd.getTag().toString().equalsIgnoreCase("EDIT")) {
                //CHI HIỂN THỊ BOX ĐỂ CẬP NHẬT
                ((PlanSaleDetailItemFragment) currentFragment).onAddPlanSaleDetail(false);
                btnPlanSaleDetailAdd.setTag("SAVE");
                btnPlanSaleDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            } else {
                //HIỂN THỊ BOX MỚI ĐỂ THÊM
                ((PlanSaleDetailItemFragment) currentFragment).onAddPlanSaleDetail(true);
                btnPlanSaleDetailAdd.setTag("SAVE");
                btnPlanSaleDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }
        }
    }

    @OnClick(R.id.btnPlanSaleDetailDel)
    public void onPlanSaleMarketDel(){
        final Fragment currentFragment = adapter.getItem(viewPager.getCurrentItem());
        if(currentFragment instanceof PlanSaleDetailItemFragment){
            ((PlanSaleDetailItemFragment) currentFragment).onDeletedPlanSaleDetail();
        }
    }

    @OnClick(R.id.btnSavePlanSale)
    public void onSaveOnly(){
        ReceiveDataFragment();
        if(oPlanSale==null || oPlanSale.getPlanId().isEmpty()){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập kế hoạch bán hàng..", Toast.LENGTH_SHORT).show();
            return;
        }

        mDB.addPlanSale(oPlanSale);

        if(mDB.getSizePlanSale(oPlanSale.getPlanId())>0) {
            mDB.delPlanSaleDetail(oPlanSale.getPlanId());
            // Save market report
            if(oPlanSaleDetail != null && oPlanSaleDetail.size() >0){
                for (SM_PlanSaleDetail oDetail : oPlanSaleDetail) {
                    mDB.addSalePlanDetail(oDetail);
                }
            }
        }

        Toast.makeText(this, "Ghi báo cáo kĩ thuật thành công", Toast.LENGTH_SHORT).show();
        finish();
        isSaved=true;
    }


    @OnClick(R.id.btnPostPlanSale)
    public void onPostPlanSale(){
        ReceiveDataFragment();

        if (APINet.isNetworkAvailable(PlanSaleFormActivity.this)==false){
            Toast.makeText(PlanSaleFormActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
            return;
        }
        if(oPlanSale==null || oPlanSale.getPlanId().isEmpty()){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập kế hoạch bán hàng..", Toast.LENGTH_SHORT).show();
            return;
        }
        if(oPlanSaleDetail==null || oPlanSaleDetail.size()<=0){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập kế hoạch bán hàng..", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oPlanSale.getPlanCode().isEmpty() || oPlanSaleDetail.size()<=0) {
            Toast.makeText(this, "Không tìm thấy số kế hoạch bán hàng hoặc chưa nhập kế hoạch bán hàng", Toast.LENGTH_SHORT).show();
        }
        else{
            mDB.addPlanSale(oPlanSale);
            if(mDB.getSizePlanSale(oPlanSale.getPlanId())>0) {

                for (SM_PlanSaleDetail oDetail : oPlanSaleDetail) {
                    mDB.addSalePlanDetail(oDetail);
                }

                onPostPlanSale(oPlanSale, oPlanSaleDetail);
            }else{
                Toast.makeText(this, "Không thể ghi kế hoạch bán hàng..", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        isSaved=true;
    }

    //POST
    private String getPlanSaleDetailData(final List<SM_PlanSaleDetail> detail){
        String mDetail="";
        try{
            if(detail!=null){
                for (SM_PlanSaleDetail oOdt : detail) {
                    String mRow="";
                    if(oOdt.getPlanDetailId()!=null && !oOdt.getPlanDetailId().isEmpty()){
                        mRow=oOdt.getPlanDetailId()+"#";
                        if(oOdt.getPlanId()!=null && !oOdt.getPlanId().isEmpty()){
                            mRow+=oOdt.getPlanId()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getCustomerId()!=null && !oOdt.getCustomerId().isEmpty()){
                            mRow+=oOdt.getCustomerId()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getProductCode()!=null && !oOdt.getProductCode().isEmpty()){
                            mRow+=oOdt.getProductCode()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getAmountBox()!=null){
                            mRow+=oOdt.getAmountBox()+"#";
                        }else{
                            mRow+="0"+"#";
                        }
                        if(oOdt.getAmount()!=null){
                            mRow+=oOdt.getAmount().toString()+"#";
                        }else{
                            mRow+="0"+"#";
                        }
                        if(oOdt.getNotes()!=null){
                            mRow+=oOdt.getNotes().toString()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getNotes2()!=null){
                            mRow+=oOdt.getNotes2().toString()+"#";
                        }else{
                            mRow+="0"+"#";
                        }
                        mRow+="|";
                        mDetail+=mRow;
                    }
                }
            }

        }catch (Exception ex){}
        return  mDetail;
    }


    private void onPostPlanSale(final SM_PlanSale planSale ,final List<SM_PlanSaleDetail> detail){
        try{
            if (APINet.isNetworkAvailable(PlanSaleFormActivity.this)==false){
                Toast.makeText(PlanSaleFormActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
                return;
            }

            final String Imei=AppUtils.getImeil(this);
            final String ImeiSim=AppUtils.getImeilsim(this);
            final String mDataPlanSaleDetail = getPlanSaleDetailData(detail);

            if(ImeiSim.isEmpty()){
                Toast.makeText(this,"Không đọc được số IMEI từ thiết bị cho việc đồng bộ. Kiểm tra Sim của bạn",Toast.LENGTH_LONG).show();
                return;
            }
            if(planSale==null){
                Toast.makeText(this,"Không tìm thấy dữ liệu kế hoạch bán hàng.",Toast.LENGTH_LONG).show();
                return;
            }
            if(planSale.getPlanId()==null || planSale.getPlanCode().isEmpty()){
                Toast.makeText(this,"Không tìm thấy mã báo cáo",Toast.LENGTH_SHORT).show();
                return;
            }

            final String mUrlPostPlanSale=AppSetting.getInstance().URL_PostPlanSale();
            try {
                if (planSale.getPlanId() == null || planSale.getPlanId().isEmpty()) {
                    planSale.setPlanId("");
                }
                if (planSale.getPlanCode() == null || planSale.getPlanCode().isEmpty()) {
                    planSale.setPlanCode("");
                }
                if (planSale.getPlanDay() == null || planSale.getPlanDay().isEmpty()) {
                    planSale.setPlanDay("");
                }
                if (planSale.getStartDay() == null || planSale.getStartDay().isEmpty()) {
                    planSale.setStartDay("");
                }
                if (planSale.getEndDay() == null || planSale.getEndDay().isEmpty()) {
                    planSale.setEndDay("");
                }
                if (planSale.getPlanName() == null || planSale.getPlanName().isEmpty()) {
                    planSale.setPlanName("");
                }
                if (planSale.getNotes() == null || planSale.getNotes().isEmpty()) {
                    planSale.setNotes("");
                }
                if(planSale.getIsStatus() == null){
                    planSale.setIsStatus("1");
                }
            }catch(Exception ex){
                Toast.makeText(PlanSaleFormActivity.this, "Không tìm thấy dữ liệu đã quét.." + ex.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            RequestBody DataBody = new FormBody.Builder()
                    .add("imei", Imei)
                    .add("imeisim", ImeiSim)
                    .add("plancode",planSale.getPlanCode())
                    .add("startday", planSale.getStartDay())
                    .add("endday", planSale.getEndDay())
                    .add("customerid", "")
                    .add("planname", planSale.getPlanName())
                    .add("isstatus", planSale.getIsStatus())
                    .add("notes", planSale.getNotes())
                    .add("plansaledetail", mDataPlanSaleDetail)
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
                                Toast.makeText(PlanSaleFormActivity.this, "Đồng  bộ thành công.", Toast.LENGTH_LONG).show();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                planSale.setPostDay(sdf.format(new Date()));
                                planSale.setPost(true);
                                planSale.setIsStatus("2");
                                mDB.editPlanSale(planSale);
                                finish();
                            }
                            else if(ResPonseRs.contains("SYNC_REG") || ResPonseRs.contains("SYNC_NOT_REG")){
                                Toast.makeText(PlanSaleFormActivity.this, "Thiết bị chưa được đăng ký hoặc chưa xác thực từ Server.", Toast.LENGTH_LONG).show();
                            }else if(ResPonseRs.contains("SYNC_ACTIVE")) {
                                Toast.makeText(PlanSaleFormActivity.this, "Thiết bị chưa kích hoạt...", Toast.LENGTH_LONG).show();
                            }else if(ResPonseRs.contains("SYNC_APPROVE") || ResPonseRs.contains("SYNC_APPROVE")){
                                Toast.makeText(PlanSaleFormActivity.this, "Đơn hàng đang được xử lý. Bạn không thể gửi điều chỉnh.", Toast.LENGTH_LONG).show();
                            }else if (ResPonseRs.contains("SYNC_BODY_NULL")) {
                                Toast.makeText(PlanSaleFormActivity.this, "Tham số gửi lên BODY=NULL", Toast.LENGTH_LONG).show();
                            } else if (ResPonseRs.contains("SYNC_ORDERID_NULL")) {
                                Toast.makeText(PlanSaleFormActivity.this, "Mã số REPORT_TECH_ID=NULL", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(PlanSaleFormActivity.this  , "Không nhận được trang thải trả về.", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception ex){ }
                    // finish();
                }

                @Override
                public void onHttpFailer(Exception e) {
                    dismissProgressDialog();
                    Toast.makeText(PlanSaleFormActivity.this,"Không thể đồng bộ:"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            },mUrlPostPlanSale,"POST_PLAN_SALE",DataBody).execute();


        }catch (Exception ex){
            Toast.makeText(PlanSaleFormActivity.this,"Không thể đồng bộ:"+ex.getMessage(),Toast.LENGTH_LONG).show();
            dismissProgressDialog();
        }
    }
}
