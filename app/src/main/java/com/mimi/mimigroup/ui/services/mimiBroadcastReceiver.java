package com.mimi.mimigroup.ui.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mimi.mimigroup.db.DBGimsHelper;

public class mimiBroadcastReceiver extends BroadcastReceiver {

    private DBGimsHelper mDB;

    @Override
    public void onReceive(Context context, Intent intent) {
        mDB=DBGimsHelper.getInstance(context);

        if (intent.getAction().equals("ORDER_STATUS")) {
            String msg=intent.getStringExtra("Data").toString();
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        }
    }
}
