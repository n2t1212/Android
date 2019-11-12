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
import com.mimi.mimigroup.model.SM_ReportTech;
import com.mimi.mimigroup.model.SM_ReportTechActivity;
import com.mimi.mimigroup.model.SM_ReportTechCompetitor;
import com.mimi.mimigroup.model.SM_ReportTechDisease;
import com.mimi.mimigroup.model.SM_ReportTechMarket;
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

    public String getmReportTechID() {
        return mReportTechID;
    }

    public String mReportTechID="";

    public String getmPar_Symbol() {
        return mPar_Symbol;
    }

    private String mPar_Symbol;
    private String mAction="";
    private DBGimsHelper mDB;
    private boolean isSaved=false;

    SM_ReportTech oReportTech;
    List<SM_ReportTechMarket> oReportTechMarket;
    List<SM_ReportTechDisease> oReportTechDisease;
    List<SM_ReportTechCompetitor> oReportTechCompetitor;
    List<SM_ReportTechActivity> oReportTechActivityThisWeek;
    List<SM_ReportTechActivity> oReportTechActivityNextWeek;

    ReportTechFormItemFragment ReportTechFragment;
    ReportTechMarketItemFragment ReportTechMarketFragment;
    ReportTechCompetitorItemFragment ReportTechCompetitorFragment;
    ReportTechActivityThisWeekItemFragment ReportTechActivityThisWeekFragment;
    ReportTechActivityNextWeekItemFragment ReportTechActivityNextWeekFragment;

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

    public List<SM_ReportTechActivity> getoReportTechActivityThisWeek() {
        return oReportTechActivityThisWeek;
    }
    public List<SM_ReportTechActivity> getoReportTechActivityNextWeek() {
        return oReportTechActivityNextWeek;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_tech_form);
        mDB=DBGimsHelper.getInstance(this);

        adapter = new FragmentPagerTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        mReportTechID = getIntent().getStringExtra("ReportTechID");
        mPar_Symbol  = getIntent().getStringExtra("PAR_SYMBOL");
        mAction=getIntent().getAction().toString();
        if(mAction=="EDIT"){
            oReportTech = mDB.getReportTechById(mReportTechID);
            oReportTechMarket = mDB.getAllReportTechMarket(mReportTechID);
            oReportTechDisease = mDB.getAllReportTechDisease(mReportTechID);
            oReportTechCompetitor = mDB.getAllReportTechCompetitor(mReportTechID);
            oReportTechActivityThisWeek = mDB.getAllReportTechActivity(mReportTechID, 0);
            oReportTechActivityNextWeek = mDB.getAllReportTechActivity(mReportTechID, 1);
        }else{
            oReportTech = new SM_ReportTech();
            oReportTechMarket = new ArrayList<>();
            oReportTechDisease = new ArrayList<>();
            oReportTechCompetitor= new ArrayList<>();
            oReportTechActivityThisWeek = new ArrayList<>();
            oReportTechActivityNextWeek = new ArrayList<>();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String mReportDate = sdf.format(new Date());
            String mRequestDate= AppUtils.DateAdd(mReportDate, 3, "yyyy-MM-dd");

            SimpleDateFormat sdfOrderCode = new SimpleDateFormat("ddMMyy");
            String mOrderCodeDMY = sdfOrderCode.format(new Date());
            SimpleDateFormat sdfhhMMss = new SimpleDateFormat("hhmmss");
            String mReportCodeHMS = sdfhhMMss.format(new Date());
            String mReportCode=mPar_Symbol+'-'+mReportCodeHMS+'/'+mOrderCodeDMY;

            oReportTech.setReportTechId(mReportTechID);
            oReportTech.setReportCode(mReportCode);
            oReportTech.setReportDate(mReportDate);
            oReportTech.setIsStatus("0");
            oReportTech.setPost(false);
        }

        btnReportTechDetailAdd.setVisibility(View.GONE);
        btnReportTechDetailDel.setVisibility(View.GONE);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        ReportTechFragment = new ReportTechFormItemFragment();
                        adapter.addFragment(ReportTechFragment, "Báo Cáo Kỹ Thuật");

                        ReportTechMarketFragment = new ReportTechMarketItemFragment();
                        adapter.addFragment(ReportTechMarketFragment, "Thị Trường");

                        ReportTechCompetitorFragment = new ReportTechCompetitorItemFragment();
                        adapter.addFragment(ReportTechCompetitorFragment, "Đối Thủ");

                        ReportTechActivityThisWeekFragment = new ReportTechActivityThisWeekItemFragment();
                        adapter.addFragment(ReportTechActivityThisWeekFragment, "Trong Tuần");

                        ReportTechActivityNextWeekFragment = new ReportTechActivityNextWeekItemFragment();
                        adapter.addFragment(ReportTechActivityNextWeekFragment, "Tuần Tới");

                        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                            @Override
                            public void onTabSelected(TabLayout.Tab tab) {
                                switch (tab.getPosition()){
                                    case 0:
                                        btnReportTechDetailAdd.setVisibility(View.INVISIBLE);
                                        btnReportTechDetailDel.setVisibility(View.INVISIBLE);
                                        break;
                                    case 1:
                                        btnReportTechDetailAdd.setVisibility(View.VISIBLE);
                                        btnReportTechDetailDel.setVisibility(View.INVISIBLE);
                                        btnReportTechDetailAdd.setTag("ADD");
                                        break;
                                    case 2:
                                        btnReportTechDetailAdd.setVisibility(View.VISIBLE);
                                        btnReportTechDetailDel.setVisibility(View.INVISIBLE);
                                        btnReportTechDetailAdd.setTag("ADD");
                                        break;
                                    case 3:
                                        btnReportTechDetailAdd.setVisibility(View.VISIBLE);
                                        btnReportTechDetailDel.setVisibility(View.INVISIBLE);
                                        btnReportTechDetailAdd.setTag("ADD");
                                        break;
                                    case 4:
                                        btnReportTechDetailAdd.setVisibility(View.VISIBLE);
                                        btnReportTechDetailDel.setVisibility(View.INVISIBLE);
                                        btnReportTechDetailAdd.setTag("ADD");
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


    /*[TRANSFER DATA FOR FRAMGMENT]*/
    public void setVisibleDetailDelete(boolean isVisible){
        try {
            if (isVisible) {
                btnReportTechDetailDel.setVisibility(View.VISIBLE);
            } else {
                btnReportTechDetailDel.setVisibility(View.INVISIBLE);
            }
        }catch (Exception ex){}
    }
    public void setButtonEditStatus(boolean isEdit){
        if(isEdit){
            btnReportTechDetailAdd.setTag("EDIT");
            btnReportTechDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_edit));
        }else{
            btnReportTechDetailAdd.setTag("ADD");
            btnReportTechDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_add));
        }
    }
    private void ReceiveDataFragment(){
        //SAVE DETAIL
        try{
            if(btnReportTechDetailAdd.getTag()!=null && btnReportTechDetailAdd.getTag().toString().equalsIgnoreCase("SAVE")) {
                onReportTechDetailAdd();

            }
        }catch (Exception ex){}

        if(ReportTechFragment !=null) {
            oReportTech = ReportTechFragment.getSMReportTech();
        }
        if(ReportTechMarketFragment!=null) {
            oReportTechMarket = ReportTechMarketFragment.getListReportTechMarket();
        }
        if(ReportTechCompetitorFragment!=null) {
            oReportTechCompetitor = ReportTechCompetitorFragment.getListReportTechCompetitor();
        }
        if(ReportTechActivityThisWeekFragment != null){
            oReportTechActivityThisWeek = ReportTechActivityThisWeekFragment.getListReportTechActivityThisWeek();
        }
        if(ReportTechActivityNextWeekFragment != null){
            oReportTechActivityNextWeek = ReportTechActivityNextWeekFragment.getListReportTechActivityNextWeek();
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

    @OnClick(R.id.btnReportTechDetailAdd)
    public void onReportTechDetailAdd(){
        final Fragment currentFragment = adapter.getItem(viewPager.getCurrentItem());

        if(currentFragment instanceof ReportTechMarketItemFragment){
            if(btnReportTechDetailAdd.getTag()!=null && btnReportTechDetailAdd.getTag().toString().equalsIgnoreCase("SAVE")){
                if(((ReportTechMarketItemFragment) currentFragment).onSaveReportTechMarket()) {
                    btnReportTechDetailAdd.setTag("ADD");
                    btnReportTechDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_add));
                }
            }else if(btnReportTechDetailAdd.getTag()!=null && btnReportTechDetailAdd.getTag().toString().equalsIgnoreCase("EDIT")){
                //CHI HIỂN THỊ BOX ĐỂ CẬP NHẬT
                ((ReportTechMarketItemFragment) currentFragment).onAddReportTechMarket(false);
                btnReportTechDetailAdd.setTag("SAVE");
                btnReportTechDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }else {
                //HIỂN THỊ BOX MỚI ĐỂ THÊM
                ((ReportTechMarketItemFragment) currentFragment).onAddReportTechMarket(true);
                btnReportTechDetailAdd.setTag("SAVE");
                btnReportTechDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }
        } else if(currentFragment instanceof ReportTechCompetitorItemFragment){
            if(btnReportTechDetailAdd.getTag()!=null && btnReportTechDetailAdd.getTag().toString().equalsIgnoreCase("SAVE")){
                if(((ReportTechCompetitorItemFragment) currentFragment).onSaveReportTechCompetitor()) {
                    btnReportTechDetailAdd.setTag("ADD");
                    btnReportTechDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_add));
                }
            }else if(btnReportTechDetailAdd.getTag()!=null && btnReportTechDetailAdd.getTag().toString().equalsIgnoreCase("EDIT")){
                //CHI HIỂN THỊ BOX ĐỂ CẬP NHẬT
                ((ReportTechCompetitorItemFragment) currentFragment).onAddReportTechCompetitor(false);
                btnReportTechDetailAdd.setTag("SAVE");
                btnReportTechDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }else {
                //HIỂN THỊ BOX MỚI ĐỂ THÊM
                ((ReportTechCompetitorItemFragment) currentFragment).onAddReportTechCompetitor(true);
                btnReportTechDetailAdd.setTag("SAVE");
                btnReportTechDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }
        } else if(currentFragment instanceof ReportTechActivityThisWeekItemFragment){
            if(btnReportTechDetailAdd.getTag()!=null && btnReportTechDetailAdd.getTag().toString().equalsIgnoreCase("SAVE")){
                if(((ReportTechActivityThisWeekItemFragment) currentFragment).onSaveReportTechActivity()) {
                    btnReportTechDetailAdd.setTag("ADD");
                    btnReportTechDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_add));
                }
            }else if(btnReportTechDetailAdd.getTag()!=null && btnReportTechDetailAdd.getTag().toString().equalsIgnoreCase("EDIT")){
                //CHI HIỂN THỊ BOX ĐỂ CẬP NHẬT
                ((ReportTechActivityThisWeekItemFragment) currentFragment).onAddReportTechActivity(false);
                btnReportTechDetailAdd.setTag("SAVE");
                btnReportTechDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }else {
                //HIỂN THỊ BOX MỚI ĐỂ THÊM
                ((ReportTechActivityThisWeekItemFragment) currentFragment).onAddReportTechActivity(true);
                btnReportTechDetailAdd.setTag("SAVE");
                btnReportTechDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }
        } else if(currentFragment instanceof ReportTechActivityNextWeekItemFragment){
            if(btnReportTechDetailAdd.getTag()!=null && btnReportTechDetailAdd.getTag().toString().equalsIgnoreCase("SAVE")){
                if(((ReportTechActivityNextWeekItemFragment) currentFragment).onSaveReportTechActivity()) {
                    btnReportTechDetailAdd.setTag("ADD");
                    btnReportTechDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_add));
                }
            }else if(btnReportTechDetailAdd.getTag()!=null && btnReportTechDetailAdd.getTag().toString().equalsIgnoreCase("EDIT")){
                //CHI HIỂN THỊ BOX ĐỂ CẬP NHẬT
                ((ReportTechActivityNextWeekItemFragment) currentFragment).onAddReportTechActivity(false);
                btnReportTechDetailAdd.setTag("SAVE");
                btnReportTechDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }else {
                //HIỂN THỊ BOX MỚI ĐỂ THÊM
                ((ReportTechActivityNextWeekItemFragment) currentFragment).onAddReportTechActivity(true);
                btnReportTechDetailAdd.setTag("SAVE");
                btnReportTechDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }
        }
    }

    @OnClick(R.id.btnReportTechDetailDel)
    public void onReportTechMarketDel(){
        final Fragment currentFragment = adapter.getItem(viewPager.getCurrentItem());
        if(currentFragment instanceof ReportTechMarketItemFragment){
            ((ReportTechMarketItemFragment) currentFragment).onDeletedReportTechMarket();
        } else if(currentFragment instanceof ReportTechCompetitorItemFragment){
            ((ReportTechCompetitorItemFragment) currentFragment).onDeletedReportTechCompetitor();
        } else if(currentFragment instanceof ReportTechActivityThisWeekItemFragment){
            ((ReportTechActivityThisWeekItemFragment) currentFragment).onDeletedReportTechActivity();
        } else if(currentFragment instanceof ReportTechActivityNextWeekItemFragment){
            ((ReportTechActivityNextWeekItemFragment) currentFragment).onDeletedReportTechActivity();
        }
    }

    @OnClick(R.id.btnSaveReportTech)
    public void onSaveOnly(){
        ReceiveDataFragment();
        if(oReportTech==null || oReportTech.getReportTechId().isEmpty()){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập báo cáo kỹ thuật..", Toast.LENGTH_SHORT).show();
            return;
        }
        /*if(oReportTechMarket==null || oReportTechMarket.size()<=0){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập báo cáo thị trường..", Toast.LENGTH_SHORT).show();
            return;
        }
        if(oReportTechCompetitor==null || oReportTechCompetitor.size()<=0){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập báo cáo đối thủ..", Toast.LENGTH_SHORT).show();
            return;
        }
        if(oReportTechActivityThisWeek==null || oReportTechActivityThisWeek.size()<=0){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập báo cáo trong tuần trường..", Toast.LENGTH_SHORT).show();
            return;
        }*/

        mDB.addReportTech(oReportTech);

        if(mDB.getSizeReportTech(oReportTech.getReportTechId())>0) {
            mDB.delReportTechDetail(oReportTech.getReportTechId());
            // Save market report
            if(oReportTechMarket != null && oReportTechMarket.size() >0){
                for (SM_ReportTechMarket oDetail : oReportTechMarket) {
                    mDB.addReportTechMarket(oDetail);
                }
            }
            // Save competitor report
            if(oReportTechCompetitor != null && oReportTechCompetitor.size() > 0){
                for (SM_ReportTechCompetitor oDetail : oReportTechCompetitor) {
                    mDB.addReportTechCompetitor(oDetail);
                }
            }

            // Save activity this week
            if(oReportTechActivityThisWeek != null && oReportTechActivityThisWeek.size() > 0){
                for (SM_ReportTechActivity oDetail : oReportTechActivityThisWeek) {
                    mDB.addReportTechActivity(oDetail);
                }
            }

            // Save activity next week
            if(oReportTechActivityNextWeek != null && oReportTechActivityNextWeek.size() >0){
                for (SM_ReportTechActivity oDetail : oReportTechActivityNextWeek) {
                    mDB.addReportTechActivity(oDetail);
                }
            }
        }

        Toast.makeText(this, "Ghi báo cáo kĩ thuật thành công", Toast.LENGTH_SHORT).show();
        finish();
        isSaved=true;
    }


    @OnClick(R.id.btnPostReportTech)
    public void onPostReportTech(){
        ReceiveDataFragment();

        if (APINet.isNetworkAvailable(ReportTechFormActivity.this)==false){
            Toast.makeText(ReportTechFormActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
            return;
        }
        if(oReportTech==null || oReportTech.getReportTechId().isEmpty()){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập báo cáo kỹ thuật..", Toast.LENGTH_SHORT).show();
            return;
        }
        if(oReportTechMarket==null || oReportTechMarket.size()<=0){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập báo cáo thị trường..", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oReportTech.getReportCode().isEmpty() || oReportTechMarket.size()<=0) {
            Toast.makeText(this, "Không tìm thấy số báo cáo kỹ thuật hoặc chưa nhập báo cáo thị trường", Toast.LENGTH_SHORT).show();
        }
        else{
            mDB.addReportTech(oReportTech);
            if(mDB.getSizeReportTech(oReportTech.getReportTechId())>0) {
                for (SM_ReportTechMarket oDetail : oReportTechMarket) {
                    mDB.addReportTechMarket(oDetail);
                }
                //onPostReportTech(oReportTech, oReportTechMarket);
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
