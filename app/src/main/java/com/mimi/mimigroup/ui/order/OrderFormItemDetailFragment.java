package com.mimi.mimigroup.ui.order;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.base.BaseFragment;
import com.mimi.mimigroup.model.DM_Product;
import com.mimi.mimigroup.model.DM_Tree;
import com.mimi.mimigroup.model.SM_OrderDetail;
import com.mimi.mimigroup.ui.adapter.OrderDetailAdapter;
import com.mimi.mimigroup.ui.adapter.SearchProductAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldEditText;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class OrderFormItemDetailFragment extends BaseFragment{
    @BindView(R.id.rvOrderDetailList)
    RecyclerView rvOrderDetailList;

    @BindView(R.id.spProductOrderDetail)
    AutoCompleteTextView spProductOrderDetail;
    //@BindView(R.id.spProductUnit)
    //Spinner spProductUnit;
    @BindView(R.id.tvAmountDetail)
    CustomBoldEditText tvAmountDetail;
    @BindView(R.id.tvAmountBoxDetail)
    CustomBoldEditText tvAmountBoxDetail;

    @BindView(R.id.tvPriceDetail)
    CustomBoldEditText tvPriceDetail;
    @BindView(R.id.tvOriginMoneyDetail)
    CustomBoldTextView tvOriginMoneyDetail;
    @BindView(R.id.tvOrderDetailNotes)
    CustomBoldEditText tvOrderDetailNotes;

    /*@BindView(R.id.spTree)
    Spinner spTree;
    */
    @BindView(R.id.tvSPTree)
    CustomTextView tvSPTree;

    @BindView(R.id.Layout_OrderItem)
    LinearLayout Layout_OrderItem;

    OrderDetailAdapter adapter;
    private String mOrderID="";
    private String mParSymbol="";
    private String mAction="";

    List<SM_OrderDetail> lstOderDt;
    List<DM_Product> lstProduct;
    List<DM_Tree> lstTree;

    //List<String> lstUnit;
    //List<String> lstUnitDefault;
    private DM_Product oProductSel;
    private Float mConvertBox;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_order_form_detail;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TRIGGER ONCLICK ROW
        adapter = new OrderDetailAdapter(new OrderDetailAdapter.ListItemClickListener() {
            @Override
            public void onItemClick(List<SM_OrderDetail> SelectList) {
                if(SelectList.size()>0){
                    for(SM_OrderDetail osmDT:SelectList) {
                        setOrderDetailProductRow(osmDT);
                        break;
                    }
                    //Chỉ cho phép sửa khi chọn 1 dòng
                    if(SelectList.size()>1){
                        ((OrderFormActivity) getActivity()).setButtonEditStatus(false);
                        Layout_OrderItem.setVisibility(View.GONE);
                        Toast.makeText(getContext(),"Bạn chọn quá nhiều để có thể sửa..",Toast.LENGTH_SHORT).show();
                    }else{
                        ((OrderFormActivity) getActivity()).setButtonEditStatus(true);
                    }
                    ((OrderFormActivity) getActivity()).setVisibleDetailDelete(true);
                }else{
                    ((OrderFormActivity) getActivity()).setVisibleDetailDelete(false);
                    ((OrderFormActivity) getActivity()).setButtonEditStatus(false);
                }
            }
        });
        rvOrderDetailList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOrderDetailList.setAdapter(adapter);

        //POSTDELAY -> GET PARA FROM ACTIVITY
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //GET PARAMETER FORM ACTIVITY
                        OrderFormActivity oActivity = (OrderFormActivity) getActivity();
                        lstOderDt = oActivity.getOrderDetailActivity();
                        mOrderID=oActivity.getOrderID();
                        mAction=oActivity.getAction();
                        if (lstOderDt != null) {
                            adapter.setOrderDetailList(lstOderDt);
                        }
                        lstProduct=oActivity.getListProduct();
                        mParSymbol=oActivity.getParSymbol();
                        lstTree=oActivity.getListTree();
                        initDropdownSP();
                        initDropdownCT();
           }} ,300);

        Layout_OrderItem.setVisibility(View.GONE);
    }

    //CALL FROM ACTIVITY
    public List<SM_OrderDetail> getListOrderDetail(){
        return lstOderDt;
    }


    private String[] lstTreeSelect;
    private boolean[] lstTreeSelectChecked;
    private ArrayList<Integer> lstTreeSelectPos = new ArrayList<>();

    private void initDropdownCT(){
        try{
            if(lstTree==null){lstTree=new ArrayList<DM_Tree>();}
            //lstTree.add(new DM_Tree(1,"LUA","Cây lúa"));

            lstTreeSelect=new String[lstTree.size()];
            lstTreeSelectChecked=new boolean[lstTree.size()];

            for(int i=0;i<lstTree.size();i++){
               lstTreeSelect[i]=lstTree.get(i).getTreeName();
               lstTreeSelectChecked[i]=false;
            }
        }catch (Exception ex){}
    }

    private String getListTreeName(String mlstTreeSelect){
        try{
            String mTreeListName="";
            lstTreeSelectPos.clear();
            if(!mlstTreeSelect.isEmpty()){
                String[] mlstTreeCode=mlstTreeSelect.split(",");
                for(int iP=0;iP<mlstTreeCode.length;iP++){
                   for(int jP=0;jP<lstTree.size();jP++){
                       if(lstTree.get(jP).getTreeCode().contains(mlstTreeCode[iP])){
                           lstTreeSelectPos.add(jP);
                           mTreeListName=mTreeListName+lstTree.get(jP).getTreeName();
                           if(iP!=mlstTreeCode.length-1) {
                               mTreeListName =mTreeListName + ",";
                           }
                       }
                   }
                }
            }
            return  mTreeListName;
        }catch (Exception ex){return  "";}
    }

    @OnClick(R.id.tvSPTree)
    public void onClickTree(){
        try{
            Arrays.fill(lstTreeSelectChecked,false);
            for(int i=0;i<lstTreeSelectPos.size();i++){
                lstTreeSelectChecked[lstTreeSelectPos.get(i)]=true;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false);
            builder.setTitle("Chọn cây trồng");
            builder.setMultiChoiceItems(lstTreeSelect,lstTreeSelectChecked, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                   if(isChecked){
                       lstTreeSelectPos.add(position);
                   }else{
                       lstTreeSelectPos.remove(Integer.valueOf(position));
                   }
                }
            });

            builder.setPositiveButton("Chấp nhận", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    tvSPTree.setText("");
                    String mTreeSelectCode="",mTreeSelectName="";
                    for (int i = 0; i<lstTreeSelectPos.size(); i++){
                        mTreeSelectCode=mTreeSelectCode+ lstTree.get(lstTreeSelectPos.get(i)).getTreeCode();
                        mTreeSelectName=mTreeSelectName+ lstTree.get(lstTreeSelectPos.get(i)).getTreeName();
                        if(i!=lstTreeSelectPos.size()-1){
                            mTreeSelectCode=mTreeSelectCode+",";
                            mTreeSelectName=mTreeSelectName+",";
                        }
                        tvSPTree.setText(mTreeSelectName);
                        tvSPTree.setTag(mTreeSelectCode);
                    }
                }
            });
            builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   dialog.dismiss();
                }
            });

            builder.setNeutralButton("Bỏ chọn", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    for (int i = 0; i < lstTreeSelectChecked.length; i++) {
                        lstTreeSelectChecked[i] = false;
                        lstTreeSelectPos.clear();
                        tvSPTree.setText("");
                    }
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

            dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_2);
            Button btnPositiveButton=((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            Button btnNegetiveButton=((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

            btnPositiveButton.setTextColor(getResources().getColor(R.color.ButtonDialogColor));
            btnPositiveButton.setBackgroundColor(getResources().getColor(R.color.ButtonDialogBackground));
            btnPositiveButton.setPaddingRelative(20,2,20,2);
            btnNegetiveButton.setTextColor(getResources().getColor(R.color.ButtonDialogColor2));

        }
        catch(Exception ex) { }

    }


    private void initDropdownSP(){
        try{
            ArrayList<DM_Product> oListSP=new ArrayList<DM_Product>();
            for(int i=0;i<lstProduct.size();++i){
                oListSP.add(new DM_Product(lstProduct.get(i).getProductCode(),lstProduct.get(i).getProductName(),lstProduct.get(i).getUnit(),lstProduct.get(i).getSpecification(),lstProduct.get(i).getConvertBox(),false));
            }

            SearchProductAdapter adapter = new SearchProductAdapter(getContext(), R.layout.search_product,oListSP);
            spProductOrderDetail.setDropDownBackgroundResource(R.drawable.liner_dropdownlist);
            spProductOrderDetail.setThreshold(1);
            spProductOrderDetail.setAdapter(adapter);
            spProductOrderDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                        /*[FOR SPINNER UNIT]
                        String mUnit=oProductSel.getUnit();
                        int isPos =0;
                        lstUnit=new ArrayList<String>();
                        if(mUnit!=null && !mUnit.isEmpty()) {
                            lstUnit.add(mUnit);
                            for(String sU:lstUnitDefault){
                                if(!sU.equalsIgnoreCase(mUnit)){
                                    lstUnit.add(sU);
                                }
                            }
                            isPos = setPosSpin(lstUnit, mUnit);
                        }else{
                            for(String sU:lstUnitDefault){
                                lstUnit.add(sU);
                            }
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lstUnit);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spProductUnit.setAdapter(dataAdapter);
                        spProductUnit.setSelection(isPos);*/

                    }catch (Exception ex){}
                }
            });
            spProductOrderDetail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        spProductOrderDetail.selectAll();
                    }
                }
            });

        }catch (Exception ex){}
    }


    private int setPosSpin(List<String> lstSpin,String mValue){
        try{
            for(int i=0;i<lstSpin.size();++i){
                if(lstSpin.get(i).toString().equals(mValue)){
                    return i;
                }
            }
            return -1;
        }catch (Exception ex){return -1;}
    }

    private String getSpin(final Spinner oSpin){
        try{
            int iPos=oSpin.getSelectedItemPosition();
            if (iPos<=0){
                return "";
            }
            return oSpin.getItemAtPosition(iPos).toString();
        }catch (Exception ex){}
        return null;
    }

    private void setOrderDetailProductRow(SM_OrderDetail osmDT){
        try {
            if (osmDT != null) {
                oProductSel=((OrderFormActivity) getActivity()).getProduct(osmDT.getProductCode());
                if (oProductSel!=null && oProductSel.getProductCode()!=null && !oProductSel.getProductCode().isEmpty()) {
                    if (osmDT.getProductCode() != null) {
                        oProductSel.setProductCode(osmDT.getProductCode());
                    }
                    if (osmDT.getProductName() != null) {
                        oProductSel.setProductName(osmDT.getProductName());
                        spProductOrderDetail.setText(osmDT.getProductName());
                    }
                    if (osmDT.getUnit() != null) {
                        oProductSel.setUnit(osmDT.getUnit());
                    }
                    if (osmDT.getSpec() != null) {
                        oProductSel.setSpecification(osmDT.getSpec());
                    }

                    if(oProductSel.getConvertBox()!=null) {
                        mConvertBox = oProductSel.getConvertBox();
                    }else{
                        mConvertBox=Float.valueOf(0);
                    }

                    if (osmDT.getAmount() != null) {
                        tvAmountDetail.setText(osmDT.getAmount().toString());
                    }
                    if (osmDT.getAmountBox() != null) {
                        tvAmountBoxDetail.setText(osmDT.getAmountBox().toString());
                    }
                    if (osmDT.getPrice() != null) {
                        tvPriceDetail.setText(osmDT.getPrice().toString());
                    }
                    if (osmDT.getOriginMoney() != null) {
                        tvOriginMoneyDetail.setText(osmDT.getOriginMoney().toString());
                    }
                    if (osmDT.getNotes() != null) {
                        tvOrderDetailNotes.setText(osmDT.getNotes());
                    } else {
                        tvOrderDetailNotes.setText("");
                    }
                    //TREELIST
                    if (osmDT.getNotes2() != null) {
                        tvSPTree.setText(getListTreeName(osmDT.getNotes2()));
                        tvSPTree.setTag(osmDT.getNotes2());
                    } else {
                        tvSPTree.setText("");
                        tvSPTree.setTag("");
                    }

                /*[FOR SPINNER UNIT]
                int isPos =0;
                lstUnit=new ArrayList<String>();
                if(osmDT.getUnit()!=null && !osmDT.getUnit().isEmpty()) {
                   lstUnit.add(osmDT.getUnit());
                   for(String sU:lstUnitDefault){
                       if(!sU.equalsIgnoreCase(osmDT.getUnit())){
                           lstUnit.add(sU);
                       }
                   }
                   isPos = setPosSpin(lstUnit, osmDT.getUnit());
                }else{
                    for(String sU:lstUnitDefault){
                      lstUnit.add(sU);
                    }
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lstUnit);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spProductUnit.setAdapter(dataAdapter);
                spProductUnit.setSelection(isPos);
                */
                }else{
                    Toast.makeText(getContext(),"Sản phẩm hiện không có trong danh mục..", Toast.LENGTH_SHORT).show();
                }

            }
        }catch (Exception ex){Toast.makeText(getContext(),"Không thể khởi tạo thông tin để sửa..",Toast.LENGTH_SHORT).show();}
    }


    public boolean onAddOrderDetail(boolean isAddnew){
        ((OrderFormActivity) getActivity()).setVisibleDetailDelete(false);
        if(Layout_OrderItem.getVisibility()==View.GONE){
            Layout_OrderItem.setVisibility(View.VISIBLE);
            //Clear Input
            try {
                if(isAddnew) {
                    /*
                    if(lstUnit==null) {
                        lstUnit = new ArrayList<String>();
                    }else{
                        lstUnit.clear();
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lstUnit);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spProductUnit.setAdapter(dataAdapter);
                   */

                    spProductOrderDetail.setText("");
                    spProductOrderDetail.requestFocus();
                    tvAmountDetail.setText("");
                    tvAmountBoxDetail.setText("");
                    tvPriceDetail.setText("");
                    tvOriginMoneyDetail.setText("");
                    tvOrderDetailNotes.setText("");
                    tvSPTree.setText("");
                    tvSPTree.setTag("");
                    lstTreeSelectPos.clear();
                }
            }catch (Exception ex){}
            return true;
        }
        return  true;
    }

    public boolean onSaveOrderDetail(){
        try{
          if(onSaveAddOrderDetail()){
              if(Layout_OrderItem.getVisibility()==View.VISIBLE) {
                  Layout_OrderItem.setVisibility(View.GONE);
                  adapter.clearSelected();
              }
              return  true;
          }
        }catch (Exception ex){return  false;}
        return  false;
    }

    private boolean onSaveAddOrderDetail(){
       if(oProductSel!=null && oProductSel.getProductCode()!=null && !oProductSel.getProductCode().isEmpty()){
           SM_OrderDetail oDetail=new SM_OrderDetail();
           oDetail.setProductID("SP"+oProductSel.getProductCode());
           oDetail.setProductCode(oProductSel.getProductCode());
           oDetail.setProductName(oProductSel.getProductName());

           /*
           String mUnit="";
           try{
               mUnit=getSpin(spProductUnit);
           }catch (Exception ex){}
           if(mUnit== null || mUnit.isEmpty()) {
               oDetail.setUnit(oProductSel.getUnit());
           }else{
               oDetail.setUnit(mUnit);
           }*/
           oDetail.setUnit(oProductSel.getUnit());
           oDetail.setSpec(oProductSel.getSpecification());
           if(oProductSel.getConvertBox()!=null) {
               oDetail.setConvertBox(oProductSel.getConvertBox());
           }
           if (tvAmountDetail.getText()==null || tvAmountDetail.getText().toString().isEmpty() || tvAmountDetail.getText().toString().equalsIgnoreCase("0")){
               Toast oT= Toast.makeText(getContext(),"Bạn chưa nhập số lượng...",Toast.LENGTH_LONG);
               oT.setGravity(Gravity.CENTER,0,0);
               oT.show();

               tvAmountDetail.requestFocus();
               return false;
           }else{
               oDetail.setAmount(Integer.valueOf(tvAmountDetail.getText().toString()));
           }
           if(tvAmountBoxDetail.getText()==null || tvAmountBoxDetail.getText().toString().isEmpty()) {
               oDetail.setAmountBox(Float.valueOf(0));
           }else{
               oDetail.setAmountBox(Float.valueOf(tvAmountBoxDetail.getText().toString()));
           }

           if (!tvPriceDetail.getText().toString().isEmpty()){
               oDetail.setPrice(Double.valueOf(tvPriceDetail.getText().toString()));
           }
           if (!tvOriginMoneyDetail.getText().toString().isEmpty()){
               oDetail.setOriginMoney(Double.valueOf(tvOriginMoneyDetail.getText().toString()));
           }
           if (!tvOrderDetailNotes.getText().toString().isEmpty()){
               oDetail.setNotes(tvOrderDetailNotes.getText().toString());
           }
           if (!tvSPTree.getTag().toString().isEmpty()){
               oDetail.setNotes2(tvSPTree.getTag().toString());
           }

           boolean isExist=false;
           for(int i=0;i<lstOderDt.size();i++){
               if(lstOderDt.get(i).getProductCode().equalsIgnoreCase(oDetail.getProductCode())){
                   //lstOderDt.remove(i);
                   isExist=true;
                   if(oDetail.getProductCode()!=null) {
                       lstOderDt.get(i).setProductCode(oDetail.getProductCode());
                   }else{
                       lstOderDt.get(i).setProductCode("");
                   }
                   if(oDetail.getProductName()!=null) {
                       lstOderDt.get(i).setProductName(oDetail.getProductName());
                   }else{
                       lstOderDt.get(i).setProductName("");
                   }
                   if(oDetail.getUnit()!=null) {
                       lstOderDt.get(i).setUnit(oDetail.getUnit());
                   }else{
                       lstOderDt.get(i).setUnit("");
                   }
                   if(oDetail.getSpec()!=null) {
                       lstOderDt.get(i).setSpec(oDetail.getSpec());
                   }else{
                       lstOderDt.get(i).setSpec("");
                   }
                   if(oDetail.getConvertBox()!=null) {
                       lstOderDt.get(i).setConvertBox(oDetail.getConvertBox());
                   }else{
                       lstOderDt.get(i).setConvertBox(Float.valueOf(0));
                   }
                   if(oDetail.getAmount()!=null){
                       lstOderDt.get(i).setAmount(oDetail.getAmount());
                   }else{
                       lstOderDt.get(i).setAmount(0);
                   }
                   if(oDetail.getAmountBox()!=null){
                       lstOderDt.get(i).setAmountBox(oDetail.getAmountBox());
                   }else{
                       lstOderDt.get(i).setAmountBox(Float.valueOf(0));
                   }
                   if(oDetail.getPrice()!=null) {
                       lstOderDt.get(i).setPrice(oDetail.getPrice());
                   }else{
                       lstOderDt.get(i).setPrice(0.0);
                   }
                   if(oDetail.getOriginMoney()!=null) {
                       lstOderDt.get(i).setOriginMoney(oDetail.getOriginMoney());
                   }else{
                       lstOderDt.get(i).setOriginMoney(0.0);
                   }
                   if(oDetail.getNotes()!=null) {
                       lstOderDt.get(i).setNotes(oDetail.getNotes());
                   }else{
                       lstOderDt.get(i).setNotes("");
                   }
                   //TREELIST
                   if(oDetail.getNotes2()!=null) {
                       lstOderDt.get(i).setNotes2(oDetail.getNotes2());
                   }else{
                       lstOderDt.get(i).setNotes2("");
                   }


                   Toast.makeText(getContext(),"Sản phẩm đã được thêm..",Toast.LENGTH_SHORT).show();
                   break;
               }
           }

           if(!isExist) {
               SimpleDateFormat Od = new SimpleDateFormat("ddMMyyyyHHmmssSS");
               String mOrderDetailID = "CT"+mParSymbol+Od.format(new Date());
               oDetail.setOrderDetailID(mOrderDetailID);
               oDetail.setOrderID(mOrderID);
               lstOderDt.add(oDetail);
           }

           adapter.setOrderDetailList(lstOderDt);
           Toast.makeText(getContext(),String.valueOf(lstOderDt.size())+": Sản phẩm được chọn..",Toast.LENGTH_SHORT).show();

           //Clear Input
           /*lstUnit.clear();
           ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lstUnit);
           dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
           spProductUnit.setAdapter(dataAdapter);
           */
           spProductOrderDetail.setText("");
           spProductOrderDetail.requestFocus();
           tvAmountDetail.setText("");
           tvAmountBoxDetail.setText("");
           tvPriceDetail.setText("");
           tvOriginMoneyDetail.setText("");
           tvOrderDetailNotes.setText("");
           tvSPTree.setText("");
           tvSPTree.setTag("");
           oProductSel=new DM_Product();
           return  true;
       }else{
           Toast.makeText(getContext(),"Không có sản phẩm nào được chọn..",Toast.LENGTH_SHORT).show();
           return false;
       }

    }

    public void onDeletedOrderDetail() {
        if(adapter.SelectedList==null || adapter.SelectedList.size()<=0) {
            Toast oToat=Toast.makeText(getContext(),"Bạn chưa chọn sản phẩm để xóa...",Toast.LENGTH_LONG);
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
                for(SM_OrderDetail oDTSel:adapter.SelectedList){
                    for(SM_OrderDetail oDT:lstOderDt){
                        if(oDTSel.equals(oDT)){
                            lstOderDt.remove(oDT);
                            break;
                        }
                    }
                }
                adapter.SelectedList.clear();
                adapter.setOrderDetailList(lstOderDt);
                ((OrderFormActivity) getActivity()).setVisibleDetailDelete(false);
                ((OrderFormActivity) getActivity()).setButtonEditStatus(false);
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


    @OnTextChanged(R.id.tvPriceDetail)
    public void onPriceDetailChanged(){
        try{
            Double iPrice=0.0;
            Integer iAmount=0;
            if(tvPriceDetail.getText()!=null && !tvPriceDetail.getText().toString().isEmpty()){
                iPrice=Double.valueOf(tvPriceDetail.getText().toString());
            } else{
                iPrice=0.0;
            }
            if(tvAmountDetail.getText()!=null && !tvAmountDetail.getText().toString().isEmpty()){
                iAmount=Integer.valueOf(tvAmountDetail.getText().toString());
            } else{
                iAmount=0;
            }

            Double oMoney=iPrice*iAmount;
            tvOriginMoneyDetail.setText(oMoney.toString());

        }catch (Exception ex){}
    }


    @OnTextChanged(R.id.tvAmountBoxDetail)
    public void onAmountBoxDetailChanged(){
      try{
          if(!spProductOrderDetail.getText().toString().isEmpty()){
              if(mConvertBox>0){
                  Double iAmount=0.0;
                  iAmount=Double.valueOf(tvAmountBoxDetail.getText().toString())*mConvertBox;
                  tvAmountDetail.setText(String.valueOf(iAmount.intValue()));
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

    @OnTextChanged(R.id.tvAmountDetail)
    public void onAmountDetailChanged(){
        try{
            Double iPrice=0.0;
            Integer iAmount=0;
            if(tvPriceDetail.getText()!=null && !tvPriceDetail.getText().toString().isEmpty()){
                iPrice=Double.valueOf(tvPriceDetail.getText().toString());
            } else{
                iPrice=0.0;
            }
            if(tvAmountDetail.getText()!=null && !tvAmountDetail.getText().toString().isEmpty()){
                iAmount=Integer.valueOf(tvAmountDetail.getText().toString());
            } else{
                iAmount=0;
            }

            Double oMoney=iPrice*iAmount;
            tvOriginMoneyDetail.setText(oMoney.toString());
            /*
            try{
                if(mConvertBox>0){
                   tvAmountBoxDetail.setText(String.valueOf(Float.valueOf(tvAmountDetail.getText().toString())/mConvertBox));
                }
            }catch (Exception ex){}
           */

        }catch (Exception ex){}
    }
}
