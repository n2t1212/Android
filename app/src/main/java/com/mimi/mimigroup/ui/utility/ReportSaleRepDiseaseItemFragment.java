package com.mimi.mimigroup.ui.utility;

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
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.DM_Tree;
import com.mimi.mimigroup.model.DM_Tree_Disease;
import com.mimi.mimigroup.model.SM_ReportSaleRepDisease;
import com.mimi.mimigroup.ui.adapter.ReportSaleRepDiseaseAdapter;
import com.mimi.mimigroup.ui.adapter.SearchTreeAdapter;
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

public class ReportSaleRepDiseaseItemFragment extends BaseFragment {

    @BindView(R.id.rvReportSaleRepDiseaseList)
    RecyclerView rvReportSaleRepDiseaseList;
    @BindView(R.id.spTree)
    AutoCompleteTextView spTree;
    @BindView(R.id.tvTitle)
    CustomBoldEditText tvTitle;
    @BindView(R.id.tvAcreage)
    CustomBoldEditText tvAcreage;
    @BindView(R.id.tvNotes)
    CustomBoldEditText tvNotes;
    @BindView(R.id.Layout_ReportSaleRepDiseaseItem)
    LinearLayout Layout_ReportSaleRepDiseaseItem;

    ReportSaleRepDiseaseAdapter adapter;
    private String mReportSaleRepId = "";
    private String mParSymbol = "";
    private String mAction = "";

    List<SM_ReportSaleRepDisease> lstReportSaleRepDisease;
    String currentDiseaseId;
    List<DM_Tree> lstTree;
    List<DM_Tree_Disease> lstTreeDisease;
    DM_Tree oTreeSel;

    DBGimsHelper mDB = null;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_report_sale_rep_disease_form;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDB = DBGimsHelper.getInstance(getActivity().getBaseContext());
        adapter = new ReportSaleRepDiseaseAdapter(new ReportSaleRepDiseaseAdapter.ListItemClickListener() {
            @Override
            public void onItemClick(List<SM_ReportSaleRepDisease> SelectList) {
                if (SelectList.size() > 0) {
                    for (SM_ReportSaleRepDisease osmDT : SelectList) {
                        setReportSaleRepDiseaseRow(osmDT);
                        break;
                    }
                    //Chỉ cho phép sửa khi chọn 1 dòng
                    if (SelectList.size() > 1) {
                        ((ReportSaleRepFormActivity) getActivity()).setButtonEditStatus(false);
                        Layout_ReportSaleRepDiseaseItem.setVisibility(View.GONE);
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
        rvReportSaleRepDiseaseList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReportSaleRepDiseaseList.setAdapter(adapter);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //GET PARAMETER FORM ACTIVITY
                        ReportSaleRepFormActivity oActivity = (ReportSaleRepFormActivity) getActivity();
                        lstReportSaleRepDisease = oActivity.getoReportSaleRepDisease();
                        mReportSaleRepId=oActivity.getmReportSaleRepID();
                        mAction=oActivity.getAction();
                        if (lstReportSaleRepDisease != null) {
                            adapter.setsmoReportSaleRepDisease(lstReportSaleRepDisease);
                        }
                        mParSymbol=oActivity.getmPar_Symbol();
                        lstTree=oActivity.getListTree();
                        initDropdownTree();
                    }} ,300);

        Layout_ReportSaleRepDiseaseItem.setVisibility(View.GONE);
    }

    // CALL FROM ACTIVITY
    public List<SM_ReportSaleRepDisease> getListReportSaleRepDisease(){
        return lstReportSaleRepDisease;
    }

    private String[] lstTreeSelect;
    private boolean[] lstTreeSelectChecked;
    private ArrayList<Integer> lstTreeSelectPos = new ArrayList<>();

    private void initDropdownTree(){
        try{
            ArrayList<DM_Tree> oListTree=new ArrayList<DM_Tree>();
            for(int i=0;i<lstTree.size();++i){
                oListTree.add(lstTree.get(i));
            }
            SearchTreeAdapter adapter = new SearchTreeAdapter(getContext(), R.layout.search_tree,oListTree);
            spTree.setDropDownBackgroundResource(R.drawable.liner_dropdownlist);
            spTree.setThreshold(1);
            spTree.setAdapter(adapter);
            spTree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int iPosition, long l) {
                    try {
                        oTreeSel = (DM_Tree) adapterView.getItemAtPosition(iPosition);

                    }catch (Exception ex){}
                }
            });

        }catch (Exception ex){}
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

    private void setReportSaleRepDiseaseRow(SM_ReportSaleRepDisease osmDT) {
        try {
            if (osmDT != null) {
                if (osmDT.getTreeCode() != null) {
                    if(lstTree != null && lstTree.size() >0){
                        for(DM_Tree tree: lstTree){
                            if(tree.getTreeCode().equals(osmDT.getTreeCode())){
                                oTreeSel = tree;
                            }
                        }
                    }
                    if(oTreeSel != null){
                        spTree.setText(oTreeSel.getTreeName());
                        spTree.setTag(oTreeSel.getTreeCode());
                    }
                }else {
                    spTree.setText("");
                    spTree.setTag("");
                }
                if(osmDT.getTitle() != null){
                    tvTitle.setText(osmDT.getTitle());
                }
                if(osmDT.getArceage() != null){
                    tvAcreage.setText(osmDT.getArceage().toString());
                }
                if (osmDT.getNotes() != null) {
                    tvNotes.setText(osmDT.getNotes());
                }

                currentDiseaseId = osmDT.getDiseaseId();
            } else {
                Toast.makeText(getContext(), "Không tồn tại báo cáo dịch bệnh..", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            Toast.makeText(getContext(), "Không thể khởi tạo thông tin để sửa..", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean onAddReportSaleRepDisease(boolean isAddnew) {
        ((ReportSaleRepFormActivity) getActivity()).setVisibleDetailDelete(false);
        if (Layout_ReportSaleRepDiseaseItem.getVisibility() == View.GONE) {
            Layout_ReportSaleRepDiseaseItem.setVisibility(View.VISIBLE);
            //Clear Input
            try {
                if (isAddnew) {
                    spTree.setText("");
                    spTree.setTag("");
                    tvTitle.setText("");
                    tvAcreage.setText("");
                    tvNotes.setText("");

                }
            } catch (Exception ex) {
            }
            return true;
        }
        return true;
    }

    public boolean onSaveReportSaleRepDisease(){
        try{
            if(onSaveAddReportSaleRepDisease()){
                if(Layout_ReportSaleRepDiseaseItem.getVisibility()==View.VISIBLE) {
                    Layout_ReportSaleRepDiseaseItem.setVisibility(View.GONE);
                    adapter.clearSelected();
                }
                return  true;
            }
        }catch (Exception ex){return  false;}
        return  false;
    }

    private boolean onSaveAddReportSaleRepDisease() {
        SM_ReportSaleRepDisease oDetail = new SM_ReportSaleRepDisease();

        // EDIT
        if(currentDiseaseId != null && currentDiseaseId.length() > 0){
            oDetail.setDiseaseId(currentDiseaseId);
            currentDiseaseId = "";
        }

        if (oTreeSel == null || oTreeSel.getTreeCode() == null) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa chọn cây trồng...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            spTree.requestFocus();
            return false;
        } else {
            oDetail.setTreeCode(oTreeSel.getTreeCode());
        }

        if (tvNotes.getText() == null || tvNotes.getText().toString().isEmpty()) {
            oDetail.setNotes("");
        } else {
            oDetail.setNotes(tvNotes.getText().toString());
        }
        if (tvTitle.getText() == null || tvTitle.getText().toString().isEmpty()) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập tiêu dề...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            tvTitle.requestFocus();
            return false;
        } else {
            oDetail.setTitle(tvTitle.getText().toString());
        }
        if (tvAcreage.getText() == null || tvAcreage.getText().toString().isEmpty()) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập diện tích...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            tvAcreage.requestFocus();
            return false;
        } else {
            oDetail.setArceage(Float.parseFloat(tvAcreage.getText().toString()));
        }

        boolean isExist = false;
        for (int i = 0; i < lstReportSaleRepDisease.size(); i++) {
            if (lstReportSaleRepDisease.get(i).getDiseaseId().equalsIgnoreCase(oDetail.getDiseaseId())) {
                isExist = true;
                if (oDetail.getTitle() != null) {
                    lstReportSaleRepDisease.get(i).setTitle(oDetail.getTitle());
                } else {
                    lstReportSaleRepDisease.get(i).setTitle("");
                }
                if (oDetail.getNotes() != null) {
                    lstReportSaleRepDisease.get(i).setNotes(oDetail.getNotes());
                } else {
                    lstReportSaleRepDisease.get(i).setNotes("");
                }
                if (oDetail.getTreeCode() != null) {
                    lstReportSaleRepDisease.get(i).setTreeCode(oDetail.getTreeCode());
                } else {
                    lstReportSaleRepDisease.get(i).setTreeCode("");
                }

                if (oDetail.getArceage() != null) {
                    lstReportSaleRepDisease.get(i).setArceage(oDetail.getArceage());
                } else {
                    lstReportSaleRepDisease.get(i).setArceage(0f);
                }


                Toast.makeText(getContext(), "Báo cáo dịch hại đã được thêm..", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        if (!isExist) {
            SimpleDateFormat Od = new SimpleDateFormat("ddMMyyyyHHmmssSS");
            String mDiseaseId = "BCDH" + mParSymbol + Od.format(new Date());
            oDetail.setDiseaseId(mDiseaseId);
            oDetail.setReportSaleId(mReportSaleRepId);
            lstReportSaleRepDisease.add(oDetail);
        }

        adapter.setsmoReportSaleRepDisease(lstReportSaleRepDisease);
        Toast.makeText(getContext(), String.valueOf(lstReportSaleRepDisease.size()) + ": Báo cáo dịch hại được chọn..", Toast.LENGTH_SHORT).show();

        tvTitle.setText("");
        tvNotes.setText("");
        spTree.setText("");
        spTree.setTag("");
        tvAcreage.setText("");
        return true;
    }

    public void onDeletedReportSaleRepDisease(){
        if(adapter.SelectedList==null || adapter.SelectedList.size()<=0) {
            Toast oToat=Toast.makeText(getContext(),"Bạn chưa chọn báo cáo dịch hại để xóa...",Toast.LENGTH_LONG);
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
                for(SM_ReportSaleRepDisease oDTSel:adapter.SelectedList){
                    for(SM_ReportSaleRepDisease oDT:lstReportSaleRepDisease){
                        if(oDTSel.equals(oDT)){
                            lstReportSaleRepDisease.remove(oDT);
                            break;
                        }
                    }
                }
                adapter.SelectedList.clear();
                adapter.setsmoReportSaleRepDisease(lstReportSaleRepDisease);

                // Set view
                rvReportSaleRepDiseaseList.setAdapter(adapter);
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
