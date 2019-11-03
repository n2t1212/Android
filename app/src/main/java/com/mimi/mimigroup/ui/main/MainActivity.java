package com.mimi.mimigroup.ui.main;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mimi.mimigroup.R;
import com.mimi.mimigroup.api.APIGPSTrack;
import com.mimi.mimigroup.api.APINet;
import com.mimi.mimigroup.api.APINetCallBack;
import com.mimi.mimigroup.api.SyncGet;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.HT_PARA;
import com.mimi.mimigroup.model.SystemParam;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.ui.login.LoginActivity;
import com.mimi.mimigroup.ui.order.OrderFragment;
import com.mimi.mimigroup.ui.services.mimiService;
import com.mimi.mimigroup.ui.setting.SettingFragment;
import com.mimi.mimigroup.ui.utility.UtilityFragment;
import com.mimi.mimigroup.ui.visitcard.VisitCardFragment;
import com.mimi.mimigroup.utils.AppUtils;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private DBGimsHelper mDB;
    APIGPSTrack locationTrack;

    //SERVICES
    //private IntentFilter mIntentFilter;
    //private mimiBroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 50);
        }else{
            tabLayout.addTab(tabLayout.newTab().setCustomView(getTabView(0)));
            tabLayout.addTab(tabLayout.newTab().setCustomView(getTabView(1)));
            tabLayout.addTab(tabLayout.newTab().setCustomView(getTabView(2)));
            tabLayout.addTab(tabLayout.newTab().setCustomView(getTabView(3)));
            tabLayout.addOnTabSelectedListener(this);
            replaceFragment(new OrderFragment(), R.id.flContain);
        }
        mDB = DBGimsHelper.getInstance(this);

        if(APINet.isNetworkAvailable(this)) {
            if(!mDB.getParam("PAR_REG").contains("1")) {
               onSyncPARA();
            }
        }

        //[11022019]
        locationTrack = new APIGPSTrack(MainActivity.this);
        if(!locationTrack.canGetLocation()){
            locationTrack.showSettingsAlert();
        }else{
            locationTrack.getLocation();
        }

        try{
          String mParScope=mDB.getParam("PAR_SCOPE");
          AppSetting.getInstance().setParscope(mParScope);
        }catch (Exception ex){}

        try {
            String mSERVER_IP=mDB.getParam("PAR_SERVER_IP");
            String mSERVER_PORT=mDB.getParam("PAR_SERVER_PORT");
            if(mSERVER_IP!=null && !mSERVER_IP.isEmpty() && !mSERVER_IP.equalsIgnoreCase(AppSetting.getInstance().getServerIP())){
                AppSetting.getInstance().setServerIP(mSERVER_IP);
            }else if(mSERVER_IP.isEmpty() || mSERVER_IP==null){
                HT_PARA oPar=new HT_PARA();
                oPar.setParaCode("PAR_SERVER_IP");
                oPar.setParaValue(AppSetting.getInstance().getServerIP());
                mDB.addHTPara(oPar);
            }

            if(mSERVER_PORT!=null && !mSERVER_PORT.isEmpty() && !mSERVER_PORT.equalsIgnoreCase(AppSetting.getInstance().getPort())) {
                AppSetting.getInstance().setPort(mSERVER_PORT);
            }else if(mSERVER_PORT==null || mSERVER_PORT.isEmpty() ){
                HT_PARA oPar=new HT_PARA();
                oPar.setParaCode("PAR_SERVER_PORT");
                oPar.setParaValue(AppSetting.getInstance().getPort());
                mDB.addHTPara(oPar);
            }
        }catch (Exception ex){}


        //CALL SERVICE
        //stopService(new Intent(this,APILocationServices.class));
        //Start MyService cùng với IntentFilter
        if(!isMyServiceRunning(mimiService.class)) {
            //mReceiver = new mimiBroadcastReceiver();
            //mIntentFilter = new IntentFilter();
            //mIntentFilter.addAction("ORDER_STATUS");
            Intent serviceIntent = new Intent(getApplicationContext(), mimiService.class);
            startService(serviceIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tabLayout.getSelectedTabPosition() == 0){
            replaceFragment(new OrderFragment(), R.id.flContain);
        }

        //service
        //registerReceiver(mReceiver, mIntentFilter);
    }

    //services
    @Override
    protected void onPause() {
        //unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    protected void onStop(){
        //stopService(new Intent(this,APILocationServices.class));
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 50 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED){

            tabLayout.addTab(tabLayout.newTab().setCustomView(getTabView(0)));
            tabLayout.addTab(tabLayout.newTab().setCustomView(getTabView(1)));
            tabLayout.addTab(tabLayout.newTab().setCustomView(getTabView(2)));
            tabLayout.addTab(tabLayout.newTab().setCustomView(getTabView(3)));

            tabLayout.addOnTabSelectedListener(this);
            replaceFragment(new OrderFragment(), R.id.flContain);
        }else{
            Toast.makeText(this, "Ứng dụng không đủ quyền để tiếp tục", Toast.LENGTH_LONG).show();
        }
    }

    //private String tabTitles[] = new String[] { "Quét", "Lịch sử","Vào Ra", "Cài đặt" };
    private String tabTitles[] = new String[] { "Đơn Hàng", "Chấm Công","Tiện Ích","Thiết Lập" };
    private int[] imageResId = { R.drawable.tiva_order_w,R.drawable.tiva_timekeeping_w,R.drawable.tiva_utiliti_w,  R.drawable.tiva_setting_w};

    public View getTabView(int position) {
        //View v = LayoutInflater.from(App.getContext()).inflate(R.layout.custom_tab, null);
        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
        CustomBoldTextView tv = v.findViewById(R.id.tvTab);
        tv.setText(tabTitles[position]);
        ImageView img = v.findViewById(R.id.ivTab);
        img.setImageResource(imageResId[position]);
        return v;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        /*if (tab.getPosition() == 0){
            replaceFragment(new ScanFragment(), R.id.flContain);
            replaceFragment(new ScanFragment(), R.id.flContain);
        }else if (tab.getPosition() == 1){
            replaceFragment(new HistoryFragment(), R.id.flContain);
        }else if (tab.getPosition() == 2){
            replaceFragment(new VisitCardFragment(), R.id.flContain);
        }
        else if (tab.getPosition() == 3){
            replaceFragment(new SettingFragment(), R.id.flContain);
        }*/
        if (tab.getPosition() == 0){
            replaceFragment(new OrderFragment(), R.id.flContain);
        }else if (tab.getPosition() == 1){
            replaceFragment(new VisitCardFragment(), R.id.flContain);
        }else if (tab.getPosition() == 2){
            replaceFragment(new UtilityFragment(), R.id.flContain);
        }else if (tab.getPosition() == 3){
            replaceFragment(new SettingFragment(), R.id.flContain);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    @Override
    public void onTabReselected(TabLayout.Tab tab) {}


    //[11022019]
    private void turnGPSOn(){
        try {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if (!provider.contains("gps")) { //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
                Toast.makeText(this,"Đã bật GPS",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){}
    }

    //SYNC-GETPARA
    private void onSyncPARA(){
        final String Imei=AppUtils.getImeil(this);
        final String ImeiSim=AppUtils.getImeilsim(this );

        final String mUrlGetPara = AppSetting.getInstance().URL_SyncHTPARA(Imei, ImeiSim);
        new SyncGet(new APINetCallBack() {
            @Override
            public void onHttpStart() {
                Toast oToast=Toast.makeText(MainActivity.this,"Đang tại lại tham số",Toast.LENGTH_LONG);
                oToast.setGravity(Gravity.CENTER,0,0);
                oToast.show();
            }

            @Override
            public void onHttpSuccess(final String ResPonseRs) {
                if(ResPonseRs!=null && ResPonseRs!="" && ResPonseRs!="[]" && !ResPonseRs.isEmpty()){
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HashMap<String, String> map = new HashMap<>();
                                Gson gson = new Gson();
                                Type type = new TypeToken<Collection<HT_PARA>>() {}.getType();
                                Collection<HT_PARA> enums = gson.fromJson(ResPonseRs, type);
                                HT_PARA[] ArrPARA = enums.toArray(new HT_PARA[enums.size()]);
                                for(HT_PARA oPar:ArrPARA){
                                    mDB.addHTPara(oPar);
                                    map.put(oPar.getParaCode(), oPar.getParaValue());
                                }
                                mDB.addHTPara(new HT_PARA("PAR_IMEI",Imei));
                                mDB.addHTPara(new HT_PARA("PAR_IMEISIM",ImeiSim));
                                try {
                                    String PAR_TIME_SYNC="0",PAR_KEY_ENCRYPT="",PAR_SCOPE="0",PAR_DAY_CLEAR="",PAR_ADMIN_PASS="",PAR_ISACTIVE="",PAR_SYMBOL="AA";
                                    if (map.containsKey("PAR_TIMER_SYNC")){
                                        PAR_TIME_SYNC=map.get("PAR_TIMER_SYNC").toString();
                                    }
                                    if (map.containsKey("PAR_KEY_ENCRYPT")){
                                        PAR_KEY_ENCRYPT=map.get("PAR_KEY_ENCRYPT").toString();
                                    }
                                    if (map.containsKey("PAR_SCOPE")){
                                        PAR_SCOPE=map.get("PAR_SCOPE").toString();
                                    }
                                    if (map.containsKey("PAR_DAY_CLEAR")){
                                        PAR_DAY_CLEAR=map.get("PAR_DAY_CLEAR").toString();
                                    }
                                    if (map.containsKey("PAR_ADMIN_PASS")){
                                        PAR_ADMIN_PASS=map.get("PAR_ADMIN_PASS").toString();
                                    }
                                    if (map.containsKey("PAR_ISACTIVE")){
                                        PAR_ISACTIVE=map.get("PAR_ISACTIVE").toString();
                                    }

                                    SystemParam param = new SystemParam(Integer.valueOf(PAR_TIME_SYNC), PAR_KEY_ENCRYPT, Integer.valueOf(PAR_SCOPE),
                                            Integer.valueOf(PAR_DAY_CLEAR),PAR_ADMIN_PASS, Integer.valueOf(PAR_ISACTIVE));
                                    AppSetting.getInstance().setParam(param);
                                }catch (Exception ex){}

                                //NEÚ ĐÃ ĐĂNG KÝ THÌ VÔ MAIN,ELSE LOGIN PAR_REG=1 Đăng ký, 0:Ko dk
                                if (mDB.getParam("PAR_REG").toString().equalsIgnoreCase("1")){
                                    if(mDB.getParam("PAR_ISACTIVE").toString().contains("0")) {
                                      Toast oToast=Toast.makeText(MainActivity.this,"Thiết bị của bạn chưa Xác nhận",Toast.LENGTH_LONG);
                                      oToast.setGravity(Gravity.CENTER,0,0);
                                      oToast.show();
                                    }
                                }else{
                                    final Dialog oDlg=new Dialog(MainActivity.this);
                                    oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                    oDlg.setContentView(R.layout.dialog_yesno);
                                    oDlg.setTitle("");
                                    CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
                                    dlgTitle.setText("XÁC NHẬN");
                                    CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
                                    dlgContent.setText("Thiết bị này chưa đăng ký. Bạn có muốn gửi thông tin đăng ký ?");
                                    CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
                                    CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

                                    btnYes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                            MainActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

            @Override
            public void onHttpFailer(Exception e) {}
        },mUrlGetPara,"HT_PARA").execute();

    }


    //SERVICE
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                  Log.i ("isMyServiceRunning?", true+"");
                  return true;
                }
            }
        }
        //Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    /*
  //BROADCAST RECEIVER FOR SERVICE
  //Tạo một BroadcastReceiver lắng nghe service
  private BroadcastReceiver mReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
          if (intent.getAction().equals(mBroadcastAction)) {
              String msg=intent.getStringExtra("Data").toString();
              //Intent stopIntent = new Intent(MainActivity.this,mimiService.class);
              //stopService(stopIntent);
              Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
          }
      }
  };
  */

}
