package com.mimi.mimigroup.ui.utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.api.APINet;
import com.mimi.mimigroup.api.APINetCallBack;
import com.mimi.mimigroup.api.SyncPost;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.SM_PlanSale;
import com.mimi.mimigroup.model.SM_PlanSaleDetail;
import com.mimi.mimigroup.ui.adapter.PlanSaleAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.utils.AppUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class PlanSaleActivity extends BaseActivity {
    @BindView(R.id.rvPlanSaleList)
    RecyclerView rvPlanSaleList;

    PlanSaleAdapter adapter;
    private DBGimsHelper mDB;
    private String mfDay;
    private String mtDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_sale);
        adapter = new PlanSaleAdapter();
        mDB = DBGimsHelper.getInstance(PlanSaleActivity.this);
        rvPlanSaleList.setLayoutManager(new LinearLayoutManager(PlanSaleActivity.this));
        rvPlanSaleList.setAdapter(adapter);
        onLoadDataSource("", "");
    }

    private void onLoadDataSource(String fDay,String tDay){
        try{
            ((BaseActivity)PlanSaleActivity.this).showProgressDialog("Đang nạp danh sách kế hoạch bán hàng...");
            if(tDay=="") {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fNow = sdf.format(new Date());
                fDay = AppUtils.DateAdd(fNow, -7, "yyyy-MM-dd");
                tDay = AppUtils.DateAdd(fNow, 1, "yyyy-MM-dd");
            }
            mfDay=fDay;
            mtDay=tDay;

            ((BaseActivity)PlanSaleActivity.this).showProgressDialog("Đang nạp danh sách kế hoạch bán hàng...");
            List<SM_PlanSale> lst= mDB.getAllPlanSale(fDay,tDay);
            adapter.setPlanSaleList(lst);
            ((BaseActivity) PlanSaleActivity.this).dismissProgressDialog();
        }catch (Exception ex){
            ((BaseActivity) PlanSaleActivity.this).dismissProgressDialog();
        }
    }

    @OnClick(R.id.ivBack)
    public void onBack(){
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isEditAdd=false;
        if (requestCode == REQUEST_CODE_ADD) {
            if (resultCode == 2001) {
                onLoadDataSource(mfDay,mtDay);
            }
        }
        if(requestCode==REQUEST_CODE_EDIT){
            if (resultCode == 2001) {
                //UPDATE EDIT STATUS GRID
                SM_PlanSale oSMPay=mDB.getPlanSaleById(adapter.SelectedList.get(0).getPlanId());
            }
        }
    }

    @OnClick(R.id.btnPlanSaleMenuList)
    public void onMenuClick(View v){
        String POPUP_CONSTANT = "mPopup";
        String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
        PopupMenu popup = new PopupMenu(PlanSaleActivity.this, v);
        try{
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(POPUP_CONSTANT)) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(POPUP_FORCE_SHOW_ICON, boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        }catch (Exception ex){}
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mnuAdd:
                        onAddPlanSaleClicked();
                        return true;
                    case R.id.mnuEdit:
                        onEditPlanSaleClicked();
                        return true;
                    case R.id.mnuDel:
                        onDelPlanSaleClicked();
                        return true;
                    case R.id.mnuSynPost:
                        final Dialog oDlg=new Dialog(PlanSaleActivity.this);
                        oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        oDlg.setContentView(R.layout.dialog_yesno);
                        oDlg.setTitle("");
                        CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
                        dlgTitle.setText("XÁC NHẬN");
                        CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
                        dlgContent.setText("Bạn có chắc muốn gửi phiếu kế hoạch bán hàng ?");
                        CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
                        CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onPostPlanSale();
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
                        return true;

                    case R.id.mnuSearch:
                        onSearchPlanSaleClicked();
                        return true;

                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.mnu_plansale);
        popup.show();
    }

    final int REQUEST_CODE_ADD=1;
    final int REQUEST_CODE_EDIT=2;
    boolean isEditAdd=false;

    public void onAddPlanSaleClicked(){
        String mParSymbol=mDB.getParam("PAR_SYMBOL");
        if (mParSymbol==null || mParSymbol.isEmpty()){mParSymbol="MT";}
        SimpleDateFormat Od = new SimpleDateFormat("ddMMyyyyHHmmssSS");
        String mPlanSaleID = "KHBH"+mParSymbol+Od.format(new Date());
        if(!mPlanSaleID.isEmpty()) {
            Intent intent = new Intent(PlanSaleActivity.this, PlanSaleActivity.class);
            intent.setAction("ADD");
            intent.putExtra("PlanID",mPlanSaleID);
            intent.putExtra("PAR_SYMBOL", mParSymbol);
            startActivityForResult(intent, REQUEST_CODE_ADD);
            isEditAdd = true;
        }else{
            Toast oT= Toast.makeText(PlanSaleActivity.this,"Không thể tạo Số Phiếu..", Toast.LENGTH_SHORT);
            oT.setGravity(Gravity.CENTER,0,0);
            oT.show();
        }
    }

    public void onEditPlanSaleClicked(){
        try {
            List<SM_PlanSale> oPlanSaleSel = adapter.SelectedList;

            if(oPlanSaleSel == null || oPlanSaleSel.size() <=0) {
                Toast.makeText(PlanSaleActivity.this, "Bạn chưa chọn kế hoạch", Toast.LENGTH_SHORT).show();
                return;
            }

            if(oPlanSaleSel.size() > 1){
                Toast.makeText(PlanSaleActivity.this, "Bạn chọn quá nhiều kế hoạch để sửa. Vui lòng chọn lại...", Toast.LENGTH_SHORT).show();
                adapter.clearSelected();
                return;
            }

            if (oPlanSaleSel.get(0).getPlanId() != "") {
                String mParSymbol=mDB.getParam("PAR_SYMBOL");
                if (mParSymbol==null || mParSymbol.isEmpty()){mParSymbol="MT";}
                Intent intent = new Intent(PlanSaleActivity.this,PlanSaleActivity.class);
                intent.setAction("EDIT");
                intent.putExtra("PlanID", oPlanSaleSel.get(0).getPlanId());
                intent.putExtra("PAR_SYMBOL", mParSymbol);
                startActivityForResult(intent,REQUEST_CODE_EDIT);
                isEditAdd=true;
            } else {
                Toast.makeText(PlanSaleActivity.this, "Bạn chưa chọn kế hoạch.", Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Toast.makeText(PlanSaleActivity.this, "ERR."+ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean  onDelPlanSaleClicked(){
        final List<SM_PlanSale> lstPlanSale = adapter.SelectedList;
        if(lstPlanSale.size()<=0){
            Toast.makeText(PlanSaleActivity.this,"Bạn chưa chọn mẫu tin để xóa.",Toast.LENGTH_LONG).show();
            return false;
        }
        final Dialog oDlg=new Dialog(PlanSaleActivity.this);
        oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        oDlg.setContentView(R.layout.dialog_yesno);
        oDlg.setTitle("");
        CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
        dlgTitle.setText("XÁC NHẬN XÓA");
        CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
        dlgContent.setText("Bạn có chắc muốn xóa ?");
        CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
        CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (SM_PlanSale oPlanSale : lstPlanSale) {
                    mDB.delPlanSale(oPlanSale.getPlanId());
                }
                onLoadDataSource(mfDay,mtDay);
                Toast.makeText(PlanSaleActivity.this,"Đã xóa mẫu tin thành công",Toast.LENGTH_SHORT).show();
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
        return  true;
    }

    private int startYear, startMonth, startDay, endYear, endMonth, endDay;
    public void onSearchPlanSaleClicked(){
        try{
            LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
            View customView = inflater.inflate(R.layout.dialog_picker, null);
            final DatePicker dpStartDate = (DatePicker) customView.findViewById(R.id.dpStartDate);
            final DatePicker dpEndDate = (DatePicker) customView.findViewById(R.id.dpEndDate);

            AlertDialog.Builder builder = new AlertDialog.Builder(PlanSaleActivity.this);
            builder.setView(customView); // Set the view of the dialog to your custom layout
            builder.setTitle("");
            builder.setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startYear = dpStartDate.getYear();
                    startMonth = dpStartDate.getMonth();
                    startDay = dpStartDate.getDayOfMonth();
                    endYear = dpEndDate.getYear();
                    endMonth = dpEndDate.getMonth();
                    endDay = dpEndDate.getDayOfMonth();

                    Date dFday,dTday;
                    dFday=AppUtils.getDate(startYear,startMonth,startDay);
                    dTday=AppUtils.getDate(endYear,endMonth,endDay);

                    if(dFday.after(dTday)){
                        Toast.makeText(PlanSaleActivity.this,"Chọn khoảng thời gian không hợp lệ.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    mfDay=sdf.format(dFday);
                    mtDay=sdf.format(dTday);
                    mtDay=AppUtils.DateAdd(mtDay,1,"yyyy-MM-dd");

                    if(mfDay!="" && mtDay!="") {
                        onLoadDataSource(mfDay,mtDay);
                    }
                    dialog.dismiss();
                }});
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }});

            Dialog dialog=builder.create();
            dialog.show();

            dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialogdate);
            Button btnPositiveButton=((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            Button btnNegetiveButton=((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

            btnPositiveButton.setTextColor(getResources().getColor(R.color.ButtonDialogColor));
            btnPositiveButton.setBackgroundColor(getResources().getColor(R.color.ButtonDialogBackground));
            btnPositiveButton.setPaddingRelative(20,2,20,2);
            btnNegetiveButton.setTextColor(getResources().getColor(R.color.ButtonDialogColor2));

        }catch (Exception ex){}
    }

    public void onPostPlanSale(){
        List<SM_PlanSale> oSel = adapter.SelectedList;

        if (oSel == null || oSel.size() <= 0) {
            Toast.makeText(PlanSaleActivity.this, "Bạn chưa chọn phiếu", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oSel.size() > 1) {
            Toast.makeText(PlanSaleActivity.this, "Bạn chọn quá nhiều phiếu để sửa. Vui lòng chọn lại...", Toast.LENGTH_SHORT).show();
            adapter.clearSelected();
            return;
        }

        SM_PlanSale planSale = oSel.get(0);

        if (APINet.isNetworkAvailable(PlanSaleActivity.this)==false){
            Toast.makeText(PlanSaleActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
            return;
        }
        if(planSale==null || planSale.getPlanId().isEmpty()){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập kế hoạch bán hàng..", Toast.LENGTH_SHORT).show();
            return;
        }

        List<SM_PlanSaleDetail> oPlanSaleDetail = mDB.getAllPlanSaleDetail(planSale.getPlanId());

        onPostPlanSale(planSale, oPlanSaleDetail);

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
            if (APINet.isNetworkAvailable(PlanSaleActivity.this)==false){
                Toast.makeText(PlanSaleActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
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
                Toast.makeText(PlanSaleActivity.this, "Không tìm thấy dữ liệu đã quét.." + ex.getMessage(), Toast.LENGTH_LONG).show();
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
                                Toast.makeText(PlanSaleActivity.this, "Đồng  bộ thành công.", Toast.LENGTH_LONG).show();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                planSale.setPostDay(sdf.format(new Date()));
                                planSale.setPost(true);
                                planSale.setIsStatus("2");
                                mDB.editPlanSale(planSale);
                                finish();
                            }
                            else if(ResPonseRs.contains("SYNC_REG") || ResPonseRs.contains("SYNC_NOT_REG")){
                                Toast.makeText(PlanSaleActivity.this, "Thiết bị chưa được đăng ký hoặc chưa xác thực từ Server.", Toast.LENGTH_LONG).show();
                            }else if(ResPonseRs.contains("SYNC_ACTIVE")) {
                                Toast.makeText(PlanSaleActivity.this, "Thiết bị chưa kích hoạt...", Toast.LENGTH_LONG).show();
                            }else if(ResPonseRs.contains("SYNC_APPROVE") || ResPonseRs.contains("SYNC_APPROVE")){
                                Toast.makeText(PlanSaleActivity.this, "Đơn hàng đang được xử lý. Bạn không thể gửi điều chỉnh.", Toast.LENGTH_LONG).show();
                            }else if (ResPonseRs.contains("SYNC_BODY_NULL")) {
                                Toast.makeText(PlanSaleActivity.this, "Tham số gửi lên BODY=NULL", Toast.LENGTH_LONG).show();
                            } else if (ResPonseRs.contains("SYNC_ORDERID_NULL")) {
                                Toast.makeText(PlanSaleActivity.this, "Mã số REPORT_TECH_ID=NULL", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(PlanSaleActivity.this  , "Không nhận được trang thải trả về.", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception ex){ }
                    // finish();
                }

                @Override
                public void onHttpFailer(Exception e) {
                    dismissProgressDialog();
                    Toast.makeText(PlanSaleActivity.this,"Không thể đồng bộ:"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            },mUrlPostPlanSale,"POST_PLAN_SALE",DataBody).execute();


        }catch (Exception ex){
            Toast.makeText(PlanSaleActivity.this,"Không thể đồng bộ:"+ex.getMessage(),Toast.LENGTH_LONG).show();
            dismissProgressDialog();
        }
    }

}

