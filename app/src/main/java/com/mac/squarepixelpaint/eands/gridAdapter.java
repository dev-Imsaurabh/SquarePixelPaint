package com.mac.squarepixelpaint.eands;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.mac.squarepixelpaint.R;


public class gridAdapter extends BaseAdapter
{
    Context context;
    int boardSize;
    boolean touch;


    public gridAdapter(Context context,int boardSize) {
        this.context=context;
        this.boardSize=boardSize;
    }


    @Override
    public int getCount()
    {
        return boardSize*boardSize;
    }

    @Override
    public Object getItem(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.square_item,null);

        ImageView img=(ImageView)view.findViewById(R.id.iconimage);
        ImageView img1=(ImageView)view.findViewById(R.id.iconimage1);
        ImageView img2=(ImageView)view.findViewById(R.id.iconimage2);

        if(boardSize==8){
            img1.setVisibility(View.GONE);
            img2.setVisibility(View.GONE);
            SetBox(img);

        }else if(boardSize==16){

            img1.setVisibility(View.GONE);
            img2.setVisibility(View.GONE);
            SetBox(img);

        }else if(boardSize==24){
            img.setVisibility(View.GONE);
            img2.setVisibility(View.GONE);
            SetBox(img1);

        }else if(boardSize==32){
            img.setVisibility(View.GONE);
            img1.setVisibility(View.GONE);
            SetBox(img2);
        }else if(boardSize==64){
            img.setVisibility(View.GONE);
            img1.setVisibility(View.GONE);
            SetBox(img2);
        }


        return view;
    }

    private void SetBox(ImageView img) {


        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                SharedPreferences prefs = context.getSharedPreferences("colorPref", MODE_PRIVATE);
                int color = prefs.getInt("color", R.color.white); //0 is the default value.

                SharedPreferences prefs1 = context.getSharedPreferences("ePref", MODE_PRIVATE);
                int erase = prefs1.getInt("erase", 0); //0 is the default value.
                if(erase==0){
                    view.setBackgroundColor(color);

                }else{
                    view.setBackground(null);
                }



                return false;
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                SharedPreferences prefs1 = context.getSharedPreferences("ePref", MODE_PRIVATE);
                int erase = prefs1.getInt("erase", 0); //0 is the default value.
                SharedPreferences prefs = context.getSharedPreferences("colorPref", MODE_PRIVATE);
                int color = prefs.getInt("color", R.color.white); //0 is the default value.

                if(erase==1){

                    img.setBackground(null);
                }else{
                    img.setBackgroundColor(color);

                }


            }
        });


    }


}
