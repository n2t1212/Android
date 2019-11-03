package com.mimi.mimigroup.api;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

public class APIGoogleTrack  {
    Context Ctx;
    private APILocationCallBack mAPILocationCallBack;
    private static final String TAG = "APIGoogleAPI";

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;


    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 500;

    public APIGoogleTrack(final APILocationCallBack mLocationCallBack, Context mCtx) {
        this.Ctx=mCtx;
        this.mAPILocationCallBack=mLocationCallBack;

        this.locationRequest = new LocationRequest();
        this.locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        this.locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(this.locationRequest);
        this.locationSettingsRequest = builder.build();

        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location currentLocation = locationResult.getLastLocation();
                if(currentLocation!=null) {
                    mAPILocationCallBack.onCurrentLocation(currentLocation);

                    //CallRemoveUpdate;
                    onRemoveUpdate();
                }
            }
        };

        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.Ctx);
        if (ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mCtx, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 50);

            return;
        }

        this.mFusedLocationClient.requestLocationUpdates(this.locationRequest,this.locationCallback, Looper.myLooper());
    }

    public LocationSettingsRequest getLocationSettingsRequest() {
        return this.locationSettingsRequest;
    }
    public void onRemoveUpdate() {
        this.mFusedLocationClient.removeLocationUpdates(this.locationCallback);
    }
}
