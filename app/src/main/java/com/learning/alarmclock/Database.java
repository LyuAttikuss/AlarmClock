package com.learning.alarmclock;

import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Database extends SQLiteOpenHelper {
    static String DATABASE_NAME = "DB";
    static int DATABASE_VERSION = 1;
    static SQLiteDatabase database = null;
    static Database instance = null;

    public static String TABLE_NAME = "alarm";
    public static String COLUMN_ALARM_ID = "_id";
    public static String COLUMN_ALARM_TITLE = "alarm_title";
    public static String COLUMN_ALARM_TIME = "alarm_time";
    public static String COLUMN_ALARM_DAYS = "alarm_days";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new Database(context);
        }
    }

    public static SQLiteDatabase getDatabase() {
        if (database == null) {
            database = instance.getWritableDatabase();
        }

        return database;
    }

    public static long create(Alarm alarm) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ALARM_TITLE, alarm.getAlarmTitle());
        cv.put(COLUMN_ALARM_TIME, alarm.getAlarmTitle());
        cv.put(COLUMN_ALARM_DAYS, alarm.getFrequency());

        return getDatabase().insert(TABLE_NAME, null, cv);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                        + COLUMN_ALARM_ID + " INTEGER primary key autoincrement, "
                        + COLUMN_ALARM_TITLE + " TEXT NOT NULL, "
                        + COLUMN_ALARM_TIME + "TEXT NOT NULL, "
                        + COLUMN_ALARM_DAYS + "TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
