package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.DM_Province;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProvinceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DM_Province> provinceList;
    List<DM_Province> SelectedList=new ArrayList<DM_Province>();

    public ProvinceAdapter() {
        provinceList = new ArrayList<>();
    }

    public void setProvinceList(List<DM_Province> provinceList) {
        this.provinceList = provinceList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ProvinceHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_province, viewGroup, false));
    }

    @Override
     public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        //viewHolder.setIsRecyclable(false);
        if (viewHolder instanceof ProvinceHolder){
            ((ProvinceHolder) viewHolder).bind(provinceList.get(iPos));
        }

        setRowSelected(SelectedList.contains(provinceList.get(iPos)),viewHolder);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(provinceList.get(iPos))){
                    SelectedList.remove(provinceList.get(iPos));
                    setRowSelected(false,viewHolder);
                }else{
                    SelectedList.add(provinceList.get(iPos));
                    setRowSelected(true,viewHolder);
                }
               //notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return provinceList.size();
    }

    class ProvinceHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvTinh)
        CustomTextView tvTinh;
        @BindView(R.id.tvCode)
        CustomTextView tvCode;

        public ProvinceHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(DM_Province province){
            if (province != null){
                if (province.getProvince() != null){
                    tvTinh.setText(province.getProvince());
                }
                if (province.getProvinceCode() != null){
                    tvCode.setText(province.getProvinceCode());
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
