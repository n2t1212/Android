package com.mimi.mimigroup.api;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.mimi.mimigroup.model.DM_Customer_Distance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SyncFindCustomer extends AsyncTask<Void, Integer, List<DM_Customer_Distance>> {
    private SyncFindCustomerCallback mSyncCallBack;
    private Exception mException;
    private List<DM_Customer_Distance> mLstCus;
    private Double CLongitude;
    private Double CLatitude;
    private Float Scope;
    private int ReturnListSize;

    private float CalDistance(Double Lng1,Double Lat1, Double CusLng,Double CusLat){
        try{
            if (CusLat>0 && CusLng>0 && Lng1>0 && Lat1>0){
                Location locationA = new Location("A");
                locationA.setLatitude(Lat1);
                locationA.setLongitude(Lng1);
                Location locationB = new Location("B");
                locationB.setLatitude(CusLat);
                locationB.setLongitude(CusLng);
                return  (int)locationA.distanceTo(locationB);
            }
            return  0;
        }catch (Exception ex){return  0;}
    }

    public  SyncFindCustomer(SyncFindCustomerCallback SynCallB,List<DM_Customer_Distance> lstMCus,Double CLongitude,Double CLatitude,Float Scope,int ReturnListSize){
        this.mSyncCallBack=SynCallB;
        this.mLstCus=lstMCus;
        this.CLongitude=CLongitude;
        this.CLatitude=CLatitude;
        this.Scope=Scope;
        this.ReturnListSize=ReturnListSize;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(mSyncCallBack!=null){
            mSyncCallBack.onSyncStart();
        }
    }

    @Override
    protected List<DM_Customer_Distance> doInBackground(Void... arg0) {
        try {
            List<DM_Customer_Distance> lstCusSel = new ArrayList<DM_Customer_Distance>();
            for (DM_Customer_Distance oC:mLstCus){
                oC.setDistance(CalDistance(CLongitude,CLatitude,oC.getLongititude(),oC.getLatitude()));
            }

            Collections.sort(mLstCus);
            //Collections.reverse(lstCus);

            //Metter
            int iSqno=0;
            if(this.Scope>0){
                for (DM_Customer_Distance oCusSel:mLstCus){
                    if(oCusSel.getDistance()<=Scope){
                        lstCusSel.add(oCusSel);
                        //iSqno+=1;
                        //if(iSqno>=ReturnListSize){break;}
                    }
                    if(iSqno>=ReturnListSize){break;}
                    iSqno+=1;
                }
            }else{
                for (DM_Customer_Distance oCusSel:mLstCus){
                    lstCusSel.add(oCusSel);
                    if(iSqno>=ReturnListSize){break;}
                    iSqno+=1;
                }
            }

            return  lstCusSel;

        } catch (Exception e) {
            mException=e;
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer...values){}
    @Override
    protected void onPostExecute(List<DM_Customer_Distance> result) {
        super.onPostExecute(result);
        //Call HttpCallBack
        Log.d("SYNC_RESULT",result.toArray().toString());
        if(mSyncCallBack!=null){
            if(mException==null){
                mSyncCallBack.onSyncSuccess(result);
            }else{
               mSyncCallBack.onSyncFailer(mException);
            }
        }

    }

}
