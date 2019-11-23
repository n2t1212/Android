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
import com.mimi.mimigroup.model.DM_Tree;

import java.util.ArrayList;
import java.util.List;

public class SearchTreeAdapter extends ArrayAdapter<DM_Tree> implements Filterable {

    int resource;
    List<DM_Tree> oTree, tempTree, suggestions;
    Context ctx;

    public SearchTreeAdapter(Context context, int resource, ArrayList<DM_Tree> mTree) {
        super(context, resource, mTree);
        this.ctx=context;
        this.resource=resource;
        this.oTree=mTree;
        this.tempTree=new ArrayList<DM_Tree>(this.oTree);
        this.suggestions=new ArrayList<DM_Tree>();
    }

    @Override
    public int getCount() {
        return this.oTree.size();
    }

    @Override
    public DM_Tree getItem(int position) {
        return this.oTree.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.search_tree, parent, false);
            }
            DM_Tree itemCus = this.oTree.get(position);
            if (itemCus  != null) {
                TextView txtTreeCode = (TextView) view.findViewById(R.id.txtcb_treecode);
                if (txtTreeCode != null) txtTreeCode.setText(itemCus.getTreeCode());
                TextView txtTreeName = (TextView) view.findViewById(R.id.txtcb_treename);
                if (txtTreeName!= null) txtTreeName.setText(itemCus.getTreeName());
            }
        }catch (Exception ex) {
            Toast.makeText(ctx, "Không thể tìm cây trồng:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        try {
            return nameFilter;
        }catch (Exception ex){
            Toast.makeText(ctx, "Không thể tìm cây trồng:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    Filter  nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((DM_Tree) resultValue).getTreeName();
            return str;
        }
        @Override
        protected synchronized FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (DM_Tree itemCus : tempTree) {
                    if (itemCus.getTreeCode().toLowerCase().contains(constraint.toString().toLowerCase()) || itemCus.getTreeName().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
                oTree= (ArrayList<DM_Tree>) results.values;
            } else {
                oTree = new ArrayList<DM_Tree>();
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    };

}


