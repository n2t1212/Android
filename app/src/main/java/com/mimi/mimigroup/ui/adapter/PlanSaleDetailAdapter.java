package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.DM_Customer;
import com.mimi.mimigroup.model.DM_Product;
import com.mimi.mimigroup.model.SM_PlanSaleDetail;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlanSaleDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public DBGimsHelper mDB = null;
    public void setsmoPlanSaleDetail(List<SM_PlanSaleDetail> smoPlanSaleDetail) {
        this.smoPlanSaleDetail = smoPlanSaleDetail;
    }

    List<SM_PlanSaleDetail> smoPlanSaleDetail;
    public List<SM_PlanSaleDetail> SelectedList = new ArrayList<>();

    public interface ListItemClickListener{
        void onItemClick(List<SM_PlanSaleDetail> SelectList);
    }

    private final PlanSaleDetailAdapter.ListItemClickListener mOnItemClicked;

    public PlanSaleDetailAdapter(PlanSaleDetailAdapter.ListItemClickListener mOnClickListener) {
        smoPlanSaleDetail = new ArrayList<>();
        this.mOnItemClicked=mOnClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mDB = DBGimsHelper.getInstance(viewGroup.getContext());
        return new PlanSaleDetailAdapter.PlanSaleDetailHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_plan_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder( final RecyclerView.ViewHolder viewHolder,final int iPos) {
        if(viewHolder instanceof PlanSaleDetailAdapter.PlanSaleDetailHolder){
            ((PlanSaleDetailAdapter.PlanSaleDetailHolder) viewHolder).bind(smoPlanSaleDetail.get(iPos));
        }

        setRowSelected(SelectedList.contains(smoPlanSaleDetail.get(iPos)),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(smoPlanSaleDetail.get(iPos))){
                    SelectedList.remove(smoPlanSaleDetail.get(iPos));
                    setRowSelected(false,viewHolder);
                    clearSelected();
                }else{
                    SelectedList.add(smoPlanSaleDetail.get(iPos));
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
        if(smoPlanSaleDetail != null) {
            return  smoPlanSaleDetail.size();
        }
        return 0;
    }

    public void clearSelected(){
        SelectedList.clear();
        notifyDataSetChanged();
    }

    class PlanSaleDetailHolder  extends RecyclerView.ViewHolder {
        @BindView(R.id.tvCustomer)
        CustomTextView tvCustomer;
        @BindView(R.id.tvProductCode)
        CustomTextView tvProductCode;
        @BindView(R.id.tvAmountBox)
        CustomTextView tvAmountBox;
        @BindView(R.id.tvAmount)
        CustomTextView tvAmount;
        @BindView(R.id.tvNotes)
        CustomTextView tvNotes;
        @BindView(R.id.tvNotes2)
        CustomTextView tvNotes2;

        public PlanSaleDetailHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(SM_PlanSaleDetail oDetail){
            if(oDetail != null){
                if(oDetail.getCustomerId() != null){
                    DM_Customer customer = mDB.getCustomer(oDetail.getCustomerId());
                    if(customer != null){
                        tvCustomer.setText(customer.getCustomerName());
                        tvCustomer.setTag(oDetail.getCustomerId());
                    }else{
                        tvCustomer.setText("");
                        tvCustomer.setTag("");
                    }
                }
                if(oDetail.getProductCode() != null)
                {
                    DM_Product product = mDB.getProduct(oDetail.getProductCode());
                    tvProductCode.setText(product.getProductName());
                }else{
                    tvProductCode.setText("");
                }
                if(oDetail.getAmountBox() != null)
                {
                    tvAmountBox.setText(oDetail.getAmountBox().toString());
                }else{
                    tvAmountBox.setText("");
                }
                if(oDetail.getAmount() != null)
                {
                    tvAmount.setText(oDetail.getAmount().toString());
                }else{
                    tvAmount.setText("");
                }
                tvNotes.setText(oDetail.getNotes());
                tvNotes2.setText(oDetail.getNotes2());
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

