package com.example.treeco;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BaseAdapterMEPast extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    ArrayList<Integer> arEventID;
    ArrayList<String> arEventName, arEventLoc, arEventTime;

    public BaseAdapterMEPast(Context context, ArrayList<Integer> eventid, ArrayList<String> eventname, ArrayList<String> eventlocation, ArrayList<String> eventdatetime) {

        inflater = (LayoutInflater.from(context));
        this.context = context;
        this.arEventID = eventid;
        this.arEventLoc = eventlocation;
        this.arEventTime = eventdatetime;
        this.arEventName = eventname;
    }

    @Override
    public int getCount() {
        return arEventID.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.card_event, null);

        TextView tv = convertView.findViewById(R.id.event_no);
        //tv.setText(event_number[position]);
        tv.setText(arEventName.get(position));
        TextView ELocation = convertView.findViewById(R.id.elocation);
        ELocation.setText(arEventLoc.get(position));

        //Date
        TextView tvDate = convertView.findViewById(R.id.datefield);
        String Datetime = arEventTime.get(position);
        String[] splited = Datetime.split(" ");
        tvDate.setText(splited[0]);

        //Time
        TextView tvTime = convertView.findViewById(R.id.timefield);
        String[] splited2 = splited[1].split("\\+");
        tvTime.setText(splited2[0]);
        return convertView;

    }
}
