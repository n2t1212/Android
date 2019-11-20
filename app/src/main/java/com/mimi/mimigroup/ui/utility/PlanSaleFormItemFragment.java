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
import com.mimi.mimigroup.model.SM_PlanSale;
import com.mimi.mimigroup.ui.custom.CustomBoldEditText;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

public class PlanSaleFormItemFragment  extends BaseFragment {

    @BindView(R.id.tvPlanCode)
    CustomTextView tvPlanCode;
    @BindView(R.id.tvPlanDay)
    CustomBoldTextView tvPlanDay;

    @BindView(R.id.tvStartDay)
    CustomBoldEditText tvStartDay;
    @BindView(R.id.tvEndDay)
    CustomBoldEditText tvEndDay;

    @BindView(R.id.tvPlanName)
    CustomBoldEditText tvPlanName;
    @BindView(R.id.tvNotes)
    CustomBoldEditText tvNotes;
    @BindView(R.id.tvIsStatus)
    CustomBoldTextView tvIsStatus;

    private DBGimsHelper mDB;
    private String mReportTechID="";
    private SM_PlanSale oPlanSale;
    private DatePickerDialog dtPicker;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_sale_plan_form;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDB=DBGimsHelper.getInstance(getContext());

        PlanSaleFormActivity oActivity = (PlanSaleFormActivity) getActivity();
        oPlanSale = oActivity.getoPlanSale();

        if(oPlanSale != null && oPlanSale.getPlanId() != "")
        {
            if(oPlanSale.getPlanCode() != null)
            {
                tvPlanCode.setText(oPlanSale.getPlanCode());
            }
            if(oPlanSale.getPlanDay() != null)
            {
                tvPlanDay.setText(oPlanSale.getPlanDay());
            }
            if(oPlanSale.getStartDay() != null)
            {
                tvStartDay.setText(oPlanSale.getStartDay());
            }
            if(oPlanSale.getEndDay() != null)
            {
                tvEndDay.setText(oPlanSale.getEndDay());
            }
            if(oPlanSale.getPlanName() != null)
            {
                tvPlanName.setText(oPlanSale.getPlanName());
            }
            if(oPlanSale.getNotes() != null)
            {
                tvNotes.setText(oPlanSale.getNotes());
            }
            if(oPlanSale.getIsStatus() != null)
            {
                tvIsStatus.setText(oPlanSale.getIsStatus());
            }
        }
    }

    @OnClick(R.id.tvPlanDay)
    public void onPlanDay()
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
                        tvPlanDay.setText(dateString);
                    }
                }, year, month, day);

        dtPicker.getDatePicker().setCalendarViewShown(false);
        dtPicker.getDatePicker().setSpinnersShown(true);
        dtPicker.show();
    }

    @OnClick(R.id.tvStartDay)
    public void onStartDay()
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
                        tvStartDay.setText(dateString);
                    }
                }, year, month, day);

        dtPicker.getDatePicker().setCalendarViewShown(false);
        dtPicker.getDatePicker().setSpinnersShown(true);
        dtPicker.show();
    }

    @OnClick(R.id.tvEndDay)
    public void onEndDay()
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
                        tvEndDay.setText(dateString);
                    }
                }, year, month, day);

        dtPicker.getDatePicker().setCalendarViewShown(false);
        dtPicker.getDatePicker().setSpinnersShown(true);
        dtPicker.show();
    }

    public SM_PlanSale getSMPlanSale()
    {
        try {
            oPlanSale.setPlanCode(tvPlanCode.getText().toString());
            if(tvPlanDay.getText().toString().isEmpty()){
                oPlanSale.setPlanDay("");
            }else{
                oPlanSale.setPlanDay(tvPlanDay.getText().toString());
            }
            if(tvStartDay.getText().toString().isEmpty()){
                oPlanSale.setStartDay("");
            }else{
                oPlanSale.setStartDay(tvStartDay.getText().toString());
            }
            if(tvEndDay.getText().toString().isEmpty()){
                oPlanSale.setEndDay("");
            }else{
                oPlanSale.setEndDay(tvEndDay.getText().toString());
            }
            if(tvPlanName.getText().toString().isEmpty()){
                oPlanSale.setPlanName("");
            }else{
                oPlanSale.setPlanName(tvPlanName.getText().toString());
            }
            if(tvIsStatus.getText().toString().isEmpty()){
                oPlanSale.setIsStatus("");
            }else{
                oPlanSale.setIsStatus(tvIsStatus.getText().toString());
            }

        }catch (Exception ex){}
        return oPlanSale;
    }
}
