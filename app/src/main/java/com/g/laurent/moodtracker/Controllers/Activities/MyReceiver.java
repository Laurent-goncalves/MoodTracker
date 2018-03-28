package com.g.laurent.moodtracker.Controllers.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("eeeee alarm works !!!!!!!!!!!!!!!!!!!!");

        /*Intent scheduledIntent = new Intent(context, MainActivity.class);
        scheduledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(scheduledIntent);*/

    }
}