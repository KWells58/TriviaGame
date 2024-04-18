package edu.floridapoly.securesoftware.spring24.triviagame;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthManager {

    private Context context;
    private SharedPreferences sharedPreferences;

    private static final String USER_PREFS = "UserPrefs";

    public AuthManager(Context context) {
        this.context = context;
        initializeSharedPreferences();
    }

    private void initializeSharedPreferences() {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            sharedPreferences = EncryptedSharedPreferences.create(
                    USER_PREFS,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            Log.e("AuthManager", "Failed to initialize EncryptedSharedPreferences", e);
            throw new RuntimeException("Could not initialize EncryptedSharedPreferences", e);
        }
    }

    public boolean createAccount(String username, String password) {
        if (sharedPreferences.contains(username)) {
            return false; // Username already exists
        }
        if (!isPasswordValid(password)) {
            return false; // Password not valid
        }
        String hashedPassword = hashPassword(password);
        sharedPreferences.edit().putString(username, hashedPassword).apply();
        return true;
    }

    public boolean login(String username, String password) {
        String storedHashedPassword = sharedPreferences.getString(username, null);
        String hashedPassword = hashPassword(password);
        return (storedHashedPassword != null && storedHashedPassword.equals(hashedPassword));
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 10; // Add more complexity checks as necessary
    }

    public boolean changePassword(String username, String currentPassword, String newPassword) {
        String storedHashedPassword = sharedPreferences.getString(username, null);
        if (storedHashedPassword == null || !storedHashedPassword.equals(hashPassword(currentPassword))) {
            return false; // Current password incorrect
        }
        if (!isPasswordValid(newPassword)) {
            return false; // New password not valid
        }
        sharedPreferences.edit().putString(username, hashPassword(newPassword)).apply();
        return true;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(encodedhash, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            Log.e("AuthManager", "Failed to hash password", e);
            return null;
        }
    }
}
