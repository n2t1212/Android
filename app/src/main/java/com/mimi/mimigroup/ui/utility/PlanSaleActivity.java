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
                                //onPostPlanSale();
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
            Intent intent = new Intent(PlanSaleActivity.this, PlanSaleFormActivity.class);
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
                Intent intent = new Intent(PlanSaleActivity.this,PlanSaleFormActivity.class);
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

    /*public void onPostPlanSale(){
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

        SM_PlanSale tech = oSel.get(0);

        if (APINet.isNetworkAvailable(PlanSaleActivity.this)==false){
            Toast.makeText(PlanSaleActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
            return;
        }
        if(tech==null || tech.getPlanSaleId().isEmpty()){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập kế hoạch bán hàng..", Toast.LENGTH_SHORT).show();
            return;
        }

        List<SM_PlanSaleMarket> oPlanSaleMarket = mDB.getAllPlanSaleMarket(tech.getPlanSaleId());
        List<SM_PlanSaleDisease> oPlanSaleDisease = mDB.getAllPlanSaleDisease(tech.getPlanSaleId());

        onPostPlanSale(tech, oPlanSaleMarket, oPlanSaleDisease, oPlanSaleCompetitor, oPlanSaleActivityThisWeek, oPlanSaleActivityNextWeek);

    }

    //POST 
    private String getPlanSaleMarketData(final List<SM_PlanSaleMarket> markets){
        String mTechDetail="";
        try{
            if(markets!=null){
                for (SM_PlanSaleMarket oOdt : markets) {
                    String mRow="";
                    if(oOdt.getMarketId()!=null && !oOdt.getMarketId().isEmpty()){
                        mRow=oOdt.getMarketId()+"#";
                        if(oOdt.getPlanSaleId()!=null && !oOdt.getPlanSaleId().isEmpty()){
                            mRow+=oOdt.getPlanSaleId()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getTitle()!=null && !oOdt.getTitle().isEmpty()){
                            mRow+=oOdt.getTitle()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getNotes()!=null && !oOdt.getNotes().isEmpty()){
                            mRow+=oOdt.getNotes()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getUsefull()!=null && !oOdt.getUsefull().isEmpty()){
                            mRow+=oOdt.getUsefull()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getHarmful()!=null && !oOdt.getHarmful().isEmpty()){
                            mRow+=oOdt.getHarmful().toString()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        mRow+="|";
                        mTechDetail+=mRow;
                    }
                }
            }

        }catch (Exception ex){}
        return  mTechDetail;
    }

    private String getPlanSaleDiseaseData(final List<SM_PlanSaleDisease> diseases){
        String mTechDetail="";
        try{
            if(diseases!=null){
                for (SM_PlanSaleDisease oOdt : diseases) {
                    String mRow="";
                    if(oOdt.getDiseaseId()!=null && !oOdt.getDiseaseId().isEmpty()){
                        mRow=oOdt.getDiseaseId()+"#";
                        if(oOdt.getPlanSaleId()!=null && !oOdt.getPlanSaleId().isEmpty()){
                            mRow+=oOdt.getPlanSaleId()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getTreeCode()!=null && !oOdt.getTreeCode().isEmpty()){
                            mRow+=oOdt.getTreeCode()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getTitle()!=null && !oOdt.getTitle().isEmpty()){
                            mRow+=oOdt.getTitle()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getAcreage()!=null){
                            mRow+=oOdt.getAcreage()+"#";
                        }else{
                            mRow+="0"+"#";
                        }
                        if(oOdt.getDisease()!=null && !oOdt.getDisease().isEmpty()){
                            mRow+=oOdt.getDisease().toString()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getPrice()!=null){
                            mRow+=oOdt.getPrice()+"#";
                        }else{
                            mRow+="0"+"#";
                        }
                        if(oOdt.getNotes()!=null && !oOdt.getNotes().isEmpty()){
                            mRow+=oOdt.getNotes().toString()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        mRow+="|";
                        mTechDetail+=mRow;
                    }
                }
            }

        }catch (Exception ex){}
        return  mTechDetail;
    }

    private String getPlanSaleCompetitorData(final List<SM_PlanSaleCompetitor> competitors){
        String mTechDetail="";
        try{
            if(competitors!=null){
                for (SM_PlanSaleCompetitor oOdt : competitors) {
                    String mRow="";
                    if(oOdt.getCompetitorId()!=null && !oOdt.getCompetitorId().isEmpty()){
                        mRow=oOdt.getCompetitorId()+"#";
                        if(oOdt.getPlanSaleId()!=null && !oOdt.getPlanSaleId().isEmpty()){
                            mRow+=oOdt.getPlanSaleId()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getTitle()!=null && !oOdt.getTitle().isEmpty()){
                            mRow+=oOdt.getTitle()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getNotes()!=null && !oOdt.getNotes().isEmpty()){
                            mRow+=oOdt.getNotes()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getUseful()!=null && !oOdt.getUseful().isEmpty()){
                            mRow+=oOdt.getUseful().toString()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getHarmful()!=null && !oOdt.getHarmful().isEmpty()){
                            mRow+=oOdt.getHarmful().toString()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        mRow+="|";
                        mTechDetail+=mRow;
                    }
                }
            }

        }catch (Exception ex){}
        return  mTechDetail;
    }

    private String getPlanSaleActivityData(List<SM_PlanSaleActivity> thisWeek, List<SM_PlanSaleActivity> nextWeek){
        String mTechDetail="";
        try{
            if(nextWeek != null){
                for(SM_PlanSaleActivity o: nextWeek){
                    thisWeek.add(o);
                }
            }

            if(thisWeek!=null){
                for (SM_PlanSaleActivity oOdt : thisWeek) {
                    String mRow="";
                    if(oOdt.getActivitieId()!=null && !oOdt.getActivitieId().isEmpty()){
                        mRow=oOdt.getActivitieId()+"#";
                        if(oOdt.getPlanSaleId()!=null && !oOdt.getPlanSaleId().isEmpty()){
                            mRow+=oOdt.getPlanSaleId()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getIsType()!=null){
                            mRow+=oOdt.getIsType()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getTitle()!=null && !oOdt.getTitle().isEmpty()){
                            mRow+=oOdt.getTitle()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getNotes()!=null && !oOdt.getNotes().isEmpty()){
                            mRow+=oOdt.getNotes().toString()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getAchievement()!=null && !oOdt.getAchievement().isEmpty()){
                            mRow+=oOdt.getAchievement().toString()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        mRow+="|";
                        mTechDetail+=mRow;
                    }
                }
            }

        }catch (Exception ex){}
        return  mTechDetail;
    }

    private void onPostPlanSale(final SM_PlanSale tech ,final List<SM_PlanSaleMarket> markets, final List<SM_PlanSaleDisease> diseases,
                                  final List<SM_PlanSaleCompetitor> competitor, final List<SM_PlanSaleActivity> thisWeeks, final List<SM_PlanSaleActivity> nextWeeks){
        try{
            if (APINet.isNetworkAvailable(PlanSaleActivity.this)==false){
                Toast.makeText(PlanSaleActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
                return;
            }

            final String Imei=AppUtils.getImeil(this);
            final String ImeiSim=AppUtils.getImeilsim(this);
            final String mDataPlanSaleMarket = getPlanSaleMarketData(markets);
            final String mDataPlanSaleDisease = getPlanSaleDiseaseData(diseases);
            final String mDataPlanSaleCompetitor = getPlanSaleCompetitorData(competitor);
            final String mDataPlanSaleActivitie = getPlanSaleActivityData(thisWeeks, nextWeeks);

            if(ImeiSim.isEmpty()){
                Toast.makeText(this,"Không đọc được số IMEI từ thiết bị cho việc đồng bộ. Kiểm tra Sim của bạn",Toast.LENGTH_LONG).show();
                return;
            }
            if(tech==null){
                Toast.makeText(this,"Không tìm thấy dữ liệu kế hoạch bán hàng.",Toast.LENGTH_LONG).show();
                return;
            }
            if(tech.getPlanSaleId()==null || tech.getReportCode().isEmpty()){
                Toast.makeText(this,"Không tìm thấy mã báo cáo",Toast.LENGTH_SHORT).show();
                return;
            }

            final String mUrlPostPlanSale=AppSetting.getInstance().URL_PostPlanSale();
            try {
                if (tech.getReportCode() == null || tech.getReportCode().isEmpty()) {
                    tech.setReportCode("");
                }
                if (tech.getReportName() == null || tech.getReportName().isEmpty()) {
                    tech.setReportName("");
                }
                if (tech.getReportDate() == null || tech.getReportDate().isEmpty()) {
                    tech.setReportDate("");
                }

                if (tech.getLatitude() == null || tech.getLatitude().toString().isEmpty()) {
                    tech.setLatitude(0.0f);
                }
                if (tech.getLongtitude() == null || tech.getLongtitude().toString().isEmpty()) {
                    tech.setLongtitude(0.0f);
                }
                if (tech.getLocationAddress() == null || tech.getLocationAddress().toString().isEmpty()) {
                    tech.setLocationAddress("N/A");
                }
                if(tech.getReceiverList() == null || tech.getReceiverList().toString().isEmpty()){
                    tech.setReceiverList("");
                }
                if(tech.getNotes() == null || tech.getNotes().toString().isEmpty()){
                    tech.setNotes("");
                }
                if(tech.getIsStatus() == null){
                    tech.setIsStatus("1");
                }
            }catch(Exception ex){
                Toast.makeText(PlanSaleActivity.this, "Không tìm thấy dữ liệu đã quét.." + ex.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            RequestBody DataBody = new FormBody.Builder()
                    .add("imei", Imei)
                    .add("imeisim", ImeiSim)
                    .add("reportcode",tech.getReportCode())
                    .add("reportday", tech.getReportDate())
                    .add("reportname", tech.getReportName())
                    .add("customerid", "")
                    .add("longitude", Float.toString(tech.getLongtitude()))
                    .add("latitude", Float.toString(tech.getLatitude()))
                    .add("locationaddress", tech.getLocationAddress())
                    .add("receiverlist", tech.getReceiverList())
                    .add("notes",tech.getNotes())
                    .add("isstatus", tech.getIsStatus())
                    .add("PlanSalemarket", mDataPlanSaleMarket)
                    .add("PlanSaledisease",mDataPlanSaleDisease)
                    .add("PlanSalecompetitor",mDataPlanSaleCompetitor)
                    .add("PlanSaleactivitie",mDataPlanSaleActivitie)
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
                                tech.setPostDate(sdf.format(new Date()));
                                tech.setPost(true);
                                tech.setIsStatus("2");
                                mDB.editPlanSale(tech);
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
            },mUrlPostPlanSale,"POST_REPORT_TECH",DataBody).execute();


        }catch (Exception ex){
            Toast.makeText(PlanSaleActivity.this,"Không thể đồng bộ:"+ex.getMessage(),Toast.LENGTH_LONG).show();
            dismissProgressDialog();
        }
    }*/

}

