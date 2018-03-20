package com.g.laurent.moodtracker.Controllers.Activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.g.laurent.moodtracker.Controllers.Fragments.ChronoFragment;
import com.g.laurent.moodtracker.R;

public class ChronoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chrono);
        configureandshow_chronofragment();
    }

    private void configureandshow_chronofragment(){

        ChronoFragment chronoFragment = new ChronoFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_chrono_layout, chronoFragment).commit();

    }
}
