package com.mimi.mimigroup.ui.order;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mimi.mimigroup.R;
import com.mimi.mimigroup.api.APINet;
import com.mimi.mimigroup.api.APINetCallBack;
import com.mimi.mimigroup.api.SyncCallBack;
import com.mimi.mimigroup.api.SyncGet;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.SM_OrderDelivery;
import com.mimi.mimigroup.model.SM_OrderDeliveryDetail;
import com.mimi.mimigroup.model.SM_OrderDelivery_Sync;
import com.mimi.mimigroup.ui.adapter.DeliveryDetailAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.utils.AppUtils;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;


public class OrderDeliveryActivity extends BaseActivity{
    @BindView(R.id.rvDeliveryOrder)
    RecyclerView rvDeliveryOrder;

    @BindView(R.id.tvDeliOrderCode)
    CustomBoldTextView tvDeliOrderCode;
    @BindView(R.id.tvDeliRequestDate)
    CustomBoldTextView tvDeliRequestDate;
    @BindView(R.id.tvDeliCustomer)
    CustomBoldTextView tvDeliCustomer;

    @BindView(R.id.spTransportCode)
    Spinner spTransportCode;
    @BindView(R.id.tvDeliveryDate)
    CustomBoldTextView tvDeliveryDate;
    @BindView(R.id.tvDeliveryNo)
    CustomBoldTextView tvDeliveryNo;
    @BindView(R.id.tvNumberPlate)
    CustomBoldTextView tvNumberPlate;
    @BindView(R.id.tvDeliveryStaff)
    CustomBoldTextView tvDeliveryStaff;
    @BindView(R.id.tvHandleStaff)
    CustomBoldTextView tvHandleStaff;
    @BindView(R.id.tvDeliveryNotes)
    CustomBoldTextView tvDeliveryNotes;
    @BindView(R.id.tvDeliveryTotalMoney)
    CustomBoldTextView tvDeliveryTotalMoney;

    @BindView(R.id.btnDeliveryMore)
    CustomBoldTextView btnDeliveryMore;
    @BindView(R.id.Layout_DeliverInfo)
    LinearLayout Layout_DeliverInfo;

    DeliveryDetailAdapter adapter;
    private DBGimsHelper mDB;
    private String mOrderID;
    private String mOrderCode;
    private String mRequestDate;
    private String mCustomerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_delivery);
        mDB=DBGimsHelper.getInstance(this);
        adapter=new DeliveryDetailAdapter();
        rvDeliveryOrder.setLayoutManager(new LinearLayoutManager(OrderDeliveryActivity.this));
        rvDeliveryOrder.setAdapter(adapter);

        try {
            mOrderID = getIntent().getStringExtra("orderid");
            mOrderCode=getIntent().getStringExtra("ordercode");
            mRequestDate=getIntent().getStringExtra("requestdate");
            mCustomerName=getIntent().getStringExtra("customername");
            if(mOrderCode!=null){
                tvDeliOrderCode.setText(mOrderCode);
            }
            if(mRequestDate!=null){
                tvDeliRequestDate.setText(mRequestDate);
            }
            if(mCustomerName!=null){
                tvDeliCustomer.setText(mCustomerName);
            }
        }catch (Exception ex){}


        onLoadDelivery();
        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                   onRequestDelivery();
                };
            },300);
    }

    private void onLoadDelivery(){
        try{
            List<SM_OrderDelivery> lstDelivery=mDB.getAllDelivery(mOrderID);
            if (lstDelivery!=null && !lstDelivery.isEmpty()) {
                spTransportCode.setTag(lstDelivery);
                spTransportCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        List<SM_OrderDelivery> lstDeli = (List<SM_OrderDelivery>) spTransportCode.getTag();
                        SM_OrderDelivery oDeliSel=lstDeli.get(position);
                        if(oDeliSel!=null) {
                            onSetDeliveryInfo(oDeliSel);
                            onLoadDeliveryDetailDataSource(oDeliSel.getDeliveryOrderID());
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
                List<String> lstTransportCode = new ArrayList<>();
                for (SM_OrderDelivery oDeli : lstDelivery) {
                    lstTransportCode.add(oDeli.getTransportCode());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(OrderDeliveryActivity.this, android.R.layout.simple_spinner_item, lstTransportCode);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTransportCode.setAdapter(dataAdapter);
            }

            Toast.makeText(OrderDeliveryActivity.this,"Có "+ Integer.toString(lstDelivery.size())+" được nạp..",Toast.LENGTH_SHORT).show();
        }catch (Exception ex){}
    }

    private void onLoadDeliveryDetailDataSource(String mDeliveryID){
        try{
            List<SM_OrderDeliveryDetail> lstDeliveryDetail=mDB.getAllDeliveryDetail(mDeliveryID);
            adapter.setDeliveryOrderList(lstDeliveryDetail);
            Toast.makeText(OrderDeliveryActivity.this,"Có "+ Integer.toString(lstDeliveryDetail.size())+" được nạp..",Toast.LENGTH_SHORT).show();
        }catch (Exception ex){}
    }

    private void onSetDeliveryInfo(SM_OrderDelivery oDeli){
        try{
            if(oDeli!=null){
                if(oDeli.getDeliveryDate()!=null && !oDeli.getDeliveryDate().toString().isEmpty()){
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        tvDeliveryDate.setText(sdf.format(oDeli.getDeliveryDate()));
                    }catch (Exception ex){
                        tvDeliveryDate.setText(oDeli.getDeliveryDate());
                    }
                }
                if(oDeli.getDeliveryNum()!=null){
                    tvDeliveryNo.setText(oDeli.getDeliveryNum().toString());
                }
                if(oDeli.getNumberPlate()!=null){
                    tvNumberPlate.setText(oDeli.getNumberPlate());
                }
                if(oDeli.getDeliveryStaff()!=null){
                    tvDeliveryStaff.setText(oDeli.getDeliveryStaff());
                }
                if(oDeli.getHandlingStaff()!=null){
                   tvHandleStaff.setText(oDeli.getHandlingStaff());
                }
                if(oDeli.getTotalMoney()!=null){
                    tvDeliveryTotalMoney.setText(AppUtils.getMoneyFormat(oDeli.getTotalMoney().toString(),"VND"));
                }
                if(oDeli.getDeliveryDesc()!=null){
                    tvDeliveryNotes.setText(oDeli.getDeliveryDesc());
                }
            }
        }catch (Exception ex){}
    }

    @OnClick(R.id.ivBack)
    public void onBack(){
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @OnClick(R.id.btnDeliveryMore)
    public void onDeliveryMore(){
         if(Layout_DeliverInfo.getVisibility()==View.GONE){
             Layout_DeliverInfo.setVisibility(View.VISIBLE);
             btnDeliveryMore.setText("...^");
         }else{
             Layout_DeliverInfo.setVisibility(View.GONE);
             btnDeliveryMore.setText("...v");
         }
    }

    @OnClick(R.id.btnDownDelivery)
    public void onDownloadDelivery(){
        if(Layout_DeliverInfo.getVisibility()==View.VISIBLE){
            Layout_DeliverInfo.setVisibility(View.GONE);
            btnDeliveryMore.setText("...v");
        }
        onRequestDelivery();
    }


    private void onRequestDelivery(){
        if(mOrderID!="" || mOrderCode!=""){
            try{
                if (APINet.isNetworkAvailable(OrderDeliveryActivity.this)==false){
                    Toast.makeText(OrderDeliveryActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
                    return;
                }

                String Imei=AppUtils.getImeil(getApplicationContext());
                String ImeiSim=AppUtils.getImeilsim(getApplicationContext());
                final String mUrlGetDelivery=AppSetting.getInstance().URL_GetDelivery(Imei,ImeiSim,"DELIVERY",mOrderID,mOrderCode);

                new SyncGet(new APINetCallBack() {
                    @Override
                    public void onHttpStart() {
                        showProgressDialog("Đang tải thông tin xử lý đơn hàng.");
                    }
                    @Override
                    public void onHttpSuccess(String ResPonseRs) {
                        OrderDeliveryActivity.this.dismissProgressDialog();
                        if( ResPonseRs==null || ResPonseRs.toString().trim()==null || ResPonseRs.isEmpty()) {
                            Toast.makeText(OrderDeliveryActivity.this,"Vui lòng kiểm tra kết nối mạng..",Toast.LENGTH_SHORT).show();
                            OrderDeliveryActivity.this.dismissProgressDialog();
                        }else if( ResPonseRs.equalsIgnoreCase("[]")){
                            OrderDeliveryActivity.this.dismissProgressDialog();
                            Toast.makeText(OrderDeliveryActivity.this, "Không có thông tin vận chuyển mới về đơn hàng..", Toast.LENGTH_SHORT).show();
                            return;
                        }else if(ResPonseRs.contains("SYNC_REG: Thiết bị chưa được đăng ký")){
                            OrderDeliveryActivity.this.dismissProgressDialog();
                            Toast.makeText(OrderDeliveryActivity.this, "Thiết bị chưa đăng ký. Vui lòng đăng ký trước...", Toast.LENGTH_SHORT).show();
                            return;
                        }else if(ResPonseRs.contains("SYNC_ACTIVE: Thiết bị đã đăng ký nhưng chưa kích hoạt")) {
                            OrderDeliveryActivity.this.dismissProgressDialog();
                            Toast.makeText(OrderDeliveryActivity.this, "Thiết bị đã đăng ký nhưng chưa kích hoạt..", Toast.LENGTH_SHORT).show();
                            return;
                        }else if(ResPonseRs.contains("SYNC_ORDER_EMPTY")){
                            OrderDeliveryActivity.this.dismissProgressDialog();
                            Toast.makeText(OrderDeliveryActivity.this, "Số đơn hàng yêu cầu không có thật (Empty)..", Toast.LENGTH_SHORT).show();
                            return;
                        }else if(ResPonseRs.contains("SYNC_NOT_FOUND")){
                            OrderDeliveryActivity.this.dismissProgressDialog();
                            Toast.makeText(OrderDeliveryActivity.this, "Không tìm thấy thông tin vận chuyển của đơn hàng này...", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<Collection<SM_OrderDelivery_Sync>>() {}.getType();
                                Collection<SM_OrderDelivery> enums = gson.fromJson(ResPonseRs, type);
                                SM_OrderDelivery_Sync[] ArrDelivery = enums.toArray(new SM_OrderDelivery_Sync[enums.size()]);

                                //Async Add DELIVERY
                                if ( ArrDelivery!=null && ArrDelivery.length>0) {
                                    new OrderDeliveryActivity.AsyncUpdate(new SyncCallBack() {
                                        @Override
                                        public void onSyncStart() {
                                            Toast.makeText(OrderDeliveryActivity.this, "Đang cập nhật thông tin vận đơn", Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onSyncSuccess(String ResPonseRs) {
                                            Toast.makeText(OrderDeliveryActivity.this, "Tải vận đơn thành công.", Toast.LENGTH_SHORT).show();
                                            OrderDeliveryActivity.this.dismissProgressDialog();
                                            onLoadDelivery();
                                        }
                                        @Override
                                        public void onSyncFailer(Exception e) {
                                            Toast.makeText(OrderDeliveryActivity.this,"Lỗi cập nhật:"+e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                                            OrderDeliveryActivity.this.dismissProgressDialog();
                                        }
                                    }).execute(ArrDelivery);
                                }else{
                                    OrderDeliveryActivity.this.dismissProgressDialog();
                                    Toast.makeText(OrderDeliveryActivity.this,"Không đọc được dữ liệu tải về",Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                OrderDeliveryActivity.this.dismissProgressDialog();
                                Toast.makeText(OrderDeliveryActivity.this,"Không đọc được dữ liệu tải về"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onHttpFailer(Exception e) {
                        dismissProgressDialog();
                    }
                },mUrlGetDelivery,"DELIVER_HIS").execute();

            }catch (Exception ex){
                dismissProgressDialog();
            }
        }
    }

    private void onSyncConfirm(String sisType,String sOrderID){
        final String mUrlConFirm=AppSetting.getInstance().URL_SyncConfirmOrder(AppUtils.getImeil(OrderDeliveryActivity.this),AppUtils.getImeilsim(OrderDeliveryActivity.this),sisType,sOrderID);
        new SyncGet(new APINetCallBack() {
            @Override
            public void onHttpStart() {}
            @Override
            public void onHttpSuccess(String ResPonseRs) {
                if(ResPonseRs!=null && !ResPonseRs.isEmpty() && ResPonseRs.equalsIgnoreCase("SYNC_OK")){
                    Toast.makeText(OrderDeliveryActivity.this,"Xác nhận đồng bộ thành công",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onHttpFailer(Exception e) {}
        }, mUrlConFirm, "CONFITM").execute();

    }

    //ASYNC BACKGROUDN DOWNLOAD DELIVERY
    private class AsyncUpdate extends AsyncTask<SM_OrderDelivery_Sync[],String,String> {
        private SyncCallBack mSyncCallBack;
        private Exception mException;

        private AsyncUpdate(SyncCallBack mCallBack) {
            this.mSyncCallBack=mCallBack;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(this.mSyncCallBack!=null) {
                this.mSyncCallBack.onSyncStart();
            }
        }
        @Override
        protected String doInBackground(SM_OrderDelivery_Sync[]... params) {
            try {
                int iSize = params[0].length;
                int iSqno = 1;
                publishProgress("[Đang xử lý dữ liệu vận chuyển..]");

                List<SM_OrderDelivery>lstDeli=new ArrayList<SM_OrderDelivery>();
                for (SM_OrderDelivery_Sync oItem : params[0]) {
                    if (!oItem.getTransportCode().isEmpty()) {
                          SM_OrderDelivery oDeli=new SM_OrderDelivery();
                          oDeli.setDeliveryOrderID(oItem.getDeliveryOrderID());
                          oDeli.setOrderID(oItem.getOrderID());
                          oDeli.setTransportCode(oItem.getTransportCode());
                          oDeli.setNumberPlate(oItem.getNumberPlate());
                          oDeli.setCarType(oItem.getCarType());
                          oDeli.setDeliveryStaff(oItem.getDeliveryStaff());
                          oDeli.setDeliveryNum(oItem.getDeliveryNum());
                          oDeli.setDeliveryDate(oItem.getDeliveryDate());
                          oDeli.setHandlingStaff(oItem.getHandlingStaff());
                          oDeli.setTotalMoney(oItem.getTotalMoney());
                          oDeli.setDeliveryDesc(oItem.getDeliveryDesc());

                          if(!lstDeli.contains(oDeli)){
                              lstDeli.add(oDeli);
                          }
                        //mDB.addDelivery(oItem);
                        publishProgress("[" + Integer.toString(iSqno) + "/" + Integer.toString(iSize) + "] " + oItem.getTransportCode());
                    }
                    iSqno += 1;
                }

                iSize = params[0].length;
                iSqno = 1;
                publishProgress("[Đang cập nhật dữ liệu vận chuyển..]");
                for(SM_OrderDelivery oDeli:lstDeli){
                    mDB.delDelivery(oDeli.getDeliveryOrderID());
                    mDB.addDelivery(oDeli);
                    if(oDeli.getDeliveryOrderID()!=null && !oDeli.getDeliveryOrderID().isEmpty()){
                        //mDB.delAllDeliveryDetail(oDeli.getDeliveryOrderID());
                        for (SM_OrderDelivery_Sync oItem : params[0]) {
                            if(oDeli.getDeliveryOrderID().equalsIgnoreCase(oItem.getDeliveryOrderID())){
                              SM_OrderDeliveryDetail oDeliDT=new SM_OrderDeliveryDetail();
                              oDeliDT.setDeliveryOrderDetailID(oItem.getDeliveryOrderDetailID());
                              oDeliDT.setDeliveryOrderID(oDeli.getDeliveryOrderID());
                              oDeliDT.setProductCode(oItem.getProductCode());
                              oDeliDT.setProductName(oItem.getProductName());
                              oDeliDT.setUnit(oItem.getUnit());
                              oDeliDT.setSpec(oItem.getSpec());
                              oDeliDT.setAmount(oItem.getAmount());
                              oDeliDT.setPrice(oItem.getPrice());
                              oDeliDT.setOriginMoney(oItem.getOriginMoney());

                              mDB.addDeliveryDetail(oDeliDT);
                            }
                        }

                    }
                }

            } catch (Exception ex) {
                mException = ex;
                ex.printStackTrace();
                publishProgress("ERR");
            }
            return "";
        }
        @Override
        protected void onProgressUpdate(String... values) {
            if(values[0].toString().equalsIgnoreCase("ERR")){
                OrderDeliveryActivity.this.dismissProgressDialog();
            }else {
                OrderDeliveryActivity.this.setTextProgressDialog(values[0]);
            }
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (mSyncCallBack != null) {
                if (mException == null) {
                    mSyncCallBack.onSyncSuccess(result);
                    onSyncConfirm("Delivery",mOrderID);
                } else {
                    mSyncCallBack.onSyncFailer(mException);
                }
            }
        }
    }




}
