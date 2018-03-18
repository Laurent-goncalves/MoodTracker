package com.g.laurent.moodtracker.Controllers;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.g.laurent.moodtracker.Models.PageAdapter;
import com.g.laurent.moodtracker.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //3 - Configure ViewPager
        this.configureViewPager();
    }

    private void configureViewPager(){

        ImageView icon_com = (ImageView) findViewById(R.id.activity_main_icon_comment);
        ImageView icon_chrono = (ImageView) findViewById(R.id.activity_main_icon_chrono);
        icon_com.setImageResource(R.drawable.ic_note_add_black);
        icon_chrono.setImageResource(R.drawable.ic_history_black);

        // 1 - Get ViewPager from layout
        ViewPager pager = (ViewPager)findViewById(R.id.activity_main_viewpager);
        // 2 - Set Adapter PageAdapter and glue it together
        pager.setAdapter(new PageAdapter(getSupportFragmentManager(), getResources().getIntArray(R.array.colorPagesViewPager)) {
        });
    }

}
