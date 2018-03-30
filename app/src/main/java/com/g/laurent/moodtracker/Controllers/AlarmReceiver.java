package com.g.laurent.moodtracker.Controllers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    private callbackAlarm mcallbackAlarm;

    @Override
    public void onReceive(Context context, Intent intent) {
        mcallbackAlarm.save_perm_last_feeling();
    }

    public interface callbackAlarm {
        void save_perm_last_feeling();
    }


    public void createCallbackAlarm(callbackAlarm mcallbackAlarm){
        this.mcallbackAlarm=mcallbackAlarm;
    }
}
