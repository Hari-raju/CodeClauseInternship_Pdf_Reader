package com.raju.pdfreader;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.raju.pdfreader.adapter.FileSelectListener;
import com.raju.pdfreader.adapter.PdfAdapter;
import com.raju.pdfreader.databinding.ActivityMainBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FileSelectListener {
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 100;
    private ActivityMainBinding mainBinding;
    private PdfAdapter adapter;
    private List<File> files;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        init();
        checkPermissions();
    }

    private void init(){
        files=new ArrayList<>();
    }

    private void checkPermissions(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            if(Environment.isExternalStorageManager()){
                getPdfs();
            }
            else{
                try{
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s",new Object[]{getApplicationContext().getPackageName()})));
                    reqLauncher.launch(intent);
                }
                catch (Exception e){
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    reqLauncher.launch(intent);
                }
            }
        }
        else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                getPdfs();
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            }
        }
    }

    ActivityResultLauncher<Intent> reqLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if(o.getResultCode()== Activity.RESULT_OK){
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
                    if (Environment.isExternalStorageManager()){
                        getPdfs();
                    }
                    else{
                        //Add a Alert Box
                    }
                }
            }
        }
    });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
              getPdfs();
            }
            else{
                //Add a Alert Box
            }
        }
    }

    private void getPdfs(){
        Search_Dir(Environment.getExternalStorageDirectory());
        PdfAdapter adapter = new PdfAdapter(MainActivity.this,files,this);
        if(adapter.getItemCount()>0){
            mainBinding.recyclerView.setAdapter(adapter);
            mainBinding.recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void Search_Dir(File dir) {
        String pdfPattern = ".pdf";
        //If its a directory first we are gonna store all the files in a array(including dir also)
        File FileList[] = dir.listFiles();
        if (FileList != null) {
            for (int i = 0; i < FileList.length; i++) {
                if (FileList[i].isDirectory()) {
                    //If its inside a directory we will recursively search inside it for a pdf file
                    Search_Dir(FileList[i]);
                } else {
                    if (FileList[i].getName().endsWith(pdfPattern)){
                        //Here we will have that pdf file.
                        files.add(FileList[i]);
                    }
                }
            }
        }
    }

    @Override
    public void onPdfSelected(File file) {
        Intent intent = new Intent(MainActivity.this, PdfActivity.class);
        intent.putExtra("path",file.getAbsolutePath());
        startActivity(intent);
    }
}