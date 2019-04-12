package com.mau.dalvi.locationserver;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends Fragment {


    private TextView tv1;
    private Button btnViewGroups, btnMap, btnFindMe;
    private Controller controller;

    public HomePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        initComponents(view);
        return view;
    }

    public void setController(Controller controller){
        this.controller = controller;
    }


    private void initComponents(View view) {
        tv1 = view.findViewById(R.id.tv1);
        btnViewGroups = view.findViewById(R.id.btnStart);
        btnFindMe = view.findViewById(R.id.btnFindMe);
        btnMap = view.findViewById(R.id.btnMap);

        ButtonListener listener = new ButtonListener();
        btnMap.setOnClickListener(listener);
        btnFindMe.setOnClickListener(listener);
        btnViewGroups.setOnClickListener(listener);

    }

    public void setText(double latitude, double longitude){
        tv1.setText("You're at: " + latitude +", " + longitude);

    }

    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v == btnMap){
                controller.btnMapClicked();
            } else if (v == btnFindMe){
                controller.btnFindMeClicked();
            } else if (v == btnViewGroups) {
                try {
                    controller.btnViewGroupsClicked();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}


