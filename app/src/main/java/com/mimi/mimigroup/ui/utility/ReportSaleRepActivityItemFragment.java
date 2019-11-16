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
import com.mimi.mimigroup.model.SM_ReportSaleRepActivitie;
import com.mimi.mimigroup.ui.adapter.ReportSaleRepActivityAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldEditText;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class ReportSaleRepActivityItemFragment extends BaseFragment {

    @BindView(R.id.rvReportSaleRepActivityList)
    RecyclerView rvReportSaleRepActivityList;
    @BindView(R.id.tvTitle)
    CustomBoldEditText tvTitle;
    @BindView(R.id.tvNotes)
    CustomBoldEditText tvNotes;
    @BindView(R.id.tvWorkday)
    CustomBoldEditText tvWorkday;
    @BindView(R.id.tvPlace)
    CustomBoldEditText tvPlace;

    @BindView(R.id.Layout_ReportSaleRepActivityItem)
    LinearLayout Layout_ReportSaleRepActivityItem;

    ReportSaleRepActivityAdapter adapter;
    private String mReportSaleId = "";
    private String mParSymbol = "";
    private String mAction = "";

    List<SM_ReportSaleRepActivitie> lstReportSaleRepActivity;
    String currentActivityId;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_report_sale_rep_activity_form;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ReportSaleRepActivityAdapter(new ReportSaleRepActivityAdapter.ListItemClickListener() {
            @Override
            public void onItemClick(List<SM_ReportSaleRepActivitie> SelectList) {
                if (SelectList.size() > 0) {
                    for (SM_ReportSaleRepActivitie osmDT : SelectList) {
                        setReportSaleRepActivityRow(osmDT);
                        break;
                    }
                    //Chỉ cho phép sửa khi chọn 1 dòng
                    if (SelectList.size() > 1) {
                        ((ReportSaleRepFormActivity) getActivity()).setButtonEditStatus(false);
                        Layout_ReportSaleRepActivityItem.setVisibility(View.GONE);
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
        rvReportSaleRepActivityList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReportSaleRepActivityList.setAdapter(adapter);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //GET PARAMETER FORM ACTIVITY
                        ReportSaleRepFormActivity oActivity = (ReportSaleRepFormActivity) getActivity();
                        lstReportSaleRepActivity = oActivity.getoReportSaleRepActivity();
                        mReportSaleId = oActivity.getmReportSaleRepID();
                        mAction = oActivity.getAction();
                        if (lstReportSaleRepActivity != null) {
                            adapter.setsmoReportSaleRepActivity(lstReportSaleRepActivity);
                        }
                        mParSymbol = oActivity.getmPar_Symbol();
                    }
                }, 300);

        Layout_ReportSaleRepActivityItem.setVisibility(View.GONE);
    }

    // CALL FROM ACTIVITY
    public List<SM_ReportSaleRepActivitie> getListReportSaleRepActivity() {
        return lstReportSaleRepActivity;
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

    private void setReportSaleRepActivityRow(SM_ReportSaleRepActivitie osmDT) {
        try {
            if (osmDT != null) {
                if (osmDT.getTitle() != null) {
                    tvTitle.setText(osmDT.getTitle());
                }
                if (osmDT.getNotes() != null) {
                    tvNotes.setText(osmDT.getNotes());
                }
                if (osmDT.getWorkDay() != null) {
                    tvWorkday.setText(osmDT.getWorkDay());
                }
                if (osmDT.getPlace() != null) {
                    tvPlace.setText(osmDT.getPlace());
                }
                currentActivityId = osmDT.getActivitieId();
            } else {
                Toast.makeText(getContext(), "Không tồn tại báo cáo lịch công tác..", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            Toast.makeText(getContext(), "Không thể khởi tạo thông tin để sửa..", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean onAddReportSaleRepActivity(boolean isAddnew) {
        ((ReportSaleRepFormActivity) getActivity()).setVisibleDetailDelete(false);
        if (Layout_ReportSaleRepActivityItem.getVisibility() == View.GONE) {
            Layout_ReportSaleRepActivityItem.setVisibility(View.VISIBLE);
            //Clear Input
            try {
                if (isAddnew) {
                    tvTitle.setText("");
                    tvNotes.setText("");
                    tvWorkday.setText("");
                    tvPlace.setText("");
                }
            } catch (Exception ex) {
            }
            return true;
        }
        return true;
    }

    public boolean onSaveReportSaleRepActivity() {
        try {
            if (onSaveAddReportSaleRepActivity()) {
                if (Layout_ReportSaleRepActivityItem.getVisibility() == View.VISIBLE) {
                    Layout_ReportSaleRepActivityItem.setVisibility(View.GONE);
                    adapter.clearSelected();
                }
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }

    private boolean onSaveAddReportSaleRepActivity() {
        SM_ReportSaleRepActivitie oDetail = new SM_ReportSaleRepActivitie();

        // EDIT
        if (currentActivityId != null && currentActivityId.length() > 0) {
            oDetail.setActivitieId(currentActivityId);
            currentActivityId = "";
        }

        if (tvTitle.getText() == null || tvTitle.getText().toString().isEmpty()) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập tiêu đề...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            return false;
        } else {
            oDetail.setTitle(tvTitle.getText().toString());
        }

        if (tvNotes.getText() == null || tvNotes.getText().toString().isEmpty()) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập ghi chú...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            return false;
        } else {
            oDetail.setNotes(tvNotes.getText().toString());
        }
        if (tvWorkday.getText() == null || tvWorkday.getText().toString().isEmpty()) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập ngày làm việc...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            return false;
        } else {
            oDetail.setWorkDay(tvWorkday.getText().toString());
        }
        if (tvPlace.getText() == null || tvPlace.getText().toString().isEmpty()) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập địa điểm...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            return false;
        } else {
            oDetail.setPlace(tvPlace.getText().toString());
        }

        boolean isExist = false;
        for (int i = 0; i < lstReportSaleRepActivity.size(); i++) {
            if (lstReportSaleRepActivity.get(i).getActivitieId().equalsIgnoreCase(oDetail.getActivitieId())) {
                isExist = true;
                if (oDetail.getTitle() != null) {
                    lstReportSaleRepActivity.get(i).setTitle(oDetail.getTitle());
                } else {
                    lstReportSaleRepActivity.get(i).setTitle("");
                }
                if (oDetail.getNotes() != null) {
                    lstReportSaleRepActivity.get(i).setNotes(oDetail.getNotes());
                } else {
                    lstReportSaleRepActivity.get(i).setNotes("");
                }
                if (oDetail.getWorkDay() != null) {
                    lstReportSaleRepActivity.get(i).setWorkDay(oDetail.getWorkDay());
                } else {
                    lstReportSaleRepActivity.get(i).setWorkDay("");
                }
                if (oDetail.getPlace() != null) {
                    lstReportSaleRepActivity.get(i).setPlace(oDetail.getPlace());
                } else {
                    lstReportSaleRepActivity.get(i).setPlace("");
                }

                Toast.makeText(getContext(), "Báo cáo lịch công tác đã được thêm..", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        if (!isExist) {
            SimpleDateFormat Od = new SimpleDateFormat("ddMMyyyyHHmmssSS");
            String mId = "BCLCT" + mParSymbol + Od.format(new Date());
            oDetail.setActivitieId(mId);
            oDetail.setReportSaleId(mReportSaleId);
            oDetail.setIsType("1");
            lstReportSaleRepActivity.add(oDetail);
        }

        adapter.setsmoReportSaleRepActivity(lstReportSaleRepActivity);
        Toast.makeText(getContext(), String.valueOf(lstReportSaleRepActivity.size()) + ": Báo cáo lịch công tác được chọn..", Toast.LENGTH_SHORT).show();

        tvTitle.setText("");
        tvNotes.setText("");
        tvWorkday.setText("");
        tvPlace.setText("");
        return true;
    }

    public void onDeletedReportSaleRepActivity() {
        if (adapter.SelectedList == null || adapter.SelectedList.size() <= 0) {
            Toast oToat = Toast.makeText(getContext(), "Bạn chưa chọn báo cáo lịch công tác để xóa...", Toast.LENGTH_LONG);
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
                for (SM_ReportSaleRepActivitie oDTSel : adapter.SelectedList) {
                    for (SM_ReportSaleRepActivitie oDT : lstReportSaleRepActivity) {
                        if (oDTSel.equals(oDT)) {
                            lstReportSaleRepActivity.remove(oDT);
                            break;
                        }
                    }
                }
                adapter.SelectedList.clear();
                adapter.setsmoReportSaleRepActivity(lstReportSaleRepActivity);

                // Set view
                rvReportSaleRepActivityList.setAdapter(adapter);
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


