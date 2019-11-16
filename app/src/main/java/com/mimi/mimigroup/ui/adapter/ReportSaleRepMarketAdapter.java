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
import com.mimi.mimigroup.model.SM_ReportSaleRepMarket;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportSaleRepMarketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public DBGimsHelper mDB = null;
    public void setsmoReportSaleRepMarket(List<SM_ReportSaleRepMarket> smoReportSaleRepMarket) {
        this.smoReportSaleRepMarket = smoReportSaleRepMarket;
    }

    List<SM_ReportSaleRepMarket> smoReportSaleRepMarket;
    public List<SM_ReportSaleRepMarket> SelectedList = new ArrayList<>();

    public interface ListItemClickListener{
        void onItemClick(List<SM_ReportSaleRepMarket> SelectList);
    }

    private final ReportSaleRepMarketAdapter.ListItemClickListener mOnItemClicked;

    public ReportSaleRepMarketAdapter(ReportSaleRepMarketAdapter.ListItemClickListener mOnClickListener) {
        smoReportSaleRepMarket = new ArrayList<>();
        this.mOnItemClicked=mOnClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mDB = DBGimsHelper.getInstance(viewGroup.getContext());
        return new ReportSaleRepMarketAdapter.ReportSaleRepMarketHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_report_sale_rep_market_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder( final RecyclerView.ViewHolder viewHolder,final int iPos) {
        if(viewHolder instanceof ReportSaleRepMarketAdapter.ReportSaleRepMarketHolder){
            ((ReportSaleRepMarketAdapter.ReportSaleRepMarketHolder) viewHolder).bind(smoReportSaleRepMarket.get(iPos));
        }

        setRowSelected(SelectedList.contains(smoReportSaleRepMarket.get(iPos)),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(smoReportSaleRepMarket.get(iPos))){
                    SelectedList.remove(smoReportSaleRepMarket.get(iPos));
                    setRowSelected(false,viewHolder);
                    clearSelected();
                }else{
                    SelectedList.add(smoReportSaleRepMarket.get(iPos));
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
        if(smoReportSaleRepMarket != null) {
            return  smoReportSaleRepMarket.size();
        }
        return 0;
    }

    public void clearSelected(){
        SelectedList.clear();
        notifyDataSetChanged();
    }

    class ReportSaleRepMarketHolder  extends RecyclerView.ViewHolder {
        @BindView(R.id.tvCustomer)
        CustomTextView tvCustomer;
        @BindView(R.id.tvCompanyName)
        CustomTextView tvCompanyName;
        @BindView(R.id.tvProductCode)
        CustomTextView tvProductCode;
        @BindView(R.id.tvPrice)
        CustomTextView tvPrice;
        @BindView(R.id.tvNotes)
        CustomTextView tvNotes;

        public ReportSaleRepMarketHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(SM_ReportSaleRepMarket oDetail){
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
                if(oDetail.getCompanyName() != null){
                    tvCompanyName.setText(oDetail.getCompanyName());
                }
                if(oDetail.getProductCode() != null)
                {
                    DM_Product product = mDB.getProduct(oDetail.getProductCode());
                    tvProductCode.setText(product.getProductName());
                }else{
                    tvProductCode.setText("");
                }
                if(oDetail.getPrice() != null)
                {
                    tvPrice.setText(oDetail.getPrice().toString());
                }else{
                    tvPrice.setText("");
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
