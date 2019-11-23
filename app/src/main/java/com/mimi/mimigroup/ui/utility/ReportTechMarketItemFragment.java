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
import com.mimi.mimigroup.model.SM_ReportTechMarket;
import com.mimi.mimigroup.ui.adapter.ReportTechMarketAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldEditText;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class ReportTechMarketItemFragment extends BaseFragment {

    @BindView(R.id.rvReportTechMarketList)
    RecyclerView rvReportTechMarketList;
    @BindView(R.id.tvTitle)
    CustomBoldEditText tvTitle;
    @BindView(R.id.tvNotes)
    CustomBoldEditText tvNotes;
    @BindView(R.id.tvUseful)
    CustomBoldEditText tvUseful;
    @BindView(R.id.tvHarmful)
    CustomBoldEditText tvHarmful;
    @BindView(R.id.Layout_ReportTechMarketItem)
    LinearLayout Layout_ReportTechMarketItem;

    ReportTechMarketAdapter adapter;
    private String mReportTechId = "";
    private String mParSymbol = "";
    private String mAction = "";

    List<SM_ReportTechMarket> lstReportTechMarket;
    String currentMarketId;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_report_tech_market_form;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ReportTechMarketAdapter(new ReportTechMarketAdapter.ListItemClickListener() {
            @Override
            public void onItemClick(List<SM_ReportTechMarket> SelectList) {
                if (SelectList.size() > 0) {
                    for (SM_ReportTechMarket osmDT : SelectList) {
                        setReportTechMarketRow(osmDT);
                        break;
                    }
                    //Chỉ cho phép sửa khi chọn 1 dòng
                    if (SelectList.size() > 1) {
                        ((ReportTechFormActivity) getActivity()).setButtonEditStatus(false);
                        Layout_ReportTechMarketItem.setVisibility(View.GONE);
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
        rvReportTechMarketList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReportTechMarketList.setAdapter(adapter);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //GET PARAMETER FORM ACTIVITY
                        ReportTechFormActivity oActivity = (ReportTechFormActivity) getActivity();
                        lstReportTechMarket = oActivity.getoReportTechMarket();
                        mReportTechId=oActivity.getmReportTechID();
                        mAction=oActivity.getAction();
                        if (lstReportTechMarket != null) {
                            adapter.setSmoReportTechMarket(lstReportTechMarket);
                        }
                        mParSymbol=oActivity.getmPar_Symbol();
                    }} ,300);

        Layout_ReportTechMarketItem.setVisibility(View.GONE);
    }

    // CALL FROM ACTIVITY
    public List<SM_ReportTechMarket> getListReportTechMarket(){
        return lstReportTechMarket;
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

    private void setReportTechMarketRow(SM_ReportTechMarket osmDT) {
        try {
            if (osmDT != null) {
                if (osmDT.getTitle() != null) {
                    tvTitle.setText(osmDT.getTitle());
                }
                if (osmDT.getNotes() != null) {
                    tvNotes.setText(osmDT.getNotes());
                }
                if (osmDT.getUsefull() != null) {
                    tvUseful.setText(osmDT.getUsefull());
                }
                if (osmDT.getHarmful() != null) {
                    tvHarmful.setText(osmDT.getHarmful());
                }

                currentMarketId = osmDT.getMarketId();
            } else {
                Toast.makeText(getContext(), "Không tồn tại báo cáo thị trường..", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            Toast.makeText(getContext(), "Không thể khởi tạo thông tin để sửa..", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean onAddReportTechMarket(boolean isAddnew) {
        ((ReportTechFormActivity) getActivity()).setVisibleDetailDelete(false);
        if (Layout_ReportTechMarketItem.getVisibility() == View.GONE) {
            Layout_ReportTechMarketItem.setVisibility(View.VISIBLE);
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

    public boolean onSaveReportTechMarket(){
        try{
            if(onSaveAddReportTechMarket()){
                if(Layout_ReportTechMarketItem.getVisibility()==View.VISIBLE) {
                    Layout_ReportTechMarketItem.setVisibility(View.GONE);
                    adapter.clearSelected();
                }
                return  true;
            }
        }catch (Exception ex){return  false;}
        return  false;
    }

    private boolean onSaveAddReportTechMarket() {
        SM_ReportTechMarket oDetail = new SM_ReportTechMarket();

        // EDIT
        if(currentMarketId != null && currentMarketId.length() > 0){
            oDetail.setMarketId(currentMarketId);
            currentMarketId = "";
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
            oDetail.setUsefull(tvUseful.getText().toString());
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
        for (int i = 0; i < lstReportTechMarket.size(); i++) {
            if (lstReportTechMarket.get(i).getMarketId().equalsIgnoreCase(oDetail.getMarketId())) {
                isExist = true;
                if (oDetail.getTitle() != null) {
                    lstReportTechMarket.get(i).setTitle(oDetail.getTitle());
                } else {
                    lstReportTechMarket.get(i).setTitle("");
                }
                if (oDetail.getNotes() != null) {
                    lstReportTechMarket.get(i).setNotes(oDetail.getNotes());
                } else {
                    lstReportTechMarket.get(i).setNotes("");
                }
                if (oDetail.getUsefull() != null) {
                    lstReportTechMarket.get(i).setUsefull(oDetail.getUsefull());
                } else {
                    lstReportTechMarket.get(i).setUsefull("");
                }
                if (oDetail.getHarmful() != null) {
                    lstReportTechMarket.get(i).setHarmful(oDetail.getHarmful());
                } else {
                    lstReportTechMarket.get(i).setHarmful("");
                }

                Toast.makeText(getContext(), "Báo cáo thị trường đã được thêm..", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        if (!isExist) {
            SimpleDateFormat Od = new SimpleDateFormat("ddMMyyyyHHmmssSS");
            String mMarketId = "BCTT" + mParSymbol + Od.format(new Date());
            oDetail.setMarketId(mMarketId);
            oDetail.setReportTechId(mReportTechId);
            lstReportTechMarket.add(oDetail);
        }

        adapter.setSmoReportTechMarket(lstReportTechMarket);
        Toast.makeText(getContext(), String.valueOf(lstReportTechMarket.size()) + ": Báo cáo thị trường được chọn..", Toast.LENGTH_SHORT).show();

        tvTitle.setText("");
        tvNotes.setText("");
        tvUseful.setText("");
        tvHarmful.setText("");
        return true;
    }

    public void onDeletedReportTechMarket(){
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
                for(SM_ReportTechMarket oDTSel:adapter.SelectedList){
                    for(SM_ReportTechMarket oDT:lstReportTechMarket){
                        if(oDTSel.equals(oDT)){
                            lstReportTechMarket.remove(oDT);
                            break;
                        }
                    }
                }
                adapter.SelectedList.clear();
                adapter.setSmoReportTechMarket(lstReportTechMarket);

                // Set view
                rvReportTechMarketList.setAdapter(adapter);
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
