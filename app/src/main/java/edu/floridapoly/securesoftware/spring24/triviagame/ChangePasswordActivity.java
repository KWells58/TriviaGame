package edu.floridapoly.securesoftware.spring24.triviagame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class ChangePasswordActivity extends AppCompatActivity {

    private EditText currentPasswordEditText;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private Button changePasswordButton;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        authManager = new AuthManager(this);

        currentPasswordEditText = findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        changePasswordButton = findViewById(R.id.changePasswordButton);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }


    private void changePassword() {
        String currentPassword = currentPasswordEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        Log.d("ChangePassword", "Current password: " + currentPassword);
        Log.d("ChangePassword", "New password: " + newPassword);
        Log.d("ChangePassword", "Confirm password: " + confirmPassword);

        // Add validation for password length, match, etc.
        if (newPassword.length() < 10) {
            // Password is too short
            newPasswordEditText.setError("Password must be at least 10 characters long");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            // New password and confirm password do not match
            confirmPasswordEditText.setError("Passwords do not match");
            return;
        }

        // Call the changePassword method of AuthManager
        boolean passwordChanged = authManager.changePassword(getLoggedInUsername(), currentPassword, newPassword);
        Log.d("ChangePassword", "Password changed: " + passwordChanged);

        if (passwordChanged) {
            // Password changed successfully
            Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
            // Clear the new password field
            newPasswordEditText.setText("");
        } else {
            // Failed to change password (incorrect current password or invalid new password)
            Toast.makeText(this, "Failed to change password", Toast.LENGTH_SHORT).show();
        }
    }

    private String getLoggedInUsername() {
        SharedPreferences preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return preferences.getString("username", "");
    }

}
