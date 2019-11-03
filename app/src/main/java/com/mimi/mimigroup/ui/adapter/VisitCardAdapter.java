package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.SM_VisitCard;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VisitCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<SM_VisitCard> smVisitcardList;
    public List<SM_VisitCard> SelectedList=new ArrayList<SM_VisitCard>();

    //Trigger Onclick from Adapter
    public interface ListItemClickListener{
        void onItemClick(List<SM_VisitCard> SelectList);
    }
    private final ListItemClickListener mOnItemClicked;

    public VisitCardAdapter(ListItemClickListener mOnClickListener) {
        smVisitcardList=new ArrayList<SM_VisitCard>();

        this.mOnItemClicked=mOnClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VisitCardHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_visitcard, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        if (viewHolder instanceof VisitCardHolder){
            if(this.smVisitcardList!=null && !this.smVisitcardList.isEmpty()) {
                ((VisitCardHolder) viewHolder).bind(smVisitcardList.get(iPos));
            }
        }

        setRowSelected(SelectedList.contains(smVisitcardList.get(iPos)),smVisitcardList.get(iPos).getVisitType(),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(smVisitcardList.get(iPos))){
                    SelectedList.remove(smVisitcardList.get(iPos));
                    setRowSelected(false,smVisitcardList.get(iPos).getVisitType(),viewHolder);
                }else{
                    SelectedList.add(smVisitcardList.get(iPos));
                    setRowSelected(true,smVisitcardList.get(iPos).getVisitType(),viewHolder);
                }

                mOnItemClicked.onItemClick(SelectedList);
                //notifyDataSetChanged();
            }
        });

    }

    public void setVisitCardList(List<SM_VisitCard> mSMVisitcardLst) {
        this.smVisitcardList = mSMVisitcardLst;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        try {
            return this.smVisitcardList.size();
        }catch (Exception ex){return  0;}
    }

    public class VisitCardHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvVisitDay)
        CustomTextView tvVisitDay;

        @BindView(R.id.tvVisitType)
        CustomTextView tvVisitType;

        @BindView(R.id.tvVisitCustomer)
        CustomTextView tvVisitCustomer;
        @BindView(R.id.tvVisitTime)
        CustomTextView tvVisitTime;
        @BindView(R.id.tvVisitAddress)
        CustomTextView tvVisitAddress;

        @BindView(R.id.tvVisitNotes)
        CustomTextView tvVisitNotes;
        @BindView(R.id.tvVisitSync)
        CustomTextView tvVisitSync;
        @BindView(R.id.tvVisitSyncDay)
        CustomTextView tvVisitSyncDay;


        public VisitCardHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(SM_VisitCard oQr) {
            try {
                if (oQr != null) {
                    if (oQr.getVisitDay() != null) {
                        tvVisitDay.setText(oQr.getVisitDay());
                    }
                    if (oQr.getCustomerName() != null) {
                        tvVisitCustomer.setText(oQr.getCustomerName());
                    }
                    if (oQr.getVisitType() != null) {
                        if(oQr.getVisitType().equalsIgnoreCase("IN")){
                            tvVisitType.setText("Vào");
                        }else if(oQr.getVisitType().equalsIgnoreCase("OUT")) {
                            tvVisitType.setText("Ra");
                        }
                    }
                    if (oQr.getVisitTime() != null) {
                        tvVisitTime.setText(oQr.getVisitTime());
                    }
                    if (oQr.getLocationAddress() != null) {
                        tvVisitAddress.setText(oQr.getLocationAddress());
                    }
                    if (oQr.getVisitNotes() != null) {
                        tvVisitNotes.setText(oQr.getVisitNotes());
                    }

                    if(oQr.getSync()!=null){
                        if(oQr.getSync()==true || oQr.getSync().equals(true) || oQr.getSync().equals("1")){
                            tvVisitSync.setText("[x]");
                        }else{
                            tvVisitSync.setText("");
                        }
                    }
                    if (oQr.getSyncDay() != null) {
                        tvVisitSyncDay.setText(oQr.getSyncDay());
                    }
                }
            }catch (Exception e){
                Log.d("ERR_BIND_VISITCARD",e.getMessage());
            }
        }
    }


    private void setRowSelected(boolean isSelected,String VisitType,RecyclerView.ViewHolder viewHolder){
        if(isSelected){
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#F8D8E7"));
            viewHolder.itemView.setSelected(true);
        }else {
            if(VisitType.equalsIgnoreCase("OUT")){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#f7e6ff"));
                viewHolder.itemView.setSelected(false);
            }else {
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                viewHolder.itemView.setSelected(false);
            }
        }
    }


}
