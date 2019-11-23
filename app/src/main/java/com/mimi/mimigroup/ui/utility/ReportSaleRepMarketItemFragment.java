package com.mimi.mimigroup.ui.utility;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.base.BaseFragment;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.DM_Customer;
import com.mimi.mimigroup.model.DM_Customer_Search;
import com.mimi.mimigroup.model.DM_Product;
import com.mimi.mimigroup.model.SM_ReportSaleRepMarket;
import com.mimi.mimigroup.ui.adapter.ReportSaleRepMarketAdapter;
import com.mimi.mimigroup.ui.adapter.SearchCustomerAdapter;
import com.mimi.mimigroup.ui.adapter.SearchProductAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldEditText;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class ReportSaleRepMarketItemFragment extends BaseFragment {

    @BindView(R.id.rvReportSaleRepMarketList)
    RecyclerView rvReportSaleRepMarketList;

    @BindView(R.id.spCustomer)
    AutoCompleteTextView spCustomer;

    @BindView(R.id.spProduct)
    AutoCompleteTextView spProduct;

    @BindView(R.id.tvCompanyName)
    CustomBoldEditText tvCompanyName;
    @BindView(R.id.tvPrice)
    CustomBoldEditText tvPrice;
    @BindView(R.id.tvNotes)
    CustomBoldEditText tvNotes;

    @BindView(R.id.Layout_ReportSaleRepMarketItem)
    LinearLayout Layout_ReportSaleRepMarketItem;

    ReportSaleRepMarketAdapter adapter;
    private String mReportSaleId = "";
    private String mParSymbol = "";
    private String mAction = "";

    List<SM_ReportSaleRepMarket> lstReportSaleRepMarket;
    SM_ReportSaleRepMarket oReport;
    String currentMarketId;
    List<DM_Customer_Search> lstCustomer;
    List<DM_Product> lstProduct;
    private DM_Customer_Search oCustomerSel;
    private DM_Product oProductSel;
    private Float mConvertBox;

    DBGimsHelper mDB = null;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_report_sale_rep_market_form;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDB = DBGimsHelper.getInstance(view.getContext());
        lstCustomer=mDB.getAllCustomerSearch();
        lstProduct=mDB.getAllProduct();
        adapter = new ReportSaleRepMarketAdapter(new ReportSaleRepMarketAdapter.ListItemClickListener() {
            @Override
            public void onItemClick(List<SM_ReportSaleRepMarket> SelectList) {
                if (SelectList.size() > 0) {
                    for (SM_ReportSaleRepMarket osmDT : SelectList) {
                        setReportSaleRepMarketRow(osmDT);
                        break;
                    }
                    //Chỉ cho phép sửa khi chọn 1 dòng
                    if (SelectList.size() > 1) {
                        ((ReportSaleRepFormActivity) getActivity()).setButtonEditStatus(false);
                        Layout_ReportSaleRepMarketItem.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Bạn chọn quá nhiều để có thể sửa..", Toast.LENGTH_SHORT).show();
                    } else {
                        ((ReportSaleRepFormActivity) getActivity()).setButtonEditStatus(true);
                    }
                    ((ReportSaleRepFormActivity) getActivity()).setVisibleDetailDelete(true);
                } else {
                    ((ReportSaleRepFormActivity) getActivity()).setVisibleDetailDelete(false);
                    ((ReportSaleRepFormActivity) getActivity()).setButtonEditStatus(false);
                }
            }
        });
        rvReportSaleRepMarketList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReportSaleRepMarketList.setAdapter(adapter);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //GET PARAMETER FORM ACTIVITY
                        ReportSaleRepFormActivity oActivity = (ReportSaleRepFormActivity) getActivity();
                        lstReportSaleRepMarket = oActivity.getoReportSaleRepMarket();
                        mReportSaleId=oActivity.getmReportSaleRepID();
                        mAction=oActivity.getAction();
                        if (lstReportSaleRepMarket != null) {
                            adapter.setsmoReportSaleRepMarket(lstReportSaleRepMarket);
                        }
                        mParSymbol=oActivity.getmPar_Symbol();
                        initDropdownCustomer();
                        initDropdownSP();
                    }} ,300);

        Layout_ReportSaleRepMarketItem.setVisibility(View.GONE);
    }

    private void initDropdownCustomer(){
        try{
            ArrayList<DM_Customer_Search> oListCus=new ArrayList<DM_Customer_Search>();
            for(int i=0;i<lstCustomer.size();++i){
                oListCus.add(new DM_Customer_Search(lstCustomer.get(i).getCustomerid(),lstCustomer.get(i).getCustomerCode(),
                        lstCustomer.get(i).getCustomerName(),lstCustomer.get(i).getShortName(),lstCustomer.get(i).getProvinceName(),
                        lstCustomer.get(i).getLongititude(),lstCustomer.get(i).getLatitude()));
            }
            SearchCustomerAdapter adapter = new SearchCustomerAdapter(getContext(), R.layout.search_customer,oListCus);
            spCustomer.setDropDownBackgroundResource(R.drawable.liner_dropdownlist);
            spCustomer.setThreshold(1);
            spCustomer.setAdapter(adapter);
            spCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int iPosition, long l) {
                    try {
                        oCustomerSel = (DM_Customer_Search) adapterView.getItemAtPosition(iPosition);
                        String mUnit=oCustomerSel.getCustomerid();
                        if(oCustomerSel!=null && oCustomerSel.getCustomerid()!=""){
                            oReport.setCustomerId(oCustomerSel.getCustomerid());
                        }

                    }catch (Exception ex){}
                }
            });

        }catch (Exception ex){}
    }

    private void initDropdownSP(){
        try{
            ArrayList<DM_Product> oListSP=new ArrayList<DM_Product>();
            for(int i=0;i<lstProduct.size();++i){
                oListSP.add(new DM_Product(lstProduct.get(i).getProductCode(),lstProduct.get(i).getProductName(),lstProduct.get(i).getUnit(),lstProduct.get(i).getSpecification(),lstProduct.get(i).getConvertBox(),false));
            }

            SearchProductAdapter adapter = new SearchProductAdapter(getContext(), R.layout.search_product,oListSP);
            spProduct.setDropDownBackgroundResource(R.drawable.liner_dropdownlist);
            spProduct.setThreshold(1);
            spProduct.setAdapter(adapter);
            spProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int iPosition, long l) {
                    try {
                        oProductSel = (DM_Product) adapterView.getItemAtPosition(iPosition);
                        if(oProductSel!=null){
                            if(oProductSel.getConvertBox()!=null){
                                mConvertBox=oProductSel.getConvertBox();
                            }else{
                                mConvertBox=Float.valueOf(0);
                            }
                        }

                    }catch (Exception ex){}
                }
            });
            spProduct.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        spProduct.selectAll();
                    }
                }
            });

        }catch (Exception ex){}
    }

    // CALL FROM ACTIVITY
    public List<SM_ReportSaleRepMarket> getListReportSaleRepMarket(){
        return lstReportSaleRepMarket;
    }

    private int setPosSpin(List<String> lstSpin, String mValue) {
        try {
            for (int i = 0; i < lstSpin.size(); ++i) {
                if (lstSpin.get(i).toString().equals(mValue)) {
                    return i;
                }
            }
            return -1;
        } catch (Exception ex) {
            return -1;
        }
    }

    private String getSpin(final Spinner oSpin) {
        try {
            int iPos = oSpin.getSelectedItemPosition();
            if (iPos <= 0) {
                return "";
            }
            return oSpin.getItemAtPosition(iPos).toString();
        } catch (Exception ex) {
        }
        return null;
    }

    private void setReportSaleRepMarketRow(SM_ReportSaleRepMarket osmDT) {
        try {
            if (osmDT != null) {
                if (osmDT.getCustomerId() != null) {
                    for(DM_Customer_Search cus: lstCustomer){
                        if(cus.getCustomerid().equals(osmDT.getCustomerId())){
                            oCustomerSel = cus;
                            break;
                        }
                    }
                    if(oCustomerSel != null && oCustomerSel.getCustomerName() != null){
                        spCustomer.setText(oCustomerSel.getCustomerName());
                    }else{
                        spCustomer.setText("");
                    }

                }

                if(osmDT.getProductCode() != null)
                {
                    for(DM_Product pro: lstProduct){
                        if(pro.getProductCode().equals(osmDT.getProductCode())){
                            oProductSel = pro;
                        }
                    }
                    if(oProductSel != null && oProductSel.getProductName() != null){
                        spProduct.setText(oProductSel.getProductName());
                    }else{
                        spProduct.setText("");
                    }

                }

                if(osmDT.getCompanyName() != null){
                    tvCompanyName.setText(osmDT.getCompanyName());
                }

                if(osmDT.getPrice() != null){
                    tvPrice.setText(osmDT.getPrice().toString());
                }

                if (osmDT.getNotes() != null) {
                    tvNotes.setText(osmDT.getNotes());
                }

                currentMarketId = osmDT.getMarketId();
            } else {
                Toast.makeText(getContext(), "Không tồn tại báo cáo thị trường..", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            Toast.makeText(getContext(), "Không thể khởi tạo thông tin để sửa..", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean onAddReportSaleRepMarket(boolean isAddnew) {
        ((ReportSaleRepFormActivity) getActivity()).setVisibleDetailDelete(false);
        if (Layout_ReportSaleRepMarketItem.getVisibility() == View.GONE) {
            Layout_ReportSaleRepMarketItem.setVisibility(View.VISIBLE);
            //Clear Input
            try {
                if (isAddnew) {
                    spCustomer.setText("");
                    spProduct.setText("");
                    tvCompanyName.setText("");
                    tvPrice.setText("");
                    tvNotes.setText("");
                }
            } catch (Exception ex) {
            }
            return true;
        }
        return true;
    }

    public boolean onSaveReportSaleRepMarket(){
        try{
            if(onSaveAddReportSaleRepMarket()){
                if(Layout_ReportSaleRepMarketItem.getVisibility()==View.VISIBLE) {
                    Layout_ReportSaleRepMarketItem.setVisibility(View.GONE);
                    adapter.clearSelected();
                }
                return  true;
            }
        }catch (Exception ex){return  false;}
        return  false;
    }

    private boolean onSaveAddReportSaleRepMarket() {
        SM_ReportSaleRepMarket oDetail = new SM_ReportSaleRepMarket();

        if(oCustomerSel == null || oCustomerSel.getCustomerid() == null){
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập khách hàng...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            spCustomer.requestFocus();
            return false;
        } else {
            oDetail.setCustomerId(oCustomerSel.getCustomerid());
        }

        if(oProductSel == null || oProductSel.getProductCode().isEmpty()){
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập sản phẩm...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            spProduct.requestFocus();
            return false;
        } else {
            oDetail.setProductCode(oProductSel.getProductCode());
        }
        // EDIT
        if(currentMarketId != null && currentMarketId.length() > 0){
            oDetail.setMarketId(currentMarketId);
            currentMarketId = "";
        }

        if (tvCompanyName.getText() == null || tvCompanyName.getText().toString().isEmpty()) {
            /*Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập tên công ty...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            return false;*/
            oDetail.setCompanyName("");
        } else {
            oDetail.setCompanyName(tvCompanyName.getText().toString());
        }

        if (tvPrice.getText() == null || tvPrice.getText().toString().isEmpty()) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập giá...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            tvPrice.requestFocus();
            return false;
        } else {
            oDetail.setPrice(Float.parseFloat(tvPrice.getText().toString()));
        }

        if (tvNotes.getText() == null || tvNotes.getText().toString().isEmpty()) {
            oDetail.setNotes("");
        } else {
            oDetail.setNotes(tvNotes.getText().toString());
        }

        boolean isExist = false;
        for (int i = 0; i < lstReportSaleRepMarket.size(); i++) {
            if (lstReportSaleRepMarket.get(i).getMarketId().equalsIgnoreCase(oDetail.getMarketId())) {
                isExist = true;
                if (oDetail.getCustomerId() != null) {
                    lstReportSaleRepMarket.get(i).setCustomerId(oDetail.getCustomerId());
                } else {
                    lstReportSaleRepMarket.get(i).setCustomerId("");
                }
                if (oDetail.getProductCode() != null) {
                    lstReportSaleRepMarket.get(i).setProductCode(oDetail.getProductCode());
                } else {
                    lstReportSaleRepMarket.get(i).setProductCode("");
                }
                if (oDetail.getCompanyName() != null) {
                    lstReportSaleRepMarket.get(i).setCompanyName(oDetail.getCompanyName());
                } else {
                    lstReportSaleRepMarket.get(i).setCompanyName("");
                }
                if (oDetail.getPrice() != null) {
                    lstReportSaleRepMarket.get(i).setPrice(oDetail.getPrice());
                } else {
                    lstReportSaleRepMarket.get(i).setPrice(0f);
                }
                if (oDetail.getNotes() != null) {
                    lstReportSaleRepMarket.get(i).setNotes(oDetail.getNotes());
                } else {
                    lstReportSaleRepMarket.get(i).setNotes("");
                }

                Toast.makeText(getContext(), "Báo cáo thị trường đã được thêm..", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        if (!isExist) {
            SimpleDateFormat Od = new SimpleDateFormat("ddMMyyyyHHmmssSS");
            String mMarketId = "BCSALETT" + mParSymbol + Od.format(new Date());
            oDetail.setMarketId(mMarketId);
            oDetail.setReportSaleId(mReportSaleId);
            lstReportSaleRepMarket.add(oDetail);
        }

        adapter.setsmoReportSaleRepMarket(lstReportSaleRepMarket);
        Toast.makeText(getContext(), String.valueOf(lstReportSaleRepMarket.size()) + ": Báo cáo thị trường được chọn..", Toast.LENGTH_SHORT).show();

        spCustomer.setText("");
        spProduct.setText("");
        tvCompanyName.setText("");
        tvNotes.setText("");
        tvPrice.setText("");

        oProductSel = null;
        oCustomerSel = null;
        return true;
    }

    public void onDeletedReportSaleRepMarket(){
        if(adapter.SelectedList==null || adapter.SelectedList.size()<=0) {
            Toast oToat=Toast.makeText(getContext(),"Bạn chưa chọn báo cáo thị trường để xóa...",Toast.LENGTH_LONG);
            oToat.setGravity(Gravity.CENTER,0,0);
            oToat.show();
            return;
        }

        final Dialog oDlg=new Dialog(getContext());
        oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        oDlg.setContentView(R.layout.dialog_yesno);
        oDlg.setTitle("");
        CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
        dlgTitle.setText("Xác nhận");
        CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
        dlgContent.setText("Bạn có chắc muốn xóa ?");
        CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
        CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(SM_ReportSaleRepMarket oDTSel:adapter.SelectedList){
                    for(SM_ReportSaleRepMarket oDT:lstReportSaleRepMarket){
                        if(oDTSel.equals(oDT)){
                            lstReportSaleRepMarket.remove(oDT);
                            break;
                        }
                    }
                }
                adapter.SelectedList.clear();
                adapter.setsmoReportSaleRepMarket(lstReportSaleRepMarket);

                // Set view
                rvReportSaleRepMarketList.setAdapter(adapter);
                ((ReportSaleRepFormActivity) getActivity()).setVisibleDetailDelete(false);
                ((ReportSaleRepFormActivity) getActivity()).setButtonEditStatus(false);
                Toast.makeText(getContext(), "Đã xóa mẫu tin thành công", Toast.LENGTH_SHORT).show();

                oDlg.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oDlg.dismiss();
                return;
            }
        });
        oDlg.show();
    }
}
