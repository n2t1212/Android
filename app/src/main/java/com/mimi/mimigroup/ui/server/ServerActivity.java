package com.mimi.mimigroup.ui.server;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mimi.mimigroup.R;
import com.mimi.mimigroup.api.APINetCallBack;
import com.mimi.mimigroup.api.SyncGet;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.model.HT_PARA;
import com.mimi.mimigroup.model.SystemParam;
import com.mimi.mimigroup.ui.custom.CustomEditText;
import com.mimi.mimigroup.ui.login.LoginActivity;
import com.mimi.mimigroup.ui.main.MainActivity;
import com.mimi.mimigroup.utils.AppUtils;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import com.mimi.mimigroup.db.DBGimsHelper;

import butterknife.BindView;
import butterknife.OnClick;

public class ServerActivity extends BaseActivity implements ServerView{

    @BindView(R.id.edtIP)
    CustomEditText edtIP;
    @BindView(R.id.edtPort)
    CustomEditText edtPort;

    private DBGimsHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        mDB=DBGimsHelper.getInstance(this);

        edtIP.setText(AppSetting.getInstance().getServerIP());
        edtPort.setText(AppSetting.getInstance().getPort());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 50);
        }

        //check nếu trùng IMEI & IMEISIM vào Form Main
        try{
            if(!mDB.getParam("PAR_SERVER_IP").isEmpty()){
                String mIMEI=mDB.getParam("PAR_IMEI");
                String mIMEISIM=mDB.getParam("PAR_IMEISIM");

                if(AppUtils.getImeil(ServerActivity.this).contentEquals(mIMEI) && AppUtils.getImeilsim(ServerActivity.this).contentEquals(mIMEISIM)){
                    onNextPage(true);
                    Log.d("IMEI",mIMEI);
                }
            }
        }catch (Exception ex){}
    }

    @OnClick(R.id.tvContinue)
    public void onContinueClick(){
        String ip = edtIP.getText().toString().trim();
        String port = edtPort.getText().toString().trim();
        if (!ip.isEmpty() && !port.isEmpty()){
            //CHECK SERVER
            String mUrlGet=AppSetting.getInstance().URL_CheckServer();
            new SyncGet(new APINetCallBack(){
                @Override
                public void onHttpStart() {
                    showProgressDialog("Đang kiểm tra kết nối máy chủ..");
                }

                @Override
                public void onHttpSuccess(String ResPonseRs) {
                    if (ResPonseRs!=null && ResPonseRs.contains("SYNC_OK")){
                        Log.v("RESP_CHECKSERVER",ResPonseRs);
                        onCheckSuccess(edtIP.getText().toString(),edtPort.getText().toString());
                    }
                }

                @Override
                public void onHttpFailer(Exception e) {
                    onCheckFail(e.getMessage());
                    //Toast.makeText(ServerActivity.this, "Lỗi kết nối server:"+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }, mUrlGet, "CHECK_SERVER").execute();

        }else{
            Toast.makeText(this, "Vui lòng điên đủ IP và Port", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckSuccess(String ip, String port) {
        dismissProgressDialog();
        Toast.makeText(this, "Kiểm tra server thành công", Toast.LENGTH_SHORT).show();
        AppSetting.getInstance().setServerIP(ip);
        AppSetting.getInstance().setPort(port);

        mDB.addHTPara(new HT_PARA("PAR_SERVER_IP",ip));
        mDB.addHTPara(new HT_PARA("PAR_SERVER_PORT",port));
        //Kiểm tra kết nối nếu OK tải tham số hệ thống
        //Đọc tham số par_isactive. Nếu SYNC_REG: thì vô LoginActive else vô Main
        fSyncPARA();
        //startActivity(new Intent(this, LoginActivity.class), true);
        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void onNextPage(boolean isMain){
        if (isMain){
            startActivity(new Intent(ServerActivity.this, MainActivity.class), true);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else{
            startActivity(new Intent(this, LoginActivity.class), true);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public void onCheckFail(String msg) {
        dismissProgressDialog();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String msg) {
        dismissProgressDialog();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    //SYNC-GETPARA
    private void fSyncPARA(){
        final String Imei=AppUtils.getImeil(ServerActivity.this);
        final String ImeiSim=AppUtils.getImeilsim(getApplicationContext());

        final String mUrlGetPara = AppSetting.getInstance().URL_SyncHTPARA(Imei, ImeiSim);
        new SyncGet(new APINetCallBack() {
            @Override
            public void onHttpStart() {
                showProgressDialog("Đang tải tham số hệ thống.");
                Log.d("SYNC_HTPARA",mUrlGetPara);
            }

            @Override
            public void onHttpSuccess(final String ResPonseRs) {
                if(ResPonseRs!=null && ResPonseRs!="" && !ResPonseRs.isEmpty()){
                    Log.d("RESP_SYNC_HTPARA",ResPonseRs);
                    ServerActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //JSONObject json = new JSONObject(myResponse);
                                //json.getString("Para_Value");
                                //txtString.setText("First Name: "+json.getJSONObject("data").getString("first_name") + "\nLast Name: " + json.getJSONObject("data").getString("last_name"));

                                HashMap<String, String> map = new HashMap<>();
                                Gson gson = new Gson();
                                Type type = new TypeToken<Collection<HT_PARA>>() {}.getType();
                                Collection<HT_PARA> enums = gson.fromJson(ResPonseRs, type);
                                HT_PARA[] ArrPARA = enums.toArray(new HT_PARA[enums.size()]);
                                for(HT_PARA oPar:ArrPARA){
                                    mDB.addHTPara(oPar);
                                    map.put(oPar.getParaCode(), oPar.getParaValue());
                                    Log.v("PARA_SYNC",oPar.getParaCode());
                                }
                                mDB.addHTPara(new HT_PARA("PAR_IMEI",Imei));
                                mDB.addHTPara(new HT_PARA("PAR_IMEISIM",ImeiSim));

                                try {
                                    String PAR_TIME_SYNC="0",PAR_KEY_ENCRYPT="",PAR_SCOPE="",PAR_DAY_CLEAR="",PAR_ADMIN_PASS="",PAR_ISACTIVE="";
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
                                Log.d("PAR_REG",mDB.getParam("PAR_REG").toString());
                                if (mDB.getParam("PAR_REG").toString().contains("1")){
                                    if(mDB.getParam("PAR_ISACTIVE").toString().contains("0")) {
                                        Toast.makeText(ServerActivity.this,"Thiết bị của bạn chưa Xác nhận",Toast.LENGTH_SHORT).show();
                                    }
                                    onNextPage(true);
                                }else{
                                    onNextPage(false);
                                }



                            } catch (Exception e) {
                                e.printStackTrace();
                                onError(e.getMessage().toString());
                            }
                        }
                    });
                }
            }

            @Override
            public void onHttpFailer(Exception e) {

            }
        },mUrlGetPara,"HT_PARA").execute();

    }

}
