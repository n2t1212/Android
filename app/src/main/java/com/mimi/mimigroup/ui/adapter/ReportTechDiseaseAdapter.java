package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.SM_ReportTechDisease;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.ui.utility.ReportTechActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportTechDiseaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public void setsmoReportTechDisease(List<SM_ReportTechDisease> smoReportTechDisease) {
        this.smoReportTechDisease = smoReportTechDisease;
    }

    List<SM_ReportTechDisease> smoReportTechDisease;
    public List<SM_ReportTechDisease> SelectedList = new ArrayList<>();

    public interface ListItemClickListener{
        void onItemClick(List<SM_ReportTechDisease> SelectList);
    }

    private final ReportTechDiseaseAdapter.ListItemClickListener mOnItemClicked;

    public ReportTechDiseaseAdapter(ReportTechDiseaseAdapter.ListItemClickListener mOnClickListener) {
        smoReportTechDisease = new ArrayList<>();
        this.mOnItemClicked=mOnClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ReportTechCompetitorHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_report_tech_disease_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder( final RecyclerView.ViewHolder viewHolder,final int iPos) {
        if(viewHolder instanceof ReportTechDiseaseAdapter.ReportTechCompetitorHolder){
            ((ReportTechCompetitorHolder) viewHolder).bind(smoReportTechDisease.get(iPos));
        }

        setRowSelected(SelectedList.contains(smoReportTechDisease.get(iPos)),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(smoReportTechDisease.get(iPos))){
                    SelectedList.remove(smoReportTechDisease.get(iPos));
                    setRowSelected(false,viewHolder);
                    clearSelected();
                }else{
                    SelectedList.add(smoReportTechDisease.get(iPos));
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
        if(smoReportTechDisease != null) {
            return  smoReportTechDisease.size();
        }
        return 0;
    }

    public void clearSelected(){
        SelectedList.clear();
        notifyDataSetChanged();
    }

    class ReportTechCompetitorHolder  extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTreeCode)
        CustomTextView tvTreeCode;
        @BindView(R.id.tvTitle)
        CustomTextView tvTitle;
        @BindView(R.id.tvAcreage)
        CustomTextView tvAcreage;
        @BindView(R.id.tvDisease)
        CustomTextView tvDisease;
        @BindView(R.id.tvPrice)
        CustomTextView tvPrice;
        @BindView(R.id.tvNotes)
        CustomTextView tvNotes;

        public ReportTechCompetitorHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(SM_ReportTechDisease oDetail){
            if(oDetail != null){

                if(oDetail.getTreeCode() != null){
                    tvTreeCode.setText(oDetail.getTreeCode());
                }
                if(oDetail.getTitle() != null){
                    tvTitle.setText(oDetail.getTitle());
                }
                if(oDetail.getAcreage() != null)
                {
                    tvAcreage.setText(oDetail.getAcreage().toString());
                }
                if(oDetail.getDisease() != null)
                {
                    tvDisease.setText(oDetail.getDisease());
                }
                if(oDetail.getPrice() != null)
                {
                    tvPrice.setText(oDetail.getPrice().toString());
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
