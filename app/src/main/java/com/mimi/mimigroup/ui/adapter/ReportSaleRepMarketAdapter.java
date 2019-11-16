package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.SM_ReportSaleRepMarket;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportSaleRepMarketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public void setSmoReportTechMarket(List<SM_ReportSaleRepMarke> smoReportTechMarket) {
        this.smoReportTechMarket = smoReportTechMarket;
    }

    List<SM_ReportSaleRepMarket> smoReportTechMarket;
    public List<SM_ReportSaleRepMarket> SelectedList = new ArrayList<>();

    public interface ListItemClickListener{
        void onItemClick(List<SM_ReportSaleRepMarket> SelectList);
    }

    private final ReportSaleRepMarketAdapter.ListItemClickListener mOnItemClicked;

    public ReportSaleRepMarketAdapter(ReportSaleRepMarketAdapter.ListItemClickListener mOnClickListener) {
        smoReportTechMarket = new ArrayList<>();
        this.mOnItemClicked=mOnClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ReportSaleRepMarketAdapter.ReportTechMarketHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_report_tech_market_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder( final RecyclerView.ViewHolder viewHolder,final int iPos) {
        if(viewHolder instanceof ReportSaleRepMarketAdapter.ReportTechMarketHolder){
            ((ReportSaleRepMarketAdapter.ReportTechMarketHolder) viewHolder).bind(smoReportTechMarket.get(iPos));
        }

        setRowSelected(SelectedList.contains(smoReportTechMarket.get(iPos)),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(smoReportTechMarket.get(iPos))){
                    SelectedList.remove(smoReportTechMarket.get(iPos));
                    setRowSelected(false,viewHolder);
                    clearSelected();
                }else{
                    SelectedList.add(smoReportTechMarket.get(iPos));
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
        if(smoReportTechMarket != null) {
            return  smoReportTechMarket.size();
        }
        return 0;
    }

    public void clearSelected(){
        SelectedList.clear();
        notifyDataSetChanged();
    }

    class ReportTechMarketHolder  extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        CustomTextView tvTitle;
        @BindView(R.id.tvNotes)
        CustomTextView tvNotes;
        @BindView(R.id.tvUseful)
        CustomTextView tvUseful;
        @BindView(R.id.tvHarmful)
        CustomTextView tvHarmful;

        public ReportTechMarketHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(SM_ReportSaleRepMarket oDetail){
            if(oDetail != null){
                if(oDetail.getTitle() != null){
                    tvTitle.setText(oDetail.getTitle());
                }
                if(oDetail.getNotes() != null){
                    tvNotes.setText(oDetail.getNotes());
                }
                if(oDetail.getUsefull() != null)
                {
                    tvUseful.setText(oDetail.getUsefull());
                }
                if(oDetail.getHarmful() != null)
                {
                    tvHarmful.setText(oDetail.getHarmful());
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
