package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.DM_Season;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeasonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DM_Season> seasonList;
    public List<DM_Season> SelectedList=new ArrayList<DM_Season>();

    public SeasonAdapter() {
        seasonList = new ArrayList<>();
    }


    public void setseasonList(List<DM_Season> seasonList) {
        this.seasonList = seasonList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SeasonAdapter.SeasonHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_season_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        if (viewHolder instanceof SeasonAdapter.SeasonHolder){
            ((SeasonAdapter.SeasonHolder) viewHolder).bind(seasonList.get(iPos));
        }

        setRowSelected(SelectedList.contains(seasonList.get(iPos)),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(seasonList.get(iPos))){
                    SelectedList.remove(seasonList.get(iPos));
                    setRowSelected(false,viewHolder);
                }else{
                    SelectedList.add(seasonList.get(iPos));
                    setRowSelected(true,viewHolder);
                }
                //notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return seasonList.size();
    }

    class SeasonHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvSeasonCode)
        CustomTextView tvSeasonCode;
        @BindView(R.id.tvSeasonName)
        CustomTextView tvSeasonName;

        public SeasonHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(DM_Season oDM){
            if (oDM != null){
                tvSeasonCode.setText(oDM.getSeasonCode());
                if (oDM.getSeasonName() != null){
                    tvSeasonName.setText(oDM.getSeasonName());
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


