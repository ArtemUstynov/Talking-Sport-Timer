package com.sporttimeradfree.singlemind;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JohnGalt on 30.12.2016.
 *
 * Class to reduce bloat code and to make working with SQL easier
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "workoutDB";
    public static final String TABLE_WORKOUTS = "workouts";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_TIMES_IN_SET = "timesInSet";
    public static final String KEY_WORK_TIME = "workTime";
    public static final String KEY_REST_TIME = "restTime";
    public static final String KEY_DELAY = "delay";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_WORKOUTS +
                " ( " + KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " text, " +
                KEY_TIMES_IN_SET + " integer, " +
                KEY_WORK_TIME + " integer, " +
                KEY_REST_TIME + " integer, " +
                KEY_DELAY + " integer " + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_WORKOUTS);
        onCreate(db);
    }
}
