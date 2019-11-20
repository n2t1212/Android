package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.SM_PlanSale;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlanSaleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<SM_PlanSale> smPlanSaleList;
    public List<SM_PlanSale> SelectedList = new ArrayList<>();

    public PlanSaleAdapter() {
        smPlanSaleList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PlanSaleAdapter.PlanSaleHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_plan_sale_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        if (viewHolder instanceof PlanSaleAdapter.PlanSaleHolder){
            if(this.smPlanSaleList!=null && !this.smPlanSaleList.isEmpty()) {
                ((PlanSaleAdapter.PlanSaleHolder) viewHolder).bind(smPlanSaleList.get(iPos));
            }
        }

        setRowSelected(SelectedList.contains(smPlanSaleList.get(iPos)),smPlanSaleList.get(iPos).getIsStatus(),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(smPlanSaleList.get(iPos))){
                    SelectedList.remove(smPlanSaleList.get(iPos));
                    setRowSelected(false,smPlanSaleList.get(iPos).getIsStatus(),viewHolder);
                }else{
                    SelectedList.add(smPlanSaleList.get(iPos));
                    setRowSelected(true,smPlanSaleList.get(iPos).getIsStatus(),viewHolder);
                }
                //notifyDataSetChanged();
            }
        });

    }

    public void setPlanSaleList(List<SM_PlanSale> mPlanSaleList)
    {
        this.smPlanSaleList = mPlanSaleList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        try {
            return this.smPlanSaleList.size();
        }catch (Exception ex){return  0;}
    }

    public void clearSelected(){
        SelectedList.clear();
        notifyDataSetChanged();
    }

    public class PlanSaleHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvPlanCode)
        CustomTextView tvPlanCode;
        @BindView(R.id.tvPlanDay)
        CustomTextView tvPlanDay;
        @BindView(R.id.tvStartDay)
        CustomTextView tvStartDay;
        @BindView(R.id.tvEndDay)
        CustomTextView tvEndDay;
        @BindView(R.id.tvPlanName)
        CustomTextView tvPlanName;
        @BindView(R.id.tvPostDay)
        CustomTextView tvPostDay;
        @BindView(R.id.tvIsPost)
        CustomTextView tvIsPost;
        @BindView(R.id.tvIsStatus)
        CustomTextView tvIsStatus;
        @BindView(R.id.tvNotes)
        CustomTextView tvNotes;


        public PlanSaleHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(SM_PlanSale oPlanSale)
        {
            try
            {
                if(oPlanSale != null)
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
                    if(oPlanSale.getPostDay() != null)
                    {
                        tvPostDay.setText(oPlanSale.getPostDay());
                    }
                    if(oPlanSale.getNotes() != null)
                    {
                        tvNotes.setText(oPlanSale.getNotes());
                    }
                    if(oPlanSale.getIsStatus() != null)
                    {
                        tvIsStatus.setText(oPlanSale.getIsStatus());
                    }
                    if(oPlanSale.getPost() != null)
                    {
                        if(oPlanSale.getPost()==true || oPlanSale.getPost().equals(true) || oPlanSale.getPost().equals("1"))
                        {
                            tvIsPost.setText("Đã gửi");
                        }
                        else
                        {
                            tvIsPost.setText("Chưa gửi");
                        }
                    }
                    if(oPlanSale.getPostDay() != null)
                    {
                        tvPostDay.setText(oPlanSale.getPostDay());
                    }
                }

            }catch (Exception e){
                Log.d("ERR_BIND_PLAN_SALE",e.getMessage());
            }
        }
    }

    public void setRowSelected(boolean isSelected, String IsStatus, RecyclerView.ViewHolder viewHolder){
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

