package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.DM_Employee;
import com.mimi.mimigroup.model.SM_ReportSaleRep;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportSaleRepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<SM_ReportSaleRep> smReportSaleRepList;
    public List<SM_ReportSaleRep> SelectedList = new ArrayList<>();
    private DBGimsHelper mDB;

    public ReportSaleRepAdapter() {
        smReportSaleRepList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mDB = DBGimsHelper.getInstance(viewGroup.getContext());
        return new ReportSaleRepAdapter.ReportTechHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_report_sale_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        if (viewHolder instanceof ReportSaleRepAdapter.ReportTechHolder){
            if(this.smReportSaleRepList!=null && !this.smReportSaleRepList.isEmpty()) {
                ((ReportSaleRepAdapter.ReportTechHolder) viewHolder).bind(smReportSaleRepList.get(iPos));
            }
        }

        setRowSelected(SelectedList.contains(smReportSaleRepList.get(iPos)),smReportSaleRepList.get(iPos).getIsStatus(),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(smReportSaleRepList.get(iPos))){
                    SelectedList.remove(smReportSaleRepList.get(iPos));
                    setRowSelected(false,smReportSaleRepList.get(iPos).getIsStatus(),viewHolder);
                }else{
                    SelectedList.add(smReportSaleRepList.get(iPos));
                    setRowSelected(true,smReportSaleRepList.get(iPos).getIsStatus(),viewHolder);
                }
                //notifyDataSetChanged();
            }
        });

    }

    public void setReportSaleList(List<SM_ReportSaleRep> mReportTechList)
    {
        this.smReportSaleRepList = mReportTechList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        try {
            return this.smReportSaleRepList.size();
        }catch (Exception ex){return  0;}
    }

    public void clearSelected(){
        SelectedList.clear();
        notifyDataSetChanged();
    }

    public class ReportTechHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvReportCode)
        CustomTextView tvReportCode;
        @BindView(R.id.tvReportName)
        CustomTextView tvReportName;
        @BindView(R.id.tvReportDay)
        CustomTextView tvReportDay;
        @BindView(R.id.tvLongitude)
        CustomTextView tvLongitude;
        @BindView(R.id.tvLatitude)
        CustomTextView tvLatitude;
        @BindView(R.id.tvLocationAddress)
        CustomTextView tvLocationAddress;
        @BindView(R.id.tvReceiverList)
        CustomTextView tvReceiverList;
        @BindView(R.id.tvNotes)
        CustomTextView tvNotes;
        @BindView(R.id.tvIsStatus)
        CustomTextView tvIsStatus;
        @BindView(R.id.tvIsPost)
        CustomTextView tvIsPost;
        @BindView(R.id.tvPostDay)
        CustomTextView tvPostDay;

        public ReportTechHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(SM_ReportSaleRep oReportTech)
        {
            try
            {
                if(oReportTech != null)
                {
                    if(oReportTech.getReportCode() != null)
                    {
                        tvReportCode.setText(oReportTech.getReportCode());
                    }
                    if(oReportTech.getReportName() != null)
                    {
                        tvReportName.setText(oReportTech.getReportName());
                    }
                    if(oReportTech.getReportDay() != null)
                    {
                        tvReportDay.setText(oReportTech.getReportDay());
                    }
                    if(oReportTech.getLongtitude() != null)
                    {
                        tvLongitude.setText(oReportTech.getLongtitude().toString());
                    }
                    if(oReportTech.getLatitude() != null)
                    {
                        tvLatitude.setText(oReportTech.getLatitude().toString());
                    }
                    if(oReportTech.getLocationAddress() != null)
                    {
                        tvLocationAddress.setText(oReportTech.getLocationAddress());
                    }
                    if(oReportTech.getReceiverList() != null && oReportTech.getReceiverList().length() > 0)
                    {
                        String[] empId = oReportTech.getReceiverList().split(",");
                        if(empId.length > 0){
                            List<DM_Employee> lstEmp = mDB.getEmpByListId(empId);
                            String lstName = "";
                            if(lstEmp != null && lstEmp.size() > 0){
                                for(int i = 0; i < lstEmp.size(); i++){
                                    if(i > 0){
                                        lstName += ",";
                                    }
                                    lstName += lstEmp.get(i).getEmployeeName();
                                }
                            }
                            tvReceiverList.setText(lstName);
                        }else {
                            tvReceiverList.setText("");
                        }
                    }
                    if(oReportTech.getNotes() != null)
                    {
                        tvNotes.setText(oReportTech.getNotes());
                    }
                    if(oReportTech.getPost() != null)
                    {
                        if(oReportTech.getPost()==true || oReportTech.getPost().equals(true) || oReportTech.getPost().equals("1"))
                        {
                            tvIsPost.setText("Đã gửi");
                        }
                        else
                        {
                            tvIsPost.setText("Chưa gửi");
                        }
                    }
                    if(oReportTech.getPostDay() != null)
                    {
                        tvPostDay.setText(oReportTech.getPostDay());
                    }
                }

            }catch (Exception e){
                Log.d("ERR_BIND_REPORT_SALE",e.getMessage());
            }
        }
    }

    public void setRowSelected(boolean isSelected, Integer IsStatus, RecyclerView.ViewHolder viewHolder){
        if(isSelected){
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#F8D8E7"));
            viewHolder.itemView.setSelected(true);
        }else {
            if(IsStatus.equals(1)){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#dedcb7"));
                viewHolder.itemView.setSelected(false);
            }else if(IsStatus.equals(2)){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#d5c8df"));
                viewHolder.itemView.setSelected(false);
            }else if(IsStatus.equals(3)){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#c8dfce"));
                viewHolder.itemView.setSelected(false);
            }else if(IsStatus.equals(4)){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#bff2f1"));
                viewHolder.itemView.setSelected(false);
            }else if(IsStatus.equals(5)){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#f1c2c7"));
                viewHolder.itemView.setSelected(false);
            }
            else {
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                viewHolder.itemView.setSelected(false);
            }

        }
    }
}
