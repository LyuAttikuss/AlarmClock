package com.learning.alarmclock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

public class AlarmPreferences extends AppCompatActivity {
    private RadioGroup btnType;
    Alarm alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_preferences);

        Intent intent = getIntent();
        alarm = (Alarm) intent.getExtras().get("alarm");

        btnType = (RadioGroup) findViewById(R.id.alarm_type);
        btnType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                Integer[] days = {};

                switch (checkedId) {
                    case 0:
                        alarm.setDays(days);
                        alarm.setFrequency("Один раз");
                    case 1:
                        days = new Integer[]{0, 1, 2, 3, 4, 5, 6};
                        alarm.setDays(days);
                        alarm.setFrequency("Ежедневно");
                    case 2:
                        days = new Integer[]{0, 1, 2, 3, 4};
                        alarm.setDays(days);
                        alarm.setFrequency("Дни недели");
                    case 3:
                        days = new Integer[]{5, 6};
                        alarm.setDays(days);
                        alarm.setFrequency("Выходные");
                    case 4:
                        days = new Integer[]{};
                        alarm.setDays(days);
                        alarm.setFrequency("Еженедельно");
                }
            }
        });

    }

}
