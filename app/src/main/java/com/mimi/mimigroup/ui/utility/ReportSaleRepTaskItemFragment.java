package com.mimi.mimigroup.ui.utility;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.base.BaseFragment;
import com.mimi.mimigroup.model.SM_ReportSaleRepActivitie;
import com.mimi.mimigroup.ui.adapter.ReportSaleRepTaskAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldEditText;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ReportSaleRepTaskItemFragment extends BaseFragment {

    @BindView(R.id.rvReportSaleRepTaskList)
    RecyclerView rvReportSaleRepTaskList;
    @BindView(R.id.tvTitle)
    CustomBoldEditText tvTitle;
    @BindView(R.id.tvNotes)
    CustomBoldEditText tvNotes;
    @BindView(R.id.tvWorkday)
    CustomTextView tvWorkday;
    @BindView(R.id.tvPlace)
    CustomBoldEditText tvPlace;

    @BindView(R.id.Layout_ReportSaleRepTaskItem)
    LinearLayout Layout_ReportSaleRepTaskItem;

    ReportSaleRepTaskAdapter adapter;
    private String mReportSaleId = "";
    private String mParSymbol = "";
    private String mAction = "";
    private DatePickerDialog dtPicker;

    List<SM_ReportSaleRepActivitie> lstReportSaleRepTask;
    String currentActivityId;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_report_sale_rep_task_form;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ReportSaleRepTaskAdapter(new ReportSaleRepTaskAdapter.ListItemClickListener() {
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
                        Layout_ReportSaleRepTaskItem.setVisibility(View.GONE);
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
        rvReportSaleRepTaskList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReportSaleRepTaskList.setAdapter(adapter);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //GET PARAMETER FORM ACTIVITY
                        ReportSaleRepFormActivity oActivity = (ReportSaleRepFormActivity) getActivity();
                        lstReportSaleRepTask = oActivity.getoReportSaleRepTask();
                        mReportSaleId = oActivity.getmReportSaleRepID();
                        mAction = oActivity.getAction();
                        if (lstReportSaleRepTask != null) {
                            adapter.setsmoReportSaleRepTask(lstReportSaleRepTask);
                        }
                        mParSymbol = oActivity.getmPar_Symbol();
                    }
                }, 300);

        Layout_ReportSaleRepTaskItem.setVisibility(View.GONE);
    }

    // CALL FROM ACTIVITY
    public List<SM_ReportSaleRepActivitie> getListReportSaleRepTask() {
        return lstReportSaleRepTask;
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
                Toast.makeText(getContext(), "Không tồn tại báo cáo công việc..", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            Toast.makeText(getContext(), "Không thể khởi tạo thông tin để sửa..", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean onAddReportSaleRepActivity(boolean isAddnew) {
        ((ReportSaleRepFormActivity) getActivity()).setVisibleDetailDelete(false);
        if (Layout_ReportSaleRepTaskItem.getVisibility() == View.GONE) {
            Layout_ReportSaleRepTaskItem.setVisibility(View.VISIBLE);
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
                if (Layout_ReportSaleRepTaskItem.getVisibility() == View.VISIBLE) {
                    Layout_ReportSaleRepTaskItem.setVisibility(View.GONE);
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

        if (tvPlace.getText() == null || tvPlace.getText().toString().isEmpty()) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập địa điểm...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            tvPlace.requestFocus();
            return false;
        } else {
            oDetail.setPlace(tvPlace.getText().toString());
        }

        if (tvWorkday.getText() == null || tvWorkday.getText().toString().isEmpty()) {
            Toast oT = Toast.makeText(getContext(), "Bạn chưa nhập ngày làm việc...", Toast.LENGTH_LONG);
            oT.setGravity(Gravity.CENTER, 0, 0);
            oT.show();
            tvWorkday.requestFocus();
            return false;
        } else {
            oDetail.setWorkDay(tvWorkday.getText().toString());
        }

        boolean isExist = false;
        for (int i = 0; i < lstReportSaleRepTask.size(); i++) {
            if (lstReportSaleRepTask.get(i).getActivitieId().equalsIgnoreCase(oDetail.getActivitieId())) {
                isExist = true;
                if (oDetail.getTitle() != null) {
                    lstReportSaleRepTask.get(i).setTitle(oDetail.getTitle());
                } else {
                    lstReportSaleRepTask.get(i).setTitle("");
                }
                if (oDetail.getNotes() != null) {
                    lstReportSaleRepTask.get(i).setNotes(oDetail.getNotes());
                } else {
                    lstReportSaleRepTask.get(i).setNotes("");
                }
                if (oDetail.getWorkDay() != null) {
                    lstReportSaleRepTask.get(i).setWorkDay(oDetail.getWorkDay());
                } else {
                    lstReportSaleRepTask.get(i).setWorkDay("");
                }
                if (oDetail.getPlace() != null) {
                    lstReportSaleRepTask.get(i).setPlace(oDetail.getPlace());
                } else {
                    lstReportSaleRepTask.get(i).setPlace("");
                }

                Toast.makeText(getContext(), "Báo cáo công việc đã được thêm..", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        if (!isExist) {
            SimpleDateFormat Od = new SimpleDateFormat("ddMMyyyyHHmmssSS");
            String mId = "BCV" + mParSymbol + Od.format(new Date());
            oDetail.setActivitieId(mId);
            oDetail.setReportSaleId(mReportSaleId);
            oDetail.setIsType("0");
            lstReportSaleRepTask.add(oDetail);
        }

        adapter.setsmoReportSaleRepTask(lstReportSaleRepTask);
        Toast.makeText(getContext(), String.valueOf(lstReportSaleRepTask.size()) + ": Báo cáo công việc được chọn..", Toast.LENGTH_SHORT).show();

        tvTitle.setText("");
        tvNotes.setText("");
        tvWorkday.setText("");
        tvPlace.setText("");
        return true;
    }

    public void onDeletedReportSaleRepActivity() {
        if (adapter.SelectedList == null || adapter.SelectedList.size() <= 0) {
            Toast oToat = Toast.makeText(getContext(), "Bạn chưa chọn báo cáo công việc để xóa...", Toast.LENGTH_LONG);
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
                    for (SM_ReportSaleRepActivitie oDT : lstReportSaleRepTask) {
                        if (oDTSel.equals(oDT)) {
                            lstReportSaleRepTask.remove(oDT);
                            break;
                        }
                    }
                }
                adapter.SelectedList.clear();
                adapter.setsmoReportSaleRepTask(lstReportSaleRepTask);

                // Set view
                rvReportSaleRepTaskList.setAdapter(adapter);
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

    @OnClick(R.id.tvWorkday)
    public void onWorkday()
    {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        // date picker dialog
        dtPicker = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cldr.set(year,monthOfYear, dayOfMonth);
                        String dateString = sdf.format(cldr.getTime());
                        tvWorkday.setText(dateString);
                    }
                }, year, month, day);

        dtPicker.getDatePicker().setCalendarViewShown(false);
        dtPicker.getDatePicker().setSpinnersShown(true);
        dtPicker.show();
    }
}

