package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.DM_Product;
import com.mimi.mimigroup.model.DM_Tree;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TreeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DM_Tree> treeList;
    public List<DM_Tree> SelectedList=new ArrayList<DM_Tree>();

    public TreeAdapter() {
        treeList = new ArrayList<>();
    }


    public void setTreeList(List<DM_Tree> treeList) {
        this.treeList = treeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TreeHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_tree_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        if (viewHolder instanceof TreeAdapter.TreeHolder){
            ((TreeAdapter.TreeHolder) viewHolder).bind(treeList.get(iPos));
        }

        setRowSelected(SelectedList.contains(treeList.get(iPos)),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(treeList.get(iPos))){
                    SelectedList.remove(treeList.get(iPos));
                    setRowSelected(false,viewHolder);
                }else{
                    SelectedList.add(treeList.get(iPos));
                    setRowSelected(true,viewHolder);
                }
                //notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return treeList.size();
    }

    class TreeHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvTreeCode)
        CustomTextView tvTreeCode;
        @BindView(R.id.tvTreeName)
        CustomTextView tvTreeName;

        public TreeHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(DM_Tree oDM){
            if (oDM != null){
                tvTreeCode.setText(oDM.getTreeCode());
                if (oDM.getTreeName() != null){
                    tvTreeName.setText(oDM.getTreeName());
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

