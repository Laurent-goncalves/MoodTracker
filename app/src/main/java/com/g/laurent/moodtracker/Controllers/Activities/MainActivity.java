package com.g.laurent.moodtracker.Controllers.Activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.g.laurent.moodtracker.Controllers.Fragments.PageFragment;
import com.g.laurent.moodtracker.Models.PageAdapter;
import com.g.laurent.moodtracker.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.ButterKnife;
import static java.lang.System.currentTimeMillis;

public class MainActivity extends AppCompatActivity implements PageFragment.callbackMainActivity {

    @BindView(R.id.activity_main_icon_comment) ImageView icon_com;
    @BindView(R.id.activity_main_icon_chrono) ImageView icon_chrono;

    public static final String BUNDLE_STATE_LAST_FEELING = "last feeling";
    public static final String BUNDLE_STATE_LAST_COMMENT= "last comment";
    public static final String BUNDLE_STATE_LAST_DATE= "last date";

    private int fragment_number;
    private EditText input;
    private SharedPreferences sharedPreferences;
    private HashMap<String,Integer> feelings_saved;
    private HashMap<String,String> comments_saved;
    private int last_feeling;
    private long last_date;
    private String last_comment;
    private String[] chrono_texts;
    private ViewPager pager;
    private PageAdapter page_adapter;
    private Bundle bundle;

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Recover sharedpreferences
        sharedPreferences = this.getSharedPreferences("chrono", Context.MODE_PRIVATE);


        // Initialize variants
        fragment_number = 0;
        bundle = new Bundle();
        recover_data_saved(savedInstanceState);
        chrono_texts = getResources().getStringArray(R.array.text_chrono_list);
        new_values();
        System.out.println("eeeee    last_feeling=" + last_feeling + "   last_comment=" + last_comment + "     last_date=" + last_date);

        // Configure ViewPager, button chronology and buttons comment
        this.configureChronoButton();
        this.configureCommentButton();
        this.configureViewPager();
        this.configure_page_adapter(fragment_number,last_feeling, last_comment);
        display_data_saved();


        // SETTING THE ALARM
        alarmMgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        // Set the alarm to start at 0:00 AM
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,3);

        // setRepeating() lets you specify a precise custom interval--in this case,
        // 1 day
        alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                60000, alarmIntent);


       // display_data_saved();

    }



    // ------- CONFIGURATION ----------

    private void recover_data_saved(Bundle savedInstanceState){

        if (savedInstanceState != null) {
            last_date= savedInstanceState.getLong(BUNDLE_STATE_LAST_DATE);

            if(last_date==0)
                last_feeling = -1;
            else
                last_feeling = savedInstanceState.getInt(BUNDLE_STATE_LAST_FEELING,-1);

            last_comment= savedInstanceState.getString(BUNDLE_STATE_LAST_COMMENT,null);

        } else {
            last_date = 0;
            last_feeling = -1;
            last_comment=null;
        }
    }

    private void create_builder_for_AlertDialog(){

        AlertDialog.Builder builder;

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
                        configure_page_adapter(fragment_number,last_feeling,last_comment);
                        pager.setCurrentItem(fragment_number);
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

    private void configure_page_adapter(int current_frag, int last_feeling, String last_comment){

        page_adapter = new PageAdapter(getSupportFragmentManager(),
                getResources().getIntArray(R.array.colorPagesViewPager),last_feeling,last_comment);

            pager=null;
            configureViewPager();
            pager.setAdapter(page_adapter);
            pager.setCurrentItem(current_frag);

    }

    private void configureViewPager(){

        // Get ViewPager from layout
        pager = (ViewPager)findViewById(R.id.activity_main_viewpager);

        // Set OnPageChangeListener
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                update_fragment_number(position);

               if(last_feeling==position) {
                    icon_com.setVisibility(View.VISIBLE);
                    icon_com.setEnabled(true);
                } else {
                   icon_com.setVisibility(View.INVISIBLE);
                   icon_com.setEnabled(false);

               }
            }

            @Override
            public void onPageSelected(int position) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void update_fragment_number(int position){this.fragment_number = position;}

    @Override
    public void save_temp_last_feeling(int position, String comment) {

        this.last_feeling = position;
        this.last_date = currentTimeMillis() ;
        configure_page_adapter(fragment_number, last_feeling, comment);
    }

    // ------- RECOVER DATA FROM SHAREDPREFERRENCES ----------

    private void recover_feelings_and_comments_saved(){

        feelings_saved = new HashMap<>();
        comments_saved= new HashMap<>();

        // For each keys related to the 7 days, recover the date, the feeling number and the eventual comment
        for(int i=chrono_texts.length-1;i>=0;i--){
            Long date_feeling_long = sharedPreferences.getLong("DATE_TIME_" + i,0);
            String date_feeling = DateFormat.format("dd/MM/yyyy", new Date(date_feeling_long)).toString();

            // the data are booked in temporary hashmaps feelings_saved and comments_saved
            feelings_saved.put(date_feeling,sharedPreferences.getInt("FEELING_" + i,-1));
            comments_saved.put(date_feeling,sharedPreferences.getString("COMMENT_" + i,null));
        }
    }

    // ------- SAVE DATA IN SHAREDPREFERRENCES ----------

    private void save_chronology_in_sharedpreferrences(){

        if(last_feeling !=-1) { // if the last feeling has not been saved

            // we recover the data saved
            recover_feelings_and_comments_saved();

            String last_date_string = DateFormat.format("dd/MM/yyyy", new Date(last_date)).toString();

            // Save all other feelings that can be saved (need to shift the dates)
            for(int i=1;i<=chrono_texts.length;i++) {

                String date_feeling = DateFormat.format("dd/MM/yyyy", new Date(currentTimeMillis()-24*60*60*1000*i)).toString();

                if(feelings_saved.get(date_feeling)!=null)// if the feeling is saved in sharedpreferences at the date "date_feeling"
                    // save the feeling, the comment and the date "i"
                    save_in_sharedpreferrences(i - 1, feelings_saved.get(date_feeling), comments_saved.get(date_feeling), currentTimeMillis() - 24 * 60 * 60 * 1000 * i);
                else if (date_feeling.equals(last_date_string))
                    save_in_sharedpreferrences(i - 1, last_feeling, last_comment, last_date);
                else
                    save_in_sharedpreferrences(i-1,-1,null,currentTimeMillis() - 24 * 60 * 60 * 1000 * i);

            }

            recover_feelings_and_comments_saved();
            display_data_saved();

        }

        // The values are reset
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

    // ------- WHEN THE ACTIVITY REOPENS, CHECK IF DATA CAN BE SAVED -----------

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("eeeee onResume : last_feeling =" + last_feeling + "    last_comment=" + last_comment);

        String date_today = (new Date(currentTimeMillis())).toString();
        String date_to_compare = (new Date(last_date)).toString();

        System.out.println("eeeee onResume : date_today =" + date_today + "    date_to_compare=" + date_to_compare);

        // if the last_date is different than today's date, the data can be saved in sharedpreferences
        if(!date_to_compare.equals(date_today) && last_date != 0) {
            save_chronology_in_sharedpreferrences();
            System.out.println("eeeee save_chronology_in_sharedpreferrences");
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt(BUNDLE_STATE_LAST_FEELING, last_feeling);
        outState.putString(BUNDLE_STATE_LAST_COMMENT, last_comment);
        outState.putLong(BUNDLE_STATE_LAST_DATE, last_date);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();

        bundle.putInt(BUNDLE_STATE_LAST_FEELING, last_feeling);
        bundle.putString(BUNDLE_STATE_LAST_COMMENT, last_comment);
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        last_feeling=bundle.getInt(BUNDLE_STATE_LAST_FEELING);
        last_comment=bundle.getString(BUNDLE_STATE_LAST_COMMENT);

        page_adapter = new PageAdapter(getSupportFragmentManager(),
                getResources().getIntArray(R.array.colorPagesViewPager),last_feeling,last_comment);

        if(pager !=null) {
            pager.setAdapter(page_adapter);
            pager.setCurrentItem(0);
        }
    }

    private void display_data_saved(){

        System.out.println("eeeed -------------------------------------------------------------");
        for(int i = chrono_texts.length-1;i>=0;i--) {
            Long date_feeling_long = sharedPreferences.getLong("DATE_TIME_" + i, 0);
            String date_feeling = DateFormat.format("dd/MM/yyyy", new Date(date_feeling_long)).toString();
            String comment = sharedPreferences.getString("COMMENT_" + i, null);
            int feeling = sharedPreferences.getInt("FEELING_" + i, -1);

            System.out.println("eeeedd " + i + " " + date_feeling + "  " + feeling + "   " + comment );
        }
        System.out.println("eeeed -------------------------------------------------------------");
    }

    private void new_values() {

        int last = 29;

        save_in_sharedpreferrences(0,0,"commentaire1",new GregorianCalendar(2018,2,last).getTimeInMillis());
        save_in_sharedpreferrences(1,3,null,new GregorianCalendar(2018,2,last-1).getTimeInMillis());
        save_in_sharedpreferrences(2,4,"eeee",new GregorianCalendar(2018,2,last-2).getTimeInMillis());
        save_in_sharedpreferrences(3,3,"il a plu toute la journée...",new GregorianCalendar(2018,2,last-3).getTimeInMillis());
        save_in_sharedpreferrences(4,2,null,new GregorianCalendar(2018,2,last-4).getTimeInMillis());
        save_in_sharedpreferrences(5,1,"il fait beau",new GregorianCalendar(2018,2,last-5).getTimeInMillis());
        save_in_sharedpreferrences(6,0,"y'a plus rien dans le frigo !!",new GregorianCalendar(2018,2,last-6).getTimeInMillis());
    }


}

