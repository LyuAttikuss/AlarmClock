package com.learning.alarmclock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class AlarmPreferences extends AppCompatActivity {
    private AlarmAdapter alarmAdapter;

    private RadioGroup btnType;
    private Button chkMonday;
    private CheckBox chkTuesday;
    private CheckBox chkWednesday;
    private CheckBox chkThursday;
    private CheckBox chkFriday;
    private CheckBox chkSaturday;
    private CheckBox chkSunday;
    private Button btnSave;
    Alarm alarm;
    Integer[] alarmDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_preferences);

        Intent intent = getIntent();
        alarm = (Alarm) intent.getExtras().get("alarm");

        chkMonday = (CheckBox) findViewById(R.id.monday);
        chkTuesday = (CheckBox) findViewById(R.id.tuesday);
        chkWednesday = (CheckBox) findViewById(R.id.wednesday);
        chkThursday = (CheckBox) findViewById(R.id.thursday);
        chkFriday = (CheckBox) findViewById(R.id.friday);
        chkSaturday = (CheckBox) findViewById(R.id.saturday);
        chkSunday = (CheckBox) findViewById(R.id.sunday);
        btnSave = (Button) findViewById(R.id.save);

        btnType = (RadioGroup) findViewById(R.id.alarm_type);
        btnType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                View typeGroup = group.findViewById(checkedId);
                int index = group.indexOfChild(typeGroup);
                Integer[] days = {};

                // TODO: Установить будильники по дням
                switch (index) {
                    case 0:
                        alarm.setDays(days);
                        alarm.setFrequency("Один раз");
                        break;
                    case 1:
                        days = new Integer[]{0, 1, 2, 3, 4, 5, 6};
                        alarm.setDays(days);
                        alarm.setFrequency("Ежедневно");
                        break;
                    case 2:
                        days = new Integer[]{0, 1, 2, 3, 4};
                        alarm.setDays(days);
                        alarm.setFrequency("Дни недели");
                        break;
                    case 3:
                        days = new Integer[]{5, 6};
                        alarm.setDays(days);
                        alarm.setFrequency("Выходные");
                        break;
                    case 4:
                        days = new Integer[]{};
                        alarm.setDays(days);
                        alarm.setFrequency("Еженедельно");
                        break;
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("alarm", alarm);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

}
