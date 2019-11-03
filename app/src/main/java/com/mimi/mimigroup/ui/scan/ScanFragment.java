package com.mimi.mimigroup.ui.scan;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;

import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.mimi.mimigroup.R;
import com.mimi.mimigroup.app.AppSetting;
import com.mimi.mimigroup.base.BaseFragment;
import com.mimi.mimigroup.ui.help.HelpActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

import butterknife.OnClick;
import info.androidhive.barcode.BarcodeReader;

import static android.app.Activity.RESULT_OK;

public class ScanFragment extends BaseFragment implements BarcodeReader.BarcodeReaderListener {
    private BarcodeReader barcodeReader;
    public CameraSource mCameraSource;
    private Camera mCamera = null;
    boolean flash = false;

    @Override
    protected int getLayoutResourceId() {
        if(AppSetting.getInstance().isAndroidVersion5()) {
            return R.layout.fragment_scan;
        }else{
            return R.layout.fragment_scan_21;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ADD 19/01/2019
        /*
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {android.Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 50);
        }*/

    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeReader = (BarcodeReader) getChildFragmentManager().findFragmentById(R.id.barcode_fragment);
        barcodeReader.setListener(this);
        barcodeReader.setBeepSoundFile("android.resource://com.npc.grc/raw/tick.wav");
        //barcodeReader.setBeepSoundFile(getResources().getResourcePackageName(R.raw.beep));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @OnClick(R.id.llLight)
    public void onLight(){
        flash=!flash;
        //barcodeReader.getmCameraSource().setFlashMode(flash ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF);
        //AddNew MT
        try {

            mCamera=getCamera(mCameraSource);
            if (mCamera!=null){
                Camera.Parameters param = mCamera.getParameters();
                param.setFlashMode(!flash?Camera.Parameters.FLASH_MODE_TORCH :Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(param);
                flash = !flash;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"Không thể bật đèn khi quét "+e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.llPicture)
    public void onPicture(){
        try {
            Intent photoPic = new Intent(Intent.ACTION_PICK);
            photoPic.setType("image/*");
            startActivityForResult(photoPic, 100);
        }catch (Exception ex){
            Toast.makeText(getContext(),"Không thể quét từ Hình Ảnh: "+ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.llHelp)
    public void onHelp(){
        startActivity(new Intent(getContext(), HelpActivity.class));
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.btnEnterCode)
    public void onEnterCodeClicked(){
        Intent intent = new Intent(getContext(), ScanResultActivity.class);
        intent.setAction("ENTER_CODE");
        intent.putExtra("DATA_SCAN_RESULT", "ENTER_CODE");
        startActivity(intent);
    }


    @Override
    public void onScanned(Barcode barcode) {
        try {
            if (AppSetting.getInstance().getSound()) {
                barcodeReader.playBeep();
            }
            if (AppSetting.getInstance().getVibra()) {
                Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(400);
            }
        }catch (Exception ex){}

        if(!barcode.displayValue.isEmpty()) {
            String DATA_SCAN= new String(Base64.decode(barcode.displayValue, 1));
            Intent intent = new Intent(getContext(), ScanResultActivity.class);
            intent.putExtra("DATA_SCAN_RESULT", DATA_SCAN);
            startActivity(intent);
        }else{
            Toast.makeText(getContext(),"Không đọc được dữ liệu QR",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getContext(), "Không có quyền camera", Toast.LENGTH_LONG).show();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    //doing some uri parsing
                    Uri selectedImage = data.getData();
                    InputStream imageStream = null;
                    try {
                        //getting the image
                        imageStream = getContext().getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        Toast.makeText(getContext(), "File not found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    //decoding bitmap
                    Bitmap bMap = BitmapFactory.decodeStream(imageStream);
                    Frame frame = new Frame.Builder().setBitmap(bMap).build();

                    //MT:Addnew QR_CODE
                    BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getContext())
                            .setBarcodeFormats(Barcode.QR_CODE)
                            .build();

                    //MT ADD CAMERA BUILDER
                    CameraSource.Builder mBuilder=new CameraSource.Builder(getContext(),barcodeDetector)
                            .setAutoFocusEnabled(true)
                            .setRequestedFps(35.f)
                            .setRequestedPreviewSize(1600,1024);

                   //MT ADD DEMO
                   mCameraSource= new CameraSource.Builder(getContext(),barcodeDetector)
                            .setAutoFocusEnabled(true)
                            .setRequestedFps(35.0f)
                            .setRequestedPreviewSize(1600,1024)
                            .build();



                    SparseArray<Barcode> sparseArray = barcodeDetector.detect(frame);
                    if (sparseArray != null && sparseArray.size() > 0){
                        String DATA_SCAN = new String(Base64.decode(sparseArray.valueAt(0).displayValue, 1));
                        if (AppSetting.getInstance().getSound()){
                            barcodeReader.playBeep();
                        }
                        if (AppSetting.getInstance().getVibra()){
                            Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(400);
                        }
                        Intent intent = new Intent(getContext(), ScanResultActivity.class);
                        intent.putExtra("DATA_SCAN_RESULT", DATA_SCAN);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getContext(), "Mã QR không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    //MT-ADDNEW
    public static Camera getCamera(@NonNull CameraSource cameraSource) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        return camera;
                    }
                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        return null;
    }

}
