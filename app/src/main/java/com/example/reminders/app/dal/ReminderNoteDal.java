package com.example.reminders.app.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.reminders.app.Constants;
import com.example.reminders.app.dal.dto.ReminderNote;
import com.google.inject.Inject;
import roboguice.util.Strings;

import java.util.ArrayList;

public class ReminderNoteDal extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = Constants.DB_VERSION;
    // Database Name
    private static final String DATABASE_NAME = Constants.DB_NAME;

    public static final String REMINDER_NOTE_TABLE = "REMINDER_NOTE_TABLE";

    @Inject
    public ReminderNoteDal(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BLOCKED_PHONES_TABLE =
                "CREATE TABLE "+ REMINDER_NOTE_TABLE +
                        " ( "+ KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL DEFAULT 1, "
                        + KEY_NOTE + " TEXT,"
                        + KEY_AUTHOR+" TEXT )";

        db.execSQL(CREATE_BLOCKED_PHONES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.w("MyAppTag", "Updating database from version " + oldVersion
                    + " to " + newVersion + " .Existing data will be lost.");
            // Drop older books table if existed
            db.execSQL("DROP TABLE IF EXISTS " + REMINDER_NOTE_TABLE);

            // create fresh books table
            this.onCreate(db);
        }

    }


    private static final String KEY_ID = "KEY_ID";
    private static final String KEY_NOTE = "KEY_NOTE";
    private static final String KEY_AUTHOR = "KEY_AUTHOR";

    public long addItem(ReminderNote reminderNote) {
        Log.d(Constants.LOGGER_TAG, "add saved-offer");
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        //values.put(KEY_ID, phone.id);
        values.put(KEY_NOTE, reminderNote.note);
        values.put(KEY_AUTHOR, reminderNote.author);

        // 3. insert
        long newRowId =
                db.insertWithOnConflict(REMINDER_NOTE_TABLE, KEY_ID,
                values, SQLiteDatabase.CONFLICT_IGNORE);

        if (newRowId > 0) {
            final String text = String.format("item was added to table: %s",
                    REMINDER_NOTE_TABLE);
            Log.d(Constants.LOGGER_TAG, text);
        }

        // 4. close
        db.close();
        return newRowId;
    }

    public long updateItem(ReminderNote reminderNote) {
        Log.d(Constants.LOGGER_TAG, "add saved-offer");
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_NOTE, reminderNote.note);
        values.put(KEY_AUTHOR, reminderNote.author);

        // 3. update
        long newRowId =
                db.update(REMINDER_NOTE_TABLE, values,
                        KEY_ID + " = ?",
                        new String[]{String.valueOf(reminderNote.id)});

        if (newRowId > 0) {
            final String text = String.format("item was updated in table: %s",
                    REMINDER_NOTE_TABLE);
            Log.d(Constants.LOGGER_TAG, text);
        }

        // 4. close
        db.close();
        return newRowId;
    }


    public ArrayList<ReminderNote> getAllRegexItems(String author) {
        ArrayList<ReminderNote> results = new ArrayList<>();

        Cursor cursor = this.getReadableDatabase().query(
                REMINDER_NOTE_TABLE,
                new String[] { KEY_ID, KEY_NOTE, KEY_AUTHOR}, KEY_AUTHOR+" = ?",
                Strings.isEmpty(author)?  new String[] { "%*%" } : new String[] { "%"+author+"%" }, null, null, null);

        while(cursor.moveToNext()) {
            ReminderNote result = new ReminderNote();
            result.id = cursor.getInt(0);
            result.note = cursor.getString(1);
            result.author = cursor.getString(2);
            results.add(result);
        }
        return results;
    }

}
