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

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private Button btnSignIn;
    private TextView tvWelcome;
    private EditText etName;
    private Controller controller;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initComponents(view);
        return view;
    }

    public void setController(Controller controller){this.controller = controller;}

    private void initComponents(View view) {
        tvWelcome = view.findViewById(R.id.tvWelcome);
        etName = view.findViewById(R.id.etName);
        btnSignIn = view.findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etName.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "You need a username to start",Toast.LENGTH_SHORT).show();
                } else if(!etName.getText().toString().isEmpty()){
                    controller.setUsername(etName.getText().toString());
                    controller.btnSignInClicked();
                }

            }
        });
    }

}
