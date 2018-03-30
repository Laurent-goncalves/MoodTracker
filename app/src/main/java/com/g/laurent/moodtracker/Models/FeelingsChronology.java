package com.g.laurent.moodtracker.Models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import com.g.laurent.moodtracker.Controllers.Activities.MainActivity;
import com.g.laurent.moodtracker.Controllers.AlarmReceiver;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import static java.lang.System.currentTimeMillis;


public class FeelingsChronology  {

    private SharedPreferences sharedPreferences;
    private HashMap<String,Feeling> feelings_saved;
    private int number_feelings_saved;
    private Feeling currentFeeling;
    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;
    private AlarmReceiver.callbackAlarm mcallbackAlarm;
    private AlarmReceiver alarmReceiver;


    public FeelingsChronology(int number_feelings_saved, SharedPreferences sharedPreferences, Feeling currentFeeling){


        mcallbackAlarm=this;
        alarmReceiver = new AlarmReceiver();
        alarmReceiver.createCallbackAlarm(mcallbackAlarm);

        this.sharedPreferences = sharedPreferences;
        this.number_feelings_saved=number_feelings_saved;
        this.currentFeeling=currentFeeling;
        recover_feelings_and_comments_saved();
        // this.configureAlarmManager();
    }

    private void recover_feelings_and_comments_saved(){

        feelings_saved = new HashMap<>();

        // For each keys related to the 7 days, recover the date, the feeling number and the eventual comment
        for(int i=number_feelings_saved-1;i>=0;i--){

            // recover in sharedPreferences the different parameters necessary to create a feeling
            Long date_feeling_long = sharedPreferences.getLong("DATE_TIME_" + i,0);
            int feeling_number = sharedPreferences.getInt("FEELING_" + i,-1);
            String comment = sharedPreferences.getString("COMMENT_" + i,null);
            String date_feeling_String = DateFormat.format("dd/MM/yyyy", new Date(date_feeling_long)).toString();

            // Creation of the feeling
            Feeling feeling = new Feeling(feeling_number,date_feeling_long,comment);

            feelings_saved.put(date_feeling_String,feeling);
        }
    }

    public void save_chronology_in_sharedpreferrences(){

        // Save the current feeling
        String date_feeling = DateFormat.format("dd/MM/yyyy", new Date(currentTimeMillis()-24*60*60*1000)).toString();
        save_in_sharedpreferrences(0, currentFeeling);

        // Save all other feelings that can be saved (need to shift the dates)
        for(int i=1;i<=number_feelings_saved;i++) {
            String date_feeling_str = DateFormat.format("dd/MM/yyyy", new Date(currentTimeMillis()-24*60*60*1000*i)).toString();

            if(feelings_saved.get(date_feeling_str)!=null) // if the date for position "i" has a feeling in sharedpreferences
                save_in_sharedpreferrences(i,feelings_saved.get(date_feeling_str)); // it is saved at the correct place in sharedpreferences
            else // if not, a null feeling is saved at the correct place in sharedpreferences
                save_in_sharedpreferrences(i,new Feeling(-1,currentTimeMillis()-24*60*60*1000*i,null));
        }
    }

    private void save_in_sharedpreferrences(int position, Feeling feeling){
        sharedPreferences
                .edit()
                .putInt("FEELING_" + position, feeling.getFeeling())
                .putString("COMMENT_" + position, feeling.getComment())
                .putLong("DATE_TIME_" + position, feeling.getDate())
                .apply();
    }




    private void configureAlarmManager(){
        Intent alarmIntent = new Intent(MainActivity.this, alarmReceiver.getClass());
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        startAlarm();
    }

    private void startAlarm() {

        // Set the alarm to start at 0:00 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        // Create alarm
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void new_values() {

        int last = 29;

        save_in_sharedpreferrences(0,new Feeling(0,new GregorianCalendar(2018,2,last).getTimeInMillis(),"commentaire1"));
        save_in_sharedpreferrences(1,new Feeling(3,new GregorianCalendar(2018,2,last-1).getTimeInMillis(),null));
        save_in_sharedpreferrences(2,new Feeling(4,new GregorianCalendar(2018,2,last-2).getTimeInMillis(),"eeee"));
        save_in_sharedpreferrences(3,new Feeling(3,new GregorianCalendar(2018,2,last-3).getTimeInMillis(),"il a plu toute la journée..."));
        save_in_sharedpreferrences(4,new Feeling(2,new GregorianCalendar(2018,2,last-4).getTimeInMillis(),null);
        save_in_sharedpreferrences(5,new Feeling(1,new GregorianCalendar(2018,2,last-5).getTimeInMillis(),"il fait beau"));
        save_in_sharedpreferrences(6,new Feeling(0,new GregorianCalendar(2018,2,last-6).getTimeInMillis(),"y'a plus rien dans le frigo !!"));
    }

}
