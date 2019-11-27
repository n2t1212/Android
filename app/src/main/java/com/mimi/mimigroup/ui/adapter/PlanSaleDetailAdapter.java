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

    public void setsmoPlanSaleDetail(List<SM_PlanSaleDetail> smoPlanSaleDetail) {
        this.smoPlanSaleDetail = smoPlanSaleDetail;
        notifyDataSetChanged();
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
        @BindView(R.id.tvProductName)
        CustomTextView tvProductName;
        @BindView(R.id.tvUnit)
        CustomTextView tvUnit;
        @BindView(R.id.tvSpec)
        CustomTextView tvSpec;
        @BindView(R.id.tvAmountBox)
        CustomTextView tvAmountBox;
        @BindView(R.id.tvAmount)
        CustomTextView tvAmount;
        @BindView(R.id.tvNotes)
        CustomTextView tvNotes;

        public PlanSaleDetailHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void bind(SM_PlanSaleDetail oDetail){
            if(oDetail != null){
                if(oDetail.getCustomerId() != null){
                    tvCustomer.setText(oDetail.getCustomerName());
                    tvCustomer.setTag(oDetail.getCustomerId());
                }
                if(oDetail.getProductCode() != null){
                   tvProductCode.setText(oDetail.getProductCode());
                }else{
                    tvProductCode.setText("");
                }
                if(oDetail.getProductName()!=null) {
                    tvProductName.setText(oDetail.getProductName());
                }
                if(oDetail.getUnit()!=null){
                    tvUnit.setText(oDetail.getUnit());
                }
                if(oDetail.getSpec()!=null){
                    tvSpec.setText(oDetail.getSpec());
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

