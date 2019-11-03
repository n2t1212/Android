package com.mimi.mimigroup.ui.history;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.api.APINet;
import com.mimi.mimigroup.api.APINetCallBack;
import com.mimi.mimigroup.api.SyncPost;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.base.BaseFragment;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.FlagPost;
import com.mimi.mimigroup.model.QR_QRSCAN;
import com.mimi.mimigroup.ui.adapter.HistoryAdapter;
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

//CALL FROM MAIN ACTIVITI
public class HistoryFragment extends BaseFragment  implements PopupMenu.OnMenuItemClickListener{

    @BindView(R.id.rvHistory)
    RecyclerView rvHistory;

    HistoryAdapter adapter;
    private DBGimsHelper mDB;
    private String mfDay;
    private String mtDay;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new HistoryAdapter();
        rvHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDB=DBGimsHelper.getInstance(getContext());
        rvHistory.setAdapter(adapter);

       /* new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onLoadDataSource("","");
                    };
                },300);
       */
        onLoadDataSource("","");
        //onLoadDataSource("","");
    }
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_history;
    }

    private void onLoadDataSource(String fDay,String tDay){
        try{
            ((BaseActivity)getActivity()).showProgressDialog("Đang nạp lịch sử quét mã QR.");
           if(tDay=="") {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fNow = sdf.format(new Date());
                fDay = AppUtils.DateAdd(fNow, -7, "yyyy-MM-dd");
                tDay = AppUtils.DateAdd(fNow, 1, "yyyy-MM-dd");
            }

            mfDay=fDay;
            mtDay=tDay;
            ((BaseActivity)getActivity()).showProgressDialog("Đang nạp dữ liệu mã QR đã quét");
            List<QR_QRSCAN> lstQR= mDB.getAllQRScan(fDay,tDay);
            adapter.setHistoryList(lstQR);

            ((BaseActivity) getActivity()).dismissProgressDialog();
        }catch (Exception ex){
           ((BaseActivity) getActivity()).dismissProgressDialog();
            //Toast.makeText(getContext(),"SHOW_CUS_ERR"+ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


    public void onSyncUploadClicked(){
        if (APINet.isNetworkAvailable(getContext())==false){
            Toast.makeText(getContext(),"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
            return;
        }

        final String Imei=AppUtils.getImeil(getContext());
        final String ImeiSim=AppUtils.getImeilsim(getContext());
        if(ImeiSim.isEmpty() || Imei.isEmpty()){
            Toast.makeText(getContext(),"Không đọc được số IMEI từ thiết bị cho việc đồng bộ. Kiểm tra Sim của bạn",Toast.LENGTH_LONG).show();
            return;
        }else if(ImeiSim=="N/A"){
            Toast.makeText(getContext(),"Không đọc được số Serial SIM, Kiểm tra lại SIM của bạn.",Toast.LENGTH_LONG).show();
        }

        final List<QR_QRSCAN> lstQr=adapter.SelectedList;
        if(lstQr.size()>0){
            final String mUrlPostQR=AppSetting.getInstance().URL_PostQR();

            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getContext());
            alertDialog2.setTitle("Xác nhận");
            alertDialog2.setMessage("Bạn có chắc muốn tải lên?");
            alertDialog2.setPositiveButton("Đồng ý",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //for (QR_QRSCAN oQr : lstQr) {
                             //    if(oQr.getSync().equals(false) && !oQr.getQrid().isEmpty() && !oQr.getCustomerid().isEmpty()){
                                    onPostQRScan(lstQr,Imei,ImeiSim,mUrlPostQR);
                             //    }
                            //}
                            //onLoadDataSource(mfDay,mtDay);
                            //Toast.makeText(getContext(),"Đã tải mẫu tin thành công",Toast.LENGTH_SHORT).show();
                        }
                    });
            alertDialog2.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            alertDialog2.show();
        }else{
            Toast.makeText(getContext(),"Bạn chưa chọn mẫu tin để tải lên.",Toast.LENGTH_SHORT).show();
        }
    }


    public void onViewScanHistoryClicked(){
        List<QR_QRSCAN> lstQr=adapter.SelectedList;
        if(lstQr.size()>0) {
            for(QR_QRSCAN oQR:lstQr) {
                Intent intent = new Intent(getContext(), HistoryServerActivity.class);
                intent.putExtra("qrid",oQR.getQrid());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            }
        }else{
            Toast.makeText(getContext(),"Bạn chưa chọn mẫu tin để xem",Toast.LENGTH_SHORT).show();
        }

    }

    public void onSearchHistoryClicked(){
        try{
            LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
            View customView = inflater.inflate(R.layout.dialog_picker, null);
            final DatePicker dpStartDate = (DatePicker) customView.findViewById(R.id.dpStartDate);
            final DatePicker dpEndDate = (DatePicker) customView.findViewById(R.id.dpEndDate);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(customView); // Set the view of the dialog to your custom layout
            builder.setTitle("Chọn khoản thời gian");
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
                       Toast.makeText(getContext(),"Chọn khoản thời gian không hợp lệ.",Toast.LENGTH_SHORT).show();
                       return;
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    mfDay=sdf.format(dFday);
                    mtDay=sdf.format(dTday);
                    mtDay=AppUtils.DateAdd(mtDay,1,"yyyy-MM-dd");

                    if(mfDay!="" && mtDay!="") {
                        onLoadDataSource(mfDay,mtDay);
                    }
                    //Log.d("Year",Integer.toString(startYear));
                    //Log.d("Fday",mfDay);

                    dialog.dismiss();
                }});
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }});
            // Create and show the dialog
            builder.create().show();

        }catch (Exception ex){}
    }

    @OnClick(R.id.btnHisMenuList)
    public void onMenuClick(View v){
        String POPUP_CONSTANT = "mPopup";
        String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";

        PopupMenu popup = new PopupMenu(getContext(), v);
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
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.mnu_history_scan);

        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
       // HistoryFragment oFrame=new HistoryFragment();
        switch (item.getItemId()) {
            case R.id.mnuScanHis:
                 onViewScanHistoryClicked();

                return true;
            case R.id.mnuSynUpload:
                onSyncUploadClicked();
                return  true;

            case R.id.mnuSearchHis:
                onSearchHistoryClicked();
                return true;

            case R.id.mnuDelHis:
                final List<QR_QRSCAN> lstQR=adapter.SelectedList;
                if(lstQR.size()<=0){
                    Toast.makeText(getContext(),"Bạn chưa chọn mẫu tin để xóa.",Toast.LENGTH_LONG).show();
                    return false;
                }
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getContext());
                alertDialog2.setTitle("Xác nhận");
                alertDialog2.setMessage("Bạn có chắc muốn xóa ?");
                alertDialog2.setPositiveButton("Đồng ý",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                for (QR_QRSCAN oQr : lstQR) {
                                    //if (oQr.getQrscanid() != null && oQr.getQrscanid() != "") {
                                    mDB.delQRScan(oQr);
                                    //}
                                }
                                onLoadDataSource(mfDay,mtDay);
                                Toast.makeText(getContext(),"Đã xóa mẫu tin thành công",Toast.LENGTH_SHORT).show();
                            }
                        });
                alertDialog2.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      return;
                    }
                });
                alertDialog2.show();


            default:
                return false;
        }
    }



    private void showDialogValid(String title, String message) {

        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getContext());
        alertDialog2.setTitle(title);
        alertDialog2.setMessage(message);
        alertDialog2.setPositiveButton("Đồng ý",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog2.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog2.show();
    }


    //TÌM KIẾM THEO NGÀY
    private int startYear, startMonth, startDay, endYear, endMonth, endDay;

    public void showDatePicker() {
        // Inflate your custom layout containing 2 DatePickers
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        View customView = inflater.inflate(R.layout.dialog_picker, null);

        // Define your date pickers
        final DatePicker dpStartDate = (DatePicker) customView.findViewById(R.id.dpStartDate);
        final DatePicker dpEndDate = (DatePicker) customView.findViewById(R.id.dpEndDate);

        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(customView); // Set the view of the dialog to your custom layout
        builder.setTitle("Chọn khoản thời gian");
        builder.setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startYear = dpStartDate.getYear();
                startMonth = dpStartDate.getMonth();
                startDay = dpStartDate.getDayOfMonth();
                endYear = dpEndDate.getYear();
                endMonth = dpEndDate.getMonth();
                endDay = dpEndDate.getDayOfMonth();

                dialog.dismiss();
            }});
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
            }});
        // Create and show the dialog
        builder.create().show();
    }


    //POST QR_SCAN
    private void onPostQRScan(final List<QR_QRSCAN> lstQRS,String mImei,String mImeiSim, final String mUrlPost){
        Integer iSyncCount=0;
        try{
           final FlagPost flagPost = new FlagPost(0, 0);
             for (final QR_QRSCAN oQr : lstQRS) {
                //if (oQr.getSync().equals(false) && !oQr.getQrid().isEmpty() && !oQr.getCustomerid().isEmpty()) {
                 if (!oQr.getQrid().isEmpty() && !oQr.getCustomerid().isEmpty()) {
                  try {
                      if (oQr.getProductCode() == null || oQr.getProductCode().isEmpty()) {
                          oQr.setProductCode("");
                      }
                       if (oQr.getProductName() == null || oQr.getProductName().isEmpty()) {
                         oQr.setProductName("");
                       }
                       if (oQr.getUnit() == null || oQr.getUnit().isEmpty()) {
                          oQr.setUnit("");
                       }
                        if (oQr.getSpecification() == null || oQr.getSpecification().isEmpty()) {
                            oQr.setSpecification("");
                        }
                        if (oQr.getCommandNo() == null || oQr.getCommandNo().isEmpty()) {
                            oQr.setCommandNo("");
                        }
                        if (oQr.getScanDay() == null) {
                            oQr.setScanDay("");
                        }
                        if (oQr.getScanNo() == null || oQr.getScanNo().toString().isEmpty()) {
                            oQr.setScanNo(0);
                        }
                        if (oQr.getLatitude() == null || oQr.getLatitude().toString().isEmpty()) {
                            oQr.setLongitude(0.0);
                        }
                        if (oQr.getLongitude() == null || oQr.getLongitude().toString().isEmpty()) {
                            oQr.setLongitude(0.0);
                        }
                        if (oQr.getLocationAddress() == null || oQr.getLocationAddress().toString().isEmpty()) {
                            oQr.setLocationAddress("N/A");
                        }

                        if (oQr.getScanSupportID() == null || oQr.getScanSupportID().isEmpty()) {
                            oQr.setScanSupportID("");
                        }
                        if(oQr.getScanType()==null || oQr.getScanType().isEmpty() ){
                           oQr.setScanType("NA");
                        }
                    }catch(Exception ex){
                        Toast.makeText(getContext(), "Không tìm thấy dữ liệu đã quét.." + ex.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    RequestBody DataBody = new FormBody.Builder()
                            .add("imei", mImei)
                            .add("imeisim", mImeiSim)
                            .add("employeeid", "")
                            .add("qrid", oQr.getQrid())
                            .add("customerid", oQr.getCustomerid())
                            .add("productcode", oQr.getProductCode())
                            .add("productname", oQr.getProductName())
                            .add("unit", oQr.getUnit())
                            .add("specification", oQr.getSpecification())
                            .add("commandno", oQr.getCommandNo())
                            .add("scanno", Integer.toString(oQr.getScanNo()))
                            .add("scanday", oQr.getScanDay())
                            .add("quantity", "1")
                            .add("longitude", Double.toString(oQr.getLongitude()))
                            .add("latitude", Double.toString(oQr.getLatitude()))
                            .add("scope", "100")
                            .add("locationaddress", oQr.getLocationAddress())
                            .add("scansupportid", oQr.getScanSupportID())
                            .add("notes", "")
                            .add("scantype",oQr.getScanType())

                            .build();

                     iSyncCount+=1;
                    new SyncPost(new APINetCallBack() {
                        @Override
                        public void onHttpStart() {
                            // Log.d("SYNC_QR_POST",mUrlPost);
                            Toast.makeText(getContext(), "Đang đóng gói dữ liệu...", Toast.LENGTH_SHORT).show();
                            flagPost.setSendPost(flagPost.getSendPost()+1);
                        }

                        @Override
                        public void onHttpSuccess(String ResPonseRs) {
                            try {
                                flagPost.setReceivePost(flagPost.getReceivePost()+1);

                                if (!ResPonseRs.isEmpty()) {
                                    if (ResPonseRs.contains("SYNC_OK")) {
                                        Toast.makeText(getContext(), String.format("Đồng bộ '%s'  thành công.", oQr.getProductCode()), Toast.LENGTH_SHORT).show();
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        oQr.setSyncDay(sdf.format(new Date()));
                                        oQr.setSync(true);
                                        mDB.editQRScanStatus(oQr);

                                    } else if (ResPonseRs.contains("SYNC_NOT_REG") || ResPonseRs.contains("SYNC_NOT_REG")) {
                                        Toast.makeText(getContext(), "Thiết bị của bạn chưa đăng ký hoặc chưa xác thực từ server", Toast.LENGTH_LONG).show();
                                    } else if (ResPonseRs.contains("SYNC_ACTIVE")) {
                                        Toast.makeText(getContext(), "Thiết bị của bạn đã đăng ký nhưng chưa được kích hoạt", Toast.LENGTH_LONG).show();
                                    } else if (ResPonseRs.contains("SYNC_BODY_NULL")) {
                                        Toast.makeText(getContext(), "Tham số gửi lên BODY=NULL", Toast.LENGTH_LONG).show();
                                    } else if (ResPonseRs.contains("SYNC_QRID_NULL")) {
                                        Toast.makeText(getContext(), "Mã số QRScaniD=NULL", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (Exception ex) {
                                return;
                            }
                            if(flagPost.getSendPost()==flagPost.getReceivePost()){
                                onLoadDataSource(mfDay,mtDay);
                            }
                            // finish();
                        }

                        @Override
                        public void onHttpFailer(Exception e) {
                            Toast.makeText(getContext(), "Không thể đồng bộ:" + e.getMessage(), Toast.LENGTH_LONG).show();

                            flagPost.setReceivePost(flagPost.getReceivePost()+1);
                            if(flagPost.getSendPost()==flagPost.getReceivePost()){
                                onLoadDataSource(mfDay,mtDay);
                            }
                        }
                    }, mUrlPost, "POST_QR", DataBody).execute();

                }
            }
        }catch (Exception ex){
            Toast.makeText(getContext(),"Không thể đồng bộ:"+ex.getMessage(),Toast.LENGTH_LONG).show();
        }

        if(iSyncCount==0){
            Toast.makeText(getContext(),"Không có mẫu tin chưa đồng bộ",Toast.LENGTH_LONG).show();
        }
        adapter.SelectedList.clear();
    }




}
