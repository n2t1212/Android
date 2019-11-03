package com.mimi.mimigroup.ui.history;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mimi.mimigroup.R;
import com.mimi.mimigroup.api.APINet;
import com.mimi.mimigroup.api.APINetCallBack;
import com.mimi.mimigroup.api.SyncGet;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.QR_QRSCAN_HIS;
import com.mimi.mimigroup.ui.adapter.HistoryServerAdapter;
import com.mimi.mimigroup.utils.AppUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class HistoryServerActivity extends BaseActivity{
    @BindView(R.id.rvHistoryServer)
    RecyclerView rvHistoryServer;

    HistoryServerAdapter adapter;
    private DBGimsHelper mDB;
    private String mQRScanid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_server);
        mDB=DBGimsHelper.getInstance(this);
        adapter=new HistoryServerAdapter();
        rvHistoryServer.setLayoutManager(new LinearLayoutManager(HistoryServerActivity.this));
        rvHistoryServer.setAdapter(adapter);

        mQRScanid = getIntent().getStringExtra("qrid");
        onRequestHistoryFromServer(mQRScanid);

        /*
        Thread mCallBack=new Thread(){
            public void run(){
                try {
                    sleep(1000);
                } catch (Exception e) {}
                finally{
                   onRequestHistoryFromServer(mQRScanid);
                }
            }
        };
        mCallBack.start();
        */
    }

    @OnClick(R.id.ivBack)
    public void onBack(){
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void onRequestHistoryFromServer(String mQRScanid){
        if(mQRScanid!=""){
            try{
                if (APINet.isNetworkAvailable(HistoryServerActivity.this)==false){
                    Toast.makeText(HistoryServerActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
                    return;
                }

                String Imei=AppUtils.getImeil(getApplicationContext());
                String ImeiSim=AppUtils.getImeilsim(getApplicationContext());
                final String mUrlGetQRHIS=AppSetting.getInstance().URL_GetQR(Imei,ImeiSim,mQRScanid,false,2);

                new SyncGet(new APINetCallBack() {
                    @Override
                    public void onHttpStart() {
                        showProgressDialog("Đang tải lịch sử quét.");
                    }
                    @Override
                    public void onHttpSuccess(String ResPonseRs) {
                        dismissProgressDialog();
                        //Log.d("RESPON_GET_HIS",ResPonseRs);

                        try {
                            if (ResPonseRs!=null && !ResPonseRs.isEmpty()) {
                                if(ResPonseRs.contains("[]") || ResPonseRs.equalsIgnoreCase("[]")){
                                    Toast.makeText(HistoryServerActivity.this, "\"Mã này chưa có lịch sử quét", Toast.LENGTH_SHORT).show();
                                    return;
                                }else if(ResPonseRs.contains("SYNC_REG: Thiết bị của bạn chưa được đăng ký")){
                                    Toast.makeText(HistoryServerActivity.this, "Thiết bị của bạn chưa đăng ký. Vui lòng đăng ký trước...", Toast.LENGTH_SHORT).show();
                                    return;
                                }else if(ResPonseRs.contains("SYNC_ACTIVE: Thiết bị của bạn đã đăng ký nhưng chưa kích hoạt")) {
                                    Toast.makeText(HistoryServerActivity.this, "Thiết bị của bạn đã đăng ký nhưng chưa kích hoạt từ server...", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                Gson gson = new Gson();
                                Type type = new TypeToken<Collection<QR_QRSCAN_HIS>>() {}.getType();
                                Collection<QR_QRSCAN_HIS> enums = gson.fromJson(ResPonseRs, type);
                                QR_QRSCAN_HIS[] ArrQrHis = enums.toArray(new QR_QRSCAN_HIS[enums.size()]);
                               // Log.d("QRR_QR_HIS", Integer.toString(ArrQrHis.length));
                                if(ArrQrHis!=null) {
                                    List<QR_QRSCAN_HIS> lstQr=new ArrayList<QR_QRSCAN_HIS>();
                                    for(QR_QRSCAN_HIS oQR:ArrQrHis){
                                        //Log.d("QR_LOCATION",oQR.getLocationAddress());
                                        lstQr.add(oQR);
                                    }
                                    adapter.setHistoryServerList(lstQr);
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
                //Log.d("GET_QR_SCAN_HS_ERR",ex.getMessage().toString());
                dismissProgressDialog();
            }
        }
    }




}
