package com.mimi.mimigroup.ui.setting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.mimi.mimigroup.model.DM_Product;
import com.mimi.mimigroup.ui.adapter.ProductAdapter;
import com.mimi.mimigroup.utils.AppUtils;


import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;

public class ProductFragment extends BaseFragment{
    @BindView(R.id.rvProduct)
    RecyclerView rvProcuct;
    ProductAdapter adapter;
    private DBGimsHelper mDB;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_product;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ProductAdapter();
        mDB=DBGimsHelper.getInstance(getContext());

        rvProcuct.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProcuct.setAdapter(adapter);
        onLoadDataSource();

    }

    private void onLoadDataSource(){
        try{
            List<DM_Product> lstProduct=mDB.getAllProduct();
            adapter.setProductList(lstProduct);

            Toast.makeText(getContext(),"Có "+ Integer.toString(lstProduct.size())+" được nạp..",Toast.LENGTH_SHORT).show();
        }catch (Exception ex){}
    }



    //GỌI TỪ Nút Download ItemActivity
    public void onDownloadClicked(final boolean isAll){
        if (APINet.isNetworkAvailable(getContext())==false){
            Toast.makeText(getContext(),"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
            return;
        }

        final String mUrlGet=AppSetting.getInstance().URL_SyncDM(AppUtils.getImeil(getContext()),AppUtils.getImeilsim(getContext()),"DM_PRODUCT",isAll);
        new SyncGet(new APINetCallBack() {
            @Override
            public void onHttpStart() {
                ((BaseActivity) getActivity()).showProgressDialog("Đang tải dữ liệu sản phẩm.");
                Log.d("URL_SYNC_PRODUCT",mUrlGet);
            }

            @Override
            public void onHttpSuccess(String ResPonseRs) {
                if( ResPonseRs==null || ResPonseRs.toString().trim()==null || ResPonseRs.isEmpty()) {
                    Toast.makeText(getContext(),"Vui lòng kiểm tra kết nối mạng..",Toast.LENGTH_SHORT).show();
                    ((BaseActivity) getActivity()).dismissProgressDialog();
                }else if( ResPonseRs.equalsIgnoreCase("[]")){
                    ((BaseActivity) getActivity()).dismissProgressDialog();
                    Toast.makeText(getContext(), "Không tìm thấy dữ liệu mới..", Toast.LENGTH_SHORT).show();
                    return;
                }else if(ResPonseRs.contains("SYNC_REG: Thiết bị của bạn chưa được đăng ký")){
                    ((BaseActivity) getActivity()).dismissProgressDialog();
                    Toast.makeText(getContext(), "Thiết bị của bạn chưa đăng ký. Vui lòng đăng ký trước...", Toast.LENGTH_SHORT).show();
                    return;
                }else if(ResPonseRs.contains("SYNC_ACTIVE: Thiết bị của bạn đã đăng ký nhưng chưa kích hoạt")) {
                    ((BaseActivity) getActivity()).dismissProgressDialog();
                    Toast.makeText(getContext(), "Thiết bị của bạn đã đăng ký nhưng chưa kích hoạt từ server...", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    try {
                        //JSONObject json = new JSONObject(myResponse);
                        //json.getString("Para_Value");
                        //txtString.setText("First Name: "+json.getJSONObject("data").getString("first_name") + "\nLast Name: " + json.getJSONObject("data").getString("last_name"));

                        Gson gson = new Gson();
                        Type type = new TypeToken<Collection<DM_Product>>() {}.getType();
                        Collection<DM_Product> enums = gson.fromJson(ResPonseRs, type);
                        DM_Product[] ArrProduct = enums.toArray(new DM_Product[enums.size()]);

                        //Async Add Province
                        if ( ArrProduct!=null && ArrProduct.length>0) {
                            new AsyncUpdate(new SyncCallBack() {
                                @Override
                                public void onSyncStart() {
                                    if(isAll) {
                                        mDB.delProduct("", true);
                                    }
                                    Toast.makeText(getContext(), "Đang cập nhật dữ liệu.", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onSyncSuccess(String ResPonseRs) {
                                    Toast.makeText(getContext(), "Tải dữ liệu thành công.", Toast.LENGTH_SHORT).show();
                                    ((BaseActivity) getActivity()).dismissProgressDialog();
                                     onLoadDataSource();
                                }
                                @Override
                                public void onSyncFailer(Exception e) {
                                    Toast.makeText(getContext(),"Lỗi cập nhật:"+e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                                    ((BaseActivity) getActivity()).dismissProgressDialog();
                                }
                            }).execute(ArrProduct);
                        }else{
                            ((BaseActivity) getActivity()).dismissProgressDialog();
                            Toast.makeText(getContext(),"Không đọc được dữ liệu tải về",Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        ((BaseActivity) getActivity()).dismissProgressDialog();
                         Toast.makeText(getContext(),"Không đọc được dữ liệu tải về"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }


            @Override
            public void onHttpFailer(Exception e) {
                ((BaseActivity) getActivity()).dismissProgressDialog();
                Toast.makeText(getContext(),"Không đọc được dữ liệu tải về"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }, mUrlGet, "DWN_PRODUCT").execute();

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
    private class AsyncUpdate extends AsyncTask<DM_Product[],String,String> {
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
        protected String doInBackground(DM_Product[]... params) {
            try {
                int iSize = params[0].length;
                int iSqno = 1;
                for (DM_Product oItem : params[0]) {
                    if (!oItem.getProductCode().isEmpty()) {
                        mDB.addProduct(oItem);
                        publishProgress("[" + Integer.toString(iSqno) + "/" + Integer.toString(iSize) + "] " + oItem.getProductName());
                    }
                    iSqno += 1;
                }
            } catch (Exception ex) {
                mException = ex;
                ex.printStackTrace();
                publishProgress("ERR");
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if(values[0].toString().equalsIgnoreCase("ERR")){
                ((BaseActivity) getActivity()).dismissProgressDialog();
            }else {
                ((BaseActivity) getActivity()).setTextProgressDialog(values[0]);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (mSyncCallBack != null) {
                if (mException == null) {
                    mSyncCallBack.onSyncSuccess(result);
                    onSyncConfirm("DM_PRODUCT");
                } else {
                    mSyncCallBack.onSyncFailer(mException);
                }
            }
        }
    }


}
