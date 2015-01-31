package com.example.reminders.app.dal;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import com.example.reminders.app.Constants;

public class DalFactory extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = Constants.DB_VERSION;
    // Database Name
    private static final String DATABASE_NAME = Constants.DB_NAME;

    private ReminderNoteDal reminderNoteDal;


    //or via guice provider
    public DalFactory(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        reminderNoteDal = new ReminderNoteDal(context);
        this.getReadableDatabase(); //just a trigger
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        //db.execSQL("PRAGMA foreign_keys=ON;");
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON;");
        reminderNoteDal.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        reminderNoteDal.onUpgrade(sqLiteDatabase, i, i1);
    }




}
