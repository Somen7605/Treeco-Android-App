package com.example.treeco;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class BaseAdapterUEThisWeek extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    CardView card_event;
    ArrayList<Integer> arEventID;
    ArrayList<String> arEventName, arEventLoc, arEventTime;

    public BaseAdapterUEThisWeek(Context context, ArrayList<Integer> eventid, ArrayList<String> eventname, ArrayList<String> eventlocation, ArrayList<String> eventdatetime) {

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
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        convertview = inflater.inflate(R.layout.card_event, null);
        TextView tvName = convertview.findViewById(R.id.event_no);
        TextView tvInstatus = convertview.findViewById(R.id.taxtin);
        ImageView imgStatus = convertview.findViewById(R.id.imgshowin);
        if (UpcomingEventFragment.Event_id_list.contains(arEventID.get(position))) {
            tvInstatus.setVisibility(View.VISIBLE);
            imgStatus.setVisibility(View.VISIBLE);
        } else {
            tvInstatus.setVisibility(View.GONE);
            imgStatus.setVisibility(View.GONE);
        }
        tvName.setText(arEventName.get(position));
        TextView tvDate = convertview.findViewById(R.id.datefield);
        String Datetimeval = arEventTime.get(position);
        String[] splited = Datetimeval.split("\\s+");
        tvDate.setText(splited[0]);
        TextView tvTime = convertview.findViewById(R.id.timefield);
        String[] splitedtimeval = splited[1].split("\\+");
        tvTime.setText(splitedtimeval[0]);
        TextView ELocation = convertview.findViewById(R.id.elocation);
        ELocation.setText(arEventLoc.get(position));
        card_event = convertview.findViewById(R.id.event_card);
        card_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context.getApplicationContext(), "Card is Clicked",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, EachEventDetailsShow.class);
                intent.putExtra("position", position);
                intent.putIntegerArrayListExtra("EventIDArray", arEventID);
                context.startActivity(intent);
            }
        });

        return convertview;
    }
}
