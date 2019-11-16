package com.mimi.mimigroup.ui.utility;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import com.mimi.mimigroup.base.BaseFragment;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.DM_Customer;
import com.mimi.mimigroup.model.DM_District;
import com.mimi.mimigroup.model.DM_Employee;
import com.mimi.mimigroup.model.DM_Product;
import com.mimi.mimigroup.model.DM_Province;
import com.mimi.mimigroup.model.DM_Season;
import com.mimi.mimigroup.model.DM_Ward;
import com.mimi.mimigroup.model.HT_PARA;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.ui.help.HelpActivity;
import com.mimi.mimigroup.ui.login.LoginActivity;
import com.mimi.mimigroup.ui.setting.CustomerActivity;
import com.mimi.mimigroup.ui.setting.EmployeeActivity;
import com.mimi.mimigroup.ui.setting.ItemActivity;
import com.mimi.mimigroup.ui.setting.ParamActivity;
import com.mimi.mimigroup.ui.setting.ProductActivity;
import com.mimi.mimigroup.utils.AppUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.OnClick;

public class UtilityFragment extends BaseFragment {
    private DBGimsHelper mDB;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDB = DBGimsHelper.getInstance(getActivity());
    }

    @Override
    protected int getLayoutResourceId() {
        if (AppSetting.getInstance().isAndroidVersion5()) {
            return R.layout.activity_utilitiy;
        } else {
            return R.layout.activity_utilitiy;
        }
    }

    @OnClick(R.id.tvCustomerPay)
    public void onCustomerPay() {
        startActivity(new Intent(getContext(), CustomerPayActivity.class));
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /*Report sale click event*/
    @OnClick(R.id.tvReportSale)
    public void onReportSale()
    {
        startActivity(new Intent(getContext(), ReportSaleRepActivity.class));
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /*Report technical click event*/
    @OnClick(R.id.tvReportTech)
    public void onReportTech()
    {
        startActivity(new Intent(getContext(), ReportTechActivity.class));
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    //@OnClick(R.id.tvSyncAll)
    public void onSyncAllClicked() {
        if (APINet.isNetworkAvailable(getContext()) == false) {
            Toast oToast = Toast.makeText(getContext(), "Vui lòng kiểm tra kết nối mạng.", Toast.LENGTH_LONG);
            oToast.setGravity(Gravity.CENTER, 0, 0);
            oToast.show();
            return;
        }

        final Dialog oDlg=new Dialog(getContext());
        oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        oDlg.setContentView(R.layout.dialog_yesnosync);
        oDlg.setTitle("");
        CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
        dlgTitle.setText("XÁC NHẬN");
        CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
        dlgContent.setText("Vui lòng chọn hình thức đồng bộ (Tải mới/Tải tất cả)?");
        CustomBoldTextView btnAll=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonAll);
        CustomBoldTextView btnNew=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNew);
        CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSyncAll(true);
                oDlg.dismiss();
            }
        });
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSyncAll(false);
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

        /*
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getContext());
        alertDialog2.setTitle("Xác nhận");
        alertDialog2.setMessage("Bạn có muốn tải tất cả danh mục ?");
        alertDialog2.setPositiveButton("Bỏ qua",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      return;
                    }
                });
        alertDialog2.setNeutralButton("Tải mới", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onSyncAll(false);
            }
        });
        alertDialog2.setNegativeButton("Tất cả", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onSyncAll(true);
            }
        });
        alertDialog2.show();*/
    }



    private void onSyncAll(Boolean isAll) {
        List<String> lstSyncType = new ArrayList<>();
        lstSyncType.add("HT_PARA");
        lstSyncType.add("DM_PRODUCT");
        lstSyncType.add("DM_PROVINCE");
        lstSyncType.add("DM_DISTRICT");
        lstSyncType.add("DM_WARD");
        lstSyncType.add("DM_CUSTOMER");
        lstSyncType.add("DM_EMPLOYEE");
        lstSyncType.add("DM_SEASON");

        String mSyncTitle = "";
        String mUrlSync = "";
        String mIMEI = AppUtils.getImeil(getContext());
        String mImeiSim = AppUtils.getImeilsim(getContext());

        Toast mT= Toast.makeText(getContext(), "Đang kiểm tra kết nối máy chủ.", Toast.LENGTH_SHORT);
        mT.setGravity(Gravity.CENTER,0,0);
        mT.show();

        for (String mType : lstSyncType) {
            switch (mType) {
                case "HT_PARA":
                    mUrlSync = AppSetting.getInstance().URL_SyncHTPARA(mIMEI, mImeiSim);
                    mSyncTitle = "Đang tải tham số";
                    break;
                case "DM_PRODUCT":
                    mUrlSync = AppSetting.getInstance().URL_SyncDM(mIMEI, mImeiSim, mType, isAll);
                    mSyncTitle = "Đang tải danh mục sản phẩm";
                    break;
                case "DM_PROVINCE":
                    mUrlSync = AppSetting.getInstance().URL_SyncDM(mIMEI, mImeiSim, mType, isAll);
                    mSyncTitle = "Đang tải danh mục địa phương";
                    break;
                case "DM_DISTRICT":
                    mUrlSync = AppSetting.getInstance().URL_SyncDM(mIMEI, mImeiSim, mType, isAll);
                    mSyncTitle = "Đang tải danh mục quận/huyện";
                    break;
                case "DM_WARD":
                    mUrlSync = AppSetting.getInstance().URL_SyncDM(mIMEI, mImeiSim, mType, isAll);
                    mSyncTitle = "Đang tải danh mục phường/xã";
                    break;
                case "DM_CUSTOMER":
                    mUrlSync = AppSetting.getInstance().URL_SyncDM(mIMEI, mImeiSim, mType, isAll);
                    mSyncTitle = "Đang tải danh mục khách hàng";
                    break;
                case "DM_EMPLOYEE":
                    mUrlSync = AppSetting.getInstance().URL_SyncDM(mIMEI, mImeiSim, mType, isAll);
                    mSyncTitle = "Đang tải danh mục khách hàng";
                    break;
                case "DM_SEASON":
                    mUrlSync = AppSetting.getInstance().URL_SyncDM(mIMEI, mImeiSim, mType, isAll);
                    mSyncTitle = "Đang tải danh mục mùa vụ";

            }
            final String isType=mType;
            final String isUrl=mUrlSync;
            final String isSyncTitle=mSyncTitle;

            Thread mCallBack=new Thread(){
                public void run(){
                    try {
                        sleep(1000);
                    } catch (Exception e) {}
                    finally{
                        onDownload(isUrl,isSyncTitle,isType);
                        Log.d("URL_SYNC",isUrl);
                    }
                }
            };
            mCallBack.start();
        }
    }


    public void onDownload(final String mUrlGet, final String mSyncTitle, final String mType) {
        new SyncGet(new APINetCallBack() {
            @Override
            public void onHttpStart() {
                ((BaseActivity) getActivity()).showProgressDialog(mSyncTitle);
            }

            @Override
            public void onHttpSuccess(String ResPonseRs) {
                //Log.d("Rep",ResPonseRs);
                if (ResPonseRs == null || ResPonseRs.toString().trim() == null || ResPonseRs.isEmpty()) {
                    Toast.makeText(getContext(), "Không thể đọc dữ liệu từ server..RES:NULL", Toast.LENGTH_SHORT).show();
                    return;
                }else if( ResPonseRs.equalsIgnoreCase("[]")){
                    Toast.makeText(getContext(), "Không tìm thấy dữ liệu mới..", Toast.LENGTH_SHORT).show();
                    return;
                }else if(ResPonseRs.contains("SYNC_REG: Thiết bị của bạn chưa được đăng ký")){
                    Toast.makeText(getContext(), "Thiết bị của bạn chưa đăng ký. Vui lòng đăng ký trước...", Toast.LENGTH_SHORT).show();
                    return;
                }else if(ResPonseRs.contains("SYNC_ACTIVE: Thiết bị của bạn đã đăng ký nhưng chưa kích hoạt")){
                    Toast.makeText(getContext(),"Thiết bị của bạn đã đăng ký nhưng chưa kích hoạt từ server...", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    try {
                        Gson gson = new Gson();
                        Type type=null;
                        switch (mType) {
                            case "HT_PARA":
                                type = new TypeToken<Collection<HT_PARA>>() {}.getType();
                                Collection<HT_PARA> enums = gson.fromJson(ResPonseRs, type);
                                HT_PARA[] ArrPar = enums.toArray(new HT_PARA[enums.size()]);
                                if(ArrPar!=null && ArrPar.length>0){
                                   AsyncUpdate oUpdate=  new AsyncUpdate(mType,new SyncCallBack() {
                                        @Override
                                        public void onSyncStart() {
                                            Toast.makeText(getContext(), "Đang cập nhật dữ liệu.", Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onSyncSuccess(String ResPonseRs) {
                                            Toast.makeText(getContext(), "Tải dữ liệu thành công.", Toast.LENGTH_SHORT).show();
                                            //((BaseActivity) getActivity()).dismissProgressDialog();
                                        }
                                        @Override
                                        public void onSyncFailer(Exception e) {
                                            Toast.makeText(getContext(), "Lỗi cập nhật khách hàng:" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                            //((BaseActivity) getActivity()).dismissProgressDialog();
                                        }
                                    });//.execute(ArrPar);
                                    oUpdate.oHT_Para=ArrPar;
                                    oUpdate.execute();
                                }
                               break;

                            case "DM_PRODUCT":
                                type = new TypeToken<Collection<DM_Product>>() {}.getType();
                                Collection<DM_Product> enums2 = gson.fromJson(ResPonseRs, type);
                                DM_Product[] ArrProduct = enums2.toArray(new DM_Product[enums2.size()]);
                                if(ArrProduct!=null && ArrProduct.length>0){
                                  AsyncUpdate oSyncUpdate=  new AsyncUpdate(mType,new SyncCallBack() {
                                        @Override
                                        public void onSyncStart() {
                                            Toast.makeText(getContext(), "Đang cập nhật dữ liệu.", Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onSyncSuccess(String ResPonseRs) {
                                            Toast.makeText(getContext(), "Tải dữ liệu thành công.", Toast.LENGTH_SHORT).show();
                                            //((BaseActivity) getActivity()).dismissProgressDialog();
                                        }
                                        @Override
                                        public void onSyncFailer(Exception e) {
                                            Toast.makeText(getContext(), "Lỗi cập nhật khách hàng:" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                            //((BaseActivity) getActivity()).dismissProgressDialog();
                                        }
                                    });
                                  oSyncUpdate.oDMProduct=ArrProduct;
                                  oSyncUpdate.execute();

                                }
                                break;

                            case "DM_PROVINCE":
                                type = new TypeToken<Collection<DM_Province>>() {}.getType();
                                Collection<DM_Province> enums3 = gson.fromJson(ResPonseRs, type);
                                DM_Province[] ArrProvince = enums3.toArray(new DM_Province[enums3.size()]);

                                if(ArrProvince!=null && ArrProvince.length>0){
                                  AsyncUpdate oSyncUpdate=  new AsyncUpdate(mType,new SyncCallBack() {
                                        @Override
                                        public void onSyncStart() {
                                            Toast.makeText(getContext(), "Đang cập nhật dữ liệu.", Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onSyncSuccess(String ResPonseRs) {
                                            Toast.makeText(getContext(), "Tải dữ liệu thành công.", Toast.LENGTH_SHORT).show();
                                           // ((BaseActivity) getActivity()).dismissProgressDialog();
                                        }
                                        @Override
                                        public void onSyncFailer(Exception e) {
                                            Toast.makeText(getContext(), "Lỗi cập nhật khách hàng:" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                            //((BaseActivity) getActivity()).dismissProgressDialog();
                                        }
                                    });//.execute(ArrProvince);
                                    oSyncUpdate.oDMProvince=ArrProvince;
                                    oSyncUpdate.execute();
                                }

                                break;

                            case "DM_DISTRICT":
                                type = new TypeToken<Collection<DM_District>>() {}.getType();
                                Collection<DM_District> enums4 = gson.fromJson(ResPonseRs, type);
                                DM_District[] ArrDist = enums4.toArray(new DM_District[enums4.size()]);
                                if(ArrDist!=null && ArrDist.length>0){
                                 AsyncUpdate oSyncUpdate= new AsyncUpdate(mType,new SyncCallBack() {
                                        @Override
                                        public void onSyncStart() {
                                            Toast.makeText(getContext(), "Đang cập nhật dữ liệu.", Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onSyncSuccess(String ResPonseRs) {
                                            Toast.makeText(getContext(), "Tải dữ liệu thành công.", Toast.LENGTH_SHORT).show();
                                            //((BaseActivity) getActivity()).dismissProgressDialog();
                                        }
                                        @Override
                                        public void onSyncFailer(Exception e) {
                                            Toast.makeText(getContext(), "Lỗi cập nhật khách hàng:" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                           // ((BaseActivity) getActivity()).dismissProgressDialog();
                                        }
                                    });
                                    oSyncUpdate.oDMDistrict=ArrDist;
                                    oSyncUpdate.execute();
                                }
                                break;

                            case "DM_WARD":
                                type = new TypeToken<Collection<DM_Ward>>() {}.getType();
                                Collection<DM_Ward> enums5 = gson.fromJson(ResPonseRs, type);
                                DM_Ward[] ArrWard = enums5.toArray(new DM_Ward[enums5.size()]);
                                if(ArrWard!=null && ArrWard.length>0){
                                 AsyncUpdate oSyncUpdate=new AsyncUpdate(mType,new SyncCallBack() {
                                        @Override
                                        public void onSyncStart() {
                                            Toast.makeText(getContext(), "Đang cập nhật dữ liệu.", Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onSyncSuccess(String ResPonseRs) {
                                            Toast.makeText(getContext(), "Tải dữ liệu thành công.", Toast.LENGTH_SHORT).show();
                                            //((BaseActivity) getActivity()).dismissProgressDialog();
                                        }
                                        @Override
                                        public void onSyncFailer(Exception e) {
                                            Toast.makeText(getContext(), "Lỗi cập nhật khách hàng:" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                            //((BaseActivity) getActivity()).dismissProgressDialog();
                                        }
                                    });//.execute(ArrWard);
                                    oSyncUpdate.oDMWard=ArrWard;
                                    oSyncUpdate.execute();
                                }
                                break;

                            case "DM_CUSTOMER":
                                type = new TypeToken<Collection<DM_Customer>>() {}.getType();
                                Collection<DM_Customer> enums6 = gson.fromJson(ResPonseRs, type);
                                DM_Customer[]  ArrCus = enums6.toArray(new DM_Customer[enums6.size()]);
                                if(ArrCus!=null && ArrCus.length>0){
                                    AsyncUpdate oSyncUpdate= new AsyncUpdate(mType,new SyncCallBack() {
                                        @Override
                                        public void onSyncStart() {
                                            Toast.makeText(getContext(), "Đang cập nhật dữ liệu.", Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onSyncSuccess(String ResPonseRs) {
                                            Toast.makeText(getContext(), "Tải dữ liệu thành công.", Toast.LENGTH_SHORT).show();
                                           // ((BaseActivity) getActivity()).dismissProgressDialog();
                                        }
                                        @Override
                                        public void onSyncFailer(Exception e) {
                                            Toast.makeText(getActivity(), "Lỗi cập nhật khách hàng:" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                           // ((BaseActivity) getActivity()).dismissProgressDialog();
                                        }
                                    });//.execute(ArrCus);

                                    oSyncUpdate.oDMCus=ArrCus;
                                    oSyncUpdate.execute();
                                }
                                break;


                            case "DM_EMPLOYEE":
                                type = new TypeToken<Collection<DM_Employee>>() {}.getType();
                                Collection<DM_Employee> enums7 = gson.fromJson(ResPonseRs, type);
                                DM_Employee[] ArrEmp = enums7.toArray(new DM_Employee[enums7.size()]);
                                if(ArrEmp!=null && ArrEmp.length>0){
                                    AsyncUpdate oSyncUpdate=new AsyncUpdate(mType,new SyncCallBack() {
                                        @Override
                                        public void onSyncStart() {
                                            Toast.makeText(getContext(), "Đang cập nhật dữ liệu.", Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onSyncSuccess(String ResPonseRs) {
                                            Toast.makeText(getContext(), "Tải dữ liệu thành công.", Toast.LENGTH_SHORT).show();
                                            //((BaseActivity) getActivity()).dismissProgressDialog();
                                        }
                                        @Override
                                        public void onSyncFailer(Exception e) {
                                            Toast.makeText(getContext(), "Lỗi cập nhật nhân viên:" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                            //((BaseActivity) getActivity()).dismissProgressDialog();
                                        }
                                    });//.execute(ArrWard);
                                    oSyncUpdate.oDMEmp=ArrEmp;
                                    oSyncUpdate.execute();
                                }
                                break;
                            case "DM_SEASON":
                                type = new TypeToken<Collection<DM_Employee>>() {}.getType();
                                Collection<DM_Employee> enums8 = gson.fromJson(ResPonseRs, type);
                                DM_Season[] ArrSeason = enums8.toArray(new DM_Season[enums8.size()]);
                                if(ArrSeason!=null && ArrSeason.length>0){
                                    AsyncUpdate oSyncUpdate=new AsyncUpdate(mType,new SyncCallBack() {
                                        @Override
                                        public void onSyncStart() {
                                            Toast.makeText(getContext(), "Đang cập nhật dữ liệu.", Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onSyncSuccess(String ResPonseRs) {
                                            Toast.makeText(getContext(), "Tải dữ liệu thành công.", Toast.LENGTH_SHORT).show();
                                            //((BaseActivity) getActivity()).dismissProgressDialog();
                                        }
                                        @Override
                                        public void onSyncFailer(Exception e) {
                                            Toast.makeText(getContext(), "Lỗi cập nhật mùa vụ:" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                            //((BaseActivity) getActivity()).dismissProgressDialog();
                                        }
                                    });//.execute(ArrWard);
                                    oSyncUpdate.oDMSeason=ArrSeason;
                                    oSyncUpdate.execute();
                                }
                                break;
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        ((BaseActivity) getActivity()).dismissProgressDialog();

                    }
                }
            }

            @Override
            public void onHttpFailer(Exception e) {
                ((BaseActivity) getActivity()).dismissProgressDialog();
            }
        }, mUrlGet, mType).execute();
    }



    private void onSyncConfirm(String mtblName){
        final String mUrlConFirm=AppSetting.getInstance().URL_SyncConfirm(AppUtils.getImeil(getContext()),AppUtils.getImeilsim(getContext()),mtblName);
        new SyncGet(new APINetCallBack() {
            @Override
            public void onHttpStart() {
                //((BaseActivity) getActivity()).showProgressDialog("Đang tải dữ liệu khách hàng.");
                //Log.d("URL_SYNC_CUSTOMER",mUrlGet);
            }
            @Override
            public void onHttpSuccess(String ResPonseRs) {
               if(ResPonseRs!=null && !ResPonseRs.isEmpty() && ResPonseRs.equalsIgnoreCase("SYNC_OK")){
                   Toast.makeText(getContext(),"Xác nhận tải thành công",Toast.LENGTH_SHORT).show();
               }
            }
            @Override
            public void onHttpFailer(Exception e) {

            }
        }, mUrlConFirm, "CONFITM").execute();

    }

    //ASYNC BACKGROUDN DOWNLOAD CUSTOMER
    private class AsyncUpdate extends AsyncTask<Void,String,String> {
        private SyncCallBack mSyncCallBack;
        private Exception mException;
        private String mType="";
        private String mSyncTitle="";
        public HT_PARA[] oHT_Para;
        public DM_Product[] oDMProduct;
        public DM_Province[] oDMProvince;
        public DM_District[] oDMDistrict;
        public DM_Ward[] oDMWard;
        public DM_Customer[] oDMCus;
        public DM_Employee[] oDMEmp;
        public DM_Season[] oDMSeason;


        private AsyncUpdate(String isType,SyncCallBack mCallBack){
            this.mSyncCallBack=mCallBack;
            this.mType=isType;
        }

        @Override
        protected void onPreExecute(){
            ((BaseActivity) getActivity()).showProgressDialog2(mSyncTitle,"Đang xử lý dữ liệu");
        }
        @Override
        protected String doInBackground(Void...params){
            try{
                int iSize=0;
                int iSqno=1;
                switch (mType){
                    case "HT_PARA":
                        //HT_PARA[] aDATA = (HT_PARA[]) params[0];
                        mSyncTitle="Đang tải Tham số";
                        if(oHT_Para!=null) {
                            iSize = oHT_Para.length;
                            for (HT_PARA oPar : oHT_Para) {
                                if (!oPar.getParaCode().isEmpty()) {
                                    mDB.addHTPara(oPar);
                                    publishProgress("Tham số: [" + Integer.toString(iSqno) + "/" + Integer.toString(iSize) + "] " + oPar.getParaCode());
                                }
                                iSqno += 1;
                            }
                        }
                       break;

                    case "DM_PRODUCT":
                        //List<DM_Product> aPRODUCT = (ArrayList<DM_Product>) params[0];
                        //DM_Product[] aPRODUCT=(DM_Product[]) params[0];
                        mSyncTitle="Đang tải Sản phẩm";
                        if(oDMProduct!=null) {
                            iSize = oDMProduct.length;
                            for (DM_Product oPar : oDMProduct) {
                                if (!oPar.getProductCode().isEmpty()) {
                                    mDB.addProduct(oPar);
                                    publishProgress("Sản phẩm: [" + Integer.toString(iSqno) + "/" + Integer.toString(iSize) + "] " + oPar.getProductName());
                                }
                                iSqno += 1;
                            }
                        }
                     break;

                    case "DM_PROVINCE":
                        mSyncTitle="Đang tải Địa phương";
                        if(oDMProvince!=null) {
                            iSize = oDMProvince.length;
                            for (DM_Province oPar : oDMProvince) {
                                if (!oPar.getProvince().isEmpty()) {
                                    mDB.addProvince(oPar);
                                    publishProgress("Địa phương: [" + Integer.toString(iSqno) + "/" + Integer.toString(iSize) + "] " + oPar.getProvince());
                                }
                                iSqno += 1;
                            }
                        }
                        break;

                    case "DM_DISTRICT":
                        mSyncTitle="Đang tải Quận/Huyện";
                        if(oDMDistrict!=null) {
                            iSize = oDMDistrict.length;
                            for (DM_District oPar : oDMDistrict) {
                                if (!oPar.getDistrict().isEmpty()) {
                                    mDB.addDistrict(oPar);
                                    publishProgress("Quận/Huyện: [" + Integer.toString(iSqno) + "/" + Integer.toString(iSize) + "] " + oPar.getDistrict());
                                }
                                iSqno += 1;
                            }

                        }
                        break;

                    case "DM_WARD":
                        //List<DM_Ward> aWard = (ArrayList<DM_Ward>) params[0];
                        //DM_Ward[] aWard=(DM_Ward[]) params[0];
                        mSyncTitle="Đang tải Phường/Xã ";
                        iSize=oDMWard.length;
                        for(DM_Ward oPar:oDMWard){
                            if(!oPar.getWard().isEmpty()){
                                mDB.addWard(oPar);
                                publishProgress("Xã: ["+Integer.toString(iSqno)+"/"+Integer.toString(iSize)+"] "+  oPar.getWard());
                            }
                            iSqno+=1;
                        }
                        break;

                    case "DM_CUSTOMER":
                        //List<DM_Customer> aCus = (ArrayList<DM_Customer>) params[0];
                        //DM_Customer[] aCus=(DM_Customer[])params[0];
                        mSyncTitle="Đang tải khách hàng ";
                        if(oDMCus!=null) {
                            iSize = oDMCus.length;
                            for (DM_Customer oPar : oDMCus) {
                                if (!oPar.getCustomerid().isEmpty()) {
                                    mDB.addCustomer(oPar);
                                    publishProgress("Khách hàng: [" + Integer.toString(iSqno) + "/" + Integer.toString(iSize) + "] " + oPar.getCustomerName());
                                }
                                iSqno += 1;
                            }
                        }
                        break;

                    case "DM_EMPLOYEE":
                        mSyncTitle="Đang tải nhân viên";
                        if(oDMEmp!=null) {
                            iSize = oDMEmp.length;
                            for (DM_Employee oPar : oDMEmp) {
                                if (!oPar.getEmployeeid().isEmpty()) {
                                    mDB.addEmployee(oPar);
                                    publishProgress("Nhân viên: [" + Integer.toString(iSqno) + "/" + Integer.toString(iSize) + "] " + oPar.getEmployeeName());
                                }
                                iSqno += 1;
                            }
                        }
                        break;
                    case "DM_SEASON":
                        mSyncTitle="Đang tải mùa vụ";
                        if(oDMEmp!=null) {
                            iSize = oDMEmp.length;
                            for (DM_Employee oPar : oDMEmp) {
                                if (!oPar.getEmployeeid().isEmpty()) {
                                    mDB.addEmployee(oPar);
                                    publishProgress("Mùa vụ: [" + Integer.toString(iSqno) + "/" + Integer.toString(iSize) + "] " + oPar.getEmployeeName());
                                }
                                iSqno += 1;
                            }
                        }
                        break;
                }



            }catch (Exception ex){
                mException=ex;
                ex.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(String...values){
            ((BaseActivity) getActivity()).setTextProgressDialog2(mSyncTitle,values[0]);
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            ((BaseActivity) getActivity()).dismissProgressDialog();
            if(mSyncCallBack!=null){
                if(mException==null){
                    mSyncCallBack.onSyncSuccess(result);
                    onSyncConfirm(mType);
                }else{
                    mSyncCallBack.onSyncFailer(mException);
                }
            }
        }

    }



}


