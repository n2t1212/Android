package com.mimi.mimigroup.app;

import android.os.Build;

import com.mimi.mimigroup.model.SystemParam;
import com.orhanobut.hawk.Hawk;

public class AppSetting {
    private static final AppSetting instance = new AppSetting();

    public synchronized static AppSetting getInstance(){
        return instance;
    }
    public static final String PROJECT_NUMBER="137755505472";
    public static final String SERVER_IP = "server-ip";
    public static final String PORT = "port";
    public static final String SOUND = "sound";
    public static final String VIBRA = "vibra";
    public static final String POST_SCAN_QR = "post_scan_qr";//Gửi QR ngay khi quét
    public static final String ACTIVE = "active";
    public static final String PARAM = "param";
    public static final String RUNNABLE = "runnable";
    public static final String PAR_SCOPE="0";

    private String serverIP;
    private String port;
    private boolean sound;
    private boolean vibra;
    private boolean post_scan_qr;
    private boolean isActive;
    private boolean runnable;
    private SystemParam param;
    private String parscope;


    private AppSetting(){
       loadData();
    }

    public boolean isRunnable() {
        return true;
        //return runnable;
    }

    public void setRunnable(boolean runnable) {
        this.runnable = runnable;
        Hawk.put(RUNNABLE, runnable);
    }

    public SystemParam getParam() {
        return param;
    }

    public void setParam(SystemParam param) {
        this.param = param;
        Hawk.put(PARAM, param);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        Hawk.put(ACTIVE, active);
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
        Hawk.put(SERVER_IP, serverIP);
    }

    public String getPort() {
        return port;
    }
    public void setPort(String port) {
        this.port = port;
        Hawk.put(PORT, port);
    }

    public boolean getSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
        Hawk.put(SOUND, sound);
    }

    public boolean getVibra() {
        return vibra;
    }

    public void setVibra(boolean vibra) {
        this.vibra = vibra;
        Hawk.put(VIBRA, vibra);
    }

    public String getParscope() {
        if(this.parscope==null || this.parscope.isEmpty()) {
            return "0";
        }else {
            return this.parscope;
        }
    }

    public void setParscope(String Parscope) {
        if(parscope==null || parscope.isEmpty()){parscope="0";}
        this.parscope = Parscope;
        Hawk.put(PAR_SCOPE, Parscope);
    }

    public boolean getPostScanQR() {
        return post_scan_qr;
    }
    public void setPostScanQR(boolean post_scan_qr) {
        this.post_scan_qr = post_scan_qr;
        Hawk.put(POST_SCAN_QR, post_scan_qr);
    }

    private void clear(){
        Hawk.deleteAll();
        Hawk.destroy();
    }


    private void loadData(){

        this.sound = Hawk.get(SOUND, false);
        this.vibra = Hawk.get(VIBRA, true);
        this.post_scan_qr = Hawk.get(POST_SCAN_QR, false);
        this.serverIP = Hawk.get(SERVER_IP, "113.161.164.228");
        this.port = Hawk.get(PORT, "9000");
        this.isActive = Hawk.get(ACTIVE, false);
        this.param = Hawk.get(PARAM, null);
        this.runnable = Hawk.get(RUNNABLE, false);
        try {
          this.parscope=  Hawk.get(PAR_SCOPE,"0");
        }catch (Exception ex){}
    }


    public boolean isAndroidVersion5(){
        //Build.VERSION_CODES.M: ANDROID 6... MarshMallow,Build.VERSION_CODES.LOLLIPOP: Android 5.0..
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return true;
        }else {
            return false;
        }
    }

    public String URL_SERVER(){
      //return   "http://" + AppSetting.getInstance().getServerIP() + ":" + AppSetting.getInstance().getPort();
        return   "http://" + getServerIP() + ":" + getPort();
    }

    public String URL_CheckServer(){
        return URL_SERVER() +"/MIMIAPI/fCheckLinkServer";
    }

    public String URL_SyncConfirm(String Imei,String ImeiSim,String tblName){
        return URL_SERVER() +"/MIMIAPI/fSyncConfirm?imei="+Imei+"&imeisim="+ImeiSim+"&tblname="+tblName;
    }

    public String URL_Register(String Imei,String ImeiSim,String mFullName){
        return URL_SERVER() +"/MIMIAPI/fRegDevice?imei="+Imei+"&imeisim="+ImeiSim+"&fullname="+mFullName;
    }

    public String URL_SyncHTPARA(String Imei,String ImeiSim){
        return URL_SERVER() +"/MIMIAPI/fSyncPara?imei="+Imei+"&imeisim="+ImeiSim;
    }

    public String URL_SyncDM(String Imei,String ImeiSim,String mTblName,Boolean AllRow){
        return URL_SERVER() +"/MIMIAPI/fSyncDM?imei="+Imei+"&imeisim="+ImeiSim +"&tblname="+mTblName+"&allrow="+(AllRow==false?"0":"1");
    }

    public String URL_GetQR(String Imei,String ImeiSim,String mQRid,Boolean AllRow,Integer istype){
        return URL_SERVER() +"/MIMIAPI/fGetQR?imei="+Imei+"&imeisim="+ImeiSim+"&qrid="+mQRid+"&allrow="+(AllRow==false?"0":"1"+"&istype="+istype.toString());
    }

    public String URL_GetFindQRID(String Imei,String ImeiSim,String mQRid,Boolean AllRow,Integer istype){
        return URL_SERVER() +"/MIMIAPI/fGetFindQRID?imei="+Imei+"&imeisim="+ImeiSim+"&qrid="+mQRid+"&allrow="+(AllRow==false?"0":"1"+"&istype="+istype.toString());
    }

    public String URL_Search(String Imei,String ImeiSim,String mType,String mPar0,String mPar1,String mPar2){
        return URL_SERVER() +"/MIMIAPI/fMTSearch?imei="+Imei+"&imeisim="+ImeiSim+"&istype="+mType+"&par0="+mPar0+"&par1="+mPar1+"&par2="+mPar2;
    }

    public String URL_GetDelivery(String Imei,String ImeiSim,String mType,String mOrderID,String mOrderCode){
        return URL_SERVER() +"/MIMIAPI/fSyncDilivery?imei="+Imei+"&imeisim="+ImeiSim+"&orderid="+mOrderID+"&ordercode="+mOrderCode+"&istype="+mType;
    }
    public String URL_SyncConfirmOrder(String Imei,String ImeiSim,String isType,String OrderID){
        return URL_SERVER() +"/MIMIAPI/fSyncConfirmOrder?imei="+Imei+"&imeisim="+ImeiSim+"&istype="+isType+"&orderid="+OrderID;
    }
    public String URL_GetTotalSale(String Imei,String ImeiSim,String mType,String mFday,String mTDay){
        return URL_SERVER() +"/MIMIAPI/fSyncTotalSale?imei="+Imei+"&imeisim="+ImeiSim+"&fday="+mFday+"&tday="+mTDay+"&istype="+mType;
    }
    public String URL_GetStatus(String Imei,String ImeiSim,String mType){
        return URL_SERVER() +"/MIMIAPI/fSyncStatus?imei="+Imei+"&imeisim="+ImeiSim+"&istype="+mType;
    }

    public String URL_PostQR(){
        return URL_SERVER() +"/MIMIAPI/fPostQR";
    }

    public String URL_PostCus(){
        return URL_SERVER() +"/MIMIAPI/fPostCustomer";
    }

    public String URL_PostVisitCard(){
        return URL_SERVER() +"/MIMIAPI/fPostTimekeeping";
    }

    public String URL_PostOrder(){
        return URL_SERVER() +"/MIMIAPI/fPostOrder";
    }
    public String URL_PostPay(){
        return URL_SERVER() +"/MIMIAPI/fPostPay";
    }

    public String URL_PostReportTech()
    {
        return URL_SERVER() + "/MIMIAPI/fPostReportTech";
    }

    public String URL_PostReportSale()
    {
        return URL_SERVER() + "/MIMIAPI/fPostReportSale";
    }
}
