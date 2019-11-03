package com.mimi.mimigroup.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APINet {
    //GET network request
    public static String GET(OkHttpClient client, String url) throws IOException {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            client=new OkHttpClient.Builder().connectTimeout(10,TimeUnit.SECONDS).writeTimeout(3600,TimeUnit.SECONDS).readTimeout(3600,TimeUnit.SECONDS).build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch (IOException e){
            e.printStackTrace();
            Log.v("GET_HTTP_ERR",e.toString());
        }
        return  null ;
    }
    //POST network request

    public static String POST(OkHttpClient client, String url, RequestBody body) throws IOException {
        try{
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            client=new OkHttpClient.Builder().connectTimeout(10,TimeUnit.SECONDS).writeTimeout(3600,TimeUnit.SECONDS).readTimeout(3600,TimeUnit.SECONDS).build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch (IOException e) {
            e.printStackTrace();
            Log.v("POST_HTTP_ERR:",e.toString());
        }
        return  null;
    }



    public static boolean isNetworkAvailable(Context c) {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }catch (Exception ex){return false;}
    }

}
