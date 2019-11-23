package com.mimi.mimigroup.ui.utility;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.base.BaseFragment;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.DM_Employee;
import com.mimi.mimigroup.model.SM_ReportTech;
import com.mimi.mimigroup.ui.custom.CustomBoldEditText;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ReportTechFormItemFragment extends BaseFragment {

    @BindView(R.id.tvReportCode)
    CustomTextView tvReportCode;
    @BindView(R.id.tvReportDay)
    CustomBoldTextView tvReportDay;

    @BindView(R.id.tvReportName)
    CustomBoldEditText tvReportName;
    @BindView(R.id.tvLocationAddress)
    CustomBoldEditText tvLocationAddress;
    @BindView(R.id.tvReceiverList)
    CustomTextView tvReceiverList;
    @BindView(R.id.tvNotes)
    CustomBoldEditText tvNotes;
    @BindView(R.id.tvIsStatus)
    CustomBoldTextView tvIsStatus;

    private DBGimsHelper mDB;
    private String mReportTechID="";
    private SM_ReportTech oReportTech;
    private DatePickerDialog dtPicker;

    List<DM_Employee> lstEmp;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_report_tech_form;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDB=DBGimsHelper.getInstance(getContext());

        ReportTechFormActivity oActivity = (ReportTechFormActivity) getActivity();
        oReportTech = oActivity.getoReportTech();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                       lstEmp = mDB.getAllEmployee();
                        initDropdownEmp();
                        if(oReportTech != null && oReportTech.getReportTechId() != "")
                        {
                            if(oReportTech.getReportCode() != null)
                            {
                                tvReportCode.setText(oReportTech.getReportCode());
                            }
                            if(oReportTech.getReportName() != null)
                            {
                                tvReportName.setText(oReportTech.getReportName());
                            }
                            if(oReportTech.getReportDate() != null)
                            {
                                tvReportDay.setText(oReportTech.getReportDate());
                            }
                            if(oReportTech.getLocationAddress() != null)
                            {
                                tvLocationAddress.setText(oReportTech.getLocationAddress());
                            }
                            if(oReportTech.getReceiverList() != null)
                            {
                                tvReceiverList.setText(getListEmpName(oReportTech.getReceiverList()));
                                tvReceiverList.setTag(oReportTech.getReceiverList());
                            }else{
                                tvReceiverList.setText("");
                                tvReceiverList.setTag("");
                            }
                            if(oReportTech.getNotes() != null)
                            {
                                tvNotes.setText(oReportTech.getNotes());
                            }
                            if(oReportTech.getIsStatus() != null)
                            {
                                tvIsStatus.setText(oReportTech.getIsStatus());
                            }
                        }
                    }} ,300);
    }

    private String[] lstEmpSelect;
    private boolean[] lstEmpSelectChecked;
    private ArrayList<Integer> lstEmpSelectPos = new ArrayList<>();

    private void initDropdownEmp(){
        try{
            if(lstEmp==null){lstEmp=new ArrayList<DM_Employee>();}

            lstEmpSelect=new String[lstEmp.size()];
            lstEmpSelectChecked=new boolean[lstEmp.size()];

            for(int i=0;i<lstEmp.size();i++){
                lstEmpSelect[i]=lstEmp.get(i).getEmployeeName();
                lstEmpSelectChecked[i]=false;
            }
        }catch (Exception ex){}
    }

    private String getListEmpName(String mlstEmpSelect){
        try{
            String mEmpListName="";
            lstEmpSelectPos.clear();
            if(!mlstEmpSelect.isEmpty()){
                String[] mlstEmpId=mlstEmpSelect.split(",");
                for(int iP=0;iP<mlstEmpId.length;iP++){
                    for(int jP=0;jP<lstEmp.size();jP++){
                        if(lstEmp.get(jP).getEmployeeid().contains(mlstEmpId[iP])){
                            lstEmpSelectPos.add(jP);
                            mEmpListName=mEmpListName+lstEmp.get(jP).getEmployeeName();
                            if(iP!=mlstEmpId.length-1) {
                                mEmpListName =mEmpListName + ",";
                            }
                        }
                    }
                }
            }
            return  mEmpListName;
        }catch (Exception ex){return  "";}
    }

    @OnClick(R.id.tvReceiverList)
    public void onClickReceiverList(){
        try{
            Arrays.fill(lstEmpSelectChecked,false);
            for(int i=0;i<lstEmpSelectPos.size();i++){
                lstEmpSelectChecked[lstEmpSelectPos.get(i)]=true;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false);
            builder.setTitle("Chọn người gửi");
            builder.setMultiChoiceItems(lstEmpSelect,lstEmpSelectChecked, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                    if(isChecked){
                        lstEmpSelectPos.add(position);
                    }else{
                        lstEmpSelectPos.remove(Integer.valueOf(position));
                    }
                }
            });

            builder.setPositiveButton("Chấp nhận", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    tvReceiverList.setText("");
                    String mEmpSelectId="",mEmpSelectName="";
                    for (int i = 0; i<lstEmpSelectPos.size(); i++){
                        mEmpSelectId=mEmpSelectId+ lstEmp.get(lstEmpSelectPos.get(i)).getEmployeeid();
                        mEmpSelectName=mEmpSelectName+ lstEmp.get(lstEmpSelectPos.get(i)).getEmployeeName();
                        if(i!=lstEmpSelectPos.size()-1){
                            mEmpSelectId=mEmpSelectId+",";
                            mEmpSelectName=mEmpSelectName+",";
                        }
                        tvReceiverList.setText(mEmpSelectName);
                        tvReceiverList.setTag(mEmpSelectId);
                    }
                }
            });
            builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setNeutralButton("Bỏ chọn", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    for (int i = 0; i < lstEmpSelectChecked.length; i++) {
                        lstEmpSelectChecked[i] = false;
                        lstEmpSelectPos.clear();
                        tvReceiverList.setText("");
                    }
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

            dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_2);
            Button btnPositiveButton=((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            Button btnNegetiveButton=((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

            btnPositiveButton.setTextColor(getResources().getColor(R.color.ButtonDialogColor));
            btnPositiveButton.setBackgroundColor(getResources().getColor(R.color.ButtonDialogBackground));
            btnPositiveButton.setPaddingRelative(20,2,20,2);
            btnNegetiveButton.setTextColor(getResources().getColor(R.color.ButtonDialogColor2));

        }
        catch(Exception ex) { }

    }


    @OnClick(R.id.tvReportDay)
    public void onRequestDate()
    {
        final Calendar cldr = java.util.Calendar.getInstance();
        int day = cldr.get(java.util.Calendar.DAY_OF_MONTH);
        int month = cldr.get(java.util.Calendar.MONTH);
        int year = cldr.get(java.util.Calendar.YEAR);

        // date picker dialog
        dtPicker = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cldr.set(year,monthOfYear, dayOfMonth);
                        String dateString = sdf.format(cldr.getTime());
                        tvReportDay.setText(dateString);
                    }
                }, year, month, day);

        dtPicker.getDatePicker().setCalendarViewShown(false);
        dtPicker.getDatePicker().setSpinnersShown(true);
        dtPicker.show();
    }

    public SM_ReportTech getSMReportTech()
    {
        try {
            oReportTech.setReportCode(tvReportCode.getText().toString());
            oReportTech.setReportDate(tvReportDay.getText().toString());
            if(tvReportName.getText().toString().isEmpty()){
                oReportTech.setReportName("");
            }else{
                oReportTech.setReportName(tvReportName.getText().toString());
            }
            if(tvLocationAddress.getText().toString().isEmpty()){
                oReportTech.setLocationAddress("");
            }else{
                oReportTech.setLocationAddress(tvLocationAddress.getText().toString());
            }
            if(tvReceiverList.getText().toString().isEmpty()){
                oReportTech.setReceiverList("");
            }else{
                oReportTech.setReceiverList(tvReceiverList.getTag().toString());
            }
            if(tvNotes.getText().toString().isEmpty()){
                oReportTech.setNotes("");
            }else{
                oReportTech.setNotes(tvNotes.getText().toString());
            }
            if(tvIsStatus.getText().toString().isEmpty()){
                oReportTech.setIsStatus("");
            }else{
                oReportTech.setIsStatus(tvIsStatus.getText().toString());
            }

        }catch (Exception ex){}
        return oReportTech;
    }
}
