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
import com.mimi.mimigroup.model.DM_Season;
import com.mimi.mimigroup.model.DM_Tree;
import com.mimi.mimigroup.model.SM_ReportSaleRepSeason;
import com.mimi.mimigroup.ui.adapter.ReportSaleRepSeasonAdapter;
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

public class ReportSaleRepSeasonItemFragment extends BaseFragment {

    @BindView(R.id.rvReportSaleRepSeasonList)
    RecyclerView rvReportSaleRepSeasonList;
    @BindView(R.id.spTree)
    AutoCompleteTextView spTree;
    @BindView(R.id.tvSeason)
    CustomTextView tvSeason;
    @BindView(R.id.tvTitle)
    CustomBoldEditText tvTitle;
    @BindView(R.id.tvAcreage)
    CustomBoldEditText tvAcreage;
    @BindView(R.id.tvNotes)
    CustomBoldEditText tvNotes;
    @BindView(R.id.Layout_ReportSaleRepSeasonItem)
    LinearLayout Layout_ReportSaleRepSeasonItem;

    ReportSaleRepSeasonAdapter adapter;
    private String mReportSaleRepId = "";
    private String mParSymbol = "";
    private String mAction = "";

    List<SM_ReportSaleRepSeason> lstReportSaleRepSeason;
    String currentSeasonId;
    List<DM_Tree> lstTree;
    List<DM_Season> lstSeason;
    DM_Tree oTreeSel;

    DBGimsHelper mDB = null;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_report_sale_rep_season_form;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDB = DBGimsHelper.getInstance(getActivity().getBaseContext());
        adapter = new ReportSaleRepSeasonAdapter(new ReportSaleRepSeasonAdapter.ListItemClickListener() {
            @Override
            public void onItemClick(List<SM_ReportSaleRepSeason> SelectList) {
                if (SelectList.size() > 0) {
                    for (SM_ReportSaleRepSeason osmDT : SelectList) {
                        setReportSaleRepSeasonRow(osmDT);
                        break;
                    }
                    //Chỉ cho phép sửa khi chọn 1 dòng
                    if (SelectList.size() > 1) {
                        ((ReportSaleRepFormActivity) getActivity()).setButtonEditStatus(false);
                        Layout_ReportSaleRepSeasonItem.setVisibility(View.GONE);
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
        rvReportSaleRepSeasonList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReportSaleRepSeasonList.setAdapter(adapter);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //GET PARAMETER FORM ACTIVITY
                        ReportSaleRepFormActivity oActivity = (ReportSaleRepFormActivity) getActivity();
                        lstReportSaleRepSeason = oActivity.getoReportSaleRepSeason();
                        mReportSaleRepId=oActivity.getmReportSaleRepID();
                        mAction=oActivity.getAction();
                        if (lstReportSaleRepSeason != null) {
                            adapter.setsmoReportSaleRepSeason(lstReportSaleRepSeason);
                        }
                        mParSymbol=oActivity.getmPar_Symbol();
                        lstTree=oActivity.getListTree();
                        lstSeason = mDB.getAllSeason();
                        initDropdownTree();
                        initDropdownSeason();
                    }} ,300);

        Layout_ReportSaleRepSeasonItem.setVisibility(View.GONE);
    }

    // CALL FROM ACTIVITY
    public List<SM_ReportSaleRepSeason> getListReportSaleRepSeason(){
        return lstReportSaleRepSeason;
    }

    private String[] lstTreeSelect;
    private boolean[] lstTreeSelectChecked;
    private ArrayList<Integer> lstTreeSelectPos = new ArrayList<>();

    private String[] lstSeasonSelect;
    private boolean[] lstSeasonSelectChecked;
    private ArrayList<Integer> lstSeasonSelectPos = new ArrayList<>();

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

    private void initDropdownSeason(){
        try{
            if(lstSeason==null){lstSeason=new ArrayList<DM_Season>();}

            lstSeasonSelect=new String[lstSeason.size()];
            lstSeasonSelectChecked=new boolean[lstSeason.size()];

            for(int i=0;i<lstSeason.size();i++){
                lstSeasonSelect[i]=lstSeason.get(i).getSeasonName();
                lstSeasonSelectChecked[i]=false;
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

    private String getListSeasonName(String mlstSeasonSelect){
        try{
            String mSeasonListName="";
            lstSeasonSelectPos.clear();
            if(!mlstSeasonSelect.isEmpty()){
                String[] mlstSeasonCode=mlstSeasonSelect.split(",");
                for(int iP=0;iP<mlstSeasonCode.length;iP++){
                    for(int jP=0;jP<lstSeason.size();jP++){
                        if(lstSeason.get(jP).getSeasonCode().contains(mlstSeasonCode[iP])){
                            lstSeasonSelectPos.add(jP);
                            mSeasonListName=mSeasonListName+lstSeason.get(jP).getSeasonName();
                            if(iP!=mlstSeasonCode.length-1) {
                                mSeasonListName =mSeasonListName + ",";
                            }
                        }
                    }
                }
            }
            return  mSeasonListName;
        }catch (Exception ex){return  "";}
    }



    @OnClick(R.id.tvSeason)
    public void onClickSeason(){
        try{
            Arrays.fill(lstSeasonSelectChecked,false);
            for(int i=0;i<lstSeasonSelectPos.size();i++){
                lstSeasonSelectChecked[lstSeasonSelectPos.get(i)]=true;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false);
            builder.setTitle("Chọn mùa vụ");
            builder.setMultiChoiceItems(lstSeasonSelect,lstSeasonSelectChecked, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                    if(isChecked){
                        lstSeasonSelectPos.add(position);
                    }else{
                        lstSeasonSelectPos.remove(Integer.valueOf(position));
                    }
                }
            });

            builder.setPositiveButton("Chấp nhận", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    tvSeason.setText("");
                    String mSeasonSelectCode="",mSeasonSelectName="";
                    for (int i = 0; i<lstSeasonSelectPos.size(); i++){
                        mSeasonSelectCode=mSeasonSelectCode+ lstSeason.get(lstSeasonSelectPos.get(i)).getSeasonCode();
                        mSeasonSelectName=mSeasonSelectName+ lstSeason.get(lstSeasonSelectPos.get(i)).getSeasonName();
                        if(i!=lstSeasonSelectPos.size()-1){
                            mSeasonSelectCode=mSeasonSelectCode+",";
                            mSeasonSelectName=mSeasonSelectName+",";
                        }
                        tvSeason.setText(mSeasonSelectName);
                        tvSeason.setTag(mSeasonSelectCode);
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
                    for (int i = 0; i < lstSeasonSelectChecked.length; i++) {
                        lstSeasonSelectChecked[i] = false;
                        lstSeasonSelectPos.clear();
                        tvSeason.setText("");
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

    private void setReportSaleRepSeasonRow(SM_ReportSaleRepSeason osmDT) {
        try {
            if (osmDT != null) {
                if (osmDT.getTreeCode() != null && lstTree != null && lstTree.size() > 0) {
                    for(DM_Tree tree: lstTree){
                        if(tree.getTreeCode().equals(osmDT.getTreeCode())){
                            oTreeSel = tree;
                            break;
                        }
                    }

                    if(oTreeSel != null && oTreeSel.getTreeCode() != null){
                        spTree.setText(oTreeSel.getTreeName());
                        spTree.setTag(oTreeSel.getTreeCode());
                    }

                }else {
                    spTree.setText("");
                    spTree.setTag("");
                }

                if (osmDT.getSeasonCode() != null) {
                    tvSeason.setText(getListSeasonName(osmDT.getSeasonCode()));
                    tvSeason.setTag(osmDT.getSeasonCode());
                }else {
                    tvSeason.setText("");
                    tvSeason.setTag("");
                }

                if(osmDT.getTitle() != null){
                    tvTitle.setText(osmDT.getTitle());
                }
                if(osmDT.getAcreage() != null){
                    tvAcreage.setText(osmDT.getAcreage().toString());
                }
                if (osmDT.getNotes() != null) {
                    tvNotes.setText(osmDT.getNotes());
                }

                currentSeasonId = osmDT.getSeasonId();
            } else {
                Toast.makeText(getContext(), "Không tồn tại báo cáo mùa vụ..", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            Toast.makeText(getContext(), "Không thể khởi tạo thông tin để sửa..", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean onAddReportSaleRepSeason(boolean isAddnew) {
        ((ReportSaleRepFormActivity) getActivity()).setVisibleDetailDelete(false);
        if (Layout_ReportSaleRepSeasonItem.getVisibility() == View.GONE) {
            Layout_ReportSaleRepSeasonItem.setVisibility(View.VISIBLE);
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

    public boolean onSaveReportSaleRepSeason(){
        try{
            if(onSaveAddReportSaleRepSeason()){
                if(Layout_ReportSaleRepSeasonItem.getVisibility()==View.VISIBLE) {
                    Layout_ReportSaleRepSeasonItem.setVisibility(View.GONE);
                    adapter.clearSelected();
                }
                return  true;
            }
        }catch (Exception ex){return  false;}
        return  false;
    }

    private boolean onSaveAddReportSaleRepSeason() {
        SM_ReportSaleRepSeason oDetail = new SM_ReportSaleRepSeason();

        // EDIT
        if(currentSeasonId != null && currentSeasonId.length() > 0){
            oDetail.setSeasonId(currentSeasonId);
            currentSeasonId = "";
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

        if (tvSeason.getTag().toString().isEmpty()) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa chọn mùa vụ...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            tvSeason.requestFocus();
            return false;
        } else {
            oDetail.setSeasonCode(tvSeason.getTag().toString());
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
            oDetail.setAcreage(Float.parseFloat(tvAcreage.getText().toString()));
        }

        boolean isExist = false;
        for (int i = 0; i < lstReportSaleRepSeason.size(); i++) {
            if (lstReportSaleRepSeason.get(i).getSeasonId().equalsIgnoreCase(oDetail.getSeasonId())) {
                isExist = true;
                if (oDetail.getTitle() != null) {
                    lstReportSaleRepSeason.get(i).setTitle(oDetail.getTitle());
                } else {
                    lstReportSaleRepSeason.get(i).setTitle("");
                }
                if (oDetail.getNotes() != null) {
                    lstReportSaleRepSeason.get(i).setNotes(oDetail.getNotes());
                } else {
                    lstReportSaleRepSeason.get(i).setNotes("");
                }
                if (oDetail.getTreeCode() != null) {
                    lstReportSaleRepSeason.get(i).setTreeCode(oDetail.getTreeCode());
                } else {
                    lstReportSaleRepSeason.get(i).setTreeCode("");
                }
                if (oDetail.getSeasonCode() != null) {
                    lstReportSaleRepSeason.get(i).setSeasonCode(oDetail.getSeasonCode());
                } else {
                    lstReportSaleRepSeason.get(i).setSeasonCode("");
                }
                if (oDetail.getAcreage() != null) {
                    lstReportSaleRepSeason.get(i).setAcreage(oDetail.getAcreage());
                } else {
                    lstReportSaleRepSeason.get(i).setAcreage(0f);
                }


                Toast.makeText(getContext(), "Báo cáo dịch hại đã được thêm..", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        if (!isExist) {
            SimpleDateFormat Od = new SimpleDateFormat("ddMMyyyyHHmmssSS");
            String mSeasonId = "BCMV" + mParSymbol + Od.format(new Date());
            oDetail.setSeasonId(mSeasonId);
            oDetail.setReportSaleId(mReportSaleRepId);
            lstReportSaleRepSeason.add(oDetail);
        }

        adapter.setsmoReportSaleRepSeason(lstReportSaleRepSeason);
        Toast.makeText(getContext(), String.valueOf(lstReportSaleRepSeason.size()) + ": Báo cáo mùa vụ được chọn..", Toast.LENGTH_SHORT).show();

        tvTitle.setText("");
        tvNotes.setText("");
        spTree.setText("");
        spTree.setTag("");
        tvAcreage.setText("");
        tvSeason.setText("");
        tvSeason.setTag("");
        return true;
    }

    public void onDeletedReportSaleRepSeason(){
        if(adapter.SelectedList==null || adapter.SelectedList.size()<=0) {
            Toast oToat=Toast.makeText(getContext(),"Bạn chưa chọn báo cáo mùa vụ để xóa...",Toast.LENGTH_LONG);
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
                for(SM_ReportSaleRepSeason oDTSel:adapter.SelectedList){
                    for(SM_ReportSaleRepSeason oDT:lstReportSaleRepSeason){
                        if(oDTSel.equals(oDT)){
                            lstReportSaleRepSeason.remove(oDT);
                            break;
                        }
                    }
                }
                adapter.SelectedList.clear();
                adapter.setsmoReportSaleRepSeason(lstReportSaleRepSeason);

                // Set view
                rvReportSaleRepSeasonList.setAdapter(adapter);
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
