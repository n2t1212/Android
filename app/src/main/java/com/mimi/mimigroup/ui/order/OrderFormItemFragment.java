package com.mimi.mimigroup.ui.order;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Toast;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.api.SyncFindCustomer;
import com.mimi.mimigroup.api.SyncFindCustomerCallback;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseFragment;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.DM_Customer_Distance;
import com.mimi.mimigroup.model.DM_Customer_Search;
import com.mimi.mimigroup.model.SM_Order;
import com.mimi.mimigroup.ui.adapter.SearchCustomerAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldEditText;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderFormItemFragment extends BaseFragment {
    @BindView(R.id.tvOrderCode)
    CustomBoldTextView tvOrderCode;
    @BindView(R.id.tvOrderDate)
    CustomBoldTextView tvOrderDate;


    @BindView(R.id.tvRequestDate)
    CustomBoldEditText tvRequestDate;
    @BindView(R.id.tvCustomerCode)
    CustomBoldTextView tvCustomerCode;
    @BindView(R.id.tvCustomerAddress)
    CustomBoldTextView tvCustomerAddress;
    @BindView(R.id.tvOriginMoney)
    CustomBoldTextView tvOriginMoney;
    @BindView(R.id.tvOrderStatus)
    CustomBoldTextView tvOrderStatus;
    @BindView(R.id.tvOrderNotes)
    CustomBoldEditText tvOrderNotes;

    @BindView(R.id.spCustomerOrder)
    AutoCompleteTextView spCustomerOrder;

    @BindView(R.id.cbxSample)
    CheckBox cbxSample;

    private DBGimsHelper mDB;
    private String mOrderID="";
    private SM_Order oOrder;
    List<DM_Customer_Search> lstCustomer;
    private DatePickerDialog dtPicker;
    private DM_Customer_Search oCustomerSel;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_order_form;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDB=DBGimsHelper.getInstance(getContext());
       //mOrderID=getArguments().getString("OrderID");
        OrderFormActivity oActivity=(OrderFormActivity) getActivity();
        oOrder=oActivity.getOrderActivity();
        lstCustomer=oActivity.getListCustomerSearch();
        new android.os.Handler().postDelayed(
          new Runnable() {
          public void run() {
              if(oOrder!=null && oOrder.getOrderID()!=""){
                  if(oOrder.getOrderCode()!=null){
                      tvOrderCode.setText(oOrder.getOrderCode());
                  }
                  if(oOrder.getOrderDate()!=null){
                      tvOrderDate.setText(oOrder.getOrderDate());
                  }
                  if(oOrder.getRequestDate()!=null){
                      tvRequestDate.setText(oOrder.getRequestDate());
                  }
                  if(oOrder.getRequestDate()!=null){
                      tvRequestDate.setText(oOrder.getRequestDate());
                  }
                  if(oOrder.getOriginMoney()!=null){
                      tvOriginMoney.setText(oOrder.getOriginMoney().toString());
                  }
                  if(oOrder.getOrderStatusDesc()!=null){
                      tvOrderStatus.setText(oOrder.getOrderStatusDesc());
                  }
                  if(oOrder.getOrderNotes()!=null){
                      tvOrderNotes.setText(oOrder.getOrderNotes());
                  }
                  if(oOrder.getCustomerName()!=null){
                      spCustomerOrder.setText(oOrder.getCustomerName());
                  }

                  if(oOrder.getCustomerCode()!=null){
                      tvCustomerCode.setText(oOrder.getCustomerCode());
                  }

                  if(oOrder.getCustomerAddress()!=null){
                      tvCustomerAddress.setText(oOrder.getCustomerAddress());
                  }

                  if(oOrder.getSample() != null && oOrder.getSample()){
                      cbxSample.setChecked(true);
                  }
                }
              initDropdownCustomer();
            }
         },300);
    }


    private void initDropdownCustomer(){
        try{
            ArrayList<DM_Customer_Search> oListCus=new ArrayList<DM_Customer_Search>();
            for(int i=0;i<lstCustomer.size();++i){
                oListCus.add(new DM_Customer_Search(lstCustomer.get(i).getCustomerid(),lstCustomer.get(i).getCustomerCode(),
                        lstCustomer.get(i).getCustomerName(),lstCustomer.get(i).getShortName(),lstCustomer.get(i).getProvinceName(),
                        lstCustomer.get(i).getLongititude(),lstCustomer.get(i).getLatitude()));
            }
            SearchCustomerAdapter adapter = new SearchCustomerAdapter(getContext(), R.layout.search_customer,oListCus);
            spCustomerOrder.setDropDownBackgroundResource(R.drawable.liner_dropdownlist);
            spCustomerOrder.setThreshold(1);
            spCustomerOrder.setAdapter(adapter);
            spCustomerOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int iPosition, long l) {
                    try {
                        oCustomerSel = (DM_Customer_Search) adapterView.getItemAtPosition(iPosition);
                        String mUnit=oCustomerSel.getCustomerid();
                        if(oCustomerSel!=null && oCustomerSel.getCustomerid()!=""){
                            oOrder.setCustomerID(oCustomerSel.getCustomerid());
                            if(oCustomerSel.getCustomerCode()!=null) {
                                oOrder.setCustomerCode(oCustomerSel.getCustomerCode());
                                tvCustomerCode.setText(oCustomerSel.getCustomerCode());
                            }else{
                                tvCustomerCode.setText("");
                            }
                            if(oCustomerSel.getCustomerName()!=null){
                                oOrder.setCustomerName(oCustomerSel.getCustomerName());
                            }
                            if(oCustomerSel.getProvinceName()!=null) {
                                oOrder.setCustomerAddress(oCustomerSel.getProvinceName());
                                tvCustomerAddress.setText(oCustomerSel.getProvinceName());
                            }else{
                                tvCustomerAddress.setText("");
                            }
                        }

                    }catch (Exception ex){}
                }
            });

        }catch (Exception ex){}
    }


    @OnClick(R.id.tvRequestDate)
    public void onRequestDate(){
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        // date picker dialog
        dtPicker = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cldr.set(year,monthOfYear, dayOfMonth);
                        String dateString = sdf.format(cldr.getTime());
                        tvRequestDate.setText(dateString);
                        //tvRequestDate.setText( Integer.toString(year)+"-"+Integer.toString(monthOfYear + 1)+ "-" + Integer.toString(dayOfMonth ));
                    }
                }, year, month, day);

        dtPicker.getDatePicker().setCalendarViewShown(false);
        dtPicker.getDatePicker().setSpinnersShown(true);
        dtPicker.show();
    }


     public SM_Order getSMOrder(){
         try{
             oOrder.setOrderCode(tvOrderCode.getText().toString());
             oOrder.setOrderDate(tvOrderDate.getText().toString());
             oOrder.setRequestDate(tvRequestDate.getText().toString());

             if(tvOriginMoney.getText().toString().isEmpty()){
                 oOrder.setOriginMoney(0.0);
             }else{
                 oOrder.setOriginMoney(Double.valueOf(tvOriginMoney.getText().toString()));
             }
             if(tvOrderStatus.getText().toString().isEmpty()){
                 oOrder.setOrderStatusDesc("");
             }else{
                 oOrder.setOrderStatusDesc(tvOrderStatus.getText().toString());
             }
             if(tvOrderNotes.getText().toString().isEmpty()){
                 oOrder.setOrderNotes("");
             }else{
                 oOrder.setOrderNotes(tvOrderNotes.getText().toString());
             }
             if(tvCustomerCode.getText().toString().isEmpty()){
                 oOrder.setCustomerCode("");
             }else{
                 oOrder.setCustomerCode(tvCustomerCode.getText().toString());
             }

             if(tvCustomerAddress.getText().toString().isEmpty()){
                 oOrder.setCustomerAddress("");
             }else{
                 oOrder.setCustomerAddress(tvCustomerAddress.getText().toString());
             }

             if(cbxSample.isChecked()){
                 oOrder.setSample(true);
             } else {
                 oOrder.setSample(false);
             }
             //oOrder.setCustomerID(0);

         }catch (Exception ex){}
        return oOrder;
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
                                spCustomerOrder.setTag(lstSel);
                               // spCustomer.setOnItemSelectedListener();
                                List<String> customers = new ArrayList<>();
                                for (DM_Customer_Distance oCus : lstSel) {
                                    customers.add(oCus.getCustomerName());
                                }
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, customers);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spCustomerOrder.setAdapter(dataAdapter);
                            }

                            if(lstSel.isEmpty() || lstSel.size()<=0){
                                Toast.makeText(getContext(),"Không tìm thấy khách hàng trong phạm vi tọa độ này.",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onSyncFailer(Exception e) {
                            // dismissProgressDialog();
                            Toast.makeText(getContext(),"Không thể tìm khách hàng theo tọa độ."+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }, lstCus, mLongitude, mLatitude, mScope, mReturnSize).execute();
                }
                //lstCus=onFindCustomer(mLongitude,mLatitude,Float.valueOf(100));

            }
        }catch (Exception e){
            //dismissProgressDialog();
            Toast.makeText(getContext(),"Không thể tìm khách hàng."+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


}
