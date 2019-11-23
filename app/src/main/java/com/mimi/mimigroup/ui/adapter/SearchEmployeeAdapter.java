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
import com.mimi.mimigroup.model.DM_Employee;

import java.util.ArrayList;
import java.util.List;

public class SearchEmployeeAdapter extends ArrayAdapter<DM_Employee> implements Filterable {

    int resource;
    List<DM_Employee> oEmployee, tempEmployee, suggestions;
    Context ctx;

    public SearchEmployeeAdapter(Context context, int resource, List<DM_Employee> mEmployee) {
        super(context, resource, mEmployee);
        this.ctx=context;
        this.resource=resource;
        this.oEmployee=mEmployee;
        this.tempEmployee=new ArrayList<DM_Employee>(this.oEmployee);
        this.suggestions=new ArrayList<DM_Employee>();
    }

    @Override
    public int getCount() {
        return this.oEmployee.size();
    }

    @Override
    public DM_Employee getItem(int position) {
        return this.oEmployee.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.search_employee, parent, false);
            }
            DM_Employee itemCus = this.oEmployee.get(position);
            if (itemCus  != null) {
                TextView txtEmployeeCode = (TextView) view.findViewById(R.id.txtcb_employeecode);
                if (txtEmployeeCode != null) txtEmployeeCode.setText(itemCus.getEmployeeCode());
                TextView txtEmployeeName = (TextView) view.findViewById(R.id.txtcb_employeename);
                if (txtEmployeeName!= null) txtEmployeeName.setText(itemCus.getEmployeeName());
                TextView txtShortName = (TextView) view.findViewById(R.id.txtcb_shortname);
            }
        }catch (Exception ex) {
            Toast.makeText(ctx, "Không thể tìm nhân viên:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        try {
            return nameFilter;
        }catch (Exception ex){
            Toast.makeText(ctx, "Không thể tìm nhân viên:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    Filter  nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((DM_Employee) resultValue).getEmployeeName();
            return str;
        }
        @Override
        protected synchronized FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (DM_Employee itemCus : tempEmployee) {
                    if (itemCus.getEmployeeCode().toLowerCase().contains(constraint.toString().toLowerCase()) || itemCus.getEmployeeName().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
                oEmployee= (ArrayList<DM_Employee>) results.values;
            } else {
                oEmployee = new ArrayList<DM_Employee>();
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    };

}


