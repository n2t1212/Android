package com.mimi.mimigroup.ui.utility;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.base.BaseFragment;
import com.mimi.mimigroup.model.SM_ReportTechCompetitor;
import com.mimi.mimigroup.ui.adapter.ReportTechCompetitorAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldEditText;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class ReportTechCompetitorItemFragment extends BaseFragment {

    @BindView(R.id.rvReportTechCompetitorList)
    RecyclerView rvReportTechCompetitorList;
    @BindView(R.id.tvTitle)
    CustomBoldEditText tvTitle;
    @BindView(R.id.tvNotes)
    CustomBoldEditText tvNotes;
    @BindView(R.id.tvUseful)
    CustomBoldEditText tvUseful;
    @BindView(R.id.tvHarmful)
    CustomBoldEditText tvHarmful;
    @BindView(R.id.Layout_ReportTechCompetitorItem)
    LinearLayout Layout_ReportTechCompetitorItem;

    ReportTechCompetitorAdapter adapter;
    private String mReportTechId = "";
    private String mParSymbol = "";
    private String mAction = "";

    List<SM_ReportTechCompetitor> lstReportTechCompetitor;
    String currentCompetitorId;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_report_tech_competitor_form;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ReportTechCompetitorAdapter(new ReportTechCompetitorAdapter.ListItemClickListener() {
            @Override
            public void onItemClick(List<SM_ReportTechCompetitor> SelectList) {
                if (SelectList.size() > 0) {
                    for (SM_ReportTechCompetitor osmDT : SelectList) {
                        setReportTechMarketRow(osmDT);
                        break;
                    }
                    //Chỉ cho phép sửa khi chọn 1 dòng
                    if (SelectList.size() > 1) {
                        ((ReportTechFormActivity) getActivity()).setButtonEditStatus(false);
                        Layout_ReportTechCompetitorItem.setVisibility(View.GONE);
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
        rvReportTechCompetitorList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReportTechCompetitorList.setAdapter(adapter);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //GET PARAMETER FORM ACTIVITY
                        ReportTechFormActivity oActivity = (ReportTechFormActivity) getActivity();
                        lstReportTechCompetitor = oActivity.getoReportTechCompetitor();
                        mReportTechId=oActivity.getmReportTechID();
                        mAction=oActivity.getAction();
                        if (lstReportTechCompetitor != null) {
                            adapter.setsmoReportTechCompetitor(lstReportTechCompetitor);
                        }
                        mParSymbol=oActivity.getmPar_Symbol();
                    }} ,300);

        Layout_ReportTechCompetitorItem.setVisibility(View.GONE);
    }

    // CALL FROM ACTIVITY
    public List<SM_ReportTechCompetitor> getListReportTechCompetitor(){
        return lstReportTechCompetitor;
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

    private void setReportTechMarketRow(SM_ReportTechCompetitor osmDT) {
        try {
            if (osmDT != null) {
                if (osmDT.getTitle() != null) {
                    tvTitle.setText(osmDT.getTitle());
                }
                if (osmDT.getNotes() != null) {
                    tvNotes.setText(osmDT.getNotes());
                }
                if (osmDT.getUseful() != null) {
                    tvUseful.setText(osmDT.getUseful());
                }
                if (osmDT.getHarmful() != null) {
                    tvHarmful.setText(osmDT.getHarmful());
                }

                currentCompetitorId = osmDT.getCompetitorId();
            } else {
                Toast.makeText(getContext(), "Không tồn tại báo cáo đối thủ..", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            Toast.makeText(getContext(), "Không thể khởi tạo thông tin để sửa..", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean onAddReportTechCompetitor(boolean isAddnew) {
        ((ReportTechFormActivity) getActivity()).setVisibleDetailDelete(false);
        if (Layout_ReportTechCompetitorItem.getVisibility() == View.GONE) {
            Layout_ReportTechCompetitorItem.setVisibility(View.VISIBLE);
            //Clear Input
            try {
                if (isAddnew) {
                    tvTitle.setText("");
                    tvNotes.setText("");
                    tvUseful.setText("");
                    tvHarmful.setText("");
                }
            } catch (Exception ex) {
            }
            return true;
        }
        return true;
    }

    public boolean onSaveReportTechCompetitor(){
        try{
            if(onSaveAddReportTechCompetitor()){
                if(Layout_ReportTechCompetitorItem.getVisibility()==View.VISIBLE) {
                    Layout_ReportTechCompetitorItem.setVisibility(View.GONE);
                    adapter.clearSelected();
                }
                return  true;
            }
        }catch (Exception ex){return  false;}
        return  false;
    }

    private boolean onSaveAddReportTechCompetitor() {
        SM_ReportTechCompetitor oDetail = new SM_ReportTechCompetitor();

        // EDIT
        if(currentCompetitorId != null && currentCompetitorId.length() > 0){
            oDetail.setCompetitorId(currentCompetitorId);
            currentCompetitorId = "";
        }

        if (tvTitle.getText() == null || tvTitle.getText().toString().isEmpty()) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập tiêu đề...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            tvTitle.requestFocus();
            return false;
        } else {
            oDetail.setTitle(tvTitle.getText().toString());
        }

        if (tvNotes.getText() == null || tvNotes.getText().toString().isEmpty()) {
            oDetail.setNotes("");
        } else {
            oDetail.setNotes(tvNotes.getText().toString());
        }
        if (tvUseful.getText() == null || tvUseful.getText().toString().isEmpty()) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập tác động có lợi...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            tvUseful.requestFocus();
            return false;
        } else {
            oDetail.setUseful(tvUseful.getText().toString());
        }
        if (tvHarmful.getText() == null || tvHarmful.getText().toString().isEmpty()) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập tác động có hại...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            tvHarmful.requestFocus();
            return false;
        } else {
            oDetail.setHarmful(tvHarmful.getText().toString());
        }

        boolean isExist = false;
        for (int i = 0; i < lstReportTechCompetitor.size(); i++) {
            if (lstReportTechCompetitor.get(i).getCompetitorId().equalsIgnoreCase(oDetail.getCompetitorId())) {
                isExist = true;
                if (oDetail.getTitle() != null) {
                    lstReportTechCompetitor.get(i).setTitle(oDetail.getTitle());
                } else {
                    lstReportTechCompetitor.get(i).setTitle("");
                }
                if (oDetail.getNotes() != null) {
                    lstReportTechCompetitor.get(i).setNotes(oDetail.getNotes());
                } else {
                    lstReportTechCompetitor.get(i).setNotes("");
                }
                if (oDetail.getUseful() != null) {
                    lstReportTechCompetitor.get(i).setUseful(oDetail.getUseful());
                } else {
                    lstReportTechCompetitor.get(i).setUseful("");
                }
                if (oDetail.getHarmful() != null) {
                    lstReportTechCompetitor.get(i).setHarmful(oDetail.getHarmful());
                } else {
                    lstReportTechCompetitor.get(i).setHarmful("");
                }

                Toast.makeText(getContext(), "Báo cáo đối thủ đã được thêm..", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        if (!isExist) {
            SimpleDateFormat Od = new SimpleDateFormat("ddMMyyyyHHmmssSS");
            String mMarketId = "BCDT" + mParSymbol + Od.format(new Date());
            oDetail.setCompetitorId(mMarketId);
            oDetail.setReportTechId(mReportTechId);
            lstReportTechCompetitor.add(oDetail);
        }

        adapter.setsmoReportTechCompetitor(lstReportTechCompetitor);
        Toast.makeText(getContext(), String.valueOf(lstReportTechCompetitor.size()) + ": Báo cáo đối thủ được chọn..", Toast.LENGTH_SHORT).show();

        tvTitle.setText("");
        tvNotes.setText("");
        tvUseful.setText("");
        tvHarmful.setText("");
        return true;
    }

    public void onDeletedReportTechCompetitor(){
        if(adapter.SelectedList==null || adapter.SelectedList.size()<=0) {
            Toast oToat=Toast.makeText(getContext(),"Bạn chưa chọn báo cáo đối thủ để xóa...",Toast.LENGTH_LONG);
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
                for(SM_ReportTechCompetitor oDTSel:adapter.SelectedList){
                    for(SM_ReportTechCompetitor oDT:lstReportTechCompetitor){
                        if(oDTSel.equals(oDT)){
                            lstReportTechCompetitor.remove(oDT);
                            break;
                        }
                    }
                }
                adapter.SelectedList.clear();
                adapter.setsmoReportTechCompetitor(lstReportTechCompetitor);

                // Set view
                rvReportTechCompetitorList.setAdapter(adapter);
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
