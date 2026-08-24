package com.example.elbaalvarado_event_tracking_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Login screen elements
    private EditText usernameText;
    private EditText passwordText;
    private Button buttonLogin;
    private Button buttonCreateAccount;

    // Database connection
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Keeps the layout from overlapping system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );
            return insets;
        });

        // Connects Java variables to the XML login elements
        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        // Connects the activity to the SQLite database
        databaseHelper = new DatabaseHelper(this);

        // Attempts to log in when the Login button is clicked
        buttonLogin.setOnClickListener(view -> loginUser());

        // Creates a new account when the Create Account button is clicked
        buttonCreateAccount.setOnClickListener(view -> createAccount());
    }

    // Checks the entered username and password against the database
    private void loginUser() {

        String username = usernameText.getText().toString().trim();
        String password = passwordText.getText().toString();

        // Prevents empty login attempts
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                    this,
                    "Please enter a username and password.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Checks if the username and password exist in the database
        if (databaseHelper.checkUser(username, password)) {

            Toast.makeText(
                    this,
                    "Login successful.",
                    Toast.LENGTH_SHORT
            ).show();

            // Opens the event calendar screen
            Intent intent =
                    new Intent(MainActivity.this, EventListActivity.class);

            startActivity(intent);

        } else {

            Toast.makeText(
                    this,
                    "Incorrect username or password.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // Creates a new username and password in the database
    private void createAccount() {

        String username = usernameText.getText().toString().trim();
        String password = passwordText.getText().toString();

        // Prevents accounts with blank fields
        if (username.isEmpty() || password.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please enter a username and password.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Attempts to save the account to the database
        boolean accountCreated =
                databaseHelper.addUser(username, password);

        if (accountCreated) {

            Toast.makeText(
                    this,
                    "Account created successfully.",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            Toast.makeText(
                    this,
                    "That username already exists.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}