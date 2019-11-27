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
import com.mimi.mimigroup.model.DM_Customer_Search;
import com.mimi.mimigroup.model.DM_Product;
import com.mimi.mimigroup.model.SM_PlanSaleDetail;
import com.mimi.mimigroup.ui.adapter.PlanSaleDetailAdapter;
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
import butterknife.OnTextChanged;

public class PlanSaleDetailItemFragment extends BaseFragment {
    @BindView(R.id.rvPlanSaleDetailList)
    RecyclerView rvPlanSaleDetailList;
    @BindView(R.id.spCustomer)
    AutoCompleteTextView spCustomer;
    @BindView(R.id.spProduct)
    AutoCompleteTextView spProduct;
    @BindView(R.id.tvAmountBox)
    CustomBoldEditText tvAmountBox;
    @BindView(R.id.tvAmount)
    CustomBoldEditText tvAmount;
    @BindView(R.id.tvNotes)
    CustomBoldEditText tvNotes;

    @BindView(R.id.Layout_PlanSaleDetailItem)
    LinearLayout Layout_PlanSaleDetailItem;

    PlanSaleDetailAdapter adapter;
    private String mPlanSaleId = "";
    private String mParSymbol = "";
    private String mAction = "";
    private String mRowSelectedID="";

    List<SM_PlanSaleDetail> lstPlanSaleDetail;

    List<DM_Customer_Search> lstCustomer;
    List<DM_Product> lstProduct;

    DBGimsHelper mDB = null;
    private DM_Product oProductSel;
    private Float mConvertBox;
    private DM_Customer_Search oCustomerSel;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_sale_plan_detail_form;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDB = DBGimsHelper.getInstance(view.getContext());
        lstCustomer = mDB.getAllCustomerSearch();
        lstProduct = mDB.getAllProduct();
        adapter = new PlanSaleDetailAdapter(new PlanSaleDetailAdapter.ListItemClickListener() {
            @Override
            public void onItemClick(List<SM_PlanSaleDetail> SelectList) {
                if (SelectList.size() > 0) {
                    for (SM_PlanSaleDetail osmDT : SelectList) {
                        setPlanSaleDetailRow(osmDT);
                        break;
                    }
                    //Chỉ cho phép sửa khi chọn 1 dòng
                    if (SelectList.size() > 1) {
                        ((PlanSaleFormActivity) getActivity()).setButtonEditStatus(false);
                        Layout_PlanSaleDetailItem.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Bạn chọn quá nhiều để có thể sửa..", Toast.LENGTH_SHORT).show();
                    } else {
                        ((PlanSaleFormActivity) getActivity()).setButtonEditStatus(true);
                        mRowSelectedID=SelectList.get(0).getPlanDetailId();
                    }
                    ((PlanSaleFormActivity) getActivity()).setVisibleDetailDelete(true);
                } else {
                    ((PlanSaleFormActivity) getActivity()).setVisibleDetailDelete(false);
                    ((PlanSaleFormActivity) getActivity()).setButtonEditStatus(false);
                }
            }
        });
        rvPlanSaleDetailList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPlanSaleDetailList.setAdapter(adapter);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //GET PARAMETER FORM ACTIVITY
                        PlanSaleFormActivity oActivity = (PlanSaleFormActivity) getActivity();
                        lstPlanSaleDetail = oActivity.getoPlanSaleDetail();
                        mPlanSaleId = oActivity.getmPlanSaleID();
                        mAction = oActivity.getAction();
                        if (lstPlanSaleDetail != null) {
                            adapter.setsmoPlanSaleDetail(lstPlanSaleDetail);
                        }
                        mParSymbol = oActivity.getmPar_Symbol();
                        initDropDownSP();
                        initDropdownCustomer();
                    }
                }, 300);

        Layout_PlanSaleDetailItem.setVisibility(View.GONE);
    }

    private void initDropDownSP(){
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
                    }catch (Exception ex){}
                }
            });

        }catch (Exception ex){}
    }



    // CALL FROM ACTIVITY
    public List<SM_PlanSaleDetail> getListPlanSaleDetail() {
        return lstPlanSaleDetail;
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

    private void setPlanSaleDetailRow(SM_PlanSaleDetail osmDT) {
        try {
            if (osmDT != null) {
                if(lstCustomer != null && lstCustomer.size() > 0 && osmDT.getCustomerId()!= null && !osmDT.getCustomerId().isEmpty()){
                    for(DM_Customer_Search cus: lstCustomer){
                        if(cus.getCustomerid().equals(osmDT.getCustomerId())){
                            oCustomerSel = cus;
                            break;
                        }
                    }
                    if(oCustomerSel != null && oCustomerSel.getCustomerid() != null && !oCustomerSel.getCustomerid().isEmpty()){
                        spCustomer.setText(oCustomerSel.getCustomerName());
                    }
                }

                if(lstProduct != null && lstProduct.size() > 0 && osmDT.getProductCode() != null && !osmDT.getProductCode().isEmpty()){
                    for(DM_Product pro: lstProduct){
                        if(pro.getProductCode().equals(osmDT.getProductCode())){
                            oProductSel = pro;
                            break;
                        }
                    }

                    if(oProductSel != null){
                        if(oProductSel.getProductName()!= null){
                            spProduct.setText(oProductSel.getProductName());
                        }
                        if(oProductSel.getConvertBox() != null){
                            mConvertBox = oProductSel.getConvertBox();
                        }else{
                            mConvertBox = Float.valueOf(0);
                        }
                    }
                }

                if (osmDT.getAmountBox() != null) {
                    tvAmountBox.setText(osmDT.getAmountBox().toString());
                }
                if (osmDT.getAmount() != null) {
                    tvAmount.setText(osmDT.getAmount().toString());
                }

                if (osmDT.getNotes() != null) {
                    tvNotes.setText(osmDT.getNotes());
                }
            } else {
                Toast.makeText(getContext(), "Không tồn tại kế hoạch bán hàng chi tiết..", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            Toast.makeText(getContext(), "Không thể khởi tạo thông tin để sửa..", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean onAddPlanSaleDetail(boolean isAddnew) {
        ((PlanSaleFormActivity) getActivity()).setVisibleDetailDelete(false);
        if (Layout_PlanSaleDetailItem.getVisibility() == View.GONE) {
            Layout_PlanSaleDetailItem.setVisibility(View.VISIBLE);
            try {
                if (isAddnew) {
                    spCustomer.setText("");
                    spCustomer.requestFocus();
                    spProduct.setText("");
                    tvAmountBox.setText("");
                    tvAmount.setText("");
                    tvNotes.setText("");
                    mRowSelectedID="";
                }
            } catch (Exception ex) {
            }
            return true;
        }
        return true;
    }

    public boolean onSavePlanSaleDetail() {
        try {
            if (onSaveAddPlanSaleDetail()) {
                if (Layout_PlanSaleDetailItem.getVisibility() == View.VISIBLE) {
                    Layout_PlanSaleDetailItem.setVisibility(View.GONE);
                    adapter.clearSelected();
                }
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }

    private boolean onSaveAddPlanSaleDetail() {
        if (spCustomer.getText() == null || spCustomer.getText().toString().isEmpty() || oCustomerSel==null) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập khách hàng...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            spCustomer.requestFocus();
            return false;
        }
        if (spProduct.getText() == null || spProduct.getText().toString().isEmpty() || oProductSel==null) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập sản phẩm...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            spProduct.requestFocus();
            return false;
        }

        if(oProductSel!=null && oProductSel.getProductCode()!=null && !oProductSel.getProductCode().isEmpty()){
            SM_PlanSaleDetail oDetail = new SM_PlanSaleDetail();

            oDetail.setCustomerId(oCustomerSel.getCustomerid());
            oDetail.setCustomerCode(oCustomerSel.getCustomerCode());
            oDetail.setCustomerName(oCustomerSel.getCustomerName());

            oDetail.setProductCode(oProductSel.getProductCode());
            oDetail.setProductName(oProductSel.getProductName());
            oDetail.setUnit(oProductSel.getUnit());
            oDetail.setSpec(oProductSel.getSpecification());
            if (tvAmountBox.getText() == null || tvAmountBox.getText().toString().isEmpty()) {
                Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập số lượng thùng...", Toast.LENGTH_LONG);
                oT.setGravity(Gravity.CENTER, 0, 0);
                oT.show();
                tvAmountBox.requestFocus();
                return false;
            } else {
                oDetail.setAmountBox(Double.valueOf(tvAmountBox.getText().toString()));
            }
            if (tvAmount.getText() == null || tvAmount.getText().toString().isEmpty()) {
                Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập số lượng...", Toast.LENGTH_LONG);
                oT.setGravity(Gravity.CENTER, 0, 0);
                oT.show();
                tvAmount.requestFocus();
                return false;
            } else {
                oDetail.setAmount(Double.valueOf(tvAmount.getText().toString()));
            }
            oDetail.setNotes(tvNotes.getText().toString());

            boolean isExist = false;
            for (int i = 0; i < lstPlanSaleDetail.size(); i++) {
                if (lstPlanSaleDetail.get(i).getPlanDetailId().equalsIgnoreCase(mRowSelectedID)) {
                    isExist = true;
                    if (oDetail.getCustomerId() != null) {
                        lstPlanSaleDetail.get(i).setCustomerId(oDetail.getCustomerId());
                    } else {
                        lstPlanSaleDetail.get(i).setCustomerId("");
                    }
                    if (oDetail.getProductCode() != null) {
                        lstPlanSaleDetail.get(i).setProductCode(oDetail.getProductCode());
                    } else {
                        lstPlanSaleDetail.get(i).setProductCode("");
                    }
                    if (oDetail.getProductName() != null) {
                        lstPlanSaleDetail.get(i).setProductName(oDetail.getProductName());
                    } else {
                        lstPlanSaleDetail.get(i).setProductName("");
                    }
                    if (oDetail.getUnit() != null) {
                        lstPlanSaleDetail.get(i).setUnit(oDetail.getUnit());
                    } else {
                        lstPlanSaleDetail.get(i).setUnit("");
                    }
                    if (oDetail.getSpec() != null) {
                        lstPlanSaleDetail.get(i).setSpec(oDetail.getSpec());
                    } else {
                        lstPlanSaleDetail.get(i).setSpec("");
                    }

                    if (oDetail.getAmountBox() != null) {
                        lstPlanSaleDetail.get(i).setAmountBox(oDetail.getAmountBox());
                    } else {
                        lstPlanSaleDetail.get(i).setAmountBox(0d);
                    }
                    if (oDetail.getAmount() != null) {
                        lstPlanSaleDetail.get(i).setAmount(oDetail.getAmount());
                    } else {
                        lstPlanSaleDetail.get(i).setAmount(0d);
                    }
                    if (oDetail.getNotes() != null) {
                        lstPlanSaleDetail.get(i).setNotes(oDetail.getNotes());
                    } else {
                        lstPlanSaleDetail.get(i).setNotes("");
                    }
                    Toast.makeText(getContext(), "Chi tiết kế hoạch đã được thêm..", Toast.LENGTH_SHORT).show();
                    break;
                }
            }

            if (!isExist) {
                SimpleDateFormat Od = new SimpleDateFormat("ddMMyyyyHHmmssSS");
                String mMarketId = "CT" + mParSymbol + Od.format(new Date());
                oDetail.setPlanDetailId(mMarketId);
                oDetail.setPlanId(mPlanSaleId);
                lstPlanSaleDetail.add(oDetail);
            }

            adapter.setsmoPlanSaleDetail(lstPlanSaleDetail);
            Toast.makeText(getContext(), String.valueOf(lstPlanSaleDetail.size()) + ": kế hoạch bán hàng chi tiết được chọn..", Toast.LENGTH_SHORT).show();

            spCustomer.setText("");
            spCustomer.requestFocus();
            spProduct.setText("");
            spProduct.requestFocus();
            tvAmountBox.setText("");
            tvAmount.setText("");
            tvNotes.setText("");
            mRowSelectedID="";

            return true;
        }else{
            Toast.makeText(getContext(),"Không có sản phẩm nào được chọn..",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void onDeletedPlanSaleDetail() {
        if (adapter.SelectedList == null || adapter.SelectedList.size() <= 0) {
            Toast oToat = Toast.makeText(getContext(), "Bạn chưa chọn kế hoạch bán hàng chi tiết để xóa...", Toast.LENGTH_LONG);
            oToat.setGravity(Gravity.CENTER, 0, 0);
            oToat.show();
            return;
        }

        final Dialog oDlg = new Dialog(getContext());
        oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        oDlg.setContentView(R.layout.dialog_yesno);
        oDlg.setTitle("");
        CustomTextView dlgTitle = (CustomTextView) oDlg.findViewById(R.id.dlgTitle);
        dlgTitle.setText("Xác nhận");
        CustomTextView dlgContent = (CustomTextView) oDlg.findViewById(R.id.dlgContent);
        dlgContent.setText("Bạn có chắc muốn xóa ?");
        CustomBoldTextView btnYes = (CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
        CustomBoldTextView btnNo = (CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (SM_PlanSaleDetail oDTSel : adapter.SelectedList) {
                    for (SM_PlanSaleDetail oDT : lstPlanSaleDetail) {
                        if (oDTSel.equals(oDT)) {
                            lstPlanSaleDetail.remove(oDT);
                            break;
                        }
                    }
                }
                adapter.SelectedList.clear();
                adapter.setsmoPlanSaleDetail(lstPlanSaleDetail);

                // Set view
                rvPlanSaleDetailList.setAdapter(adapter);
                ((PlanSaleFormActivity) getActivity()).setVisibleDetailDelete(false);
                ((PlanSaleFormActivity) getActivity()).setButtonEditStatus(false);
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

    @OnTextChanged(R.id.tvAmountBox)
    public void onAmountBoxChanged(){
        try{
            if(!spProduct.getText().toString().isEmpty()){
                if(mConvertBox>0){
                    Double iAmount=0.0;
                    iAmount=Double.valueOf(tvAmountBox.getText().toString())*mConvertBox;
                    tvAmount.setText(String.valueOf(iAmount.intValue()));
                }else{
                    Toast oT= Toast.makeText(getContext(),"Không đọc được quy đổi thùng...",Toast.LENGTH_SHORT);
                    oT.setGravity(Gravity.CENTER,0,0);
                    oT.show();
                }
            }else{
                Toast oT= Toast.makeText(getContext(),"Vui lòng chọn sản phẩm trước...",Toast.LENGTH_SHORT);
                oT.setGravity(Gravity.CENTER,0,0);
                oT.show();
            }

        }catch (Exception ex){}
    }
}