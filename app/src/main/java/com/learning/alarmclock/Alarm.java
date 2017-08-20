package com.learning.alarmclock;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Alarm implements Serializable {
    public String alarmTitle;
    public Calendar alarmTime = Calendar.getInstance();
    public Integer[] days = {0, 1, 2, 3, 4, 5, 6};
    public String frequency = "Один раз";
    public Boolean isActive = false;

    public String getAlarmTitle() {
        return alarmTitle;
    }

    public Calendar getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Calendar chosenTime) {
        this.alarmTime = chosenTime;
    }

    public void setAlarmTitle(Date chosenDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        this.alarmTitle = formatter.format(chosenDate);
    }

    public void setDays(Integer[] days) {
        this.days = days;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
