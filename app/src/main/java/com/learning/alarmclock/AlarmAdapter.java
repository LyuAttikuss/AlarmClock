package com.learning.alarmclock;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class AlarmAdapter extends CursorAdapter {
    private ArrayList<Alarm> alarms = new ArrayList<>();

    public AlarmAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
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
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.alarm_item, null);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String alarmTitle = cursor.getString(cursor.getColumnIndexOrThrow("alarm_title"));
        String alarmFrequency = cursor.getString(cursor.getColumnIndexOrThrow("alarm_days"));

        TextView tvAlarmTitle = (TextView) view.findViewById(R.id.alarm_title);
        TextView tvAlarmFrecuency = (TextView) view.findViewById(R.id.alarm_days);
        CheckBox chkAlarmSwitch = (CheckBox) view.findViewById(R.id.alarm_switch);

        tvAlarmTitle.setText(alarmTitle);
        tvAlarmFrecuency.setText(alarmFrequency);
    }

    public void setAlarms(ArrayList<Alarm> alarms) {
        this.alarms = alarms;
    }
}
