package com.mimi.mimigroup.ui.setting;

import android.app.Dialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.ui.adapter.FragmentPagerTabAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.OnClick;

public class CustomerActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.btnFastGetLocation)
    FloatingActionButton btnFastGetLocation;

    FragmentPagerTabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppSetting.getInstance().isAndroidVersion5()) {
            setContentView(R.layout.activity_tabmain_customer);
        }else{
            setContentView(R.layout.activity_tabmain_customer21);
        }

        adapter = new FragmentPagerTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        adapter.addFragment(new CustomerFragment(), "Khách hàng");
        //adapter.addFragment(new NCCFragment(), "Nhà cung cấp");
    }

    public void setButtonVisible(int isTrueFalse){
        btnFastGetLocation.setVisibility(isTrueFalse);
    }


    @OnClick(R.id.ivBack)
    public void onBack(){
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @OnClick(R.id.btnFastGetLocation)
    public void onFastGetLocationClicked(){
        final Fragment currentFragment = adapter.getItem(viewPager.getCurrentItem());
        if(currentFragment instanceof CustomerFragment){
            final String mUrlPostOrder=AppSetting.getInstance().URL_PostOrder();
            final Dialog oDlg=new Dialog(CustomerActivity.this);
            oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            oDlg.setContentView(R.layout.dialog_yesno);
            oDlg.setTitle("");
            CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
            dlgTitle.setText("XÁC NHẬN");
            CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
            dlgContent.setText("Bạn có muốn chấm lại tọa độ ?");
            CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
            CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CustomerFragment)currentFragment).onGetLocationClicked(true);
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
    }

    @OnClick(R.id.btnMenuList)
    public void onMenuClick(View v){
        String POPUP_CONSTANT = "mPopup";
        String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";

        PopupMenu popup = new PopupMenu(CustomerActivity.this, v);
        try{
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(POPUP_CONSTANT)) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(POPUP_FORCE_SHOW_ICON, boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        }catch (Exception ex){}
        popup.setOnMenuItemClickListener(this);

        popup.inflate(R.menu.mnu_customer);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        //Toast.makeText(this, "Selected Item: " +item.getTitle(), Toast.LENGTH_SHORT).show();
        final Fragment currentFragment = adapter.getItem(viewPager.getCurrentItem());
        switch (item.getItemId()) {
            case R.id.mnuAdd:
                if(currentFragment instanceof CustomerFragment){
                    ((CustomerFragment)currentFragment).onAddCustomerClicked();
                }
                return true;

            case R.id.mnuEdit:
                if(currentFragment instanceof CustomerFragment){
                    ((CustomerFragment)currentFragment).onEditCustomerClicked();
                }
                return true;

            case R.id.mnuDel:
                if(currentFragment instanceof CustomerFragment){
                    ((CustomerFragment)currentFragment).onDelCustomerClicked();
                }
                return true;

            case R.id.mnuDown:
                if(currentFragment instanceof CustomerFragment){
                    final Dialog oDlg=new Dialog(CustomerActivity.this);
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
                            ((CustomerFragment)currentFragment).onDownloadClicked(true);
                            oDlg.dismiss();
                        }
                    });
                    btnNew.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((CustomerFragment)currentFragment).onDownloadClicked(false);
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
                return true;

            case R.id.mnuUpload:
                if(currentFragment instanceof CustomerFragment){
                    ((CustomerFragment)currentFragment).onUploadClicked();
                }
                return true;

            case R.id.mnuViewMap:
                if(currentFragment instanceof CustomerFragment){
                    ((CustomerFragment)currentFragment).onViewMapClicked();
                }
                return true;

            default:
                return false;
        }
    }



}
