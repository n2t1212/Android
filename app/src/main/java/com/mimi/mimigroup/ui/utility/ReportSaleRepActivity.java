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
import com.mimi.mimigroup.model.SM_ReportSaleRep;
import com.mimi.mimigroup.model.SM_ReportSaleRepActivitie;
import com.mimi.mimigroup.model.SM_ReportSaleRepDisease;
import com.mimi.mimigroup.model.SM_ReportSaleRepMarket;
import com.mimi.mimigroup.model.SM_ReportSaleRepSeason;
import com.mimi.mimigroup.ui.adapter.ReportSaleRepAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.utils.AppUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class ReportSaleRepActivity extends BaseActivity {
    @BindView(R.id.rvReportSaleRepList)
    RecyclerView rvReportSaleRepList;

    ReportSaleRepAdapter adapter;
    private DBGimsHelper mDB;
    private String mfDay;
    private String mtDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_sale);
        adapter = new ReportSaleRepAdapter();
        mDB = DBGimsHelper.getInstance(ReportSaleRepActivity.this);
        rvReportSaleRepList.setLayoutManager(new LinearLayoutManager(ReportSaleRepActivity.this));
        rvReportSaleRepList.setAdapter(adapter);
        onLoadDataSource("", "");
    }

    private void onLoadDataSource(String fDay,String tDay){
        try{
            ((BaseActivity)ReportSaleRepActivity.this).showProgressDialog("Đang nạp danh sách báo cáo sale...");
            if(tDay=="") {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fNow = sdf.format(new Date());
                fDay = AppUtils.DateAdd(fNow, -7, "yyyy-MM-dd");
                tDay = AppUtils.DateAdd(fNow, 1, "yyyy-MM-dd");
            }
            mfDay=fDay;
            mtDay=tDay;

            ((BaseActivity)ReportSaleRepActivity.this).showProgressDialog("Đang nạp danh sách báo cáo sale...");
            List<SM_ReportSaleRep> lst= mDB.getAllReportSaleRep(fDay,tDay);
            adapter.setReportSaleList(lst);
            ((BaseActivity) ReportSaleRepActivity.this).dismissProgressDialog();
        }catch (Exception ex){
            ((BaseActivity) ReportSaleRepActivity.this).dismissProgressDialog();
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
                onLoadDataSource("","");
            }
        }
        if(requestCode==REQUEST_CODE_EDIT){
            if (resultCode == 2001) {
                onLoadDataSource("","");
            }
        }
    }

    @OnClick(R.id.btnReportSaleRepMenuList)
    public void onMenuClick(View v){
        String POPUP_CONSTANT = "mPopup";
        String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
        PopupMenu popup = new PopupMenu(ReportSaleRepActivity.this, v);
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
                        onAddReportSaleRepClicked();
                        return true;
                    case R.id.mnuEdit:
                        onEditReportSaleRepClicked();
                        return true;
                    case R.id.mnuDel:
                        onDelReportSaleRepClicked();
                        return true;
                    case R.id.mnuSynPost:
                        final Dialog oDlg=new Dialog(ReportSaleRepActivity.this);
                        oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        oDlg.setContentView(R.layout.dialog_yesno);
                        oDlg.setTitle("");
                        CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
                        dlgTitle.setText("XÁC NHẬN");
                        CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
                        dlgContent.setText("Bạn có chắc muốn gửi phiếu báo cáo sale ?");
                        CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
                        CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onPostReportSaleRep();
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
                        onSearchReportSaleRepClicked();
                        return true;

                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.mnu_reportsalerep);
        popup.show();
    }

    final int REQUEST_CODE_ADD=1;
    final int REQUEST_CODE_EDIT=2;
    boolean isEditAdd=false;
    public void onAddReportSaleRepClicked(){
        String mParSymbol=mDB.getParam("PAR_SYMBOL");
        if (mParSymbol==null || mParSymbol.isEmpty()){mParSymbol="MT";}
        SimpleDateFormat Od = new SimpleDateFormat("ddMMyyyyHHmmssSS");
        String mReportSaleRepID = "BCSALE"+mParSymbol+Od.format(new Date());
        if(!mReportSaleRepID.isEmpty()) {
            Intent intent = new Intent(ReportSaleRepActivity.this, ReportSaleRepFormActivity.class);
            intent.setAction("ADD");
            intent.putExtra("ReportSaleRepID",mReportSaleRepID);
            intent.putExtra("PAR_SYMBOL", mParSymbol);
            startActivityForResult(intent, REQUEST_CODE_ADD);
            isEditAdd = true;
        }else{
            Toast oT= Toast.makeText(ReportSaleRepActivity.this,"Không thể tạo Số Phiếu..", Toast.LENGTH_SHORT);
            oT.setGravity(Gravity.CENTER,0,0);
            oT.show();
        }
    }

    public void onEditReportSaleRepClicked(){
        try {
            List<SM_ReportSaleRep> oReportSaleRepSel = adapter.SelectedList;

            if(oReportSaleRepSel == null || oReportSaleRepSel.size() <=0) {
                Toast.makeText(ReportSaleRepActivity.this, "Bạn chưa chọn báo cáo", Toast.LENGTH_SHORT).show();
                return;
            }
            if(oReportSaleRepSel.size() > 1){
                Toast.makeText(ReportSaleRepActivity.this, "Bạn chọn quá nhiều báo cáo để sửa. Vui lòng chọn lại...", Toast.LENGTH_SHORT).show();
                adapter.clearSelected();
                return;
            }
            if (oReportSaleRepSel.get(0).getReportSaleId() != "") {
                String mParSymbol=mDB.getParam("PAR_SYMBOL");
                if (mParSymbol==null || mParSymbol.isEmpty()){mParSymbol="MT";}
                Intent intent = new Intent(ReportSaleRepActivity.this,ReportSaleRepFormActivity.class);
                intent.setAction("EDIT");
                intent.putExtra("ReportSaleRepID", oReportSaleRepSel.get(0).getReportSaleId());
                intent.putExtra("PAR_SYMBOL", mParSymbol);
                startActivityForResult(intent,REQUEST_CODE_EDIT);
                isEditAdd=true;
            } else {
                Toast.makeText(ReportSaleRepActivity.this, "Bạn chưa chọn báo cáo.", Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Toast.makeText(ReportSaleRepActivity.this, "ERR."+ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean  onDelReportSaleRepClicked(){
        final List<SM_ReportSaleRep> lstReportSaleRep = adapter.SelectedList;
        if(lstReportSaleRep.size()<=0){
            Toast.makeText(ReportSaleRepActivity.this,"Bạn chưa chọn mẫu tin để xóa.",Toast.LENGTH_LONG).show();
            return false;
        }
        final Dialog oDlg=new Dialog(ReportSaleRepActivity.this);
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
                for (SM_ReportSaleRep oReportSaleRep : lstReportSaleRep) {
                    mDB.delReportSale(oReportSaleRep.getReportSaleId());
                }
                onLoadDataSource(mfDay,mtDay);
                Toast.makeText(ReportSaleRepActivity.this,"Đã xóa mẫu tin thành công",Toast.LENGTH_SHORT).show();
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
    public void onSearchReportSaleRepClicked(){
        try{
            LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
            View customView = inflater.inflate(R.layout.dialog_picker, null);
            final DatePicker dpStartDate = (DatePicker) customView.findViewById(R.id.dpStartDate);
            final DatePicker dpEndDate = (DatePicker) customView.findViewById(R.id.dpEndDate);

            AlertDialog.Builder builder = new AlertDialog.Builder(ReportSaleRepActivity.this);
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
                        Toast.makeText(ReportSaleRepActivity.this,"Chọn khoảng thời gian không hợp lệ.",Toast.LENGTH_SHORT).show();
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

    public void onPostReportSaleRep(){
        List<SM_ReportSaleRep> oSel = adapter.SelectedList;

        if (oSel == null || oSel.size() <= 0) {
            Toast.makeText(ReportSaleRepActivity.this, "Bạn chưa chọn phiếu", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oSel.size() > 1) {
            Toast.makeText(ReportSaleRepActivity.this, "Bạn chọn quá nhiều phiếu để sửa. Vui lòng chọn lại...", Toast.LENGTH_SHORT).show();
            adapter.clearSelected();
            return;
        }
        SM_ReportSaleRep SaleRep = oSel.get(0);
        if (APINet.isNetworkAvailable(ReportSaleRepActivity.this)==false){
            Toast.makeText(ReportSaleRepActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
            return;
        }
        if(SaleRep==null || SaleRep.getReportSaleId().isEmpty()){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập báo cáo sale..", Toast.LENGTH_SHORT).show();
            return;
        }
        List<SM_ReportSaleRepMarket> oReportSaleRepMarket = mDB.getAllReportSaleRepMarket(SaleRep.getReportSaleId());
        List<SM_ReportSaleRepDisease> oReportSaleRepDisease = mDB.getAllReportSaleRepDisease(SaleRep.getReportSaleId());
        List<SM_ReportSaleRepSeason> oReportSaleRepSeason = mDB.getAllReportSaleRepSeason(SaleRep.getReportSaleId());
        List<SM_ReportSaleRepActivitie> oReportSaleRepActivityTask = mDB.getAllReportSaleRepActivity(SaleRep.getReportSaleId(), 0);
        List<SM_ReportSaleRepActivitie> oReportSaleRepActivityActivity = mDB.getAllReportSaleRepActivity(SaleRep.getReportSaleId(), 1);

        onPostReportSaleRep(SaleRep, oReportSaleRepMarket, oReportSaleRepDisease, oReportSaleRepSeason, oReportSaleRepActivityTask, oReportSaleRepActivityActivity);
    }
    
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
                                     List<SM_ReportSaleRepSeason> seasons, List<SM_ReportSaleRepActivitie> tasks, List<SM_ReportSaleRepActivitie> activities) {
        try {
            if (APINet.isNetworkAvailable(ReportSaleRepActivity.this) == false) {
                Toast.makeText(ReportSaleRepActivity.this, "Máy chưa kết nối mạng..", Toast.LENGTH_LONG).show();
                return;
            }
            final String Imei = AppUtils.getImeil(this);
            final String ImeiSim = AppUtils.getImeilsim(this);
            final String mDataReportSaleMarket = getSaleMarketData(markets);
            final String mDataReportSaleDisease = getSaleDiseaseData(diseases);
            final String mDataReportSaleSeason = getSaleSeasonData(seasons);
            final String mDataReportSaleActivity = getSaleActivityData(tasks, activities);

            if (ImeiSim.isEmpty()) {
                Toast.makeText(this, "Không đọc được số IMEI từ thiết bị cho việc đồng bộ. Kiểm tra Sim của bạn", Toast.LENGTH_LONG).show();
                return;
            }
            if (sale == null) {
                Toast.makeText(this, "Không tìm thấy dữ liệu báo cáo sale.", Toast.LENGTH_LONG).show();
                return;
            }
            if (sale.getReportSaleId() == null || sale.getReportSaleId().isEmpty()) {
                Toast.makeText(this, "Không tìm thấy mã báo cáo", Toast.LENGTH_SHORT).show();
                return;
            }
            final String mUrlPostSale = AppSetting.getInstance().URL_PostReportSale();
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
                if (sale.getReceiverList() == null && sale.getReceiverList().isEmpty()) {
                    sale.setReceiverList("");
                }
                if (sale.getNotes() == null && sale.getNotes().isEmpty()) {
                    sale.setNotes("");
                }
                sale.setIsStatus(1);
            } catch (Exception ex) {
                Toast.makeText(ReportSaleRepActivity.this, "Không tìm thấy dữ liệu đã quét.." + ex.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            RequestBody DataBody = new FormBody.Builder()
                    .add("imei", Imei)
                    .add("imeisim", ImeiSim)
                    .add("reportcode", sale.getReportCode())
                    .add("reportday", sale.getReportDay())
                    .add("reportname", sale.getReportName())
                    .add("customerid", "")
                    .add("longitude", Float.toString(sale.getLongtitude()))
                    .add("latitude", Float.toString(sale.getLatitude()))
                    .add("locationaddress", sale.getLocationAddress())
                    .add("receiverlist", sale.getReceiverList())
                    .add("notes", sale.getNotes())
                    .add("isstatus", Integer.toString(sale.getIsStatus()))
                    .add("reportsalerepmarket", mDataReportSaleMarket)
                    .add("reportsalerepdisease", mDataReportSaleDisease)
                    .add("reportsalerepseason", mDataReportSaleSeason)
                    .add("reportsalerepactivity", mDataReportSaleActivity)
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
                        if (ResPonseRs != null && !ResPonseRs.isEmpty()) {
                            if (ResPonseRs.contains("SYNC_OK")) {
                                Toast.makeText(ReportSaleRepActivity.this, "Đồng  bộ thành công.", Toast.LENGTH_LONG).show();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                sale.setPostDay(sdf.format(new Date()));
                                sale.setPost(true);
                                sale.setIsStatus(2);
                                mDB.editReportSale(sale);
                                setResult(2001);
                                finish();
                            } else if (ResPonseRs.contains("SYNC_REG") || ResPonseRs.contains("SYNC_NOT_REG")) {
                                Toast.makeText(ReportSaleRepActivity.this, "Thiết bị chưa được đăng ký hoặc chưa xác thực từ Server.", Toast.LENGTH_LONG).show();
                            } else if (ResPonseRs.contains("SYNC_ACTIVE")) {
                                Toast.makeText(ReportSaleRepActivity.this, "Thiết bị chưa kích hoạt...", Toast.LENGTH_LONG).show();
                            } else if (ResPonseRs.contains("SYNC_APPROVE") || ResPonseRs.contains("SYNC_APPROVE")) {
                                Toast.makeText(ReportSaleRepActivity.this, "Đơn hàng đang được xử lý. Bạn không thể gửi điều chỉnh.", Toast.LENGTH_LONG).show();
                            } else if (ResPonseRs.contains("SYNC_BODY_NULL")) {
                                Toast.makeText(ReportSaleRepActivity.this, "Tham số gửi lên BODY=NULL", Toast.LENGTH_LONG).show();
                            } else if (ResPonseRs.contains("SYNC_ORDERID_NULL")) {
                                Toast.makeText(ReportSaleRepActivity.this, "Mã số ORDERID=NULL", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ReportSaleRepActivity.this, "Không nhận được trang thải trả về.", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ex) {
                    }
                    // finish();
                }

                @Override
                public void onHttpFailer(Exception e) {
                    dismissProgressDialog();
                    Toast.makeText(ReportSaleRepActivity.this, "Không thể đồng bộ:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }, mUrlPostSale, "POST_REPORT_SALE", DataBody).execute();


        } catch (Exception ex) {
            Toast.makeText(ReportSaleRepActivity.this, "Không thể đồng bộ:" + ex.getMessage(), Toast.LENGTH_LONG).show();
            dismissProgressDialog();
        }
    }
}

