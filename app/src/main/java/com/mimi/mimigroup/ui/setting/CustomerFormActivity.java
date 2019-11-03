package com.mimi.mimigroup.ui.setting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.api.APIGPSTrack;
import com.mimi.mimigroup.api.APINet;
import com.mimi.mimigroup.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.DM_Customer;
import com.mimi.mimigroup.model.DM_District;
import com.mimi.mimigroup.model.DM_Province;
import com.mimi.mimigroup.model.DM_Ward;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomerFormActivity extends BaseActivity implements AdapterView.OnItemSelectedListener{
    @BindView(R.id.tvCustomerCode)
    EditText edtCustomerCode;
    @BindView(R.id.tvCustomerName)
    EditText edtCustomerNam;
    @BindView(R.id.tvStreet)
    EditText edtStreet;
    @BindView(R.id.tvLocationAddress)
    EditText edtLocationAddress;

    @BindView(R.id.spProvince)
    Spinner spProvince;
    @BindView(R.id.spDistrict)
    Spinner spDistrict;
    @BindView(R.id.spWard)
    Spinner spWard;


    @BindView(R.id.tvLongitude)
    EditText edtLongitude;
    @BindView(R.id.tvLatitude)
    EditText edtLatitude;
    @BindView(R.id.tvNotes)
    EditText edtNotes;

    private DBGimsHelper mDB;
    private String mCustomerID;
    private String mAction;
    private Location mLocation;
    APIGPSTrack locationTrack;

    private List<DM_Province> lstProvince=new ArrayList<DM_Province>();
    private List<DM_District> lstDistrict=new ArrayList<DM_District>();
    private List<DM_Ward> lstWard=new ArrayList<DM_Ward>();
    private Integer mProvinceid=-1;
    private Integer mDistrictid=-1;
    private Integer mWardid=-1;
    private  DM_Customer oCusSel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_frm);
        mDB=DBGimsHelper.getInstance(this);

        mCustomerID  = getIntent().getStringExtra("CustomerID");
        mAction=getIntent().getAction().toString();
        if(mAction=="EDIT") {
            if (mCustomerID != null && !mCustomerID.isEmpty()) {
                if (onLoadDataSource(mCustomerID)) {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    onLoadProvince();
                                }

                                ;
                            }, 500);
                }
            }
        }else{
            edtCustomerCode.setEnabled(true);
            oCusSel=new DM_Customer();
            onLoadProvince();
        }
    }


    private boolean onLoadDataSource(String CustomerID){
        try{
            oCusSel=new DM_Customer();
            oCusSel=mDB.getCustomer(CustomerID);
            if(oCusSel!=null){
                edtCustomerCode.setText(oCusSel.getCustomerCode());
                edtCustomerNam.setText(oCusSel.getCustomerName());
                edtStreet.setText(oCusSel.getStreet());
                edtLongitude.setText(Double.toString((oCusSel.getLongitudeTemp())));
                edtLatitude.setText(Double.toString((oCusSel.getLatitudeTemp())));
                edtLocationAddress.setText(oCusSel.getLocationAddressTemp());

                edtNotes.setText(oCusSel.getNotes());
                return true;
            }
            return false;
        }catch (Exception ex){
            Toast.makeText(this,"Không thể đọc dữ liệu:"+ex.getMessage(),Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void onLoadProvince(){
        lstProvince=mDB.getAllProvince();
        List<String> lstSProvince=new ArrayList<String>();
        for (DM_Province oProvince:lstProvince){
            lstSProvince.add(oProvince.getProvince());
        }
        spProvince.setTag(lstProvince);
        spProvince.setOnItemSelectedListener(this);
        ArrayAdapter<String> pAdapter = new ArrayAdapter<String>(CustomerFormActivity.this, android.R.layout.simple_spinner_item, lstSProvince);
        pAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProvince.setAdapter(pAdapter);
        try{
            final int iPos=pAdapter.getPosition(oCusSel.getProvinceName());
            spProvince.post(new Runnable() {
                @Override
                public void run() {
                    spProvince.setSelection(iPos);
                }
            });

        }catch (Exception ex){}

    }

    private void onLoadDistrict(Integer mProvinceID){
        try{
            if(mProvinceID!=null){
                lstDistrict=mDB.getDistrict(mProvinceID);
                List<String> lstSDistrict=new ArrayList<String>();
                for(DM_District oDis:lstDistrict){
                    lstSDistrict.add(oDis.getDistrict());
                }

                //spDistrict.setTag(lstDistrict);
                spDistrict.setOnItemSelectedListener(this);
                ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(CustomerFormActivity.this, android.R.layout.simple_spinner_item, lstSDistrict);
                dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDistrict.setAdapter(dAdapter);
                try{
                    final int iPos=dAdapter.getPosition(oCusSel.getDistrictName());
                    spDistrict.post(new Runnable() {
                        @Override
                        public void run() {
                            spDistrict.setSelection(iPos);
                        }
                    });
                }catch (Exception ex){}

            }
        }catch (Exception ex){}
    }

    private void onLoadWard(Integer mDistrictID){
        try{
            if(mDistrictID!=null){
                lstWard=mDB.getWard(mDistrictID);
                List<String> lstSWard=new ArrayList<String>();
                for(DM_Ward oWar:lstWard){
                    lstSWard.add(oWar.getWard());
                }

                //spWard.setTag(lstWard);
                spWard.setOnItemSelectedListener(this);
                ArrayAdapter<String> wAdapter = new ArrayAdapter<String>(CustomerFormActivity.this, android.R.layout.simple_spinner_item, lstSWard);
                wAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spWard.setAdapter(wAdapter);
                try{
                    final int iPos=wAdapter.getPosition(oCusSel.getWardName());
                    spWard.post(new Runnable() {
                        @Override
                        public void run() {
                            spWard.setSelection(iPos);
                        }
                    });
                }catch (Exception ex){}
            }
        }catch (Exception ex){}
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spProvince:
                //if(lstProvince!=null && lstProvince.size()>0) {
                // String mSel = parent.getItemAtPosition(position).toString();
                DM_Province oProvince = lstProvince.get(position);
                mProvinceid = oProvince.getProvinceid();
                //oCusSel.setProvinceName(oProvince.getProvince());
                onLoadDistrict(mProvinceid);
                //}
            case R.id.spDistrict:
                if(lstDistrict!=null && lstDistrict.size()>0) {
                    //String mSel2 = parent.getItemAtPosition(position).toString();
                    int iPos= spDistrict.getSelectedItemPosition();
                    if(iPos>=0 && iPos<lstDistrict.size()) {
                        DM_District oDistrict = lstDistrict.get(iPos);
                        mDistrictid = oDistrict.getDistrictid();
                        //oCusSel.setDistrictName(oDistrict.getDistrict());
                        onLoadWard(mDistrictid);
                    }
                }
            case R.id.spWard:
                if(lstWard!=null && lstWard.size()>0) {
                    int iPos= spDistrict.getSelectedItemPosition();
                    if(iPos>=0 && iPos<lstWard.size()) {
                        DM_Ward oWard = lstWard.get(iPos);
                        //oCusSel.setWardName(oWard.getWard());
                        mWardid = oWard.getWardid();
                    }
                }
        }
        //List<DM_Province> lstCusDistance = (List<DM_Province>) spProvince.getTag();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}


    @OnClick(R.id.ivBack)
    public void onBack(){
        setResult(2000);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @OnClick(R.id.btnGetLocation)
    public void onGetLocationClicked(){
        if (ActivityCompat.checkSelfPermission(CustomerFormActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CustomerFormActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CustomerFormActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 50);
        }

        locationTrack = new APIGPSTrack(CustomerFormActivity.this);
            if (locationTrack.canGetLocation()) {
                mLocation = locationTrack.getLocation();
            }else{
                locationTrack.showSettingsAlert();
                return;
            }

        mLocation=locationTrack.getLocation();
        if(mLocation!=null){
            edtLatitude.setText(Double.toString(mLocation.getLatitude()));
            edtLongitude.setText(Double.toString(mLocation.getLongitude()));
            edtLocationAddress.setText(getAddressLocation(mLocation.getLongitude(),mLocation.getLatitude()));
            Toast.makeText(CustomerFormActivity.this, "Đã cập nhật vị trí khách hàng " + edtCustomerNam.getText(), Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(CustomerFormActivity.this, "Không xác định được tọa độ. Vui lòng đợi nếu bạn mới bật GPS.", Toast.LENGTH_SHORT).show();
        }

    }


    @OnClick(R.id.btnViewMap)
    public void onViewMapClicked(){
        try {
            if (!edtLongitude.getText().toString().isEmpty() && !edtLatitude.getText().toString().isEmpty() ) {

                String url = "http://www.google.com/maps/place/" + edtLatitude.getText() + "," + edtLongitude.getText();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                ((BaseActivity) CustomerFormActivity.this).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }else {
                Toast.makeText(CustomerFormActivity.this, "Khách hàng chưa có tọa độ vị trí", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){}

    }


    @OnClick(R.id.btnSave)
    public void onSaveClicked(){
        try{
            DM_Customer oCus=new DM_Customer();
            if (mAction.contains("EDIT")) {
                oCus.setCustomerid(mCustomerID);
                oCus.setCustomerCode(edtCustomerCode.getText().toString().trim());
                oCus.setCustomerName(edtCustomerNam.getText().toString().trim());
                if(mProvinceid!=-1) {
                    oCus.setProvinceid(mProvinceid);
                }
                if(mDistrictid!=-1) {
                    oCus.setDistrictid(mDistrictid);
                }
                if(mWardid!=-1) {
                    oCus.setWardid(mWardid);
                }
                oCus.setStreet(edtStreet.getText().toString().trim());

                oCus.setLongitudeTemp(Double.valueOf(edtLongitude.getText().toString()));
                oCus.setLatitudeTemp(Double.valueOf(edtLatitude.getText().toString()));
                oCus.setLocationAddressTemp(edtLocationAddress.getText().toString());
                oCus.setNotes(edtNotes.getText().toString());
                if(oCus.getEdit()!=1){
                    //Nếu mới thì vẫn giữ trạng thái mới
                    oCus.setEdit(2);
                }
                if (mDB.editCustomer(oCus)){
                    Toast.makeText(CustomerFormActivity.this,"Ghi khách hàng thành công",Toast.LENGTH_LONG).show();
                    setResult(2001);
                    finish();
                };

            }else{
                if(mCustomerID==null || mCustomerID.isEmpty()){
                    SimpleDateFormat qrScanID = new SimpleDateFormat("ddMMyyyyHHmmssSS");
                    mCustomerID = "CUS"+qrScanID.format(new Date());
                }
                oCus.setCustomerid(mCustomerID);
                oCus.setCustomerCode(edtCustomerCode.getText().toString().toUpperCase().trim());
                oCus.setCustomerName(edtCustomerNam.getText().toString().trim());
                oCus.setProvinceid(mProvinceid);
                oCus.setDistrictid(mDistrictid);
                oCus.setWardid(mWardid);
                oCus.setStreet(edtStreet.getText().toString().trim());
                oCus.setLongitudeTemp(Double.valueOf(edtLongitude.getText().toString()));
                oCus.setLatitudeTemp(Double.valueOf(edtLatitude.getText().toString()));
                oCus.setLocationAddressTemp(edtLocationAddress.getText().toString());
                oCus.setNotes(edtNotes.getText().toString());
                oCus.setEdit(1);

                String mRs=mDB.addCustomer2(oCus);
                if (mRs.equalsIgnoreCase("OK")){
                    Toast.makeText(CustomerFormActivity.this,"Ghi khách hàng thành công",Toast.LENGTH_LONG).show();
                    setResult(2001);
                    finish();
                }else{
                    Toast.makeText(CustomerFormActivity.this,"Không thể thêm khách hàng, ERR:"+mRs,Toast.LENGTH_LONG).show();
                }
            }


        }catch (Exception ex){
            Toast.makeText(CustomerFormActivity.this,"Không thể cấp nhật. "+ex.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
    }

    public String getAddressLocation(Double Longitude,Double Latitude){
        try{
            if(APINet.isNetworkAvailable(CustomerFormActivity.this)) {
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
