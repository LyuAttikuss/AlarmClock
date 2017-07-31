package com.learning.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {
    private static String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String alarm_status = intent.getExtras().getString("Extra");
        Log.i(TAG, "Alarm status: " + alarm_status);

        Intent service_intent = new Intent(context, RingtonePlayingService.class);
        service_intent.putExtra("Extra", alarm_status);

        context.startService(service_intent);
    }
}
