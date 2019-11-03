package com.mimi.mimigroup.ui.order;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.mimi.mimigroup.model.SM_OrderTotalSales;
import com.mimi.mimigroup.ui.adapter.OrderTotalSaleAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.utils.AppUtils;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.internal.Util;


public class OrderTotalSalesActivity extends BaseActivity{
    @BindView(R.id.rvTotalSales)
    RecyclerView rvTotalSales;

    @BindView(R.id.tvTotalFdayH)
    CustomBoldTextView tvTotalFdayH;
    @BindView(R.id.tvTotalTdayH)
    CustomBoldTextView tvTotalTdayH;
    @BindView(R.id.tvTotalSaleMoneyH)
    CustomBoldTextView tvTotalSaleMoneyH;
    @BindView(R.id.tvTotalPointH)
    CustomBoldTextView tvTotalPointH;

    OrderTotalSaleAdapter adapter;
    private String mFDay;
    private String mTDay;

    private List<SM_OrderTotalSales> oLstTotalSale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_totalsales);
        //mDB=DBGimsHelper.getInstance(this);
        oLstTotalSale=new ArrayList<SM_OrderTotalSales>();

        adapter=new OrderTotalSaleAdapter();
        rvTotalSales.setLayoutManager(new LinearLayoutManager(OrderTotalSalesActivity.this));
        rvTotalSales.setAdapter(adapter);

        try {
            mFDay = getIntent().getStringExtra("mfday");
            mTDay=getIntent().getStringExtra("mtday");
        }catch (Exception ex){}

        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                   onRequestTotalSale();
                };
            },300);
    }

    private void onLoadTotalSales(){
        try{
            if (oLstTotalSale!=null)
                onSetTotalSaleInfo();
                adapter.setOrderTotalSaleList(oLstTotalSale);
            Toast.makeText(OrderTotalSalesActivity.this,"Có "+ Integer.toString(oLstTotalSale.size())+" được nạp..",Toast.LENGTH_SHORT).show();
        }catch (Exception ex){}
    }

    private void onSetTotalSaleInfo(){
        try{
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (oLstTotalSale!=null){
              if (oLstTotalSale.size()>0){
                  for(SM_OrderTotalSales oS:oLstTotalSale){
                      try {
                          tvTotalFdayH.setText(oS.getFDay());
                      }catch (Exception ex){ tvTotalFdayH.setText(""); }

                      try {
                          tvTotalTdayH.setText(oS.getTDay());
                      }catch (Exception ex){ tvTotalTdayH.setText(""); }

                      tvTotalSaleMoneyH.setText(AppUtils.getMoneyFormat(oS.getTotalMoney().toString(),"VND"));
                      tvTotalPointH.setText(AppUtils.getMoneyFormat(oS.getTotalPoint().toString(),"VND"));
                      break;
                  }
              }else{
                  tvTotalSaleMoneyH.setText("");
                  tvTotalPointH.setText("");
              }
            }
        }catch (Exception ex){}
    }

    @OnClick(R.id.ivBack)
    public void onBack(){
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    @OnClick(R.id.btnDownTotalSale)
    public void onDownloadTotalSale(){
        onRequestTotalSale();
    }

    private void onRequestTotalSale(){
        if(mFDay!="" || mTDay!=""){
            try{
                if (APINet.isNetworkAvailable(OrderTotalSalesActivity.this)==false){
                    Toast.makeText(OrderTotalSalesActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
                    return;
                }
                String Imei=AppUtils.getImeil(getApplicationContext());
                String ImeiSim=AppUtils.getImeilsim(getApplicationContext());
                final String mUrlGetDelivery=AppSetting.getInstance().URL_GetTotalSale(Imei,ImeiSim,"TOTAL",mFDay,mTDay);

                new SyncGet(new APINetCallBack() {
                    @Override
                    public void onHttpStart() {
                        showProgressDialog("Đang tải thông tin doanh số bán hàng.");
                    }
                    @Override
                    public void onHttpSuccess(String ResPonseRs) {
                        OrderTotalSalesActivity.this.dismissProgressDialog();
                        if( ResPonseRs==null || ResPonseRs.toString().trim()==null || ResPonseRs.isEmpty()) {
                            Toast.makeText(OrderTotalSalesActivity.this,"Vui lòng kiểm tra kết nối mạng..",Toast.LENGTH_SHORT).show();
                            OrderTotalSalesActivity.this.dismissProgressDialog();
                        }else if( ResPonseRs.equalsIgnoreCase("[]")){
                            OrderTotalSalesActivity.this.dismissProgressDialog();
                            Toast.makeText(OrderTotalSalesActivity.this, "Không có thông tin doanh số trong khoản thời gian này.", Toast.LENGTH_SHORT).show();
                            return;
                        }else if(ResPonseRs.contains("SYNC_REG: Thiết bị chưa được đăng ký")){
                            OrderTotalSalesActivity.this.dismissProgressDialog();
                            Toast.makeText(OrderTotalSalesActivity.this, "Thiết bị chưa đăng ký. Vui lòng đăng ký trước...", Toast.LENGTH_SHORT).show();
                            return;
                        }else if(ResPonseRs.contains("SYNC_ACTIVE: Thiết bị đã đăng ký nhưng chưa kích hoạt")) {
                            OrderTotalSalesActivity.this.dismissProgressDialog();
                            Toast.makeText(OrderTotalSalesActivity.this, "Thiết bị đã đăng ký nhưng chưa kích hoạt..", Toast.LENGTH_SHORT).show();
                            return;
                        }else if(ResPonseRs.contains("SYNC_DAY_EMPTY")){
                            OrderTotalSalesActivity.this.dismissProgressDialog();
                            Toast.makeText(OrderTotalSalesActivity.this, "Bạn chưa chọn thời gian cần xem..", Toast.LENGTH_SHORT).show();
                            return;
                        }else if(ResPonseRs.contains("SYNC_NOT_FOUND")){
                            OrderTotalSalesActivity.this.dismissProgressDialog();
                            Toast.makeText(OrderTotalSalesActivity.this, "Không tìm thấy thông tin theo yêu cầu...", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<Collection<SM_OrderTotalSales>>() {}.getType();
                                Collection<SM_OrderTotalSales> enums = gson.fromJson(ResPonseRs, type);
                                SM_OrderTotalSales[] ArrTotalSale = enums.toArray(new SM_OrderTotalSales[enums.size()]);
                                //Async Add DELIVERY
                                if ( ArrTotalSale!=null && ArrTotalSale.length>0) {
                                     oLstTotalSale=new ArrayList<SM_OrderTotalSales>();
                                      for (SM_OrderTotalSales oS:ArrTotalSale){
                                          oLstTotalSale.add(oS);
                                      }
                                      onLoadTotalSales();
                                }else{
                                    OrderTotalSalesActivity.this.dismissProgressDialog();
                                    Toast.makeText(OrderTotalSalesActivity.this,"Không đọc được dữ liệu tải về",Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                OrderTotalSalesActivity.this.dismissProgressDialog();
                                Toast.makeText(OrderTotalSalesActivity.this,"Không đọc được dữ liệu tải về"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onHttpFailer(Exception e) {
                        dismissProgressDialog();
                    }
                },mUrlGetDelivery,"TOTAL_SALE").execute();

            }catch (Exception ex){
                dismissProgressDialog();
            }
        }
    }


}
