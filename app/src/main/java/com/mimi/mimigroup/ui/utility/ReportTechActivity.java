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
import com.mimi.mimigroup.model.SM_ReportTech;
import com.mimi.mimigroup.model.SM_ReportTechActivity;
import com.mimi.mimigroup.model.SM_ReportTechCompetitor;
import com.mimi.mimigroup.model.SM_ReportTechDisease;
import com.mimi.mimigroup.model.SM_ReportTechMarket;
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
import okhttp3.FormBody;
import okhttp3.RequestBody;

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
                onLoadDataSource("","");
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

    public void onEditReportTechClicked(){
        try {
            List<SM_ReportTech> oReportTechSel = adapter.SelectedList;

            if(oReportTechSel == null || oReportTechSel.size() <=0) {
                Toast.makeText(ReportTechActivity.this, "Bạn chưa chọn báo cáo", Toast.LENGTH_SHORT).show();
                return;
            }

            if(oReportTechSel.size() > 1){
                Toast.makeText(ReportTechActivity.this, "Bạn chọn quá nhiều báo cáo để sửa. Vui lòng chọn lại...", Toast.LENGTH_SHORT).show();
                adapter.clearSelected();
                return;
            }

            if (oReportTechSel.get(0).getReportTechId() != "") {
                String mParSymbol=mDB.getParam("PAR_SYMBOL");
                if (mParSymbol==null || mParSymbol.isEmpty()){mParSymbol="MT";}
                Intent intent = new Intent(ReportTechActivity.this,ReportTechFormActivity.class);
                intent.setAction("EDIT");
                intent.putExtra("ReportTechID", oReportTechSel.get(0).getReportTechId());
                intent.putExtra("PAR_SYMBOL", mParSymbol);
                startActivityForResult(intent,REQUEST_CODE_EDIT);
                isEditAdd=true;
            } else {
                Toast.makeText(ReportTechActivity.this, "Bạn chưa chọn báo cáo.", Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Toast.makeText(ReportTechActivity.this, "ERR."+ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean  onDelReportTechClicked(){
        final List<SM_ReportTech> lstReportTech = adapter.SelectedList;
        if(lstReportTech.size()<=0){
            Toast.makeText(ReportTechActivity.this,"Bạn chưa chọn mẫu tin để xóa.",Toast.LENGTH_LONG).show();
            return false;
        }
        final Dialog oDlg=new Dialog(ReportTechActivity.this);
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
                for (SM_ReportTech oReportTech : lstReportTech) {
                    mDB.delReportTech(oReportTech.getReportTechId());
                }
                onLoadDataSource(mfDay,mtDay);
                Toast.makeText(ReportTechActivity.this,"Đã xóa mẫu tin thành công",Toast.LENGTH_SHORT).show();
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
    public void onSearchReportTechClicked(){
        try{
            LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
            View customView = inflater.inflate(R.layout.dialog_picker, null);
            final DatePicker dpStartDate = (DatePicker) customView.findViewById(R.id.dpStartDate);
            final DatePicker dpEndDate = (DatePicker) customView.findViewById(R.id.dpEndDate);

            AlertDialog.Builder builder = new AlertDialog.Builder(ReportTechActivity.this);
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
                        Toast.makeText(ReportTechActivity.this,"Chọn khoảng thời gian không hợp lệ.",Toast.LENGTH_SHORT).show();
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

    public void onPostReportTech(){
        List<SM_ReportTech> oSel = adapter.SelectedList;

        if (oSel == null || oSel.size() <= 0) {
            Toast.makeText(ReportTechActivity.this, "Bạn chưa chọn phiếu", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oSel.size() > 1) {
            Toast.makeText(ReportTechActivity.this, "Bạn chọn quá nhiều phiếu để sửa. Vui lòng chọn lại...", Toast.LENGTH_SHORT).show();
            adapter.clearSelected();
            return;
        }

        SM_ReportTech tech = oSel.get(0);

        if (APINet.isNetworkAvailable(ReportTechActivity.this)==false){
            Toast.makeText(ReportTechActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
            return;
        }
        if(tech==null || tech.getReportTechId().isEmpty()){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập báo cáo kỹ thuật..", Toast.LENGTH_SHORT).show();
            return;
        }

        List<SM_ReportTechMarket> oReportTechMarket = mDB.getAllReportTechMarket(tech.getReportTechId());
        List<SM_ReportTechDisease> oReportTechDisease = mDB.getAllReportTechDisease(tech.getReportTechId());
        List<SM_ReportTechCompetitor> oReportTechCompetitor = mDB.getAllReportTechCompetitor(tech.getReportTechId());
        List<SM_ReportTechActivity> oReportTechActivityThisWeek = mDB.getAllReportTechActivity(tech.getReportTechId(), 0);
        List<SM_ReportTechActivity> oReportTechActivityNextWeek = mDB.getAllReportTechActivity(tech.getReportTechId(), 1);

        onPostReportTech(tech, oReportTechMarket, oReportTechDisease, oReportTechCompetitor, oReportTechActivityThisWeek, oReportTechActivityNextWeek);

    }

    //POST 
    private String getReportTechMarketData(final List<SM_ReportTechMarket> markets){
        String mTechDetail="";
        try{
            if(markets!=null){
                for (SM_ReportTechMarket oOdt : markets) {
                    String mRow="";
                    if(oOdt.getMarketId()!=null && !oOdt.getMarketId().isEmpty()){
                        mRow=oOdt.getMarketId()+"#";
                        if(oOdt.getReportTechId()!=null && !oOdt.getReportTechId().isEmpty()){
                            mRow+=oOdt.getReportTechId()+"#";
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

    private String getReportTechDiseaseData(final List<SM_ReportTechDisease> diseases){
        String mTechDetail="";
        try{
            if(diseases!=null){
                for (SM_ReportTechDisease oOdt : diseases) {
                    String mRow="";
                    if(oOdt.getDiseaseId()!=null && !oOdt.getDiseaseId().isEmpty()){
                        mRow=oOdt.getDiseaseId()+"#";
                        if(oOdt.getReportTechId()!=null && !oOdt.getReportTechId().isEmpty()){
                            mRow+=oOdt.getReportTechId()+"#";
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

    private String getReportTechCompetitorData(final List<SM_ReportTechCompetitor> competitors){
        String mTechDetail="";
        try{
            if(competitors!=null){
                for (SM_ReportTechCompetitor oOdt : competitors) {
                    String mRow="";
                    if(oOdt.getCompetitorId()!=null && !oOdt.getCompetitorId().isEmpty()){
                        mRow=oOdt.getCompetitorId()+"#";
                        if(oOdt.getReportTechId()!=null && !oOdt.getReportTechId().isEmpty()){
                            mRow+=oOdt.getReportTechId()+"#";
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

    private String getReportTechActivityData(List<SM_ReportTechActivity> thisWeek, List<SM_ReportTechActivity> nextWeek){
        String mTechDetail="";
        try{
            if(nextWeek != null){
                for(SM_ReportTechActivity o: nextWeek){
                    thisWeek.add(o);
                }
            }

            if(thisWeek!=null){
                for (SM_ReportTechActivity oOdt : thisWeek) {
                    String mRow="";
                    if(oOdt.getActivitieId()!=null && !oOdt.getActivitieId().isEmpty()){
                        mRow=oOdt.getActivitieId()+"#";
                        if(oOdt.getReportTechId()!=null && !oOdt.getReportTechId().isEmpty()){
                            mRow+=oOdt.getReportTechId()+"#";
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

    private void onPostReportTech(final SM_ReportTech tech , final List<SM_ReportTechMarket> markets, final List<SM_ReportTechDisease> diseases,
                                  final List<SM_ReportTechCompetitor> competitor, final List<SM_ReportTechActivity> thisWeeks, final List<SM_ReportTechActivity> nextWeeks){
        try{
            if (APINet.isNetworkAvailable(ReportTechActivity.this)==false){
                Toast.makeText(ReportTechActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
                return;
            }

            final String Imei=AppUtils.getImeil(this);
            final String ImeiSim=AppUtils.getImeilsim(this);
            final String mDataReportTechMarket = getReportTechMarketData(markets);
            final String mDataReportTechDisease = getReportTechDiseaseData(diseases);
            final String mDataReportTechCompetitor = getReportTechCompetitorData(competitor);
            final String mDataReportTechActivitie = getReportTechActivityData(thisWeeks, nextWeeks);

            if(ImeiSim.isEmpty()){
                Toast.makeText(this,"Không đọc được số IMEI từ thiết bị cho việc đồng bộ. Kiểm tra Sim của bạn",Toast.LENGTH_LONG).show();
                return;
            }
            if(tech==null){
                Toast.makeText(this,"Không tìm thấy dữ liệu báo cáo kỹ thuật.",Toast.LENGTH_LONG).show();
                return;
            }
            if(tech.getReportTechId()==null || tech.getReportCode().isEmpty()){
                Toast.makeText(this,"Không tìm thấy mã báo cáo",Toast.LENGTH_SHORT).show();
                return;
            }

            final String mUrlPostReportTech=AppSetting.getInstance().URL_PostReportTech();
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
                Toast.makeText(ReportTechActivity.this, "Không tìm thấy dữ liệu đã quét.." + ex.getMessage(), Toast.LENGTH_LONG).show();
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
                    .add("reporttechmarket", mDataReportTechMarket)
                    .add("reporttechdisease",mDataReportTechDisease)
                    .add("reporttechcompetitor",mDataReportTechCompetitor)
                    .add("reporttechactivitie",mDataReportTechActivitie)
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
                                Toast.makeText(ReportTechActivity.this, "Đồng  bộ thành công.", Toast.LENGTH_LONG).show();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                tech.setPostDate(sdf.format(new Date()));
                                tech.setPost(true);
                                tech.setIsStatus("2");
                                mDB.editReportTech(tech);
                                finish();
                            }
                            else if(ResPonseRs.contains("SYNC_REG") || ResPonseRs.contains("SYNC_NOT_REG")){
                                Toast.makeText(ReportTechActivity.this, "Thiết bị chưa được đăng ký hoặc chưa xác thực từ Server.", Toast.LENGTH_LONG).show();
                            }else if(ResPonseRs.contains("SYNC_ACTIVE")) {
                                Toast.makeText(ReportTechActivity.this, "Thiết bị chưa kích hoạt...", Toast.LENGTH_LONG).show();
                            }else if(ResPonseRs.contains("SYNC_APPROVE") || ResPonseRs.contains("SYNC_APPROVE")){
                                Toast.makeText(ReportTechActivity.this, "Đơn hàng đang được xử lý. Bạn không thể gửi điều chỉnh.", Toast.LENGTH_LONG).show();
                            }else if (ResPonseRs.contains("SYNC_BODY_NULL")) {
                                Toast.makeText(ReportTechActivity.this, "Tham số gửi lên BODY=NULL", Toast.LENGTH_LONG).show();
                            } else if (ResPonseRs.contains("SYNC_ORDERID_NULL")) {
                                Toast.makeText(ReportTechActivity.this, "Mã số REPORT_TECH_ID=NULL", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(ReportTechActivity.this  , "Không nhận được trang thải trả về.", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception ex){ }
                    // finish();
                }

                @Override
                public void onHttpFailer(Exception e) {
                    dismissProgressDialog();
                    Toast.makeText(ReportTechActivity.this,"Không thể đồng bộ:"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            },mUrlPostReportTech,"POST_REPORT_TECH",DataBody).execute();


        }catch (Exception ex){
            Toast.makeText(ReportTechActivity.this,"Không thể đồng bộ:"+ex.getMessage(),Toast.LENGTH_LONG).show();
            dismissProgressDialog();
        }
    }

}
