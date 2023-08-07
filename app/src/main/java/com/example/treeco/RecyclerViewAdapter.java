package com.example.treeco;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Bitmap> arrimg;

    public RecyclerViewAdapter(Context context, ArrayList<Bitmap> imagegallery) {
        mContext = context;
        this.arrimg = imagegallery;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.imageView.setImageBitmap(arrimg.get(position));
        holder.imageView.setLayoutParams(new RecyclerView.LayoutParams(200, 200));
        holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the clicked image and set it as the background image
                ImageView backgroundImage = ((Activity) v.getContext()).findViewById(R.id.background_image);
                backgroundImage.setImageBitmap(arrimg.get(position));
                backgroundImage.setVisibility(View.VISIBLE);
                LinearLayout linearLayout=((Activity) v.getContext()).findViewById(R.id.linearLayout);
                linearLayout.setVisibility(View.VISIBLE);
                FloatingActionButton floatingActionButton=((Activity) v.getContext()).findViewById(R.id.captureBtn);
                floatingActionButton.setVisibility(View.GONE);
                RecyclerView recyclerView=((Activity) v.getContext()).findViewById(R.id.recyclerView);
                recyclerView.setVisibility(View.GONE);
                ExtendedFloatingActionButton doneBtn=((Activity) v.getContext()).findViewById(R.id.doneButton2);
                doneBtn.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrimg.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }

    }

}
