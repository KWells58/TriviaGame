package edu.floridapoly.securesoftware.spring24.triviagame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoresActivity extends AppCompatActivity {
    private ListView scoresListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        scoresListView = findViewById(R.id.scoresListView);

        // Retrieve quiz results from the database
        ScoresDbHelper dbHelper = new ScoresDbHelper(this);
        List<String> quizResults = dbHelper.getAllQuizResults();

        // Display quiz results in the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, quizResults);
        scoresListView.setAdapter(adapter);
    }
}