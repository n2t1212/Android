package com.mimi.mimigroup.ui.order;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.api.APINet;
import com.mimi.mimigroup.api.APINetCallBack;
import com.mimi.mimigroup.api.SyncPost;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.DM_Customer_Search;
import com.mimi.mimigroup.model.DM_Product;
import com.mimi.mimigroup.model.DM_Tree;
import com.mimi.mimigroup.model.SM_Order;
import com.mimi.mimigroup.model.SM_OrderDetail;
import com.mimi.mimigroup.ui.adapter.FragmentPagerTabAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.utils.AppUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class OrderFormActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
     FragmentPagerTabAdapter adapter;

    @BindView(R.id.btnOrderDetailAdd)
    FloatingActionButton btnOrderDetailAdd;
    @BindView(R.id.btnOrderDetailDel)
    FloatingActionButton btnOrderDetailDel;

     public String mOrderID="";
     private String mPar_Symbol;
     private String mAction="";
     private DBGimsHelper mDB;
     private boolean isSaved=false;

     SM_Order oOrder;
     List<SM_OrderDetail> oOrderDetail;
     OrderFormItemFragment OrderFragment;
     OrderFormItemDetailFragment OrderDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);
        mDB=DBGimsHelper.getInstance(this);

        adapter = new FragmentPagerTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        //adapter.addFragment(new OrderFormItemFragment(), "Đơn Hàng");
        //adapter.addFragment(new OrderFormItemDetailFragment(), "Chi Tiết Đơn Hàng");
        mOrderID  = getIntent().getStringExtra("OrderID");
        mPar_Symbol  = getIntent().getStringExtra("PAR_SYMBOL");
        mAction=getIntent().getAction().toString();

        if(mAction=="EDIT"){
            oOrder=mDB.getSMOrder(mOrderID);
            oOrderDetail=mDB.getAllSMOrderDetail(mOrderID);
            if(oOrder!=null){
                oOrder.setOrderStatus(1);
            }
        }else{
            oOrder = new SM_Order();
            oOrderDetail=new ArrayList<SM_OrderDetail>();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String mOrderDate = sdf.format(new Date());
            String mRequestDate= AppUtils.DateAdd(mOrderDate, 3, "yyyy-MM-dd");

            SimpleDateFormat sdfOrderCode = new SimpleDateFormat("ddMMyy");
            String mOrderCodeDMY = sdfOrderCode.format(new Date());
            SimpleDateFormat sdfhhMMss = new SimpleDateFormat("hhmmss");
            String mOrderCodeHMS = sdfhhMMss.format(new Date());
            String mOrderCode=mPar_Symbol+'-'+mOrderCodeHMS+'/'+mOrderCodeDMY;

            oOrder.setOrderID(mOrderID);
            oOrder.setOrderCode(mOrderCode);
            oOrder.setOrderDate(mOrderDate);
            oOrder.setRequestDate(mRequestDate);
            oOrder.setOrderStatus(0);
            oOrder.setPost(false);
            oOrder.setSample(false);
        }
        btnOrderDetailAdd.setVisibility(View.GONE);
        btnOrderDetailDel.setVisibility(View.GONE);
        new android.os.Handler().postDelayed(
          new Runnable() {
              public void run() {
                 OrderFragment = new OrderFormItemFragment();
                 adapter.addFragment(OrderFragment, "Đơn Hàng");

                 OrderDetailFragment = new OrderFormItemDetailFragment();
                 adapter.addFragment(OrderDetailFragment, "Chi Tiết Đơn Hàng");

                  tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                      @Override
                      public void onTabSelected(TabLayout.Tab tab) {
                          switch (tab.getPosition()){
                              case 0:
                                  btnOrderDetailAdd.setVisibility(View.INVISIBLE);
                                  btnOrderDetailDel.setVisibility(View.INVISIBLE);
                                  break;
                              case 1:
                                  btnOrderDetailAdd.setVisibility(View.VISIBLE);
                                  btnOrderDetailDel.setVisibility(View.INVISIBLE);
                                  btnOrderDetailAdd.setTag("ADD");
                                  break;
                          }
                      }
                      @Override
                      public void onTabUnselected(TabLayout.Tab tab) {}

                      @Override
                      public void onTabReselected(TabLayout.Tab tab) {}
                  });

              }
          },300);


    }

    /*[TRANSFER DATA FOR FRAMGMENT]*/
    public void setVisibleDetailDelete(boolean isVisible){
        try {
            if (isVisible) {
                btnOrderDetailDel.setVisibility(View.VISIBLE);
            } else {
                btnOrderDetailDel.setVisibility(View.INVISIBLE);
            }
        }catch (Exception ex){}
    }
    public void setButtonEditStatus(boolean isEdit){
        if(isEdit){
            btnOrderDetailAdd.setTag("EDIT");
            btnOrderDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_edit));
        }else{
            btnOrderDetailAdd.setTag("ADD");
            btnOrderDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_add));
        }
    }

    public String getAction(){return this.mAction;}
    public String getOrderID(){
        return  this.mOrderID;
    }

    public String getParSymbol(){return  this.mPar_Symbol;}

    public SM_Order getOrderActivity(){
        return  this.oOrder;
    }
    public List<SM_OrderDetail> getOrderDetailActivity(){
        return  this.oOrderDetail;
    }

    public DM_Product getProduct(String mProductCode){
        DM_Product oProduct=new DM_Product();
        try{
            oProduct=mDB.getProduct(mProductCode);
        }catch (Exception ex){}
        return  oProduct;
    }
    public List<DM_Product> getListProduct(){
        List<DM_Product> lstProduct=new ArrayList<DM_Product>();
        try{
            lstProduct=mDB.getAllProduct();
        }catch (Exception ex){}
        return  lstProduct;
    }

    public List<DM_Tree> getListTree(){
        List<DM_Tree> lstTree=new ArrayList<DM_Tree>();
        try{
            lstTree=mDB.getAllTree();
        }catch (Exception ex){}
        return  lstTree;
    }

    public List<DM_Customer_Search> getListCustomerSearch(){
        List<DM_Customer_Search> lstCustomer=new ArrayList<DM_Customer_Search>();
        try{
            lstCustomer=mDB.getAllCustomerSearch();
        }catch (Exception ex){}
        return  lstCustomer;
    }
    /*[END]*/

    private void ReceiveDataFragment(){
        //SAVE DETAIL
        try{
            if(btnOrderDetailAdd.getTag()!=null && btnOrderDetailAdd.getTag().toString().equalsIgnoreCase("SAVE")) {
                onOrderDetailAdd();
            }
        }catch (Exception ex){}

        if(OrderFragment!=null) {
            oOrder = OrderFragment.getSMOrder();
        }
        if(OrderDetailFragment!=null) {
            oOrderDetail = OrderDetailFragment.getListOrderDetail();
        }
    }

    @Override
    public void onBackPressed() {
        if (!isSaved) {
            final Dialog oDlg=new Dialog(this);
            oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            oDlg.setContentView(R.layout.dialog_yesno);
            oDlg.setTitle("");
            CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
            dlgTitle.setText("THÔNG BÁO");
            CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
            dlgContent.setText("Dữ liệu đơn hàng chưa cập nhật. Bạn có muốn bỏ qua ?");
            CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
            CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onExcuteBackPress();
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

        } else {
            super.onBackPressed();
        }
    }

    private void onExcuteBackPress(){
        super.onBackPressed();
    }
    @OnClick(R.id.ivBack)
    public void onBack(){
        if (!isSaved) {
            final Dialog oDlg=new Dialog(this);
            oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            oDlg.setContentView(R.layout.dialog_yesno);
            oDlg.setTitle("");
            CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
            dlgTitle.setText("THÔNG BÁO");
            CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
            dlgContent.setText("Dữ liệu đơn hàng chưa cập nhật. Bạn có muốn bỏ qua ?");
            CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
            CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onExcuteBackPress();
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

            return;
        } else {
            super.onBackPressed();
        }

        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

  //[CALL SAVE & DEL ORDER DETAIL]
    //CALL FLOAT BUTTON ADD & SAVE
    @OnClick(R.id.btnOrderDetailAdd)
    public void onOrderDetailAdd(){
        final Fragment currentFragment = adapter.getItem(viewPager.getCurrentItem());
        if(currentFragment instanceof OrderFormItemDetailFragment){
            if(btnOrderDetailAdd.getTag()!=null && btnOrderDetailAdd.getTag().toString().equalsIgnoreCase("SAVE")){
                if(((OrderFormItemDetailFragment) currentFragment).onSaveOrderDetail()) {
                    btnOrderDetailAdd.setTag("ADD");
                    btnOrderDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_add));
                }
            }else if(btnOrderDetailAdd.getTag()!=null && btnOrderDetailAdd.getTag().toString().equalsIgnoreCase("EDIT")){
                //CHI HIỂN THỊ BOX ĐỂ CẬP NHẬT
                ((OrderFormItemDetailFragment) currentFragment).onAddOrderDetail(false);
                btnOrderDetailAdd.setTag("SAVE");
                btnOrderDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }else {
                //HIỂN THỊ BOX MỚI ĐỂ THÊM
               ((OrderFormItemDetailFragment) currentFragment).onAddOrderDetail(true);
                btnOrderDetailAdd.setTag("SAVE");
                btnOrderDetailAdd.setImageDrawable(getResources().getDrawable(R.drawable.tiva_accept));
            }
        }
    }

    @OnClick(R.id.btnOrderDetailDel)
    public void onOrderDetailDel(){
        final Fragment currentFragment = adapter.getItem(viewPager.getCurrentItem());
        if(currentFragment instanceof OrderFormItemDetailFragment){
            ((OrderFormItemDetailFragment) currentFragment).onDeletedOrderDetail();
        }
    }
    //[END CALL]



    @OnClick(R.id.btnSaveOrder)
    public void onSaveOnly(){
        ReceiveDataFragment();
        if(oOrder==null || oOrder.getOrderID().isEmpty()){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập đơn hàng..", Toast.LENGTH_SHORT).show();
            return;
        }
        if(oOrderDetail==null || oOrderDetail.size()<=0){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập chi tiết đơn hàng..", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oOrder.getCustomerID().isEmpty()) {
            Toast.makeText(this, "Bạn chưa chọn khách hàng..", Toast.LENGTH_SHORT).show();
            return;
        }
        mDB.addSMOrder(oOrder);
        if(mDB.getSizeSMOrder(oOrder.getOrderID())>0) {
            mDB.delAllSMOrderDetail(oOrder.getOrderID());
            for (SM_OrderDetail oDetail : oOrderDetail) {
                mDB.addSMOrderDetail(oDetail);
            }
        }
        Toast.makeText(this, "Ghi đơn hàng thành công", Toast.LENGTH_SHORT).show();
        setResult(2001);
        finish();
        isSaved=true;
    }


    @OnClick(R.id.btnPostOrder)
    public void onPostOrder(){
         ReceiveDataFragment();

        if (APINet.isNetworkAvailable(OrderFormActivity.this)==false){
            Toast.makeText(OrderFormActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
            return;
        }
        if(oOrder==null || oOrder.getOrderID().isEmpty()){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập đơn hàng..", Toast.LENGTH_SHORT).show();
            return;
        }
        if(oOrderDetail==null || oOrderDetail.size()<=0){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập chi tiết đơn hàng..", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oOrder.getOrderCode().isEmpty() || oOrderDetail.size()<=0) {
            Toast.makeText(this, "Không tìm thấy số đơn hàng hoặc chưa nhập chi tiết đơn hàng", Toast.LENGTH_SHORT).show();
        }else if (oOrder.getCustomerID().isEmpty()) {
            Toast.makeText(this, "Bạn chưa chọn khách hàng..", Toast.LENGTH_SHORT).show();
        }
        else{
            mDB.addSMOrder(oOrder);
            if(mDB.getSizeSMOrder(oOrder.getOrderID())>0) {
                for (SM_OrderDetail oDetail : oOrderDetail) {
                    mDB.addSMOrderDetail(oDetail);
                }
                onPostOrder(oOrder, oOrderDetail);
            }else{
                Toast.makeText(this, "Không thể ghi đơn hàng..", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        isSaved=true;
    }

    //POST ORDER
    private String getOrderDetailPost(List<SM_OrderDetail> lstOrderDetail){
        String mOrderDetail="";
        try{
            if(lstOrderDetail!=null){
                for (SM_OrderDetail oOdt : lstOrderDetail) {
                    String mRow="";
                    if(oOdt.getProductID()!=null && !oOdt.getProductID().isEmpty()){
                        mRow=oOdt.getProductID()+"#";
                        if(oOdt.getProductCode()!=null && !oOdt.getProductCode().isEmpty()){
                            mRow+=oOdt.getProductCode()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getUnit()!=null && !oOdt.getUnit().isEmpty()){
                            mRow+=oOdt.getUnit()+"#";
                        }else{
                            mRow+=""+"#";
                        }
                        if(oOdt.getAmount()!=null){
                            mRow+=oOdt.getAmount().toString()+"#";
                        }else{
                            mRow+="0"+"#";
                        }
                        if(oOdt.getAmountBox()!=null){
                            mRow+=oOdt.getAmountBox().toString()+"#";
                        }else{
                            mRow+="0"+"#";
                        }
                        if(oOdt.getPrice()!=null){
                            mRow+=oOdt.getPrice().toString()+"#";
                        }else{
                            mRow+="0"+"#";
                        }
                        if(oOdt.getNotes()!=null){
                            mRow+=oOdt.getNotes().toString()+"#";
                        }else{
                            mRow+="#";
                        }
                        if(oOdt.getNotes2()!=null){
                            mRow+=oOdt.getNotes2().toString()+"#";
                        }else{
                            mRow+="#";
                        }
                        mRow+="|";
                        mOrderDetail+=mRow;
                    }
                }
            }

        }catch (Exception ex){}
        return  mOrderDetail;
    }

    private void onPostOrder(final SM_Order oOd, final List<SM_OrderDetail> lstOrderDetail){
        try{
            if (APINet.isNetworkAvailable(OrderFormActivity.this)==false){
                Toast.makeText(OrderFormActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
                return;
            }

            final String Imei=AppUtils.getImeil(this);
            final String ImeiSim=AppUtils.getImeilsim(this);
            final String mDataOrderDetail=getOrderDetailPost(lstOrderDetail);

            if(ImeiSim.isEmpty()){
                Toast.makeText(this,"Không đọc được số IMEI từ thiết bị cho việc đồng bộ. Kiểm tra Sim của bạn",Toast.LENGTH_LONG).show();
                return;
            }
            if(oOd==null){
                Toast.makeText(this,"Không tìm thấy dữ liệu đơn hàng.",Toast.LENGTH_LONG).show();
                return;
            }
            if(oOd.getOrderID()==null || oOd.getOrderCode().isEmpty()){
                Toast.makeText(this,"Không tìm thấy mã đơn hàng",Toast.LENGTH_SHORT).show();
                return;
            }else if(oOd.getCustomerID()==null || oOd.getCustomerID().isEmpty()){
                Toast.makeText(this,"Bạn chưa chọn khách hàng cho đơn hàng này",Toast.LENGTH_SHORT).show();
                return;
            }

            final String mUrlPostOrder=AppSetting.getInstance().URL_PostOrder();
            try {
                if (oOd.getOrderCode() == null || oOd.getOrderCode().isEmpty()) {
                    oOd.setOrderCode("");
                }
                if (oOd.getRequestDate() == null || oOd.getRequestDate().isEmpty()) {
                    oOd.setRequestDate("");
                }
                if (oOd.getOrderDate() == null || oOd.getOrderDate().isEmpty()) {
                    oOd.setOrderDate("");
                }
                if (oOd.getMaxDebt() == null ) {
                    oOd.setMaxDebt(0);
                }
                if (oOd.getOrderNotes() == null || oOd.getOrderNotes().isEmpty()) {
                    oOd.setOrderNotes("");
                }

                if (oOd.getLatitude() == null || oOd.getLatitude().toString().isEmpty()) {
                    oOd.setLatitude(0.0);
                }
                if (oOd.getLongitude() == null || oOd.getLongitude().toString().isEmpty()) {
                    oOd.setLongitude(0.0);
                }
                if (oOd.getLocationAddress() == null || oOd.getLocationAddress().toString().isEmpty()) {
                    oOd.setLocationAddress("N/A");
                }
                if(oOd.getOrderStatus()==null){
                    oOd.setOrderStatus(1);
                }
            }catch(Exception ex){
                Toast.makeText(OrderFormActivity.this, "Không tìm thấy dữ liệu đã quét.." + ex.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            String isSample = "";
            if(oOd.getSample() != null){
                if(oOd.getSample().equals(true) || oOd.getSample().equals("1") || oOd.getSample()){
                    isSample = "MAU";
                }
            }

        RequestBody DataBody = new FormBody.Builder()
                .add("imei", Imei)
                .add("imeisim", ImeiSim)
                .add("customerid",oOd.getCustomerID())
                .add("orderid", oOd.getOrderID())
                .add("ordercode", oOd.getOrderCode())
                .add("orderdate", oOd.getOrderDate())
                .add("requestdate", oOd.getRequestDate())
                .add("orderstatus", Integer.toString(oOd.getOrderStatus()))
                .add("maxdebit", Integer.toString( oOd.getMaxDebt()))
                .add("longitude", Double.toString(oOd.getLongitude()))
                .add("latitude", Double.toString(oOd.getLatitude()))
                .add("locationaddress", oOd.getLocationAddress())
                .add("notes", oOd.getOrderNotes())
                .add("ordertype", isSample)
                .add("orderdetail",mDataOrderDetail)
                .build();
            new SyncPost(new APINetCallBack() {
                @Override
                public void onHttpStart() {
                    showProgressDialog("Đang đồng bộ dữ liệu về Server.");
                }
                @Override
                public void onHttpSuccess(String ResPonseRs) {
                    try {
                        dismissProgressDialog();
                        if (ResPonseRs!=null && !ResPonseRs.isEmpty()) {
                            if (ResPonseRs.contains("SYNC_OK")) {
                                Toast.makeText(OrderFormActivity.this, "Đồng  bộ thành công.", Toast.LENGTH_LONG).show();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                oOd.setPostDay(sdf.format(new Date()));
                                oOd.setPost(true);
                                oOd.setOrderStatus(2);
                                mDB.editSMOrder(oOd);
                                setResult(2001);
                                finish();
                            }
                            else if(ResPonseRs.contains("SYNC_REG") || ResPonseRs.contains("SYNC_NOT_REG")){
                                Toast.makeText(OrderFormActivity.this, "Thiết bị chưa được đăng ký hoặc chưa xác thực từ Server.", Toast.LENGTH_LONG).show();
                            }else if(ResPonseRs.contains("SYNC_ACTIVE")) {
                                Toast.makeText(OrderFormActivity.this, "Thiết bị chưa kích hoạt...", Toast.LENGTH_LONG).show();
                            }else if(ResPonseRs.contains("SYNC_APPROVE") || ResPonseRs.contains("SYNC_APPROVE")){
                                    Toast.makeText(OrderFormActivity.this, "Đơn hàng đang được xử lý. Bạn không thể gửi điều chỉnh.", Toast.LENGTH_LONG).show();
                            }else if (ResPonseRs.contains("SYNC_BODY_NULL")) {
                                Toast.makeText(OrderFormActivity.this, "Tham số gửi lên BODY=NULL", Toast.LENGTH_LONG).show();
                            } else if (ResPonseRs.contains("SYNC_ORDERID_NULL")) {
                                Toast.makeText(OrderFormActivity.this, "Mã số ORDERID=NULL", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(OrderFormActivity.this  , "Không nhận được trang thải trả về.", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception ex){ }
                    // finish();
                }

                @Override
                public void onHttpFailer(Exception e) {
                    dismissProgressDialog();
                    Toast.makeText(OrderFormActivity.this,"Không thể đồng bộ:"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            },mUrlPostOrder,"POST_ORDER",DataBody).execute();


        }catch (Exception ex){
            Toast.makeText(OrderFormActivity.this,"Không thể đồng bộ:"+ex.getMessage(),Toast.LENGTH_LONG).show();
            dismissProgressDialog();
        }
    }


}
