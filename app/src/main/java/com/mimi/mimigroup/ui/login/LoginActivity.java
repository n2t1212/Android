package com.mimi.mimigroup.ui.login;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mimi.mimigroup.R;
import com.mimi.mimigroup.api.APINetCallBack;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.model.HT_PARA;
import com.mimi.mimigroup.model.SystemParam;
import com.mimi.mimigroup.ui.custom.CustomEditText;
import com.mimi.mimigroup.ui.main.MainActivity;
import com.mimi.mimigroup.utils.AppUtils;

import com.mimi.mimigroup.api.SyncGet;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

import com.mimi.mimigroup.db.DBGimsHelper;

public class LoginActivity extends BaseActivity implements LoginView{

    @BindView(R.id.edtName)
    CustomEditText edtName;
    @BindView(R.id.edtDvi)
    CustomEditText edtDvi;

    private DBGimsHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppSetting.getInstance().isAndroidVersion5()) {
            setContentView(R.layout.activity_login);
        }else{
            setContentView(R.layout.activity_login_21);
        }
       // presenter = new LoginPresenter(this, subscriptions);
        mDB=DBGimsHelper.getInstance(this);

        /* XÁC THỰC QUYỀN (ĐÃ XÁC THỰC TỪ MÀN HÌNH Server
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 50);
        }*/
    }

    @OnClick(R.id.ivBack)
    public void onBack(){
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @OnClick(R.id.tvContinue)
    public void onContinue(){
        String name = edtName.getText().toString().trim();
        String dvi = edtDvi.getText().toString().trim();
        name=name+"|"+dvi;
        if (!name.isEmpty() && !dvi.isEmpty()){

            //REGISTER
            final String mUrlGet=AppSetting.getInstance().URL_Register(AppUtils.getImeil(this),AppUtils.getImeilsim(this),name);
             new SyncGet(new APINetCallBack() {
                 @Override
                 public void onHttpStart() {
                     showProgressDialog("Đang đăng ký thiết bị..");
                     //Log.d("URL",mUrlGet);
                 }

                 @Override
                 public void onHttpSuccess(String ResPonseRs) {
                     if (ResPonseRs!=null){
                         //Log.d("RES_LOGIN",ResPonseRs);
                         if (ResPonseRs.toUpperCase().contains("SYNC_OK")){
                             AppSetting.getInstance().setActive(false);
                             Toast oT= Toast.makeText(LoginActivity.this,"Đăng ký thiết bị thành công. Vui lòng chờ xác thực...",Toast.LENGTH_LONG);
                             oT.setGravity(Gravity.CENTER,0,0);
                             oT.show();
                             fSyncPARA();

                         }else if(ResPonseRs.contains("SYNC_ERR: Số IMEI và IMEI SIM không thể bỏ trống")){
                             AppSetting.getInstance().setActive(false);
                             onError("Số IMEI và Serial SIM của bạn không xác định [null]..");

                         }else if(ResPonseRs.contains("SYNC_WAIT: Thiết bị của bạn đã được đăng ký. Vui lòng chờ duyệt")){
                             AppSetting.getInstance().setActive(false);
                             onError("Thiết bị của bạn đã được đăng ký. Vui lòng chờ duyệt..");
                             fSyncPARA();
                         }

                         else if(ResPonseRs.contains("SYNC_STOP: Thiết bị của bạn đã bị khóa. Vui lòng liên hệ Quản trị")){
                             AppSetting.getInstance().setActive(false);
                             onError("Thiết bị của bạn đã bị khóa. Vui lòng liên hệ Quản trị..");
                             fSyncPARA();
                         }else if(ResPonseRs.contains("SYNC_ACTIVE: Thiết bị của bạn đã được đăng ký")){
                             AppSetting.getInstance().setActive(false);
                             onError("Thiết bị của bạn đã đăng ký và xác nhận..");
                             fSyncPARA();
                         }
                         else if(ResPonseRs.toUpperCase().contains("SYNC_ACTIVE") || ResPonseRs.toUpperCase().contains("SYNC_WAIT") || ResPonseRs.toUpperCase().contains("SYNC_STOP")){
                             AppSetting.getInstance().setActive(false);
                             //Log.d("SyncPARA","CallSyncPara");
                             fSyncPARA();
                         }else{
                             onError("Không xác định trạng thái dữ liệu trả về");
                         }
                     }else{
                         onError("Không thể gửi thông tin đăng ký. Vui lòng kiểm tra kết nối Mạng.");
                     }

                 }

                 @Override
                 public void onHttpFailer(Exception e) {
                     onError(e.getMessage().toString());
                 }
             }, mUrlGet, "REGISTER").execute();

        }else{
            Toast.makeText(this, "Vui lòng nhập tên và phòng ban", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 50 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED){

        }else{
            Toast.makeText(this, "Ứng dụng không đủ quyền để tiếp tục", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoginSuccess() {
        dismissProgressDialog();

        Toast.makeText(LoginActivity.this,"Đã gửi đăng ký thành công!",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class), true);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onLoginFail(String msg) {
        dismissProgressDialog();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class), true);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onError(String msg) {
        dismissProgressDialog();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    //SYNC-GETPARA
    private void fSyncPARA(){
        final String Imei=AppUtils.getImeil(LoginActivity.this);
        final String ImeiSim=AppUtils.getImeilsim(getApplicationContext());

        final String mUrlGetPara = AppSetting.getInstance().URL_SyncHTPARA(Imei, ImeiSim);
        new SyncGet(new APINetCallBack() {
            @Override
            public void onHttpStart() {
                showProgressDialog("Đang tải tham số hệ thống.");
                //Log.d("SYNC_HTPARA",mUrlGetPara);
            }

            @Override
            public void onHttpSuccess(final String ResPonseRs) {
                if(ResPonseRs!=null && ResPonseRs!="" && !ResPonseRs.isEmpty()){
                        //Log.d("RESP_SYNC_HTPARA",ResPonseRs);
                        onLoginSuccess();
                        LoginActivity.this.runOnUiThread(new Runnable() {
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
                                    //Log.v("PARA_SYNC",oPar.getParaCode());
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
                                //Log.d("PAR_REG",mDB.getParam("PAR_REG").toString());
                                /*if (mDB.getParam("PAR_REG").toString().contains("1")){
                                    if(mDB.getParam("PAR_ISACTIVE").toString().contains("0")) {
                                        Toast.makeText(LoginActivity.this,"Thiết bị của bạn chưa Xác thực",Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(LoginActivity.this,"Thiết bị đã đăng ký và xác thực...",Toast.LENGTH_SHORT).show();
                                    }
                                    onLoginSuccess();
                                } else{
                                    onLoginFail("Không thể gửi thông tin đăng ký thiết bị");
                                }*/


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
