package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.DM_District;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DistrictAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DM_District> districtList;
    List<DM_District> SelectedList=new ArrayList<DM_District>();

    public DistrictAdapter() {
        districtList = new ArrayList<>();
    }

    public void setDistrictList(List<DM_District> districtList) {
        this.districtList = districtList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DistrictHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_district, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        if (viewHolder instanceof DistrictHolder){
            ((DistrictHolder) viewHolder).bind(districtList.get(iPos));
        }

        setRowSelected(SelectedList.contains(districtList.get(iPos)),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(districtList.get(iPos))){
                    SelectedList.remove(districtList.get(iPos));
                    setRowSelected(false,viewHolder);
                }else{
                    SelectedList.add(districtList.get(iPos));
                    setRowSelected(true,viewHolder);
                }
                //notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return districtList.size();
    }

    class DistrictHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvDistrict)
        CustomTextView tvDistrict;
        @BindView(R.id.tvProvince)
        CustomTextView tvProvince;

        public DistrictHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(DM_District district){

            if (district != null){
                if (district.getDistrict() != null){
                    tvDistrict.setText(district.getDistrict());
                }
                if (district.getProvince() != null){
                    tvProvince.setText(district.getProvince());
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
