package com.learning.alarmclock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class RingtonePlayingService extends Service {
    private static String TAG = "RingtonePlayingService";
    private MediaPlayer mPlayer;
    private int startId;
    private boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        String title = getResources().getString(R.string.app_name);
        String message = getResources().getString(R.string.alarmTurnOff);

        final NotificationManager mNM = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        // TODO: поправить уведомление
        Intent mainIntent = new Intent(this.getApplicationContext(), AlarmClockActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);

        NotificationCompat.Builder mNotify  = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pIntent)
                .setAutoCancel(true);

        String status = intent.getExtras().getString("Extra");
        startId = (status != null && status.equals("alarm on")) ? 1 : 0;

        // Будильник включен, проиграть мелодию
        if (!this.isRunning && startId == 1) {
            mPlayer = MediaPlayer.create(this, R.raw.notification_sound);
            mPlayer.start();

            //mNM.notify(0, mNotify.build());

            this.isRunning = true;
            this.startId = 0;
        }

        // Играет мелодия, отключить будильник
        else if (this.isRunning && startId == 0) {
            mPlayer.stop();
            mPlayer.reset();

            this.isRunning = false;
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isRunning = false;
        Toast.makeText(this, R.string.service_stopped, Toast.LENGTH_SHORT).show();
    }
}
