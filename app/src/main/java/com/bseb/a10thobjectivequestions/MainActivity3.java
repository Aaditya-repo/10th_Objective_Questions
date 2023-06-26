package com.bseb.a10thobjectivequestions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;

import java.io.File;

public class MainActivity3 extends AppCompatActivity {


    private PDFView pdfView;
    private String url;

    public MainActivity3() {
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    TextView textView;
    ImageView imageView;

    PDFLoading loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        pdfView = findViewById(R.id.pdf);
        url = getIntent().getStringExtra("links");
        imageView = findViewById(R.id.back_btn1);
        textView = findViewById(R.id.setTitle1);
        textView.setText(getIntent().getStringExtra("title"));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSupportNavigateUp();
            }
        });
        loading = new PDFLoading(this);
        loading.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loading.show();
        loadFile(url);

    }


    private void loadFile(String url) {
        FileLoader.with(this)
                .load(url)
                .fromDirectory("beta", FileLoader.DIR_EXTERNAL_PRIVATE)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {

                        File loadedFile = response.getBody();
                       loading.dismiss();
                        pdfView.fromFile(loadedFile)
                                .password(null)
                                .defaultPage(0)
                                .enableSwipe(true)
                                .swipeHorizontal(false)
                                .enableDoubletap(true)
                                .spacing(2)
                                .scrollHandle(new DefaultScrollHandle(getApplicationContext()))
                                .load();

                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        Toast.makeText(MainActivity3.this, "Error"+t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }



}