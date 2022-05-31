package com.mac.squarepixelpaint;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShareActivity extends AppCompatActivity {
    private ImageView share_iv;
    private Uri imageUri;
    private Button share_btn,rate_us;
    private  Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        imageUri=Uri.parse(getIntent().getStringExtra("uri"));
        share_iv=findViewById(R.id.share_iv);
        share_btn=findViewById(R.id.share_btn);
        rate_us=findViewById(R.id.rate_us_btn);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        share_iv.setImageBitmap(bitmap);

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable=share_iv.getDrawable();
                Bitmap finalImage=((BitmapDrawable)drawable).getBitmap();

                try {
                    File file = new File(getApplicationContext().getExternalCacheDir(), File.separator +"change_back1.png");
                    FileOutputStream fOut = new FileOutputStream(file);

                    finalImage.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);
                    final Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID +".provider", file);
                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("image/*");

                    startActivity(Intent.createChooser(intent, "Share image via"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ShareActivity.this, "file not found", Toast.LENGTH_SHORT).show();
                }

            }
        });

        rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });



    }

}