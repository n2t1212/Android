package com.mimi.mimigroup.ui.utility;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.mimi.mimigroup.model.SM_CustomerPay;
import com.mimi.mimigroup.ui.adapter.CustomerPayAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.utils.AppUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class CustomerPayActivity extends BaseActivity{
    @BindView(R.id.rvPayList)
    RecyclerView rvPayList;

    CustomerPayAdapter adapter;
    private DBGimsHelper mDB;
    private String mfDay;
    private String mtDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerpay);
        adapter = new CustomerPayAdapter();
        mDB = DBGimsHelper.getInstance(CustomerPayActivity.this);
        rvPayList.setLayoutManager(new LinearLayoutManager(CustomerPayActivity.this));
        rvPayList.setAdapter(adapter);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                 != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                 != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 50);
            }
        }*/

        onLoadDataSource("", "");
    }


    private void onLoadDataSource(String fDay,String tDay){
        try{
            ((BaseActivity)CustomerPayActivity.this).showProgressDialog("Đang nạp danh sách phiếu thu...");
            if(tDay=="") {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fNow = sdf.format(new Date());
                fDay = AppUtils.DateAdd(fNow, -7, "yyyy-MM-dd");
                tDay = AppUtils.DateAdd(fNow, 1, "yyyy-MM-dd");
            }
            mfDay=fDay;
            mtDay=tDay;
            ((BaseActivity)CustomerPayActivity.this).showProgressDialog("Đang nạp danh sách phiếu thu...");
            List<SM_CustomerPay> lstPay= mDB.getAllCustomerPay(fDay,tDay);
            adapter.setCustomerPayList(lstPay);
            ((BaseActivity) CustomerPayActivity.this).dismissProgressDialog();
        }catch (Exception ex){
            ((BaseActivity) CustomerPayActivity.this).dismissProgressDialog();
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
                SM_CustomerPay oSMPay=mDB.getCustomerPay(adapter.SelectedList.get(0).getPayID());
                //adapter.setUpdate(oSMPay,oCus.getEdit());
            }
        }
    }



    @OnClick(R.id.btnPayMenuList)
    public void onMenuClick(View v){
        String POPUP_CONSTANT = "mPopup";
        String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
        PopupMenu popup = new PopupMenu(CustomerPayActivity.this, v);
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
                        onAddPayClicked();
                        return true;
                    case R.id.mnuEdit:
                        onEditPayClicked();
                        return true;
                    case R.id.mnuDel:
                       onDelPayClicked();
                        return true;
                    case R.id.mnuSynPost:
                        final Dialog oDlg=new Dialog(CustomerPayActivity.this);
                        oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        oDlg.setContentView(R.layout.dialog_yesno);
                        oDlg.setTitle("");
                        CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
                        dlgTitle.setText("XÁC NHẬN");
                        CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
                        dlgContent.setText("Bạn có chắc muốn gửi phiếu thu ?");
                        CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
                        CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onPostPay();
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
                        onSearchPayClicked();
                        return true;

                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.mnu_customerpay);
        popup.show();
    }




    final int REQUEST_CODE_ADD=1;
    final int REQUEST_CODE_EDIT=2;
    boolean isEditAdd=false;
    public void onAddPayClicked(){
        String mParSymbol=mDB.getParam("PAR_SYMBOL");
        if (mParSymbol==null || mParSymbol.isEmpty()){mParSymbol="MT";}
        SimpleDateFormat Od = new SimpleDateFormat("ddMMyyyyHHmmssSS");
        String mPayID = "PT"+mParSymbol+Od.format(new Date());
        if(!mPayID.isEmpty()) {
            Intent intent = new Intent(CustomerPayActivity.this, CustomerPayFormActivity.class);
            intent.setAction("ADD");
            intent.putExtra("PayID",mPayID);
            intent.putExtra("PAR_SYMBOL", mParSymbol);
            startActivityForResult(intent, REQUEST_CODE_ADD);
            isEditAdd = true;
        }else{
            Toast oT= Toast.makeText(CustomerPayActivity.this,"Không thể tạo Số Phiếu..", Toast.LENGTH_SHORT);
            oT.setGravity(Gravity.CENTER,0,0);
            oT.show();
        }
    }

    public void onEditPayClicked(){
        try {
            List<SM_CustomerPay> oPaySel = adapter.SelectedList;
            if (oPaySel == null || oPaySel.size() <= 0) {
                Toast.makeText(CustomerPayActivity.this, "Bạn chưa chọn phiếu", Toast.LENGTH_SHORT).show();
                return;
            }
            if (oPaySel.size() > 1) {
                Toast.makeText(CustomerPayActivity.this, "Bạn chọn quá nhiều phiếu để sửa. Vui lòng chọn lại...", Toast.LENGTH_SHORT).show();
                adapter.clearSelected();
                return;
            }
            if (oPaySel.get(0).getPayID() != "") {
                String mParSymbol=mDB.getParam("PAR_SYMBOL");
                if (mParSymbol==null || mParSymbol.isEmpty()){mParSymbol="MT";}
                Intent intent = new Intent(CustomerPayActivity.this,CustomerPayFormActivity.class);
                intent.setAction("EDIT");
                intent.putExtra("PayID", oPaySel.get(0).getPayID());
                intent.putExtra("PAR_SYMBOL", mParSymbol);
                startActivityForResult(intent,REQUEST_CODE_EDIT);
                isEditAdd=true;
            } else {
                Toast.makeText(CustomerPayActivity.this, "Bạn chưa chọn phiếu.", Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Toast.makeText(CustomerPayActivity.this, "ERR."+ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean  onDelPayClicked(){
        final List<SM_CustomerPay> lstPay=adapter.SelectedList;
        if(lstPay.size()<=0){
            Toast.makeText(CustomerPayActivity.this,"Bạn chưa chọn mẫu tin để xóa.",Toast.LENGTH_LONG).show();
            return false;
        }
        final Dialog oDlg=new Dialog(CustomerPayActivity.this);
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
                for (SM_CustomerPay oPay : lstPay) {
                    mDB.delCustomerPay(oPay);
                }
                onLoadDataSource(mfDay,mtDay);
                Toast.makeText(CustomerPayActivity.this,"Đã xóa mẫu tin thành công",Toast.LENGTH_SHORT).show();
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



    //TÌM KIẾM THEO NGÀY
    private int startYear, startMonth, startDay, endYear, endMonth, endDay;
    public void onSearchPayClicked(){
        try{
            LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
            View customView = inflater.inflate(R.layout.dialog_picker, null);
            final DatePicker dpStartDate = (DatePicker) customView.findViewById(R.id.dpStartDate);
            final DatePicker dpEndDate = (DatePicker) customView.findViewById(R.id.dpEndDate);

            AlertDialog.Builder builder = new AlertDialog.Builder(CustomerPayActivity.this);
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
                        Toast.makeText(CustomerPayActivity.this,"Chọn khoản thời gian không hợp lệ.",Toast.LENGTH_SHORT).show();
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



    public void onPostPay(){
        List<SM_CustomerPay> oPaySel = adapter.SelectedList;
        if (oPaySel == null || oPaySel.size() <= 0) {
            Toast.makeText(CustomerPayActivity.this, "Bạn chưa chọn phiếu", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oPaySel.size() > 1) {
            Toast.makeText(CustomerPayActivity.this, "Bạn chọn quá nhiều phiếu để sửa. Vui lòng chọn lại...", Toast.LENGTH_SHORT).show();
            adapter.clearSelected();
            return;
        }

        SM_CustomerPay oPay=oPaySel.get(0);
        if(oPay==null || oPay.getPayID().isEmpty()){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập phiếu thu..", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oPay.getCustomerID().isEmpty()) {
            Toast.makeText(this, "Bạn chưa chọn khách hàng..", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oPay.getPayCode().isEmpty()) {
            Toast.makeText(this, "Không phát sinh được số phiếu..", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oPay.getPayMoney().equals(0) || oPay.getPayMoney()<0) {
            Toast.makeText(this, "Bạn chưa nhập số tiền cần thu..", Toast.LENGTH_SHORT).show();
            return;
        }
        if(oPay.getPayDate().isEmpty()){
            Toast.makeText(this, "Bạn chưa nhập ngày thu..", Toast.LENGTH_SHORT).show();
            return;
        }
        onRunPostPay(oPay);
    }


    private void onRunPostPay(final SM_CustomerPay oP){
        try{
            if (APINet.isNetworkAvailable(CustomerPayActivity.this)==false){
                Toast.makeText(CustomerPayActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
                return;
            }
            final String Imei=AppUtils.getImeil(this);
            final String ImeiSim=AppUtils.getImeilsim(this);

            if(ImeiSim.isEmpty()){
                Toast.makeText(this,"Không đọc được số IMEI từ thiết bị cho việc đồng bộ. Kiểm tra Sim của bạn",Toast.LENGTH_LONG).show();
                return;
            }
            if(oP==null){
                Toast.makeText(this,"Không tìm thấy dữ liệu phiếu thu.",Toast.LENGTH_LONG).show();
                return;
            }
            final String mUrlPostPay=AppSetting.getInstance().URL_PostPay();
            try {
                if (oP.getPayCode() == null || oP.getPayCode().isEmpty()) {
                    oP.setPayCode("");
                }
                if (oP.getPayName() == null || oP.getPayName().isEmpty()) {
                    oP.setPayName("");
                }
                if (oP.getPayDate() == null || oP.getPayDate().isEmpty()) {
                    oP.setPayDate("");
                }
                if (oP.getPayMoney() == null || oP.getPayMoney()<0) {
                    oP.setPayMoney(Double.valueOf("0"));
                }
                if (oP.getPayNotes() == null || oP.getPayNotes().isEmpty()) {
                    oP.setPayNotes("");
                }
                if (oP.getPayPic() == null || oP.getPayPic().isEmpty()) {
                    oP.setPayPic("");
                }

                if (oP.getLatitude() == null || oP.getLatitude().toString().isEmpty()) {
                    oP.setLatitude(0.0);
                }
                if (oP.getLongitude() == null || oP.getLongitude().toString().isEmpty()) {
                    oP.setLongitude(0.0);
                }
                if (oP.getLocationAddress() == null || oP.getLocationAddress().toString().isEmpty()) {
                    oP.setLocationAddress("N/A");
                }
                if(oP.getPayStatus()==null){
                    oP.setPayStatus(1);
                }
            }catch(Exception ex){
                Toast.makeText(CustomerPayActivity.this, "Không tìm thấy dữ liệu đã quét.." + ex.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            RequestBody DataBody = new FormBody.Builder()
                    .add("imei", Imei)
                    .add("imeisim", ImeiSim)
                    .add("customerid",oP.getCustomerID())
                    .add("payid", oP.getPayID())
                    .add("paycode", oP.getPayCode())
                    .add("payname", oP.getPayName())
                    .add("paydate", oP.getPayDate())
                    .add("paystatus", Integer.toString(oP.getPayStatus()))
                    .add("paymoney", Double.toString( oP.getPayMoney()))
                    .add("longitude", Double.toString(oP.getLongitude()))
                    .add("latitude", Double.toString(oP.getLatitude()))
                    .add("locationaddress", oP.getLocationAddress())
                    .add("paynotes", oP.getPayNotes())
                    .add("paypic", oP.getPayPic())
                    .add("paypicbase", oP.getPayPicBase())

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
                                Toast.makeText(CustomerPayActivity.this, "Đồng  bộ thành công.", Toast.LENGTH_LONG).show();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a",Locale.US);
                                oP.setPostDay(sdf.format(new Date()));
                                oP.setPost(true);
                                oP.setPayStatus(2);
                                mDB.editCustomerPayStatus(oP);
                                finish();
                            }
                            else if(ResPonseRs.contains("SYNC_REG") || ResPonseRs.contains("SYNC_NOT_REG")){
                                Toast.makeText(CustomerPayActivity.this, "Thiết bị chưa được đăng ký hoặc chưa xác thực từ Server.", Toast.LENGTH_LONG).show();
                            }else if(ResPonseRs.contains("SYNC_ACTIVE")) {
                                Toast.makeText(CustomerPayActivity.this, "Thiết bị chưa kích hoạt...", Toast.LENGTH_LONG).show();
                            }else if(ResPonseRs.contains("SYNC_APPROVE") || ResPonseRs.contains("SYNC_APPROVE")){
                                Toast.makeText(CustomerPayActivity.this, "Đơn hàng đang được xử lý. Bạn không thể gửi điều chỉnh.", Toast.LENGTH_LONG).show();
                            }else if (ResPonseRs.contains("SYNC_BODY_NULL")) {
                                Toast.makeText(CustomerPayActivity.this, "Tham số gửi lên BODY=NULL", Toast.LENGTH_LONG).show();
                            } else if (ResPonseRs.contains("SYNC_ORDERID_NULL")) {
                                Toast.makeText(CustomerPayActivity.this, "Mã số ORDERID=NULL", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(CustomerPayActivity.this  , "Không nhận được trang thải trả về.", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception ex){ }
                    // finish();
                }

                @Override
                public void onHttpFailer(Exception e) {
                    dismissProgressDialog();
                    Toast.makeText(CustomerPayActivity.this,"Không thể đồng bộ:"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            },mUrlPostPay,"POST_PAY",DataBody).execute();
        }catch (Exception ex){
            Toast.makeText(CustomerPayActivity.this,"Không thể đồng bộ:"+ex.getMessage(),Toast.LENGTH_LONG).show();
            dismissProgressDialog();
        }
    }



}
