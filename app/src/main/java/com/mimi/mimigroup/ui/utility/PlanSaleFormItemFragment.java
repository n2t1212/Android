package com.mimi.mimigroup.ui.utility;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.base.BaseFragment;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.SM_PlanSale;
import com.mimi.mimigroup.ui.custom.CustomBoldEditText;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.utils.AppUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class PlanSaleFormItemFragment  extends BaseFragment {

    @BindView(R.id.tvPlanCode)
    CustomTextView tvPlanCode;
    @BindView(R.id.tvPlanDay)
    CustomTextView tvPlanDay;

    @BindView(R.id.tvStartDay)
    CustomTextView tvStartDay;
    @BindView(R.id.tvEndDay)
    CustomTextView tvEndDay;

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

        }
    }

    @OnClick(R.id.tvPlanDay)
    public void onPlanDay()
    {
        /*
        final Calendar cldr = Calendar.getInstance();
        int day= cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

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
        */
    }

    @OnClick(R.id.tvStartDay)
    public void onStartDay(){
        final Calendar cldr = Calendar.getInstance();
        int day= cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        dtPicker = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cldr.set(year,monthOfYear, dayOfMonth);
                        String dateString = sdf.format(cldr.getTime());

                        // Check start day >= now
                        String currentDay = sdf.format(new Date());
                        float check = AppUtils.datediff2(currentDay, dateString, "yyyy-MM-dd");

                        if(check > 0){
                            Toast.makeText(getContext(), "Ngày bắt đầu phải lớn hơn hoặc bằng ngày hiện tại..", Toast.LENGTH_SHORT).show();
                            tvStartDay.setText("");
                            tvStartDay.requestFocus();
                            return;
                        }

                        tvStartDay.setText(dateString);
                    }
                }, year, month, day);

        dtPicker.getDatePicker().setCalendarViewShown(false);
        dtPicker.getDatePicker().setSpinnersShown(true);
        dtPicker.show();
    }

    @OnClick(R.id.tvEndDay)
    public void onEndDay(){
        final Calendar cldr = Calendar.getInstance();
        int day= cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        dtPicker = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cldr.set(year,monthOfYear, dayOfMonth);
                        String dateString = sdf.format(cldr.getTime());

                        // Check end day >= start day
                        if(tvStartDay.getText() != null && !tvStartDay.getText().toString().equals(""))
                        {
                            String startDay = tvStartDay.getText().toString();
                            float check = AppUtils.datediff2(startDay, dateString, "yyyy-MM-dd");

                            if(check > 0){
                                Toast.makeText(getContext(), "Ngày kết thúc phải lớn hơn hoặc bằng từ ngày..", Toast.LENGTH_SHORT).show();
                                tvEndDay.setText("");
                                tvEndDay.requestFocus();
                                return;
                            }
                        }else{
                            Toast.makeText(getContext(), "Vui lòng chọn ngày bắt đầu..", Toast.LENGTH_SHORT).show();
                            tvEndDay.setText("");
                            tvStartDay.requestFocus();
                            return;
                        }



                        tvEndDay.setText(dateString);
                    }
                }, year, month, day);

        dtPicker.getDatePicker().setCalendarViewShown(false);
        dtPicker.getDatePicker().setSpinnersShown(true);
        dtPicker.show();
    }

    public SM_PlanSale getSMPlanSale(){
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
            if(tvNotes.getText().toString().isEmpty()){
                oPlanSale.setNotes("");
            }else{
                oPlanSale.setNotes(tvNotes.getText().toString());
            }

        }catch (Exception ex){}
        return oPlanSale;
    }
}
