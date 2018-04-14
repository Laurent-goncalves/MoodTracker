package com.g.laurent.moodtracker.Models;

public class Feeling {

    private int feeling;
    private long date;
    private String comment;

    public Feeling(int last_feeling, long last_date, String last_comment){
        this.feeling=last_feeling;
        this.date=last_date;
        this.comment=last_comment;
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

    public void setComment(String feeling_comment){
        this.comment=feeling_comment;
    }

}
