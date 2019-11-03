package com.mimi.mimigroup.ui.visitcard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
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
import com.mimi.mimigroup.model.SM_VisitCard;
import com.mimi.mimigroup.ui.adapter.VisitCardAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.ui.login.LoginActivity;
import com.mimi.mimigroup.ui.main.MainActivity;
import com.mimi.mimigroup.utils.AppUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class VisitCardFragment extends BaseFragment implements PopupMenu.OnMenuItemClickListener{

    @BindView(R.id.rvVisitCard)
    RecyclerView rvVisitCard;

    @BindView(R.id.btnVisitMenuList)
    FloatingActionButton btnVisitMenuList;

    VisitCardAdapter adapter;
    private DBGimsHelper mDB;

    String mfDay="";
    String mtDay="";


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new VisitCardAdapter(new VisitCardAdapter.ListItemClickListener() {
            @Override
            public void onItemClick(List<SM_VisitCard> SelectList) {
                if(SelectList.size()>0){
                    btnVisitMenuList.setVisibility(View.VISIBLE);
                }else{
                    btnVisitMenuList.setVisibility(View.INVISIBLE);
                }
            }
        });

        rvVisitCard.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDB=DBGimsHelper.getInstance(getContext());
        rvVisitCard.setAdapter(adapter);

        onLoadDataSource("","");
    }
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_visitcard;
    }

    private void onLoadDataSource(String fDay,String tDay){
        try {
            List<SM_VisitCard> lstVisitCard;
            if (tDay == "") {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                tDay = sdf.format(new Date());
                fDay = AppUtils.DateAdd(tDay, -7, "yyyy-MM-dd");
                tDay = AppUtils.DateAdd(tDay, 2, "yyyy-MM-dd");

                mfDay=fDay;
                mtDay=tDay;

                lstVisitCard = mDB.getAllVisitCard(fDay, tDay);
                adapter.setVisitCardList(lstVisitCard);
            }else{
                lstVisitCard = mDB.getAllVisitCard(fDay, tDay);
                adapter.setVisitCardList(lstVisitCard);
            }

            adapter.SelectedList.clear();
            btnVisitMenuList.setVisibility(View.INVISIBLE);
        }catch (Exception e){}
    }



    private static final int REQUEST_CODE_IN = 0;
    private static final int REQUEST_CODE_OUT = 1;

    @OnClick(R.id.btnVisitIn)
    public void onVisitInClicked(){
        String mVisitCardID="";
        List<SM_VisitCard> lstVisitSel=adapter.SelectedList;
        if(lstVisitSel.size()>1){
            Toast oToast=Toast.makeText(getContext(),"Bạn đang chọn nhiều hơn 1 mẫu tin",Toast.LENGTH_LONG);
            oToast.setGravity(Gravity.CENTER,0,0);
            oToast.show();
            adapter.SelectedList.clear();
            onLoadDataSource(mfDay,mtDay);
            return;
        }else if(lstVisitSel.size()==1){
            mVisitCardID=lstVisitSel.get(0).getVisitCardID();
            if(lstVisitSel.get(0).getVisitType().equalsIgnoreCase("OUT")){
                Toast oToast=Toast.makeText(getContext(),"Bạn đang chọn mẫu tin thuộc thẻ chấm Ra.",Toast.LENGTH_LONG);
                oToast.setGravity(Gravity.CENTER,0,0);
                oToast.show();
                return;
            }
        }

        Intent intent = new Intent(getContext(), VisitCardResultActivity.class);
        intent.setAction("IN");
        intent.putExtra("DATA_VISITCARD_ID", mVisitCardID);
        startActivityForResult(intent,REQUEST_CODE_IN);
    }

    @OnClick(R.id.btnVisitOut)
    public void onVisitOutClicked(){
        String mVisitCardID="";
        List<SM_VisitCard> lstVisitSel=adapter.SelectedList;
        if(lstVisitSel.size()>1){
            Toast oToast=Toast.makeText(getContext(),"Bạn đang chọn nhiều hơn 1 mẫu tin",Toast.LENGTH_LONG);
            oToast.setGravity(Gravity.CENTER,0,0);
            oToast.show();
            adapter.SelectedList.clear();
            onLoadDataSource(mfDay,mtDay);
            return;
        }else if(lstVisitSel.size()==1){
            mVisitCardID=lstVisitSel.get(0).getVisitCardID();
            if(lstVisitSel.get(0).getVisitType().equalsIgnoreCase("IN")){
                Toast oToast=Toast.makeText(getContext(),"Bạn đang chọn mẫu tin thuộc thẻ chấm Vào.",Toast.LENGTH_LONG);
                oToast.setGravity(Gravity.CENTER,0,0);
                oToast.show();
                return;
            }
        }
        Intent intent = new Intent(getContext(), VisitCardResultActivity.class);
        intent.setAction("OUT");
        intent.putExtra("DATA_VISITCARD_ID", mVisitCardID);
        //startActivity(intent);
        startActivityForResult(intent,REQUEST_CODE_OUT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IN) {
            //resultcode=0:Back,1:Save
            if (resultCode == 2001) {
                onLoadDataSource(mfDay,mtDay);
            }
        }
        if(requestCode==REQUEST_CODE_OUT){
            if (resultCode == 2001) {
                 onLoadDataSource(mfDay,mtDay);
            }
        }
    }


    //menulist
    @OnClick(R.id.btnVisitMenuList)
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
        popup.inflate(R.menu.mnu_visitcard);

        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // HistoryFragment oFrame=new HistoryFragment();
        switch (item.getItemId()) {
            case R.id.mnuSynVisit:
                final List<SM_VisitCard> lstVS0=adapter.SelectedList;
                if(lstVS0.size()<=0){
                    Toast.makeText(getContext(),"Bạn chưa chọn mẫu tin để đồng bộ.",Toast.LENGTH_LONG).show();
                    return false;
                }
                final Dialog oDlg=new Dialog(getContext());
                oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                oDlg.setContentView(R.layout.dialog_yesno);
                oDlg.setTitle("");
                CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
                dlgTitle.setText("XÁC NHẬN");
                CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
                dlgContent.setText("Bạn có chắc muốn đồng bộ thẻ thăm ?");
                CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
                CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPostVisit(lstVS0);
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

            case R.id.mnuDelVisit:
                final List<SM_VisitCard> lstVS=adapter.SelectedList;
                if(lstVS.size()<=0){
                    Toast.makeText(getContext(),"Bạn chưa chọn mẫu tin để xóa.",Toast.LENGTH_LONG).show();
                    return false;
                }
                final Dialog oDlg2=new Dialog(getContext());
                oDlg2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                oDlg2.setContentView(R.layout.dialog_yesno);
                oDlg2.setTitle("");
                CustomTextView dlgTitle2=(CustomTextView) oDlg2.findViewById(R.id.dlgTitle);
                dlgTitle2.setText("XÁC NHẬN");
                CustomTextView dlgContent2=(CustomTextView) oDlg2.findViewById(R.id.dlgContent);
                dlgContent2.setText("Bạn có chắc muốn xóa ?");
                CustomBoldTextView btnYes2=(CustomBoldTextView) oDlg2.findViewById(R.id.dlgButtonYes);
                CustomBoldTextView btnNo2=(CustomBoldTextView) oDlg2.findViewById(R.id.dlgButtonNo);

                btnYes2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (SM_VisitCard oVisit : lstVS) {
                            mDB.delVisitCard(oVisit);
                        }
                        onLoadDataSource(mfDay,mtDay);
                        Toast.makeText(getContext(),"Đã xóa mẫu tin thành công",Toast.LENGTH_SHORT).show();
                        oDlg2.dismiss();
                    }
                });
                btnNo2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        oDlg2.dismiss();
                        return;
                    }
                });
                oDlg2.show();
                return  true;

            default:
                return false;
        }
    }


    //POST VISIT CARD
    //POST QR_SCAN
    private void onPostVisit(final List<SM_VisitCard> lstVisit){
        try{
            if (APINet.isNetworkAvailable(getContext())==false){
                Toast.makeText(getContext(),"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
                return;
            }

            String Imei=AppUtils.getImeil(getContext());
            String ImeiSim=AppUtils.getImeilsim(getContext());

            if(ImeiSim.isEmpty()){
                ImeiSim=mDB.getParam("PAR_IMEISIM");
                if(ImeiSim.isEmpty()) {
                    Toast.makeText(getContext(), "Không đọc được số IMEISIM từ thiết bị cho việc đồng bộ. Kiểm tra Sim của bạn", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            if(Imei.isEmpty()){
                Imei=mDB.getParam("PAR_IMEI");
                if(ImeiSim.isEmpty()) {
                    Toast.makeText(getContext(), "Không đọc được số IMEI từ thiết bị cho việc đồng bộ.", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            ((BaseActivity) getActivity()).showProgressDialog("Đang thiết lập kết nối..");

            final FlagPost flagPost=new FlagPost(0,0);
            for (final SM_VisitCard oVisit:lstVisit) {
                if (oVisit != null && !oVisit.getVisitCardID().isEmpty()) {

                    final String mUrlPostQR = AppSetting.getInstance().URL_PostVisitCard();
                    try {
                        if (oVisit.getVisitType() == null || oVisit.getVisitType().isEmpty()) {
                            oVisit.setVisitType("");
                        }
                        if (oVisit.getVisitTime() == null || oVisit.getVisitTime().isEmpty()) {
                            oVisit.setVisitTime("");
                        }
                        if (oVisit.getLongitude() == null) {
                            oVisit.setLongitude(Double.valueOf(0));
                        }
                        if (oVisit.getLatitude() == null) {
                            oVisit.setLatitude(Double.valueOf(0));
                        }
                        if (oVisit.getLocationAddress() == null) {
                            oVisit.setLocationAddress("N/A");
                        }
                    } catch (Exception ex) {
                        Toast.makeText(getContext(), "Không tìm thấy dữ liệu thẻ thăm.." + ex.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    RequestBody DataBody = new FormBody.Builder()
                            .add("imei", Imei)
                            .add("imeisim", ImeiSim)
                            .add("visitcardid", oVisit.getVisitCardID())
                            .add("visittype", oVisit.getVisitType())
                            .add("visitday", oVisit.getVisitDay())

                            .add("customerid", oVisit.getCustomerID())
                            .add("visittime", oVisit.getVisitTime())
                            .add("longitude", Double.toString(oVisit.getLongitude()))
                            .add("latitude", Double.toString(oVisit.getLatitude()))
                            .add("locationaddress", oVisit.getLocationAddress())

                            .add("notes", "")
                            .add("represent", "")
                            .build();

                  SyncPost oSyncPost=  new SyncPost(new APINetCallBack() {

                        @Override
                        public void onHttpStart() {
                            //((BaseActivity) getActivity()).showProgressDialog("Đang thiết lập kết nối..");
                            flagPost.setSendPost(flagPost.getSendPost()+1);
                            //Log.d("SendSEQN",Integer.toString(flagPost.getSendPost()));
                        }

                        @Override
                        public void onHttpSuccess(String ResPonseRs) {
                            try {
                                flagPost.setReceivePost(flagPost.getReceivePost()+1);
                                //Log.d("ReceiveSEQN",Integer.toString(flagPost.getReceivePost()));

                                ((BaseActivity) getActivity()).dismissProgressDialog();
                                if (!ResPonseRs.isEmpty()) {
                                    if (ResPonseRs.contains("SYNC_OK")) {
                                        Toast.makeText(getContext(), "Đồng bộ thành công.", Toast.LENGTH_LONG).show();
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a",Locale.US);
                                        oVisit.setSyncDay(sdf.format(new Date()));
                                        oVisit.setSync(true);
                                        mDB.editVisitCard(oVisit);
                                    }else{
                                        Toast.makeText(getContext(), "Không thể đồng bộ "+ResPonseRs, Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (Exception ex) {
                                Toast.makeText(getContext(), "Không thể đồng bộ "+ex.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            // finish();

                            if(flagPost.getSendPost()==flagPost.getReceivePost()){
                                onLoadDataSource(mfDay,mtDay);
                                ((BaseActivity) getActivity()).dismissProgressDialog();
                            }
                        }

                        @Override
                        public void onHttpFailer(Exception e) {
                            ((BaseActivity) getActivity()).dismissProgressDialog();
                            Toast.makeText(getContext(), "Không thể đồng bộ:" + e.getMessage(), Toast.LENGTH_LONG).show();

                            flagPost.setReceivePost(flagPost.getReceivePost()+1);
                            if(flagPost.getSendPost()==flagPost.getReceivePost()){
                                onLoadDataSource(mfDay,mtDay);
                            }
                        }
                    }, mUrlPostQR, "POST_VISITCARD", DataBody);//.execute();

                    if(Build.VERSION.SDK_INT>=11) {
                        oSyncPost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }else{
                        oSyncPost.execute();
                    }
                }
            }

           // onLoadDataSource(mfDay,mtDay);

        }catch (Exception ex){
            Toast.makeText(getContext(),"Không thể đồng bộ:"+ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


}
