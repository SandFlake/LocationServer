package com.mau.dalvi.locationserver;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            initializeSystem();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initializeSystem() throws IOException {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Controller(this);
    }

    public void setFragment(Fragment fragment, boolean backstack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.container, fragment);

        if (backstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }


}




