package com.learning.alarmclock;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Alarm implements Serializable {
    public long id;
    public String title;
    public Calendar time = Calendar.getInstance();
    public Integer[] days = {0, 1, 2, 3, 4, 5, 6};
    public String frequency = "Один раз";
    public Boolean isActive = false;

    public String getAlarmTitle() {
        return title;
    }

    public Calendar getAlarmTime() {
        return time;
    }

    public void setAlarmTime(Calendar chosenTime) {
        this.time = chosenTime;
    }

    public void setAlarmTitle(Date chosenDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        this.title = formatter.format(chosenDate);
    }

    public void setDays(Integer[] days) {
        this.days = days;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFrequency() {
        return frequency;
    }
}
