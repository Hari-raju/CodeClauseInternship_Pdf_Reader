package com.raju.pdfreader;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.raju.pdfreader.databinding.ActivityPdfBinding;

import java.io.File;
import java.util.Objects;

public class PdfActivity extends AppCompatActivity {
    private ActivityPdfBinding pdfBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pdfBinding = ActivityPdfBinding.inflate(getLayoutInflater());
        setContentView(pdfBinding.getRoot());
        init();
    }

    private void init() {
        String path = getIntent().getStringExtra("path");
        File file = new File(Objects.requireNonNull(path));
        Uri uri = Uri.fromFile(file);
        pdfBinding.pdfView.fromUri(uri).load();
    }
}