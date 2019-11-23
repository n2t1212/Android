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
import com.mimi.mimigroup.model.SM_ReportTechActivity;
import com.mimi.mimigroup.ui.adapter.ReportTechActivityThisWeekAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldEditText;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class ReportTechActivityThisWeekItemFragment extends BaseFragment {

    @BindView(R.id.rvReportTechActivityThisWeekList)
    RecyclerView rvReportTechActivityThisWeekList;
    @BindView(R.id.tvTitle)
    CustomBoldEditText tvTitle;
    @BindView(R.id.tvNotes)
    CustomBoldEditText tvNotes;
    @BindView(R.id.tvAchievement)
    CustomBoldEditText tvAchievement;

    @BindView(R.id.Layout_ReportTechActivityThisWeekItem)
    LinearLayout Layout_ReportTechActivityThisWeekItem;

    ReportTechActivityThisWeekAdapter adapter;
    private String mReportTechId = "";
    private String mParSymbol = "";
    private String mAction = "";

    List<SM_ReportTechActivity> lstReportTechActivity;
    String currentActivityId;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_report_tech_activity_this_week_form;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ReportTechActivityThisWeekAdapter(new ReportTechActivityThisWeekAdapter.ListItemClickListener() {
            @Override
            public void onItemClick(List<SM_ReportTechActivity> SelectList) {
                if (SelectList.size() > 0) {
                    for (SM_ReportTechActivity osmDT : SelectList) {
                        setReportTechActivityRow(osmDT);
                        break;
                    }
                    //Chỉ cho phép sửa khi chọn 1 dòng
                    if (SelectList.size() > 1) {
                        ((ReportTechFormActivity) getActivity()).setButtonEditStatus(false);
                        Layout_ReportTechActivityThisWeekItem.setVisibility(View.GONE);
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
        rvReportTechActivityThisWeekList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReportTechActivityThisWeekList.setAdapter(adapter);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //GET PARAMETER FORM ACTIVITY
                        ReportTechFormActivity oActivity = (ReportTechFormActivity) getActivity();
                        lstReportTechActivity = oActivity.getoReportTechActivityThisWeek();
                        mReportTechId=oActivity.getmReportTechID();
                        mAction=oActivity.getAction();
                        if (lstReportTechActivity != null) {
                            adapter.setsmoReportTechActivity(lstReportTechActivity);
                        }
                        mParSymbol=oActivity.getmPar_Symbol();
                    }} ,300);

        Layout_ReportTechActivityThisWeekItem.setVisibility(View.GONE);
    }

    // CALL FROM ACTIVITY
    public List<SM_ReportTechActivity> getListReportTechActivityThisWeek(){
        return lstReportTechActivity;
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

    private void setReportTechActivityRow(SM_ReportTechActivity osmDT) {
        try {
            if (osmDT != null) {
                if (osmDT.getTitle() != null) {
                    tvTitle.setText(osmDT.getTitle());
                }
                if (osmDT.getNotes() != null) {
                    tvNotes.setText(osmDT.getNotes());
                }
                if (osmDT.getAchievement() != null) {
                    tvAchievement.setText(osmDT.getAchievement());
                }

                currentActivityId = osmDT.getActivitieId();
            } else {
                Toast.makeText(getContext(), "Không tồn tại báo cáo hoạt động trong tuần..", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            Toast.makeText(getContext(), "Không thể khởi tạo thông tin để sửa..", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean onAddReportTechActivity(boolean isAddnew) {
        ((ReportTechFormActivity) getActivity()).setVisibleDetailDelete(false);
        if (Layout_ReportTechActivityThisWeekItem.getVisibility() == View.GONE) {
            Layout_ReportTechActivityThisWeekItem.setVisibility(View.VISIBLE);
            //Clear Input
            try {
                if (isAddnew) {
                    tvTitle.setText("");
                    tvNotes.setText("");
                    tvAchievement.setText("");
                }
            } catch (Exception ex) {
            }
            return true;
        }
        return true;
    }

    public boolean onSaveReportTechActivity(){
        try{
            if(onSaveAddReportTechActivity()){
                if(Layout_ReportTechActivityThisWeekItem.getVisibility()==View.VISIBLE) {
                    Layout_ReportTechActivityThisWeekItem.setVisibility(View.GONE);
                    adapter.clearSelected();
                }
                return  true;
            }
        }catch (Exception ex){return  false;}
        return  false;
    }

    private boolean onSaveAddReportTechActivity() {
        SM_ReportTechActivity oDetail = new SM_ReportTechActivity();

        // EDIT
        if(currentActivityId != null && currentActivityId.length() > 0){
            oDetail.setActivitieId(currentActivityId);
            currentActivityId = "";
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
        if (tvAchievement.getText() == null || tvAchievement.getText().toString().isEmpty()) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập kết quả đạt được...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            tvAchievement.requestFocus();
            return false;
        } else {
            oDetail.setAchievement(tvAchievement.getText().toString());
        }

        boolean isExist = false;
        for (int i = 0; i < lstReportTechActivity.size(); i++) {
            if (lstReportTechActivity.get(i).getActivitieId().equalsIgnoreCase(oDetail.getActivitieId())) {
                isExist = true;
                if (oDetail.getTitle() != null) {
                    lstReportTechActivity.get(i).setTitle(oDetail.getTitle());
                } else {
                    lstReportTechActivity.get(i).setTitle("");
                }
                if (oDetail.getNotes() != null) {
                    lstReportTechActivity.get(i).setNotes(oDetail.getNotes());
                } else {
                    lstReportTechActivity.get(i).setNotes("");
                }
                if (oDetail.getAchievement() != null) {
                    lstReportTechActivity.get(i).setAchievement(oDetail.getAchievement());
                } else {
                    lstReportTechActivity.get(i).setAchievement("");
                }

                Toast.makeText(getContext(), "Báo cáo hoạt động trong tuần đã được thêm..", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        if (!isExist) {
            SimpleDateFormat Od = new SimpleDateFormat("ddMMyyyyHHmmssSS");
            String mId = "BCHDTT" + mParSymbol + Od.format(new Date());
            oDetail.setActivitieId(mId);
            oDetail.setReportTechId(mReportTechId);
            oDetail.setIsType(0);
            lstReportTechActivity.add(oDetail);
        }

        adapter.setsmoReportTechActivity(lstReportTechActivity);
        Toast.makeText(getContext(), String.valueOf(lstReportTechActivity.size()) + ": Báo cáo hoạt động trong tuần được chọn..", Toast.LENGTH_SHORT).show();

        tvTitle.setText("");
        tvNotes.setText("");
        tvAchievement.setText("");
        return true;
    }

    public void onDeletedReportTechActivity(){
        if(adapter.SelectedList==null || adapter.SelectedList.size()<=0) {
            Toast oToat=Toast.makeText(getContext(),"Bạn chưa chọn báo cáo hoạt động trong tuần để xóa...",Toast.LENGTH_LONG);
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
                for(SM_ReportTechActivity oDTSel:adapter.SelectedList){
                    for(SM_ReportTechActivity oDT:lstReportTechActivity){
                        if(oDTSel.equals(oDT)){
                            lstReportTechActivity.remove(oDT);
                            break;
                        }
                    }
                }
                adapter.SelectedList.clear();
                adapter.setsmoReportTechActivity(lstReportTechActivity);

                // Set view
                rvReportTechActivityThisWeekList.setAdapter(adapter);
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

