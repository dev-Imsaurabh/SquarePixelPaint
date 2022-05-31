package com.mac.squarepixelpaint.eands;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mac.squarepixelpaint.R;
import com.mac.squarepixelpaint.ShareActivity;
import com.mac.squarepixelpaint.color.colorAdapter;
import com.mac.squarepixelpaint.color.colorModel;


import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {
    GridView grid;
    GridView dummy_grid;
    GridView grid2;
    GridView grid3;
    gridAdapter obj;
    frame_grip_adapter obj2;
    Bitmap bitmap;
    private View view1, view2;
    private boolean gridShowing = true;
    private ImageView iv;
    private colorAdapter colorAdapter;
    private ArrayList<colorModel> list;
    private Button choose_color, remove_grid, saveImage;
    private final int initialColor = R.color.white;
    private Button eraser_btn;
    private RecyclerView rec_color;
    private BitmapDrawable bitmapDrawable;
    private Handler handler;
    private Runnable runnable;
    private int boardSize;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();

        boardSize = Integer.parseInt(getIntent().getStringExtra("boardSize"));
        SetPref();

        list = new ArrayList<>();
        grid = (GridView) findViewById(R.id.datagrid);
        dummy_grid = (GridView) findViewById(R.id.dummy_grid);
        saveImage = (Button) findViewById(R.id.saveImage);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        eraser_btn = findViewById(R.id.erase_btn);
        rec_color = findViewById(R.id.rec_color);
        rec_color.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rec_color.setHasFixedSize(true);
        SharedPreferences prefs = getSharedPreferences("colorPref", MODE_PRIVATE);
        int color = prefs.getInt("color", R.color.white); //0 is the default value.
        colorModel colorModel = new colorModel(color);
        list.add(colorModel);
        colorAdapter = new colorAdapter(this, list);
        colorAdapter.notifyDataSetChanged();
        rec_color.setAdapter(colorAdapter);

        final ZoomLinearLayout zoomLinearLayout = (ZoomLinearLayout) findViewById(com.otaliastudios.zoom.R.id.zoom);
        zoomLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                zoomLinearLayout.init(MainActivity.this);
                return false;
            }
        });
//
//
//        grid.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
//            public void onSwipeTop() {
//                grid.callOnClick();
//            }
//            public void onSwipeRight() {
//                grid.callOnClick();
//            }
//            public void onSwipeLeft() {
//                grid.callOnClick();
//            }
//            public void onSwipeBottom() {
//                grid.callOnClick();
//            }
//
//        });

        //        RequestColoumWidth();

        SetupBoard(boardSize);
        choose_color = findViewById(R.id.choose_color);
        remove_grid = findViewById(R.id.remove_grid);
        iv = findViewById(R.id.iv);

        UpdatePreview();
        choose_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(MainActivity.this, initialColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {


                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        SharedPreferences.Editor editor = getSharedPreferences("colorPref", MODE_PRIVATE).edit();
                        editor.putInt("color", color);
                        editor.apply();
                        com.mac.squarepixelpaint.color.colorModel colorModel1 = new colorModel(color);
                        list.add(colorModel1);
                        colorAdapter.notifyDataSetChanged();


                    }
                });
                dialog.show();
            }

        });
        obj = new gridAdapter(this, boardSize);
        obj2 = new frame_grip_adapter(this, boardSize);
        grid.setAdapter(obj);
        dummy_grid.setAdapter(obj2);

        remove_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (gridShowing) {
                        dummy_grid.setVisibility(View.GONE);
                        view1.setVisibility(View.GONE);
                        view2.setVisibility(View.GONE);
                        gridShowing = false;
                        remove_grid.setText("Show Grid");
                    } else {
                        dummy_grid.setVisibility(View.VISIBLE);
                        view1.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.VISIBLE);
                        gridShowing = true;
                        remove_grid.setText("Hide Grid");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        eraser_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("ePref", MODE_PRIVATE);
                int erase = prefs.getInt("erase", 0); //0 is the default value.
                if (erase == 0) {
                    SharedPreferences.Editor editor = getSharedPreferences("ePref", MODE_PRIVATE).edit();
                    editor.putInt("erase", 1);
                    editor.apply();
                    eraser_btn.setText("Eraser OFF");
                    eraser_btn.setBackground(getDrawable(R.drawable.button_border1));

                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("ePref", MODE_PRIVATE).edit();
                    editor.putInt("erase", 0);
                    editor.apply();
                    eraser_btn.setText("Eraser ON");
                    eraser_btn.setBackground(getDrawable(R.drawable.button_border));


                }


            }
        });
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Show();
            }
        });

    }

    private void SetupBoard(int boardSize) {

        switch (boardSize) {
            case 8:
                grid.setNumColumns(8);
                dummy_grid.setNumColumns(8);
                grid.setHorizontalSpacing(-600);
                dummy_grid.setHorizontalSpacing(-600);
                break;

            case 16:
                grid.setNumColumns(16);
                dummy_grid.setNumColumns(16);
                grid.setHorizontalSpacing(-120);
                dummy_grid.setHorizontalSpacing(-120);
                break;

            case 24:
                grid.setNumColumns(24);
                dummy_grid.setNumColumns(24);
                grid.setHorizontalSpacing(0);
                dummy_grid.setHorizontalSpacing(0);
                break;

            case 32:
                grid.setNumColumns(32);
                dummy_grid.setNumColumns(32);
                grid.setHorizontalSpacing(0);
                dummy_grid.setHorizontalSpacing(0);
                break;

            //just 32
        }


    }


    private void UpdatePreview() {
        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, 200);
                grid.setDrawingCacheEnabled(true);
                grid.buildDrawingCache(true);

                bitmap = Bitmap.createBitmap(grid.getDrawingCache());
                Canvas canvas = new Canvas();
                grid.draw(canvas);
                grid.setDrawingCacheEnabled(false);
                iv.setImageBitmap(bitmap);
            }
        }, 200);
    }

    private void SetPref() {

        SharedPreferences.Editor editor1 = getSharedPreferences("ePref", MODE_PRIVATE).edit();
        editor1.putInt("erase", 0);
        editor1.apply();

    }

    private void Show() {
        grid.setDrawingCacheEnabled(true);
        grid.buildDrawingCache(true);

        bitmap = Bitmap.createBitmap(grid.getDrawingCache());
        Canvas canvas = new Canvas();
        grid.draw(canvas);
        grid.setDrawingCacheEnabled(false);
        iv.setImageBitmap(bitmap);
        saveImage();
    }


    private void RequestColoumWidth() {

        int myRequestedColumnWidth = grid.getRequestedColumnWidth();

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = size.y;

        int myGridColumns = width / myRequestedColumnWidth;

        if (myGridColumns == 0 || myGridColumns == 1)
            myGridColumns = 2;
        int myColumnWidth = (width / myGridColumns);

        grid.setNumColumns(myGridColumns);


    }

    private void saveImage() {
        bitmapDrawable = (BitmapDrawable) iv.getDrawable();
        bitmap = bitmapDrawable.getBitmap();
        saveImageToGallery(bitmap);
    }


    private void saveImageToGallery(Bitmap bitmap4) {

        OutputStream fos;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = getContentResolver();
                ContentValues contentValues = new ContentValues();
                String fileName = "Image_" + System.currentTimeMillis() + ".jpg";
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "images/jpg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + File.separator + "Square Pixel Paint");
                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
                bitmap4.compress(Bitmap.CompressFormat.PNG, 100, fos);
                Objects.requireNonNull(fos);

                StartShareActivity(imageUri);


                Toast.makeText(MainActivity.this, "saved in > Internal storage/DCIM/Square Pixel Paint", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {

            Toast.makeText(MainActivity.this, "Error\n" + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    private void StartShareActivity(Uri imageUri) {
        Intent intent = new Intent(MainActivity.this, ShareActivity.class);
        intent.putExtra("uri", imageUri.toString());
        startActivity(intent);

    }

    private void requestPermissions() {
        // below line is use to request
        // permission in the current activity.
        Dexter.withActivity(this)
                // below line is use to request the number of
                // permissions which are required in our app.
                .withPermissions(Manifest.permission.CAMERA,
                        // below is the list of permissions
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                // after adding permissions we are
                // calling an with listener method.
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        // this method is called when all permissions are granted
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            // do you work now
                            Toast.makeText(MainActivity.this, "All the permissions are granted..", Toast.LENGTH_SHORT).show();
                        }
                        // check for permanent denial of any permission
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permanently,
                            // we will show user a dialog message.
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        // this method is called when user grants some
                        // permission and denies some of them.
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            // this method is use to handle error
            // in runtime permissions
            @Override
            public void onError(DexterError error) {
                // we are displaying a toast message for error message.
                Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
            }
        })
                // below line is use to run the permissions
                // on same thread and to check the permissions
                .onSameThread().check();
    }
}