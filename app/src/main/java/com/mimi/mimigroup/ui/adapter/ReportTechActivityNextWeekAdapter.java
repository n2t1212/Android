package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.SM_ReportTechActivity;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportTechActivityNextWeekAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public void setsmoReportTechActivity(List<SM_ReportTechActivity> smoReportTechActivity) {
        this.smoReportTechActivity = smoReportTechActivity;
    }

    List<SM_ReportTechActivity> smoReportTechActivity;
    public List<SM_ReportTechActivity> SelectedList = new ArrayList<>();

    public interface ListItemClickListener{
        void onItemClick(List<SM_ReportTechActivity> SelectList);
    }

    private final ListItemClickListener mOnItemClicked;

    public ReportTechActivityNextWeekAdapter(ListItemClickListener mOnClickListener) {
        smoReportTechActivity = new ArrayList<>();
        this.mOnItemClicked=mOnClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ReportTechActivityNextWeekHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_report_tech_activity_next_week_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder( final RecyclerView.ViewHolder viewHolder,final int iPos) {
        if(viewHolder instanceof ReportTechActivityNextWeekAdapter.ReportTechActivityNextWeekHolder){
            ((ReportTechActivityNextWeekHolder) viewHolder).bind(smoReportTechActivity.get(iPos));
        }

        setRowSelected(SelectedList.contains(smoReportTechActivity.get(iPos)),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(smoReportTechActivity.get(iPos))){
                    SelectedList.remove(smoReportTechActivity.get(iPos));
                    setRowSelected(false,viewHolder);
                    clearSelected();
                }else{
                    SelectedList.add(smoReportTechActivity.get(iPos));
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
        if(smoReportTechActivity != null) {
            return  smoReportTechActivity.size();
        }
        return 0;
    }

    public void clearSelected(){
        SelectedList.clear();
        notifyDataSetChanged();
    }

    class ReportTechActivityNextWeekHolder  extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        CustomTextView tvTitle;
        @BindView(R.id.tvNotes)
        CustomTextView tvNotes;
        @BindView(R.id.tvAchievement)
        CustomTextView tvAchievement;

        public ReportTechActivityNextWeekHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(SM_ReportTechActivity oDetail){
            if(oDetail != null){
                if(oDetail.getTitle() != null){
                    tvTitle.setText(oDetail.getTitle());
                }
                if(oDetail.getNotes() != null){
                    tvNotes.setText(oDetail.getNotes());
                }
                if(oDetail.getAchievement() != null)
                {
                    tvAchievement.setText(oDetail.getAchievement());
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


