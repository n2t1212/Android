package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.DM_Season;
import com.mimi.mimigroup.model.DM_Tree;
import com.mimi.mimigroup.model.SM_ReportSaleRepSeason;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportSaleRepSeasonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private DBGimsHelper mDB = null;
    public void setsmoReportSaleRepSeason(List<SM_ReportSaleRepSeason> smoReportSaleRepSeason) {
        this.smoReportSaleRepSeason = smoReportSaleRepSeason;
    }

    List<SM_ReportSaleRepSeason> smoReportSaleRepSeason;
    public List<SM_ReportSaleRepSeason> SelectedList = new ArrayList<>();

    public interface ListItemClickListener{
        void onItemClick(List<SM_ReportSaleRepSeason> SelectList);
    }

    private final ReportSaleRepSeasonAdapter.ListItemClickListener mOnItemClicked;

    public ReportSaleRepSeasonAdapter(ReportSaleRepSeasonAdapter.ListItemClickListener mOnClickListener) {
        smoReportSaleRepSeason = new ArrayList<>();
        this.mOnItemClicked=mOnClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mDB = DBGimsHelper.getInstance(viewGroup.getContext());
        return new ReportSaleRepSeasonAdapter.ReportSaleRepSeasonHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_report_sale_rep_season_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder( final RecyclerView.ViewHolder viewHolder,final int iPos) {
        if(viewHolder instanceof ReportSaleRepSeasonAdapter.ReportSaleRepSeasonHolder){
            ((ReportSaleRepSeasonAdapter.ReportSaleRepSeasonHolder) viewHolder).bind(smoReportSaleRepSeason.get(iPos));
        }

        setRowSelected(SelectedList.contains(smoReportSaleRepSeason.get(iPos)),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(smoReportSaleRepSeason.get(iPos))){
                    SelectedList.remove(smoReportSaleRepSeason.get(iPos));
                    setRowSelected(false,viewHolder);
                    clearSelected();
                }else{
                    SelectedList.add(smoReportSaleRepSeason.get(iPos));
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
        if(smoReportSaleRepSeason != null) {
            return  smoReportSaleRepSeason.size();
        }
        return 0;
    }

    public void clearSelected(){
        SelectedList.clear();
        notifyDataSetChanged();
    }

    class ReportSaleRepSeasonHolder  extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTreeCode)
        CustomTextView tvTreeCode;
        @BindView(R.id.tvSeasonCode)
        CustomTextView tvSeasonCode;
        @BindView(R.id.tvTitle)
        CustomTextView tvTitle;
        @BindView(R.id.tvAcreage)
        CustomTextView tvAcreage;
        @BindView(R.id.tvNotes)
        CustomTextView tvNotes;

        public ReportSaleRepSeasonHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(SM_ReportSaleRepSeason oDetail){
            if(oDetail != null){
                if(oDetail.getTreeCode() != null){
                    DM_Tree tree = mDB.getTreeByCode(oDetail.getTreeCode());

                    if(tree != null){
                        tvTreeCode.setText(tree.getTreeName());
                    }else{
                        tvTreeCode.setText("");
                    }
                }
                if(oDetail.getSeasonCode() != null){
                    DM_Season season = mDB.getSeasonByCode(oDetail.getSeasonCode());

                    if(season != null){
                        tvSeasonCode.setText(season.getSeasonName());
                    }else{
                        tvSeasonCode.setText("");
                    }
                }
                if(oDetail.getTitle() != null){
                    tvTitle.setText(oDetail.getTitle());
                }
                if(oDetail.getAcreage() != null)
                {
                    tvAcreage.setText(oDetail.getAcreage().toString());
                }
                if(oDetail.getNotes() != null){
                    tvNotes.setText(oDetail.getNotes());
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