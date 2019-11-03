package com.mimi.mimigroup.ui.setting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.mimi.mimigroup.model.DM_District;
import com.mimi.mimigroup.ui.adapter.DistrictAdapter;
import com.mimi.mimigroup.utils.AppUtils;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;

import com.mimi.mimigroup.db.DBGimsHelper;

public class DistrictFragment extends BaseFragment implements ItemView{

    @BindView(R.id.rvDistrict)
    RecyclerView rvDistrict;
    DistrictAdapter adapter;

    private DBGimsHelper mDB;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_district;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new DistrictAdapter();
        mDB=DBGimsHelper.getInstance(getContext());
        rvDistrict.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDistrict.setAdapter(adapter);
        onLoadDataSource();
    }

    private void onLoadDataSource(){
        try{
            List<DM_District> lstDistrict=mDB.getAllDistrict();
            adapter.setDistrictList(lstDistrict);
            Toast.makeText(getContext(),"Có "+ Integer.toString(lstDistrict.size())+" dòng được nạp...",Toast.LENGTH_SHORT).show();
        }catch (Exception ex){Toast.makeText(getContext(),"Nạp dữ liệu:"+ex.getMessage().toString(),Toast.LENGTH_LONG).show();}
    }

    @Override
    public void onGetDataSuccess(Object datas) {
        ((BaseActivity) getActivity()).dismissProgressDialog();
        List<DM_District> districtList = (List<DM_District>) datas;
        adapter.setDistrictList(districtList);
    }

    @Override
    public void onError(String msg) {
        ((BaseActivity) getActivity()).dismissProgressDialog();
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    //GỌI TỪ Nút Download ItemActivity
    public void onDownloadClicked(final boolean isAll){
        if (APINet.isNetworkAvailable(getContext())==false){
            Toast.makeText(getContext(),"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
            return;
        }

        final String mUrlGet=AppSetting.getInstance().URL_SyncDM(AppUtils.getImeil(getContext()),AppUtils.getImeilsim(getContext()),"DM_DISTRICT",isAll);
        new SyncGet(new APINetCallBack() {
            @Override
            public void onHttpStart() {
                ((BaseActivity) getActivity()).showProgressDialog("Đang tải dữ liệu địa phương.");
                //Log.d("URL_SYNC_DISTRICT",mUrlGet);
            }

            @Override
            public void onHttpSuccess(String ResPonseRs) {
                if( ResPonseRs==null || ResPonseRs.toString().trim()==null || ResPonseRs.isEmpty()) {
                    ((BaseActivity) getActivity()).dismissProgressDialog();
                    Toast.makeText(getContext(),"Vui lòng kiểm tra kết nối mạng..",Toast.LENGTH_SHORT).show();
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
                    //Log.d("RESP_SYNC_DISTRICT",ResPonseRs);
                    try {
                        //JSONObject json = new JSONObject(myResponse);
                        //json.getString("Para_Value");
                        //txtString.setText("First Name: "+json.getJSONObject("data").getString("first_name") + "\nLast Name: " + json.getJSONObject("data").getString("last_name"));

                        Gson gson = new Gson();
                        Type type = new TypeToken<Collection<DM_District>>() {}.getType();
                        Collection<DM_District> enums = gson.fromJson(ResPonseRs, type);
                        DM_District[] ArrDistrict = enums.toArray(new DM_District[enums.size()]);

                        //Async Add Province
                        if ( ArrDistrict!=null &&  ArrDistrict.length>0) {
                            new DistrictFragment.AsyncUpdate(new SyncCallBack() {
                                @Override
                                public void onSyncStart() {
                                    if(isAll) {
                                        mDB.delDistrict("", true);
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
                            }).execute(ArrDistrict);
                        }else{
                            ((BaseActivity) getActivity()).dismissProgressDialog();
                            Toast.makeText(getContext(),"Không đọc được dữ liệu tải về",Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        onError(e.getMessage().toString());
                    }
                }
            }

            @Override
            public void onHttpFailer(Exception e) {
                onError(e.getMessage());
            }
        }, mUrlGet, "DWN_DISTRICT").execute();
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
    private class AsyncUpdate extends AsyncTask<DM_District[],String,String> {
        private SyncCallBack mSyncCallBack;
        private Exception mException;

        private AsyncUpdate(SyncCallBack mCallBack){
            this.mSyncCallBack=mCallBack;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            if(this.mSyncCallBack!=null) {
                this.mSyncCallBack.onSyncStart();
            }
        }
        @Override
        protected String doInBackground(DM_District[]...params){
            try{
                int iSize=params[0].length;
                int iSqno=1;
                for(DM_District oItem:params[0]){
                    if(!Integer.toString(oItem.getDistrictid()).isEmpty()){
                        mDB.addDistrict(oItem);
                        publishProgress("["+Integer.toString(iSqno)+"/"+Integer.toString(iSize)+"] "+  oItem.getDistrict());
                    }
                    iSqno+=1;
                }
            }catch (Exception ex){
                mException=ex;
                ex.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(String...values){
            ((BaseActivity) getActivity()).setTextProgressDialog(values[0]);
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(mSyncCallBack!=null){
                if(mException==null){
                    mSyncCallBack.onSyncSuccess(result);
                    onSyncConfirm("DM_DISTRICT");
                }else{
                    mSyncCallBack.onSyncFailer(mException);
                }
            }
        }
    }


}
