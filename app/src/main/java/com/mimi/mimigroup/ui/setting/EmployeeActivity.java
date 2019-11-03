package com.mimi.mimigroup.ui.setting;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.ui.adapter.FragmentPagerTabAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class EmployeeActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
     FragmentPagerTabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        adapter = new FragmentPagerTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        adapter.addFragment(new EmployeeFragment(), "Nhân Viên");
        //adapter.addFragment(new ProductGroupFragment(), "Nhóm sản phẩm");
    }


    @OnClick(R.id.ivBack)
    public void onBack(){
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @OnClick(R.id.btnEmpDownload)
    public void onDownload(){
       final Fragment currentFragment = adapter.getItem(viewPager.getCurrentItem());
        if(currentFragment instanceof EmployeeFragment){
            final Dialog oDlg=new Dialog(EmployeeActivity.this);
            oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            oDlg.setContentView(R.layout.dialog_yesnosync);
            oDlg.setTitle("");
            CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
            dlgTitle.setText("XÁC NHẬN");
            CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
            dlgContent.setText("Vui lòng chọn hình thức đồng bộ (Tải mới/Tải tất cả)?");
            CustomBoldTextView btnAll=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonAll);
            CustomBoldTextView btnNew=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNew);
            CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

            btnAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((EmployeeFragment)currentFragment).onDownloadClicked(true);
                    oDlg.dismiss();
                }
            });
            btnNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((EmployeeFragment)currentFragment).onDownloadClicked(false);
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
            /*
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(EmployeeActivity.this);
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
                    ((EmployeeFragment)currentFragment).onDownloadClicked(true);
                }
            });
            alertDialog2.setNegativeButton("Tải mới", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((EmployeeFragment)currentFragment).onDownloadClicked(false);
                }
            });
            alertDialog2.show();*/
        }

        //if(currentFragment instanceof ProductGroupFragment){
            //((ProductGroupFragment)currentFragment).onDownloadClicked();
        //}

    }

}
