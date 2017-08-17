package com.learning.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class AlarmClockActivity extends AppCompatActivity {
    private static String ALARM_TITLE = "title";
    private static String ALARM_TIME = "time";
    private static String ALARM_DAYS = "days";

    private AlarmManager alarm_manager;
    private Button alarm_on;
    private Button alarm_off;
    private Button add;
    private PendingIntent pending_intent;
    private ListView alarmsList;

    int mHour;
    int mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);

        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_on = (Button) findViewById(R.id.alarmOn);
        alarm_off = (Button) findViewById(R.id.alarmOff);
        add = (Button) findViewById(R.id.add);
        alarmsList = (ListView) findViewById(R.id.alarms_list);

        final ArrayList<HashMap<String, Object>> alarms = new ArrayList<>();

        //final ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.alarm_item, R.id.alarm_edit, alarms);
        final SimpleAdapter adapter = new SimpleAdapter(this, alarms, R.layout.alarm_item, new String[]{ALARM_TITLE}, new int[]{R.id.alarm_edit});
        alarmsList.setAdapter(adapter);

        final Intent intent = new Intent(AlarmClockActivity.this, AlarmReceiver.class);

        alarmsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showNotify(alarms.get(position).toString());
                Intent prefIntent = new Intent(AlarmClockActivity.this, AlarmPreferences.class);
                startActivity(prefIntent);
            }
        });

        // TODO: move to AlertActivity
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pending_intent = PendingIntent.getBroadcast(AlarmClockActivity.this, 0,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarm_manager.cancel(pending_intent);

                intent.putExtra("Extra", "alarm off");
                sendBroadcast(intent);

                showNotify(getResources().getString(R.string.alarmTurnedOff));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AlarmClockActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar mChosenTime = Calendar.getInstance();
                        mChosenTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        mChosenTime.set(Calendar.MINUTE, minute);

                        Date mChosenDate = mChosenTime.getTime();
                        Date mCurrentDate = mCurrentTime.getTime();

                        mCurrentTime.set(Calendar.SECOND, 0);
                        mCurrentTime.set(Calendar.MILLISECOND, 0);

                        if (mChosenDate.getTime() - mCurrentDate.getTime() <= 0) {
                            mChosenTime.add(Calendar.DATE, 1);
                            mChosenDate = mChosenTime.getTime();
                        }

                        String remainingTime = calculateRemainingTime(mChosenDate, mCurrentDate);
                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put(ALARM_TITLE, formatter.format(mChosenDate));
                        hashMap.put(ALARM_TIME, mChosenTime);
                        alarms.add(hashMap);

                        adapter.notifyDataSetChanged();

                        setAlarm(hourOfDay, minute);

                        showNotify("Будильник зазвонит через " + remainingTime);
                    }
                }, hour, minute, true);

                mTimePicker.setButton(TimePickerDialog.BUTTON_NEGATIVE, "Отмена", mTimePicker);
                mTimePicker.setButton(TimePickerDialog.BUTTON_POSITIVE, "ОК", mTimePicker);
                mTimePicker.show();
            }
        });
    }

    private void showNotify(String notification) {
        Toast.makeText(this, notification, Toast.LENGTH_SHORT).show();
    }

    // TODO: дни
    private String calculateRemainingTime(Date chosenTime, Date currentDate) {
        Long remainingSeconds = chosenTime.getTime() - currentDate.getTime();

        int remainingHours = (int) TimeUnit.MILLISECONDS.toHours(remainingSeconds);
        int remainingMinutes = (int) (TimeUnit.MILLISECONDS.toMinutes(remainingSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(remainingSeconds)));

        String[] decDay = {"день", "дня", "дней"};
        String[] decHours = {"час", "часа", "часов"};
        String[] decMinutes = {"минуту", "минуты", "минут"};
        String remainingTime;

        if (remainingHours == 0) {
            remainingTime = String.format("%d %s",
                    remainingMinutes,
                    decOfNumber(remainingMinutes, decMinutes));
        } else if (remainingMinutes == 0) {
            remainingTime = String.format("%d %s",
                    remainingHours,
                    decOfNumber(remainingHours, decHours));
        } else {
            remainingTime = String.format("%d %s %d %s",
                    remainingHours,
                    decOfNumber(remainingHours, decHours),
                    remainingMinutes,
                    decOfNumber(remainingMinutes, decMinutes));
        }

        return remainingTime;
    }

    private String decOfNumber(int number, String[] titles) {
        int[] decCases = {2, 0, 1, 1, 1, 2};
        int variant = number % 100 > 4 && number % 100 < 20 ? 2 : decCases[Math.min(number % 10, 5)];
        return titles[variant];
    }

    private void setAlarm(int hour, int minute) {
        Calendar alarmTime = Calendar.getInstance();
        alarmTime.set(Calendar.HOUR_OF_DAY, hour);
        alarmTime.set(Calendar.MINUTE, minute);
        //alarmTime.set(Calendar.DAY_OF_WEEK, day);
        alarmTime.add(Calendar.SECOND, 0);

        Intent intent = new Intent(AlarmClockActivity.this, AlarmReceiver.class);
        intent.putExtra("Extra", "alarm on");

        pending_intent = PendingIntent.getBroadcast(AlarmClockActivity.this,
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                alarmTime.getTimeInMillis(), 1000 * 60 * 1, pending_intent);
    }
}

