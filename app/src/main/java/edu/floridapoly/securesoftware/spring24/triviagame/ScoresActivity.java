package edu.floridapoly.securesoftware.spring24.triviagame;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        // Retrieve scores from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String scores = sharedPreferences.getString("scores", "");

        // Display scores
        TextView scoresTextView = findViewById(R.id.scoresTextView);
        scoresTextView.setText(scores);
    }
}
