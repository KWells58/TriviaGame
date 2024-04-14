package edu.floridapoly.securesoftware.spring24.triviagame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewUsername;
    private Button buttonChangePassword;
    private Button buttonShowScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewUsername = findViewById(R.id.textViewUsername);
        buttonChangePassword = findViewById(R.id.buttonChangePassword);
        buttonShowScores = findViewById(R.id.buttonShowScores);

        // Retrieve username from shared prefrences
        SharedPreferences preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String username = preferences.getString("username", "");
        textViewUsername.setText("Username: " + username);

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        buttonShowScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ScoresActivity
                Intent intent = new Intent(ProfileActivity.this, ScoresActivity.class);
                startActivity(intent);
            }
        });
    }
}
