package com.mimi.mimigroup.ui.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.mimi.mimigroup.R;
import com.mimi.mimigroup.api.APIGPSTrack;
import com.mimi.mimigroup.api.APINet;
import com.mimi.mimigroup.api.APINetCallBack;
import com.mimi.mimigroup.api.SyncPost;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseActivity;
import com.mimi.mimigroup.db.DBGimsHelper;
import com.mimi.mimigroup.model.DM_Customer_Search;
import com.mimi.mimigroup.model.DM_Product;
import com.mimi.mimigroup.model.SM_CustomerPay;
import com.mimi.mimigroup.model.SM_Order;
import com.mimi.mimigroup.model.SM_OrderDetail;
import com.mimi.mimigroup.ui.adapter.FragmentPagerTabAdapter;
import com.mimi.mimigroup.ui.adapter.SearchCustomerAdapter;
import com.mimi.mimigroup.ui.custom.CustomBoldEditText;
import com.mimi.mimigroup.ui.custom.CustomBoldTextView;
import com.mimi.mimigroup.ui.custom.CustomTextView;
import com.mimi.mimigroup.ui.order.OrderFormItemDetailFragment;
import com.mimi.mimigroup.ui.order.OrderFormItemFragment;
import com.mimi.mimigroup.ui.visitcard.VisitCardResultActivity;
import com.mimi.mimigroup.utils.AppUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class CustomerPayFormActivity extends BaseActivity {
    @BindView(R.id.tvPayCode)
    CustomBoldTextView tvPayCode;
    @BindView(R.id.tvPayDate)
    CustomBoldEditText tvPayDate;
    @BindView(R.id.tvPayMoney)
    CustomBoldEditText tvPayMoney;
    @BindView(R.id.tvPayNotes)
    CustomBoldEditText tvPayNotes;
    @BindView(R.id.tvPayStatus)
    CustomBoldTextView tvPayStatus;

    @BindView(R.id.tvCustomerCode)
    CustomBoldTextView tvCustomerCode;
    @BindView(R.id.tvLocationAddress)
    CustomBoldTextView tvLocationAddress;
    @BindView(R.id.spCustomerPay)
    AutoCompleteTextView spCustomerPay;

    @BindView(R.id.imgPayPic)
    ImageView imgPayPic;

     public String mPayID="";
     private String mPar_Symbol;
     private String mAction="";
     private DBGimsHelper mDB;
     private boolean isSaved=false;

     private SM_CustomerPay oPay;
     List<DM_Customer_Search> lstCustomer;
     private  DM_Customer_Search oCustomerSel;

    Location mLocation;
    APIGPSTrack locationTrack;

    Uri picUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerpay_form);
        mDB=DBGimsHelper.getInstance(this);

        mPayID  = getIntent().getStringExtra("PayID");
        mPar_Symbol  = getIntent().getStringExtra("PAR_SYMBOL");
        mAction=getIntent().getAction().toString();
        isSaved=false;
        lstCustomer=mDB.getAllCustomerSearch();

        locationTrack=new APIGPSTrack(CustomerPayFormActivity.this);
        if(locationTrack.canGetLocation()){
            mLocation=locationTrack.getLocation();
        }else{
            locationTrack.showSettingsAlert();
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                  mLocation = locationTrack.getLocation();
                  if(mLocation==null){
                    locationTrack=new APIGPSTrack(CustomerPayFormActivity.this);
                    if(locationTrack.canGetLocation()){
                       mLocation=locationTrack.getLocation();
                     }
                  }
                  initDropdownCustomer();
                  if(mAction=="EDIT"){
                      oPay=mDB.getCustomerPay(mPayID);
                      if(oPay!=null){
                          oPay.setPayStatus(1);
                          oPay.setLatitude(mLocation.getLatitude());
                          oPay.setLongitude(mLocation.getLongitude());
                          oPay.setLocationAddress(getAddressLocation(mLocation.getLongitude(),mLocation.getLatitude()));

                          tvPayDate.setText(oPay.getPayDate());
                          tvPayCode.setText(oPay.getPayCode());
                          tvPayMoney.setText(oPay.getPayMoney().toString());
                          tvPayNotes.setText(oPay.getPayNotes());
                          tvPayStatus.setText(oPay.getPayStatusDesc());
                          try{
                              spCustomerPay.setText(oPay.getCustomerName());
                              tvCustomerCode.setText(oPay.getCustomerCode());
                              tvLocationAddress.setText(oPay.getLocationAddress());

                              if (oPay.getPayPic()!=null && !oPay.getPayPic().isEmpty()) {
                                  //InputStream in = new java.net.URL(oPay.getPayPic()).openStream();
                                  //Bitmap imgPic = BitmapFactory.decodeStream(in);
                                  Bitmap imgPic = BitmapFactory.decodeFile(oPay.getPayPic());
                                  imgPayPic.setImageBitmap(imgPic);
                                  oPay.setPayPicBase(getBitmapBase64(imgPic));
                              }

                          }catch (Exception ex){}

                      }
                  }else{
                      oPay = new SM_CustomerPay();
                      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
                      String mPayDate = sdf.format(new Date());
                      SimpleDateFormat sdfPayCode = new SimpleDateFormat("ddMMyy");
                      String mPayCodeDMY = sdfPayCode.format(new Date());
                      SimpleDateFormat sdfhhMMss = new SimpleDateFormat("hhmmss");
                      String mPayCodeHMS = sdfhhMMss.format(new Date());
                      String mPayCode=mPar_Symbol+'-'+mPayCodeDMY+'/'+mPayCodeHMS;

                      tvPayDate.setText(mPayDate);
                      tvPayCode.setText(mPayCode);
                      if (mLocation!=null) {
                          if (mLocation.getLatitude()>0) {
                              tvLocationAddress.setText(getAddressLocation(mLocation.getLongitude(), mLocation.getLatitude()));
                          }
                      }
                      oPay.setPayID(mPayID);
                      oPay.setPayDate(mPayDate);
                      oPay.setPayCode(mPayCode);
                      oPay.setPayStatus(0);
                      oPay.setPost(false);
                      oPay.setLatitude(mLocation.getLatitude());
                      oPay.setLongitude(mLocation.getLongitude());
                      oPay.setLocationAddress(tvLocationAddress.getText().toString());
                  }

              }
          },300);
    }


    private void initDropdownCustomer(){
        try{
            ArrayList<DM_Customer_Search> oListCus=new ArrayList<DM_Customer_Search>();
            for(int i=0;i<lstCustomer.size();++i){
                oListCus.add(new DM_Customer_Search(lstCustomer.get(i).getCustomerid(),lstCustomer.get(i).getCustomerCode(),
                        lstCustomer.get(i).getCustomerName(),lstCustomer.get(i).getShortName(),lstCustomer.get(i).getProvinceName(),
                        lstCustomer.get(i).getLongititude(),lstCustomer.get(i).getLatitude()));
            }
            SearchCustomerAdapter adapter = new SearchCustomerAdapter(CustomerPayFormActivity.this , R.layout.search_customer,oListCus);
            spCustomerPay.setDropDownBackgroundResource(R.drawable.liner_dropdownlist);
            spCustomerPay.setThreshold(1);
            spCustomerPay.setAdapter(adapter);
            spCustomerPay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int iPosition, long l) {
                    try {
                        oCustomerSel = (DM_Customer_Search) adapterView.getItemAtPosition(iPosition);
                        String mUnit=oCustomerSel.getCustomerid();
                        if(oCustomerSel!=null && oCustomerSel.getCustomerid()!=""){
                            oPay.setCustomerID(oCustomerSel.getCustomerid());
                            if(oCustomerSel.getCustomerCode()!=null) {
                                oPay.setCustomerCode(oCustomerSel.getCustomerCode());
                                tvCustomerCode.setText(oCustomerSel.getCustomerCode());
                            }else{
                                tvCustomerCode.setText("");
                            }
                            if(oCustomerSel.getCustomerName()!=null){
                                oPay.setCustomerName(oCustomerSel.getCustomerName());
                            }
                        }

                    }catch (Exception ex){}
                }
            });

        }catch (Exception ex){}
    }

    public String getAddressLocation(Double Longitude,Double Latitude){
        try{
            if(APINet.isNetworkAvailable(this)) {
                List<Address> lstAddress;
                Geocoder gCoder = new Geocoder(this, Locale.getDefault());
                lstAddress = gCoder.getFromLocation(Latitude, Longitude, 1);
                String mAddress = lstAddress.get(0).getAddressLine(0);
                return mAddress;
            }else{
                return "N/A";
            }
        }catch (Exception ex){}
        return "N/A";
    }

    public String getBitmapBase64(Bitmap bitmap) {
        try{
            //bitmap.setWidth(1024);
            Bitmap oBm=getScaleBitmap(bitmap);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            oBm.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            byte[] byteFormat = stream.toByteArray();
            String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
            return imgString;
        }catch (Exception ex){return  "";}
    }

    private Bitmap getScaleBitmap(Bitmap bm){
        try{
            int maxHeight =1024;
            int maxWidth =1280;
            float scale = Math.min(((float)maxHeight / bm.getWidth()), ((float)maxWidth / bm.getHeight()));

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap oRtBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, false);
            return oRtBm;
        }catch (Exception ex){return  bm;}
    }



    @Override
    public void onBackPressed() {
        if (!isSaved) {
            final Dialog oDlg=new Dialog(this);
            oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            oDlg.setContentView(R.layout.dialog_yesno);
            oDlg.setTitle("");
            CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
            dlgTitle.setText("THÔNG BÁO");
            CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
            dlgContent.setText("Dữ liệu đơn hàng chưa cập nhật. Bạn có muốn bỏ qua ?");
            CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
            CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onExcuteBackPress();
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

        } else {
            super.onBackPressed();
        }
    }

    private void onExcuteBackPress(){
        super.onBackPressed();
    }
    @OnClick(R.id.ivBack)
    public void onBack(){
        if (!isSaved) {
            final Dialog oDlg=new Dialog(this);
            oDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            oDlg.setContentView(R.layout.dialog_yesno);
            oDlg.setTitle("");
            CustomTextView dlgTitle=(CustomTextView) oDlg.findViewById(R.id.dlgTitle);
            dlgTitle.setText("THÔNG BÁO");
            CustomTextView dlgContent=(CustomTextView) oDlg.findViewById(R.id.dlgContent);
            dlgContent.setText("Dữ liệu đơn hàng chưa cập nhật. Bạn có muốn bỏ qua ?");
            CustomBoldTextView btnYes=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonYes);
            CustomBoldTextView btnNo=(CustomBoldTextView) oDlg.findViewById(R.id.dlgButtonNo);

            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onExcuteBackPress();
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

            return;
        } else {
            super.onBackPressed();
        }

        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    private boolean isCapture=false;
    //static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 200;
    private boolean isCamera=false;

    @OnClick(R.id.btnPayPicAttach)
    public void onPayPicAttach() {
        try {
            //startActivityForResult(getPickImageChooserIntent(), REQUEST_IMAGE_CAPTURE);
            startActivityForResult(getPickImageChooserIntent(), REQUEST_IMAGE_CAPTURE);
        } catch (Exception ex) {
      }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==Activity.RESULT_OK) {
            if (requestCode ==REQUEST_IMAGE_CAPTURE) {
                String filePath = getImageFilePath(data);
                if (filePath != null) {
                    try {
                        Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                        if (selectedImage == null) {
                            picUri = data.getData();
                            imgPayPic.setImageURI(picUri);
                            imgPayPic.isDrawingCacheEnabled();
                            imgPayPic.buildDrawingCache();
                            Bitmap oBitmap = imgPayPic.getDrawingCache();
                            oPay.setPayPicBase(getBitmapBase64(oBitmap));
                            oPay.setPayPic(filePath);
                        } else {
                            imgPayPic.setImageBitmap(selectedImage);
                            if (selectedImage != null) {
                                oPay.setPayPicBase(getBitmapBase64(selectedImage));
                            }
                            oPay.setPayPic(filePath);
                            if (isCamera) {
                                try {
                                    //TRƯỜNG HỢP CAPTURE -> COPY SANG TÊN FILE MỚI
                                    SimpleDateFormat sdfPic = new SimpleDateFormat("ddMMyyhhmmss", Locale.US);
                                    String mPicFile = "mimi_" + sdfPic.format(new Date()) + ".png";
                                    File oFile = new File(filePath.replace("mimiPhoto.png", ""), mPicFile);
                                    FileOutputStream fOut = new FileOutputStream(oFile);
                                    //selectedImage.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                                    oPay.setPayPic("");
                                    if (oPay.getPayPicBase()!=null && !oPay.getPayPicBase().isEmpty()) {
                                        byte[] oBitmapDecode = Base64.decode(oPay.getPayPicBase(), Base64.DEFAULT);
                                        fOut.write(oBitmapDecode);
                                        //fOut.flush();
                                        fOut.close();
                                        oPay.setPayPic(oFile.getPath());
                                    }
                                }catch (Exception ex){}
                            }

                        }


                    } catch (Exception ex) {
                        Toast.makeText(CustomerPayFormActivity.this, "Không thể đọc tập tin: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    if (!oPay.getPayPicBase().isEmpty()) {
                        Toast.makeText(CustomerPayFormActivity.this, Integer.toString(oPay.getPayPicBase().length()), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(CustomerPayFormActivity.this, "Không đọc được đường dẫn tập tin..", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


   /* [OLD]
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && null != data) {
            String mImgPath="";
            try{
                imgPayPic.setImageBitmap(null);
                oPay.setPayPicBase("");
                if (isCapture) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imgPayPic.setImageBitmap(imageBitmap);
                    if(imageBitmap!=null){
                        oPay.setPayPicBase(getBitmapBase64(imageBitmap));
                    }
                    Uri imgUri = data.getData();
                    mImgPath=getPathFromURI(imgUri);
                }else{
                    Uri selectedImage = data.getData();
                    imgPayPic.setImageURI(selectedImage);
                    imgPayPic.isDrawingCacheEnabled();
                    imgPayPic.buildDrawingCache();
                    Bitmap oBitmap=imgPayPic.getDrawingCache();
                    oPay.setPayPicBase(getBitmapBase64(oBitmap));
                    mImgPath=data.getData().getPath();
                }
                oPay.setPayPic(mImgPath);

                Toast.makeText(CustomerPayFormActivity.this ,oPay.getPayPicBase().length(),Toast.LENGTH_LONG).show();
            }catch (Exception ex){}
         }
    }

    private static int RESULT_LOAD_IMAGE = 1;
    //@OnClick(R.id.btnPayPic)
    public void onCapturePayPic(){
        try{
            isCapture=true;
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
               startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
             }
        }catch (Exception ex){}
    }
    //@OnClick(R.id.btnBrownPayPic)
    public  void onBrownPayPic(){
        try{
            isCapture=false;
            Intent IntentBrown = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            IntentBrown.setType("image/*");
            String[] mimeTypes = {"image/jpeg", "image/png"};
            IntentBrown.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
            startActivityForResult(IntentBrown, RESULT_LOAD_IMAGE);
        }catch (Exception ex){}
    }
   */


    /*PHOTO*/
    public Intent getPickImageChooserIntent() {
        try{
            Uri outputFileUri = getCaptureImageOutputUri();
            List<Intent> allIntents = new ArrayList<>();
            PackageManager packageManager = getPackageManager();
            Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            /*LIST CAMERA*/
            try{
                List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
                for (ResolveInfo res : listCam) {
                    Intent intent = new Intent(captureIntent);
                    intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    intent.setPackage(res.activityInfo.packageName);
                    if (outputFileUri != null) {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    }
                    allIntents.add(intent);
                }
            }catch (Exception ex){}

            /*LIST PHOTO*/
            try {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                //String[] mimeTypes = {"image/jpeg", "image/png"};
                //galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
                for (ResolveInfo res : listGallery) {
                    Intent intent = new Intent(galleryIntent);
                    intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    intent.setPackage(res.activityInfo.packageName);
                    allIntents.add(intent);
                }
            }catch (Exception ex){}

            Intent mainIntent = allIntents.get(allIntents.size() - 1);
            /*
            for (Intent intent : allIntents) {
                if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                    mainIntent = intent;
                    break;
                }
            }
            allIntents.remove(mainIntent);
            */
            Intent chooserIntent = Intent.createChooser(mainIntent, "Chọn Hình");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

            return chooserIntent;
        }catch (Exception ex){
            Toast.makeText(CustomerPayFormActivity.this,"Không thể chọn ảnh: "+ ex.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
        return null;
    }

    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }
    private Uri getCaptureImageOutputUri() {
        try {
            Uri outputFileUri = null;
            File getImage = getExternalFilesDir("");
            if (getImage != null) {
                SimpleDateFormat sdfPic = new SimpleDateFormat("ddMMyyhhmmss",Locale.US);
                String mPicFile ="mimi_"+sdfPic.format(new Date())+".png";
                mPicFile="mimiPhoto.png";
                outputFileUri = Uri.fromFile(new File(getImage.getPath(), mPicFile));
            }
            return outputFileUri;
        }catch (Exception ex){return  null;}
    }

    private String getImageFromFilePath(Intent data) {
        try {
            isCamera = data == null || data.getData() == null;
            if (isCamera) {
                return getCaptureImageOutputUri().getPath();
            }
            else return getPathFromURI(data.getData());
        }catch (Exception ex){return  "";}
    }

    private String getPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Audio.Media.DATA};
            Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }catch (Exception ex){return "";}
    }

    /*END PHOTO*/

    @OnClick(R.id.btnSavePay)
    public void onSavePayOnly(){
        if(oPay==null || oPay.getPayID().isEmpty()){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập phiếu thu..", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oPay.getCustomerID()==null || oPay.getCustomerID().isEmpty()) {
            Toast.makeText(this, "Bạn chưa chọn khách hàng..", Toast.LENGTH_SHORT).show();
            //return;
        }
        if (oPay.getPayCode().isEmpty()) {
            Toast.makeText(this, "Không phát sinh được số phiếu..", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!tvPayMoney.getText().toString().isEmpty()) {
            oPay.setPayMoney(Double.valueOf(tvPayMoney.getText().toString()));
        }else{
            oPay.setPayMoney(Double.valueOf(0));
        }
        oPay.setPayNotes(tvPayNotes.getText().toString());
        oPay.setPayDate(tvPayDate.getText().toString());

        if (oPay.getPayMoney().equals(0) || oPay.getPayMoney()<0) {
            Toast.makeText(this, "Bạn chưa nhập số tiền cần thu..", Toast.LENGTH_SHORT).show();
            return;
        }
        if(oPay.getPayDate().isEmpty()){
            Toast.makeText(this, "Bạn chưa nhập ngày thu..", Toast.LENGTH_SHORT).show();
            return;
        }
        oPay.setPayName("AAA");

        mDB.addCustomerPay(oPay);
        Toast.makeText(this, "Ghi phiếu thu thành công", Toast.LENGTH_SHORT).show();
        setResult(2001);
        finish();
        isSaved=true;
    }

    @OnClick(R.id.btnPostPay)
    public void onPostPay(){
        if(oPay==null || oPay.getPayID().isEmpty()){
            Toast.makeText(this, "Không khởi tạo được hoặc chưa nhập phiếu thu..", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oPay.getCustomerID().isEmpty()) {
            Toast.makeText(this, "Bạn chưa chọn khách hàng..", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oPay.getPayCode().isEmpty()) {
            Toast.makeText(this, "Không phát sinh được số phiếu..", Toast.LENGTH_SHORT).show();
            return;
        }
        oPay.setPayMoney(Double.valueOf(tvPayMoney.getText().toString()));
        oPay.setPayNotes(tvPayNotes.getText().toString());
        oPay.setPayDate(tvPayDate.getText().toString());

        if (oPay.getPayMoney().equals(0) || oPay.getPayMoney()<0) {
            Toast.makeText(this, "Bạn chưa nhập số tiền cần thu..", Toast.LENGTH_SHORT).show();
            return;
        }
        if(oPay.getPayDate().isEmpty()){
            Toast.makeText(this, "Bạn chưa nhập ngày thu..", Toast.LENGTH_SHORT).show();
            return;
        }
        oPay.setPayName("AAA");

        mDB.addCustomerPay(oPay);
        if (mDB.getSizePay(oPay.getPayID())>0){
            onRunPostPay(oPay);
        }else{
            Toast.makeText(this, "Không thể ghi phiếu thu..", Toast.LENGTH_SHORT).show();
        }
        setResult(2001);
        isSaved=true;
    }

    private void onRunPostPay(final SM_CustomerPay oP){
        try{
            if (APINet.isNetworkAvailable(CustomerPayFormActivity.this)==false){
                Toast.makeText(CustomerPayFormActivity.this,"Máy chưa kết nối mạng..",Toast.LENGTH_LONG).show();
                return;
            }
            final String Imei=AppUtils.getImeil(this);
            final String ImeiSim=AppUtils.getImeilsim(this);

            if(ImeiSim.isEmpty()){
                Toast.makeText(this,"Không đọc được số IMEI từ thiết bị cho việc đồng bộ. Kiểm tra Sim của bạn",Toast.LENGTH_LONG).show();
                return;
            }
            if(oP==null){
                Toast.makeText(this,"Không tìm thấy dữ liệu phiếu thu.",Toast.LENGTH_LONG).show();
                return;
            }

            final String mUrlPostPay=AppSetting.getInstance().URL_PostPay();
            try {
                if (oP.getPayCode() == null || oP.getPayCode().isEmpty()) {
                    oP.setPayCode("");
                }
                if (oP.getPayName() == null || oP.getPayName().isEmpty()) {
                    oP.setPayName("");
                }
                if (oP.getPayDate() == null || oP.getPayDate().isEmpty()) {
                    oP.setPayDate("");
                }
                if (oP.getPayMoney() == null || oP.getPayMoney()<0) {
                    oP.setPayMoney(Double.valueOf("0"));
                }
                if (oP.getPayNotes() == null || oP.getPayNotes().isEmpty()) {
                    oP.setPayNotes("");
                }
                if (oP.getPayPic() == null || oP.getPayPic().isEmpty()) {
                    oP.setPayPic("");
                }

                if (oP.getLatitude() == null || oP.getLatitude().toString().isEmpty()) {
                    oP.setLatitude(0.0);
                }
                if (oP.getLongitude() == null || oP.getLongitude().toString().isEmpty()) {
                    oP.setLongitude(0.0);
                }
                if (oP.getLocationAddress() == null || oP.getLocationAddress().toString().isEmpty()) {
                    oP.setLocationAddress("N/A");
                }
                if(oP.getPayStatus()==null){
                    oP.setPayStatus(1);
                }
            }catch(Exception ex){
                Toast.makeText(CustomerPayFormActivity.this, "Không tìm thấy dữ liệu đã quét.." + ex.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

        RequestBody DataBody = new FormBody.Builder()
                .add("imei", Imei)
                .add("imeisim", ImeiSim)
                .add("customerid",oP.getCustomerID())
                .add("payid", oP.getPayID())
                .add("paycode", oP.getPayCode())
                .add("payname", oP.getPayName())
                .add("paydate", oP.getPayDate())
                .add("paystatus", Integer.toString(oP.getPayStatus()))
                .add("paymoney", Double.toString( oP.getPayMoney()))
                .add("longitude", Double.toString(oP.getLongitude()))
                .add("latitude", Double.toString(oP.getLatitude()))
                .add("locationaddress", oP.getLocationAddress())
                .add("paynotes", oP.getPayNotes())
                .add("paypic", oP.getPayPic())
                .add("paypicbase", oP.getPayPicBase())

                .build();
            new SyncPost(new APINetCallBack() {
                @Override
                public void onHttpStart() {
                    showProgressDialog("Đang đồng bộ dữ liệu về Server.");
                }
                @Override
                public void onHttpSuccess(String ResPonseRs) {
                    try {
                        dismissProgressDialog();
                        if (ResPonseRs!=null && !ResPonseRs.isEmpty()) {
                            if (ResPonseRs.contains("SYNC_OK")) {
                                Toast.makeText(CustomerPayFormActivity.this, "Đồng  bộ thành công.", Toast.LENGTH_LONG).show();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a",Locale.US);
                                oP.setPostDay(sdf.format(new Date()));
                                oP.setPost(true);
                                oP.setPayStatus(2);
                                mDB.editCustomerPayStatus(oP);
                                finish();
                            }
                            else if(ResPonseRs.contains("SYNC_REG") || ResPonseRs.contains("SYNC_NOT_REG")){
                                Toast.makeText(CustomerPayFormActivity.this, "Thiết bị chưa được đăng ký hoặc chưa xác thực từ Server.", Toast.LENGTH_LONG).show();
                            }else if(ResPonseRs.contains("SYNC_ACTIVE")) {
                                Toast.makeText(CustomerPayFormActivity.this, "Thiết bị chưa kích hoạt...", Toast.LENGTH_LONG).show();
                            }else if(ResPonseRs.contains("SYNC_APPROVE") || ResPonseRs.contains("SYNC_APPROVE")){
                                    Toast.makeText(CustomerPayFormActivity.this, "Đơn hàng đang được xử lý. Bạn không thể gửi điều chỉnh.", Toast.LENGTH_LONG).show();
                            }else if (ResPonseRs.contains("SYNC_BODY_NULL")) {
                                Toast.makeText(CustomerPayFormActivity.this, "Tham số gửi lên BODY=NULL", Toast.LENGTH_LONG).show();
                            } else if (ResPonseRs.contains("SYNC_ORDERID_NULL")) {
                                Toast.makeText(CustomerPayFormActivity.this, "Mã số ORDERID=NULL", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(CustomerPayFormActivity.this  , "Không nhận được trang thải trả về.", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception ex){ }
                    // finish();
                }

                @Override
                public void onHttpFailer(Exception e) {
                    dismissProgressDialog();
                    Toast.makeText(CustomerPayFormActivity.this,"Không thể đồng bộ:"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            },mUrlPostPay,"POST_PAY",DataBody).execute();
        }catch (Exception ex){
            Toast.makeText(CustomerPayFormActivity.this,"Không thể đồng bộ:"+ex.getMessage(),Toast.LENGTH_LONG).show();
            dismissProgressDialog();
        }
    }

}
