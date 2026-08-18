# Android Event Tracking App

An Android event management application developed in Java using Android Studio and SQLite. The application allows users to create an account, manage events, organize events by category, navigate an interactive calendar, and configure SMS reminder permissions.

## Application Screenshots

### Login
<img width="180" height="326" alt="login screen" src="https://github.com/user-attachments/assets/f0f9155b-7254-4952-9a25-00dff33284e4" /> 


### Event Calendar
<img width="191" height="431" alt="Event Calendar" src="https://github.com/user-attachments/assets/ddd26de5-325a-4275-a1b4-347fd04adb13" />


### Create Event
<img width="188" height="430" alt="Create Event" src="https://github.com/user-attachments/assets/e07e6deb-894d-4dd0-a0ad-807e1d53c8cd" />


### Saved Events
<img width="193" height="425" alt="Saved Events" src="https://github.com/user-attachments/assets/2e8c4d10-c475-45e3-9ee7-857224213124" />


### SMS Notifications
<img width="184" height="423" alt="Reminders" src="https://github.com/user-attachments/assets/82e903e6-9dd5-4421-bb4d-823a37c3e8cc" />


## Features

- User account creation and login
- SQLite database for persistent data storage
- Create, view, update, and delete events
- Store event details including:
  - Event title
  - Date
  - Time
  - Location
  - Reminder
  - Category
- Organize events into School, Work, and Personal categories
- Filter events by category
- Navigate between previous and upcoming calendar months
- Dynamic calendar that updates dates based on the selected month
- Optional SMS reminder permissions
- Sign-out functionality
- Scrollable event list for viewing saved events

## Technologies Used

- Java
- Android Studio
- Android SDK
- SQLite
- XML
- ConstraintLayout
- Android Emulator

## Database Design

The application uses SQLite for local data storage.

The database contains tables for:

### Users
Stores account information used for authentication.

- User ID
- Username
- Password

### Events
Stores information associated with saved events.

- Event ID
- Title
- Date
- Time
- Place
- Reminder
- Category

The application performs CRUD operations on event data, allowing users to create, read, update, and delete records.

## Application Structure

The application is organized into several Android activities:

- **MainActivity** – Handles user login and account creation.
- **EventListActivity** – Displays the calendar and saved events and provides access to event management features.
- **CreateEventActivity** – Allows users to create new events or edit existing events.
- **NotificationActivity** – Handles SMS notification preferences and permission requests.
- **DatabaseHelper** – Manages the SQLite database, tables, queries, and CRUD operations.

## What I Learned

This project provided me with hands-on experience developing a complete Android application while also learning how apps come together to create something functional and useful. Throughout each week, I learned how to work with data storage and apply useful elements into my event-tracking app. 
Through the project, I gained experience with:
- Designing Android interfaces using XML
- Developing Android functionality with Java
- Implementing SQLite database operations
- Performing CRUD operations
- Managing multiple Android activities
- Passing data between activities using Intents
- Handling runtime permissions
- Creating dynamic calendar functionality
- Debugging application and emulator issues
- Designing an application around user requirements

## Future Improvements

Potential future improvements include:

- Date and time picker components
- Visual indicators for events on calendar dates
- More advanced reminder scheduling
- Additional event categories
- Improved authentication security
- Enhanced UI customization
- Cloud synchronization across devices

## Author

**Elba Alvarado**  
Computer Science Student  
Southern New Hampshire University
