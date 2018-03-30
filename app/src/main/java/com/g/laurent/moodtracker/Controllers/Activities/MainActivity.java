package com.g.laurent.moodtracker.Controllers.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.g.laurent.moodtracker.Controllers.Fragments.PageFragment;
import com.g.laurent.moodtracker.Models.Feeling;
import com.g.laurent.moodtracker.Models.FeelingsChronology;
import com.g.laurent.moodtracker.Models.PageAdapter;
import com.g.laurent.moodtracker.R;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;
import static java.lang.System.currentTimeMillis;

public class MainActivity extends AppCompatActivity implements PageFragment.callbackMainActivity {

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
    private PageAdapter page_adapter;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Recover sharedpreferences
        sharedPreferences=getSharedPreferences("chrono",Context.MODE_PRIVATE);

        // Initialize variants
        fragment_number = 0;
        onRestoreInstanceState(savedInstanceState);
        chrono_texts = getResources().getStringArray(R.array.text_chrono_list);
        mFeelingsChronology = new FeelingsChronology(chrono_texts.length,sharedPreferences,current_feeling);

        // Configure ViewPager, button chronology and buttons comment
        this.configureChronoButton();
        this.configureCommentButton();
        this.configureViewPager();
        this.configure_page_adapter(fragment_number,current_feeling);
    }

    // ------- CONFIGURATION ----------

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);

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
            current_feeling = new Feeling(-1,0,null);

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
                        current_feeling.setComment(input.getText().toString());
                        configure_page_adapter(fragment_number,current_feeling);
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

        // Associate an event instruction in case the user click on the icon chronology
        icon_chrono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ChronoActivity.class);
                startActivity(i); // we start the activity ChronoActivity
            }
        });
    }

    private void configure_page_adapter(int current_frag, Feeling feeling){
        page_adapter = new PageAdapter(getSupportFragmentManager(),
                getResources().getIntArray(R.array.colorPagesViewPager),feeling);

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
                configureCommentButton();
            }

            @Override
            public void onPageSelected(int position) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void update_fragment_number(int position){this.fragment_number = position;}

    @Override
    public void save_temp_last_feeling(int position, Feeling feeling) {

        current_feeling = feeling;
        configure_page_adapter(fragment_number, current_feeling);
    }

    // ------- WHEN THE ACTIVITY REOPENS, CHECK IF DATA CAN BE SAVED -----------

    @Override
    protected void onResume() {
        super.onResume();

        String date_today = (new Date(currentTimeMillis())).toString();
        String date_to_compare = (new Date(current_feeling.getDate())).toString();

        // if the last_date is different than today's date, the data can be saved in sharedpreferences
        if(!date_to_compare.equals(date_today) && current_feeling.getDate() != 0) {
            mFeelingsChronology.save_chronology_in_sharedpreferrences();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt(BUNDLE_STATE_LAST_FEELING, current_feeling.getFeeling());
        outState.putString(BUNDLE_STATE_LAST_COMMENT, current_feeling.getComment());
        outState.putLong(BUNDLE_STATE_LAST_DATE, current_feeling.getDate());
        outState.putInt(BUNDLE_STATE_FRAGMENT_NUM, fragment_number);

        configureCommentButton();
        super.onSaveInstanceState(outState);
    }

    /*@Override
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
    }*/

}
