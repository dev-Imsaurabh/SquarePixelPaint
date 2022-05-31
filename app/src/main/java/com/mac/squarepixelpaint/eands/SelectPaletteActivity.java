package com.mac.squarepixelpaint.eands;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mac.squarepixelpaint.R;


public class SelectPaletteActivity extends AppCompatActivity implements View.OnClickListener {
    private Button board_8, board_16, board_24, board_32, board_64, board_96, board_160, board_192;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_palette);
        board_8=findViewById(R.id.board_8);
        board_16=findViewById(R.id.board_16);
        board_24=findViewById(R.id.board_24);
        board_32=findViewById(R.id.board_32);
        board_64=findViewById(R.id.board_64);
        board_96=findViewById(R.id.board_96);
        board_160=findViewById(R.id.board_160);
        board_192=findViewById(R.id.board_192);
        dialog=new ProgressDialog(this);

        board_8.setOnClickListener(this);
        board_16.setOnClickListener(this);
        board_24.setOnClickListener(this);
        board_32.setOnClickListener(this);
        board_64.setOnClickListener(this);
        board_96.setOnClickListener(this);
        board_160.setOnClickListener(this);
        board_192.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        dialog.setMessage("Preparing Board...");
        dialog.setCancelable(true);
        dialog.show();
        switch (view.getId()){
            case R.id.board_8:
                StartMainActivity(8);
                break;
            case R.id.board_16:
                StartMainActivity(16);
                break;
            case R.id.board_24:
                StartMainActivity(24);
                break;
            case R.id.board_32:
                StartMainActivity(32);
                break;
            case R.id.board_64:
                StartMainActivity(64);
                break;
            case R.id.board_96:
                StartMainActivity(96);
                break;
            case R.id.board_160:
                StartMainActivity(160);
                break;
            case R.id.board_192:
                StartMainActivity(192);
                break;



        }
    }

    private void StartMainActivity(int boardSize) {
        Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        },1000);
        Intent intent = new Intent(SelectPaletteActivity.this,MainActivity.class);
        intent.putExtra("boardSize",String.valueOf(boardSize));
        startActivity(intent);

    }
}