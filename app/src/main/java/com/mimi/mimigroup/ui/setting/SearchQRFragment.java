package com.mimi.mimigroup.ui.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.mimi.mimigroup.model.KS_Search;
import com.mimi.mimigroup.ui.adapter.SearchAdapter;
import com.mimi.mimigroup.ui.custom.CustomEditText;
import com.mimi.mimigroup.utils.AppUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;

public class SearchQRFragment extends BaseFragment{
    @BindView(R.id.rvSearch)
    RecyclerView rvSearch;

    @BindView(R.id.filterSCusCode)
    CustomEditText filterSCusCode;
    @BindView(R.id.filterSCusName)
    CustomEditText filterSCusName;
    @BindView(R.id.filterSProvince)
    CustomEditText filterSProvince;
    @BindView(R.id.filterSEmployee)
    CustomEditText filterSEmployee;

    SearchAdapter adapter;
    private DBGimsHelper mDB;
    private List<KS_Search> lstSearch;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.search_qr_fragment;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDB=DBGimsHelper.getInstance(getContext());
        adapter=new SearchAdapter(new SearchAdapter.SearchItemClickListener() {
            @Override
            public void onSearchItemClick(List<KS_Search> SelectList) {
                if(SelectList.size()>0){
                    ((SearchActivity)getActivity()).setButtonVisible(View.VISIBLE);
                }else{
                    ((SearchActivity)getActivity()).setButtonVisible(View.INVISIBLE);
                }
            }
        });


        rvSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSearch.setAdapter(adapter);
        //onLoadDataSource();

        lstSearch=new ArrayList<KS_Search>();

        onDownloadClicked(true);

        filterSCusCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.mFilterCustomerCode=s.toString();
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        filterSCusName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.mFilterCustomerName=s.toString();
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        filterSProvince.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.mFilterProvince=s.toString();
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        filterSEmployee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.mFilterEmployeeName=s.toString();
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void onLoadDataSource(List<KS_Search> lstSearch){
        try{
            //List<DM_Product> lstProduct=mDB.getAllProduct();
            adapter.setSearchList(lstSearch);
            Toast.makeText(getContext(),"Có "+ Integer.toString(lstSearch.size())+" được nạp..",Toast.LENGTH_SHORT).show();
        }catch (Exception ex){}
    }



    private List<KS_Search> DBKSSearch=null;
    public  final void fAddKSSearch(KS_Search oSearch){
        DBKSSearch.add(oSearch);
    }

    //GỌI TỪ Nút Download ItemActivity
    public void onDownloadClicked(final boolean isAll){
        if (APINet.isNetworkAvailable(getContext())==false){
            Toast.makeText(getContext(),"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
            return;
        }

        if(lstSearch!=null) {
            lstSearch.clear();
        }
        lstSearch=new ArrayList<KS_Search>();

        final String mUrlGet=AppSetting.getInstance().URL_Search(AppUtils.getImeil(getContext()),AppUtils.getImeilsim(getContext()),"QR","","","");
        new SyncGet(new APINetCallBack() {
            @Override
            public void onHttpStart() {
                ((BaseActivity) getActivity()).showProgressDialog("Đang tải dữ liệu sản phẩm.");
                // Log.d("URL_SYNC_SEARCH",mUrlGet);
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
                        Type type = new TypeToken<Collection<KS_Search>>() {}.getType();
                        Collection<KS_Search> enums = gson.fromJson(ResPonseRs, type);
                        KS_Search[] ArrSearch = enums.toArray(new KS_Search[enums.size()]);

                        //Async Add Province
                        if ( ArrSearch!=null && ArrSearch.length>0) {
                            /*
                            int iSqno=1;
                            int iSize=ArrSearch.length;
                            for (KS_Search oItem : ArrSearch) {
                                if (!oItem.getCustomerName().isEmpty()) {

                                    lstSearch.add(oItem);
                                    //fAddKSSearch(oItem);
                                    ((BaseActivity) getActivity()).setTextProgressDialog("[" + Integer.toString(iSqno) + "/" + Integer.toString(iSize) + "] " + oItem.getCustomerName());

                                }
                                iSqno += 1;
                            }*/

                            new AsyncUpdate(new SyncCallBack() {
                                @Override
                                public void onSyncStart() {
                                    //DBKSSearch.clear();
                                    Toast.makeText(getContext(), "Đang cập nhật dữ liệu.", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onSyncSuccess(String ResPonseRs) {
                                    Toast.makeText(getContext(), "Tải dữ liệu thành công.", Toast.LENGTH_SHORT).show();
                                    ((BaseActivity) getActivity()).dismissProgressDialog();
                                     //onLoadDataSource(DBKSSearch);
                                    onLoadDataSource(lstSearch);
                                }
                                @Override
                                public void onSyncFailer(Exception e) {
                                    Toast.makeText(getContext(),"Lỗi cập nhật:"+e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                                    ((BaseActivity) getActivity()).dismissProgressDialog();
                                }
                            }).execute(ArrSearch);

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
        }, mUrlGet, "SEARCH_QR").execute();

    }

    public void onSearchViewMap(){
        try {
            List<KS_Search> oKSSearch=adapter.SelectedList;
            if (oKSSearch == null || oKSSearch.size()<=0) {
                Toast.makeText(getActivity(), "Bạn chưa chọn khách hàng", Toast.LENGTH_SHORT).show();
                return;
            }
            if (oKSSearch.size()>1) {
                Toast.makeText(getActivity(), "Bạn đang chọn nhiều hơn 1 mẫu tin", Toast.LENGTH_SHORT).show();
                adapter.SelectedList.clear();
                return;
            }

            if (oKSSearch.get(0).getLatitude() > 0 && oKSSearch.get(0).getLongitude() > 0) {
                String url = "http://www.google.com/maps/place/" + oKSSearch.get(0).getLatitude() + "," + oKSSearch.get(0).getLongitude();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                ((BaseActivity) getActivity()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                Toast.makeText(getActivity(), "Khách hàng chưa có tọa độ vị trí", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception ex){}
    }

    //ASYNC BACKGROUDN DOWNLOAD CUSTOMER
    private class AsyncUpdate extends AsyncTask<KS_Search[],String,String> {
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
        protected String doInBackground(KS_Search[]... params) {
            try {
                int iSize = params[0].length;
                int iSqno = 1;
                for (KS_Search oItem : params[0]) {
                    if (!oItem.getCustomerName().isEmpty()) {
                        lstSearch.add(oItem);
                        publishProgress("[" + Integer.toString(iSqno) + "/" + Integer.toString(iSize) + "] " + oItem.getCustomerName());
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

                } else {
                    mSyncCallBack.onSyncFailer(mException);
                }
            }
        }
    }


}
