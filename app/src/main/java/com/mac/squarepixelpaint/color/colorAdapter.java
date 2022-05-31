package com.mac.squarepixelpaint.color;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mac.squarepixelpaint.R;

import java.util.List;

public class colorAdapter extends RecyclerView.Adapter<colorAdapter.colorViewAdapter> {
    private Context context;
    private List<colorModel> list;

    public colorAdapter(Context context, List<colorModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public colorViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_color_palette,parent,false);

        return new colorViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull colorViewAdapter holder, int position) {

        colorModel colorModel = list.get(position);

        holder.color_palette.setCardBackgroundColor(colorModel.getColor());
        holder.color_palette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = context.getSharedPreferences("colorPref", MODE_PRIVATE).edit();
                editor.putInt("color",colorModel.getColor());
                editor.apply();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class colorViewAdapter extends RecyclerView.ViewHolder {
        private CardView color_palette;

        public colorViewAdapter(@NonNull View itemView) {
            super(itemView);
            color_palette=itemView.findViewById(R.id.color_palette);
        }
    }
}
