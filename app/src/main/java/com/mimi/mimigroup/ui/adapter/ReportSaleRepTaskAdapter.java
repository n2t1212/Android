package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.SM_ReportSaleRepActivitie;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportSaleRepTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public void setsmoReportSaleRepTask(List<SM_ReportSaleRepActivitie> smoReportSaleRepTask) {
        this.smoReportSaleRepTask = smoReportSaleRepTask;
        notifyDataSetChanged();
    }

    List<SM_ReportSaleRepActivitie> smoReportSaleRepTask;
    public List<SM_ReportSaleRepActivitie> SelectedList = new ArrayList<>();

    public interface ListItemClickListener{
        void onItemClick(List<SM_ReportSaleRepActivitie> SelectList);
    }

    private final ReportSaleRepTaskAdapter.ListItemClickListener mOnItemClicked;

    public ReportSaleRepTaskAdapter(ReportSaleRepTaskAdapter.ListItemClickListener mOnClickListener) {
        smoReportSaleRepTask = new ArrayList<>();
        this.mOnItemClicked=mOnClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ReportSaleRepTaskAdapter.ReportSaleRepTaskHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_report_sale_rep_task_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder( final RecyclerView.ViewHolder viewHolder,final int iPos) {
        if(viewHolder instanceof ReportSaleRepTaskAdapter.ReportSaleRepTaskHolder){
            ((ReportSaleRepTaskAdapter.ReportSaleRepTaskHolder) viewHolder).bind(smoReportSaleRepTask.get(iPos));
        }

        setRowSelected(SelectedList.contains(smoReportSaleRepTask.get(iPos)),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(smoReportSaleRepTask.get(iPos))){
                    SelectedList.remove(smoReportSaleRepTask.get(iPos));
                    setRowSelected(false,viewHolder);
                    clearSelected();
                }else{
                    SelectedList.add(smoReportSaleRepTask.get(iPos));
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
        if(smoReportSaleRepTask != null) {
            return  smoReportSaleRepTask.size();
        }
        return 0;
    }

    public void clearSelected(){
        SelectedList.clear();
        notifyDataSetChanged();
    }

    class ReportSaleRepTaskHolder  extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        CustomTextView tvTitle;
        @BindView(R.id.tvNotes)
        CustomTextView tvNotes;
        @BindView(R.id.tvWorkday)
        CustomTextView tvWorkday;
        @BindView(R.id.tvPlace)
        CustomTextView tvPlace;

        public ReportSaleRepTaskHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(SM_ReportSaleRepActivitie oDetail){
            if(oDetail != null){
                if(oDetail.getTitle() != null){
                    tvTitle.setText(oDetail.getTitle());
                }
                if(oDetail.getNotes() != null){
                    tvNotes.setText(oDetail.getNotes());
                }
                if(oDetail.getWorkDay() != null){
                    tvWorkday.setText(oDetail.getWorkDay());
                }
                if(oDetail.getPlace() != null){
                    tvPlace.setText(oDetail.getPlace());
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


