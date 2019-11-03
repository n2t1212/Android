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
import com.mimi.mimigroup.model.KS_Search;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    List<KS_Search> searchList;
    List<KS_Search> searchListFilter;
    public List<KS_Search> SelectedList=new ArrayList<KS_Search>();
    public String mFilterCustomerCode="";
    public String mFilterCustomerName="";
    public String mFilterProvince="";
    public String mFilterEmployeeName="";

    //Trigger Onclick from Adapter
    public interface SearchItemClickListener{
        void onSearchItemClick(List<KS_Search> SelectList);
    }
    private final SearchItemClickListener mOnSearchItemClicked;
    //ENDTRIGGER

    public SearchAdapter(SearchItemClickListener onSearchItemClick) {
        searchList = new ArrayList<KS_Search>();
        searchListFilter=new ArrayList<KS_Search>();
        this.mOnSearchItemClicked=onSearchItemClick;
    }


    public void setSearchList(List<KS_Search> msearchList) {
        this.searchList = msearchList;
        this.searchListFilter=msearchList;
        notifyDataSetChanged();
        SelectedList.clear();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SearchHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_qr_fragment_item,viewGroup,false));
    }


    private RecyclerView.ViewHolder oldHolder=null;

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        if (viewHolder instanceof SearchAdapter.SearchHolder){
            ((SearchAdapter.SearchHolder) viewHolder).bind(searchListFilter.get(iPos));
        }

        setRowSelected(SelectedList.contains(searchListFilter.get(iPos)),viewHolder);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (oldHolder != null) {
                        setRowSelected(false, oldHolder);
                    }
                    oldHolder = viewHolder;
                }catch (Exception ex){}

                if(SelectedList.contains(searchListFilter.get(iPos))){
                    SelectedList.remove(searchListFilter.get(iPos));
                    setRowSelected(false,viewHolder);
                }else{
                    SelectedList.clear();
                    SelectedList.add(searchListFilter.get(iPos));
                    setRowSelected(true,viewHolder);
                }


                mOnSearchItemClicked.onSearchItemClick(SelectedList);
                //notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        try {
            return searchListFilter.size();
        }catch (Exception ex){return  0;}
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<KS_Search> mListSearch = new ArrayList<KS_Search>();
                if (constraint.toString().trim().isEmpty()){
                    results.values = searchList;
                }else{
                    for (KS_Search oSearch : searchList){
                        if(!mFilterCustomerCode.isEmpty() && !mFilterCustomerName.isEmpty() && !mFilterProvince.isEmpty() && !mFilterEmployeeName.isEmpty()){
                            if (oSearch.getCustomerCode().toString().toLowerCase().contains(mFilterCustomerCode.toString().toLowerCase()) && oSearch.getCustomerName().toString().toLowerCase().contains(mFilterCustomerName.toString().toLowerCase()) &&
                                    oSearch.getProvince().toString().toLowerCase().contains(mFilterProvince.toString().toLowerCase())  && oSearch.getEmployeeName().toString().toLowerCase().contains(mFilterEmployeeName.toString().toLowerCase()) ){
                                mListSearch.add(oSearch);
                            }
                        }else if(mFilterCustomerCode!="" && mFilterCustomerName!="" && mFilterProvince!=""){
                            if (oSearch.getCustomerCode().toString().toLowerCase().contains(mFilterCustomerCode.toString().toLowerCase()) && oSearch.getCustomerName().toString().toLowerCase().contains(mFilterCustomerName.toString().toLowerCase()) &&
                                    oSearch.getProvince().toString().toLowerCase().contains(mFilterProvince.toString().toLowerCase()) ){
                                mListSearch.add(oSearch);
                            }
                        } else if(mFilterCustomerCode!="" && mFilterCustomerName!=""){
                            if (oSearch.getCustomerCode().toString().toLowerCase().contains(mFilterCustomerCode.toString().toLowerCase()) && oSearch.getCustomerName().toString().toLowerCase().contains(mFilterCustomerName.toString().toLowerCase())){
                                mListSearch.add(oSearch);
                            }
                        }else if(mFilterProvince!="" && mFilterEmployeeName!=""){
                            if (oSearch.getProvince().toString().toLowerCase().contains(mFilterProvince.toString().toLowerCase()) && oSearch.getEmployeeName().toString().toLowerCase().contains(mFilterEmployeeName.toString().toLowerCase())){
                                mListSearch.add(oSearch);
                            }
                        }else if(mFilterCustomerCode!="" && mFilterEmployeeName!=""){
                            if (oSearch.getCustomerCode().toString().toLowerCase().contains(mFilterCustomerCode.toString().toLowerCase()) && oSearch.getEmployeeName().toString().toLowerCase().contains(mFilterEmployeeName.toString().toLowerCase())){
                                mListSearch.add(oSearch);
                            }
                        }
                        else if(mFilterCustomerCode!=""){
                            if (oSearch.getCustomerCode().toString().toLowerCase().contains(mFilterCustomerCode.toString().toLowerCase())){
                                mListSearch.add(oSearch);
                            }
                        }else if(mFilterCustomerName!=""){
                            if (oSearch.getCustomerName().toString().toLowerCase().contains(mFilterCustomerName.toString().toLowerCase())){
                                mListSearch.add(oSearch);
                            }
                        }else if(mFilterProvince!=""){
                            if (oSearch.getProvince().toString().toLowerCase().contains(mFilterProvince.toString().toLowerCase())){
                                mListSearch.add(oSearch);
                            }
                        }else if(mFilterEmployeeName!=""){
                            if (oSearch.getEmployeeName().toString().toLowerCase().contains(mFilterEmployeeName.toString().toLowerCase())){
                                mListSearch.add(oSearch);
                            }
                        }
                        else  if (oSearch.toString().toLowerCase().contains(constraint.toString().toLowerCase())){
                            mListSearch.add(oSearch);
                        }
                    }

                    results.values = mListSearch;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                searchListFilter = (List<KS_Search>) results.values;
                notifyDataSetChanged();
            }
        };
    }



    class SearchHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvSearchSTT)
        CustomTextView tvSearchSTT;
        @BindView(R.id.tvSearchCusCode)
        CustomTextView tvSearchCusCode;
        @BindView(R.id.tvSearchCusName)
        CustomTextView tvSearchCusName;
        @BindView(R.id.tvSearchProvince)
        CustomTextView tvSearchProvince;
        @BindView(R.id.tvSearchRanked)
        CustomTextView tvSearchRanked;
        @BindView(R.id.tvSearchEmployee)
        CustomTextView tvSearchEmployee;
        @BindView(R.id.tvSearchEmployeeTel)
        CustomTextView tvSearchEmployeeTel;
        @BindView(R.id.tvSearchAddress)
        CustomTextView tvSearchAddress;
        @BindView(R.id.tvSearchTimeScan)
        CustomTextView tvSearchTimeScan;

        @BindView(R.id.tvSearchProductCode)
        CustomTextView tvSearchProductCode;
        @BindView(R.id.tvSearchProductName)
        CustomTextView tvSearchProductName;
        @BindView(R.id.tvSearchProductSpec)
        CustomTextView tvSearchProductSpec;
        @BindView(R.id.tvSearchQuantity)
        CustomTextView tvSearchQuantity;

        public SearchHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(KS_Search mSearch){
            if (mSearch != null){
                tvSearchSTT.setText(Integer.toString((mSearch.getSeqno())));
                if (mSearch.getCustomerCode() != null){
                    tvSearchCusCode.setText(mSearch.getCustomerCode());
                }
                if (mSearch.getCustomerName() != null){
                    tvSearchCusName.setText(mSearch.getCustomerName());
                }
                if (mSearch.getProvince() != null){
                    tvSearchProvince.setText(mSearch.getProvince());
                }
                if (mSearch.getRanked() != null){
                    tvSearchRanked.setText(mSearch.getRanked());
                }
                if (mSearch.getEmployeeName() != null){
                    tvSearchEmployee.setText(mSearch.getEmployeeName());
                }
                if (mSearch.getEmployeeTel() != null){
                    tvSearchEmployeeTel.setText(mSearch.getEmployeeTel());
                }
                if (mSearch.getAddressScan() != null){
                    tvSearchAddress.setText(mSearch.getAddressScan());
                }

                if (mSearch.getTimeScan() != null){
                    tvSearchTimeScan.setText(mSearch.getTimeScan());
                }

                if (mSearch.getProductCode() != null){
                    tvSearchProductCode.setText(mSearch.getProductCode());
                }
                if (mSearch.getProductName() != null){
                    tvSearchProductName.setText(mSearch.getProductName());
                }
                if (mSearch.getSpecification() != null){
                    tvSearchProductSpec.setText(mSearch.getSpecification());
                }
                if (mSearch.getQuantity() != null){
                    tvSearchQuantity.setText(Integer.toString(mSearch.getQuantity()));
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

