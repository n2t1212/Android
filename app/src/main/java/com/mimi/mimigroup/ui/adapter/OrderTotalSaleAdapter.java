package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.SM_OrderTotalSales;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderTotalSaleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<SM_OrderTotalSales> smDetailList;
    public List<SM_OrderTotalSales> SelectedList=new ArrayList<SM_OrderTotalSales>();

    public OrderTotalSaleAdapter() {
        smDetailList=new ArrayList<SM_OrderTotalSales>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OrderHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_order_totalsales_item, viewGroup, false));
    }

    @Override
     public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        if (viewHolder instanceof OrderHolder){
            if(this.smDetailList!=null && !this.smDetailList.isEmpty()) {
                ((OrderHolder) viewHolder).bind(smDetailList.get(iPos));
            }
        }

        setRowSelected(SelectedList.contains(smDetailList.get(iPos)),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(smDetailList.get(iPos))){
                    SelectedList.remove(smDetailList.get(iPos));
                    setRowSelected(false,viewHolder);
                    clearSelected();
                }else{
                    SelectedList.add(smDetailList.get(iPos));
                    setRowSelected(true,viewHolder);
                }
                //notifyDataSetChanged();
            }
        });

    }

    public void setOrderTotalSaleList(List<SM_OrderTotalSales> mOrderTotalSaleList) {
        this.smDetailList =mOrderTotalSaleList;
         notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        try {
            return this.smDetailList.size();
        }catch (Exception ex){return  0;}
    }
    public void clearSelected(){
        SelectedList.clear();
        notifyDataSetChanged();
    }

    public class OrderHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvTotalSaleSTT)
        CustomTextView tvTotalSaleSTT;
        @BindView(R.id.tvTotalSaleOrderCode)
        CustomTextView tvTotalSaleOrderCode;
        @BindView(R.id.tvTotalSaleDay)
        CustomTextView tvTotalSaleDay;
        @BindView(R.id.tvTotalSaleMoney)
        CustomTextView tvTotalSaleMoney;
        @BindView(R.id.tvTotalSaleStatus)
        CustomTextView tvTotalSaleStatus;
        @BindView(R.id.tvTotalSaleNotes)
        CustomTextView tvTotalSaleNotes;


        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(SM_OrderTotalSales oDetail) {
            try {
                if (oDetail!= null) {
                    if (oDetail.getSeqno() != null) {
                        tvTotalSaleSTT.setText(oDetail.getSeqno().toString());
                    }
                    if (oDetail.getOrderCode() != null) {
                        tvTotalSaleOrderCode.setText(oDetail.getOrderCode());
                    }
                    if (oDetail.getOrderDate()!= null) {
                        tvTotalSaleDay.setText(oDetail.getOrderDate());
                    }
                    if (oDetail.getOrderMoney()!= null) {
                        tvTotalSaleMoney.setText(AppUtils.getMoneyFormat(oDetail.getOrderMoney().toString(),"VND"));
                    }
                    if (oDetail.getOrderStatus()!= null) {
                        tvTotalSaleStatus.setText(oDetail.getOrderStatus());
                    }
                    if (oDetail.getOrderNotes()!= null) {
                        tvTotalSaleNotes.setText(oDetail.getOrderNotes());
                    }

                }
            }catch (Exception e){
                Log.d("ERR_BIND_DELI_DETAIL",e.getMessage());
            }
        }
    }

    public void setRowSelected(boolean isSelected,RecyclerView.ViewHolder viewHolder){
        if(isSelected){
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#F8D8E7"));
            viewHolder.itemView.setSelected(true);
        }else {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            viewHolder.itemView.setSelected(false);
        }
    }

}
