package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.DM_Tree;
import com.mimi.mimigroup.model.SM_ReportSaleRepDisease;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportSaleRepDiseaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private DBGimsHelper mDB = null;
    public void setsmoReportSaleRepDisease(List<SM_ReportSaleRepDisease> smoReportSaleRepDisease) {
        this.smoReportSaleRepDisease = smoReportSaleRepDisease;
        notifyDataSetChanged();
    }

    List<SM_ReportSaleRepDisease> smoReportSaleRepDisease;
    public List<SM_ReportSaleRepDisease> SelectedList = new ArrayList<>();

    public interface ListItemClickListener{
        void onItemClick(List<SM_ReportSaleRepDisease> SelectList);
    }

    private final ReportSaleRepDiseaseAdapter.ListItemClickListener mOnItemClicked;

    public ReportSaleRepDiseaseAdapter(ReportSaleRepDiseaseAdapter.ListItemClickListener mOnClickListener) {
        smoReportSaleRepDisease = new ArrayList<>();
        this.mOnItemClicked=mOnClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mDB = DBGimsHelper.getInstance(viewGroup.getContext());
        return new ReportSaleRepDiseaseAdapter.ReportSaleRepDiseaseHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_report_sale_rep_disease_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder( final RecyclerView.ViewHolder viewHolder,final int iPos) {
        if(viewHolder instanceof ReportSaleRepDiseaseAdapter.ReportSaleRepDiseaseHolder){
            ((ReportSaleRepDiseaseAdapter.ReportSaleRepDiseaseHolder) viewHolder).bind(smoReportSaleRepDisease.get(iPos));
        }

        setRowSelected(SelectedList.contains(smoReportSaleRepDisease.get(iPos)),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(smoReportSaleRepDisease.get(iPos))){
                    SelectedList.remove(smoReportSaleRepDisease.get(iPos));
                    setRowSelected(false,viewHolder);
                    clearSelected();
                }else{
                    SelectedList.add(smoReportSaleRepDisease.get(iPos));
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
        if(smoReportSaleRepDisease != null) {
            return  smoReportSaleRepDisease.size();
        }
        return 0;
    }

    public void clearSelected(){
        SelectedList.clear();
        notifyDataSetChanged();
    }

    class ReportSaleRepDiseaseHolder  extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTreeCode)
        CustomTextView tvTreeCode;
        @BindView(R.id.tvTitle)
        CustomTextView tvTitle;
        @BindView(R.id.tvAcreage)
        CustomTextView tvAcreage;
        @BindView(R.id.tvNotes)
        CustomTextView tvNotes;

        public ReportSaleRepDiseaseHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(SM_ReportSaleRepDisease oDetail){
            if(oDetail != null){
                if(oDetail.getTreeCode() != null){
                    DM_Tree tree = mDB.getTreeByCode(oDetail.getTreeCode());

                    if(tree != null){
                        tvTreeCode.setText(tree.getTreeName());
                    }else{
                        tvTreeCode.setText("");
                    }
                }
                if(oDetail.getTitle() != null){
                    tvTitle.setText(oDetail.getTitle());
                }
                if(oDetail.getArceage() != null)
                {
                    tvAcreage.setText(oDetail.getArceage().toString());
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
