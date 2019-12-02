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
import com.mimi.mimigroup.model.DM_TreeStages;

import java.util.ArrayList;
import java.util.List;

public class SearchTreeStagesAdapter extends ArrayAdapter<DM_TreeStages> implements Filterable {

    int resource;
    List<DM_TreeStages> oTreeStages, tempTree, suggestions;
    Context ctx;

    public SearchTreeStagesAdapter(Context context, int resource, ArrayList<DM_TreeStages> mTree) {
        super(context, resource, mTree);
        this.ctx=context;
        this.resource=resource;
        this.oTreeStages=mTree;
        this.tempTree=new ArrayList<DM_TreeStages>(this.oTreeStages);
        this.suggestions=new ArrayList<DM_TreeStages>();
    }

    @Override
    public int getCount() {
        return this.oTreeStages.size();
    }

    @Override
    public DM_TreeStages getItem(int position) {
        return this.oTreeStages.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.search_tree_stages, parent, false);
            }
            DM_TreeStages itemCus = this.oTreeStages.get(position);
            if (itemCus  != null) {
                TextView txtTreeCode = (TextView) view.findViewById(R.id.txtcb_stagescode);
                if (txtTreeCode != null) txtTreeCode.setText(itemCus.getStagesCode());
                TextView txtTreeName = (TextView) view.findViewById(R.id.txtcb_stagesname);
                if (txtTreeName!= null) txtTreeName.setText(itemCus.getStagesName());
            }
        }catch (Exception ex) {
            Toast.makeText(ctx, "Không thể tìm giai đoạn:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        try {
            return nameFilter;
        }catch (Exception ex){
            Toast.makeText(ctx, "Không thể tìm giai đoạn:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    Filter  nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((DM_TreeStages) resultValue).getStagesName();
            return str;
        }
        @Override
        protected synchronized FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (DM_TreeStages itemCus : tempTree) {
                    if (itemCus.getStagesCode().toLowerCase().contains(constraint.toString().toLowerCase()) || itemCus.getStagesName().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
                oTreeStages= (ArrayList<DM_TreeStages>) results.values;
            } else {
                oTreeStages = new ArrayList<DM_TreeStages>();
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    };

}


