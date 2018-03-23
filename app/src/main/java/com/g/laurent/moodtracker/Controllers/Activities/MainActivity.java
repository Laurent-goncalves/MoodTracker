package com.g.laurent.moodtracker.Controllers.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.g.laurent.moodtracker.Controllers.Fragments.PageFragment;
import com.g.laurent.moodtracker.Models.PageAdapter;
import com.g.laurent.moodtracker.R;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.System.currentTimeMillis;

public class MainActivity extends AppCompatActivity implements PageFragment.callbackMainActivity, PageFragment.callbackMainActivity_test{

    @BindView(R.id.activity_main_icon_comment) ImageView icon_com;
    @BindView(R.id.activity_main_icon_chrono) ImageView icon_chrono;

    private AlertDialog.Builder builder;
    private EditText input;
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
        ButterKnife.bind(this);

        // Recover sharedpreferences
        sharedPreferences = this.getSharedPreferences("chrono", Context.MODE_PRIVATE);
        feelings_saved = new HashMap<>();
        comments_saved= new HashMap<>();
        new_values();



        last_feeling = 2;
        last_date = currentTimeMillis() - 24 * 60 * 60 * 1000;
        last_comment="tout premier commentaire!";
        feeling_saved=false;

        // Configure ViewPager, button chronology and buttons comment
        this.configureChronoButton();
        this.configureCommentButton();
        this.configureViewPager();

        //getStatusBarHeight();
    }

    // ------- CONFIGURATION ----------

    private void create_builder_for_AlertDialog(){

        // Create builder to build alertdialog window to get comment from the user
        builder = new AlertDialog.Builder(MainActivity.this,R.style.AlertDialogCustom);

        // Initialize and set input
        input = new EditText(MainActivity.this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        // Change some attributes of the builder
        builder.setTitle("Commentaire");
        builder.setCancelable(true);
        builder.setView(input);

        // Associate a button "ANNULER"
        builder.setNegativeButton("ANNULER",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {}
                });

        // Associate a button "VALIDER" and save the text written in last_comment
        builder.setPositiveButton("VALIDER",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        last_comment = input.getText().toString();
                    }
                });
        builder.show();
    }

    private void configureCommentButton(){

        // Add the icon drawable in imageView
        icon_com.setImageResource(R.drawable.ic_note_add_black);

        // Associate an event instruction in case the user click on the icon comment
        icon_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_builder_for_AlertDialog();
            }
        });
    }

    private void configureChronoButton() {

        // Add the icon drawable in imageView
        icon_chrono.setImageResource(R.drawable.ic_history_black);

        // Associate an event instruction in case the user click on the icon chronology
        icon_chrono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ChronoActivity.class);
                startActivity(i); // we start the activity ChronoActivity
            }
        });
    }

    private void configureViewPager(){
        // Get ViewPager from layout
        ViewPager pager = (ViewPager)findViewById(R.id.activity_main_viewpager);
        // Set Adapter PageAdapter and glue it together
        pager.setAdapter(new PageAdapter(getSupportFragmentManager(),
                getResources().getIntArray(R.array.colorPagesViewPager),selected(),comments()) {
        });
    }

    private Boolean[] selected() {

        int feeling_nb = getResources().getIntArray(R.array.colorPagesViewPager).length;
        Boolean[] list_selected = new Boolean[feeling_nb];

        for(int i = 0; i<feeling_nb;i++) {
            list_selected[i] = i == last_feeling;
        }
        return list_selected;
    }

    private String[] comments() {

        int feeling_nb = getResources().getIntArray(R.array.colorPagesViewPager).length;
        String[] list_comments = new String[feeling_nb];

            for(int i = 0; i<feeling_nb;i++) {
                if(i == last_feeling)
                    list_comments[i] = last_comment;
                else
                    list_comments[i] = null;
            }

        return list_comments;
    }


    // ------- RECOVER DATA FROM SHAREDPREFERRENCES ----------

    private void recover_feelings_and_comments_saved(){

        // For each keys related to the 7 days, recover the date, the feeling number and the eventual comment
        for(int i=6;i>=0;i--){
            Long date_feeling_long = sharedPreferences.getLong("DATE_TIME_" + i,0);
            String date_feeling = DateFormat.format("dd/MM/yyyy", new Date(date_feeling_long)).toString();

            // the data are booked in temporary hashmaps feelings_saved and comments_saved
            feelings_saved.put(date_feeling,sharedPreferences.getInt("FEELING_" + i,-1));
            comments_saved.put(date_feeling,sharedPreferences.getString("COMMENT_" + i,null));
        }
    }

    // ------- SAVE DATA IN SHAREDPREFERRENCES ----------

    private void save_chronology_in_sharedpreferrences(){

        if(!feeling_saved && feelings_saved!=null) { // if the last feeling has not been saved

            // we recover the data saved
            recover_feelings_and_comments_saved();

            //display_data_saved();

            String last_date_string = DateFormat.format("dd/MM/yyyy", new Date(last_date)).toString();

            // Save all other feelings that can be saved (need to shift the dates)
            for(int i=1;i<=7;i++) {

                String date_feeling = DateFormat.format("dd/MM/yyyy", new Date(currentTimeMillis()-24*60*60*1000*i)).toString();

                if(feelings_saved.get(date_feeling)!=null) // if the feeling is saved in sharedpreferences at the date "date_feeling"
                    // save the feeling, the comment and the date "i"
                    save_in_sharedpreferrences(i-1,feelings_saved.get(date_feeling),comments_saved.get(date_feeling),currentTimeMillis() - 24 * 60 * 60 * 1000 * i);

                else if (date_feeling.equals(last_date_string))
                    save_in_sharedpreferrences(i-1, last_feeling, last_comment, last_date);
                else
                    save_in_sharedpreferrences(i-1,-1,null,currentTimeMillis() - 24 * 60 * 60 * 1000 * i);


            }

            recover_feelings_and_comments_saved();

            //display_data_saved();
            /*System.out.println("eeee START OF RECOVER FEELING");
            recover_feelings_and_comments_saved();
            System.out.println("eeee END OF RECOVER FEELING");*/
        }

        // The values are reset
        this.feeling_saved = true;
        this.last_comment = null;
        this.last_date = 0;
        this.last_feeling = -1;

    }

    private void save_in_sharedpreferrences(int position, int feeling, String comment, Long date){
        sharedPreferences
                .edit()
                .putInt("FEELING_" + position, feeling)
                .putString("COMMENT_" + position, comment)
                .putLong("DATE_TIME_" + position, date)
                .apply();
    }

    @Override
    public void save_temp_last_feeling(int position) {

        this.last_feeling=position;
        this.last_date = currentTimeMillis() ;
        this.feeling_saved=false;



    }

    // ------- WHEN THE ACTIVITY REOPENS, CHECK IF DATA CAN BE SAVED -----------
    @Override
    protected void onResume() {
        super.onResume();

        String date_today = (new Date(currentTimeMillis())).toString();
        String date_to_compare = (new Date(last_date)).toString();

        // if the last_date is different than today's date, the data can be saved in sharedpreferences
        if(!date_to_compare.equals(date_today) && last_date != 0)
            save_chronology_in_sharedpreferrences();
    }


    private void display_data_saved(){

        System.out.println("eeeed -------------------------------------------------------------");
        for(int i = 0;i<=6;i++) {
            Long date_feeling_long = sharedPreferences.getLong("DATE_TIME_" + i, 0);
            String date_feeling = DateFormat.format("dd/MM/yyyy", new Date(date_feeling_long)).toString();

            System.out.println("eeeedd " + i + " " + date_feeling + "  " + feelings_saved.get(date_feeling) + "   " + comments_saved.get(date_feeling));
        }
        System.out.println("eeeed -------------------------------------------------------------");
    }

    private void new_values() {

        int last = 21;

        save_in_sharedpreferrences(0,1,"commentaire1",new GregorianCalendar(2018,2,last).getTimeInMillis());
        save_in_sharedpreferrences(1,1,"commentaire2",new GregorianCalendar(2018,2,last-1).getTimeInMillis());
        save_in_sharedpreferrences(2,3,null,new GregorianCalendar(2018,2,last-2).getTimeInMillis());
        save_in_sharedpreferrences(3,2,null,new GregorianCalendar(2018,2,last-3).getTimeInMillis());
        save_in_sharedpreferrences(4,4,null,new GregorianCalendar(2018,2,last-4).getTimeInMillis());
        save_in_sharedpreferrences(5,2,"avant dernier commentaire",new GregorianCalendar(2018,2,last-5).getTimeInMillis());
        save_in_sharedpreferrences(6,1,"dernier commentaire",new GregorianCalendar(2018,2,last-6).getTimeInMillis());
    }




    @Override
    public void display_test() {


        System.out.println("eeee88 " + last_feeling + "  " + last_date+ "  " + feeling_saved);

    }
}


