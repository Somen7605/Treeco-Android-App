package com.example.treeco;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MyEventFragment extends Fragment {

    Button planned, past;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_event, container, false);
        planned = v.findViewById(R.id.planned);
        planned.setBackgroundColor(getResources().getColor(R.color.trees_near_by_color));
        past = v.findViewById(R.id.past);
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PlannedFragment fragment1 = new PlannedFragment();
        fragmentTransaction.replace(R.id.fragment_my_event, fragment1);
        fragmentTransaction.commit();

        planned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                planned.setBackgroundColor(getResources().getColor(R.color.trees_near_by_color));
                past.setBackgroundColor(getResources().getColor(R.color.white));

                try {
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    PlannedFragment fragment1 = new PlannedFragment();
                    fragmentTransaction.replace(R.id.fragment_my_event, fragment1);
                    fragmentTransaction.commit();
                } catch (Exception e) {
                    //Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        });
        past.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                past.setBackgroundColor(getResources().getColor(R.color.trees_near_by_color));
                planned.setBackgroundColor(getResources().getColor(R.color.white));

                try {
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    PastFragment fragment1 = new PastFragment();
                    fragmentTransaction.replace(R.id.fragment_my_event, fragment1);
                    fragmentTransaction.commit();
                } catch (Exception e) {
                    //Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


        // Inflate the layout for this fragment
        return v;
    }
}