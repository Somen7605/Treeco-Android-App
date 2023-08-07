package com.example.treeco;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.File;


public class ActivityFragment extends Fragment implements View.OnClickListener {
    Button btntag, btnevent, btnmaintainance, btnPlantatree;

    public ActivityFragment() {        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_activity, container, false);
        btntag = v.findViewById(R.id.base_ek104);
        btnevent = v.findViewById(R.id.base_ek101);
        btnmaintainance = v.findViewById(R.id.base_ek107);
        btnPlantatree = v.findViewById(R.id.base_ek1011);
        btnPlantatree.setOnClickListener(this);
        btnmaintainance = v.findViewById(R.id.base_ek107);
        btntag.setOnClickListener(this);
        btnevent.setOnClickListener(this);
        btnmaintainance.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.base_ek104) {
            //to clear the image store directory
            File fdelete = new File(getActivity().getExternalFilesDir(null).getAbsolutePath() + "/TempDirfortreeco/");
            if (fdelete.isDirectory()) {
                String[] children = fdelete.list();
                for (int i = 0; i < children.length; i++) {
                    new File(fdelete, children[i]).delete();
                }
                //Toast.makeText(getContext(),"Inside tag"+children.length,Toast.LENGTH_LONG).show();
            }

            Intent itag = new Intent(getContext(), TagActivity1.class);
            startActivity(itag);

        }
        if (v.getId() == R.id.base_ek1011) {
            // Toast.makeText(getContext(),"Inside plant a tree",Toast.LENGTH_LONG).show();
            //to clear the image store directory
            File fdelete = new File(getActivity().getExternalFilesDir(null).getAbsolutePath() + "/TempDirfortreeco/");
            if (fdelete.isDirectory()) {
                String[] children = fdelete.list();
                for (int i = 0; i < children.length; i++) {
                    new File(fdelete, children[i]).delete();
                }
                //Toast.makeText(getContext(),"Inside tag"+children.length,Toast.LENGTH_LONG).show();
            }

            Intent iplant = new Intent(getContext(), PlantActivity1.class);
            startActivity(iplant);

        }
        if (v.getId() == R.id.base_ek101) {
            Intent ievent = new Intent(getContext(), EventDetails.class);
            startActivity(ievent);

        }


        if (v.getId() == R.id.base_ek107) {
            Toast.makeText(getContext(), "Maintainance Feature not implemented yet", Toast.LENGTH_LONG).show();
        }


    }
}