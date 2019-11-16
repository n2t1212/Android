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
import com.mimi.mimigroup.model.SM_ReportTechDisease;
import com.mimi.mimigroup.ui.adapter.ReportTechDiseaseAdapter;
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

public class ReportTechDiseaseItemFragment extends BaseFragment {

    @BindView(R.id.rvReportTechDiseaseList)
    RecyclerView rvReportTechDiseaseList;
    @BindView(R.id.tvTree)
    CustomTextView tvTree;
    @BindView(R.id.tvTitle)
    CustomBoldEditText tvTitle;
    @BindView(R.id.tvAcreage)
    CustomBoldEditText tvAcreage;
    @BindView(R.id.tvDisease)
    CustomBoldEditText tvDisease;
    @BindView(R.id.tvPrice)
    CustomBoldEditText tvPrice;
    @BindView(R.id.tvNotes)
    CustomBoldEditText tvNotes;
    @BindView(R.id.Layout_ReportTechDiseaseItem)
    LinearLayout Layout_ReportTechDiseaseItem;

    ReportTechDiseaseAdapter adapter;
    private String mReportTechId = "";
    private String mParSymbol = "";
    private String mAction = "";

    List<SM_ReportTechDisease> lstReportTechDisease;
    String currentDiseaseId;
    List<DM_Tree> lstTree;
    List<DM_Tree_Disease> lstTreeDisease;

    DBGimsHelper mDB = null;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_report_tech_disease_form;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDB = DBGimsHelper.getInstance(getActivity().getBaseContext());
        adapter = new ReportTechDiseaseAdapter(new ReportTechDiseaseAdapter.ListItemClickListener() {
            @Override
            public void onItemClick(List<SM_ReportTechDisease> SelectList) {
                if (SelectList.size() > 0) {
                    for (SM_ReportTechDisease osmDT : SelectList) {
                        setReportTechDiseaseRow(osmDT);
                        break;
                    }
                    //Chỉ cho phép sửa khi chọn 1 dòng
                    if (SelectList.size() > 1) {
                        ((ReportTechFormActivity) getActivity()).setButtonEditStatus(false);
                        Layout_ReportTechDiseaseItem.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Bạn chọn quá nhiều để có thể sửa..", Toast.LENGTH_SHORT).show();
                    } else {
                        ((ReportTechFormActivity) getActivity()).setButtonEditStatus(true);
                    }
                    ((ReportTechFormActivity) getActivity()).setVisibleDetailDelete(true);
                } else {
                    ((ReportTechFormActivity) getActivity()).setVisibleDetailDelete(false);
                    ((ReportTechFormActivity) getActivity()).setButtonEditStatus(false);
                }
            }
        });
        rvReportTechDiseaseList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReportTechDiseaseList.setAdapter(adapter);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //GET PARAMETER FORM ACTIVITY
                        ReportTechFormActivity oActivity = (ReportTechFormActivity) getActivity();
                        lstReportTechDisease = oActivity.getoReportTechDisease();
                        mReportTechId=oActivity.getmReportTechID();
                        mAction=oActivity.getAction();
                        if (lstReportTechDisease != null) {
                            adapter.setsmoReportTechDisease(lstReportTechDisease);
                        }
                        mParSymbol=oActivity.getmPar_Symbol();
                        lstTree=oActivity.getListTree();
                        initDropdownCT();
                    }} ,300);

        Layout_ReportTechDiseaseItem.setVisibility(View.GONE);
    }

    // CALL FROM ACTIVITY
    public List<SM_ReportTechDisease> getListReportTechDisease(){
        return lstReportTechDisease;
    }

    private String[] lstTreeSelect;
    private boolean[] lstTreeSelectChecked;
    private ArrayList<Integer> lstTreeSelectPos = new ArrayList<>();
    private String[] lstTreeDiseaseSelect;
    private boolean[] lstTreeDiseaseSelectChecked;
    private ArrayList<Integer> lstTreeDiseaseSelectPos = new ArrayList<>();

    private void initDropdownCT(){
        try{
            if(lstTree==null){lstTree=new ArrayList<DM_Tree>();}

            lstTreeSelect=new String[lstTree.size()];
            lstTreeSelectChecked=new boolean[lstTree.size()];

            for(int i=0;i<lstTree.size();i++){
                lstTreeSelect[i]=lstTree.get(i).getTreeName();
                lstTreeSelectChecked[i]=false;
            }
        }catch (Exception ex){}
    }

    private void initDropDownTreeDisease(){
        try {
            if(lstTreeDisease == null) {
                lstReportTechDisease = new ArrayList<>();
            }

            lstTreeDiseaseSelect=new String[lstTreeDisease.size()];
            lstTreeDiseaseSelectChecked=new boolean[lstTreeDisease.size()];

            for(int i=0;i<lstTreeDisease.size();i++){
                lstTreeDiseaseSelect[i]=lstTreeDisease.get(i).getDiseaseName();
                lstTreeDiseaseSelectChecked[i]=false;
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

    private String getListTreeDiseaseName(String mlstTreeDiseaseSelect){
        try{
            String mTreeDiseaseListName="";
            lstTreeDiseaseSelectPos.clear();
            if(!mlstTreeDiseaseSelect.isEmpty()){
                String[] mlstDiseaseCode=mlstTreeDiseaseSelect.split(",");
                for(int iP=0;iP<mlstDiseaseCode.length;iP++){
                    for(int jP=0;jP<lstTreeDisease.size();jP++){
                        if(lstTreeDisease.get(jP).getDiseaseCode().contains(mlstDiseaseCode[iP])){
                            lstTreeDiseaseSelectPos.add(jP);
                            mTreeDiseaseListName=mTreeDiseaseListName+lstTreeDisease.get(jP).getDiseaseName();
                            if(iP!=mlstDiseaseCode.length-1) {
                                mTreeDiseaseListName =mTreeDiseaseListName + ",";
                            }
                        }
                    }
                }
            }
            return  mTreeDiseaseListName;
        }catch (Exception ex){return  "";}
    }

    @OnClick(R.id.tvTree)
    public void onClickTree(){
        try{
            Arrays.fill(lstTreeSelectChecked,false);
            for(int i=0;i<lstTreeSelectPos.size();i++){
                lstTreeSelectChecked[lstTreeSelectPos.get(i)]=true;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false);
            builder.setTitle("Chọn dịch hại");
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
                    tvTree.setText("");
                    String mTreeSelectCode="",mTreeSelectName="";
                    for (int i = 0; i<lstTreeSelectPos.size(); i++){
                        mTreeSelectCode=mTreeSelectCode+ lstTree.get(lstTreeSelectPos.get(i)).getTreeCode();
                        mTreeSelectName=mTreeSelectName+ lstTree.get(lstTreeSelectPos.get(i)).getTreeName();
                        if(i!=lstTreeSelectPos.size()-1){
                            mTreeSelectCode=mTreeSelectCode+",";
                            mTreeSelectName=mTreeSelectName+",";
                        }
                        tvTree.setText(mTreeSelectName);
                        tvTree.setTag(mTreeSelectCode);
                        lstTreeDisease = mDB.getListTreeDiseaseByTreeCode(mTreeSelectCode);
                        initDropDownTreeDisease();
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
                        tvTree.setText("");
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

    @OnClick(R.id.tvDisease)
    public void onClickTreeDisease(){
        try{
            Arrays.fill(lstTreeDiseaseSelectChecked,false);
            for(int i=0;i<lstTreeDiseaseSelectPos.size();i++){
                lstTreeDiseaseSelectChecked[lstTreeDiseaseSelectPos.get(i)]=true;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false);
            builder.setTitle("Chọn cây trồng");
            builder.setMultiChoiceItems(lstTreeDiseaseSelect,lstTreeDiseaseSelectChecked, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                    if(isChecked){
                        lstTreeDiseaseSelectPos.add(position);
                    }else{
                        lstTreeDiseaseSelectPos.remove(Integer.valueOf(position));
                    }
                }
            });

            builder.setPositiveButton("Chấp nhận", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    tvDisease.setText("");
                    String mTreeSelectCode="",mTreeSelectName="";
                    for (int i = 0; i<lstTreeDiseaseSelectPos.size(); i++){
                        mTreeSelectCode=mTreeSelectCode+ lstTreeDisease.get(lstTreeDiseaseSelectPos.get(i)).getDiseaseCode();
                        mTreeSelectName=mTreeSelectName+ lstTreeDisease.get(lstTreeDiseaseSelectPos.get(i)).getDiseaseName();
                        if(i!=lstTreeDiseaseSelectPos.size()-1){
                            mTreeSelectCode=mTreeSelectCode+",";
                            mTreeSelectName=mTreeSelectName+",";
                        }
                        tvDisease.setText(mTreeSelectName);
                        tvDisease.setTag(mTreeSelectCode);
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
                    for (int i = 0; i < lstTreeDiseaseSelectChecked.length; i++) {
                        lstTreeDiseaseSelectChecked[i] = false;
                        lstTreeDiseaseSelectPos.clear();
                        tvDisease.setText("");
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

    private void setReportTechDiseaseRow(SM_ReportTechDisease osmDT) {
        try {
            if (osmDT != null) {
                if (osmDT.getTreeCode() != null) {
                    tvTree.setText(getListTreeName(osmDT.getTreeCode()));
                    tvTree.setTag(osmDT.getTreeCode());
                    lstTreeDisease = mDB.getListTreeDiseaseByTreeCode(osmDT.getTreeCode());
                }else {
                    tvTree.setText("");
                    tvTree.setTag("");
                }
                if(osmDT.getTitle() != null){
                    tvTitle.setText(osmDT.getTitle());
                }
                if(osmDT.getAcreage() != null){
                    tvAcreage.setText(osmDT.getAcreage().toString());
                }
                if(osmDT.getDisease() != null){
                    tvDisease.setText(getListTreeDiseaseName(osmDT.getDisease()));
                    tvDisease.setTag(osmDT.getDisease());
                }
                if(osmDT.getPrice() != null){
                    tvPrice.setText(osmDT.getPrice().toString());
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

    public boolean onAddReportTechDisease(boolean isAddnew) {
        ((ReportTechFormActivity) getActivity()).setVisibleDetailDelete(false);
        if (Layout_ReportTechDiseaseItem.getVisibility() == View.GONE) {
            Layout_ReportTechDiseaseItem.setVisibility(View.VISIBLE);
            //Clear Input
            try {
                if (isAddnew) {
                    tvTree.setText("");
                    tvTree.setTag("");
                    tvTitle.setText("");
                    tvAcreage.setText("");
                    tvDisease.setText("");
                    tvDisease.setTag("");
                    tvPrice.setText("");
                    tvNotes.setText("");

                }
            } catch (Exception ex) {
            }
            return true;
        }
        return true;
    }

    public boolean onSaveReportTechDisease(){
        try{
            if(onSaveAddReportTechDisease()){
                if(Layout_ReportTechDiseaseItem.getVisibility()==View.VISIBLE) {
                    Layout_ReportTechDiseaseItem.setVisibility(View.GONE);
                    adapter.clearSelected();
                }
                return  true;
            }
        }catch (Exception ex){return  false;}
        return  false;
    }

    private boolean onSaveAddReportTechDisease() {
        SM_ReportTechDisease oDetail = new SM_ReportTechDisease();

        // EDIT
        if(currentDiseaseId != null && currentDiseaseId.length() > 0){
            oDetail.setDiseaseId(currentDiseaseId);
            currentDiseaseId = "";
        }

        if (tvTree.getTag().toString().isEmpty()) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa chọn cây trồng...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            return false;
        } else {
            oDetail.setTreeCode(tvTree.getTag().toString());
        }

        if (tvNotes.getText() == null || tvNotes.getText().toString().isEmpty()) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập ghi chú...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            return false;
        } else {
            oDetail.setNotes(tvNotes.getText().toString());
        }
        if (tvTitle.getText() == null || tvTitle.getText().toString().isEmpty()) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập tiêu dề...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            return false;
        } else {
            oDetail.setTitle(tvTitle.getText().toString());
        }
        if (tvAcreage.getText() == null || tvAcreage.getText().toString().isEmpty()) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập diện tích...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            return false;
        } else {
            oDetail.setAcreage(Float.parseFloat(tvAcreage.getText().toString()));
        }
        if (tvDisease.getTag().toString().isEmpty()) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa chọn dịch hại...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            return false;
        } else {
            oDetail.setDisease(tvDisease.getTag().toString());
        }
        if (tvPrice.getText() == null || tvPrice.getText().toString().isEmpty()) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập giá cây trồng...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            return false;
        } else {
            oDetail.setPrice(Float.parseFloat(tvPrice.getText().toString()));
        }

        boolean isExist = false;
        for (int i = 0; i < lstReportTechDisease.size(); i++) {
            if (lstReportTechDisease.get(i).getDiseaseId().equalsIgnoreCase(oDetail.getDiseaseId())) {
                isExist = true;
                if (oDetail.getTitle() != null) {
                    lstReportTechDisease.get(i).setTitle(oDetail.getTitle());
                } else {
                    lstReportTechDisease.get(i).setTitle("");
                }
                if (oDetail.getNotes() != null) {
                    lstReportTechDisease.get(i).setNotes(oDetail.getNotes());
                } else {
                    lstReportTechDisease.get(i).setNotes("");
                }
                if (oDetail.getTreeCode() != null) {
                    lstReportTechDisease.get(i).setTreeCode(oDetail.getTreeCode());
                } else {
                    lstReportTechDisease.get(i).setTreeCode("");
                }
                if (oDetail.getDisease() != null) {
                    lstReportTechDisease.get(i).setDisease(oDetail.getDisease());
                } else {
                    lstReportTechDisease.get(i).setDisease("");
                }
                if (oDetail.getAcreage() != null) {
                    lstReportTechDisease.get(i).setAcreage(oDetail.getAcreage());
                } else {
                    lstReportTechDisease.get(i).setPrice(0f);
                }
                if (oDetail.getPrice() != null) {
                    lstReportTechDisease.get(i).setPrice(oDetail.getPrice());
                } else {
                    lstReportTechDisease.get(i).setPrice(0f);
                }

                Toast.makeText(getContext(), "Báo cáo dịch hại đã được thêm..", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        if (!isExist) {
            SimpleDateFormat Od = new SimpleDateFormat("ddMMyyyyHHmmssSS");
            String mDiseaseId = "BCDH" + mParSymbol + Od.format(new Date());
            oDetail.setDiseaseId(mDiseaseId);
            oDetail.setReportTechId(mReportTechId);
            lstReportTechDisease.add(oDetail);
        }

        adapter.setsmoReportTechDisease(lstReportTechDisease);
        Toast.makeText(getContext(), String.valueOf(lstReportTechDisease.size()) + ": Báo cáo dịch hại được chọn..", Toast.LENGTH_SHORT).show();

        tvTitle.setText("");
        tvNotes.setText("");
        tvTree.setText("");
        tvTree.setTag("");
        tvAcreage.setText("");
        tvDisease.setText("");
        tvDisease.setTag("");
        tvPrice.setText("");
        return true;
    }

    public void onDeletedReportTechDisease(){
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
                for(SM_ReportTechDisease oDTSel:adapter.SelectedList){
                    for(SM_ReportTechDisease oDT:lstReportTechDisease){
                        if(oDTSel.equals(oDT)){
                            lstReportTechDisease.remove(oDT);
                            break;
                        }
                    }
                }
                adapter.SelectedList.clear();
                adapter.setsmoReportTechDisease(lstReportTechDisease);

                // Set view
                rvReportTechDiseaseList.setAdapter(adapter);
                ((ReportTechFormActivity) getActivity()).setVisibleDetailDelete(false);
                ((ReportTechFormActivity) getActivity()).setButtonEditStatus(false);
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
