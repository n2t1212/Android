package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.DM_Employee;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DM_Employee> employeeList;
    public List<DM_Employee> SelectedList=new ArrayList<DM_Employee>();

    public EmployeeAdapter() {
        employeeList = new ArrayList<>();
    }


    public void setEmployeeList(List<DM_Employee> oEmployeeList) {
        this.employeeList = oEmployeeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new EmployeeHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_employee,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        if (viewHolder instanceof EmployeeAdapter.EmployeeHolder){
            ((EmployeeAdapter.EmployeeHolder) viewHolder).bind(employeeList.get(iPos));
        }

        setRowSelected(SelectedList.contains(employeeList.get(iPos)),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(employeeList.get(iPos))){
                    SelectedList.remove(employeeList.get(iPos));
                    setRowSelected(false,viewHolder);
                }else{
                    SelectedList.add(employeeList.get(iPos));
                    setRowSelected(true,viewHolder);
                }
                //notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    class EmployeeHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvEmployeeCode)
        CustomTextView tvEmployeeCode;
        @BindView(R.id.tvEmployeeName)
        CustomTextView tvEmployeeName;
        @BindView(R.id.tvNotes)
        CustomTextView tvNotes;

        public EmployeeHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(DM_Employee employee){
            if (employee != null){
                if(employee.getEmployeeCode()!=null) {
                    tvEmployeeCode.setText(employee.getEmployeeCode());
                }
                if (employee.getEmployeeName() != null){
                    tvEmployeeName.setText(employee.getEmployeeName());
                }
                if (employee.getNotes() != null){
                    tvNotes.setText(employee.getNotes());
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

