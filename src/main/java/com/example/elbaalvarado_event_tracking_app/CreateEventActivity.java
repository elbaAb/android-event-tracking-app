package com.example.elbaalvarado_event_tracking_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class CreateEventActivity extends AppCompatActivity {
    private int eventId = -1;
    private EditText inputEventTitle;
    private EditText inputEventDate;
    private EditText inputEventTime;
    private EditText inputEventPlace;
    private EditText inputEventReminder;

    private Button buttonSaveEvent;

    private DatabaseHelper databaseHelper;
    private RadioGroup categoryGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connects this activity to the Create Event layout
        setContentView(R.layout.activity_create_event);

        // Connects Java variables to XML input fields
        inputEventTitle = findViewById(R.id.inputEventTitle);
        inputEventDate = findViewById(R.id.inputEventDate);
        inputEventTime = findViewById(R.id.inputEventTime);
        inputEventPlace = findViewById(R.id.inputEventPlace);
        inputEventReminder = findViewById(R.id.inputEventReminder);

        buttonSaveEvent = findViewById(R.id.buttonSaveEvent);

        // Checks whether an existing event is being edited
        Intent intent = getIntent();

        eventId = intent.getIntExtra("eventId", -1);

        if (eventId != -1) {

            inputEventTitle.setText(intent.getStringExtra("title"));
            inputEventDate.setText(intent.getStringExtra("date"));
            inputEventTime.setText(intent.getStringExtra("time"));
            inputEventPlace.setText(intent.getStringExtra("place"));
            inputEventReminder.setText(intent.getStringExtra("reminder"));

            buttonSaveEvent.setText("Update Event");
        }

        // Connects this activity to the SQLite database
        databaseHelper = new DatabaseHelper(this);

        // Saves the event when the button is clicked
        buttonSaveEvent.setOnClickListener(view -> saveEvent());

        categoryGroup = findViewById(R.id.categoryGroup);
    }

    // Saves the event information into the database
    private void saveEvent() {

        // Gets the information entered by the user
        String title = inputEventTitle.getText().toString().trim();
        String date = inputEventDate.getText().toString().trim();
        String time = inputEventTime.getText().toString().trim();
        String place = inputEventPlace.getText().toString().trim();
        String reminder = inputEventReminder.getText().toString().trim();

        String category = "None";

        int selectedCategoryId = categoryGroup.getCheckedRadioButtonId();

        if (selectedCategoryId != -1) {

            RadioButton selectedCategory =
                    findViewById(selectedCategoryId);

            category = selectedCategory.getText().toString();
        }

        // Prevents saving an event without required information
        if (title.isEmpty() || date.isEmpty() || time.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please enter an event title, date, and time.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        boolean success;

        // If there is no event ID, create a new event
        if (eventId == -1) {

            success = databaseHelper.addEvent(
                    title,
                    date,
                    time,
                    place,
                    reminder,
                    category
            );

        } else {

            // If an event ID exists, update that event
            success = databaseHelper.updateEvent(
                    eventId,
                    title,
                    date,
                    time,
                    place,
                    reminder,
                    category
            );
        }

        if (success) {

            // Displays the appropriate message
            if (eventId == -1) {

                Toast.makeText(
                        this,
                        "Event saved successfully.",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                Toast.makeText(
                        this,
                        "Event updated successfully.",
                        Toast.LENGTH_SHORT
                ).show();
            }

            // Returns to the calendar
            Intent intent = new Intent(
                    CreateEventActivity.this,
                    EventListActivity.class
            );

            startActivity(intent);
            finish();

        } else {

            Toast.makeText(
                    this,
                    "Unable to save event.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}