package com.g.laurent.moodtracker.Controllers.Activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.g.laurent.moodtracker.Controllers.Fragments.PageFragment;
import com.g.laurent.moodtracker.Models.AlarmReceiver;
import com.g.laurent.moodtracker.Models.Feeling;
import com.g.laurent.moodtracker.Models.FeelingsChronology;
import com.g.laurent.moodtracker.Models.PageAdapter;
import com.g.laurent.moodtracker.R;
import java.util.Calendar;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;
import static java.lang.System.currentTimeMillis;

public class MainActivity extends AppCompatActivity implements PageFragment.callbackMainActivity, AlarmReceiver.callbackAlarm{

    @BindView(R.id.activity_main_icon_comment) ImageView icon_com;
    @BindView(R.id.activity_main_icon_chrono) ImageView icon_chrono;

    public static final String BUNDLE_STATE_LAST_FEELING = "last feeling";
    public static final String BUNDLE_STATE_LAST_COMMENT= "last comment";
    public static final String BUNDLE_STATE_LAST_DATE= "last date";
    public static final String BUNDLE_STATE_FRAGMENT_NUM= "fragment number";

    private int fragment_number;
    private EditText input;
    private Feeling current_feeling;
    private FeelingsChronology mFeelingsChronology;
    private String[] chrono_texts;
    private ViewPager pager;
    private SharedPreferences sharedPreferences;
    private PendingIntent pendingIntent;
    private AlarmReceiver alarmReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Recover sharedpreferences
        sharedPreferences=getSharedPreferences("chrono",MODE_PRIVATE);

        // Initialize variables
        fragment_number = 0;
        chrono_texts = getResources().getStringArray(R.array.text_chrono_list);
        define_current_feeling(savedInstanceState);
        mFeelingsChronology = new FeelingsChronology(chrono_texts.length,sharedPreferences);

        // Configuration of alarm for saving feeling each day
        AlarmReceiver.callbackAlarm mcallbackAlarm = this;
        alarmReceiver = new AlarmReceiver();
        alarmReceiver.createCallbackAlarm(mcallbackAlarm);

        // Configure ViewPager, button chronology and buttons comment
        this.configureChronoButton();
        this.configureCommentButton();
        this.configureViewPager();
        this.configure_page_adapter(fragment_number,current_feeling);
        this.configureAlarmManager();
    }

    private void configureAlarmManager(){
        Intent alarmIntent = new Intent(getApplicationContext(), alarmReceiver.getClass());
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        startAlarm();
    }

    private void startAlarm() {
        // Set the alarm to start at 0:00 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE,0);

        // Create alarm
        AlarmManager manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        if(manager!=null)
            manager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void save_chronology_in_sharedpreferrences(){

        // Save the current feeling in position 0
        save_in_sharedpreferrences(0, current_feeling);

        // Save all other feelings that can be saved (need to shift the dates)
        for(int i=1;i<=chrono_texts.length;i++) {

            if(mFeelingsChronology.getFeeling(i)!=null) // if the date for position "i" has a feeling in sharedpreferences
                save_in_sharedpreferrences(i,mFeelingsChronology.getFeeling(i)); // it is saved at the correct place in sharedpreferences
            else // if not, a null feeling is saved at the correct place in sharedpreferences
                save_in_sharedpreferrences(i,new Feeling(-1,currentTimeMillis()-24*60*60*1000*i,null));
        }
        current_feeling=new Feeling(-1,currentTimeMillis(),null); // re-initialize current feeling
        mFeelingsChronology = new FeelingsChronology(chrono_texts.length,sharedPreferences);
    }

    private void save_in_sharedpreferrences(int position, Feeling feeling){
        sharedPreferences
                .edit()
                .putInt("FEELING_" + position, feeling.getFeeling())
                .putString("COMMENT_" + position, feeling.getComment())
                .putLong("DATE_TIME_" + position, feeling.getDate())
                .apply();
    }

    // ----------------------- METHODS FOR SAVING AND RECOVER THE DATA CURRENT FEELING in case of change of activity -----------
    // -----------------------                   or switching the telephone to land mode                   -----------

    private void save_current_feeling(Bundle bundle){
        bundle.putInt(BUNDLE_STATE_LAST_FEELING, current_feeling.getFeeling());
        bundle.putString(BUNDLE_STATE_LAST_COMMENT, current_feeling.getComment());
        bundle.putLong(BUNDLE_STATE_LAST_DATE, current_feeling.getDate());
        bundle.putInt(BUNDLE_STATE_FRAGMENT_NUM, fragment_number);
    }

    private void define_current_feeling(Bundle savedInstanceState){

        if (savedInstanceState != null) {

            Long date = savedInstanceState.getLong(BUNDLE_STATE_LAST_DATE);
            int feeling;
            String comment;

            if(date==0)
                feeling = -1;
            else
                feeling = savedInstanceState.getInt(BUNDLE_STATE_LAST_FEELING,-1);

            comment= savedInstanceState.getString(BUNDLE_STATE_LAST_COMMENT,null);
            current_feeling = new Feeling(feeling,date,comment);
            fragment_number = savedInstanceState.getInt(BUNDLE_STATE_FRAGMENT_NUM,0);

        } else
            current_feeling = new Feeling(-1,currentTimeMillis(),null);

    }

    private void restore_appli_state(Bundle savedInstanceState){
        define_current_feeling(savedInstanceState);
        configureCommentButton();
        configureChronoButton();
        configure_page_adapter(fragment_number,current_feeling);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        save_current_feeling(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        restore_appli_state(savedInstanceState);
    }

    // ---------------------------------- CONFIGURATION --------------------------------

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
                (dialog, which) -> {});

        // Associate a button "VALIDER" and save the text written in last_comment
        builder.setPositiveButton("VALIDER",
                (dialog, which) -> {
                    current_feeling.setComment(input.getText().toString());
                    configure_page_adapter(fragment_number,current_feeling);
                    pager.setCurrentItem(fragment_number);
                });
        builder.show();
    }

    private void configureCommentButton(){
        // Add the icon drawable in imageView
        icon_com.setImageResource(R.drawable.ic_note_add_black);

        // Associate an event instruction in case the user click on the icon comment
        icon_com.setOnClickListener(v -> create_builder_for_AlertDialog());

        // If the feeling displayed correspond to the feeling saved temporarily,
        // display and enable the icon comment
        if(current_feeling.getFeeling()==fragment_number) {
            icon_com.setVisibility(View.VISIBLE);
            icon_com.setEnabled(true);
        } else {
            icon_com.setVisibility(View.INVISIBLE);
            icon_com.setEnabled(false);
        }
    }

    private void configureChronoButton() {

        // Add the icon drawable in imageView
        icon_chrono.setImageResource(R.drawable.ic_history_black);

        // Associate to the icon chrono the launch of ChronoActivity in case the user click on it
        icon_chrono.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), ChronoActivity.class);
            startActivity(i); // we start the activity ChronoActivity
        });
    }

    private void configure_page_adapter(int current_frag, Feeling feeling){
        PageAdapter page_adapter = new PageAdapter(getSupportFragmentManager(),
                getResources().getIntArray(R.array.colorPagesViewPager), feeling);

        pager.setAdapter(page_adapter);
        pager.setCurrentItem(current_frag);
    }

    private void configureViewPager(){

        // Get ViewPager from layout
        pager = findViewById(R.id.activity_main_viewpager);

        // Set OnPageChangeListener
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                update_fragment_number(position); // update the feeling number currently displayed
                configureCommentButton(); // configure the icon comment
            }

            @Override
            public void onPageSelected(int position) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void update_fragment_number(int position){this.fragment_number = position;}

    //  --------------------------- CALLBACK FROM PageFragment ------------------------------
    // ----------------- To update the current feeling in MainActivity ----------------------

    @Override
    public void save_temp_last_feeling(int position, Feeling feeling) {
        current_feeling = feeling;
        configure_page_adapter(fragment_number, current_feeling);
    }

    //  ------------------------------- CALLBACK FROM ALARM RECEIVER ------------------------------------
    // ----------------- To update the chronology of feelings in sharedpreferences ----------------------

    @Override
    public void save_perm_last_feeling() {

        String date_current_feeling = DateFormat.format("dd/MM/yyyy", new Date(current_feeling.getDate())).toString();
        String date_today = DateFormat.format("dd/MM/yyyy", new Date(currentTimeMillis())).toString();

        if(!date_today.equals(date_current_feeling)) { // if the current feeling is not dated from today
            Toast toast = Toast.makeText(getApplicationContext(), "Enregistrement humeur du jour", Toast.LENGTH_SHORT);
            toast.show();
            save_chronology_in_sharedpreferrences(); // update the chronology in sharedpreferences
            configure_page_adapter(fragment_number, current_feeling); // update the viewpager
        }
    }
}

        /*int last = 1;
        save_in_sharedpreferrences(0,new Feeling(0,currentTimeMillis()-24*60*60*1000*last,"il pleut"));
        save_in_sharedpreferrences(1,new Feeling(-1,currentTimeMillis()-24*60*60*1000*(1+last),null));
        save_in_sharedpreferrences(2,new Feeling(2,currentTimeMillis()-24*60*60*1000*(2+last),null));
        save_in_sharedpreferrences(3,new Feeling(3,currentTimeMillis()-24*60*60*1000*(3+last),null));
        save_in_sharedpreferrences(4,new Feeling(-1,currentTimeMillis()-24*60*60*1000*(4+last),null));
        save_in_sharedpreferrences(5,new Feeling(4,currentTimeMillis()-24*60*60*1000*(5+last),"il fait beau"));
        save_in_sharedpreferrences(6,new Feeling(1,currentTimeMillis()-24*60*60*1000*(6+last),null));
        save_in_sharedpreferrences(7,new Feeling(0,currentTimeMillis()-24*60*60*1000*(7+last),null));*/

        /*save_in_sharedpreferrences(0,new Feeling(-1,0,null));
        save_in_sharedpreferrences(1,new Feeling(-1,0,null));
        save_in_sharedpreferrences(2,new Feeling(-1,0,null));
        save_in_sharedpreferrences(3,new Feeling(-1,0,null));
        save_in_sharedpreferrences(4,new Feeling(-1,0,null));
        save_in_sharedpreferrences(5,new Feeling(-1,0,null));
        save_in_sharedpreferrences(6,new Feeling(-1,0,null));
        save_in_sharedpreferrences(7,new Feeling(-1,0,null));
        save_in_sharedpreferrences(8,new Feeling(-1,0,null));*/