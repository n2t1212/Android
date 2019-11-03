package com.mimi.mimigroup.api;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.IOException;
 import okhttp3.OkHttpClient;

//Para: Void1:Kiểu dữ liệu tham số truyền vô doInBackground
//Void2:Kiểu dữ liệu truyền vô hàm onProcessUpdate
//Void3: Kiểu dữ liệu trả vê truyền vô hàm onPostExcute

public class SyncGet extends AsyncTask<Void, Integer, String> {
    private  String mUrl;
    private  String mType;
    private APINetCallBack mHttpCallBack;
    private Exception mException;

    ProgressDialog pDialog;

    public SyncGet(APINetCallBack mCallBack, String Url, String isType){
        this.mUrl=Url;
        this.mType=isType;
        this.mHttpCallBack=mCallBack;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(mHttpCallBack!=null){
            mHttpCallBack.onHttpStart();
        }
    }

    @Override
    protected String doInBackground(Void... arg0) {
        try {
            OkHttpClient Getclient = new OkHttpClient();
            String ResponBody = APINet.GET(Getclient, mUrl);
            return ResponBody;
            /*
            Gson gson = new Gson();
            Type type = new TypeToken<Collection<ResHT_PARA>>() {}.getType();
            Collection<ResHT_PARA> enums = gson.fromJson(response, type);
            responseApis = enums.toArray(new ResHT_PARA[enums.size()]);
            */
        } catch (IOException e) {
            mException=e;
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer...values){

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Call HttpCallBack
        //Log.d("SYNC_RESULT",result);
        if(mHttpCallBack!=null){
            if(mException==null){
                mHttpCallBack.onHttpSuccess(result);
            }else{
                mHttpCallBack.onHttpFailer(mException);
            }
        }

    }


}
