package com.mimi.mimigroup.ui.order;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.api.APINet;
import com.mimi.mimigroup.api.APINetCallBack;
import com.mimi.mimigroup.api.SyncPost;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.base.BaseFragment;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.FlagPost;
import com.mimi.mimigroup.model.SM_Order;
import com.mimi.mimigroup.model.SM_OrderDetail;
import com.mimi.mimigroup.ui.adapter.OrderAdapter;
import com.mimi.mimigroup.ui.adapter.OrderTotalSaleAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.utils.AppUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

//CALL FROM MAIN ACTIVITI
public class OrderFragment extends BaseFragment  implements PopupMenu.OnMenuItemClickListener{

    @BindView(R.id.rvOrderList)
    RecyclerView rvOrderList;

    OrderAdapter adapter;
    private DBGimsHelper mDB;
    private String mfDay;
    private String mtDay;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new OrderAdapter();
        rvOrderList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDB=DBGimsHelper.getInstance(getContext());
        rvOrderList.setAdapter(adapter);
        onLoadDataSource("","");
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_order;
    }

    private void onLoadDataSource(String fDay,String tDay){
        try{
           ((BaseActivity)getActivity()).showProgressDialog("Đang nạp danh sách đơn đặt hàng...");
           if(tDay=="") {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fNow = sdf.format(new Date());
                fDay = AppUtils.DateAdd(fNow, -7, "yyyy-MM-dd");
                tDay = AppUtils.DateAdd(fNow, 1, "yyyy-MM-dd");
            }
            mfDay=fDay;
            mtDay=tDay;
            ((BaseActivity)getActivity()).showProgressDialog("Đang nạp danh sách đơn đặt hàng...");
            List<SM_Order> lstOder= mDB.getAllSMOrder(fDay,tDay);
            adapter.setOrderList(lstOder);
            ((BaseActivity) getActivity()).dismissProgressDialog();
        }catch (Exception ex){
           ((BaseActivity) getActivity()).dismissProgressDialog();
        }
    }

    public void onSyncUploadClicked(){
        if (APINet.isNetworkAvailable(getContext())==false){
            Toast.makeText(getContext(),"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
            return;
        }
        final String Imei=AppUtils.getImeil(getContext());
        final String ImeiSim=AppUtils.getImeilsim(getContext());
        if(ImeiSim.isEmpty() || Imei.isEmpty()){
            Toast.makeText(getContext(),"Không đọc được số IMEI từ thiết bị cho việc đồng bộ. Kiểm tra Sim của bạn",Toast.LENGTH_LONG).show();
            return;
        }else if(ImeiSim=="N/A"){
            Toast.makeText(getContext(),"Không đọc được số Serial SIM, Kiểm tra lại SIM của bạn.",Toast.LENGTH_LONG).show();
        }

        final List<SM_Order> lstOrder=adapter.SelectedList;
        if(lstOrder.size()>0){
            final String mUrlPostOrder=AppSetting.getInstance().URL_PostOrder();
            final Dialog oDlg=new Dialog(getContext());
            oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            oDlg.setContentView(R.layout.dialog_yesno);
            oDlg.setTitle("");
            CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
            dlgTitle.setText("XÁC NHẬN GỬI ĐƠN HÀNG");
            CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
            dlgContent.setText("Bạn có chắc muốn gửi đơn hàng ?");
            CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
            CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPostOrder(lstOrder,Imei,ImeiSim,mUrlPostOrder);
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

        }else{
            Toast.makeText(getContext(),"Bạn chưa chọn mẫu tin để tải lên.",Toast.LENGTH_SHORT).show();
        }
    }


    public void onViewDiliveryClicked(){
        List<SM_Order> lstOrder=adapter.SelectedList;
        if(lstOrder.size()>0) {
            for(SM_Order oOder:lstOrder) {
                Intent intent = new Intent(getContext(), OrderDeliveryActivity.class);
                try {
                    intent.putExtra("orderid", oOder.getOrderID());
                    intent.putExtra("ordercode", oOder.getOrderCode());
                    intent.putExtra("requestdate", oOder.getRequestDate());
                    intent.putExtra("customername", oOder.getCustomerName());
                }catch (Exception ex){}
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            }
        }else{
            Toast.makeText(getContext(),"Bạn chưa chọn mẫu tin để xem",Toast.LENGTH_SHORT).show();
        }
    }

    public void onSearchOrderClicked(){
        try{
            LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
            View customView = inflater.inflate(R.layout.dialog_picker, null);
            final DatePicker dpStartDate = (DatePicker) customView.findViewById(R.id.dpStartDate);
            final DatePicker dpEndDate = (DatePicker) customView.findViewById(R.id.dpEndDate);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(customView); // Set the view of the dialog to your custom layout
            builder.setTitle("");
            builder.setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startYear = dpStartDate.getYear();
                    startMonth = dpStartDate.getMonth();
                    startDay = dpStartDate.getDayOfMonth();
                    endYear = dpEndDate.getYear();
                    endMonth = dpEndDate.getMonth();
                    endDay = dpEndDate.getDayOfMonth();

                    Date dFday,dTday;
                    dFday=AppUtils.getDate(startYear,startMonth,startDay);
                    dTday=AppUtils.getDate(endYear,endMonth,endDay);

                    if(dFday.after(dTday)){
                       Toast.makeText(getContext(),"Chọn khoản thời gian không hợp lệ.",Toast.LENGTH_SHORT).show();
                       return;
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    mfDay=sdf.format(dFday);
                    mtDay=sdf.format(dTday);
                    mtDay=AppUtils.DateAdd(mtDay,1,"yyyy-MM-dd");

                    if(mfDay!="" && mtDay!="") {
                        onLoadDataSource(mfDay,mtDay);
                    }
                    dialog.dismiss();
                }});
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }});

            Dialog dialog=builder.create();
            dialog.show();

            dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialogdate);
            Button btnPositiveButton=((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            Button btnNegetiveButton=((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

            btnPositiveButton.setTextColor(getResources().getColor(R.color.ButtonDialogColor));
            btnPositiveButton.setBackgroundColor(getResources().getColor(R.color.ButtonDialogBackground));
            btnPositiveButton.setPaddingRelative(20,2,20,2);
            btnNegetiveButton.setTextColor(getResources().getColor(R.color.ButtonDialogColor2));

        }catch (Exception ex){}
    }



    public void onDoanhSoClicked(){
        try{
            LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
            View customView = inflater.inflate(R.layout.dialog_picker, null);
            final DatePicker dpStartDate = (DatePicker) customView.findViewById(R.id.dpStartDate);
            final DatePicker dpEndDate = (DatePicker) customView.findViewById(R.id.dpEndDate);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(customView); // Set the view of the dialog to your custom layout
            builder.setTitle("");
            builder.setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startYear = dpStartDate.getYear();
                    startMonth = dpStartDate.getMonth();
                    startDay = dpStartDate.getDayOfMonth();
                    endYear = dpEndDate.getYear();
                    endMonth = dpEndDate.getMonth();
                    endDay = dpEndDate.getDayOfMonth();

                    Date dFday,dTday;
                    dFday=AppUtils.getDate(startYear,startMonth,startDay);
                    dTday=AppUtils.getDate(endYear,endMonth,endDay);

                    if(dFday.after(dTday)){
                        Toast.makeText(getContext(),"Chọn khoản thời gian không hợp lệ.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    mfDay=sdf.format(dFday);
                    mtDay=sdf.format(dTday);
                    mtDay=AppUtils.DateAdd(mtDay,1,"yyyy-MM-dd");

                    if(mfDay!="" && mtDay!="") {
                        Intent intent = new Intent(getContext(), OrderTotalSalesActivity.class);
                        try {
                            intent.putExtra("mfday", mfDay);
                            intent.putExtra("mtday", mtDay);
                        }catch (Exception ex){}
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }

                    dialog.dismiss();
                }});
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }});

            Dialog dialog=builder.create();
            dialog.show();

            dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialogdate);
            Button btnPositiveButton=((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            Button btnNegetiveButton=((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

            btnPositiveButton.setTextColor(getResources().getColor(R.color.ButtonDialogColor));
            btnPositiveButton.setBackgroundColor(getResources().getColor(R.color.ButtonDialogBackground));
            btnPositiveButton.setPaddingRelative(20,2,20,2);
            btnNegetiveButton.setTextColor(getResources().getColor(R.color.ButtonDialogColor2));

        }catch (Exception ex){}
    }





    final int REQUEST_CODE_ADD=1;
    final int REQUEST_CODE_EDIT=2;
    boolean isEditAdd=false;
    public void onAddOrderClicked(){
        String mParSymbol=mDB.getParam("PAR_SYMBOL");
        if (mParSymbol==null || mParSymbol.isEmpty()){mParSymbol="MT";}
        SimpleDateFormat Od = new SimpleDateFormat("ddMMyyyyHHmmssSS");
        String mOrderID = "DH"+mParSymbol+Od.format(new Date());
        if(!mOrderID.isEmpty()) {
            Intent intent = new Intent(getContext(), OrderFormActivity.class);
            intent.setAction("ADD");
            intent.putExtra("OrderID", mOrderID);
            intent.putExtra("PAR_SYMBOL", mParSymbol);
            startActivityForResult(intent, REQUEST_CODE_ADD);
            isEditAdd = true;
        }else{
            Toast oT= Toast.makeText(getContext(),"Không thể tạo Mã đơn hàng..", Toast.LENGTH_SHORT);
            oT.setGravity(Gravity.CENTER,0,0);
            oT.show();
        }
    }

    public void onEditOrderClicked(){
        try {
            List<SM_Order> oOrderSel = adapter.SelectedList;
            if (oOrderSel == null || oOrderSel.size() <= 0) {
                Toast.makeText(getActivity(), "Bạn chưa chọn đơn hàng", Toast.LENGTH_SHORT).show();
                return;
            }
            if (oOrderSel.size() > 1) {
                Toast.makeText(getActivity(), "Bạn chọn quá nhiều đơn hàng để sửa. Vui lòng chọn lại...", Toast.LENGTH_SHORT).show();
                adapter.clearSelected();
                return;
            }
            if(oOrderSel.get(0).getOrderStatus().equals(4)){
                Toast.makeText(getActivity(), "Đơn hàng đã kết thúc, Không thể sửa...", Toast.LENGTH_SHORT).show();
                return;
            }
            if (oOrderSel.get(0).getOrderID() != "") {
                String mParSymbol=mDB.getParam("PAR_SYMBOL");
                if (mParSymbol==null || mParSymbol.isEmpty()){mParSymbol="MT";}
                Intent intent = new Intent(getContext(), OrderFormActivity.class);
                intent.setAction("EDIT");
                intent.putExtra("OrderID", oOrderSel.get(0).getOrderID());
                intent.putExtra("PAR_SYMBOL", mParSymbol);
                startActivityForResult(intent,REQUEST_CODE_EDIT);
                isEditAdd=true;
            } else {
                Toast.makeText(getContext(), "Bạn chưa chọn đơn hàng.", Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Toast.makeText(getContext(), "ERR."+ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean  onDelOrderClicked(){
        final List<SM_Order> lstOder=adapter.SelectedList;
        if(lstOder.size()<=0){
            Toast.makeText(getContext(),"Bạn chưa chọn mẫu tin để xóa.",Toast.LENGTH_LONG).show();
            return false;
        }
        final Dialog oDlg=new Dialog(getContext());
        oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        oDlg.setContentView(R.layout.dialog_yesno);
        oDlg.setTitle("");
        CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
        dlgTitle.setText("XÁC NHẬN XÓA");
        CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
        dlgContent.setText("Bạn có chắc muốn xóa ?");
        CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
        CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (SM_Order oOder : lstOder) {
                    mDB.delSMOrder(oOder);
                }
                onLoadDataSource(mfDay,mtDay);
                Toast.makeText(getContext(),"Đã xóa mẫu tin thành công",Toast.LENGTH_SHORT).show();
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
        return  true;
    }

    @OnClick(R.id.btnOrderMenuList)
    public void onMenuClick(View v){
        String POPUP_CONSTANT = "mPopup";
        String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
        PopupMenu popup = new PopupMenu(getContext(), v);
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
        popup.inflate(R.menu.mnu_order);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
       // HistoryFragment oFrame=new HistoryFragment();
        switch (item.getItemId()) {
            case R.id.mnuAdd:
                onAddOrderClicked();
                return true;

            case R.id.mnuEdit:
                onEditOrderClicked();
                return  true;

            case R.id.mnuDel:
                onDelOrderClicked();
                return  true;

            case R.id.mnuDelivery:
                onViewDiliveryClicked();
                return  true;

            case R.id.mnuSynPost:
                onSyncUploadClicked();
                return  true;

            case R.id.mnuSearch:
                onSearchOrderClicked();
                return true;

            case R.id.mnuTotalSales:
                onDoanhSoClicked();
                return true;

            default:
                return false;
        }
    }


    //TÌM KIẾM THEO NGÀY
    private int startYear, startMonth, startDay, endYear, endMonth, endDay;

    public void showDatePicker() {
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        View customView = inflater.inflate(R.layout.dialog_picker, null);
        final DatePicker dpStartDate = (DatePicker) customView.findViewById(R.id.dpStartDate);
        final DatePicker dpEndDate = (DatePicker) customView.findViewById(R.id.dpEndDate);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(customView); // Set the view of the dialog to your custom layout
        builder.setTitle("Chọn khoản thời gian");
        builder.setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startYear = dpStartDate.getYear();
                startMonth = dpStartDate.getMonth();
                startDay = dpStartDate.getDayOfMonth();
                endYear = dpEndDate.getYear();
                endMonth = dpEndDate.getMonth();
                endDay = dpEndDate.getDayOfMonth();
                dialog.dismiss();
            }});
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
            }});
        builder.create().show();
    }


    private String getOrderDetailPost(String mOrderID){
       String mOrderDetail="";
        try{
           List<SM_OrderDetail> lstOrderDetail=mDB.getAllSMOrderDetail(mOrderID);
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

    //POST QR_SCAN
    private void onPostOrder(final List<SM_Order> lstOder,String mImei,String mImeiSim, final String mUrlPost){
        Integer iSyncCount=0;
        try{
           final FlagPost flagPost = new FlagPost(0, 0);
             for (final SM_Order oOd : lstOder) {
                 if (!oOd.getOrderID().isEmpty() && !oOd.getCustomerID().isEmpty()) {
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
                           oOd.setLongitude(0.0);
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
                        Toast.makeText(getContext(), "Không tìm thấy dữ liệu đã quét.." + ex.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    String mOrderDetail=getOrderDetailPost(oOd.getOrderID());

                    RequestBody DataBody = new FormBody.Builder()
                            .add("imei", mImei)
                            .add("imeisim", mImeiSim)
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
                            .add("orderdetail", mOrderDetail)

                            .build();

                     iSyncCount+=1;
                    new SyncPost(new APINetCallBack() {
                        @Override
                        public void onHttpStart() {
                            Toast.makeText(getContext(), "Đang đóng gói dữ liệu...", Toast.LENGTH_SHORT).show();
                            flagPost.setSendPost(flagPost.getSendPost()+1);
                        }

                        @Override
                        public void onHttpSuccess(String ResPonseRs) {
                            try {
                                flagPost.setReceivePost(flagPost.getReceivePost()+1);
                                if (ResPonseRs!=null && !ResPonseRs.isEmpty()) {
                                    if (ResPonseRs.contains("SYNC_OK")) {
                                        Toast.makeText(getContext(), "Gửi đơn hàng thành công.", Toast.LENGTH_LONG).show();
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                        oOd.setPostDay(sdf.format(new Date()));
                                        oOd.setPost(true);
                                        oOd.setOrderStatus(2);
                                        mDB.editSMOrder(oOd);
                                    }
                                    else if(ResPonseRs.contains("SYNC_REG") || ResPonseRs.contains("SYNC_NOT_REG")){
                                        Toast.makeText(getContext(), "Thiết bị chưa được đăng ký hoặc chưa xác thực từ Server.", Toast.LENGTH_LONG).show();
                                    }else if(ResPonseRs.contains("SYNC_ACTIVE")) {
                                        Toast.makeText(getContext(), "Thiết bị chưa kích hoạt...", Toast.LENGTH_LONG).show();
                                    }else if(ResPonseRs.contains("SYNC_APPROVE") || ResPonseRs.contains("SYNC_APPROVE")){
                                        Toast.makeText(getContext(), "Đơn hàng đang được xử lý. Bạn không thể gửi điều chỉnh.", Toast.LENGTH_LONG).show();
                                    }else if (ResPonseRs.contains("SYNC_BODY_NULL")) {
                                        Toast.makeText(getContext(), "Tham số gửi lên BODY=NULL", Toast.LENGTH_LONG).show();
                                    } else if (ResPonseRs.contains("SYNC_ORDERID_NULL")) {
                                        Toast.makeText(getContext(), "Mã số ORDERID=NULL", Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(getContext(), "Không nhận được trang thải trả về.", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception ex) {
                                return;
                            }
                            if(flagPost.getSendPost()==flagPost.getReceivePost()){
                                onLoadDataSource(mfDay,mtDay);
                            }
                            // finish();
                        }
                        @Override
                        public void onHttpFailer(Exception e) {
                            Toast.makeText(getContext(), "Không thể đồng bộ:" + e.getMessage(), Toast.LENGTH_LONG).show();

                            flagPost.setReceivePost(flagPost.getReceivePost()+1);
                            if(flagPost.getSendPost()==flagPost.getReceivePost()){
                                onLoadDataSource(mfDay,mtDay);
                            }
                        }
                    }, mUrlPost, "POST_ORDER", DataBody).execute();

                }
            }
        }catch (Exception ex){
            Toast.makeText(getContext(),"Không thể đồng bộ:"+ex.getMessage(),Toast.LENGTH_LONG).show();
        }

        if(iSyncCount==0){
            Toast.makeText(getContext(),"Không có mẫu tin chưa đồng bộ",Toast.LENGTH_LONG).show();
        }
        adapter.SelectedList.clear();
    }

}
