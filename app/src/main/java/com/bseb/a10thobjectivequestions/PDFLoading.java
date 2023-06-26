package com.bseb.a10thobjectivequestions;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public class PDFLoading extends Dialog {
    public PDFLoading(@NonNull Context context) {
        super(context);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        setTitle(null);
        setCancelable(true);
        setOnCancelListener(null);

        View view = LayoutInflater.from(context).inflate(R.layout.pdfloading,null);
        setContentView(view);


    }
}
