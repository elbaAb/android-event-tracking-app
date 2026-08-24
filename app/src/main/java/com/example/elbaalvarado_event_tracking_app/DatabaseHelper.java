package com.example.elbaalvarado_event_tracking_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "EventTracker.db";
    private static final int DATABASE_VERSION = 3;

    // User table information
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Event table information
    private static final String TABLE_EVENTS = "events";
    private static final String COLUMN_EVENT_ID = "id";
    private static final String COLUMN_EVENT_TITLE = "title";
    private static final String COLUMN_EVENT_DATE = "date";
    private static final String COLUMN_EVENT_TIME = "time";
    private static final String COLUMN_EVENT_PLACE = "place";
    private static final String COLUMN_EVENT_REMINDER = "reminder";
    private static final String COLUMN_EVENT_CATEGORY = "category";

    // Creates the database helper
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creates database tables when the database is first created
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create users table
        String createUsersTable =
                "CREATE TABLE " + TABLE_USERS + " (" +
                        COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_USERNAME + " TEXT UNIQUE, " +
                        COLUMN_PASSWORD + " TEXT)";

        db.execSQL(createUsersTable);

        // Create events table
        String createEventsTable =
                "CREATE TABLE " + TABLE_EVENTS + " (" +
                        COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_EVENT_TITLE + " TEXT, " +
                        COLUMN_EVENT_DATE + " TEXT, " +
                        COLUMN_EVENT_TIME + " TEXT, " +
                        COLUMN_EVENT_PLACE + " TEXT, " +
                        COLUMN_EVENT_REMINDER + " TEXT, " +
                        COLUMN_EVENT_CATEGORY + " TEXT)";

        db.execSQL(createEventsTable);
    }

    // Updates the database if the version changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);

        onCreate(db);
    }

    // Adds a new username and password to the database
    public boolean addUser(String username, String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);

        return result != -1;
    }

    // Checks if the entered username and password exist in the database
    public boolean checkUser(String username, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        String query =
                "SELECT * FROM " + TABLE_USERS +
                        " WHERE " + COLUMN_USERNAME + " = ? AND " +
                        COLUMN_PASSWORD + " = ?";

        Cursor cursor = db.rawQuery(
                query,
                new String[]{username, password}
        );

        boolean userExists = cursor.getCount() > 0;

        cursor.close();

        return userExists;
    }

    // Adds a new event to the database
    public boolean addEvent(String title, String date, String time,
                            String place, String reminder, String category) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_TITLE, title);
        values.put(COLUMN_EVENT_DATE, date);
        values.put(COLUMN_EVENT_TIME, time);
        values.put(COLUMN_EVENT_PLACE, place);
        values.put(COLUMN_EVENT_REMINDER, reminder);
        values.put(COLUMN_EVENT_CATEGORY, category);

        long result = db.insert(TABLE_EVENTS, null, values);

        return result != -1;
    }

    // Returns all saved events from the database
    public Cursor getAllEvents() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_EVENTS +
                        " ORDER BY " + COLUMN_EVENT_DATE + " ASC",
                null
        );
    }

    // Deletes an event using its database ID
    public boolean deleteEvent(int eventId) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                TABLE_EVENTS,
                COLUMN_EVENT_ID + " = ?",
                new String[]{String.valueOf(eventId)}
        );

        return result > 0;
    }

    // Updates an existing event in the database
    public boolean updateEvent(int eventId, String title, String date, String time,
                               String place, String reminder, String category) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_TITLE, title);
        values.put(COLUMN_EVENT_DATE, date);
        values.put(COLUMN_EVENT_TIME, time);
        values.put(COLUMN_EVENT_PLACE, place);
        values.put(COLUMN_EVENT_REMINDER, reminder);
        values.put(COLUMN_EVENT_CATEGORY, category);


        int result = db.update(
                TABLE_EVENTS,
                values,
                COLUMN_EVENT_ID + " = ?",
                new String[]{String.valueOf(eventId)}
        );

        return result > 0;
    }
}