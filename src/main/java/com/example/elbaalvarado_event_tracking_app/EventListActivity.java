package com.example.elbaalvarado_event_tracking_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class EventListActivity extends AppCompatActivity {

    // Connects to the SQLite database
    private DatabaseHelper databaseHelper;

    // Container where saved events are displayed
    private LinearLayout eventListContainer;

    // Stores the currently selected category filter
    private String currentCategory = "All Events";

    // Stores the month currently displayed on the calendar
    private Calendar currentCalendar;

    // Displays the current month and year
    private TextView textCurrentMonth;

    // Stores the 42 calendar day cells
    // 6 weeks x 7 days = 42 possible cells
    private final TextView[] dayViews = new TextView[42];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connects this activity to the event calendar XML
        setContentView(R.layout.activity_event_list);



        // CONNECT BUTTONS FROM XML

        Button buttonCreateEvent =
                findViewById(R.id.buttonCreateEvent);

        Button buttonReminders =
                findViewById(R.id.buttonRemindersFilter);

        Button buttonCategories =
                findViewById(R.id.buttonCategoriesFilter);

        Button buttonSignOut =
                findViewById(R.id.buttonSignOut);

        ImageButton buttonPreviousMonth =
                findViewById(R.id.buttonPreviousMonth);

        ImageButton buttonNextMonth =
                findViewById(R.id.buttonNextMonth);



        // CONNECT OTHER UI ELEMENTS

        // Month title
        textCurrentMonth =
                findViewById(R.id.textCurrentMonth);

        // Upcoming Events container
        eventListContainer =
                findViewById(R.id.eventListContainer);





// Stores the XML IDs for all 42 calendar cells
        int[] dayIds = {
                R.id.day1, R.id.day2, R.id.day3, R.id.day4,
                R.id.day5, R.id.day6, R.id.day7,

                R.id.day8, R.id.day9, R.id.day10, R.id.day11,
                R.id.day12, R.id.day13, R.id.day14,

                R.id.day15, R.id.day16, R.id.day17, R.id.day18,
                R.id.day19, R.id.day20, R.id.day21,

                R.id.day22, R.id.day23, R.id.day24, R.id.day25,
                R.id.day26, R.id.day27, R.id.day28,

                R.id.day29, R.id.day30, R.id.day31, R.id.day32,
                R.id.day33, R.id.day34, R.id.day35,

                R.id.day36, R.id.day37, R.id.day38, R.id.day39,
                R.id.day40, R.id.day41, R.id.day42
        };

// Connects each calendar TextView to its XML ID
        for (int i = 0; i < dayIds.length; i++) {
            dayViews[i] = findViewById(dayIds[i]);
        }


        // Starts the calendar on the current month
        currentCalendar = Calendar.getInstance();



        // CONNECT TO DATABASE
        databaseHelper = new DatabaseHelper(this);


        // CREATE EVENT BUTTON
        buttonCreateEvent.setOnClickListener(view -> {

            Intent intent = new Intent(
                    EventListActivity.this,
                    CreateEventActivity.class
            );

            startActivity(intent);
        });



        // REMINDERS BUTTON
        buttonReminders.setOnClickListener(view -> {

            Intent intent = new Intent(
                    EventListActivity.this,
                    NotificationActivity.class
            );

            startActivity(intent);
        });



        // CATEGORIES BUTTON
        buttonCategories.setOnClickListener(view -> {

            showCategoryFilter();
        });



        // SIGN OUT BUTTON
        buttonSignOut.setOnClickListener(view -> {

            Intent intent = new Intent(
                    EventListActivity.this,
                    MainActivity.class
            );

            // Clears previous activities so Back cannot
            // reopen the calendar after signing out
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
        });



        // PREVIOUS MONTH BUTTON
        buttonPreviousMonth.setOnClickListener(view -> {

            // Moves calendar back one month
            currentCalendar.add(
                    Calendar.MONTH,
                    -1
            );

            updateCalendar();
        });



        // NEXT MONTH BUTTON
        buttonNextMonth.setOnClickListener(view -> {

            // Moves calendar forward one month
            currentCalendar.add(
                    Calendar.MONTH,
                    1
            );

            updateCalendar();
        });



        // INITIAL DISPLAY
        // Displays the current month
        updateCalendar();

        // Displays saved events
        displayEvents();
    }



    // UPDATE CALENDAR
    // Updates the month title and day numbers
    private void updateCalendar() {

        int month =
                currentCalendar.get(Calendar.MONTH);

        int year =
                currentCalendar.get(Calendar.YEAR);


        // Gets the month name
        String monthName =
                new DateFormatSymbols()
                        .getMonths()[month];


        // Displays month and year
        textCurrentMonth.setText(
                monthName + " " + year
        );


        // Clears all 42 calendar cells
        for (TextView dayView : dayViews) {

            if (dayView != null) {
                dayView.setText("");
            }
        }


        // Creates a temporary calendar for the selected month
        Calendar monthCalendar =
                (Calendar) currentCalendar.clone();

        // Moves it to the first day of the month
        monthCalendar.set(
                Calendar.DAY_OF_MONTH,
                1
        );


        // Determines the weekday where the month begins
        int firstDayOfWeek =
                monthCalendar.get(
                        Calendar.DAY_OF_WEEK
                );


        // Determines how many days are in the month
        int daysInMonth =
                monthCalendar.getActualMaximum(
                        Calendar.DAY_OF_MONTH
                );


        // Sunday = 1 in Calendar.DAY_OF_WEEK,
        // so subtract 1 for array indexing
        int startingPosition =
                firstDayOfWeek - 1;


        // Places each date into the correct calendar cell
        for (int day = 1;
             day <= daysInMonth;
             day++) {

            int position =
                    startingPosition + day - 1;

            if (position < dayViews.length &&
                    dayViews[position] != null) {

                dayViews[position].setText(
                        String.valueOf(day)
                );
            }
        }
    }



    // CATEGORY FILTER
    // Displays a popup containing category options
    private void showCategoryFilter() {

        String[] categories = {
                "All Events",
                "School",
                "Work",
                "Personal"
        };

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("Choose Category");

        builder.setItems(
                categories,
                (dialog, which) -> {

                    // Saves selected category
                    currentCategory =
                            categories[which];

                    // Refreshes events using filter
                    displayEvents();
                }
        );

        builder.show();
    }



    // DISPLAY SAVED EVENTS
    private void displayEvents() {

        // Clears the previous event list
        eventListContainer.removeAllViews();

        // Reads all events from SQLite
        Cursor cursor =
                databaseHelper.getAllEvents();


        // Goes through every saved event
        while (cursor.moveToNext()) {



            // READ EVENT INFORMATION
            int eventId = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
            );

            String title = cursor.getString(
                    cursor.getColumnIndexOrThrow("title")
            );

            String date = cursor.getString(
                    cursor.getColumnIndexOrThrow("date")
            );

            String time = cursor.getString(
                    cursor.getColumnIndexOrThrow("time")
            );

            String place = cursor.getString(
                    cursor.getColumnIndexOrThrow("place")
            );

            String reminder = cursor.getString(
                    cursor.getColumnIndexOrThrow("reminder")
            );

            String category = cursor.getString(
                    cursor.getColumnIndexOrThrow("category")
            );



            // APPLY CATEGORY FILTER

            // Skip events outside the selected category
            if (!currentCategory.equals("All Events") &&
                    !currentCategory.equals(category)) {

                continue;
            }



            // CREATE EVENT ROW
            LinearLayout eventRow =
                    new LinearLayout(this);

            eventRow.setOrientation(
                    LinearLayout.HORIZONTAL
            );

            eventRow.setPadding(
                    12,
                    12,
                    12,
                    28
            );



            // EVENT INFORMATION TEXT
            TextView eventText =
                    new TextView(this);

            eventText.setText(
                    title + "\n" +
                            date + " | " + time + "\n" +
                            place + "\n" +
                            "Category: " + category + "\n" +
                            "Reminder: " + reminder
            );

            eventText.setTextSize(16);

            eventText.setPadding(
                    0,
                    0,
                    0,
                    20
            );


            // Gives event text most of the row space
            LinearLayout.LayoutParams textParams =
                    new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1
                    );

            eventText.setLayoutParams(textParams);



            // EDIT BUTTON
            Button editButton =
                    new Button(this);

            editButton.setText("Edit");


            editButton.setOnClickListener(view -> {

                Intent intent = new Intent(
                        EventListActivity.this,
                        CreateEventActivity.class
                );

                // Sends current event information
                // to the Edit/Create Event screen
                intent.putExtra(
                        "eventId",
                        eventId
                );

                intent.putExtra(
                        "title",
                        title
                );

                intent.putExtra(
                        "date",
                        date
                );

                intent.putExtra(
                        "time",
                        time
                );

                intent.putExtra(
                        "place",
                        place
                );

                intent.putExtra(
                        "reminder",
                        reminder
                );

                intent.putExtra(
                        "category",
                        category
                );

                startActivity(intent);
            });



            // DELETE BUTTON
            Button deleteButton =
                    new Button(this);

            deleteButton.setText("Delete");


            deleteButton.setOnClickListener(view -> {

                boolean deleted =
                        databaseHelper.deleteEvent(
                                eventId
                        );

                // Refreshes events after deletion
                if (deleted) {
                    displayEvents();
                }
            });



            // ADD EVENT TO SCREEN
            eventRow.addView(eventText);
            eventRow.addView(editButton);
            eventRow.addView(deleteButton);

            eventListContainer.addView(eventRow);
        }


        // Closes the database cursor
        cursor.close();
    }



    // REFRESH WHEN RETURNING TO SCREEN
    @Override
    protected void onResume() {
        super.onResume();

        // Reloads saved events whenever the user
        // returns to this activity
        if (databaseHelper != null) {

            displayEvents();
        }
    }
}