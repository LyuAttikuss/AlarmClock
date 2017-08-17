package com.learning.alarmclock;


import java.io.Serializable;
import java.util.Calendar;

public class Alarm implements Serializable {
    public String alarmName = "";
    public Calendar alarmTime = Calendar.getInstance();
    public Integer[] days = {0, 1, 2, 3, 4, 5, 6};

    public void getItem() {

    }
}
