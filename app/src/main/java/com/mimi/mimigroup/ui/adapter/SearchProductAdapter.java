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
import com.mimi.mimigroup.model.DM_Product;

import java.util.ArrayList;
import java.util.List;

public class SearchProductAdapter extends ArrayAdapter<DM_Product> implements  Filterable {

    int resource;
    List<DM_Product>oProduct, tempProduct, suggestions;
    Context ctx;

    public SearchProductAdapter(Context context, int resource, ArrayList<DM_Product> mProduct) {
        super(context, resource, mProduct);
        this.ctx=context;
        this.resource=resource;
        this.oProduct=mProduct;
        this.tempProduct=new ArrayList<DM_Product>(this.oProduct);
        this.suggestions=new ArrayList<DM_Product>();
    }

    @Override
    public int getCount() {
        return this.oProduct.size();
    }

    @Override
    public DM_Product getItem(int position) {
        return this.oProduct.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.search_product, parent, false);
            }
            DM_Product itemSP = this.oProduct.get(position);
            if (itemSP != null) {
                TextView txtProductCode = (TextView) view.findViewById(R.id.txtcb_productcode);
                if (txtProductCode != null) txtProductCode.setText(itemSP.getProductCode());

                TextView txtProductName = (TextView) view.findViewById(R.id.txtcb_productname);
                if (txtProductName != null) {
                    if(itemSP.getSpecification()!=null && !itemSP.getSpecification().isEmpty()){
                        txtProductName.setText(itemSP.getProductName() + " - "+itemSP.getSpecification()+"");
                    }else {
                        txtProductName.setText(itemSP.getProductName());
                    }
                }

                //TextView txtSpec = (TextView) view.findViewById(R.id.txtcb_spec);
                //if (txtSpec!= null) txtSpec.setText(itemSP.getSpecification());
            }
        }catch (Exception ex) {
            Toast.makeText(ctx, "Không thể tìm sản phẩm:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
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
            String str = ((DM_Product) resultValue).getProductName();
            return str;
        }
        @Override
        protected synchronized FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (DM_Product itemSP : tempProduct) {
                    if (itemSP.getProductCode().toLowerCase().contains(constraint.toString().toLowerCase()) || itemSP.getProductName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(itemSP);
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
                oProduct= (ArrayList<DM_Product>) results.values;
            } else {
                oProduct = new ArrayList<DM_Product>();
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    };

}

