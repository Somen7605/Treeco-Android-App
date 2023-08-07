package com.example.treeco;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BaseAdapterPlannedConfirmed extends BaseAdapter {

    public static String[] event_number = {"Event 3"};
    LayoutInflater inflater;
    Context context;


    public BaseAdapterPlannedConfirmed(Context context) {

        inflater = (LayoutInflater.from(context));
        this.context = context;
    }

    @Override
    public int getCount() {
        return event_number.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.cardevent2, null);

        TextView tv = convertView.findViewById(R.id.event_no);
        tv.setText(event_number[position]);
        return convertView;
    }
}
