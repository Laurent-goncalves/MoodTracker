package com.g.laurent.moodtracker.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import java.util.Date;
import java.util.HashMap;
import static java.lang.System.currentTimeMillis;

public class FeelingsChronology {

    private SharedPreferences sharedPreferences;
    private HashMap<String,Feeling> feelings_saved;
    private int number_days_chronology;

    public FeelingsChronology(int number_days_chronology, SharedPreferences sharedPreferences, Context context){

        this.sharedPreferences = sharedPreferences;
        this.number_days_chronology=number_days_chronology;
        recover_feelings_and_comments_saved();
    }

    public int getNumber_days_chronology(){
        return number_days_chronology;
    }

    private void recover_feelings_and_comments_saved(){

        feelings_saved = new HashMap<>();

        // For each keys related to the 7 days, recover the date, the feeling number and the eventual comment
        for(int i=number_days_chronology-1;i>=0;i--){

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

/*  public void save_chronology_in_sharedpreferrences(){

        // Save the current feeling
        save_in_sharedpreferrences(0, currentFeeling);

        // Save all other feelings that can be saved (need to shift the dates)
        for(int i=1;i<=number_days_chronology;i++) {
            String date_feeling_str = DateFormat.format("dd/MM/yyyy", new Date(currentTimeMillis()-24*60*60*1000*i)).toString();

            if(feelings_saved.get(date_feeling_str)!=null) // if the date for position "i" has a feeling in sharedpreferences
                save_in_sharedpreferrences(i,feelings_saved.get(date_feeling_str)); // it is saved at the correct place in sharedpreferences
            else // if not, a null feeling is saved at the correct place in sharedpreferences
                save_in_sharedpreferrences(i,new Feeling(-1,currentTimeMillis()-24*60*60*1000*i,null));
        }

        // Update of Feelings chronology
        recover_feelings_and_comments_saved();
    }

    private void save_in_sharedpreferrences(int position, Feeling feeling){
        sharedPreferences
                .edit()
                .putInt("FEELING_" + position, feeling.getFeeling())
                .putString("COMMENT_" + position, feeling.getComment())
                .putLong("DATE_TIME_" + position, feeling.getDate())
                .apply();
    }

    // --------------------------------- ALARMANAGER ---------------------------------------------------- //

    @Override
    public void save_perm_last_feeling() {
        save_chronology_in_sharedpreferrences();

        String date_current_feeling = DateFormat.format("dd/MM/yyyy", new Date(currentFeeling.getDate())).toString();
        String date_today = DateFormat.format("dd/MM/yyyy", new Date(currentTimeMillis())).toString();

        if(!date_today.equals(date_current_feeling))
            this.mCallbackAlarmMainActivity.update_chronology_and_current_feeling(this, currentFeeling);
    }

    public interface callbackAlarmMainActivity {
        void update_chronology_and_current_feeling(FeelingsChronology feelingsChronology,Feeling current_feeling);
    }*/

    // ---------------------------------- INFORMATION ON FeelingsChronology ------------------------------

    public int number_of_feelings_saved(){

        int number = 0;

        for(int i = 1;i<=number_days_chronology;i++) {

            String date = create_date_ddMMyyyy(currentTimeMillis()-24*60*60*1000*i);

            if(feelings_saved.get(date)!=null) {
                if(feelings_saved.get(date).getFeeling()!= -1)
                    number++;
            }
        }
        return number; // define how many feelings are saved in the sharedpreferences
    }

    public Feeling getFeeling(int position){

        int limit = number_days_chronology;
        String date_feeling = DateFormat.format("dd/MM/yyyy", new Date(currentTimeMillis()-24*60*60*1000 * (limit-position))).toString();

        if (feelings_saved.get(date_feeling)!= null)
            return feelings_saved.get(date_feeling);
        else
            return new Feeling(-1,currentTimeMillis()-24*60*60*1000 * (limit-position),null);
    }

    // ---------------------------------- OTHER METHODS ---------------------------------------

    private String create_date_ddMMyyyy(Long date){
        return DateFormat.format("dd/MM/yyyy", new Date(date)).toString();
    }
}
