package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.SM_CustomerPay;
import com.mimi.mimigroup.model.SM_Order;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerPayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<SM_CustomerPay> smPayList;
    public List<SM_CustomerPay> SelectedList=new ArrayList<SM_CustomerPay>();

    public CustomerPayAdapter() {
        smPayList=new ArrayList<SM_CustomerPay>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OrderHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_customerpay_item, viewGroup, false));
    }

    @Override
     public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        if (viewHolder instanceof OrderHolder){
            if(this.smPayList!=null && !this.smPayList.isEmpty()) {
                ((OrderHolder) viewHolder).bind(smPayList.get(iPos));
            }
        }

        setRowSelected(SelectedList.contains(smPayList.get(iPos)),smPayList.get(iPos).getPayStatus(),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(smPayList.get(iPos))){
                    SelectedList.remove(smPayList.get(iPos));
                    setRowSelected(false,smPayList.get(iPos).getPayStatus(),viewHolder);
                }else{
                    SelectedList.add(smPayList.get(iPos));
                    setRowSelected(true,smPayList.get(iPos).getPayStatus(),viewHolder);
                }
                //notifyDataSetChanged();
            }
        });

    }

    public void setCustomerPayList(List<SM_CustomerPay> mPayList) {
        this.smPayList = mPayList;
         notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        try {
            return this.smPayList.size();
        }catch (Exception ex){return  0;}
    }

    public void clearSelected(){
        SelectedList.clear();
        notifyDataSetChanged();
    }

    public class OrderHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvPayCode)
        CustomTextView tvPayCode;
        @BindView(R.id.tvPayDate)
        CustomTextView tvPayDate;
        @BindView(R.id.tvPayMoney)
        CustomTextView tvPayMoney;
        @BindView(R.id.tvPayNotes)
        CustomTextView tvPayNotes;
        @BindView(R.id.tvPayStatus)
        CustomTextView tvPayStatus;
        @BindView(R.id.tvPayPic)
        CustomTextView tvPayPic;

        @BindView(R.id.tvCustomerCode)
        CustomTextView tvCustomerCode;
        @BindView(R.id.tvCustomerName)
        CustomTextView tvCustomerName;
        @BindView(R.id.tvLocationAddress)
        CustomTextView tvLocationAddress;
        @BindView(R.id.tvIsPost)
        CustomTextView tvIsPost;
        @BindView(R.id.tvPostDate)
        CustomTextView tvPostDate;


        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(SM_CustomerPay oPay) {
            try {
                if (oPay!= null) {
                    if (oPay.getPayCode()!= null) {
                        tvPayCode.setText(oPay.getPayCode());
                    }
                    if (oPay.getPayDate() != null) {
                        tvPayDate.setText(oPay.getPayDate());
                    }
                    if (oPay.getPayMoney() != null) {
                        tvPayMoney.setText(AppUtils.getMoneyFormat(oPay.getPayMoney().toString(),"VND"));
                    }

                    if (oPay.getCustomerCode() != null) {
                        tvCustomerCode.setText(oPay.getCustomerCode());
                    }
                    if (oPay.getCustomerName() != null) {
                        tvCustomerName.setText(oPay.getCustomerName());
                    }
                    if (oPay.getLocationAddress() != null) {
                        tvLocationAddress.setText(oPay.getLocationAddress());
                    }
                    if(oPay.getPayStatusDesc()!=null){
                        tvPayStatus.setText(oPay.getPayStatusDesc());
                    }
                    if(oPay.getPayNotes()!=null){
                        tvPayNotes.setText(oPay.getPayNotes());
                    }
                    if(oPay.getPayPic()!=null){
                        if (!oPay.getPayPic().isEmpty() && oPay.getPayPic().length()>0){
                            tvPayPic.setText("x");
                        }else{
                            tvPayPic.setText("");
                        }
                    }
                    if(oPay.getPost()!=null){
                        if(oPay.getPost()==true || oPay.getPost().equals(true) || oPay.getPost().equals("1")){
                            tvIsPost.setText("Đã gửi");
                        }else{
                            tvIsPost.setText("Chưa gửi");
                        }
                    }
                    if(oPay.getPostDay()!=null ){
                        tvPostDate.setText(oPay.getPostDay());
                    }
                }
            }catch (Exception e){
                Log.d("ERR_BIND_ORDER",e.getMessage());
            }
        }
    }

    //#f2ffcc|| 0:#ffffff,1:Edit:#E3DF91,2:Pending: #dcf409,3: Accept & Delivery:#b7e4cf,4:Complete:#bff2f1,5:Abort:
    public void setRowSelected(boolean isSelected,Integer isPayStatus,RecyclerView.ViewHolder viewHolder){
        if(isSelected){
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#F8D8E7"));
            viewHolder.itemView.setSelected(true);
        }else {
            if(isPayStatus.equals(1)){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#dedcb7"));
                viewHolder.itemView.setSelected(false);
            }else if(isPayStatus.equals(2)){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#d5c8df"));
                viewHolder.itemView.setSelected(false);
            }else if(isPayStatus.equals(3)){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#c8dfce"));
                viewHolder.itemView.setSelected(false);
            }else if(isPayStatus.equals(4)){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#bff2f1"));
                viewHolder.itemView.setSelected(false);
            }else if(isPayStatus.equals(5)){
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
