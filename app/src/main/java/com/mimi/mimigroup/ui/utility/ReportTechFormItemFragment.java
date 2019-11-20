package com.mimi.mimigroup.ui.utility;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.DatePicker;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.base.BaseFragment;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.SM_ReportTech;
import com.mimi.mimigroup.ui.custom.CustomBoldEditText;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    CustomBoldEditText tvReceiverList;
    @BindView(R.id.tvNotes)
    CustomBoldEditText tvNotes;
    @BindView(R.id.tvIsStatus)
    CustomBoldTextView tvIsStatus;

    private DBGimsHelper mDB;
    private String mReportTechID="";
    private SM_ReportTech oReportTech;
    private DatePickerDialog dtPicker;

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
                tvReceiverList.setText(oReportTech.getReceiverList());
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
                oReportTech.setReceiverList(tvReceiverList.getText().toString());
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
