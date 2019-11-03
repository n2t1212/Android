package com.mimi.mimigroup.api;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class SyncPost extends AsyncTask<Void, String, String> {
    private  String mUrl;
    private  String mType;
    private RequestBody mFormBody;

    private APINetCallBack mHttpCallBack;
    private Exception mException;

    //ProgressDialog pDialog;

    public SyncPost(APINetCallBack mCallBack, String Url, String isType,RequestBody FormBody){
        this.mUrl=Url;
        this.mType=isType;
        this.mFormBody=FormBody;
        this.mHttpCallBack=mCallBack;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(mHttpCallBack!=null){
            mHttpCallBack.onHttpStart();
        }
       // Log.d("URL",mUrl);
    }

    @Override
    protected String doInBackground(Void... arg0) {
        try {
            OkHttpClient Postclient = new OkHttpClient();
            String ResponBody = APINet.POST(Postclient, mUrl,mFormBody);
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
    protected void onProgressUpdate(String...values){

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Call HttpCallBack
        if(mHttpCallBack!=null){
            if(mException==null){
                mHttpCallBack.onHttpSuccess(result);
            }else{
                mHttpCallBack.onHttpFailer(mException);
            }
        }
       // if (pDialog.isShowing())
       //     pDialog.dismiss();
    }
}