package com.g.laurent.moodtracker.Models;

import android.text.format.DateFormat;

import java.util.Date;

public class Feeling {

    private int feeling;
    private long date;
    private String date_str;
    private String comment;

    public Feeling(int last_feeling, long last_date, String last_comment){
        this.feeling=last_feeling;
        this.date=last_date;
        setLast_date_in_String(last_date);
        this.comment=last_comment;
    }

    private void setLast_date_in_String(Long date){
        date_str= DateFormat.format("dd/MM/yyyy", new Date(date)).toString();
    }

    public int getFeeling(){
        return feeling;
    }

    public long getDate(){
        return date;
    }

    public String getComment(){
        return comment;
    }

    public void setFeeling(long feeling_date){
        this.date=feeling_date;
    }

    public void setDate(int feeling_number){
        this.feeling=feeling_number;
    }

    public void setComment(String feeling_comment){
        this.comment=feeling_comment;
    }

    public void reset_feeling(){
        this.feeling=-1;
        this.date=0;
        this.comment=null;
    }


    public void FeelingToString(){

        System.out.println("eeeee    feeling = " + feeling + "    date = " + date_str + "      comment=" + comment);
    }

}
