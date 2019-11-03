package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.QR_QRSCAN;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<QR_QRSCAN> qrScanList;
    public List<QR_QRSCAN> SelectedList=new ArrayList<QR_QRSCAN>();

    public HistoryAdapter() {
       qrScanList=new ArrayList<QR_QRSCAN>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HistoryHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_history, viewGroup, false));
    }

    @Override
     public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        if (viewHolder instanceof HistoryHolder){
            if(this.qrScanList!=null && !this.qrScanList.isEmpty()) {
                ((HistoryHolder) viewHolder).bind(qrScanList.get(iPos));
            }
        }

        setRowSelected(SelectedList.contains(qrScanList.get(iPos)),qrScanList.get(iPos).getScanType(),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(qrScanList.get(iPos))){
                    SelectedList.remove(qrScanList.get(iPos));
                    setRowSelected(false,qrScanList.get(iPos).getScanType(),viewHolder);
                }else{
                    SelectedList.add(qrScanList.get(iPos));
                    setRowSelected(true,qrScanList.get(iPos).getScanType(),viewHolder);
                }
                //notifyDataSetChanged();
            }
        });

    }

    public void setHistoryList(List<QR_QRSCAN> mQrScanList) {
        this.qrScanList = mQrScanList;
         notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        try {
            return this.qrScanList.size();
        }catch (Exception ex){return  0;}
    }

    public class HistoryHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvScanDay)
        CustomTextView tvScanDay;
        @BindView(R.id.tvisSync)
        CustomTextView tvisSync;
        @BindView(R.id.tvScanNo)
        CustomTextView tvScanNo;

        @BindView(R.id.tvProductCode)
        CustomTextView tvProductCode;
        @BindView(R.id.tvProductName)
        CustomTextView tvProductName;
        @BindView(R.id.tvUnit)
        CustomTextView tvUnit;
        @BindView(R.id.tvSpec)
        CustomTextView tvSpec;
        @BindView(R.id.tvQRCustomer)
        CustomTextView tvQRCustomer;
        @BindView(R.id.tvQRLocationAddress)
        CustomTextView tvQRLocationAddress;
        @BindView(R.id.tvQRScanSupport)
        CustomTextView tvQRScanSupport;


        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(QR_QRSCAN oQr) {
            try {
                if (oQr != null) {
                    if (oQr.getScanDay() != null) {
                        tvScanDay.setText(oQr.getScanDay());
                    }
                    if (oQr.getProductCode() != null) {
                        tvProductCode.setText(oQr.getProductCode());
                    }
                    if (oQr.getProductName() != null) {
                        tvProductName.setText(oQr.getProductName());
                    }
                    if (oQr.getUnit() != null) {
                        tvUnit.setText(oQr.getUnit());
                    }
                    if (oQr.getSpecification() != null) {
                        tvSpec.setText(oQr.getSpecification());
                    }
                    if (oQr.getLocationAddress() != null) {
                        tvQRLocationAddress.setText(oQr.getLocationAddress());
                    }
                    if (oQr.getCustomerName() != null) {
                        tvQRCustomer.setText(oQr.getCustomerName());
                    }
                    if(oQr.getScanNo()!=null){
                        tvScanNo.setText(oQr.getScanNo().toString());
                    }
                    if(oQr.getSync()!=null){
                        if(oQr.getSync()==true || oQr.getSync().equals(true) || oQr.getSync().equals("1")){
                            tvisSync.setText("[x]");
                        }else{
                            tvisSync.setText("");
                        }
                    }
                    if(oQr.getScanSupportDesc()!=null && !oQr.getScanSupportDesc().isEmpty()){
                        tvQRScanSupport.setText(oQr.getScanSupportDesc());
                    }else if(oQr.getScanSupportID()!=null && !oQr.getScanSupportID().isEmpty()){
                        tvQRScanSupport.setText(oQr.getScanSupportID());
                    }

                }
            }catch (Exception e){
                Log.d("ERR_BIND_QRSCAN",e.getMessage());
            }
        }
    }


    public void setRowSelected(boolean isSelected,String ScanType,RecyclerView.ViewHolder viewHolder){
        if(isSelected){
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#F8D8E7"));
            viewHolder.itemView.setSelected(true);
        }else {
            //viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            //viewHolder.itemView.setSelected(false);
            if(ScanType.equalsIgnoreCase("N")){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#f7e6ff"));
                viewHolder.itemView.setSelected(false);
            }else {
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                viewHolder.itemView.setSelected(false);
            }

        }
    }

}
