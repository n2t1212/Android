package com.mimi.mimigroup.ui.setting;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.ui.adapter.FragmentPagerTabAdapter;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
     FragmentPagerTabAdapter adapter;

    @BindView(R.id.btnSearchViewMap)
    FloatingActionButton btnSearchViewMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        adapter = new FragmentPagerTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        adapter.addFragment(new SearchQRFragment(), "QR quét trong ngày");
        //adapter.addFragment(new ProductGroupFragment(), "...");
    }

    public void setButtonVisible(int isTrueFalse){
        btnSearchViewMap.setVisibility(isTrueFalse);
    }


    @OnClick(R.id.ivBack)
    public void onBack(){
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @OnClick(R.id.btnSearchViewMap)
    public void onSearchViewMap(){
        final Fragment currentFragment = adapter.getItem(viewPager.getCurrentItem());
        if(currentFragment instanceof SearchQRFragment) {
            ((SearchQRFragment) currentFragment).onSearchViewMap();
        }
    }

    @OnClick(R.id.btnSearch)
    public void onDownload(){
       final Fragment currentFragment = adapter.getItem(viewPager.getCurrentItem());
        if(currentFragment instanceof SearchQRFragment){
            ((SearchQRFragment)currentFragment).onDownloadClicked(false);

            /*
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(SearchActivity.this);
            alertDialog2.setTitle("Xác nhận");
            alertDialog2.setMessage("Bạn có muốn tải tất cả danh mục ?");
            alertDialog2.setPositiveButton("Bỏ qua",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
            alertDialog2.setNeutralButton("Tất cả", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((SearchQRFragment)currentFragment).onDownloadClicked(true);
                }
            });
            alertDialog2.setNegativeButton("Tải mới", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((SearchQRFragment)currentFragment).onDownloadClicked(false);
                }
            });
            alertDialog2.show();
            */
        }

    }

}
