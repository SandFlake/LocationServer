package com.mau.dalvi.locationserver;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewGroupFragment extends Fragment {

    private TextView tvTitle;
    private EditText etGroupName;
    private Button btnRegister, btnBack;
    private Controller controller;


    public NewGroupFragment() {
        // Required empty public constructor
    }


    public void setController(Controller controller){this.controller = controller;}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_group, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        tvTitle = view.findViewById(R.id.tvTitle);
        etGroupName = view.findViewById(R.id.etGroupName);
        btnRegister = view.findViewById(R.id.btnSaveGroup);
        btnBack = view.findViewById(R.id.btnBack);

        ButtonListener listener = new ButtonListener();
        btnBack.setOnClickListener(listener);
        btnRegister.setOnClickListener(listener);
    }

    private class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (v == btnRegister){
                if(etGroupName.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please enter a group name", Toast.LENGTH_SHORT).show();
                } else if (! etGroupName.getText().toString().isEmpty()) {
                    String groupName = etGroupName.getText().toString();
                    controller.btnRegisterGroupClicked(groupName);

                    Toast.makeText(getContext(), "" + groupName + " registered to server", Toast.LENGTH_SHORT).show();
                }

            } else if(v == btnBack){
                controller.btnBackClicked();
            }

        }
    }

}
