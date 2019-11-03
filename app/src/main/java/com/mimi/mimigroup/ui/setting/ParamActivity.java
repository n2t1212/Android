package com.mimi.mimigroup.ui.setting;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mimi.mimigroup.R;
import com.mimi.mimigroup.api.APINet;
import com.mimi.mimigroup.api.APINetCallBack;
import com.mimi.mimigroup.api.SyncGet;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.model.HT_PARA;
import com.mimi.mimigroup.model.SystemParam;
import com.mimi.mimigroup.ui.custom.CustomBoldEditText;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import com.mimi.mimigroup.ui.server.ServerView;
import com.mimi.mimigroup.utils.AppUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

import com.mimi.mimigroup.db.DBGimsHelper;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ParamActivity extends BaseActivity implements ServerView {

    @BindView(R.id.tvParaImei)
    CustomTextView tvParaImei;
    @BindView(R.id.tvParaImeiSim)
    CustomTextView tvParaImeiSim;
    @BindView(R.id.tvParaActive)
    CustomTextView tvParaActive;
    @BindView(R.id.tvParaScope)
    CustomTextView tvParaScope;
    @BindView(R.id.tvParaDayClear)
    CustomTextView tvParaDayClear;

    @BindView(R.id.tvParaIP)
    CustomBoldEditText tvParaIP;
    @BindView(R.id.tvParaPort)
    CustomBoldEditText tvParaPort;
    @BindView(R.id.tvParaPassword)
    CustomBoldEditText tvParaPassword;
    @BindView(R.id.tvParaPasswordValid)
    CustomTextView tvParaPasswordValid;

    @BindView(R.id.cbSound)
    CheckBox cbSound;
    @BindView(R.id.cbVibra)
    CheckBox cbVibra;
    @BindView(R.id.cbPostScanQR)
    CheckBox cbPostScanQR;

    private DBGimsHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppSetting.getInstance().isAndroidVersion5()) {
            setContentView(R.layout.activity_param);
        }else {
            setContentView(R.layout.activity_param_21);
        }

        mDB=DBGimsHelper.getInstance(this);

        if (AppSetting.getInstance().getSound()==true){
            cbSound.setChecked(true);
        }else{
            cbSound.setChecked(false);
        }
        if (AppSetting.getInstance().getVibra()==true){
            cbVibra.setChecked(true);
        }else{
            cbVibra.setChecked(false);
        }
        if (AppSetting.getInstance().getPostScanQR()==true){
            cbPostScanQR.setChecked(true);
        }else{
            cbPostScanQR.setChecked(false);
        }
        cbSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AppSetting.getInstance().setSound(b);
            }
        });
        cbVibra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AppSetting.getInstance().setVibra(b);
            }
        });
        cbPostScanQR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AppSetting.getInstance().setPostScanQR(b);
            }
        });

        onLoadPara();
    }

    @Override
    public void onCheckSuccess(String ip, String port) {
        dismissProgressDialog();
        AppSetting.getInstance().setServerIP(ip);
        AppSetting.getInstance().setPort(port);
        Toast.makeText(this, "Cập nhật tham số thành công.", Toast.LENGTH_SHORT).show();
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



    private void onLoadPara(){
        try{
          tvParaPassword.setText("****");
          List<HT_PARA> lstPara=mDB.getAllPara();
          for(HT_PARA oPar:lstPara){
              try {
                  //Log.d("PARA_NAME",oPar.getParaCode() + " : " +oPar.getParaValue());
                  if (oPar.getParaCode() != null && oPar.getParaCode().equalsIgnoreCase("PAR_IMEI")) {
                      tvParaImei.setText(oPar.getParaValue());
                  }
                  if (oPar.getParaCode() != null && oPar.getParaCode().equalsIgnoreCase("PAR_IMEISIM")) {
                      tvParaImeiSim.setText(oPar.getParaValue());
                  }
                  if (oPar.getParaCode() != null && oPar.getParaCode().equalsIgnoreCase("PAR_ISACTIVE")) {
                      if (oPar.getParaValue().equals("1")) {
                          tvParaActive.setText("Đã kích hoạt");
                      } else {
                          tvParaActive.setText("Chưa kích hoạt");
                      }
                  }
                  if (oPar.getParaCode() != null && oPar.getParaCode().equalsIgnoreCase("PAR_SCOPE")) {
                      tvParaScope.setText(oPar.getParaValue());
                  }
                  if (oPar.getParaCode() != null && oPar.getParaCode().equalsIgnoreCase("PAR_DAY_CLEAR")) {
                      tvParaDayClear.setText(oPar.getParaValue());
                  }
                  if (oPar.getParaCode() != null && oPar.getParaCode().equalsIgnoreCase("PAR_SERVER_IP")) {
                      try{
                          if (!oPar.getParaValue().isEmpty()){
                              String[]IpOctet=oPar.getParaValue().toString().split("\\.");
                              if (IpOctet.length>3){
                                  Integer iLastOt=Integer.parseInt(IpOctet[3].toString());
                                  iLastOt=iLastOt+12;
                                  String mIP=IpOctet[0].toString()+"."+IpOctet[1].toString()+"."+IpOctet[2].toString()+"."+String.valueOf(iLastOt);
                                  tvParaIP.setText(mIP);
                              }else{
                                  tvParaIP.setText(oPar.getParaValue());
                              }
                          }
                      }catch (Exception ex){}
                  }
                  if (oPar.getParaCode() != null && oPar.getParaCode().equalsIgnoreCase("PAR_SERVER_PORT")) {
                      int iPort=0;
                      try{
                        iPort=Integer.parseInt(oPar.getParaValue());
                     }catch (Exception e){}
                      tvParaPort.setText(String.valueOf(iPort+12));
                  }
                  if (oPar.getParaCode() != null && oPar.getParaCode().equalsIgnoreCase("PAR_ADMIN_PASS")) {
                      tvParaPasswordValid.setText(oPar.getParaValue());
                  }
              }catch (Exception e){}
          }

            if (tvParaIP.getText().toString().isEmpty() || tvParaIP.getText().toString().equalsIgnoreCase("000.000.000")) {
                try{
                    if (!AppSetting.getInstance().getServerIP().isEmpty()){
                        String[]IpOctet=AppSetting.getInstance().getServerIP().split("\\.");
                        if (IpOctet.length>3){
                            Integer iLastOt=Integer.parseInt(IpOctet[3].toString());
                            iLastOt=iLastOt+12;
                            String mIP=IpOctet[0].toString()+"."+IpOctet[1].toString()+"."+IpOctet[2].toString()+"."+String.valueOf(iLastOt);
                            tvParaIP.setText(mIP);
                        }else{
                            tvParaIP.setText(AppSetting.getInstance().getServerIP());
                        }
                    }
                }catch (Exception ex){}
            }
            if (tvParaPort.getText().toString().isEmpty() || tvParaPort.getText().toString().equalsIgnoreCase("000")) {
                int iPort=0;
                try{
                    iPort=Integer.parseInt(AppSetting.getInstance().getPort());
                }catch (Exception e){}
                tvParaPort.setText(String.valueOf(iPort+12));
            }
            if(tvParaImei.getText().toString().isEmpty() || tvParaImei.getText().toString()==""){
              tvParaImei.setText(AppUtils.getImeil(this));
            }
            if(tvParaImeiSim.getText().toString().isEmpty() || tvParaImeiSim.getText().toString()==""){
                tvParaImeiSim.setText(AppUtils.getImeilsim(this));
            }
        }catch (Exception ex){
            Toast.makeText(this,"Không thể đọc bảng tham số"+ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.ivBack)
    public void onBack(){
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @OnClick(R.id.btnParaSave)
    public void onSaved(){
      String ip =tvParaIP.getText().toString().trim();
      String port = tvParaPort.getText().toString().trim();
      String Imei=tvParaImei.getText().toString().trim();
      String ImeiSim=tvParaImeiSim.getText().toString().trim();
      String PassW=tvParaPassword.getText().toString().trim();
      String PassWValid=tvParaPasswordValid.getText().toString().trim();
      String parScope=tvParaScope.getText().toString().trim();

      if(Imei=="" || Imei.equalsIgnoreCase("N/A")){Imei=AppUtils.getImeil(ParamActivity.this);}
      if(ImeiSim=="" || Imei.equalsIgnoreCase("N/A")){ImeiSim=AppUtils.getImeilsim(ParamActivity.this);}

      if(ip.isEmpty() || ip=="000.000.000"){
          Toast.makeText(this, "Bạn chưa nhập IP máy chủ.", Toast.LENGTH_SHORT).show();
          return;
      }
      if(port.isEmpty() || ip=="000"){
            Toast.makeText(this, "Bạn chưa nhập Port máy chủ", Toast.LENGTH_SHORT).show();
            return;
      }

      if(PassW.equalsIgnoreCase(PassWValid)){
          HT_PARA oPar=new HT_PARA();
          oPar.setParaCode("PAR_IMEI");
          oPar.setParaValue(Imei);
          mDB.addHTPara(oPar);

          oPar=new HT_PARA();
          oPar.setParaCode("PAR_IMEISIM");
          oPar.setParaValue(ImeiSim);
          mDB.addHTPara(oPar);

          oPar=new HT_PARA();
          oPar.setParaCode("PAR_SERVER_IP");
          oPar.setParaValue(ip);
          mDB.addHTPara(oPar);

          oPar=new HT_PARA();
          oPar.setParaCode("PAR_SERVER_PORT");
          oPar.setParaValue(port);
          mDB.addHTPara(oPar);

          try {
              AppSetting.getInstance().setServerIP(ip);
              AppSetting.getInstance().setPort(String.valueOf(port));
              AppSetting.getInstance().setParscope(parScope);
          }catch (Exception ex){}

         Toast oToast =Toast.makeText(this,"Cập nhật tham số thành công",Toast.LENGTH_LONG);
         oToast.setGravity(Gravity.CENTER,0,0);
         oToast.show();

        try {
              if(APINet.isNetworkAvailable(this)==false){
                  Toast.makeText(this,"Máy bạn chưa truy cập mạng. Không thể xác thực máy chủ",Toast.LENGTH_LONG).show();
                  return;
              }
              String mUrlGet = AppSetting.getInstance().URL_CheckServer();
              new SyncGet(new APINetCallBack() {
                  @Override
                  public void onHttpStart() {
                      showProgressDialog("Đang kiểm tra kết nối máy chủ..");
                  }

                  @Override
                  public void onHttpSuccess(String ResPonseRs) {
                      dismissProgressDialog();
                      if (ResPonseRs != null && ResPonseRs.contains("SYNC_OK")) {
                         // Log.v("RESP_CHECKSERVER", ResPonseRs);
                          Toast.makeText(ParamActivity.this, "Thông tin kết nối máy chủ hợp lệ.", Toast.LENGTH_SHORT).show();
                      } else {
                          Toast.makeText(ParamActivity.this, "Không xác thực được máy chủ. Kiểm tra thông tin hoặc mạng máy bạn", Toast.LENGTH_LONG).show();
                      }
                      onLoadPara();
                  }

                  @Override
                  public void onHttpFailer(Exception e) {
                      dismissProgressDialog();
                  }
              }, mUrlGet, "CHECK_SERVER").execute();
          }catch (Exception ex){dismissProgressDialog();}

          dismissProgressDialog();
      }
      else{
        Toast oT= Toast.makeText(this,"Mật khẩu xác thực không đúng. Liên hệ Admin để có mật khẩu cập nhật.",Toast.LENGTH_LONG);
        oT.setGravity(Gravity.CENTER,0,0);
        oT.show();
     }
    }

    @OnClick(R.id.btnParaDown)
    public void onDownload(){
        if(APINet.isNetworkAvailable(this)==false){
            Toast.makeText(this,"Máy bạn chưa kết nối mạng. Không thể tải tham số.",Toast.LENGTH_LONG).show();
            return;
        }
        fSyncPARA();
    }

    @OnLongClick(R.id.tvbtnClearDB)
    public boolean onClearDBHis(){
        try{
            final String mUrlPostOrder=AppSetting.getInstance().URL_PostOrder();
            final Dialog oDlg=new Dialog(ParamActivity.this);
            oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            oDlg.setContentView(R.layout.dialog_yesno);
            oDlg.setTitle("");
            CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
            dlgTitle.setText("Xác nhận xóa toàn bộ dữ liệu");
            CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
            dlgContent.setText("Bạn có chắc muốn xóa toàn bộ dữ liệu danh mục không?  Lưu ý: Khi [Đồng ý] ->  dữ liệu danh mục trên máy không thể phục hồi. Do vậy bạn cần phải tải lại để sử dụng..");
            CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
            CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDB.onClearHIS()){
                        Toast oT=Toast.makeText(ParamActivity.this,"Xóa dữ liệu thành công...",Toast.LENGTH_LONG);
                        oT.setGravity(Gravity.CENTER,0,0);
                        oT.show();
                    }else{
                        Toast oT=Toast.makeText(ParamActivity.this,"Một vài dữ liệu không thể xóa...",Toast.LENGTH_LONG);
                        oT.setGravity(Gravity.CENTER,0,0);
                        oT.show();
                    }
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
         return true;
        }catch (Exception ex){return false;}
    }

    //SYNC-GETPARA
    private void fSyncPARA(){
        if(tvParaImei.getText().toString().equalsIgnoreCase("N/A") || tvParaImei.getText().toString().isEmpty()){
            tvParaImei.setText(AppUtils.getImeil(ParamActivity.this));
        }
        if(tvParaImeiSim.getText().toString().equalsIgnoreCase("N/A") || tvParaImeiSim.getText().toString().isEmpty()){
            tvParaImeiSim.setText(AppUtils.getImeilsim(ParamActivity.this));
        }

        final String Imei=tvParaImei.getText().toString().trim();
        final String ImeiSim=tvParaImeiSim.getText().toString().trim();

        final String mUrlGetPara = AppSetting.getInstance().URL_SyncHTPARA(Imei, ImeiSim);
        new SyncGet(new APINetCallBack() {
            @Override
            public void onHttpStart() {
                showProgressDialog("Đang tải tham số hệ thống.");
                //Log.d("SYNC_HTPARA",mUrlGetPara);
            }

            @Override
            public void onHttpSuccess(final String ResPonseRs) {
                dismissProgressDialog();
                if(ResPonseRs!=null && ResPonseRs!="null" && ResPonseRs!="[]" && !ResPonseRs.isEmpty()){
                    //Log.d("RESP_SYNC_HTPARA",ResPonseRs);
                    //ParamActivity.this.runOnUiThread(new Runnable() {
                        //@Override
                       // public void run() {

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
                                }
                                mDB.addHTPara(new HT_PARA("PAR_IMEI",Imei));
                                mDB.addHTPara(new HT_PARA("PAR_IMEISIM",ImeiSim));

                                try {
                                    AppSetting.getInstance().setParscope(mDB.getParam("PAR_SCOPE"));
                                }catch (Exception ex){}

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
                                if (mDB.getParam("PAR_REG").toString().contains("1")){
                                    if(mDB.getParam("PAR_ISACTIVE").toString().contains("0")) {
                                        Toast.makeText(ParamActivity.this,"Thiết bị của bạn chưa Xác nhận",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                dismissProgressDialog();

                                onLoadPara();

                            } catch (Exception e) {
                                e.printStackTrace();
                                onError(e.getMessage().toString());
                            }
                       // }
                    //});
                }
            }

            @Override
            public void onHttpFailer(Exception e) {
                dismissProgressDialog();
            }
        },mUrlGetPara,"HT_PARA").execute();

    }


}
