package com.mimi.mimigroup.ui.utility;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.DatePicker;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.base.BaseFragment;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.SM_ReportSaleRep;
import com.mimi.mimigroup.ui.custom.CustomBoldEditText;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

public class ReportSaleRepFormItemFragment extends BaseFragment {

    @BindView(R.id.tvReportCode)
    CustomTextView tvReportCode;
    @BindView(R.id.tvReportDay)
    CustomBoldTextView tvReportDay;

    @BindView(R.id.tvReportName)
    CustomBoldEditText tvReportName;
    @BindView(R.id.tvLocationAddress)
    CustomBoldEditText tvLocationAddress;
    @BindView(R.id.tvReceiverList)
    CustomBoldEditText tvReceiverList;
    @BindView(R.id.tvNotes)
    CustomBoldEditText tvNotes;
    @BindView(R.id.tvIsStatus)
    CustomBoldTextView tvIsStatus;

    private DBGimsHelper mDB;
    private String mReportSaleRepID="";
    private SM_ReportSaleRep oReportSaleRep;
    private DatePickerDialog dtPicker;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_report_sale_form;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDB=DBGimsHelper.getInstance(getContext());

        ReportSaleRepFormActivity oActivity = (ReportSaleRepFormActivity) getActivity();
        oReportSaleRep = oActivity.getoReportSaleRep();

        if(oReportSaleRep != null && oReportSaleRep.getReportSaleId() != "")
        {
            if(oReportSaleRep.getReportCode() != null)
            {
                tvReportCode.setText(oReportSaleRep.getReportCode());
            }
            if(oReportSaleRep.getReportName() != null)
            {
                tvReportName.setText(oReportSaleRep.getReportName());
            }
            if(oReportSaleRep.getReportDay() != null)
            {
                tvReportDay.setText(oReportSaleRep.getReportDay());
            }
            if(oReportSaleRep.getLocationAddress() != null)
            {
                tvLocationAddress.setText(oReportSaleRep.getLocationAddress());
            }
            if(oReportSaleRep.getReceiverList() != null)
            {
                tvReceiverList.setText(oReportSaleRep.getReceiverList());
            }
            if(oReportSaleRep.getNotes() != null)
            {
                tvNotes.setText(oReportSaleRep.getNotes());
            }
            if(oReportSaleRep.getIsStatus() != null)
            {
                tvIsStatus.setText(oReportSaleRep.getIsStatus());
            }
        }
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

    public SM_ReportSaleRep getSMReportSaleRep()
    {
        try {
            oReportSaleRep.setReportCode(tvReportCode.getText().toString());
            oReportSaleRep.setReportDay(tvReportDay.getText().toString());
            if(tvReportName.getText().toString().isEmpty()){
                oReportSaleRep.setReportName("");
            }else{
                oReportSaleRep.setReportName(tvReportName.getText().toString());
            }
            if(tvLocationAddress.getText().toString().isEmpty()){
                oReportSaleRep.setLocationAddress("");
            }else{
                oReportSaleRep.setLocationAddress(tvLocationAddress.getText().toString());
            }
            if(tvReceiverList.getText().toString().isEmpty()){
                oReportSaleRep.setReceiverList("");
            }else{
                oReportSaleRep.setReceiverList(tvReceiverList.getText().toString());
            }
            if(tvNotes.getText().toString().isEmpty()){
                oReportSaleRep.setNotes("");
            }else{
                oReportSaleRep.setNotes(tvNotes.getText().toString());
            }
            if(tvIsStatus.getText().toString().isEmpty()){
                oReportSaleRep.setIsStatus("");
            }else{
                oReportSaleRep.setIsStatus(tvIsStatus.getText().toString());
            }

        }catch (Exception ex){}
        return oReportSaleRep;
    }
}
