package com.mimi.mimigroup.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppUtils {
    public static String getImeil(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return telephonyManager.getDeviceId();
    }

    public static String getImeilsim(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            String mImeiS = telephonyManager.getSimSerialNumber();
            if (mImeiS == null || mImeiS.isEmpty()) {
                return "N/A";
            }
            return mImeiS;
        }catch (Exception ex){return "N/A";}
    }

    public static  boolean isServerReachable(Context context, String url) {
        ConnectivityManager connMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL urlServer = new URL(url);
                HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
                urlConn.setConnectTimeout(3000); //<- 3Seconds Timeout
                urlConn.connect();
                if (urlConn.getResponseCode() == 200) {
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException e1) {
                return false;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }


    public static float datediff(String date1, String date2) {
        try {
            long DAY = 24 * 3600 * 1000;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date d1=sdf.parse(date1);
            Date d2=sdf.parse(date2);
            float days = (float) ((d1.getTime() - d2.getTime()) / DAY);
            return  days;
            // return Math.abs(days);
        }catch (Exception ex){return 0;}
    }

    public static float datediff2(String date1, String date2,String sFormat) {
        try {
            long DAY = 24 * 3600 * 1000;
            SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
            Date d1=sdf.parse(date1);
            Date d2=sdf.parse(date2);
            float days = (float) ((d1.getTime() - d2.getTime()) / DAY);
            return  days;
            // return Math.abs(days);
        }catch (Exception ex){return 0;}
    }

    public static String DateAdd(String curDate, int numberOfDays,String  FormatType) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(FormatType);
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(dateFormat.parse(curDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.add(Calendar.DAY_OF_YEAR, numberOfDays);
            dateFormat = new SimpleDateFormat(FormatType);
            Date newDate = new Date(c.getTimeInMillis());
            String resultDate = dateFormat.format(newDate);
            return resultDate;
        }catch (Exception ex){return curDate;}
    }

    public static Date getDate(int year, int month, int day) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        }catch (Exception ex) {return new Date();}
    }

    public static String getMoneyFormat(String amount,String UnitType) {
        try{
            if(UnitType=="USD") {
                DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
                return formatter.format(Double.parseDouble(amount));
            }else{
                DecimalFormat formatter = new DecimalFormat("###,###,###,##0");
                return formatter.format(Double.parseDouble(amount));
            }
        }catch (Exception ex){return  amount;}

    }
}
