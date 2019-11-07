package com.mimi.mimigroup.ui.utility;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.SM_CustomerPay;
import com.mimi.mimigroup.model.SM_ReportTech;
import com.mimi.mimigroup.ui.adapter.ReportTechAdapter;
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

public class ReportTechActivity extends BaseActivity {
    @BindView(R.id.rvReportTechList)
    RecyclerView rvReportTechList;

    ReportTechAdapter adapter;
    private DBGimsHelper mDB;
    private String mfDay;
    private String mtDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_tech);
        adapter = new ReportTechAdapter();
        mDB = DBGimsHelper.getInstance(ReportTechActivity.this);
        rvReportTechList.setLayoutManager(new LinearLayoutManager(ReportTechActivity.this));
        rvReportTechList.setAdapter(adapter);
        onLoadDataSource("", "");
    }

    private void onLoadDataSource(String fDay,String tDay){
        try{
            ((BaseActivity)ReportTechActivity.this).showProgressDialog("Đang nạp danh sách báo cáo kỹ thuật...");
            if(tDay=="") {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fNow = sdf.format(new Date());
                fDay = AppUtils.DateAdd(fNow, -7, "yyyy-MM-dd");
                tDay = AppUtils.DateAdd(fNow, 1, "yyyy-MM-dd");
            }
            mfDay=fDay;
            mtDay=tDay;
            ((BaseActivity)ReportTechActivity.this).showProgressDialog("Đang nạp danh sách báo cáo kỹ thuật...");
            List<SM_ReportTech> lst= mDB.getAllReportTech(fDay,tDay);
            adapter.setReportTechList(lst);
            ((BaseActivity) ReportTechActivity.this).dismissProgressDialog();
        }catch (Exception ex){
            ((BaseActivity) ReportTechActivity.this).dismissProgressDialog();
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
                SM_ReportTech oSMPay=mDB.getReportTechById(adapter.SelectedList.get(0).getReportTechId());
            }
        }
    }

    @OnClick(R.id.btnReportTechMenuList)
    public void onMenuClick(View v){
        String POPUP_CONSTANT = "mPopup";
        String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
        PopupMenu popup = new PopupMenu(ReportTechActivity.this, v);
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
                        onAddReportTechClicked();
                        return true;
                    case R.id.mnuEdit:
                        onEditReportTechClicked();
                        return true;
                    case R.id.mnuDel:
                        onDelReportTechClicked();
                        return true;
                    case R.id.mnuSynPost:
                        final Dialog oDlg=new Dialog(ReportTechActivity.this);
                        oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        oDlg.setContentView(R.layout.dialog_yesno);
                        oDlg.setTitle("");
                        CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
                        dlgTitle.setText("XÁC NHẬN");
                        CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
                        dlgContent.setText("Bạn có chắc muốn gửi phiếu báo cáo kỹ thuật ?");
                        CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
                        CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onPostReportTech();
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
                        onSearchReportTechClicked();
                        return true;

                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.mnu_reporttech);
        popup.show();
    }

    final int REQUEST_CODE_ADD=1;
    final int REQUEST_CODE_EDIT=2;
    boolean isEditAdd=false;

    public void onAddReportTechClicked(){
        String mParSymbol=mDB.getParam("PAR_SYMBOL");
        if (mParSymbol==null || mParSymbol.isEmpty()){mParSymbol="MT";}
        SimpleDateFormat Od = new SimpleDateFormat("ddMMyyyyHHmmssSS");
        String mReportTechID = "BCKT"+mParSymbol+Od.format(new Date());
        if(!mReportTechID.isEmpty()) {
            Intent intent = new Intent(ReportTechActivity.this, ReportTechFormActivity.class);
            intent.setAction("ADD");
            intent.putExtra("ReportTechID",mReportTechID);
            intent.putExtra("PAR_SYMBOL", mParSymbol);
            startActivityForResult(intent, REQUEST_CODE_ADD);
            isEditAdd = true;
        }else{
            Toast oT= Toast.makeText(ReportTechActivity.this,"Không thể tạo Số Phiếu..", Toast.LENGTH_SHORT);
            oT.setGravity(Gravity.CENTER,0,0);
            oT.show();
        }
    }

}
