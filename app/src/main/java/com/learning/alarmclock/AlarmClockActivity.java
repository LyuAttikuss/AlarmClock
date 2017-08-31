package com.learning.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AlarmClockActivity extends AppCompatActivity {
    private static String ALARM_TITLE = "title";
    private static String ALARM_TIME = "time";
    private static String ALARM_DAYS = "days";

    private AlarmManager alarmManager;
    private Button btnAlarmOff;
    private FloatingActionButton btnAdd;
    private PendingIntent pendingIntent;
    private ListView lvAlarmsList;
    private AlarmAdapter alarmAdapter;
    private ArrayList alarms;
    private Cursor alarmsDb;
    private long currentAlarmId;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        btnAlarmOff = (Button) findViewById(R.id.alarmOff);
        btnAdd = (FloatingActionButton) findViewById(R.id.add);
        lvAlarmsList = (ListView) findViewById(R.id.alarms_list);

        // Инициализация БД
        AlarmsOpenHelper.init(this);
        alarmsDb = AlarmsOpenHelper.getAlarms();
        alarmAdapter = new AlarmAdapter(this);
        alarms = alarmAdapter.transformAlarms(alarmsDb);
        lvAlarmsList.setAdapter(alarmAdapter);

        intent = new Intent(AlarmClockActivity.this, AlarmReceiver.class);

        lvAlarmsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Alarm alarm = (Alarm) alarmAdapter.getItem(position);

                currentAlarmId = alarm.id;
                Intent prefIntent = new Intent(AlarmClockActivity.this, AlarmPreferences.class);
                prefIntent.putExtra("alarm", alarm);
                startActivityForResult(prefIntent, 0);
            }
        });

        // TODO: move to AlertActivity
        btnAlarmOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingIntent = PendingIntent.getBroadcast(AlarmClockActivity.this, 0,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.cancel(pendingIntent);

                intent.putExtra("Extra", "alarm off");
                sendBroadcast(intent);

                showNotify(getResources().getString(R.string.alarmTurnedOff));
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AlarmClockActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar chosenTime = Calendar.getInstance();
                        chosenTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        chosenTime.set(Calendar.MINUTE, minute);

                        Date chosenDate = chosenTime.getTime();
                        Date currentDate = currentTime.getTime();

                        currentTime.set(Calendar.SECOND, 0);
                        currentTime.set(Calendar.MILLISECOND, 0);

                        if (chosenDate.getTime() - currentDate.getTime() <= 0) {
                            chosenTime.add(Calendar.DATE, 1);
                            chosenDate = chosenTime.getTime();
                        }

                        // Сохранение будильника
                        Alarm alarm = new Alarm();
                        alarm.setAlarmTime(chosenTime);
                        alarm.setAlarmTitle(chosenDate);
                        AlarmsOpenHelper.create(alarm);

                        // Установка
                        setAlarm(hourOfDay, minute);

                        // Обновление списка
                        alarmsDb = AlarmsOpenHelper.getAlarms();
                        alarms = alarmAdapter.transformAlarms(alarmsDb);
                        alarmAdapter.setAlarms(alarms);
                        alarmAdapter.notifyDataSetChanged();

                        String remainingTime = calculateRemainingTime(chosenDate, currentDate);
                        showNotify("Будильник зазвонит через " + remainingTime);
                    }
                }, hour, minute, true);

                mTimePicker.setButton(TimePickerDialog.BUTTON_NEGATIVE, "Отмена", mTimePicker);
                mTimePicker.setButton(TimePickerDialog.BUTTON_POSITIVE, "ОК", mTimePicker);
                mTimePicker.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data.getExtras().get("alarm") != null) {
                Alarm resultAlarm = (Alarm) data.getExtras().get("alarm");
                AlarmsOpenHelper.update(resultAlarm, currentAlarmId);
                alarmsDb = AlarmsOpenHelper.getAlarms();
                alarms = alarmAdapter.transformAlarms(alarmsDb);
                alarmAdapter.setAlarms(alarms);
                alarmAdapter.notifyDataSetChanged();
            }
        }
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

        pendingIntent = PendingIntent.getBroadcast(AlarmClockActivity.this,
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                alarmTime.getTimeInMillis(), 1000 * 60 * 1, pendingIntent);
    }

    /*
    Uri??

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] columns = new String[] {
                AlarmsOpenHelper.COLUMN_ALARM_ID,
                AlarmsOpenHelper.COLUMN_ALARM_TITLE,
                AlarmsOpenHelper.COLUMN_ALARM_TIME,
                AlarmsOpenHelper.COLUMN_ALARM_DAYS};

        return new CursorLoader(this, alarms.getNotificationUri(), columns, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        alarmAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        alarmAdapter.swapCursor(null);
    }*/
}

