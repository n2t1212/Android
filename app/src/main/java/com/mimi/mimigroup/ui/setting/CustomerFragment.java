package com.mimi.mimigroup.ui.setting;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.Gravity;
import android.view.View;

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
import com.mimi.mimigroup.api.SyncGet;
import com.mimi.mimigroup.api.SyncPost;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.base.BaseFragment;
import com.mimi.mimigroup.model.DM_Customer;

import com.mimi.mimigroup.model.FlagPost;
import com.mimi.mimigroup.ui.adapter.CustomerAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomEditText;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.utils.AppUtils;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import com.mimi.mimigroup.db.DBGimsHelper;

public class CustomerFragment extends BaseFragment implements CustomerView, CustomerAdapter.OnSelectedCustomerListener {
   @BindView(R.id.rvCustomer)
    RecyclerView rvCustomer;

    @BindView(R.id.filterCustomerCode)
    CustomEditText filterCustomerCode;
    @BindView(R.id.filterCustomerName)
    CustomEditText filterCustomerName;
    @BindView(R.id.filterProvince)
    CustomEditText filterProvince;
    @BindView(R.id.filterDistrict)
    CustomEditText filterDistrict;

    CustomerAdapter adapter;
    DM_Customer customer;
    private DBGimsHelper mDB;

    Location mLocation;
    APIGPSTrack locationTrack;

    final int REQUEST_CODE_ADD=1;
    final int REQUEST_CODE_EDIT=2;

    private ProgressDialog mtDialog;
    boolean isEditAdd=false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCustomer.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDB=DBGimsHelper.getInstance(getContext());

        //LISTEN ONCLICK SELECT FROM ADAPTER
        adapter = new CustomerAdapter(new CustomerAdapter.CustomerItemClickListener() {
            @Override
            public void onCustomerItemClick(List<DM_Customer> SelectList) {
               if(SelectList.size()>0){
                   ((CustomerActivity)getActivity()).setButtonVisible(View.VISIBLE);
               }else{
                   ((CustomerActivity)getActivity()).setButtonVisible(View.INVISIBLE);
               }
            }
        });
        adapter.setListener(this);
        rvCustomer.setAdapter(adapter);

        try {
            locationTrack = new APIGPSTrack(getContext());
            if (!locationTrack.canGetLocation()) {
                locationTrack.showSettingsAlert();
            }
        }catch (Exception ex){}

        ((BaseActivity)getActivity()).showProgressDialog("Đang nạp dữ liệu...");
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onLoadDataSource();
                    };
                },500);


        filterCustomerCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.mFilterCustomerCode=s.toString();
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        filterCustomerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.mFilterCustomerName=s.toString();
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        filterProvince.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.mFilterProvince=s.toString();
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        filterDistrict.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.mFilterDistrict=s.toString();
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        /*
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onStartTimerCheckApprove();
                    };
                },5000); */
    }


    @Override
    public void onStop() {
        try {
            if (locationTrack != null) {
                locationTrack.stopListener();
            }
        }catch (Exception ex){}

        super.onStop();
    }

    @Override
    public void onSelectedCustomer(DM_Customer customer) {
        this.customer = customer;
    }

    @Override
    protected int getLayoutResourceId() {
        //IF >=ANDROID 5.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return R.layout.activity_customer;
        }else {
            return R.layout.activity_customer;
        }
    }

    private void onLoadDataSource(){
        try{
            List<DM_Customer> lstCus= mDB.getAllCustomer();
            adapter.setCustomerList(lstCus);
            adapter.SelectedList.clear();

            Toast.makeText(getContext(),"Có "+Integer.toString(lstCus.size())+" dòng được nạp...",Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
             Toast.makeText(getContext(),"Không thể nạp dữ liệu khách hàng.",Toast.LENGTH_LONG).show();
        }

        ((BaseActivity)getActivity()).dismissProgressDialog();
     }

    @Override
    public void onError(String msg) {
        ((BaseActivity) getActivity()).dismissProgressDialog();
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onGetCustomerList(List<DM_Customer> customerList) {
        ((BaseActivity) getActivity()).dismissProgressDialog();
        adapter.setCustomerList(customerList);
    }

    public String getAddressLocation(Double Longitude,Double Latitude){
        try{
            if(APINet.isNetworkAvailable(getContext())) {
                List<Address> lstAddress;
                Geocoder gCoder = new Geocoder(getContext(), Locale.getDefault());
                lstAddress = gCoder.getFromLocation(Latitude, Longitude, 1);
                String mAddress = lstAddress.get(0).getAddressLine(0);
                return mAddress;
            }
        }catch (Exception ex){}
        return "N/A";
    }

    //CALL FROM POPUP MENU CUSTOMERACTIVITY
    //CALL TỪ MAIN ACTIVITI MENU CustomerActivity
    public void onDownloadClicked(final boolean isAll){
        if (APINet.isNetworkAvailable(getContext())==false){
            Toast.makeText(getContext(),"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
            return;
        }

        final String mUrlGet=AppSetting.getInstance().URL_SyncDM(AppUtils.getImeil(getContext()),AppUtils.getImeilsim(getContext()),"DM_CUSTOMER",isAll);
        new SyncGet(new APINetCallBack() {
            @Override
            public void onHttpStart() {
                ((BaseActivity) getActivity()).showProgressDialog("Đang kết nối máy chủ.");
               // Log.d("URL_SYNC_CUSTOMER",mUrlGet);
            }
            @Override
            public void onHttpSuccess(String ResPonseRs) {
                if( ResPonseRs==null || ResPonseRs.toString().trim()==null || ResPonseRs.isEmpty()) {
                    ((BaseActivity) getActivity()).dismissProgressDialog();
                    Toast.makeText(getContext(), "Vui lòng kiểm tra kết nối mạng..", Toast.LENGTH_SHORT).show();
                }else if( ResPonseRs.equalsIgnoreCase("[]")){
                    ((BaseActivity) getActivity()).dismissProgressDialog();
                    Toast.makeText(getContext(), "Không tìm thấy dữ liệu mới..", Toast.LENGTH_SHORT).show();
                    return;
                }else if(ResPonseRs.contains("SYNC_REG: Thiết bị của bạn chưa được đăng ký")){
                    ((BaseActivity) getActivity()).dismissProgressDialog();
                    Toast.makeText(getContext(), "Thiết bị của bạn chưa đăng ký. Vui lòng đăng ký trước...", Toast.LENGTH_SHORT).show();
                    return;
                }else if(ResPonseRs.contains("SYNC_ACTIVE: Thiết bị của bạn đã đăng ký nhưng chưa kích hoạt")) {
                    ((BaseActivity) getActivity()).dismissProgressDialog();
                    Toast.makeText(getContext(), "Thiết bị của bạn đã đăng ký nhưng chưa kích hoạt từ server...", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                   // Log.d("RESP_SYNC_DMCUS",ResPonseRs);
                    try {
                        //JSONObject json = new JSONObject(myResponse);
                        //json.getString("Para_Value");
                        //txtString.setText("First Name: "+json.getJSONObject("data").getString("first_name") + "\nLast Name: " + json.getJSONObject("data").getString("last_name"));

                        Gson gson = new Gson();
                        Type type = new TypeToken<Collection<DM_Customer>>() {}.getType();
                        Collection<DM_Customer> enums = gson.fromJson(ResPonseRs, type);
                        DM_Customer[] ArrCus = enums.toArray(new DM_Customer[enums.size()]);

                        //Async Add Customer
                        if (ArrCus!=null && ArrCus.length>0) {

                            new AsyncUpdateCustomer(new SyncCallBack() {
                                @Override
                                public void onSyncStart() {
                                    if(isAll) {
                                        mDB.delCustomer("", true);
                                    }
                                    Toast.makeText(getContext(), "Đang cập nhật dữ liệu.", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onSyncSuccess(String ResPonseRs) {
                                    Toast.makeText(getContext(), "Tải dữ liệu thành công.", Toast.LENGTH_SHORT).show();
                                    ((BaseActivity) getActivity()).dismissProgressDialog();
                                    onLoadDataSource();
                                }

                                @Override
                                public void onSyncFailer(Exception e) {
                                    Toast.makeText(getContext(),"Lỗi cập nhật khách hàng:"+e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                                    ((BaseActivity) getActivity()).dismissProgressDialog();
                                }
                            }).execute(ArrCus);
                        }else{
                            ((BaseActivity) getActivity()).dismissProgressDialog();
                            Toast.makeText(getContext(),"Không đọc được dữ liệu tải về",Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ((BaseActivity) getActivity()).dismissProgressDialog();
                        onError(e.getMessage().toString());
                    }
                }
            }
            @Override
            public void onHttpFailer(Exception e) {
                onError(e.getMessage());
            }
        }, mUrlGet, "DWN_DMCUSTOMER").execute();
    }


    public void onGetLocationClicked(final boolean isReloadList){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 50);
        }

        final List<DM_Customer> oCustomerSel=adapter.SelectedList;
        if(oCustomerSel==null || oCustomerSel.size()<=0){
            Toast.makeText(getContext(), "Bạn chưa chọn khách hàng.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(oCustomerSel.size()>1){
            Toast.makeText(getContext(), "Bạn chọn quá nhiều khách hàng để lấy tọa độn. Vui lòng chọn lại..", Toast.LENGTH_SHORT).show();
            adapter.clearSelected();
            return;
        }
        Toast.makeText(getContext(), "Đang xác định tọa độ.", Toast.LENGTH_LONG).setGravity(Gravity.CENTER,0,0);

        mLocation=null;
        if(locationTrack==null) {
            locationTrack = new APIGPSTrack(getContext());
        }

        if (locationTrack.canGetLocation()) {
            mLocation = locationTrack.getLocation();
        }else{
            locationTrack.showSettingsAlert();
        }

        //NẾU API KHÔNG XÁC ĐỊNH ĐƯỢC -> LÁY THEO GPSPROVIDER
        if(mLocation==null) {
            locationTrack.stopListener();
            mLocation = locationTrack.getLocation();
        }

        if(mLocation!=null){
            oCustomerSel.get(0).setLongitudeTemp(mLocation.getLongitude());
            oCustomerSel.get(0).setLatitudeTemp(mLocation.getLatitude());
            oCustomerSel.get(0).setLocationAddressTemp(getAddressLocation(mLocation.getLongitude(),mLocation.getLatitude()));

            if(oCustomerSel.get(0).getEdit()!=1){
                oCustomerSel.get(0).setEdit(2); //Sửa
            }

            mDB.editCustomer(oCustomerSel.get(0));

            //UPDATE EDIT STATUS GRID
            final DM_Customer oCus=mDB.getCustomer(oCustomerSel.get(0).getCustomerid());
            if(oCus.getLocationAddressTemp()!=null) {
                Toast oToast = Toast.makeText(getContext(), "Vị trí đã cập nhật:" + oCus.getLocationAddressTemp(), Toast.LENGTH_LONG);
                oToast.setGravity(Gravity.CENTER, 0, 0);
                oToast.show();
            }else{
                Toast oToast = Toast.makeText(getContext(), "Vị trí đã cập nhật", Toast.LENGTH_LONG);
                oToast.setGravity(Gravity.CENTER, 0, 0);
                oToast.show();
            }

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            adapter.setUpdate(oCus,oCus.getEdit());
                            if(isReloadList){
                                //onLoadDataSource();
                                adapter.SelectedList.clear();
                            }
                        };
                    },20);

        }else{

            Toast.makeText(getContext(), "Không xác định được tọa độ. Đang xác định theo PA2.", Toast.LENGTH_LONG).show();

            new APIGoogleTrack(new APILocationCallBack() {
                @Override
                public void onCurrentLocation(Location MTLocation) {
                    mLocation = MTLocation;
                    if (mLocation!= null) {
                        oCustomerSel.get(0).setLongitudeTemp(mLocation.getLongitude());
                        oCustomerSel.get(0).setLatitudeTemp(mLocation.getLatitude());
                        oCustomerSel.get(0).setLocationAddressTemp(getAddressLocation(mLocation.getLongitude(),mLocation.getLatitude()));

                        if(oCustomerSel.get(0).getEdit()!=1){
                            oCustomerSel.get(0).setEdit(2); //Sửa
                        }

                        mDB.editCustomer(oCustomerSel.get(0));

                        //UPDATE EDIT STATUS GRID
                        final DM_Customer oCus=mDB.getCustomer(oCustomerSel.get(0).getCustomerid());
                        if(oCus.getLocationAddressTemp()!=null) {
                            Toast oToast = Toast.makeText(getContext(), "Vị trí đã cập nhật:" + oCus.getLocationAddressTemp(), Toast.LENGTH_LONG);
                            oToast.setGravity(Gravity.CENTER, 0, 0);
                            oToast.show();
                        }else{
                            Toast oToast = Toast.makeText(getContext(), "Vị trí đã cập nhật", Toast.LENGTH_LONG);
                            oToast.setGravity(Gravity.CENTER, 0, 0);
                            oToast.show();
                        }

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        adapter.setUpdate(oCus,oCus.getEdit());
                                        if(isReloadList){
                                            //onLoadDataSource();
                                            adapter.SelectedList.clear();
                                        }
                                    };
                                },20);

                    }else{
                        Toast.makeText(getContext(),"Không xác định được tọa độ. Kiểm tra lại định vị của bạn",Toast.LENGTH_LONG).show();
                    }
                }
            },getContext());

            Toast.makeText(getContext(), "Không xác định được tọa độ. Vui lòng đợi nếu bạn mới bật GPS.", Toast.LENGTH_SHORT).show();
        }
    }


    public void onViewMapClicked(){
        try {
            List<DM_Customer> oCustomerSel=adapter.SelectedList;
            if (oCustomerSel == null || oCustomerSel.size()<=0) {
                Toast.makeText(getActivity(), "Bạn chưa chọn khách hàng", Toast.LENGTH_SHORT).show();
                return;
            }
            if (oCustomerSel.size()>1) {
                Toast.makeText(getActivity(), "Bạn chọn quá nhiều khách hàng để xem. Vui lòng chọn lại..", Toast.LENGTH_SHORT).show();
                adapter.clearSelected();
                return;
            }

            if (oCustomerSel.get(0).getLatitudeTemp() > 0 && oCustomerSel.get(0).getLongitudeTemp() > 0) {
                String url = "http://www.google.com/maps/place/" + oCustomerSel.get(0).getLatitudeTemp() + "," + oCustomerSel.get(0).getLongitudeTemp();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                ((BaseActivity) getActivity()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                Toast.makeText(getActivity(), "Khách hàng chưa có tọa độ vị trí", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception ex){}
    }

    public void onAddCustomerClicked(){
        SimpleDateFormat qrScanID = new SimpleDateFormat("ddMMyyyyHHmmssSS");
        String mCustomerID = "CUS"+qrScanID.format(new Date());

        Intent intent = new Intent(getContext(), CustomerFormActivity.class);
        intent.setAction("ADD");
        intent.putExtra("CustomerID",mCustomerID );
        //startActivity(intent);
        isEditAdd=true;
        startActivityForResult(intent,REQUEST_CODE_ADD);
    }

    public void onEditCustomerClicked(){
        try {
            List<DM_Customer> oCustomerSel = adapter.SelectedList;
            if (oCustomerSel == null || oCustomerSel.size() <= 0) {
                Toast.makeText(getActivity(), "Bạn chưa chọn khách hàng", Toast.LENGTH_SHORT).show();
                return;
            }
            if (oCustomerSel.size() > 1) {
                Toast.makeText(getActivity(), "Bạn chọn quá nhiều khách hàng dể sửa. Vui lòng chọn lại", Toast.LENGTH_SHORT).show();
                adapter.clearSelected();
                return;
            }
            if (oCustomerSel.get(0).getCustomerid() != "") {
                Intent intent = new Intent(getContext(), CustomerFormActivity.class);
                intent.setAction("EDIT");
                intent.putExtra("CustomerID", oCustomerSel.get(0).getCustomerid());
                //startActivity(intent);
                isEditAdd=true;
                startActivityForResult(intent,REQUEST_CODE_EDIT);
            } else {
                Toast.makeText(getContext(), "Bạn chưa chọn khách hàng.", Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Toast.makeText(getContext(), "ERR."+ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void onDelCustomerClicked(){
        final List<DM_Customer> oCustomerSel=adapter.SelectedList;
        if (oCustomerSel == null || oCustomerSel.size()<=0) {
            Toast.makeText(getActivity(), "Bạn chưa chọn khách hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getContext());
        alertDialog2.setTitle("Xác nhận");
        alertDialog2.setMessage("Bạn có chắc muốn xóa khách hàng?");
        alertDialog2.setPositiveButton("Đồng ý",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        for (DM_Customer oCus:oCustomerSel){
                            //oCus.setEdit(3);
                            //mDB.editCustomer(oCus);
                            mDB.delCustomer(oCus.getCustomerid(),false);
                        }
                        onLoadDataSource();
                        Toast.makeText(getContext(),"Đã cập nhật trạng thái xóa.",Toast.LENGTH_SHORT).show();
                    }
                });
        alertDialog2.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        alertDialog2.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isEditAdd=false;
        if (requestCode == REQUEST_CODE_ADD) {
            //resultcode=0:Back,1:Save
            if (resultCode == 2001) {
                onLoadDataSource();
            }
        }
        if(requestCode==REQUEST_CODE_EDIT){
            if (resultCode == 2001) {
                //UPDATE EDIT STATUS GRID
                DM_Customer oCus=mDB.getCustomer(adapter.SelectedList.get(0).getCustomerid());
                adapter.setUpdate(oCus,oCus.getEdit());
                //onLoadDataSource();
            }
        }
    }


    public void onUploadClicked(){
        if (APINet.isNetworkAvailable(getContext())==false){
            Toast.makeText(getContext(),"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
            return;
        }

        final List<DM_Customer> oCustomerSel=adapter.SelectedList;
        if (oCustomerSel == null || oCustomerSel.size()<=0) {
            Toast.makeText(getActivity(), "Bạn chưa chọn khách hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        final String Imei=AppUtils.getImeil(getContext());
        final String ImeiSim=AppUtils.getImeilsim(getContext());
        if(ImeiSim.isEmpty() || Imei.isEmpty()){
            Toast.makeText(getContext(),"Không đọc được số IMEI từ thiết bị cho việc đồng bộ. Kiểm tra Sim của bạn",Toast.LENGTH_LONG).show();
            return;
        }
        final String mUrlPostCus=AppSetting.getInstance().URL_PostCus();

        final Dialog oDlg=new Dialog(getContext());
        oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        oDlg.setContentView(R.layout.dialog_yesno);
        oDlg.setTitle("");
        CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
        dlgTitle.setText("XÁC NHẬN");
        CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
        dlgContent.setText("Bạn có chắc muốn tải lên?");
        CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
        CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPostCustomer(oCustomerSel,Imei,ImeiSim,mUrlPostCus);
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
    }
    //END MENU POPUP


    //POST CUSTOMER
    private void onPostCustomer(final List<DM_Customer> lstCus, String mImei, String mImeiSim, final String mUrlPost) {
        try {
            isEditAdd=true;
            ((BaseActivity) getActivity()).showProgressDialog("Đang thiết lập kết nối..");

            final FlagPost flagPost=new FlagPost(0,0);
            for (DM_Customer mCus:lstCus){
                    final DM_Customer oCus=mDB.getCustomer(mCus.getCustomerid());
                    if (oCus.getCustomerid() != null && !oCus.getCustomerid().isEmpty() && oCus.getEdit()>0) {
                        if(oCus.getCustomerCode()==null || oCus.getCustomerCode().isEmpty()){
                            oCus.setCustomerCode("NA");
                        }
                        if (oCus.getShortName() == null) {
                            oCus.setShortName("");
                        }
                        if (oCus.getProvinceid() == null) {
                            oCus.setProvinceid(0);
                        }
                        if (oCus.getDistrictid() == null) {
                            oCus.setDistrictid(0);
                        }
                        if (oCus.getWardid() == null) {
                            oCus.setWardid(0);
                        }
                        if (oCus.getStreet() == null) {
                            oCus.setStreet("");
                        }
                        if (oCus.getTax() == null) {
                            oCus.setTax("");
                        }
                        if (oCus.getTel() == null) {
                            oCus.setTel("");
                        }
                        if (oCus.getEmail() == null) {
                            oCus.setEmail("");
                        }
                        if (oCus.getFax() == null) {
                            oCus.setFax("");
                        }
                        if (oCus.getLongitudeTemp() == null) {
                            oCus.setLongitudeTemp(0);
                        }
                        if (oCus.getLatitudeTemp() == null) {
                            oCus.setLatitudeTemp(0);
                        }
                        if (oCus.getLocationAddressTemp() == null) {
                            oCus.setLocationAddressTemp("");
                        }
                        if (oCus.getIsLevel() == null) {
                            oCus.setIsLevel(0);
                        }
                        if (oCus.getEdit() == null) {
                            oCus.setEdit(0);
                        }
                        if (oCus.getNotes() == null) {
                            oCus.setNotes("");
                        }

                        RequestBody DataBody = new FormBody.Builder()
                                .add("imei", mImei)
                                .add("imeisim", mImeiSim)
                                .add("customerid", oCus.getCustomerid())
                                .add("customercode", oCus.getCustomerCode())
                                .add("customername", oCus.getCustomerName())
                                .add("shortname", oCus.getShortName())
                                .add("provinceid", Integer.toString(oCus.getProvinceid()))
                                .add("districtid", Integer.toString(oCus.getDistrictid()))
                                .add("wardid", Integer.toString(oCus.getWardid()))
                                .add("street", oCus.getStreet())
                                .add("tax", oCus.getTax())
                                .add("tel", oCus.getTel())
                                .add("email", oCus.getEmail())
                                .add("fax", oCus.getTax())
                                .add("longitude", Double.toString(oCus.getLongitudeTemp()))
                                .add("latitude", Double.toString(oCus.getLatitudeTemp()))
                                .add("locationaddress", oCus.getLocationAddressTemp())
                                .add("islevel", Integer.toString(oCus.getIsLevel()))
                                .add("isstatus", Integer.toString(0))
                                .add("edit", Integer.toString(oCus.getEdit()))
                                .add("notes", oCus.getNotes())
                                .add("supplier", "")
                                .build();

                        SyncPost oSyncPost=  new SyncPost(new APINetCallBack() {
                            @Override
                            public void onHttpStart() {
                                //((BaseActivity) getActivity()).showProgressDialog("Đang đồng bộ "+oCus.getCustomerName());
                                flagPost.setSendPost(flagPost.getSendPost()+1);
                            }

                            @Override
                            public void onHttpSuccess(String ResPonseRs) {
                                try{
                                    flagPost.setReceivePost(flagPost.getReceivePost()+1);

                                    if (ResPonseRs!=null && !ResPonseRs.isEmpty()) {
                                        if (ResPonseRs.contains("SYNC_OK")) {
                                            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            if(oCus.getEdit()==1){
                                                mDB.delCustomer(oCus.getCustomerid(),false);
                                            }else {
                                                oCus.setEdit(4);
                                                mDB.editCustomer(oCus);
                                            }
                                            Toast.makeText(getContext(),"Đồng bộ "+oCus.getCustomerName() +" thành công.",Toast.LENGTH_SHORT).show();

                                        }else if(ResPonseRs.contains("SYNC_REG:Thiết bị của bạn chưa được đăng ký")){
                                            Toast.makeText(getContext(),"Thiết bị chưa đăng ký...",Toast.LENGTH_SHORT).show();
                                        }else if(ResPonseRs.contains("SYNC_ACTIVE:Thiết bị của bạn đã đăng ký nhưng chưa kích hoạt")){
                                            Toast.makeText(getContext(),"Thiết bị đã đăng ký nhưng chưa kích hoạt...",Toast.LENGTH_SHORT).show();
                                        }else if(ResPonseRs.contains("SYNC_EMP:Thiết bị chưa chỉ định cho nhân viên quản quản lý")){
                                            Toast.makeText(getContext(),"Thiết bị đã đăng ký nhưng chỉ định nhân viên quản lý...",Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(getContext(),"Không thể đồng bộ "+oCus.getCustomerName(),Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception ex) {
                                    Toast.makeText(getContext(),"Không thể đồng bộ "+oCus.getCustomerName(),Toast.LENGTH_LONG).show();
                                }

                                if(flagPost.getSendPost()==flagPost.getReceivePost()){
                                    ((BaseActivity) getActivity()).dismissProgressDialog();
                                    onLoadDataSource();
                                    isEditAdd=false;
                                }
                            }

                            @Override
                            public void onHttpFailer(Exception e) {
                                Toast mToast=Toast.makeText(getContext(),"Không thể đồng bộ "+oCus.getCustomerName(),Toast.LENGTH_LONG);
                                mToast.setGravity(Gravity.CENTER,0,0);
                                mToast.show();

                                flagPost.setReceivePost(flagPost.getReceivePost()+1);
                                if(flagPost.getSendPost()==flagPost.getReceivePost()){
                                    onLoadDataSource();
                                    ((BaseActivity) getActivity()).dismissProgressDialog();
                                    isEditAdd=false;
                                }
                            }

                        }, mUrlPost, "POST_CUS", DataBody);
                        //oSyncPost.execute();

                        if(Build.VERSION.SDK_INT>=11) {
                            oSyncPost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }else{
                            oSyncPost.execute();
                        }

                         //Thread.sleep(800);
                        //SystemClock.sleep(1500);
                    }
                }

        } catch (Exception ex) {
            ((BaseActivity)getContext()).dismissProgressDialog();
        }
        ((BaseActivity)getContext()).dismissProgressDialog();
    }






    //POST CUSTOMER
    private void onPostCustomerBK(final List<DM_Customer> lstCus, String mImei, String mImeiSim, final String mUrlPost) {
        try {
             isEditAdd=true;

             new onAsyncCusPost(new SyncCallBack() {
                 @Override
                 public void onSyncStart() {
                     ((BaseActivity)getContext()).showProgressDialog("Đang đóng gói dữ liệu...");
                 }

                 @Override
                 public void onSyncSuccess(String ResPonseRs) {
                     Toast.makeText(getContext(),"Đồng bộ dữ liệu thành công.",Toast.LENGTH_LONG).show();
                      new android.os.Handler().postDelayed(
                             new Runnable() {
                                 public void run() {
                                     ((BaseActivity)getContext()).dismissProgressDialog();
                                     onLoadDataSource();
                                     isEditAdd=false;
                                     //onStartTimerCheckApprove();
                                 };
                      },3000);

                 }

                 @Override
                 public void onSyncFailer(Exception e) {
                     ((BaseActivity)getContext()).dismissProgressDialog();
                 }
             },mUrlPost, mImei, mImeiSim).execute(lstCus);
        } catch (Exception ex) {
            ((BaseActivity)getContext()).dismissProgressDialog();
        }
    }
    //ASYNC BACKGROUDN DOWNLOAD CUSTOMER
    private class onAsyncCusPost extends AsyncTask<List<DM_Customer>,String,String>{
        private SyncCallBack mSyncCallBack;
        private Exception mException;

        String mImei="";
        String mImeiSim="";
        String mUrlPost="";
        private onAsyncCusPost(SyncCallBack syncCallBack, String urlPost,String imei,String imeisim){
            this.mImei=imei;
            this.mImeiSim=imeisim;
            this.mUrlPost=urlPost;
            this.mSyncCallBack=syncCallBack;
        }
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            if(this.mSyncCallBack!=null){
                this.mSyncCallBack.onSyncStart();
            }
        }
        @Override
        protected String doInBackground(List<DM_Customer>...params){
            try{
                for (DM_Customer mCus:params[0]){
                    final DM_Customer oCus=mDB.getCustomer(mCus.getCustomerid());
                    if (oCus.getCustomerid() != null && !oCus.getCustomerid().isEmpty() && oCus.getEdit()>0) {

                        publishProgress("Đang đồng bộ "+oCus.getCustomerName());

                        if(oCus.getCustomerCode()==null || oCus.getCustomerCode().isEmpty()){
                            oCus.setCustomerCode("NA");
                        }
                        if (oCus.getShortName() == null) {
                            oCus.setShortName("");
                        }
                        if (oCus.getProvinceid() == null) {
                            oCus.setProvinceid(0);
                        }
                        if (oCus.getDistrictid() == null) {
                            oCus.setDistrictid(0);
                        }
                        if (oCus.getWardid() == null) {
                            oCus.setWardid(0);
                        }
                        if (oCus.getStreet() == null) {
                            oCus.setStreet("");
                        }
                        if (oCus.getTax() == null) {
                            oCus.setTax("");
                        }
                        if (oCus.getTel() == null) {
                            oCus.setTel("");
                        }
                        if (oCus.getEmail() == null) {
                            oCus.setEmail("");
                        }
                        if (oCus.getFax() == null) {
                            oCus.setFax("");
                        }
                        if (oCus.getLongitudeTemp() == null) {
                            oCus.setLongitudeTemp(0);
                        }
                        if (oCus.getLatitudeTemp() == null) {
                            oCus.setLatitudeTemp(0);
                        }
                        if (oCus.getLocationAddressTemp() == null) {
                            oCus.setLocationAddressTemp("");
                        }
                        if (oCus.getIsLevel() == null) {
                            oCus.setIsLevel(0);
                        }
                        if (oCus.getEdit() == null) {
                            oCus.setEdit(0);
                        }
                        if (oCus.getNotes() == null) {
                            oCus.setNotes("");
                        }

                        RequestBody DataBody = new FormBody.Builder()
                                .add("imei", mImei)
                                .add("imeisim", mImeiSim)
                                .add("customerid", oCus.getCustomerid())
                                .add("customercode", oCus.getCustomerCode())
                                .add("customername", oCus.getCustomerName())
                                .add("shortname", oCus.getShortName())
                                .add("provinceid", Integer.toString(oCus.getProvinceid()))
                                .add("districtid", Integer.toString(oCus.getDistrictid()))
                                .add("wardid", Integer.toString(oCus.getWardid()))
                                .add("street", oCus.getStreet())
                                .add("tax", oCus.getTax())
                                .add("tel", oCus.getTel())
                                .add("email", oCus.getEmail())
                                .add("fax", oCus.getTax())
                                .add("longitude", Double.toString(oCus.getLongitudeTemp()))
                                .add("latitude", Double.toString(oCus.getLatitudeTemp()))
                                .add("locationaddress", oCus.getLocationAddressTemp())
                                .add("islevel", Integer.toString(oCus.getIsLevel()))
                                .add("isstatus", Integer.toString(0))
                                .add("edit", Integer.toString(oCus.getEdit()))
                                .add("notes", oCus.getNotes())
                                .add("supplier", "")
                                .build();

                      SyncPost oSyncPost=  new SyncPost(new APINetCallBack() {
                            @Override
                            public void onHttpStart() { }

                            @Override
                            public void onHttpSuccess(String ResPonseRs) {
                                try {
                                    if (!ResPonseRs.isEmpty()) {
                                        if (ResPonseRs.contains("SYNC_OK")) {
                                            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            if(oCus.getEdit()==1){
                                                mDB.delCustomer(oCus.getCustomerid(),false);
                                            }else {
                                                oCus.setEdit(4);
                                                mDB.editCustomer(oCus);
                                            }
                                            //adapter.setUpdate(oCus, 4);
                                            //publishProgress("Đồng bộ: "+oCus.getCustomerName() + " thành công.");
                                            Toast.makeText(getContext(),"Đồng bộ "+oCus.getCustomerName() +" thành công.",Toast.LENGTH_SHORT).show();
                                        }else if(ResPonseRs.contains("SYNC_REG:Thiết bị của bạn chưa được đăng ký")){
                                            Toast.makeText(getContext(),"Thiết bị chưa đăng ký...",Toast.LENGTH_SHORT).show();
                                        }else if(ResPonseRs.contains("SYNC_ACTIVE:Thiết bị của bạn đã đăng ký nhưng chưa kích hoạt")){
                                            Toast.makeText(getContext(),"Thiết bị đã đăng ký nhưng chưa kích hoạt...",Toast.LENGTH_SHORT).show();
                                        }else if(ResPonseRs.contains("SYNC_EMP:Thiết bị chưa chỉ định cho nhân viên quản quản lý")){
                                            Toast.makeText(getContext(),"Thiết bị đã đăng ký nhưng chỉ định nhân viên quản lý...",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } catch (Exception ex) {
                                    Toast.makeText(getContext(),"Không thể đồng bộ "+oCus.getCustomerName(),Toast.LENGTH_LONG).show();
                                    //mToast.setGravity(Gravity.CENTER,0,0);
                                    //mToast.show();
                                    //publishProgress("Không thể đồng bộ: "+oCus.getCustomerName() + ".");
                                }
                            }

                            @Override
                            public void onHttpFailer(Exception e) {
                                //publishProgress("Không thể đồng bộ "+oCus.getCustomerName() + ".");
                                Toast mToast=Toast.makeText(getContext(),"Không thể đồng bộ "+oCus.getCustomerName(),Toast.LENGTH_LONG);
                                mToast.setGravity(Gravity.CENTER,0,0);
                                mToast.show();
                            }

                        }, mUrlPost, "POST_CUS", DataBody);

                        if(Build.VERSION.SDK_INT>=11) {
                            oSyncPost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }else{
                            oSyncPost.execute();
                        }

                        //Thread.sleep(800);
                        SystemClock.sleep(1500);
                    }
                }

            }catch (Exception ex){
                ex.printStackTrace();
                mException=ex;
            }
            return "POST_OK";
        }

        @Override
        protected void onProgressUpdate(String...values){
            super.onProgressUpdate(values);
            ((BaseActivity) getActivity()).setTextProgressDialog(values[0]);
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(mSyncCallBack!=null){
                if(mException==null){
                    mSyncCallBack.onSyncSuccess(result);
                    ((BaseActivity) getActivity()).dismissProgressDialog();
                }else{
                    mSyncCallBack.onSyncFailer(mException);
                    ((BaseActivity) getActivity()).dismissProgressDialog();
                }
            }

        }
    }


    private void onSyncConfirm(String mtblName){
        final String mUrlConFirm=AppSetting.getInstance().URL_SyncConfirm(AppUtils.getImeil(getContext()),AppUtils.getImeilsim(getContext()),mtblName);
        new SyncGet(new APINetCallBack() {
            @Override
            public void onHttpStart() {
                //((BaseActivity) getActivity()).showProgressDialog("Đang tải dữ liệu khách hàng.");
                //Log.d("URL_SYNC_CUSTOMER",mUrlGet);
            }
            @Override
            public void onHttpSuccess(String ResPonseRs) {
                if(ResPonseRs!=null && !ResPonseRs.isEmpty() && ResPonseRs.equalsIgnoreCase("SYNC_OK")){
                    Toast.makeText(getContext(),"Xác nhận tải thành công",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onHttpFailer(Exception e) {

            }
        }, mUrlConFirm, "CONFITM").execute();

    }


    //ASYNC BACKGROUDN DOWNLOAD CUSTOMER
    private class AsyncUpdateCustomer extends AsyncTask<DM_Customer[],String,String>{
        private SyncCallBack mSyncCallBack;
        private Exception mException;

        private AsyncUpdateCustomer(SyncCallBack mCallBack){
            this.mSyncCallBack=mCallBack;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            if(this.mSyncCallBack!=null) {
                this.mSyncCallBack.onSyncStart();
            }
        }
        @Override
        protected String doInBackground(DM_Customer[]...params){
            try{
                int iSize=params[0].length;
                int iSqno=1;
                for(DM_Customer oCus:params[0]){
                    if(!oCus.getCustomerid().isEmpty()){
                        mDB.addCustomer(oCus);
                        publishProgress("["+Integer.toString(iSqno)+"/"+Integer.toString(iSize)+"] "+  oCus.getCustomerName());
                    }
                    iSqno+=1;
                }
            }catch (Exception ex){
                mException=ex;
                ex.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(String...values){
            ((BaseActivity) getActivity()).setTextProgressDialog(values[0]);
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(mSyncCallBack!=null){
                if(mException==null){
                    mSyncCallBack.onSyncSuccess(result);
                    onSyncConfirm("DM_CUSTOMER");
                }else{
                    mSyncCallBack.onSyncFailer(mException);
                }
            }
        }

    }





    //CHECK FOR APPROVE
    Timer oTimer;
    TimerTaskCheckApprove mTimerCheckApprove;
    private void onStartTimerCheckApprove(){
        try {

            if (oTimer != null) {
                oTimer.cancel();
                oTimer = null;
            }
            Integer iMinus=2;
            oTimer = new Timer();
            mTimerCheckApprove = new TimerTaskCheckApprove();
            oTimer.schedule(mTimerCheckApprove, 1000, iMinus *60*1000);
            Toast.makeText(getContext(),"Đang kiểm tra kết quả chờ duyệt...",Toast.LENGTH_LONG).show();
        }catch (Exception ex){}
    }

    private void onStopTimerCheckApprove(){
        try {
            if (oTimer != null) {
                oTimer.cancel();
                oTimer = null;
            }
            if (mTimerCheckApprove != null) {
                mTimerCheckApprove.cancel();
                mTimerCheckApprove = null;
            }
        }catch (Exception ex){}
    }
    //Schedule Task Check Approve
    private class TimerTaskCheckApprove extends TimerTask {
                @Override
                public void run() {
                    try {
                        if(isEditAdd==true){return;}
                        if (APINet.isNetworkAvailable(getContext())) {
                            Integer iPendding=mDB.getSizeCustomerPenddingAprrove();
                            if(iPendding>0){
                                Toast.makeText(getContext(),"Đang kiểm tra thông tin duyệt..",Toast.LENGTH_SHORT).show();
                                onDownload();
                            }else{
                                onStopTimerCheckApprove();
                            }
                        }
                    }catch (Exception ex){}
                }
    }


    //ASYNC BACKGROUDN DOWNLOAD CUSTOMER
    private void onDownload(){

        final String mUrlGet=AppSetting.getInstance().URL_SyncDM(AppUtils.getImeil(getContext()),AppUtils.getImeilsim(getContext()),"DM_CUSTOMER",false);
        new SyncGet(new APINetCallBack() {
            @Override
            public void onHttpStart() { }
            @Override
            public void onHttpSuccess(String ResPonseRs) {
                if( ResPonseRs!=null || ResPonseRs.toString().trim()!="[]" || ResPonseRs.isEmpty()) {
                    if(ResPonseRs.contains("SYNC_REG: Thiết bị của bạn chưa được đăng ký")){
                        Toast.makeText(getContext(),"Thiết bị chưa đăng ký..",Toast.LENGTH_SHORT).show();
                        return;
                    }else if(ResPonseRs.contains("SYNC_ACTIVE: Thiết bị của bạn đã đăng ký nhưng chưa kích hoạt")){
                        Toast.makeText(getContext(),"Thiết bị đã đăng ký nhưng chưa kích hoạt...",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<Collection<DM_Customer>>() {}.getType();
                        Collection<DM_Customer> enums = gson.fromJson(ResPonseRs, type);
                        DM_Customer[] ArrCus = enums.toArray(new DM_Customer[enums.size()]);

                        //Async Add Customer
                        if (ArrCus!=null && ArrCus.length>0) {
                            new AsyncApproveUpdate(new SyncCallBack() {
                                @Override
                                public void onSyncStart() { }
                                @Override
                                public void onSyncSuccess(String ResPonseRs) {
                                    onLoadDataSource();
                                }

                                @Override
                                public void onSyncFailer(Exception e) { }
                            }).execute(ArrCus);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onHttpFailer(Exception e){}
        }, mUrlGet, "DWN_DMCUSTOMER").execute();
    }

    private class AsyncApproveUpdate extends AsyncTask<DM_Customer[],String,String> {
        private SyncCallBack mSyncCallBack;
        private Exception mException;

        private AsyncApproveUpdate(SyncCallBack mCallBack){
            this.mSyncCallBack=mCallBack;
        }

        @Override
        protected void onPreExecute(){
        }
        @Override
        protected String doInBackground(DM_Customer[]...params){
            try{
                for(DM_Customer oCus:params[0]){
                    if(!oCus.getCustomerid().isEmpty()){
                        mDB.addCustomer(oCus);
                    }
                }
            }catch (Exception ex){
                mException=ex;
                ex.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(String...values){ }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(mSyncCallBack!=null){
                if(mException==null){
                    mSyncCallBack.onSyncSuccess(result);
                }else{
                    mSyncCallBack.onSyncFailer(mException);
                }
            }
        }

    }



}
