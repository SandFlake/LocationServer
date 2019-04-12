package com.mau.dalvi.locationserver;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.JsonWriter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsOnServerFragment extends Fragment {

    private Controller controller;
    private TextView tvGroupInfo, tvStatus;
    private ListView lvGroupInfo;
    private Button btnBack, btnAddNewGroup, btnMapTime;
    private String username;


    public GroupsOnServerFragment() {
        // Required empty public constructor
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_groups_on_server, container, false);
        initComponents(view);
        controller.getAllGroups();
        username = controller.getUsername();
        return view;
    }

    public void onResume(){
        super.onResume();
        controller.getAllGroups();
    }

    public void initComponents(View view)  {
        tvGroupInfo = view.findViewById(R.id.tvGroupTitle);
        lvGroupInfo = view.findViewById(R.id.lvGroupInfo);
        btnBack = view.findViewById(R.id.btnBack2);
        btnAddNewGroup = view.findViewById(R.id.btnAddNewGroup);
        btnMapTime = view.findViewById(R.id.btnMap);
        tvStatus = view.findViewById(R.id.tvStatus);

        ButtonListener listener = new ButtonListener();
        btnAddNewGroup.setOnClickListener(listener);
        btnBack.setOnClickListener(listener);
        btnMapTime.setOnClickListener(listener);

    }

    public void runOnUIThread(Runnable runnable){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }

    public void arrayAdapter(ArrayList<String> existingGroups){
        ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.list_view, R.id.tvGroups, existingGroups);
                lvGroupInfo.setAdapter(groupAdapter);
                lvGroupInfo.setOnItemClickListener(new ListListener());
    }

    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v == btnAddNewGroup){
                controller.btnAddGroup();
            } else if (v == btnBack){
                controller.btnBackClicked();
            } else if (v == btnMapTime){
                controller.btnGroupMapClicked();
            }
        }
    }


    private class ListListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String groupName = parent.getItemAtPosition(position).toString();

                controller.groupJoinedClicked(groupName, username);
                controller.getGroupInfo(groupName);
        }
    }

  public void setTvStatus(String message){
        tvStatus.setText(message);

  }

    public void showMessage(String message){
        tvStatus.setText(message);
        Log.d(TAG, "showMessage: somehow" + message);


    }



}
