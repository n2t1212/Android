package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.SM_OrderDetail;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<SM_OrderDetail> smoDetailList;
    public List<SM_OrderDetail> SelectedList=new ArrayList<SM_OrderDetail>();



    //Trigger Onclick from Adapter
    public interface ListItemClickListener{
        void onItemClick(List<SM_OrderDetail> SelectList);
    }
    private final ListItemClickListener mOnItemClicked;

    public OrderDetailAdapter(ListItemClickListener mOnClickListener) {
        smoDetailList=new ArrayList<SM_OrderDetail>();
        this.mOnItemClicked=mOnClickListener;
    }
   /*
    public OrderDetailAdapter() {
        smoDetailList = new ArrayList<>();
    }
*/

    public void setOrderDetailList(List<SM_OrderDetail> oDetailList) {
        this.smoDetailList = oDetailList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OrderDetailHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_order_form_detail_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        if (viewHolder instanceof OrderDetailAdapter.OrderDetailHolder){
            ((OrderDetailAdapter.OrderDetailHolder) viewHolder).bind(smoDetailList.get(iPos));
        }


        setRowSelected(SelectedList.contains(smoDetailList.get(iPos)),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(smoDetailList.get(iPos))){
                    SelectedList.remove(smoDetailList.get(iPos));
                    setRowSelected(false,viewHolder);
                    clearSelected();
                }else{
                    SelectedList.add(smoDetailList.get(iPos));
                    setRowSelected(true,viewHolder);
                }
                mOnItemClicked.onItemClick(SelectedList);
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if(smoDetailList!=null) {
            return smoDetailList.size();
        }else{return 0;}
    }

    public void clearSelected(){
        SelectedList.clear();
        notifyDataSetChanged();
    }

    class OrderDetailHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvProductCode)
        CustomTextView tvProductCode;
        @BindView(R.id.tvProductName)
        CustomTextView tvProductName;
        @BindView(R.id.tvUnit)
        CustomTextView tvUnit;
        @BindView(R.id.tvSpec)
        CustomTextView tvSpec;
        @BindView(R.id.tvAmount)
        CustomTextView tvAmount;
        @BindView(R.id.tvAmountBox)
        CustomTextView tvAmountBox;
        @BindView(R.id.tvPrice)
        CustomTextView tvPrice;
        @BindView(R.id.tvOriginMoney)
        CustomTextView tvOriginMoney;
        @BindView(R.id.tvOrderDetailNotes)
        CustomTextView tvOrderDetailNotes;
        @BindView(R.id.tvOrderDetailNotes2)
        CustomTextView tvOrderDetailNotes2;

        public OrderDetailHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(SM_OrderDetail oDetail){
            if (oDetail != null){
                tvProductCode.setText(oDetail.getProductCode());
                if (oDetail.getProductName() != null){
                    tvProductName.setText(oDetail.getProductName());
                }
                if (oDetail.getUnit() != null){
                    tvUnit.setText(oDetail.getUnit());
                }
                if (oDetail.getSpec() != null){
                    tvSpec.setText(oDetail.getSpec());
                }
                if (oDetail.getAmount() != null){
                    tvAmount.setText(AppUtils.getMoneyFormat(oDetail.getAmount().toString(),"VND"));
                }
                if (oDetail.getAmountBox() != null){
                    tvAmountBox.setText(AppUtils.getMoneyFormat(oDetail.getAmountBox().toString(),"USD"));
                }
                if (oDetail.getPrice() != null){
                    tvPrice.setText(AppUtils.getMoneyFormat(oDetail.getPrice().toString(),"VND"));
                }

                if (oDetail.getOriginMoney() != null){
                    tvOriginMoney.setText(AppUtils.getMoneyFormat(oDetail.getOriginMoney().toString(),"VND"));
                }

                if (oDetail.getNotes() != null){
                    tvOrderDetailNotes.setText(oDetail.getNotes().toString());
                }
                if (oDetail.getNotes2() != null){
                    tvOrderDetailNotes2.setText(oDetail.getNotes2().toString());
                }
            }
        }
    }

    private void setRowSelected(boolean isSelected,RecyclerView.ViewHolder viewHolder){
        if(isSelected){
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#F8D8E7"));
            viewHolder.itemView.setSelected(true);
        }else {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            viewHolder.itemView.setSelected(false);
        }
    }
}

