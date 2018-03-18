package com.g.laurent.moodtracker.Controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.g.laurent.moodtracker.Models.PageAdapter;
import com.g.laurent.moodtracker.R;

public class MainActivity extends AppCompatActivity {

    private String comment;
    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        builder = new AlertDialog.Builder(MainActivity.this,R.style.AlertDialogCustom);
        //3 - Configure ViewPager
        this.configureChronoButton();
        this.configureCommentButton();
        //this.configureViewPager();
    }

    private void configureCommentButton(){

        ImageView icon_com = (ImageView) findViewById(R.id.activity_main_icon_comment);
        icon_com.setImageResource(R.drawable.ic_note_add_black);
        final EditText input = new EditText(this);

        icon_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);

                builder.setView(input);
                builder.setCancelable(true);
                builder.setTitle("Commentaire");

                builder.setNegativeButton("ANNULER",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                builder.setPositiveButton("VALIDER",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                comment = input.getText().toString();
                            }
                        });

                builder.show();
            }
        });



    }

    private void configureChronoButton() {

        ImageView icon_chrono = (ImageView) findViewById(R.id.activity_main_icon_chrono);
        icon_chrono.setImageResource(R.drawable.ic_history_black);

        icon_chrono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChronoFragment chronoFragment = new ChronoFragment();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_page_back, chronoFragment).commit();

            }
        });
    }

    private void configureViewPager(){
        // 1 - Get ViewPager from layout
        ViewPager pager = (ViewPager)findViewById(R.id.activity_main_viewpager);
        // 2 - Set Adapter PageAdapter and glue it together
        pager.setAdapter(new PageAdapter(getSupportFragmentManager(), getResources().getIntArray(R.array.colorPagesViewPager)) {
        });
    }

}
