package com.mimi.mimigroup.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.DM_Customer_Search;

import java.util.ArrayList;
import java.util.List;

public class SearchCustomerAdapter extends ArrayAdapter<DM_Customer_Search> implements  Filterable {

    int resource;
    List<DM_Customer_Search>oCustomer, tempCustomer, suggestions;
    Context ctx;

    public SearchCustomerAdapter(Context context, int resource, ArrayList<DM_Customer_Search> mCustomer) {
        super(context, resource, mCustomer);
        this.ctx=context;
        this.resource=resource;
        this.oCustomer=mCustomer;
        this.tempCustomer=new ArrayList<DM_Customer_Search>(this.oCustomer);
        this.suggestions=new ArrayList<DM_Customer_Search>();
    }

    @Override
    public int getCount() {
        return this.oCustomer.size();
    }

    @Override
    public DM_Customer_Search getItem(int position) {
        return this.oCustomer.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.search_customer, parent, false);
            }
            DM_Customer_Search itemCus = this.oCustomer.get(position);
            if (itemCus  != null) {
                TextView txtCustomerCode = (TextView) view.findViewById(R.id.txtcb_customercode);
                if (txtCustomerCode != null) txtCustomerCode.setText(itemCus.getCustomerCode());
                TextView txtCustomerName = (TextView) view.findViewById(R.id.txtcb_customername);
                if (txtCustomerName!= null) txtCustomerName.setText(itemCus.getCustomerName());
                TextView txtShortName = (TextView) view.findViewById(R.id.txtcb_shortname);
                if (txtShortName!= null) txtShortName.setText(itemCus.getShortName());
            }
        }catch (Exception ex) {
            Toast.makeText(ctx, "Không thể tìm khách hàng:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        try {
            return nameFilter;
        }catch (Exception ex){
            Toast.makeText(ctx, "Không thể tìm sản phẩm:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    Filter  nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((DM_Customer_Search) resultValue).getCustomerName();
            return str;
        }
        @Override
        protected synchronized FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (DM_Customer_Search itemCus : tempCustomer) {
                    if (itemCus.getCustomerCode().toLowerCase().contains(constraint.toString().toLowerCase()) || itemCus.getCustomerName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(itemCus);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                oCustomer= (ArrayList<DM_Customer_Search>) results.values;
            } else {
                oCustomer = new ArrayList<DM_Customer_Search>();
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    };

}

