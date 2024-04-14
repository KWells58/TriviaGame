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
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        // Initialize views
        scoresListView = findViewById(R.id.scoresListView);

        // Retrieve scores and time taken from SharedPreferences
        sharedPreferences = getSharedPreferences("ScoresPrefs", MODE_PRIVATE);
        Map<String, ?> allScores = sharedPreferences.getAll();

        // Create a list of strings to display in the ListView
        List<String> scoreList = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allScores.entrySet()) {
            String quizInfo = entry.getKey() + ": " + entry.getValue();
            scoreList.add(quizInfo);
        }

        // Create an ArrayAdapter to display the scores in the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scoreList);
        scoresListView.setAdapter(adapter);
    }
}
