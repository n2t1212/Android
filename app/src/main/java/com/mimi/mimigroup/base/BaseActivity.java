package com.mimi.mimigroup.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.mimi.mimigroup.R;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import rx.subscriptions.CompositeSubscription;

public class BaseActivity extends AppCompatActivity {

    protected CompositeSubscription subscriptions;
    private android.app.AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    public void startActivity(Intent intent, boolean isFinish){
        startActivity(intent);
        if (isFinish){
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        subscriptions.clear();
    }

    public void showProgressDialog(String mMsg){
        try {
            if (dialog == null) {
                dialog = new SpotsDialog.Builder().setCancelable(false).setContext(this).setMessage(mMsg).build();
            }
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }catch (Exception ex){}
    }
    public void setTextProgressDialog(String msg){
            if(dialog!=null && dialog.isShowing()){
                dialog.setMessage(msg);
            }else{
                showProgressDialog(msg);
            }
    }

    public void showProgressDialog2(String mTitle, String mMsg){
        try {
            if (dialog == null) {
                dialog = new SpotsDialog.Builder().setCancelable(false).setContext(this).setMessage(mMsg).build();
                dialog.setTitle(mTitle);
            }
            if (!dialog.isShowing()) {
                dialog.setTitle(mTitle);
                dialog.show();
            }
        }catch (Exception ex){}
    }
    public void setTextProgressDialog2(String mTitle,String msg){
        if(dialog!=null && dialog.isShowing()){
            dialog.setMessage(msg);
        }else{
            showProgressDialog2(mTitle, msg);

        }
    }

    public void dismissProgressDialog(){
        if (dialog != null && dialog.isShowing()){
            dialog.dismiss();
            dialog = null;
        }
    }

    public void addFragment(Fragment fragment, int layoutId){
       getSupportFragmentManager()
               .beginTransaction().add(layoutId, fragment).setCustomAnimations(R.anim.fab_scale_up, R.anim.fab_scale_down).commit();
    }

    public void replaceFragment(Fragment fragment, int layoutId){
        getSupportFragmentManager()
                .beginTransaction().replace(layoutId, fragment).setCustomAnimations(R.anim.fab_scale_up, R.anim.fab_scale_down).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment: getSupportFragmentManager().getFragments()){
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}

