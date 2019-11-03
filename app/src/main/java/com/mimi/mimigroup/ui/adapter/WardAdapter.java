package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.DM_Ward;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<DM_Ward> wardList;
    List<DM_Ward> SelectedList=new ArrayList<DM_Ward>();

    public WardAdapter() {
        wardList = new ArrayList<>();
    }

    public void setWardList(List<DM_Ward> wardList) {
        this.wardList = wardList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new WardHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ward, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        if (viewHolder instanceof WardHolder){
            ((WardHolder) viewHolder).bind(wardList.get(iPos));
        }

        setRowSelected(SelectedList.contains(wardList.get(iPos)),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(wardList.get(iPos))){
                    SelectedList.remove(wardList.get(iPos));
                    setRowSelected(false,viewHolder);
                }else{
                    SelectedList.add(wardList.get(iPos));
                    setRowSelected(true,viewHolder);
                }
                //notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return wardList.size();
    }

    class WardHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvWard)
        CustomTextView tvWard;
        @BindView(R.id.tvDistrict)
        CustomTextView tvDistrict;

        public WardHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(DM_Ward ward){
            if (ward != null){
                if (ward.getWard() != null){
                    tvWard.setText(ward.getWard());
                }
                if(ward.getDistrict()!=""){
                    tvDistrict.setText(ward.getDistrict());
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
