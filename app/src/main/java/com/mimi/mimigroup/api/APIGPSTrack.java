package com.mimi.mimigroup.api;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.ui.login.LoginActivity;
import com.mimi.mimigroup.ui.main.MainActivity;

import java.util.List;
import java.util.Locale;


public class APIGPSTrack extends Service implements LocationListener{
    private final Context mContext;
    boolean checkGPS = false;
    boolean checkNetwork = false;
    boolean canGetLocation = false;
    Location mLoc;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 2;
    protected LocationManager locationManager;

    public APIGPSTrack(Context mContext) {
        this.mContext = mContext;
        getLocation();
    }

    private Criteria getCriteria(String mProvider){
        Criteria mCri=new Criteria();
        if(mProvider=="GPS"){
            mCri.setAccuracy(Criteria.ACCURACY_FINE);
        }else{
            mCri.setAccuracy(Criteria.ACCURACY_COARSE);
        }
        mCri.setSpeedRequired(false);
        mCri.setAltitudeRequired(false);
        mCri.setBearingRequired(false);
        mCri.setCostAllowed(false);
        mCri.setPowerRequirement(Criteria.POWER_HIGH);

        //API level 9 and up
        mCri.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        mCri.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        return mCri;
    }

    private String getLocationProvider() {
        final Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        //criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);

        criteria.setSpeedRequired(false);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        //criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);

        //API level 9 and up
        criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        //return criteria;
        return locationManager.getBestProvider(criteria, true);
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            // get GPS status
            checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // get network provider status
            checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!checkGPS && !checkNetwork) {
                Toast.makeText(mContext, "Thiết bị của bạn chưa bật định vị", Toast.LENGTH_SHORT).show();
                this.canGetLocation=false;
            } else {
                this.canGetLocation = true;

                String mProvider=getLocationProvider();
                // if GPS Enabled get lat/long using GPS Services
                if (checkGPS) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                           // ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 50);
                            ActivityCompat.requestPermissions((Activity)mContext, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 50);
                        }
                    }
                    //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    locationManager.requestLocationUpdates(MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,getCriteria("GPS"), this,null);
                    if (locationManager != null) {
                        mLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if ( mLoc != null) {
                            latitude =  mLoc.getLatitude();
                            longitude =  mLoc.getLongitude();
                            return   mLoc;
                        }
                    }
                }

                if (checkNetwork) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 50);
                            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 50);
                        }
                    }
                    //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    locationManager.requestLocationUpdates(MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,getCriteria("NET"), this,null);
                    if (locationManager != null) {
                        mLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                    if ( mLoc != null) {
                        latitude =  mLoc.getLatitude();
                        longitude =  mLoc.getLongitude();
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  mLoc;
    }

    public double getLongitude() {
        if ( mLoc != null) {
            longitude =  mLoc.getLongitude();
        }
        return longitude;
    }

    public double getLatitude() {
        if ( mLoc!= null) {
            latitude =  mLoc.getLatitude();
        }
        return latitude;
    }
    public Location getLngLat(){
        return  mLoc;
    }
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        final Dialog oDlg=new Dialog(mContext);
        oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        oDlg.setContentView(R.layout.dialog_yesno);
        oDlg.setTitle("");
        CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
        dlgTitle.setText("ĐỊNH VỊ");
        CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
        dlgContent.setText("Thiết bị chưa bật định vi! Bạn có muốn bật GPS ?");
        CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
        CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
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
    }


    public void stopListener() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 50);
                ActivityCompat.requestPermissions((Activity)mContext, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 50);
                //return;
            }
            locationManager.removeUpdates(APIGPSTrack.this);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onLocationChanged(Location location) {
        this. mLoc=location;
    }
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }
    @Override
    public void onProviderEnabled(String s) {
    }
    @Override
    public void onProviderDisabled(String s) {
    }

    public String getAddressLocation(Double Longitude,Double Latitude){
        try{
            List<Address> lstAddress;
            Geocoder gCoder=new Geocoder(this,Locale.getDefault());
            lstAddress=gCoder.getFromLocation(Latitude,Longitude,1);
            String mAddress=lstAddress.get(0).getAddressLine(0);
            //Log.d("Address",mAddress);
            return  mAddress;
        }catch (Exception ex){}
        return "N/A";
    }
}
