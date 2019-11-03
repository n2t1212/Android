package com.mimi.mimigroup.ui.visitcard;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import com.mimi.mimigroup.R;
import com.mimi.mimigroup.api.APIGPSTrack;
import com.mimi.mimigroup.api.APIGoogleTrack;
import com.mimi.mimigroup.api.APILocationCallBack;
import com.mimi.mimigroup.api.APINet;
import com.mimi.mimigroup.api.APINetCallBack;
import com.mimi.mimigroup.api.SyncFindCustomer;
import com.mimi.mimigroup.api.SyncFindCustomerCallback;
import com.mimi.mimigroup.api.SyncPost;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.DM_Customer_Distance;
import com.mimi.mimigroup.model.SM_VisitCard;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomEditText;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.utils.AppUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class VisitCardResultActivity extends BaseActivity implements AdapterView.OnItemSelectedListener{
    @BindView(R.id.spVisitCustomer)
    Spinner spVisitCustomer;

    @BindView(R.id.tvVisitTitle)
    CustomBoldTextView tvVisitTitle;

    @BindView(R.id.tvVisitDay)
    CustomBoldTextView tvVisitDay;
    @BindView(R.id.tvVisitTime)
    CustomBoldTextView tvVisitTime;
    @BindView(R.id.tvVisitLongitude)
    CustomBoldTextView tvVisitLongitude;
    @BindView(R.id.tvVisitLatitude)
    CustomBoldTextView tvVisitLatitude;
    @BindView(R.id.tvVisitLocationAddress)
    CustomBoldTextView tvVisitLocationAddress;
    @BindView(R.id.tvVisitNotes)
    CustomEditText tvVisitNotes;

    String VisitCardID;
    String mAction;
    DM_Customer_Distance oCustomer;
    DBGimsHelper mDB;
    private SM_VisitCard oVisitCard;

    Location mLocation;
    APIGPSTrack locationTrack;

    Boolean isNew=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitcard_result);
        mDB=DBGimsHelper.getInstance(this);

        locationTrack=new APIGPSTrack(VisitCardResultActivity.this);
        if(locationTrack.canGetLocation()){
           mLocation=locationTrack.getLocation();
        }else{
            locationTrack.showSettingsAlert();
        }

        VisitCardID = getIntent().getStringExtra("DATA_VISITCARD_ID");
        mAction=getIntent().getAction().toString();
        if(mAction=="IN"){
            tvVisitTitle.setText("CHẤM GIỜ VÀO");
        }else{
            tvVisitTitle.setText("CHẤM GIỜ RA");
        }

          try{
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            mLocation = locationTrack.getLocation();
                            if(mLocation==null){
                                locationTrack=new APIGPSTrack(VisitCardResultActivity.this);
                                if(locationTrack.canGetLocation()){
                                    mLocation=locationTrack.getLocation();
                                }
                            }

                            SimpleDateFormat vsid= new SimpleDateFormat("ddMMyyyyHHmmssSS");
                            SimpleDateFormat vsday = new SimpleDateFormat("yyyy-MM-dd");
                            //SimpleDateFormat vsTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                            SimpleDateFormat vsTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a",Locale.US);

                            String mVisitDay=vsday.format(new Date());
                            String mVisitTime=vsTime.format(new Date());

                            if(VisitCardID!=null && !VisitCardID.isEmpty()) {
                                isNew=true;

                                if(mLocation!=null) {
                                    onAutoSearchCustomer(mLocation.getLongitude(), mLocation.getLatitude());
                                }

                                oVisitCard = mDB.getVisitCard(VisitCardID);

                                if(oVisitCard.getVisitDay()==null || oVisitCard.getVisitDay().isEmpty()){
                                    oVisitCard.setVisitDay(mVisitDay);
                                    tvVisitDay.setText(mVisitDay);
                                }else{
                                    tvVisitDay.setText(oVisitCard.getVisitDay());
                                }

                                if(oVisitCard.getVisitTime()==null) {
                                    oVisitCard.setVisitTime(mVisitTime);
                                    tvVisitTime.setText(mVisitTime);
                                }else {
                                    tvVisitTime.setText(oVisitCard.getVisitTime());
                                }
                                if(oVisitCard.getCustomerID()!=null){
                                    setSpinCustomer(oVisitCard.getCustomerID());
                                }

                                if (oVisitCard.getLongitude() != null) {
                                    tvVisitLongitude.setText(Double.toString(oVisitCard.getLongitude()));
                                }else{
                                    if(mLocation!=null){
                                        oVisitCard.setLongitude(mLocation.getLongitude());
                                        tvVisitLongitude.setText(Double.toString(oVisitCard.getLongitude()));
                                    }
                                }
                                if (oVisitCard.getLatitude() != null) {
                                    tvVisitLatitude.setText(Double.toString(oVisitCard.getLatitude()));
                                }else{
                                    if(mLocation!=null){
                                        oVisitCard.setLatitude(mLocation.getLatitude());
                                        tvVisitLatitude.setText(Double.toString(oVisitCard.getLatitude()));
                                    }
                                }

                                if (oVisitCard.getLocationAddress() != null) {
                                    tvVisitLocationAddress.setText(oVisitCard.getLocationAddress());
                                }else{
                                    if(mLocation!=null){
                                        oVisitCard.setLocationAddress(getAddressLocation(mLocation.getLongitude(),mLocation.getLatitude()));
                                        tvVisitLocationAddress.setText(oVisitCard.getLocationAddress());
                                    }
                                }

                                if(oVisitCard.getVisitNotes()!=null){
                                    tvVisitNotes.setText(oVisitCard.getVisitNotes());
                                }else{
                                    tvVisitNotes.setText("");
                                }

                            }else{
                                //ADDNEW
                                oVisitCard=new SM_VisitCard();
                                oVisitCard.setCustomerID("");
                                oVisitCard.setVisitType("");
                                oVisitCard.setVisitDay("");
                                oVisitCard.setVisitTime("");
                                oVisitCard.setLongitude(Double.valueOf(0));
                                oVisitCard.setLatitude(Double.valueOf(0));
                                oVisitCard.setLocationAddress("");
                                oVisitCard.setSync(false);
                                oVisitCard.setVisitNotes("");

                                VisitCardID= AppUtils.getImeil(VisitCardResultActivity.this)+ vsid.format(new Date());
                                //TÌM KHÁCH HÀNG THEO TỌA ĐỘ
                                if(mLocation!=null) {
                                    onAutoSearchCustomer(mLocation.getLongitude(), mLocation.getLatitude());
                                }

                                if(VisitCardID==null || VisitCardID.isEmpty()){
                                   Toast mToast=Toast.makeText(VisitCardResultActivity.this,"Không tạo được mã thẻ thăm",Toast.LENGTH_LONG);
                                   mToast.setGravity(Gravity.CENTER,0,0);
                                   return;
                                }
                                if(mAction==null || mAction.isEmpty()){
                                    Toast mToast=Toast.makeText(VisitCardResultActivity.this,"Không xác định được loại (Vào/Ra)",Toast.LENGTH_LONG);
                                    mToast.setGravity(Gravity.CENTER,0,0);
                                    return;
                                }

                                oVisitCard.setVisitCardID(VisitCardID);
                                oVisitCard.setVisitDay(mVisitDay);
                                oVisitCard.setVisitType(mAction);

                                oVisitCard.setVisitTime(mVisitTime);
                                if(mLocation!=null) {
                                    oVisitCard.setLongitude(mLocation.getLongitude());
                                    oVisitCard.setLatitude(mLocation.getLatitude());
                                    oVisitCard.setLocationAddress(getAddressLocation(mLocation.getLongitude(),mLocation.getLatitude()));
                                }else{
                                    oVisitCard.setLongitude(Double.valueOf(0));
                                    oVisitCard.setLatitude(Double.valueOf(0));
                                    oVisitCard.setLocationAddress("");
                                    Toast mToast=Toast.makeText(VisitCardResultActivity.this,"Không xác định được tọa độ",Toast.LENGTH_LONG);
                                    mToast.setGravity(Gravity.CENTER,0,0);
                                }

                                if (oCustomer!=null && !oCustomer.getCustomerid().isEmpty()) {
                                    oVisitCard.setCustomerID(oCustomer.getCustomerid());
                                }
                                oVisitCard.setSync(false);


                                tvVisitDay.setText(oVisitCard.getVisitDay());
                                tvVisitTime.setText(oVisitCard.getVisitTime());
                                tvVisitLongitude.setText(Double.toString(oVisitCard.getLongitude()));
                                tvVisitLatitude.setText(Double.toString(oVisitCard.getLatitude()));
                                tvVisitLocationAddress.setText(oVisitCard.getLocationAddress());

                            } //ESLE NULL


                            //XÁC ĐỊNH THEO PA2
                            if(mLocation==null){
                                Toast.makeText(VisitCardResultActivity.this, "Không xác định được tọa độ. Đang xác định theo PA2.", Toast.LENGTH_LONG).show();
                                //GOOGLE API
                                new APIGoogleTrack(new APILocationCallBack() {
                                    @Override
                                    public void onCurrentLocation(Location MTLocation) {
                                        mLocation=MTLocation;
                                        if(mLocation!=null){

                                            onAutoSearchCustomer(mLocation.getLongitude(), mLocation.getLatitude());

                                            if(isNew) {
                                                if (oVisitCard.getLongitude() != null) {
                                                    tvVisitLongitude.setText(Double.toString(oVisitCard.getLongitude()));
                                                } else {
                                                    oVisitCard.setLongitude(mLocation.getLongitude());
                                                    tvVisitLongitude.setText(Double.toString(oVisitCard.getLongitude()));
                                                }

                                                if (oVisitCard.getLatitude() != null) {
                                                    tvVisitLatitude.setText(Double.toString(oVisitCard.getLatitude()));
                                                } else {
                                                    oVisitCard.setLatitude(mLocation.getLatitude());
                                                    tvVisitLatitude.setText(Double.toString(oVisitCard.getLatitude()));
                                                }

                                                if (oVisitCard.getLocationAddress() != null) {
                                                    tvVisitLocationAddress.setText(oVisitCard.getLocationAddress());
                                                } else {
                                                    oVisitCard.setLocationAddress(getAddressLocation(mLocation.getLongitude(), mLocation.getLatitude()));
                                                    tvVisitLocationAddress.setText(oVisitCard.getLocationAddress());
                                                }
                                            }else {
                                                oVisitCard.setLongitude(mLocation.getLongitude());
                                                oVisitCard.setLatitude(mLocation.getLatitude());
                                                oVisitCard.setLocationAddress(getAddressLocation(mLocation.getLongitude(), mLocation.getLatitude()));

                                                tvVisitLongitude.setText(Double.toString(oVisitCard.getLongitude()));
                                                tvVisitLatitude.setText(Double.toString(oVisitCard.getLatitude()));
                                                tvVisitLocationAddress.setText(oVisitCard.getLocationAddress());
                                            }
                                            Toast.makeText(VisitCardResultActivity.this, "PA2: "+oVisitCard.getLocationAddress(), Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(VisitCardResultActivity.this, "Không xác định được tọa độ. Vui lòng đợi nếu mới bật GPS.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                },VisitCardResultActivity.this);

                            }


                        }
                    },
                    300);
           }catch (Exception ex){}

        //}
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
        setResult(2000);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @OnClick(R.id.btnRegetLocation)
    public void onRegetLocation(){
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 50);
            }

            locationTrack = new APIGPSTrack(VisitCardResultActivity.this);
            if (!locationTrack.canGetLocation()) {
                locationTrack.showSettingsAlert();
                return;
            }

            mLocation= locationTrack.getLocation();
            if (mLocation == null) {
                Toast.makeText(VisitCardResultActivity.this, "Đang xác định lại tọa độ.", Toast.LENGTH_SHORT).show();
                locationTrack = new APIGPSTrack(VisitCardResultActivity.this);
                if (locationTrack.canGetLocation()) {
                   mLocation = locationTrack.getLocation();
                }
            }

            if (mLocation!= null) {
                oVisitCard.setLongitude(mLocation.getLongitude());
                oVisitCard.setLatitude(mLocation.getLatitude());
                //TÌM KHÁCH HÀNG THEO TỌA ĐỘ
                onAutoSearchCustomer(oVisitCard.getLongitude(), oVisitCard.getLatitude());
                oVisitCard.setLocationAddress(getAddressLocation(mLocation.getLongitude(), mLocation.getLatitude()));

                tvVisitLongitude.setText(Double.toString(oVisitCard.getLongitude()));
                tvVisitLatitude.setText(Double.toString(oVisitCard.getLatitude()));
                tvVisitLocationAddress.setText(oVisitCard.getLocationAddress());

                Toast.makeText(VisitCardResultActivity.this, oVisitCard.getLocationAddress(), Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(VisitCardResultActivity.this, "Không xác định được tọa độ. Đang xác định theo PA2.", Toast.LENGTH_LONG).show();
                //GOOGLE API
                new APIGoogleTrack(new APILocationCallBack() {
                    @Override
                    public void onCurrentLocation(Location MTLocation) {
                        mLocation=MTLocation;
                        if(mLocation!=null){
                            oVisitCard.setLongitude(mLocation.getLongitude());
                            oVisitCard.setLatitude(mLocation.getLatitude());
                            //TÌM KHÁCH HÀNG THEO TỌA ĐỘ
                            onAutoSearchCustomer(oVisitCard.getLongitude(), oVisitCard.getLatitude());
                            oVisitCard.setLocationAddress(getAddressLocation(mLocation.getLongitude(), mLocation.getLatitude()));

                            tvVisitLongitude.setText(Double.toString(oVisitCard.getLongitude()));
                            tvVisitLatitude.setText(Double.toString(oVisitCard.getLatitude()));
                            tvVisitLocationAddress.setText(oVisitCard.getLocationAddress());

                            Toast.makeText(VisitCardResultActivity.this, "PA2: "+oVisitCard.getLocationAddress(), Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(VisitCardResultActivity.this, "Không xác định được tọa độ. Vui lòng đợi nếu mới bật GPS.", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(VisitCardResultActivity.this,"Vị trí 2:"+getAddressLocation(mLocation.getLongitude(),mLocation.getLatitude()),Toast.LENGTH_SHORT).show();

                    }
                },VisitCardResultActivity.this);
                //oGoogleLocation.onRemoveUpdate();
            }

        }catch (Exception ex){
            Toast.makeText(VisitCardResultActivity.this, "Chưa xác định được tọa độ: "+ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setSpinCustomer(String mCustomerID){
      try{
          for(int i=0;i<spVisitCustomer.getCount();i++){
              if(spVisitCustomer.getItemAtPosition(i).toString().equalsIgnoreCase(mCustomerID)){
                  spVisitCustomer.setSelection(i);
                  return;
              }
          }
      }catch (Exception ex){}
    }

    //ASync Tìm khách hàng theo tọa độ
    public void onAutoSearchCustomer(Double mLongitude,Double mLatitude){
        float mScope= Float.valueOf(AppSetting.getInstance().getParscope());//100Met;
        if(mScope<=0){
            try {
                mScope = Float.valueOf(mDB.getParam("PAR_SCOPE"));
            }catch (Exception ex){}
        }
        int mReturnSize=Integer.valueOf(5);
        //Toast.makeText(VisitCardResultActivity.this,"Đang xác định khách hàng bán kính "+Float.toString(mScope),Toast.LENGTH_SHORT).show();

        try {
          if (mLongitude > 0 && mLatitude > 0) {
              final List<DM_Customer_Distance> lstCus;
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
                              spVisitCustomer.setTag(lstSel);
                              spVisitCustomer.setOnItemSelectedListener(VisitCardResultActivity.this);
                              List<String> customers = new ArrayList<>();
                              for (DM_Customer_Distance oCus : lstSel) {
                                 customers.add(oCus.getCustomerName());
                              }

                              ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(VisitCardResultActivity.this, android.R.layout.simple_spinner_item, customers);
                              dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                              spVisitCustomer.setAdapter(dataAdapter);

                              //for (DM_Customer_Distance oC:lstSel){
                              //    Log.d("CUS_SORT:", oC.getDistance().toString());
                             // }
                          }

                          if(lstSel.isEmpty() || lstSel.size()<=0){
                              Toast.makeText(VisitCardResultActivity.this,"Không tìm thấy khách hàng trong phạm vi tọa độ này.",Toast.LENGTH_LONG).show();
                          }
                      }

                      @Override
                      public void onSyncFailer(Exception e) {
                         // dismissProgressDialog();
                          Toast.makeText(VisitCardResultActivity.this,"Không thể tìm khách hàng theo tọa độ."+e.getMessage(),Toast.LENGTH_LONG).show();
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
        List<DM_Customer_Distance> lstCusDistance = (List<DM_Customer_Distance>) spVisitCustomer.getTag();
        oCustomer = lstCusDistance.get(position);
        try {
            oVisitCard.setCustomerID(oCustomer.getCustomerid());
        }catch (Exception ex){}
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @OnClick(R.id.btnPostVisit)
    public void onUploadVisit(){
        if (APINet.isNetworkAvailable(VisitCardResultActivity.this)==false){
            Toast.makeText(VisitCardResultActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
            return;
        }

        //TEST
        //oCustomer=new DM_Customer_Distance();
        //oCustomer.setCustomerid("B565E728-D9B4-44A7-9DE5-FD36C7295EB2");
        //oVisitCard.setCustomerID("B565E728-D9B4-44A7-9DE5-FD36C7295EB2");

        if (oVisitCard != null && oCustomer != null){

            oVisitCard.setCustomerID(oCustomer.getCustomerid());
            if (oVisitCard.getVisitType() == null || oVisitCard.getVisitType().isEmpty() || (!oVisitCard.getVisitType().equalsIgnoreCase("IN") && !oVisitCard.getVisitType().equalsIgnoreCase("OUT")) ) {
                Toast mToast = Toast.makeText(this, "Không xác định được loại thẻ Vào/Ra", Toast.LENGTH_LONG);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                mToast.show();
                return;
            }

            if(oVisitCard.getCustomerID()==null || oVisitCard.getCustomerID().isEmpty()){
                Toast mToast = Toast.makeText(this, "Chưa xác định được khách hàng", Toast.LENGTH_LONG);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                mToast.show();
                return;
            }

            if (oVisitCard.getVisitCardID() == null || oVisitCard.getVisitCardID().isEmpty()) {
                Toast mToast = Toast.makeText(this, "Chưa tạo được mã thẻ thăm", Toast.LENGTH_LONG);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                mToast.show();
                return;
            }
            if (oVisitCard.getVisitDay() == null || oVisitCard.getVisitDay().isEmpty()) {
                Toast mToast = Toast.makeText(this, "Chưa xác định được ngày thăm", Toast.LENGTH_LONG);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                mToast.show();
                return;
            }
            oVisitCard.setVisitNotes(tvVisitNotes.getText().toString());

            mDB.addVisitCard(oVisitCard);
            onPostVisit(oVisitCard);
        }else{
            Toast mToast = Toast.makeText(this, "Chưa xác định được khách hàng", Toast.LENGTH_LONG);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
        }
    }

    @OnClick(R.id.btnSaveVisit)
    public void onSaveVisitOnly(){
      try {
          if (oVisitCard != null && oCustomer != null) {

              if((oVisitCard.getCustomerID()==null || oVisitCard.getCustomerID().isEmpty())){
                  Toast mToast = Toast.makeText(this, "Chưa xác định được khách hàng", Toast.LENGTH_LONG);
                  mToast.setGravity(Gravity.CENTER, 0, 0);
                  mToast.show();
                  return;
              }

              if (oVisitCard.getVisitCardID() == null || oVisitCard.getVisitCardID().isEmpty()) {
                  Toast mToast = Toast.makeText(this, "Chưa tạo được mã thẻ thăm", Toast.LENGTH_LONG);
                  mToast.setGravity(Gravity.CENTER, 0, 0);
                  mToast.show();
                  return;
              }
              if (oVisitCard.getVisitType() == null || oVisitCard.getVisitType().isEmpty()) {
                  Toast mToast = Toast.makeText(this, "Không xác định được loại thẻ (Vào/Ra)", Toast.LENGTH_LONG);
                  mToast.setGravity(Gravity.CENTER, 0, 0);
                  mToast.show();
                  return;
              }
              if (oVisitCard.getVisitDay() == null || oVisitCard.getVisitDay().isEmpty()) {
                  Toast mToast = Toast.makeText(this, "Chưa xác định được ngày thăm", Toast.LENGTH_LONG);
                  mToast.setGravity(Gravity.CENTER, 0, 0);
                  mToast.show();
                  return;
              }
              oVisitCard.setVisitNotes(tvVisitNotes.getText().toString());

              mDB.addVisitCard(oVisitCard);
              Toast.makeText(this, "Ghi dữ liệu thẻ thăm thành công", Toast.LENGTH_SHORT).show();
              setResult(2001);
              finish();
          }else{
              Toast mToast = Toast.makeText(this, "Chưa xác định được khách hàng", Toast.LENGTH_LONG);
              mToast.setGravity(Gravity.CENTER, 0, 0);
              mToast.show();
          }
      }catch (Exception ex){
          Toast mToast = Toast.makeText(this, "Lỗi ghi dữ liệu:"+ex.getMessage(), Toast.LENGTH_LONG);
          mToast.setGravity(Gravity.CENTER, 0, 0);
          mToast.show();
          return;
      }
    }



    @OnClick(R.id.btnReCheckVisit)
     public void onReCheckVisit(){
      try {
          final Dialog oDlg=new Dialog(VisitCardResultActivity.this);
          oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
          oDlg.setContentView(R.layout.dialog_yesno);
          oDlg.setTitle("");
          CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
          dlgTitle.setText("XÁC NHẬN");
          CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
          dlgContent.setText("Bạn có chắc muốn chấm lại không ?");
          CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
          CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

          btnYes.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  onReCheck();
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

      }catch (Exception ex){}
    }


    private void onReCheck(){
        try{
            Toast.makeText(VisitCardResultActivity.this,"Đang xác định lại tọa độ...",Toast.LENGTH_LONG).show();

            locationTrack = new APIGPSTrack(VisitCardResultActivity.this);
            if (locationTrack.canGetLocation()) {
               mLocation = locationTrack.getLngLat();
            }else{
                locationTrack.showSettingsAlert();
                return;
            }

            SimpleDateFormat vsid = new SimpleDateFormat("ddMMyyyyHHmmssSS");
            SimpleDateFormat vsday = new SimpleDateFormat("yyyy-MM-dd");
            //SimpleDateFormat vsTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            SimpleDateFormat vsTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a",Locale.US);
            String mVisitDay = vsday.format(new Date());
            String mVisitTime = vsTime.format(new Date());

            if(tvVisitDay.getText()==null || tvVisitDay.getText().toString().isEmpty()){
                tvVisitDay.setText(mVisitDay);
                oVisitCard.setVisitDay(mVisitDay);
            }

            try {
                if (oVisitCard.getCustomerID() == null || oVisitCard.getCustomerID().isEmpty()) {
                    if (oCustomer != null) {
                        oVisitCard.setCustomerID(oCustomer.getCustomerid());
                    }else{
                        Toast.makeText(VisitCardResultActivity.this,"Không xác định được khách hàng...",Toast.LENGTH_LONG).show();
                    }
                }
                setSpinCustomer(oVisitCard.getCustomerID());
            }catch (Exception ex){}

            if(tvVisitTime.getText()==null || tvVisitTime.getText().toString().isEmpty()){
                tvVisitTime.setText(mVisitTime);
                oVisitCard.setVisitTime(mVisitTime);
            }else{
                tvVisitTime.setText(mVisitTime);
                oVisitCard.setVisitTime(mVisitTime);
            }

            mLocation=locationTrack.getLocation();
            if(mLocation!=null){
                tvVisitLongitude.setText(Double.toString(mLocation.getLongitude()));
                oVisitCard.setLongitude(mLocation.getLongitude());

                tvVisitLatitude.setText(Double.toString(mLocation.getLatitude()));
                oVisitCard.setLatitude(mLocation.getLatitude());

                tvVisitLocationAddress.setText(getAddressLocation(mLocation.getLongitude(),mLocation.getLatitude()));
                oVisitCard.setLocationAddress(tvVisitLocationAddress.getText().toString());

                Toast.makeText(VisitCardResultActivity.this, oVisitCard.getLocationAddress(), Toast.LENGTH_LONG).show();

            }

        }catch (Exception ex){}
    }

    //POST QR_SCAN
    private void onPostVisit(final SM_VisitCard oVisit){
        try{
            if (APINet.isNetworkAvailable(VisitCardResultActivity.this)==false){
                Toast.makeText(VisitCardResultActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
                return;
            }

            String Imei=AppUtils.getImeil(VisitCardResultActivity.this);
            String ImeiSim=AppUtils.getImeilsim(VisitCardResultActivity.this);
            if(ImeiSim.isEmpty()){
                ImeiSim=mDB.getParam("PARA_IMEISIM");
                if(ImeiSim.isEmpty()) {
                    Toast.makeText(VisitCardResultActivity.this, "Không đọc được số IMEI Sim từ thiết bị cho việc đồng bộ. Kiểm tra Sim của bạn", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            if(Imei.isEmpty()){
                Imei=mDB.getParam("PARA_IMEI");
                if(Imei.isEmpty()){
                    Toast.makeText(VisitCardResultActivity.this,"Không đọc được số IMEI từ thiết bị..",Toast.LENGTH_LONG).show();
                    return;
                }
            }
            if(oVisit==null){
                Toast.makeText(VisitCardResultActivity.this,"Không tìm thấy dữ liệu đã quét..",Toast.LENGTH_LONG).show();
                return;
            }
            if(oVisit.getVisitCardID()==null || oVisit.getVisitCardID().isEmpty()){
                Toast.makeText(VisitCardResultActivity.this,"Không tìm thấy mã Thẻ thăm",Toast.LENGTH_SHORT).show();
                return;
            }else if(oVisit.getVisitType()==null || oVisit.getVisitType().isEmpty()){
                Toast.makeText(VisitCardResultActivity.this,"Không tìm thấy loại thẻ (Vào/Ra)",Toast.LENGTH_SHORT).show();
                return;
            }
            else if(oVisit.getVisitDay().isEmpty() || oVisit.getVisitDay()==""){
                Toast.makeText(VisitCardResultActivity.this,"Không tìm thấy ngày thăm",Toast.LENGTH_SHORT).show();
                return;
            }
            else if(oVisit.getCustomerID()==null || oVisit.getCustomerID().isEmpty()){
                Toast.makeText(VisitCardResultActivity.this,"Bạn chưa chọn khách hàng cho lần quét này",Toast.LENGTH_SHORT).show();
                return;
            }

            final String mUrlPostVisit=AppSetting.getInstance().URL_PostVisitCard();
            try {

                if(oVisit.getVisitType()==null || oVisit.getVisitType().isEmpty()){
                    oVisit.setVisitType("");
                }
                if(oVisit.getVisitTime()==null || oVisit.getVisitTime().isEmpty()){
                    oVisit.setVisitTime("");
                }
                if(oVisit.getLongitude()==null){
                    oVisit.setLongitude(Double.valueOf(0));
                }
                if(oVisit.getLatitude()==null){
                    oVisit.setLatitude(Double.valueOf(0));
                }
                if (oVisit.getLocationAddress() == null) {
                    oVisit.setLocationAddress("N/A");
                }
                if(oVisit.getVisitNotes()==null){
                    oVisit.setVisitNotes("N/A");
                }

            }catch (Exception ex){ Toast.makeText(VisitCardResultActivity.this,"Không tìm thấy dữ liệu thẻ thăm.."+ex.getMessage(),Toast.LENGTH_LONG).show();}

            RequestBody DataBody = new FormBody.Builder()
                    .add("imei",Imei)
                    .add("imeisim", ImeiSim)
                    .add("visitcardid", oVisit.getVisitCardID())
                    .add("visittype", oVisit.getVisitType())
                    .add("visitday", oVisit.getVisitDay())

                    .add("customerid", oVisit.getCustomerID())

                    .add("visittime", oVisit.getVisitTime())
                    .add("longitude", Double.toString(oVisit.getLongitude()))
                    .add("latitude", Double.toString(oVisit.getLatitude()))
                    .add("locationaddress",oVisit.getLocationAddress())

                    .add("notes", oVisit.getVisitNotes())
                    .add("represent", "")

                    .build();

                 new SyncPost(new APINetCallBack() {
                     @Override
                     public void onHttpStart() {
                         showProgressDialog("Đang thiết lập kết nối...");
                         //Log.d("SYNC_VISITCARD_POST", mUrlPostVisit);
                     }

                     @Override
                     public void onHttpSuccess(String ResPonseRs) {
                         try {
                             dismissProgressDialog();
                             if (!ResPonseRs.isEmpty()) {
                                 if (ResPonseRs.contains("SYNC_OK")) {
                                     Toast.makeText(VisitCardResultActivity.this, "Đồng  bộ thành công.", Toast.LENGTH_LONG).show();
                                     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a",Locale.US);
                                     oVisit.setSyncDay(sdf.format(new Date()));
                                     oVisit.setSync(true);
                                     mDB.editVisitCard(oVisit);
                                     setResult(2001);
                                     finish();
                                 }else if(ResPonseRs.contains("SYNC_REG")){
                                     Toast.makeText(VisitCardResultActivity.this, "Thiết bị chưa được đăng ký...", Toast.LENGTH_LONG).show();
                                 }else if(ResPonseRs.contains("SYNC_ACTIVE")){
                                     Toast.makeText(VisitCardResultActivity.this, "Thiết bị chưa được kích hoạt từ Server...", Toast.LENGTH_LONG).show();
                                 }else if(ResPonseRs.contains("SYNC_NULL")){
                                     Toast.makeText(VisitCardResultActivity.this, "Không nhận được dữ liệu gửi về [DATA=NULL]", Toast.LENGTH_LONG).show();
                                 }else if(ResPonseRs.contains("SYNC_VISITID_NULL")){
                                     Toast.makeText(VisitCardResultActivity.this, "Không tạo được mã thẻ thăm [VISITCARDID=NULL]", Toast.LENGTH_LONG).show();
                                 }else if(ResPonseRs.contains("SYNC_VISIT_DAY_NULL")){
                                     Toast.makeText(VisitCardResultActivity.this, "Không đọc được ngày thăm VISIT_DAY_NULL", Toast.LENGTH_LONG).show();
                                 }
                                 else{
                                     Toast.makeText(VisitCardResultActivity.this, "Lỗi không xác định:"+ResPonseRs, Toast.LENGTH_LONG).show();
                                 }
                             }else{
                                 Toast.makeText(VisitCardResultActivity.this, "Không xác định kết quả", Toast.LENGTH_LONG).show();
                             }
                         }catch (Exception ex){
                             Toast.makeText(VisitCardResultActivity.this, "Lỗi cập nhật đồng bộ:"+ex.getMessage(), Toast.LENGTH_LONG).show();
                         }
                     }

                     @Override
                     public void onHttpFailer(Exception e) {
                         dismissProgressDialog();
                         Toast.makeText(VisitCardResultActivity.this,"Không thể đồng bộ:"+e.getMessage(),Toast.LENGTH_LONG).show();
                     }
                 }, mUrlPostVisit,"POST_VISITCARD",DataBody).execute();


        }catch (Exception ex){
            Toast.makeText(VisitCardResultActivity.this,"Không thể đồng bộ:"+ex.getMessage(),Toast.LENGTH_LONG).show();
            dismissProgressDialog();
        }
    }

    public String getAddressLocation(Double Longitude,Double Latitude){
        try{
            if(APINet.isNetworkAvailable(this)) {
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

}
