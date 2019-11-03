package com.mimi.mimigroup.ui.adapter;


import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mimi.mimigroup.R;

import com.mimi.mimigroup.model.QR_QRSCAN_HIS;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryServerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<QR_QRSCAN_HIS> qrScanList;
    public List<QR_QRSCAN_HIS> SelectedList=new ArrayList<QR_QRSCAN_HIS>();

    public HistoryServerAdapter() {
        qrScanList=new ArrayList<QR_QRSCAN_HIS>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HistoryServerHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_history_server, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        if (viewHolder instanceof HistoryServerHolder){
            if(this.qrScanList!=null && !this.qrScanList.isEmpty()) {
                ((HistoryServerHolder) viewHolder).bind(qrScanList.get(iPos));
            }
        }

        setRowSelected(SelectedList.contains(qrScanList.get(iPos)),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(qrScanList.get(iPos))){
                    SelectedList.remove(qrScanList.get(iPos));
                    setRowSelected(false,viewHolder);
                }else{
                    SelectedList.add(qrScanList.get(iPos));
                    setRowSelected(true,viewHolder);
                }
                //notifyDataSetChanged();
            }
        });


    }

    public void setHistoryServerList(List<QR_QRSCAN_HIS> mQrScanList) {
        this.qrScanList = mQrScanList;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        try {
            return this.qrScanList.size();
        }catch (Exception ex){return  0;}
    }

    public class HistoryServerHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvHisSererProductCode)
        CustomTextView tvHisSererProductCode;
        @BindView(R.id.tvHisSererProductName)
        CustomTextView tvHisSererProductName;
        @BindView(R.id.tvHisServerScanday)
        CustomTextView tvHisSererScanDay;

        @BindView(R.id.tvHisServerEmployee)
        CustomTextView tvHisServerEmployee;
        @BindView(R.id.tvHisServerCustomer)
        CustomTextView tvHisServerCustomer;
        @BindView(R.id.tvHisServerScanNo)
        CustomTextView tvHisServerScanNo;
        @BindView(R.id.tvHisServerLocationAddress)
        CustomTextView tvHisServerLocationAddress;


        public HistoryServerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(QR_QRSCAN_HIS oQr) {
            try {
                if (oQr != null) {
                    if (oQr.getProductCode() != null) {
                        tvHisSererProductCode.setText(oQr.getProductCode());
                    }
                    if (oQr.getProductName() != null) {
                        tvHisSererProductName.setText(oQr.getProductName());
                    }
                    if (oQr.getScanDay() != null) {
                        tvHisSererScanDay.setText(oQr.getScanDay());
                    }
                    if (oQr.getEmployee() != null) {
                        tvHisServerEmployee.setText(oQr.getEmployee());
                    }
                    if (oQr.getCustomerName() != null) {
                        tvHisServerCustomer.setText(oQr.getCustomerName());
                    }
                    if(oQr.getLocationAddress()!=null) {
                        tvHisServerLocationAddress.setText(oQr.getLocationAddress());
                    }
                    if(!oQr.getScanNo().toString().isEmpty()) {
                       tvHisServerScanNo.setText(oQr.getScanNo().toString());
                    }

                }
            }catch (Exception e){
                Log.d("ERR_BIND_QRHIS_SERVER",e.getMessage());
            }
        }
    }


    public void setRowSelected(boolean isSelected,RecyclerView.ViewHolder viewHolder){
        if(isSelected){
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#F8D8E7"));
            viewHolder.itemView.setSelected(true);
        }else {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            viewHolder.itemView.setSelected(false);
        }
    }


}
