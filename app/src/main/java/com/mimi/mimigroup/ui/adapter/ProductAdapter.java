package com.mimi.mimigroup.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.model.DM_Product;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DM_Product> productList;
    public List<DM_Product> SelectedList=new ArrayList<DM_Product>();

    public ProductAdapter() {
        productList = new ArrayList<>();
    }


    public void setProductList(List<DM_Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ProductHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int iPos) {
        if (viewHolder instanceof ProductAdapter.ProductHolder){
            ((ProductAdapter.ProductHolder) viewHolder).bind(productList.get(iPos));
        }

        setRowSelected(SelectedList.contains(productList.get(iPos)),viewHolder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedList.contains(productList.get(iPos))){
                    SelectedList.remove(productList.get(iPos));
                    setRowSelected(false,viewHolder);
                }else{
                    SelectedList.add(productList.get(iPos));
                    setRowSelected(true,viewHolder);
                }
                //notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvProductCode)
        CustomTextView tvProductCode;
        @BindView(R.id.tvProductName)
        CustomTextView tvProductName;
        @BindView(R.id.tvUnit)
        CustomTextView tvUnit;
        @BindView(R.id.tvSpec)
        CustomTextView tvSpec;
        @BindView(R.id.tvConvertKgl)
        CustomTextView tvConvertKgl;
        @BindView(R.id.tvConvertBox)
        CustomTextView tvConvertBox;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(DM_Product product){
            if (product != null){
                tvProductCode.setText(product.getProductCode());
                if (product.getProductName() != null){
                    tvProductName.setText(product.getProductName());
                }
                if (product.getUnit() != null){
                    tvUnit.setText(product.getUnit());
                }
                if (product.getSpecification() != null){
                    tvSpec.setText(product.getSpecification());
                }
                if (product.getConvertKgl() != null){
                    tvConvertKgl.setText(product.getConvertKgl().toString());
                }
                if (product.getConvertBox() != null){
                    tvConvertBox.setText(product.getConvertBox().toString());
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

