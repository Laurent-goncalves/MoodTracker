package com.g.laurent.moodtracker.Controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
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

import java.sql.Date;
import java.text.ParseException;
import java.util.GregorianCalendar;


public class MainActivity extends AppCompatActivity {

    private String comment;
    private AlertDialog.Builder builder;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences("chrono", Context.MODE_PRIVATE);

        sharedPreferences
                .edit()
                .putInt("FEELING_0", 3)
                .putString("COMMENT_0", "Très bonne la pizza")
                .apply();

        sharedPreferences
                .edit()
                .putInt("FEELING_1", 1)
                .putString("COMMENT_1", null)
                .apply();

        sharedPreferences
                .edit()
                .putInt("FEELING_2", 0)
                .putString("COMMENT_2", "commentaires!!!!")
                .apply();

        sharedPreferences
                .edit()
                .putInt("FEELING_3", 4)
                .putString("COMMENT_3", "on est mercredi ")
                .apply();

        sharedPreferences
                .edit()
                .putInt("FEELING_4", 2)
                .putString("COMMENT_4", "rien à bouffer dans le frigo ")
                .apply();

        sharedPreferences
                .edit()
                .putInt("FEELING_5", 3)
                .putString("COMMENT_5", "il fait vraiment moche aujourd'hui")
                .apply();

        sharedPreferences
                .edit()
                .putInt("FEELING_6", 2)
                .putString("COMMENT_6", null)
                .apply();

        GregorianCalendar gc1 = new GregorianCalendar(2018,3,18);
        long timeStamp1 = gc1.getTimeInMillis();

        GregorianCalendar gc2 = new GregorianCalendar(2018,3,17);
        long timeStamp2 = gc2.getTimeInMillis();

        GregorianCalendar gc3 = new GregorianCalendar(2018,3,16);
        long timeStamp3 = gc3.getTimeInMillis();

        GregorianCalendar gc4 = new GregorianCalendar(2018,3,15);
        long timeStamp4 = gc4.getTimeInMillis();

        GregorianCalendar gc5 = new GregorianCalendar(2018,3,14);
        long timeStamp5 = gc5.getTimeInMillis();

        GregorianCalendar gc6 = new GregorianCalendar(2018,3,13);
        long timeStamp6 = gc6.getTimeInMillis();

        GregorianCalendar gc7 = new GregorianCalendar(2018,3,12);
        long timeStamp7 = gc7.getTimeInMillis();

        sharedPreferences.edit().putLong("DATE_TIME_0",timeStamp1).apply();
        sharedPreferences.edit().putLong("DATE_TIME_1",timeStamp2).apply();
        sharedPreferences.edit().putLong("DATE_TIME_2",timeStamp3).apply();
        sharedPreferences.edit().putLong("DATE_TIME_3",timeStamp4).apply();
        sharedPreferences.edit().putLong("DATE_TIME_4",timeStamp5).apply();
        sharedPreferences.edit().putLong("DATE_TIME_5", timeStamp6).apply();
        sharedPreferences.edit().putLong("DATE_TIME_6", timeStamp7).apply();

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
