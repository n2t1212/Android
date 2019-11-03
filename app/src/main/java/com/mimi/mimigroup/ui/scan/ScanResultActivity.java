package com.mimi.mimigroup.ui.scan;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import com.mimi.mimigroup.R;
import com.mimi.mimigroup.api.APIGPSTrack;
import com.mimi.mimigroup.api.APIGoogleTrack;
import com.mimi.mimigroup.api.APILocationCallBack;
import com.mimi.mimigroup.api.APINet;
import com.mimi.mimigroup.api.APINetCallBack;
import com.mimi.mimigroup.api.SyncCallBack;
import com.mimi.mimigroup.api.SyncFindCustomer;
import com.mimi.mimigroup.api.SyncFindCustomerCallback;
import com.mimi.mimigroup.api.SyncGet;
import com.mimi.mimigroup.api.SyncPost;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseActivity;

import com.mimi.mimigroup.model.DATA_JSON;
import com.mimi.mimigroup.model.DM_Customer_Distance;
import com.mimi.mimigroup.model.DM_Employee;
import com.mimi.mimigroup.model.DM_Product;
import com.mimi.mimigroup.model.QR_QRSCAN;
import com.mimi.mimigroup.model.QR_QRSCAN_HIS;

import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.utils.AppUtils;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import com.mimi.mimigroup.db.DBGimsHelper;


public class ScanResultActivity extends BaseActivity implements AdapterView.OnItemSelectedListener{
    @BindView(R.id.spCustomer)
    Spinner spCustomer;

    @BindView(R.id.spScanSupport)
    Spinner spScanSupport;
    @BindView(R.id.tvCommandNo)
    CustomBoldTextView tvCommandNo;
    @BindView(R.id.tvQrid)
    CustomBoldTextView tvQrid;
    @BindView(R.id.tvProductCode)
    CustomBoldTextView tvProductCode;
    @BindView(R.id.tvProductName)
    CustomBoldTextView tvProductName;
    @BindView(R.id.tvUnit)
    CustomBoldTextView tvUnit;
    @BindView(R.id.tvSpec)
    CustomBoldTextView tvSpec;
    @BindView(R.id.lstScanHis)
    ListView lstScanHis;
    @BindView(R.id.lineSupport)
    View lineSupport;
    @BindView(R.id.LayoutSupport)
    LinearLayout LayoutSupport;

    int scanNumber = 0;
    String ScanResuilt;
    DM_Customer_Distance oCustomer;
    DBGimsHelper mDB;
    private QR_QRSCAN mQRScan;

    Location mLastLocation;
    APIGPSTrack locationTrack;

    private boolean isEnterCode=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        mDB=DBGimsHelper.getInstance(this);


        locationTrack=new APIGPSTrack(ScanResultActivity.this);
        if(!locationTrack.canGetLocation()) {
            locationTrack.showSettingsAlert();
        }

        ScanResuilt = getIntent().getStringExtra("DATA_SCAN_RESULT");
        if (ScanResuilt != null && !ScanResuilt.isEmpty()) {
          try {
              if (ScanResuilt.equalsIgnoreCase("ENTER_CODE")) {
                  isEnterCode=true;
                  tvQrid.setTextColor(Color.parseColor("#1b950b"));
                  tvQrid.setTextSize(20);
                  showEnterCode("");
              }else{
                  isEnterCode=false;
                  String[] ADataScan = ScanResuilt.split("#");
                  //tvCode.setText(ScanResuilt.split("#")[0]);
                  if (ADataScan.length > 0) {
                      tvQrid.setText(ADataScan[0]);
                  }
                  if (ADataScan.length >= 1) {
                      tvCommandNo.setText(ADataScan[1]);
                  }
                  if (ADataScan.length >= 2) {
                      tvProductCode.setText(ADataScan[2]);
                      DM_Product oProd = mDB.getProduct(ADataScan[2]);
                      try {
                          if (oProd != null) {
                              tvProductName.setText(oProd.getProductName());
                              tvUnit.setText(oProd.getUnit());
                              tvSpec.setText(oProd.getSpecification());
                          } else {
                              if(APINet.isNetworkAvailable(this)) {
                                  onDownProduct();
                              }
                          }
                      } catch (Exception e) {
                      }
                  }
          }

            //GHI QR-SCAN
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            mQRScan = new QR_QRSCAN();
                            try {
                                String sQRScanID="";
                                try {
                                    SimpleDateFormat qrScanID = new SimpleDateFormat("ddMMyyyyHHmmssSS");
                                    sQRScanID = qrScanID.format(new Date());
                                    sQRScanID = tvQrid.getText().toString() + sQRScanID;
                                }catch (Exception ex){
                                    Toast.makeText(ScanResultActivity.this,"Không tạo được mã QRScanID",Toast.LENGTH_SHORT).show();
                                    sQRScanID=tvQrid.getText()+sdf.format(new Date()).toString();
                                }

                                mQRScan.setQrscanid( sQRScanID);
                                mQRScan.setQrid(tvQrid.getText().toString());
                                mQRScan.setCommandNo(tvCommandNo.getText().toString());
                                mQRScan.setProductCode(tvProductCode.getText().toString());
                                mQRScan.setProductName(tvProductName.getText().toString());
                                mQRScan.setUnit(tvUnit.getText().toString());
                                mQRScan.setSpecification(tvSpec.getText().toString());
                                mQRScan.setSync(Boolean.valueOf("false"));
                                mQRScan.setImei(AppUtils.getImeil(ScanResultActivity.this));
                                mQRScan.setImeiSim(AppUtils.getImeilsim(ScanResultActivity.this));
                                mQRScan.setScanNo(Integer.valueOf("1"));
                                mQRScan.setScanDay(sdf.format(new Date()));
                                mQRScan.setScanSupportID("");
                                if(isEnterCode) {
                                    mQRScan.setScanType("N");
                                }else{
                                    mQRScan.setScanType("Q");
                                }

                                //GET CUSTOMER THEO LOCATION
                                mLastLocation=locationTrack.getLocation();
                                if(mLastLocation==null){
                                    if(locationTrack.canGetLocation()) {
                                        //locationTrack.stopListener();
                                        mLastLocation = locationTrack.getLocation();
                                    }
                                }

                                if(mLastLocation!=null){
                                    mQRScan.setLongitude(mLastLocation.getLongitude());
                                    mQRScan.setLatitude(mLastLocation.getLatitude());

                                    //TÌM KHÁCH HÀNG THEO TỌA ĐỘ
                                    onAutoSearchCustomer(mQRScan.getLongitude(),mQRScan.getLatitude());

                                    mQRScan.setLocationAddress(getAddressLocation(mLastLocation.getLongitude(),mLastLocation.getLatitude()));
                                    Toast.makeText(ScanResultActivity.this,"Tọa độ đã xác định tại:" + mQRScan.getLocationAddress(),Toast.LENGTH_LONG).show();
                                    //Toast oToast=Toast.makeText(ScanResultActivity.this,"Tọa độ đã xác định tại:" + mQRScan.getLocationAddress(),Toast.LENGTH_LONG);
                                    //oToast.setGravity(Gravity.CENTER,0,0);
                                    //oToast.show();
                                }else{
                                    Toast.makeText(ScanResultActivity.this, "Không xác định được tọa độ. Đang xác định theo PA2.", Toast.LENGTH_LONG).show();

                                    new APIGoogleTrack(new APILocationCallBack() {
                                        @Override
                                        public void onCurrentLocation(Location MTLocation) {
                                            mLastLocation = MTLocation;
                                            if (mLastLocation != null) {
                                                mQRScan.setLongitude(mLastLocation.getLongitude());
                                                mQRScan.setLatitude(mLastLocation.getLatitude());

                                                //TÌM KHÁCH HÀNG THEO TỌA ĐỘ
                                                onAutoSearchCustomer(mQRScan.getLongitude(),mQRScan.getLatitude());

                                                mQRScan.setLocationAddress(getAddressLocation(mLastLocation.getLongitude(),mLastLocation.getLatitude()));
                                                Toast.makeText(ScanResultActivity.this,"PA2:" + mQRScan.getLocationAddress(),Toast.LENGTH_LONG).show();
                                            }else{
                                                Toast.makeText(ScanResultActivity.this,"Không xác định được tọa độ. Kiểm tra lại định vị của bạn",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    },ScanResultActivity.this);
                                 }

                                if (oCustomer!=null && !oCustomer.getCustomerid().isEmpty()) {
                                   mQRScan.setCustomerid(oCustomer.getCustomerid());
                                   //mDB.addQRScan(mQRScan);
                                }

                            }catch (Exception ex){
                                Toast.makeText(ScanResultActivity.this,"Không thể cập nhật dữ liệu quét."+ex.getMessage(),Toast.LENGTH_LONG).show();
                            }


                            if(!tvQrid.getText().toString().isEmpty()) {
                                onLoadScanSupport();
                                //TẢI DỮ LIỆU QRHIS
                                onDownQRScanHis(tvQrid.getText().toString().trim());
                            }
                        }
                    },300);
           }catch (Exception ex){}

        }
    }

    @Override
    protected void onStop() {

        try {
            if (locationTrack != null) {
                locationTrack.stopListener();
            }
        }catch (Exception ex){}

        super.onStop();
    }


    @OnClick(R.id.ivBack)
    public void onBack(){
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @OnClick(R.id.btnRegetLocation)
    public void onRegetLocation(){
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 50);
            }


            if(locationTrack==null) {
                locationTrack = new APIGPSTrack(ScanResultActivity.this);
            }
            locationTrack.stopListener();
            if (locationTrack.canGetLocation()) {
                mLastLocation= locationTrack.getLocation();
            }else{
                locationTrack.showSettingsAlert();
                return;
            }

            if (mLastLocation == null) {
                Toast oToast= Toast.makeText(ScanResultActivity.this, "Đang xác định lại tọa độ.", Toast.LENGTH_SHORT);
                oToast.setGravity(Gravity.CENTER,0,0);
                oToast.show();

                if(locationTrack==null){
                    locationTrack = new APIGPSTrack(ScanResultActivity.this);
                }
                locationTrack.stopListener();
                mLastLocation = locationTrack.getLocation();
            }

            if (mLastLocation != null) {
                mQRScan.setLongitude(mLastLocation.getLongitude());
                mQRScan.setLatitude(mLastLocation.getLatitude());
                  //TÌM KHÁCH HÀNG THEO TỌA ĐỘ
                onAutoSearchCustomer(mQRScan.getLongitude(), mQRScan.getLatitude());

                mQRScan.setLocationAddress(getAddressLocation(mLastLocation.getLongitude(),mLastLocation.getLatitude()));
                Toast.makeText(ScanResultActivity.this,"Tọa độ đã xác định tại:" + mQRScan.getLocationAddress(),Toast.LENGTH_LONG).show();
                //Toast oToast=Toast.makeText(ScanResultActivity.this,"Tọa độ đã xác định tại:" + mQRScan.getLocationAddress(),Toast.LENGTH_LONG);
                //oToast.setGravity(Gravity.CENTER,0,0);
                //oToast.show();

            } else {
                Toast.makeText(ScanResultActivity.this, "Không xác định được tọa độ. Đang xác định theo PA2.", Toast.LENGTH_LONG).show();

                new APIGoogleTrack(new APILocationCallBack() {
                    @Override
                    public void onCurrentLocation(Location MTLocation) {
                        mLastLocation = MTLocation;
                        if (mLastLocation != null) {
                            mQRScan.setLongitude(mLastLocation.getLongitude());
                            mQRScan.setLatitude(mLastLocation.getLatitude());

                            //TÌM KHÁCH HÀNG THEO TỌA ĐỘ
                            onAutoSearchCustomer(mQRScan.getLongitude(),mQRScan.getLatitude());

                            mQRScan.setLocationAddress(getAddressLocation(mLastLocation.getLongitude(),mLastLocation.getLatitude()));
                            Toast.makeText(ScanResultActivity.this,"PA2:" + mQRScan.getLocationAddress(),Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(ScanResultActivity.this,"Không xác định được tọa độ. Kiểm tra lại định vị của bạn",Toast.LENGTH_LONG).show();
                        }
                    }
                },ScanResultActivity.this);

                //Toast.makeText(ScanResultActivity.this, "Không xác định được tọa độ. Vui lòng đợi nếu bạn mới bật GPS.", Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Toast.makeText(ScanResultActivity.this, "Chưa xác định được tọa độ: "+ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void onLoadScanSupport(){
        try{
            List<DM_Employee> lstEMP=mDB.getAllEmployee();
            if(lstEMP!=null && lstEMP.size()>0){
               lineSupport.setVisibility(View.VISIBLE);
               LayoutSupport.setVisibility(View.VISIBLE);
                spScanSupport.setTag(lstEMP);
                spScanSupport.setOnItemSelectedListener(ScanResultActivity.this);

                List<String> ArrScanSupport = new ArrayList<>();
                ArrScanSupport.add("");
                for (DM_Employee oEmp : lstEMP) {
                    ArrScanSupport.add(oEmp.getEmployeeName());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ScanResultActivity.this, android.R.layout.simple_spinner_item, ArrScanSupport);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spScanSupport.setAdapter(dataAdapter);

            }else{
                lineSupport.setVisibility(View.GONE);
                LayoutSupport.setVisibility(View.GONE);
            }
        }catch (Exception ex){}
    }

    //ASync Tìm khách hàng theo tọa độ
    public void onAutoSearchCustomer(Double mLongitude,Double mLatitude){
        float mScope= Float.valueOf(AppSetting.getInstance().getParscope());//Integer.valueOf(130);//100Met;
        if(mScope<=0){
            try {
                mScope = Float.valueOf(mDB.getParam("PAR_SCOPE"));
            }catch (Exception ex){}
        }
        int mReturnSize=Integer.valueOf(5);
        //Toast.makeText(ScanResultActivity.this,"Đang xác định khách hàng bán kính "+Float.toString(mScope),Toast.LENGTH_SHORT).show();

        try {
          if (mLongitude > 0 && mLatitude > 0) {
              List<DM_Customer_Distance> lstCus;
              lstCus = mDB.getAllCustomerDistance();

              if (lstCus.size() > 0) {
                  new SyncFindCustomer(new SyncFindCustomerCallback() {
                      @Override
                      public void onSyncStart() {
                          //showProgressDialog("Đang tìm khách hàng theo tọa độ.");
                      }

                      @Override
                      public void onSyncSuccess(List<DM_Customer_Distance> lstSel) {
                          //dismissProgressDialog();
                          if (!lstSel.isEmpty()) {
                              spCustomer.setTag(lstSel);
                              spCustomer.setOnItemSelectedListener(ScanResultActivity.this);
                              List<String> customers = new ArrayList<>();
                              for (DM_Customer_Distance oCus : lstSel) {
                                  customers.add(oCus.getCustomerName());
                              }
                              ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ScanResultActivity.this, android.R.layout.simple_spinner_item, customers);
                              dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                              spCustomer.setAdapter(dataAdapter);
                          }

                          if(lstSel.isEmpty() || lstSel.size()<=0){
                              Toast.makeText(ScanResultActivity.this,"Không tìm thấy khách hàng trong phạm vi tọa độ này.",Toast.LENGTH_LONG).show();
                          }
                      }

                      @Override
                      public void onSyncFailer(Exception e) {
                         // dismissProgressDialog();
                          Toast.makeText(ScanResultActivity.this,"Không thể tìm khách hàng theo tọa độ."+e.getMessage(),Toast.LENGTH_LONG).show();
                      }
                  }, lstCus, mLongitude, mLatitude, mScope, mReturnSize).execute();
              }
              //lstCus=onFindCustomer(mLongitude,mLatitude,Float.valueOf(100));

          }
      }catch (Exception e){
          //dismissProgressDialog();
          Toast.makeText(this,"Không thể tìm khách hàng."+e.getMessage(),Toast.LENGTH_LONG).show();
       }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spCustomer:
                List<DM_Customer_Distance> lstCusDistance = (List<DM_Customer_Distance>) spCustomer.getTag();
                oCustomer = lstCusDistance.get(position);
                try {
                    mQRScan.setCustomerid(oCustomer.getCustomerid());
                }catch (Exception ex){}
                break;

            case R.id.spScanSupport:
                try{
                    if(position<=0){
                        mQRScan.setScanSupportID("");
                        //Log.d("EMP_ID_SUP","NOT_SUPPORT"+ mQRScan.getScanSupportID());
                    }else {
                        if (spScanSupport.getTag() != null) {
                            List<DM_Employee> lstcurEmp = (List<DM_Employee>) spScanSupport.getTag();
                            mQRScan.setScanSupportID(lstcurEmp.get(position-1).getEmployeeid());
                            //Log.d("EMP_ID_SUP", mQRScan.getScanSupportID());
                        }
                    }
                }catch (Exception ex){
                    Toast.makeText(ScanResultActivity.this,"Lỗi chọn hỗ trợ: "+ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}



    @OnClick(R.id.btnPostQR)
    public void onUpload(){
        if (APINet.isNetworkAvailable(ScanResultActivity.this)==false){
            Toast.makeText(ScanResultActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
            return;
        }

        if (mQRScan!= null && oCustomer!=null) {
             mQRScan.setCustomerid(oCustomer.getCustomerid());
             if (mQRScan.getCustomerid().isEmpty() || mQRScan.getQrid().isEmpty()) {
                 Toast.makeText(this, "Bạn chưa chọn đại lý,hoặc không tìm thấy mã QR", Toast.LENGTH_SHORT).show();
             }else{
                 mDB.addQRScan(mQRScan);
                 onPostQRScan(mQRScan);
             }
        }else{
           Toast.makeText(this, "Vui lòng chọn đại lý", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btnSaveQR)
    public void onSaveOnly(){
        if (mQRScan!= null && oCustomer!=null) {
            mQRScan.setCustomerid(oCustomer.getCustomerid());
            if (mQRScan.getCustomerid().isEmpty() || mQRScan.getQrid().isEmpty()) {
                Toast.makeText(this, "Bạn chưa chọn đại lý,hoặc không tìm thấy mã QR", Toast.LENGTH_SHORT).show();
            }else{
                mDB.addQRScan(mQRScan);
                Toast.makeText(this, "Ghi dữ liệu đã quét thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        }else{
            Toast.makeText(this, "Vui lòng chọn đại lý", Toast.LENGTH_SHORT).show();
        }
    }


   public void onDownQRScanHis(String QRID){
        try{
            if (APINet.isNetworkAvailable(ScanResultActivity.this)==false){
                Toast.makeText(ScanResultActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
                return;
            }

            String Imei=AppUtils.getImeil(getApplicationContext());
            String ImeiSim=AppUtils.getImeilsim(getApplicationContext());
            final String mUrlGetQRHIS=AppSetting.getInstance().URL_GetQR(Imei,ImeiSim,QRID,false,1);
            new SyncGet(new APINetCallBack() {
                @Override
                public void onHttpStart() {
                    //showProgressDialog("Đang tải lịch sử quét.");
                    Toast.makeText(ScanResultActivity.this,"Đang tải lịch sử quét.",Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onHttpSuccess(String ResPonseRs) {
                    try {
                        if (ResPonseRs!=null && !ResPonseRs.isEmpty()) {
                            if( ResPonseRs.equalsIgnoreCase("[]")){
                                Toast.makeText(ScanResultActivity.this, "Mã này chưa có lịch sử quét", Toast.LENGTH_SHORT).show();
                                return;
                            }else if(ResPonseRs.contains("SYNC_REG:Thiết bị của bạn chưa được đăng ký")){
                                Toast.makeText(ScanResultActivity.this, "Thiết bị của bạn chưa đăng ký. Vui lòng đăng ký trước...", Toast.LENGTH_SHORT).show();
                                return;
                            }else if(ResPonseRs.contains("SYNC_ACTIVE:Thiết bị của bạn đã đăng ký nhưng chưa kích hoạt")) {
                                Toast.makeText(ScanResultActivity.this, "Thiết bị của bạn đã đăng ký nhưng chưa kích hoạt từ server...", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Gson gson = new Gson();
                            Type type = new TypeToken<Collection<QR_QRSCAN_HIS>>() {}.getType();
                            Collection<QR_QRSCAN_HIS> enums = gson.fromJson(ResPonseRs, type);
                            QR_QRSCAN_HIS[] ArrQrHis = enums.toArray(new QR_QRSCAN_HIS[enums.size()]);
                            if(ArrQrHis!=null) {
                                //List<QR_QRSCAN_HIS> lstQr=new ArrayList<QR_QRSCAN_HIS>();
                                List<String> lstHis=new ArrayList<String>();

                                for (QR_QRSCAN_HIS oQ:ArrQrHis){
                                    //lstQr.add(oQ);
                                    lstHis.add(oQ.getScanDay()+"  - "+oQ.getEmployee()+"  -  "+oQ.getLocationAddress());
                                }
                                if(lstHis.size()>0){
                                  lstScanHis.setVisibility(View.VISIBLE);
                                  ArrayAdapter<String> adapter=new ArrayAdapter<String>(ScanResultActivity.this,R.layout.item_scanresult_history,R.id.tvQrScanHisItem,lstHis);
                                  lstScanHis.setAdapter(adapter);
                                  /*lstScanHis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      @Override
                                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                          int iTemPos=position;
                                          String mItemVal=(String) lstScanHis.getItemAtPosition(iTemPos);
                                          Toast.makeText(ScanResultActivity.this    ,mItemVal,Toast.LENGTH_SHORT).show();
                                      }
                                  });*/
                                }
                            }
                        }
                        //dismissProgressDialog();
                    }catch (Exception ex){
                       // dismissProgressDialog();
                    }
                }

                @Override
                public void onHttpFailer(Exception e) {
                  dismissProgressDialog();
                }
            },mUrlGetQRHIS,"QR_GET_HIS").execute();

        }catch (Exception ex){
            Log.d("GET_QR_SCAN_HS_ERR",ex.getMessage().toString());
           // dismissProgressDialog();
        }
    }



    //POST QR_SCAN
    private void onPostQRScan(final QR_QRSCAN oQr){
        try{
            if (APINet.isNetworkAvailable(ScanResultActivity.this)==false){
                Toast.makeText(ScanResultActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
                return;
            }

            String Imei=AppUtils.getImeil(this);
            String ImeiSim=AppUtils.getImeilsim(this);
            if(ImeiSim.isEmpty()){
                Toast.makeText(this,"Không đọc được số IMEI từ thiết bị cho việc đồng bộ. Kiểm tra Sim của bạn",Toast.LENGTH_LONG).show();
                return;
            }
            if(oQr==null){
                Toast.makeText(this,"Không tìm thấy dữ liệu đã quét..",Toast.LENGTH_LONG).show();
                return;
            }
            if(oQr.getQrid()==null || oQr.getQrid().isEmpty()){
                Toast.makeText(this,"Không tìm thấy mã QR đã quét",Toast.LENGTH_SHORT).show();
                return;
            }else if(oQr.getCustomerid()==null || oQr.getCustomerid().isEmpty()){
                Toast.makeText(this,"Bạn chưa chọn khách hàng cho lần quét này",Toast.LENGTH_SHORT).show();
                return;
            }


            final String mUrlPostQR=AppSetting.getInstance().URL_PostQR();
            try {
                if(oQr.getProductCode()==null  || oQr.getProductCode().isEmpty()){
                    oQr.setProductCode("");
                }
                if(oQr.getProductName()==null  || oQr.getProductName().isEmpty()){
                    oQr.setProductName("");
                }
                if(oQr.getUnit()==null || oQr.getUnit().isEmpty()){
                    oQr.setUnit("");
                }
                if(oQr.getSpecification()==null || oQr.getSpecification().isEmpty()){
                    oQr.setSpecification("");
                }
                if(oQr.getCommandNo()==null || oQr.getCommandNo().isEmpty()){
                    oQr.setCommandNo("");
                }

                if (oQr.getScanDay() == null) {
                    oQr.setScanDay("");
                }
                if (oQr.getScanNo()==null || oQr.getScanNo().toString().isEmpty()) {
                    oQr.setScanNo(0);
                }
                if (oQr.getLatitude()==null || oQr.getLatitude().toString().isEmpty()) {
                    oQr.setLongitude(0.0);
                }
                if (oQr.getLongitude()==null || oQr.getLongitude().toString().isEmpty()) {
                    oQr.setLongitude(0.0);
                }
                if (oQr.getLocationAddress()==null || oQr.getLocationAddress().toString().isEmpty()) {
                    oQr.setLocationAddress("N/A");
                }
                if (oQr.getScanSupportID() == null || oQr.getScanSupportID().isEmpty()) {
                    oQr.setScanSupportID("");
                }
                if(oQr.getScanType()==null || oQr.getScanType().isEmpty()){
                    oQr.setScanType("NA");
                }
            }catch (Exception ex){ Toast.makeText(this,"Không tìm thấy dữ liệu đã quét.."+ex.getMessage(),Toast.LENGTH_LONG).show();}

            RequestBody DataBody = new FormBody.Builder()
                    .add("imei",Imei)
                    .add("imeisim", ImeiSim)
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
                    .add("locationaddress",oQr.getLocationAddress())
                    .add("scansupportid", oQr.getScanSupportID())
                    .add("notes", "")
                    .add("scantype",oQr.getScanType())
                    .build();

                 new SyncPost(new APINetCallBack() {
                     @Override
                     public void onHttpStart() {
                         showProgressDialog("Đang đồng bộ dữ liệu về Server.");
                         //Log.d("SYNC_QR_POST",mUrlPostQR);
                     }

                     @Override
                     public void onHttpSuccess(String ResPonseRs) {
                         try {
                             //Log.d("POST_QR_RESPONSE",ResPonseRs);
                             dismissProgressDialog();
                             if (!ResPonseRs.isEmpty()) {
                                 if (ResPonseRs.contains("SYNC_OK")) {
                                     Toast.makeText(ScanResultActivity.this, "Đồng  bộ thành công.", Toast.LENGTH_LONG).show();
                                     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                     oQr.setSyncDay(sdf.format(new Date()));
                                     oQr.setSync(true);
                                     mDB.editQRScanStatus(oQr);
                                     finish();
                                 }
                                 else if(ResPonseRs.contains("SYNC_REG") || ResPonseRs.contains("SYNC_NOT_REG")){
                                     Toast.makeText(ScanResultActivity.this, "Thiết bị chưa được đăng ký hoặc chưa xác thực từ Server.", Toast.LENGTH_LONG).show();
                                 }else if(ResPonseRs.contains("SYNC_ACTIVE")){
                                     Toast.makeText(ScanResultActivity.this, "Thiết bị chưa kích hoạt...", Toast.LENGTH_LONG).show();
                                 }else if (ResPonseRs.contains("SYNC_BODY_NULL")) {
                                     Toast.makeText(ScanResultActivity.this, "Tham số gửi lên BODY=NULL", Toast.LENGTH_LONG).show();
                                 } else if (ResPonseRs.contains("SYNC_QRID_NULL")) {
                                     Toast.makeText(ScanResultActivity.this, "Mã số QRScaniD=NULL", Toast.LENGTH_LONG).show();
                                 }
                             }
                         }catch (Exception ex){ }
                        // finish();
                     }

                     @Override
                     public void onHttpFailer(Exception e) {
                         dismissProgressDialog();
                         Toast.makeText(ScanResultActivity.this,"Không thể đồng bộ:"+e.getMessage(),Toast.LENGTH_LONG).show();
                     }
                 },mUrlPostQR,"POST_QR",DataBody).execute();


        }catch (Exception ex){
            Toast.makeText(ScanResultActivity.this,"Không thể đồng bộ:"+ex.getMessage(),Toast.LENGTH_LONG).show();
            dismissProgressDialog();
        }
    }

    public String getAddressLocation(Double Longitude,Double Latitude){
        try{
            if (APINet.isNetworkAvailable(this)) {
                List<Address> lstAddress;
                Geocoder gCoder = new Geocoder(this, Locale.getDefault());
                lstAddress = gCoder.getFromLocation(Latitude, Longitude, 1);
                String mAddress = lstAddress.get(0).getAddressLine(0);
                return mAddress;
            }else{
                return "N/A";
            }
        }catch (Exception ex){}
        return "N/A";
    }


    //ĐỒNG BỘ DỮ LIỆU SẢN PHẨM
    private void onDownProduct(){
        try{
            final String mUrlGet=AppSetting.getInstance().URL_SyncDM(AppUtils.getImeil(ScanResultActivity.this),AppUtils.getImeilsim(ScanResultActivity.this),"DM_PRODUCT",false);
            new SyncGet(new APINetCallBack() {
                @Override
                public void onHttpStart() {
                    showProgressDialog("Đang tải dữ liệu sản phẩm.");
                }

                @Override
                public void onHttpSuccess(String ResPonseRs) {
                    if( ResPonseRs==null || ResPonseRs.toString().trim()==null || ResPonseRs.isEmpty()) {
                        Toast.makeText(ScanResultActivity.this,"Vui lòng kiểm tra kết nối mạng..",Toast.LENGTH_SHORT).show();
                        dismissProgressDialog();
                    }else if( ResPonseRs.equalsIgnoreCase("[]")){
                        dismissProgressDialog();
                        Toast.makeText(ScanResultActivity.this, "Không tìm thấy dữ liệu mới..", Toast.LENGTH_SHORT).show();
                        return;
                    }else if(ResPonseRs.contains("SYNC_REG: Thiết bị của bạn chưa được đăng ký")){
                        dismissProgressDialog();
                        Toast.makeText(ScanResultActivity.this, "Thiết bị của bạn chưa đăng ký. Vui lòng đăng ký trước...", Toast.LENGTH_SHORT).show();
                        return;
                    }else if(ResPonseRs.contains("SYNC_ACTIVE: Thiết bị của bạn đã đăng ký nhưng chưa kích hoạt")) {
                        dismissProgressDialog();
                        Toast.makeText(ScanResultActivity.this, "Thiết bị của bạn đã đăng ký nhưng chưa kích hoạt từ server...", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<Collection<DM_Product>>() {}.getType();
                            Collection<DM_Product> enums = gson.fromJson(ResPonseRs, type);
                            DM_Product[] ArrProduct = enums.toArray(new DM_Product[enums.size()]);

                            //Async Add Province
                            if ( ArrProduct!=null && ArrProduct.length>0) {
                                new AsyncUpdateProduct(new SyncCallBack() {
                                    @Override
                                    public void onSyncStart() {
                                        Toast.makeText(ScanResultActivity.this , "Đang cập nhật dữ liệu.", Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onSyncSuccess(String ResPonseRs) {
                                        Toast.makeText(ScanResultActivity.this, "Tải dữ liệu thành công.", Toast.LENGTH_SHORT).show();
                                        dismissProgressDialog();
                                    }
                                    @Override
                                    public void onSyncFailer(Exception e) {
                                        dismissProgressDialog();
                                    }
                                }).execute(ArrProduct);
                            }else{
                                dismissProgressDialog();
                                Toast.makeText(ScanResultActivity.this,"Không đọc được dữ liệu sản phẩm tải về",Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            dismissProgressDialog();
                            Toast.makeText(ScanResultActivity.this,"Không đọc được dữ liệu tải về"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                @Override
                public void onHttpFailer(Exception e) {
                    dismissProgressDialog();
                    Toast.makeText(ScanResultActivity.this,"Không đọc được dữ liệu tải về"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }, mUrlGet, "DWN_PRODUCT").execute();


        }catch (Exception ex){}
    }

    private void onSyncConfirm(String mtblName){
        final String mUrlConFirm=AppSetting.getInstance().URL_SyncConfirm(AppUtils.getImeil(this),AppUtils.getImeilsim(this),mtblName);
        new SyncGet(new APINetCallBack() {
            @Override
            public void onHttpStart() {}
            @Override
            public void onHttpSuccess(String ResPonseRs) {
                if(ResPonseRs!=null && !ResPonseRs.isEmpty() && ResPonseRs.equalsIgnoreCase("SYNC_OK")){
                    Toast.makeText(ScanResultActivity.this,"Xác nhận tải thành công",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onHttpFailer(Exception e) {dismissProgressDialog();}
        }, mUrlConFirm, "CONFITM").execute();

    }

    private class AsyncUpdateProduct extends AsyncTask<DM_Product[],String,String> {
        private SyncCallBack mSyncCallBack;
        private Exception mException;

        private AsyncUpdateProduct(SyncCallBack mCallBack) {
            this.mSyncCallBack = mCallBack;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(this.mSyncCallBack!=null){
                this.mSyncCallBack.onSyncStart();
            }
        }

        @Override
        protected String doInBackground(DM_Product[]... params) {
            try {
                int iSize = params[0].length;
                int iSqno = 1;
                for (DM_Product oItem : params[0]) {
                    if (!oItem.getProductCode().isEmpty()) {
                        mDB.addProduct(oItem);
                        publishProgress("[" + Integer.toString(iSqno) + "/" + Integer.toString(iSize) + "] " + oItem.getProductName());
                    }
                    iSqno += 1;
                }
            } catch (Exception ex) {
                mException = ex;
                ex.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            setTextProgressDialog(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (mSyncCallBack != null) {
                if (mException == null) {
                    mSyncCallBack.onSyncSuccess(result);
                    try {
                        DM_Product oProd = mDB.getProduct(tvProductCode.getText().toString().trim());
                        if (oProd != null) {
                            tvProductName.setText(oProd.getProductName());
                            tvUnit.setText(oProd.getUnit());
                            tvSpec.setText(oProd.getSpecification());
                        }
                    }catch (Exception ex){}

                    onSyncConfirm("DM_PRODUCT");
                } else {
                    mSyncCallBack.onSyncFailer(mException);
                }
            }
        }
    }



    /*14032019-ENTER QRCODE*/

    @OnClick(R.id.tvQrid)
    public void onQREnterCodeClicked(){
        if(isEnterCode){
            showEnterCode(tvQrid.getText().toString());
        }
    }


    public void showEnterCode(String mVal) {
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        View customView = inflater.inflate(R.layout.dialog_input, null);
        final EditText edtEnterCode = (EditText)customView.findViewById(R.id.tvEnterCode);
        final AlertDialog.Builder dlgbuilder = new AlertDialog.Builder(ScanResultActivity.this);
        dlgbuilder.setView(customView);

        edtEnterCode.setText(mVal);

        dlgbuilder.setTitle("MÃ QRCODE");
        dlgbuilder.setMessage("Nhập mã code bên dưới:");
        dlgbuilder.setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        dlgbuilder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }});
        //dlgbuilder.create().show();

        final AlertDialog mDlg=dlgbuilder.create();
        mDlg.show();
        mDlg.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtEnterCode.getText().toString().isEmpty()){
                    Toast.makeText(ScanResultActivity.this,"Bạn chưa nhập mã Code ?",Toast.LENGTH_SHORT).show();
                }else{
                    tvQrid.setText(edtEnterCode.getText().toString().trim());
                    if(APINet.isNetworkAvailable(ScanResultActivity.this)) {
                        onDownFindQRID(tvQrid.getText().toString());
                    }else{
                        mQRScan.setQrid(tvQrid.getText().toString());
                    }

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    //Log.d("ADD_QRSCAN","START");
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    mQRScan = new QR_QRSCAN();
                                    try {
                                        String sQRScanID="";
                                        try {
                                            SimpleDateFormat qrScanID = new SimpleDateFormat("ddMMyyyyHHmmssSS");
                                            sQRScanID = qrScanID.format(new Date());
                                            sQRScanID = tvQrid.getText().toString() + sQRScanID;
                                        }catch (Exception ex){
                                            Toast.makeText(ScanResultActivity.this,"Không tạo được mã QRScanID",Toast.LENGTH_SHORT).show();
                                            sQRScanID=tvQrid.getText()+sdf.format(new Date()).toString();
                                        }

                                        mQRScan.setQrscanid( sQRScanID);
                                        mQRScan.setQrid(tvQrid.getText().toString());
                                        mQRScan.setCommandNo(tvCommandNo.getText().toString());
                                        mQRScan.setProductCode(tvProductCode.getText().toString());
                                        mQRScan.setProductName(tvProductName.getText().toString());
                                        mQRScan.setUnit(tvUnit.getText().toString());
                                        mQRScan.setSpecification(tvSpec.getText().toString());
                                        mQRScan.setSync(Boolean.valueOf("false"));
                                        mQRScan.setImei(AppUtils.getImeil(ScanResultActivity.this));
                                        mQRScan.setImeiSim(AppUtils.getImeilsim(ScanResultActivity.this));
                                        mQRScan.setScanNo(Integer.valueOf("1"));
                                        mQRScan.setScanDay(sdf.format(new Date()));
                                        mQRScan.setScanSupportID("");
                                        mQRScan.setScanType("N");
                                        //GET CUSTOMER THEO LOCATION
                                        if(mLastLocation==null){
                                            locationTrack=new APIGPSTrack(ScanResultActivity.this);
                                            if(locationTrack.canGetLocation()){
                                                mLastLocation=locationTrack.getLngLat();
                                            }
                                        }

                                        if(mLastLocation!=null){
                                            mQRScan.setLongitude(mLastLocation.getLongitude());
                                            mQRScan.setLatitude(mLastLocation.getLatitude());

                                            //TÌM KHÁCH HÀNG THEO TỌA ĐỘ
                                            onAutoSearchCustomer(mQRScan.getLongitude(),mQRScan.getLatitude());

                                            mQRScan.setLocationAddress(getAddressLocation(mLastLocation.getLongitude(),mLastLocation.getLatitude()));
                                            Toast oToast=Toast.makeText(ScanResultActivity.this,"Tọa độ đã xác định tại:" + mQRScan.getLocationAddress(),Toast.LENGTH_LONG);
                                            oToast.setGravity(Gravity.CENTER,0,0);
                                            oToast.show();

                                        }else{
                                            Toast.makeText(ScanResultActivity.this,"Không xác định được tọa độ. Kiểm tra lại định vị của bạn",Toast.LENGTH_LONG).show();
                                        }

                                        if (oCustomer!=null && !oCustomer.getCustomerid().isEmpty()) {
                                            mQRScan.setCustomerid(oCustomer.getCustomerid());
                                            //mDB.addQRScan(mQRScan);
                                        }

                                    }catch (Exception ex){
                                        Toast.makeText(ScanResultActivity.this,"Không thể cập nhật dữ liệu quét."+ex.getMessage(),Toast.LENGTH_LONG).show();
                                    }


                                    if(!tvQrid.getText().toString().isEmpty()) {

                                        onLoadScanSupport();

                                        //TẢI DỮ LIỆU QRHIS
                                        onDownQRScanHis(tvQrid.getText().toString().trim());
                                    }
                                }
                            },300);




                    mDlg.dismiss();
                }
            }
        });
    }

    private void onDownFindQRID(final String mQRID){
        try{
            tvCommandNo.setText("");
            tvProductName.setText("");
            tvProductCode.setText("");
            tvUnit.setText("");
            tvSpec.setText("");

            final String mUrlGet=AppSetting.getInstance().URL_GetFindQRID(AppUtils.getImeil(ScanResultActivity.this),AppUtils.getImeilsim(ScanResultActivity.this),mQRID,false,0);
            new SyncGet(new APINetCallBack() {
                @Override
                public void onHttpStart() {
                    showProgressDialog("Đang tải dữ liệu ["+mQRID+"]...");
                }

                @Override
                public void onHttpSuccess(String ResPonseRs) {
                    dismissProgressDialog();
                    if( ResPonseRs==null || ResPonseRs.toString().trim()==null || ResPonseRs.isEmpty()) {
                        Toast.makeText(ScanResultActivity.this,"Vui lòng kiểm tra kết nối mạng..",Toast.LENGTH_SHORT).show();
                    }else if( ResPonseRs.equalsIgnoreCase("[]")){
                        Toast.makeText(ScanResultActivity.this, "Không tìm thấy dữ liệu mới..", Toast.LENGTH_SHORT).show();
                        return;
                    }else if(ResPonseRs.contains("SYNC_REG:Thiết bị của bạn chưa được đăng ký")){
                        Toast.makeText(ScanResultActivity.this, "Thiết bị của bạn chưa đăng ký. Vui lòng đăng ký trước...", Toast.LENGTH_SHORT).show();
                        return;
                    }else if(ResPonseRs.contains("SYNC_ACTIVE:Thiết bị của bạn đã đăng ký nhưng chưa kích hoạt")) {
                        Toast.makeText(ScanResultActivity.this, "Thiết bị của bạn đã đăng ký nhưng chưa kích hoạt từ server...", Toast.LENGTH_SHORT).show();
                        return;
                    }else if(ResPonseRs.contains("NOT_FOUND")) {
                       Toast oToast= Toast.makeText(ScanResultActivity.this, "Mã QR không hợp lệ. Không tìm thấy dữ liệu..", Toast.LENGTH_LONG);
                       oToast.setGravity(Gravity.CENTER,0,0);
                       oToast.show();
                       return;
                    }else{
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<Collection<DATA_JSON>>() {}.getType();
                            Collection<DATA_JSON> enums = gson.fromJson(ResPonseRs, type);
                            DATA_JSON[] ArrJson = enums.toArray(new DATA_JSON[enums.size()]);

                            String mQRRs="";
                            if(ArrJson.length>0){
                                mQRRs=ArrJson[0].getDATA_RS();
                            }
                            String[] ADataScan = mQRRs.split("#");
                            if (ADataScan.length >= 1) {
                                tvCommandNo.setText(ADataScan[1]);
                            }
                            if (ADataScan.length >= 2) {
                                tvProductCode.setText(ADataScan[2]);
                                DM_Product oProd = mDB.getProduct(ADataScan[2]);
                                try {
                                    if (oProd != null) {
                                        tvProductName.setText(oProd.getProductName());
                                        tvUnit.setText(oProd.getUnit());
                                        tvSpec.setText(oProd.getSpecification());
                                    } else {
                                        onDownProduct();
                                    }
                                } catch (Exception e) {
                                }
                            }

                            try{
                                mQRScan.setCommandNo(tvCommandNo.getText().toString());
                                mQRScan.setProductCode(tvProductCode.getText().toString());
                                mQRScan.setProductName(tvProductName.getText().toString());
                                mQRScan.setUnit(tvUnit.getText().toString());
                                mQRScan.setSpecification(tvSpec.getText().toString());
                            }catch (Exception ex){}

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ScanResultActivity.this,"Không đọc được dữ liệu tải về"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                @Override
                public void onHttpFailer(Exception e) {
                    dismissProgressDialog();
                    Toast.makeText(ScanResultActivity.this,"Không đọc được dữ liệu tải về"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }, mUrlGet, "DWN_QRCODE").execute();


        }catch (Exception ex){}
    }



}
