package com.mimi.mimigroup.ui.services;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mimi.mimigroup.R;
import com.mimi.mimigroup.api.APINet;
import com.mimi.mimigroup.api.APINetCallBack;
import com.mimi.mimigroup.api.SyncCallBack;
import com.mimi.mimigroup.api.SyncGet;

import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.SM_OrderStatus;
import com.mimi.mimigroup.ui.main.MainActivity;
import com.mimi.mimigroup.utils.AppUtils;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class mimiService extends Service {
    private String LOG_TAG = "SMService";
    private static int UPDATE_INTERVAL = 10*1000; //5 sec

    private Handler mHandler = new Handler();
    private Timer mTimer = null;

    private Context mContext;
    private DBGimsHelper mDB;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private String mToDay ="";
    private NotificationManager mimiNotificationManager;

    public mimiService() {}

    @Override
    public void onCreate() {
        super.onCreate();
        mToDay= sdf.format(new Date());
        Log.i(LOG_TAG, "Đang tạo dịch vụ..");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        mDB=DBGimsHelper.getInstance(this);

        try{
            mimiNotificationManager= (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            onCreateNotificationChannel();
        }catch (Exception ex){}

        if(mTimer != null) {
            mTimer.cancel();
        } else {
           mTimer= new Timer();
        }
        synchronized (mTimer) {
            mTimer.scheduleAtFixedRate(new doTimerTask(), 1000, UPDATE_INTERVAL);
        }
        Log.i(LOG_TAG, "Start mimi Services");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
       return  null;
    }

    class doTimerTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    //Intent broadcastIntent = new Intent();
                    //broadcastIntent.setAction("ORDER_STATUS");
                    //broadcastIntent.putExtra("Data", myString);
                    //sendBroadcast(broadcastIntent);
                    if (APINet.isNetworkAvailable(getApplicationContext())){
                        boolean isSync=mDB.getCheckOrderStatus(mToDay);
                        if(isSync){
                            //Toast.makeText(getApplicationContext(),"Sync",Toast.LENGTH_SHORT).show();
                            onRequestStatus();

                        }
                    }

                }
            });
        };


        private void onRequestStatus(){
                try{
                    if (APINet.isNetworkAvailable(getApplicationContext())==false){
                        return;
                    }
                    String Imei=AppUtils.getImeil(getApplicationContext());
                    String ImeiSim=AppUtils.getImeilsim(getApplicationContext());
                    final String mUrlGetDelivery=AppSetting.getInstance().URL_GetStatus(Imei,ImeiSim,"ORDER_STATUS");

                    new SyncGet(new APINetCallBack() {
                        @Override
                        public void onHttpStart() {

                        }
                        @Override
                        public void onHttpSuccess(String ResPonseRs) {

                            if( ResPonseRs!=null || ResPonseRs.toString().trim()!=null || ResPonseRs.isEmpty()) {
                                try {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<Collection<SM_OrderStatus>>() {}.getType();
                                    Collection<SM_OrderStatus> enums = gson.fromJson(ResPonseRs, type);
                                    final SM_OrderStatus[] ArrStatus = enums.toArray(new SM_OrderStatus[enums.size()]);

                                    //Async Add DELIVERY
                                    if ( ArrStatus!=null && ArrStatus.length>0) {
                                        new AsyncUpdate(new SyncCallBack() {
                                            @Override
                                            public void onSyncStart() {}
                                            @Override
                                            public void onSyncSuccess(String ResPonseRs) {
                                               //Show Notify
                                                if(ArrStatus.length>0){
                                                   shwNotify("GTCGroup","Đơn hàng của bạn đã có thông tin xử lý. Vui lòng kiểm tra...");
                                                }
                                            }
                                            @Override
                                            public void onSyncFailer(Exception e) {

                                            }
                                        }).execute(ArrStatus);
                                    }
                                } catch (Exception e) { e.printStackTrace();}
                            }
                        }

                        @Override
                        public void onHttpFailer(Exception e) { }
                    },mUrlGetDelivery,"DELIVER_HIS").execute();

                }catch (Exception ex){ }
            }
    }


    private String NOTIFY_CHANNEL_ID="MIMI";
    private void onCreateNotificationChannel() {
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "MIMIGroup";
                String description = "MIMIGROUP";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(NOTIFY_CHANNEL_ID, name, importance);
                channel.setDescription(description);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }catch (Exception ex){}

    }
    private void shwNotify(String mTitle,String mContent){
      try {
          //Toast.makeText(getApplicationContext(),mContent,Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(this, MainActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
          PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

          NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, NOTIFY_CHANNEL_ID)
                  .setSmallIcon(R.drawable.app_logo)
                  .setContentTitle(mTitle)
                  .setContentText(mContent)
                  .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                  .setContentIntent(pendingIntent)
                  .setVisibility(Notification.VISIBILITY_PUBLIC)
                  .setAutoCancel(true);

          mimiNotificationManager.notify(1, mBuilder.build());
        }catch (Exception ex){}
    }


    private void onSyncConfirm(String sisType,String sOrderID){
        final String mUrlConFirm=AppSetting.getInstance().URL_SyncConfirmOrder(AppUtils.getImeil(getApplicationContext()),AppUtils.getImeilsim(getApplicationContext()),sisType,sOrderID);
        new SyncGet(new APINetCallBack() {
            @Override
            public void onHttpStart() {}
            @Override
            public void onHttpSuccess(String ResPonseRs) {
                //if(ResPonseRs!=null && !ResPonseRs.isEmpty() && ResPonseRs.equalsIgnoreCase("SYNC_OK")){
                  //  Toast.makeText(getApplicationContext(),"Xác nhận đồng bộ thành công",Toast.LENGTH_SHORT).show();
                //}
            }
            @Override
            public void onHttpFailer(Exception e) {}
        }, mUrlConFirm, "CONFITM").execute();

    }

    private class AsyncUpdate extends AsyncTask<SM_OrderStatus[],String,String> {
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
        protected String doInBackground(SM_OrderStatus[]... params) {
            try {
                int iSize = params[0].length;
                int iSqno = 1;
                String mNotify="";
                publishProgress("[Đang cập nhật trang thái đơn hàng..]");
                for (SM_OrderStatus oSTT:params[0]){
                    try {
                        if (oSTT.getOrderCode() != null && !oSTT.getOrderCode().isEmpty()) {
                            mDB.editSMOrderStatus2(oSTT);
                            publishProgress("Đang cập nhật trang thái đơn hàng " + oSTT.getOrderCode());
                        }
                        if (oSTT != null && !oSTT.getOrderStatusDesc().isEmpty()) {
                            mNotify += oSTT.getOrderStatusDesc() + ", ";
                        }
                    }catch (Exception ex){}
                }

                return mNotify;
            } catch (Exception ex) {
                mException = ex;
                ex.printStackTrace();
                publishProgress("ERR");
            }
            return "";
        }
        @Override
        protected void onProgressUpdate(String... values) { }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (mSyncCallBack != null) {
                if (mException == null) {
                    mSyncCallBack.onSyncSuccess(result);
                    if(!result.isEmpty()) {
                        shwNotify("THÔNG TIN ĐƠN HÀNG", result);
                    }
                    onSyncConfirm("ORDER","NA");
                } else {
                    mSyncCallBack.onSyncFailer(mException);
                }
            }
        }
    }


}
