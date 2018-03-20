package com.g.laurent.moodtracker.Controllers.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.g.laurent.moodtracker.Controllers.Fragments.ChronoFragment;
import com.g.laurent.moodtracker.Models.PageAdapter;
import com.g.laurent.moodtracker.R;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private ImageView icon_com;
    private ImageView icon_chrono;
    private AlertDialog.Builder builder;
    private SharedPreferences sharedPreferences;

    private HashMap<String,Integer> feelings_saved;
    private HashMap<String,String> comments_saved;
    private int last_feeling;
    private long last_date;
    private String last_comment;
    private boolean feeling_saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences("chrono", Context.MODE_PRIVATE);
        new_values();
        builder = new AlertDialog.Builder(MainActivity.this,R.style.AlertDialogCustom);
        //3 - Configure ViewPager
        this.configureChronoButton();
        this.configureCommentButton();
        this.configureViewPager();
    }

    private void configureCommentButton(){

        icon_com = (ImageView) findViewById(R.id.activity_main_icon_comment);
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
                                last_comment = input.getText().toString();
                            }
                        });

                builder.show();
            }
        });



    }

    private void recover_feelings_and_comments_saved(){
        for(int i=6;i>=0;i--){
            Long date_feeling_long = sharedPreferences.getLong("DATE_TIME_" + i,0);
            String date_feeling = DateFormat.format("dd/MM/yyyy", new Date(date_feeling_long)).toString();
            feelings_saved.put(date_feeling,sharedPreferences.getInt("FEELING_" + i,-1));
            comments_saved.put(date_feeling,sharedPreferences.getString("COMMENT_" + i,null));
        }
    }

    private void save_new_feeling(){

        if(!feeling_saved) { // if the last feeling has not been saved

            recover_feelings_and_comments_saved();

            // Save the feeling of yesterday
            sharedPreferences
                    .edit()
                    .putInt("FEELING_0", last_feeling)
                    .putString("COMMENT_0", last_comment)
                    .putLong("DATE_TIME_0", last_date)
                    .apply();

            // Save all other feelings that can be saved (need to shift the dates)
            for(int i=1;i<7;i++) {

                String date_feeling = DateFormat.format("dd/MM/yyyy", new Date(last_date-24*60*60*1000*i)).toString();

                if(feelings_saved.get(date_feeling)!=-1) { // if the feeling is saved in sharedpreferences at the date "date_feeling"
                    // save the feeling, the comment and the date "i"
                    sharedPreferences
                            .edit()
                            .putInt("FEELING_" + i, feelings_saved.get(date_feeling))
                            .putString("COMMENT_" + i, comments_saved.get(date_feeling))
                            .putLong("DATE_TIME_" + i, last_date - 24 * 60 * 60 * 1000 * i)
                            .apply();
                } else { // save as null for the day "i"
                    sharedPreferences
                            .edit()
                            .putInt("FEELING_" + i, -1)
                            .putString("COMMENT_" + i, null)
                            .putLong("DATE_TIME_" + i, last_date - 24 * 60 * 60 * 1000 * i)
                            .apply();
                }
            }
        }
    }


    private void configureChronoButton() {

        icon_chrono = (ImageView) findViewById(R.id.activity_main_icon_chrono);
        icon_chrono.setImageResource(R.drawable.ic_history_black);

        icon_chrono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //System.out.println("eeee "  + last_comment + "  " + last_date + "   " + last_feeling);
                Intent i = new Intent(getBaseContext(), ChronoActivity.class);
                startActivity(i);
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

    private void new_values() {

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

        sharedPreferences
                .edit()
                .putInt("FEELING_0", 3)
                .putString("COMMENT_0", null)
                .putLong("DATE_TIME_0",timeStamp1)
                .apply();

        sharedPreferences
                .edit()
                .putInt("FEELING_1", 1)
                .putString("COMMENT_1", null)
                .putLong("DATE_TIME_1",timeStamp2)
                .apply();

        sharedPreferences
                .edit()
                .putInt("FEELING_2", 0)
                .putString("COMMENT_2", "commentaires!!!!")
                .putLong("DATE_TIME_2",timeStamp2)
                .apply();

        sharedPreferences
                .edit()
                .putInt("FEELING_3", 4)
                .putString("COMMENT_3", "on est mercredi ")
                .putLong("DATE_TIME_3",timeStamp3)
                .apply();

        sharedPreferences
                .edit()
                .putInt("FEELING_4", 2)
                .putString("COMMENT_4", "rien à bouffer dans le frigo ")
                .putLong("DATE_TIME_4",timeStamp4)
                .apply();

        sharedPreferences
                .edit()
                .putInt("FEELING_5", 3)
                .putString("COMMENT_5", "il fait vraiment moche aujourd'hui")
                .putLong("DATE_TIME_5",timeStamp5)
                .apply();

        sharedPreferences
                .edit()
                .putInt("FEELING_6", 2)
                .putString("COMMENT_6", null)
                .putLong("DATE_TIME_6",timeStamp6)
                .apply();


    }

}
