package com.mimi.mimigroup.api;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.DM_Customer;
import com.mimi.mimigroup.utils.AppUtils;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

public class TimerCustomerCheckApprove{

    Context ctx;
    private DBGimsHelper mDB;
    Timer oTimer;
    TimerTaskCheckApprove mTimerCheckApprove;
    Integer iRepeatSecond=5;

    public TimerCustomerCheckApprove(Context mc){
        ctx=mc;
    }

    public void onStartTimerCheckApprove(){
        try {
            if (oTimer != null) {
                oTimer.cancel();
                oTimer = null;
            }
            oTimer = new Timer();
            mTimerCheckApprove = new TimerTaskCheckApprove();
            oTimer.schedule(mTimerCheckApprove, 1000,   iRepeatSecond* 1000);
            Toast.makeText(ctx,"Trình kiểm tra khách hàng chờ duyệt đã bật...",Toast.LENGTH_LONG).show();
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

    private class TimerTaskCheckApprove extends TimerTask {
        @Override
        public void run() {
                    try {
                        if (APINet.isNetworkAvailable(ctx)) {
                            Integer iPendding=mDB.getSizeCustomerPenddingAprrove();
                            if(iPendding>0){
                                Toast.makeText(ctx,"Đang kiểm tra thông tin duyệt..",Toast.LENGTH_SHORT).show();
                                onDownload();
                            }else{
                                onStopTimerCheckApprove();
                            }
                        }
                    }catch (Exception ex){}

        }
    }

    private void onDownload(){
        if (APINet.isNetworkAvailable(ctx)==false){
            Toast.makeText(ctx,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
            return;
        }

        final String mUrlGet=AppSetting.getInstance().URL_SyncDM(AppUtils.getImeil(ctx),AppUtils.getImeilsim(ctx),"DM_CUSTOMER",false);
        new SyncGet(new APINetCallBack() {
            @Override
            public void onHttpStart() { }
            @Override
            public void onHttpSuccess(String ResPonseRs) {
                if( ResPonseRs!=null || ResPonseRs.toString().trim()!="[]" || ResPonseRs.isEmpty()) {
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
                                public void onSyncSuccess(String ResPonseRs) { }

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

    //ASYNC BACKGROUDN DOWNLOAD CUSTOMER
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
