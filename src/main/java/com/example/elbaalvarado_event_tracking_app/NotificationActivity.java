package com.example.elbaalvarado_event_tracking_app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class NotificationActivity extends AppCompatActivity {

    private Button buttonAllowSMS;
    private Button buttonNotNow;
    private EditText inputPhoneNumber;

    // Handles the result of the SMS permission request
    private final ActivityResultLauncher<String> smsPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {

                        if (isGranted) {

                            Toast.makeText(
                                    this,
                                    "SMS permission granted.",
                                    Toast.LENGTH_SHORT
                            ).show();

                            sendEventReminder();

                        } else {

                            Toast.makeText(
                                    this,
                                    "SMS permission denied. The app will continue without SMS notifications.",
                                    Toast.LENGTH_LONG
                            ).show();

                            finish();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connects this activity to the notification layout
        setContentView(R.layout.activity_notification);

        // Connects Java variables to XML elements
        buttonAllowSMS = findViewById(R.id.buttonAllowSMS);
        buttonNotNow = findViewById(R.id.buttonNotNow);
        inputPhoneNumber = findViewById(R.id.inputPhoneNumber);

        // Checks or requests SMS permission
        buttonAllowSMS.setOnClickListener(view -> checkSmsPermission());

        // Continues without SMS notifications
        buttonNotNow.setOnClickListener(view -> {

            Toast.makeText(
                    this,
                    "SMS notifications will remain disabled.",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        });
    }

    // Checks whether SEND_SMS permission has already been granted
    private void checkSmsPermission() {

        String phoneNumber =
                inputPhoneNumber.getText().toString().trim();

        // Prevents SMS setup without a phone number
        if (phoneNumber.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please enter a phone number.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED) {

            sendEventReminder();

        } else {

            // Requests permission from the user
            smsPermissionLauncher.launch(
                    Manifest.permission.SEND_SMS
            );
        }
    }

    // Sends an SMS reminder for an upcoming event
    private void sendEventReminder() {

        String phoneNumber =
                inputPhoneNumber.getText().toString().trim();

        String message =
                "Event Tracker Reminder: You have an upcoming event.";

        try {

            SmsManager smsManager = SmsManager.getDefault();

            smsManager.sendTextMessage(
                    phoneNumber,
                    null,
                    message,
                    null,
                    null
            );

            Toast.makeText(
                    this,
                    "Event reminder SMS sent.",
                    Toast.LENGTH_SHORT
            ).show();

            // Closes the notification screen and returns to the calendar
            finish();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Unable to send SMS on this device.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}