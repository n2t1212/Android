package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mimi.mimigroup.R;
import com.mimi.mimigroup.api.APINet;
import com.mimi.mimigroup.api.APINetCallBack;
import com.mimi.mimigroup.api.SyncCallBack;
import com.mimi.mimigroup.api.SyncGet;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.base.BaseFragment;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.DM_TreeStages;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.ui.setting.TreeDiseaseFragment;
import com.mimi.mimigroup.utils.AppUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TreeStagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DM_TreeStages> treeStagesList;
    public List<DM_TreeStages> SelectedList=new ArrayList<DM_TreeStages>();

    public TreeStagesAdapter() {
        treeStagesList = new ArrayList<>();
    }


    public void setTreeStagesList(List<DM_TreeStages> treeStagesList) {
        this.treeStagesList = treeStagesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TreeStagesAdapter.TreeStagesHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_tree_stages_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        if (viewHolder instanceof TreeStagesAdapter.TreeStagesHolder){
            ((TreeStagesAdapter.TreeStagesHolder) viewHolder).bind(treeStagesList.get(iPos));
        }

        setRowSelected(SelectedList.contains(treeStagesList.get(iPos)),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(treeStagesList.get(iPos))){
                    SelectedList.remove(treeStagesList.get(iPos));
                    setRowSelected(false,viewHolder);
                }else{
                    SelectedList.add(treeStagesList.get(iPos));
                    setRowSelected(true,viewHolder);
                }
                //notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return treeStagesList.size();
    }

    class TreeStagesHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvStagesCode)
        CustomTextView tvStagesCode;
        @BindView(R.id.tvStagesName)
        CustomTextView tvStagesName;

        public TreeStagesHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(DM_TreeStages oDM){
            if (oDM != null){
                if (oDM.getStagesCode() != null){
                    tvStagesCode.setText(oDM.getStagesCode());
                }
                if (oDM.getStagesName() != null){
                    tvStagesName.setText(oDM.getStagesName());
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

