package com.gesworthy.pebblepedometer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gesworthy.pebblepedometer.model.DayRecord;

/**
 * Created by Gary on 2/8/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "pebblestep";

    // Contacts table name
    private static final String TABLE_STEPS = "steps";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_STEPS = "steps";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void addRecord(DayRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, record.getDate());
        values.put(KEY_STEPS, record.getSteps());

        // Inserting Row
        db.insert(TABLE_STEPS, null, values);
        db.close(); // Closing database connection
    }

    public DayRecord getRecord(long date) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_STEPS, new String[]{KEY_ID,
                KEY_DATE, KEY_STEPS}, KEY_DATE + "=?",
                new String[]{String.valueOf(date)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DayRecord record = new DayRecord(Integer.parseInt(cursor.getString(0)),
                cursor.getLong(1), cursor.getInt(2));

        return record;
    }

    public int updateRecord(DayRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, record.getDate());
        values.put(KEY_STEPS, record.getSteps());

        // updating row
        return db.update(TABLE_STEPS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(record.getId())});
    }

    public void deleteContact(DayRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STEPS, KEY_ID + " = ?",
                new String[]{String.valueOf(record.getId())});
        db.close();
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STEPS_TABLE = "CREATE TABLE " + TABLE_STEPS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " LONG,"
                + KEY_STEPS + " INTEGER" + ")";
        db.execSQL(CREATE_STEPS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS);

        // Create tables again
        onCreate(db);
    }
}
