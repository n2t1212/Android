package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.DM_Customer;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    List<DM_Customer> customerList;
    List<DM_Customer> customerFilters;
    public List<DM_Customer> SelectedList=new ArrayList<DM_Customer>();

    public String mFilterCustomerCode="";
    public String mFilterCustomerName="";
    public String mFilterProvince="";
    public String mFilterDistrict="";

    //Trigger Onclick from Adapter
    public interface CustomerItemClickListener{
        void onCustomerItemClick(List<DM_Customer> SelectList);
    }
    private final CustomerItemClickListener mOnCustomerItemClicked;
    //ENDTRIGGER

    public CustomerAdapter(CustomerItemClickListener onCustomerItemClicked) {
        customerList = new ArrayList<DM_Customer>();
        customerFilters = new ArrayList<DM_Customer>();

        this.mOnCustomerItemClicked=onCustomerItemClicked;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CustomerHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_customer, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        //viewHolder.setIsRecyclable(false);
        if (viewHolder instanceof CustomerHolder){
            ((CustomerHolder) viewHolder).bind(customerFilters.get(iPos));
        }

        //setRowStatus(customerFilters.get(iPos).getEdit(),viewHolder);

        setRowSelected(SelectedList.contains(customerFilters.get(iPos)),customerFilters.get(iPos).getEdit(),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(customerFilters.get(iPos))){
                    SelectedList.remove(customerFilters.get(iPos));
                    setRowSelected(false,customerFilters.get(iPos).getEdit(),viewHolder);
                }else{
                    SelectedList.add(customerFilters.get(iPos));
                    setRowSelected(true,customerFilters.get(iPos).getEdit(),viewHolder);
                }

                mOnCustomerItemClicked.onCustomerItemClick(SelectedList);
                //notifyDataSetChanged();
            }
        });

    }

    public void setUpdate(DM_Customer mCus,Integer mEdit){
        try {
            for(DM_Customer oCus:customerFilters){
                if(mCus.getCustomerid()==oCus.getCustomerid()){
                    oCus.setEdit(mEdit);
                    oCus.setLatitudeTemp(mCus.getLatitudeTemp());
                    oCus.setLongitudeTemp(mCus.getLongitudeTemp());
                    oCus.setStatusDesc(mCus.getStatusDesc());
                    break;
                }
            }
            notifyDataSetChanged();
        }catch (Exception ex){}
    }
    public void setAddAdaptor(DM_Customer mCus){
        try {
            customerFilters.add(mCus);
            notifyDataSetChanged();
        }catch (Exception ex){}
    }

    public void setCustomerList(List<DM_Customer> customerList) {
        this.customerList = customerList;
        this.customerFilters = customerList;
        notifyDataSetChanged();
        SelectedList.clear();
    }

    @Override
    public int getItemCount() {
        try {
            return customerFilters.size();
        }catch (Exception ex){return  0;}
    }

    public void clearSelected(){
        SelectedList.clear();
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<DM_Customer> customerListSearch = new ArrayList<>();
                if (constraint.toString().trim().isEmpty()){
                    results.values = customerList;
                }else{
                      for (DM_Customer customer : customerList){
                        if(mFilterCustomerCode!="" && mFilterCustomerName!="" && mFilterProvince!="" && mFilterDistrict!=""){
                            if (customer.getCustomerCode().toString().toLowerCase().contains(mFilterCustomerCode.toString().toLowerCase()) && customer.getCustomerName().toString().toLowerCase().contains(mFilterCustomerName.toString().toLowerCase()) &&
                                    customer.getProvinceName().toString().toLowerCase().contains(mFilterProvince.toString().toLowerCase())  && customer.getDistrictName().toString().toLowerCase().contains(mFilterDistrict.toString().toLowerCase()) ){
                                customerListSearch.add(customer);
                            }
                        }else if(mFilterCustomerCode!="" && mFilterCustomerName!="" && mFilterProvince!=""){
                            if (customer.getCustomerCode().toString().toLowerCase().contains(mFilterCustomerCode.toString().toLowerCase()) && customer.getCustomerName().toString().toLowerCase().contains(mFilterCustomerName.toString().toLowerCase()) &&
                                    customer.getProvinceName().toString().toLowerCase().contains(mFilterProvince.toString().toLowerCase()) ){
                                customerListSearch.add(customer);
                            }
                        } else if(mFilterCustomerCode!="" && mFilterCustomerName!=""){
                            if (customer.getCustomerCode().toString().toLowerCase().contains(mFilterCustomerCode.toString().toLowerCase()) && customer.getCustomerName().toString().toLowerCase().contains(mFilterCustomerName.toString().toLowerCase())){
                                customerListSearch.add(customer);
                            }
                        }else if(mFilterProvince!="" && mFilterDistrict!=""){
                            if (customer.getProvinceName().toString().toLowerCase().contains(mFilterProvince.toString().toLowerCase()) && customer.getDistrictName().toString().toLowerCase().contains(mFilterDistrict.toString().toLowerCase())){
                                customerListSearch.add(customer);
                            }
                        }
                        else if(mFilterCustomerCode!=""){
                            if (customer.getCustomerCode().toString().toLowerCase().contains(mFilterCustomerCode.toString().toLowerCase())){
                                customerListSearch.add(customer);
                            }
                        }else if(mFilterCustomerName!=""){
                            if (customer.getCustomerName().toString().toLowerCase().contains(mFilterCustomerName.toString().toLowerCase())){
                                customerListSearch.add(customer);
                            }
                        }else if(mFilterProvince!=""){
                            if (customer.getProvinceName().toString().toLowerCase().contains(mFilterProvince.toString().toLowerCase())){
                                customerListSearch.add(customer);
                            }
                        }else if(mFilterDistrict!=""){
                            if (customer.getDistrictName().toString().toLowerCase().contains(mFilterDistrict.toString().toLowerCase())){
                                customerListSearch.add(customer);
                            }
                        }
                        else  if (customer.toString().toLowerCase().contains(constraint.toString().toLowerCase())){
                            customerListSearch.add(customer);
                        }
                    }

                    results.values = customerListSearch;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                customerFilters = (List<DM_Customer>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    OnSelectedCustomerListener listener;
    public void setListener(OnSelectedCustomerListener listener) {
        this.listener = listener;
    }

    public interface OnSelectedCustomerListener{
        void onSelectedCustomer(DM_Customer customer);
    }

    class CustomerHolder extends RecyclerView.ViewHolder{
        //@BindView(R.id.cbChon)
        //CheckBox cbChon;
        @BindView(R.id.tvMaKH)
        CustomTextView tvMaKH;
        @BindView(R.id.tvTenKH)
        CustomTextView tvTenKH;
        @BindView(R.id.tvPhone)
        CustomTextView tvPhone;
        @BindView(R.id.tvTax)
        CustomTextView tvTax;
        @BindView(R.id.tvDiachi)
        CustomTextView tvDiachi;
        @BindView(R.id.tvTinh)
        CustomTextView tvTinh;
        @BindView(R.id.tvHuyen)
        CustomTextView tvHuyen;
        @BindView(R.id.tvXa)
        CustomTextView tvXa;
        @BindView(R.id.tvCap)
        CustomTextView tvCap;
        @BindView(R.id.tvKinhdo)
        CustomTextView tvKinhdo;
        @BindView(R.id.tvVido)
        CustomTextView tvVido;
        @BindView(R.id.tvXeploai)
        CustomTextView tvXeploai; //Xanh,Vang,Cam,Đỏ
        @BindView(R.id.tvTrangthai)
        CustomTextView tvTrangthai;

        public CustomerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
           /*
            cbChon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (listener != null){
                        listener.onSelectedCustomer((DM_Customer) cbChon.getTag());
                    }
                }
            }); */
        }

        public void bind(DM_Customer customer){
            if (customer != null){
                if (customer.getCustomerCode() != null){
                    tvMaKH.setText(customer.getCustomerCode());
                }
                if (customer.getCustomerName() != null){
                    tvTenKH.setText(customer.getCustomerName());
                }
                if (customer.getTel() != null){
                    tvPhone.setText(customer.getTel());
                }
                if (customer.getTax() != null){
                    tvTax.setText(customer.getTax());
                }
                if(customer.getStreet()!=null){
                    tvDiachi.setText(customer.getStreet());
                }
                if(customer.getProvinceName()!=null){
                    tvTinh.setText(customer.getProvinceName());
                }
                if(customer.getDistrictName()!=null){
                    tvHuyen.setText(customer.getDistrictName());
                }
                if(customer.getWardName()!=null){
                    tvXa.setText(customer.getWardName());
                }
                if(Integer.toString(customer.getIsLevel())!=null){
                    tvCap.setText(Integer.toString(customer.getIsLevel()));
                }
                if(Double.toString(customer.getLongitudeTemp())!=null){
                    tvKinhdo.setText(Double.toString(customer.getLongitudeTemp()));
                }
                if(Double.toString(customer.getLatitudeTemp())!=null){
                    tvVido.setText(Double.toString(customer.getLatitudeTemp()));
                }
                if(customer.getStatusDesc()!=null) {
                    tvTrangthai.setText(customer.getStatusDesc());
                }
                if(customer.getRanked()!=null){
                    tvXeploai.setText(customer.getRanked());
                }
            }
        }
    }


    private void setRowSelected(boolean isSelected,Integer isEdit,RecyclerView.ViewHolder viewHolder){
        if(isSelected){
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#F8D8E7"));
            viewHolder.itemView.setSelected(true);
        }else {
            if(isEdit==1){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#cceeff"));
                viewHolder.itemView.setSelected(false);
            }else if(isEdit==2){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#eeccff"));
                viewHolder.itemView.setSelected(false);
            }else if(isEdit==4){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffeecc"));
                viewHolder.itemView.setSelected(false);
            }else if(isEdit==5){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#C03000"));
                viewHolder.itemView.setSelected(false);
            }else {
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                viewHolder.itemView.setSelected(false);
            }
        }
    }

    private void setRowStatus(Integer isEdit,RecyclerView.ViewHolder viewHolder){
        try{
            switch (isEdit){
                case 1:
                    viewHolder.itemView.setBackgroundColor(Color.parseColor("#cceeff"));
                case 2:
                    viewHolder.itemView.setBackgroundColor(Color.parseColor("#eeccff"));
                case 4:
                    viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffeecc"));
            }
        }catch (Exception ex){}
    }

}
