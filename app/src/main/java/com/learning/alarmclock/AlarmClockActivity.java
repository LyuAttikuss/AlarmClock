package com.learning.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmClockActivity extends AppCompatActivity {
    private AlarmManager alarm_manager;
    private TimePicker alarm_time_picker;
    private Button alarm_on;
    private Button alarm_off;
    private PendingIntent pending_intent;

    int mHour;
    int mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);

        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_time_picker = (TimePicker) findViewById(R.id.timePicker);
        alarm_on = (Button) findViewById(R.id.alarmOn);
        alarm_off = (Button) findViewById(R.id.alarmOff);

        final Intent intent = new Intent(AlarmClockActivity.this, AlarmReceiver.class);

        final Calendar calendar = Calendar.getInstance();

        alarm_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.set(Calendar.HOUR_OF_DAY, mHour);
                calendar.set(Calendar.MINUTE, mMinute);

                String hour_string = String.valueOf(mHour);
                String minute_string = String.valueOf(mMinute);

                if (mMinute < 10) {
                    minute_string = "0" + minute_string;
                }

                intent.putExtra("Extra", "alarm on");

                // Создание отложенного события
                pending_intent = PendingIntent.getBroadcast(AlarmClockActivity.this, 0,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pending_intent);

                setUpdateText(getResources().getString(R.string.alarmTurnedOn) + hour_string + ":" + minute_string);
            }
        });

        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarm_manager.cancel(pending_intent);

                intent.putExtra("Extra", "alarm off");
                sendBroadcast(intent);

                setUpdateText(getResources().getString(R.string.alarmTurnedOff));
            }
        });

        alarm_time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
            }
        });
    }

    private void setUpdateText(String notification) {
        Toast.makeText(this, notification, Toast.LENGTH_SHORT).show();
    }
}
