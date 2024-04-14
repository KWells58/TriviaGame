package edu.floridapoly.securesoftware.spring24.triviagame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private long startTime;

    private TextView questionTextView;
    private Button option1Button;
    private Button option2Button;
    private Button option3Button;
    private Button option4Button;

    private boolean quizOngoing = true; // Flag to track quiz state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Record the start time
        startTime = SystemClock.elapsedRealtime();

        // Receive the selected difficulty level
        String difficulty = getIntent().getStringExtra("difficulty");

        // Query the database for questions based on the selected difficulty level
        QuizDbHelper dbHelper = new QuizDbHelper(this);

        // Check if questions are already loaded into the database
        boolean questionsAlreadyLoaded = dbHelper.areQuestionsLoaded();

        // If questions are not already loaded, insert them into the database
        if (!questionsAlreadyLoaded) {
            // Create questions
            Question q1 = new Question("What is the capital of France?", "London", "Berlin", "Paris", "Madrid", 3, "Medium");
            Question q2 = new Question("What is the largest planet in our solar system?", "Earth", "Mars", "Jupiter", "Saturn", 3, "Medium");
            Question q3 = new Question("What is the powerhouse of the cell?", "Mitochondria", "Nucleus", "Ribosome", "Golgi apparatus", 1, "Easy");

            // Add questions to the database
            dbHelper.addQuestion(q1);
            dbHelper.addQuestion(q2);
            dbHelper.addQuestion(q3);

            // Mark questions as loaded in the database
            dbHelper.markQuestionsAsLoaded();
        }

        // Retrieve questions from the database based on the selected difficulty level
        questionList = dbHelper.getQuestionsByDifficulty(difficulty);

        // Ensure that the question list is not null and contains questions
        if (questionList == null || questionList.isEmpty()) {
            // Handle the case where no questions are retrieved from the database
            // You might want to display an error message or take appropriate action
            Log.e("QuizActivity", "No questions retrieved from the database");
            finish(); // Finish the activity
            return;
        }

        // Initialize UI components
        questionTextView = findViewById(R.id.questionTextView);
        option1Button = findViewById(R.id.option1Button);
        option2Button = findViewById(R.id.option2Button);
        option3Button = findViewById(R.id.option3Button);
        option4Button = findViewById(R.id.option4Button);

        // Display the first question
        showQuestion();

        // Set click listeners for answer buttons
        option1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(1);
            }
        });

        option2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(2);
            }
        });

        option3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(3);
            }
        });

        option4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(4);
            }
        });
    }


    private void showQuestion() {
            Question question = questionList.get(currentQuestionIndex);
            questionTextView.setText(question.getQuestion());
            option1Button.setText(question.getOption1());
            option2Button.setText(question.getOption2());
            option3Button.setText(question.getOption3());
            option4Button.setText(question.getOption4());
    }

    private void checkAnswer(int selectedOption) {
        Log.d("QuizActivity", "checkAnswer: currentQuestionIndex = " + currentQuestionIndex);
        Log.d("QuizActivity", "checkAnswer: questionList size = " + questionList.size());

        if (currentQuestionIndex < questionList.size()) {
            Question question = questionList.get(currentQuestionIndex);
            if (selectedOption == question.getAnswerNr()) {
                // Correct answer
                score++;
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            } else {
                // Incorrect answer
                Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
            }
            currentQuestionIndex++;

            if (currentQuestionIndex < questionList.size()) {
                // Display the next question
                showQuestion();
            } else {
                endQuiz();
            }
        } else {
            // Handle the case where currentQuestionIndex exceeds the questionList size
            // This could occur if the user taps too quickly or if there's a concurrency issue
            // You might want to log an error or handle it gracefully
            // For now, we'll just finish the quiz
            Log.e("QuizActivity", "checkAnswer: currentQuestionIndex exceeded questionList size");
            endQuiz();
        }
    }


    private void endQuiz() {
        // Calculate the time taken
        long elapsedTime = SystemClock.elapsedRealtime() - startTime;
        int seconds = (int) (elapsedTime / 1000);
        int minutes = seconds / 60;
        seconds %= 60;
        String timeTaken = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        // Display the user's final score and time taken
        Toast.makeText(this, "Quiz ended. Your score: " + score + "\nTime taken: " + timeTaken, Toast.LENGTH_LONG).show();

        // Store the score and time taken in the database
        ScoresDbHelper dbHelper = new ScoresDbHelper(this);
        dbHelper.addQuizResult(score, timeTaken);

        // Finish the activity
        finish();
    }

}
