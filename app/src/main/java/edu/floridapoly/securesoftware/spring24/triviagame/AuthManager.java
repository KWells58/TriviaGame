package edu.floridapoly.securesoftware.spring24.triviagame;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthManager {

    private Context context;
    private SharedPreferences sharedPreferences;

    public AuthManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
    }

    public boolean createAccount(String username, String password) {
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
        // Retrieve stored password for the given username
        String storedPassword = sharedPreferences.getString(username, null);
        // Compare stored password with the provided password
        return storedPassword != null && storedPassword.equals(password);
    }

    private boolean isPasswordValid(String password) {
        // Password must be at least 10 characters long
        return password.length() >= 10;
    }
}
