package com.learning.alarmclock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmsOpenHelper extends SQLiteOpenHelper {
    static String DATABASE_NAME = "AlarmClock.db";
    static int DATABASE_VERSION = 8;
    static SQLiteDatabase database = null;
    static AlarmsOpenHelper instance = null;

    public static String TABLE_NAME = "alarm";
    public static String COLUMN_ALARM_ID = "_id";
    public static String COLUMN_ALARM_TITLE = "title";
    public static String COLUMN_ALARM_TIME = "time";
    public static String COLUMN_ALARM_DAYS = "days";
    public static String COLUMN_ALARM_IS_ACTIVE = "active";

    public AlarmsOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new AlarmsOpenHelper(context);
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

        int isActive = alarm.getIsActive() ? 0 : 1;
        cv.put(COLUMN_ALARM_IS_ACTIVE, isActive);

        return getDatabase().insert(TABLE_NAME, null, cv);
    }

    public static long update(Alarm alarm, long rowId) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ALARM_DAYS, alarm.getFrequency());

        int isActive = alarm.getIsActive() ? 0 : 1;
        cv.put(COLUMN_ALARM_IS_ACTIVE, isActive);

        String currentRow = COLUMN_ALARM_ID + " LIKE ?";
        String[] currentArgs = {String.valueOf(rowId)};

        int count = getDatabase().update(TABLE_NAME, cv, currentRow, currentArgs);
        return count;
    }

    public static Cursor getAlarms() {
        SQLiteDatabase db = instance.getReadableDatabase();

        String[] columns = {
                COLUMN_ALARM_ID,
                COLUMN_ALARM_TITLE,
                COLUMN_ALARM_TIME,
                COLUMN_ALARM_DAYS,
                COLUMN_ALARM_IS_ACTIVE
            };

        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);

        return cursor;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                        + COLUMN_ALARM_ID + " INTEGER primary key autoincrement, "
                        + COLUMN_ALARM_TITLE + " TEXT NOT NULL, "
                        + COLUMN_ALARM_TIME + " TEXT NOT NULL, "
                        + COLUMN_ALARM_DAYS + " TEXT NOT NULL, "
                        + COLUMN_ALARM_IS_ACTIVE + " INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
