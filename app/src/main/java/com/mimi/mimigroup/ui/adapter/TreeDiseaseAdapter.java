package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.DM_Tree;
import com.mimi.mimigroup.model.DM_Tree_Disease;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TreeDiseaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DM_Tree_Disease> treedisList;
    public List<DM_Tree_Disease> SelectedList=new ArrayList<DM_Tree_Disease>();

    public TreeDiseaseAdapter() {
        treedisList = new ArrayList<>();
    }


    public void setTreeDiseaseList(List<DM_Tree_Disease> mtreedisList) {
        this.treedisList = mtreedisList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TreeDiseaseHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_tree_disease_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        if (viewHolder instanceof TreeDiseaseAdapter.TreeDiseaseHolder){
            ((TreeDiseaseAdapter.TreeDiseaseHolder) viewHolder).bind(treedisList.get(iPos));
        }

        setRowSelected(SelectedList.contains(treedisList.get(iPos)),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(treedisList.get(iPos))){
                    SelectedList.remove(treedisList.get(iPos));
                    setRowSelected(false,viewHolder);
                }else{
                    SelectedList.add(treedisList.get(iPos));
                    setRowSelected(true,viewHolder);
                }
                //notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return treedisList.size();
    }

    class TreeDiseaseHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvDisTreeName)
        CustomTextView tvDisTreeName;
        @BindView(R.id.tvDiseaseCode)
        CustomTextView tvDiseaseCode;
        @BindView(R.id.tvDiseaseName)
        CustomTextView tvDiseaseName;

        public TreeDiseaseHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(DM_Tree_Disease oDM){
            if (oDM != null){
                tvDisTreeName.setText(oDM.getTreeName());
                if (oDM.getDiseaseCode() != null){
                    tvDiseaseCode.setText(oDM.getDiseaseCode());
                }
                if (oDM.getDiseaseName() != null){
                    tvDiseaseName.setText(oDM.getDiseaseName());
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

