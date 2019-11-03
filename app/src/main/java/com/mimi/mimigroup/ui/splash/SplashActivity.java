package com.mimi.mimigroup.ui.splash;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.ui.main.MainActivity;
import com.mimi.mimigroup.ui.server.ServerActivity;
import butterknife.BindView;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.sprogressbar)
    ProgressBar spProgressBar;
    //SquareProgressBar spProgressBar;

    @BindView(R.id.txtProgress)
    TextView txtProgress;

    int percent = 0;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        //spProgressBar.setImage(R.drawable.logo);
        //spProgressBar.setColor("#ffffff");
        //spProgressBar.setPercentStyle(new PercentStyle(Paint.Align.CENTER, 150, true));
        //spProgressBar.setPercentStyle(new PercentStyle(Paint.Align.CENTER, 150, true));

        handler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(mRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(mRunnable);
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            percent++;
            if (percent > 100) {
                handler.removeCallbacks(this);
                   if (AppSetting.getInstance().isRunnable()) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class), true);
                    } else {
                        startActivity(new Intent(SplashActivity.this, ServerActivity.class), true);
                    }
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    spProgressBar.setProgress(percent);
                    txtProgress.setText(percent + "%");
                    handler.postDelayed(this, 20);
                }

            }
     };



}