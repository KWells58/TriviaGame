package edu.floridapoly.securesoftware.spring24.triviagame;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AuthManager {

    private Context context;
    private SharedPreferences sharedPreferences;

    private static final String USER_PREFS = "UserPrefs";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    public AuthManager(Context context) {

        this.context = context;
    }

    public boolean createAccount(String username, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        // Check if username already exists
        if (sharedPreferences.contains(username)) {
            return false; // Username already exists
        }
        // Validate password
        if (!isPasswordValid(password)) {
            return false; // Password not valid
        }
        // Save username and hashed password in SharedPreferences
        sharedPreferences.edit().putString(username, password).apply();
        return true;
    }

    public boolean login(String username, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        // Retrieve stored password for the given username
        String storedPassword = sharedPreferences.getString(username, null);
        // Compare stored password with the provided password
        return (storedPassword != null && storedPassword.equals(password));
    }


    private boolean isPasswordValid(String password) {
        // Password must be at least 10 characters long
        return password.length() >= 10;
    }


    public boolean changePassword(String username, String currentPassword, String newPassword) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Check if current password is correct
        String storedPasswordBeforeChange = sharedPreferences.getString(username, null);
        Log.d("ChangePassword", "Stored password before change: " + storedPasswordBeforeChange);

        if (storedPasswordBeforeChange == null || !storedPasswordBeforeChange.equals(currentPassword)) {
            Log.d("ChangePassword", "Current password incorrect");
            return false; // Current password incorrect
        }

        // Validate new password
        if (!isPasswordValid(newPassword)) {
            Log.d("ChangePassword", "New password not valid");
            return false; // New password not valid
        }

        // Save the new password
        editor.putString(username, newPassword);
        editor.apply();

        // Check the stored password after the change
        String storedPasswordAfterChange = sharedPreferences.getString(username, null);
        Log.d("ChangePassword", "Stored password after change: " + storedPasswordAfterChange);

        return true;
    }
}

