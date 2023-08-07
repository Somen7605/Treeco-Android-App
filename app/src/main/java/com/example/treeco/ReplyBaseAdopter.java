package com.example.treeco;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReplyBaseAdopter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    ArrayList<String> ArrayReplyName, ArrayReplyComment, ArrayReplyCommentedOn;
    int Arrayval;

    //TextView replierName,ReplierComment,replierCommentedOn;
    public ReplyBaseAdopter(Context c, ArrayList<String> RName, ArrayList<String> Rcomment, ArrayList<String> Rcommentedon) {
        inflater = (LayoutInflater.from(c));
        this.context = c;
        this.ArrayReplyName = RName;
        this.ArrayReplyComment = Rcomment;
        this.ArrayReplyCommentedOn = Rcommentedon;
        //Arrayval=JsonArrayParse(ArrReply);

    }

    @Override
    public int getCount() {
        return ArrayReplyName.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Toast.makeText(context, "" + ArrayReplyName.size(), Toast.LENGTH_LONG).show();
        convertView = inflater.inflate(R.layout.reply_discussion, null);
        if (ArrayReplyName.size() > 0) {
            TextView tvname = convertView.findViewById(R.id.namedisplayr);
            tvname.setText(ArrayReplyName.get(position));
            TextView tvcomment = convertView.findViewById(R.id.commentdisplayr);
            tvcomment.setText(ArrayReplyComment.get(position));
            TextView tvcommenton = convertView.findViewById(R.id.commentedonr);
            String[] splitedtimeval = ArrayReplyCommentedOn.get(position).split("\\+");
            tvcommenton.setText(splitedtimeval[0]);
        }
        return convertView;
    }

}
