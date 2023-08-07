package com.example.treeco;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.util.ArrayList;

public class GalleryImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Bitmap> arrimg;


    public GalleryImageAdapter(Context context, ArrayList<Bitmap> imagegallery) {
        mContext = context;
        this.arrimg = imagegallery;
    }

    public int getCount() {
        return arrimg.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    // Override this method according to your need
    public View getView(int index, View view, ViewGroup viewGroup) {
        // TODO Auto-generated method stub
        ImageView i = new ImageView(mContext);

        i.setImageBitmap(arrimg.get(index));
        i.setLayoutParams(new Gallery.LayoutParams(200, 200));

        i.setScaleType(ImageView.ScaleType.FIT_XY);


        return i;
    }


}

