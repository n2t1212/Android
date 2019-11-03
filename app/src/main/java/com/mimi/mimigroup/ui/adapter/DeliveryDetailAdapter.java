package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.SM_OrderDeliveryDetail;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeliveryDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<SM_OrderDeliveryDetail> smDeliveryDetailList;
    public List<SM_OrderDeliveryDetail> SelectedList=new ArrayList<SM_OrderDeliveryDetail>();

    public DeliveryDetailAdapter() {
        smDeliveryDetailList=new ArrayList<SM_OrderDeliveryDetail>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OrderHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_order_delivery_item, viewGroup, false));
    }

    @Override
     public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        if (viewHolder instanceof OrderHolder){
            if(this.smDeliveryDetailList!=null && !this.smDeliveryDetailList.isEmpty()) {
                ((OrderHolder) viewHolder).bind(smDeliveryDetailList.get(iPos));
            }
        }

        setRowSelected(SelectedList.contains(smDeliveryDetailList.get(iPos)),smDeliveryDetailList.get(iPos).getAmount(),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(smDeliveryDetailList.get(iPos))){
                    SelectedList.remove(smDeliveryDetailList.get(iPos));
                    setRowSelected(false,smDeliveryDetailList.get(iPos).getAmount(),viewHolder);
                    clearSelected();
                }else{
                    SelectedList.add(smDeliveryDetailList.get(iPos));
                    setRowSelected(true,smDeliveryDetailList.get(iPos).getAmount(),viewHolder);
                }
                //notifyDataSetChanged();
            }
        });

    }

    public void setDeliveryOrderList(List<SM_OrderDeliveryDetail> mOrderDeliveryDetailList) {
        this.smDeliveryDetailList =mOrderDeliveryDetailList;
         notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        try {
            return this.smDeliveryDetailList.size();
        }catch (Exception ex){return  0;}
    }
    public void clearSelected(){
        SelectedList.clear();
        notifyDataSetChanged();
    }

    public class OrderHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvDeliveryProductCode)
        CustomTextView tvDeliveryProductCode;
        @BindView(R.id.tvDeliveryProductName)
        CustomTextView tvDeliveryProductName;
        @BindView(R.id.tvDeliveryUnit)
        CustomTextView tvDeliveryUnit;
        @BindView(R.id.tvDeliverySpec)
        CustomTextView tvDeliverySpec;

        @BindView(R.id.tvDeliveryAmount)
        CustomTextView tvDeliveryAmount;
        @BindView(R.id.tvDeliveryPrice)
        CustomTextView tvDeliveryPrice;
        @BindView(R.id.tvDeliveryOriginMoney)
        CustomTextView tvDeliveryOriginMoney;
        @BindView(R.id.tvDeliverDetailNotes)
        CustomTextView tvDeliverDetailNotes;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(SM_OrderDeliveryDetail oDeliveryDetail) {
            try {
                if (oDeliveryDetail!= null) {
                    if (oDeliveryDetail.getProductCode() != null) {
                        tvDeliveryProductCode.setText(oDeliveryDetail.getProductCode());
                    }
                    if (oDeliveryDetail.getProductName()!= null) {
                        tvDeliveryProductName.setText(oDeliveryDetail.getProductName());
                    }
                    if (oDeliveryDetail.getUnit()!= null) {
                        tvDeliveryUnit.setText(oDeliveryDetail.getUnit());
                    }
                    if (oDeliveryDetail.getSpec()!= null) {
                        tvDeliverySpec.setText(oDeliveryDetail.getSpec());
                    }
                    if (oDeliveryDetail.getAmount() != null) {
                        tvDeliveryAmount.setText(AppUtils.getMoneyFormat(oDeliveryDetail.getAmount().toString(),"VND"));
                    }
                    if (oDeliveryDetail.getPrice()!= null) {
                        tvDeliveryPrice.setText(AppUtils.getMoneyFormat(oDeliveryDetail.getPrice().toString(),"VND"));
                    }
                    if (oDeliveryDetail.getOriginMoney()!= null) {
                        tvDeliveryOriginMoney.setText(AppUtils.getMoneyFormat(oDeliveryDetail.getOriginMoney().toString(),"VND"));
                    }
                    if (oDeliveryDetail.getNotes()!= null) {
                        tvDeliverDetailNotes.setText(oDeliveryDetail.getNotes());
                    }
                }
            }catch (Exception e){
                Log.d("ERR_BIND_DELI_DETAIL",e.getMessage());
            }
        }
    }

    public void setRowSelected(boolean isSelected,Integer OrderStatus,RecyclerView.ViewHolder viewHolder){
        if(isSelected){
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#F8D8E7"));
            viewHolder.itemView.setSelected(true);
        }else {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            viewHolder.itemView.setSelected(false);
        }
    }

}
