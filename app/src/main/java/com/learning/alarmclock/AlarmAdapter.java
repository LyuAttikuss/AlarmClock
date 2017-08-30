package com.learning.alarmclock;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AlarmAdapter extends BaseAdapter {
    private ArrayList<Alarm> alarms = new ArrayList<>();
    private AlarmClockActivity alarmActivity;

    public AlarmAdapter(AlarmClockActivity alarmClockActivity) {
        this.alarmActivity = alarmClockActivity;
    }

    @Override
    public int getCount() {
        return alarms.size();
    }

    @Override
    public Object getItem(int position) {
        return alarms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(alarmActivity).inflate(R.layout.alarm_item, null);
        Alarm alarm = (Alarm) getItem(position);
        TextView tvAlarmTitle = (TextView) view.findViewById(R.id.alarm_title);
        TextView tvAlarmFrecuency = (TextView) view.findViewById(R.id.alarm_days);
        CheckBox chkAlarmSwitch = (CheckBox) view.findViewById(R.id.alarm_switch);

        tvAlarmTitle.setText(alarm.title);
        tvAlarmFrecuency.setText(alarm.frequency);

        return view;
    }

    public void setAlarms(ArrayList<Alarm> alarms) {
        this.alarms = alarms;
    }

    public ArrayList<Alarm> transformAlarms(Cursor cursor) {
        alarms.clear();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Alarm alarm = new Alarm();
            alarm.id = cursor.getLong(cursor.getColumnIndexOrThrow(AlarmsOpenHelper.COLUMN_ALARM_ID));
            alarm.title = cursor.getString(cursor.getColumnIndexOrThrow(AlarmsOpenHelper.COLUMN_ALARM_TITLE));
            alarm.frequency = cursor.getString(cursor.getColumnIndexOrThrow(AlarmsOpenHelper.COLUMN_ALARM_DAYS));

            alarms.add(alarm);
            cursor.moveToNext();
        }

        return alarms;
    }
}
