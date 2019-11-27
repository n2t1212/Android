package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.SM_Order;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<SM_Order> smOrderList;
    public List<SM_Order> SelectedList=new ArrayList<SM_Order>();

    public OrderAdapter() {
        smOrderList=new ArrayList<SM_Order>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OrderHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_order_item, viewGroup, false));
    }

    @Override
     public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        if (viewHolder instanceof OrderHolder){
            if(this.smOrderList!=null && !this.smOrderList.isEmpty()) {
                ((OrderHolder) viewHolder).bind(smOrderList.get(iPos));
            }
        }

        setRowSelected(SelectedList.contains(smOrderList.get(iPos)),smOrderList.get(iPos).getOrderStatus(),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(smOrderList.get(iPos))){
                    SelectedList.remove(smOrderList.get(iPos));
                    setRowSelected(false,smOrderList.get(iPos).getOrderStatus(),viewHolder);
                }else{
                    SelectedList.add(smOrderList.get(iPos));
                    setRowSelected(true,smOrderList.get(iPos).getOrderStatus(),viewHolder);
                }
                //notifyDataSetChanged();
            }
        });

    }

    public void setOrderList(List<SM_Order> mOrderList) {
        this.smOrderList = mOrderList;
         notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        try {
            return this.smOrderList.size();
        }catch (Exception ex){return  0;}
    }

    public void clearSelected(){
        SelectedList.clear();
        notifyDataSetChanged();
    }

    public class OrderHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvOrderCode)
        CustomTextView tvOrderCode;
        @BindView(R.id.tvOrderDate)
        CustomTextView tvOrderDate;
        @BindView(R.id.tvRequestDate)
        CustomTextView tvRequestDate;
        @BindView(R.id.tvTotalMoney)
        CustomTextView tvTotalMoney;

        @BindView(R.id.tvCustomerCode)
        CustomTextView tvCustomerCode;
        @BindView(R.id.tvCustomerName)
        CustomTextView tvCustomerName;
        @BindView(R.id.tvCustomerAddress)
        CustomTextView tvCustomerAddress;
        @BindView(R.id.tvOrderStatus)
        CustomTextView tvOrderStatus;
        @BindView(R.id.tvOrderNotes)
        CustomTextView tvOrderNotes;
        @BindView(R.id.tvIsPost)
        CustomTextView tvIsPost;
        @BindView(R.id.tvPostDate)
        CustomTextView tvPostDate;
        @BindView(R.id.tvIsSample)
        CustomTextView tvIsSample;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(SM_Order oOrder) {
            try {
                if (oOrder!= null) {
                    if (oOrder.getOrderCode() != null) {
                        tvOrderCode.setText(oOrder.getOrderCode());
                    }
                    if (oOrder.getOrderDate() != null) {
                        tvOrderDate.setText(oOrder.getOrderDate());
                    }
                    if (oOrder.getRequestDate() != null) {
                        tvRequestDate.setText(oOrder.getRequestDate());
                    }
                    if (oOrder.getTotalMoney() != null) {
                        tvTotalMoney.setText(AppUtils.getMoneyFormat(oOrder.getTotalMoney().toString(),"VND"));
                    }
                    if (oOrder.getCustomerCode() != null) {
                        tvCustomerCode.setText(oOrder.getCustomerCode());
                    }
                    if (oOrder.getCustomerName() != null) {
                        tvCustomerName.setText(oOrder.getCustomerName());
                    }
                    if (oOrder.getCustomerAddress() != null) {
                        tvCustomerAddress.setText(oOrder.getCustomerAddress());
                    }
                    if(oOrder.getOrderStatusDesc()!=null){
                        tvOrderStatus.setText(oOrder.getOrderStatusDesc().toString());
                    }
                    if(oOrder.getOrderNotes()!=null){
                        tvOrderNotes.setText(oOrder.getOrderNotes().toString());
                    }
                    if(oOrder.getPost()!=null){
                        if(oOrder.getPost()==true || oOrder.getPost().equals(true) || oOrder.getPost().equals("1")){
                            tvIsPost.setText("Đã gửi");
                        }else{
                            tvIsPost.setText("Chưa gửi");
                        }
                    }
                    if(oOrder.getPostDay()!=null ){
                        tvPostDate.setText(oOrder.getPostDay());
                    }
                    if(oOrder.getSample() != null){
                        if(oOrder.getSample()==true || oOrder.getSample().equals(true) || oOrder.getSample().equals("1")){
                            tvIsSample.setText("x");
                        }else{
                            tvIsSample.setText("");
                        }
                    }
                }
            }catch (Exception e){
                Log.d("ERR_BIND_ORDER",e.getMessage());
            }
        }
    }

    //#f2ffcc|| 0:#ffffff,1:Edit:#E3DF91,2:Pending: #dcf409,3: Accept & Delivery:#b7e4cf,4:Complete:#bff2f1,5:Abort:
    public void setRowSelected(boolean isSelected,Integer OrderStatus,RecyclerView.ViewHolder viewHolder){
        if(isSelected){
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#F8D8E7"));
            viewHolder.itemView.setSelected(true);
        }else {
            if(OrderStatus.equals(1)){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#dedcb7"));
                viewHolder.itemView.setSelected(false);
            }else if(OrderStatus.equals(2)){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#d5c8df"));
                viewHolder.itemView.setSelected(false);
            }else if(OrderStatus.equals(3)){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#c8dfce"));
                viewHolder.itemView.setSelected(false);
            }else if(OrderStatus.equals(4)){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#bff2f1"));
                viewHolder.itemView.setSelected(false);
            }else if(OrderStatus.equals(5)){
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
