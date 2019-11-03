package com.mimi.mimigroup.ui.help;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpActivity extends BaseActivity {

    @BindView(R.id.wvHelp)
    WebView wvHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        onLoadGuide();
    }

    @OnClick(R.id.ivBack)
    public void onBack(){
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    public void onLoadGuide(){
        try {
            wvHelp.setWebViewClient(new WebViewClient());
            wvHelp.getSettings().setJavaScriptEnabled(true);
            String folderPath = "file:android_asset/html/";
            String mFileName = "mimihelp.html";
            wvHelp.loadUrl(folderPath + mFileName);
        }catch (Exception ex){}
    }
}
