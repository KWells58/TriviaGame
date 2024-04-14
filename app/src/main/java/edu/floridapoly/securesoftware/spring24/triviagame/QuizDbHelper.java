package edu.floridapoly.securesoftware.spring24.triviagame;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    // Database info
    private static final String DATABASE_NAME = "triviaQuiz.db";
    private static final int DATABASE_VERSION = 6;

    // Table and columns names
    private static final String TABLE_QUESTIONS = "questions";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_QUESTION = "question";
    private static final String COLUMN_OPTION1 = "option1";
    private static final String COLUMN_OPTION2 = "option2";
    private static final String COLUMN_OPTION3 = "option3";
    private static final String COLUMN_OPTION4 = "option4";
    private static final String COLUMN_ANSWER_NR = "answer_nr";
    private static final String COLUMN_DIFFICULTY = "difficulty";  // difficulty level
    private static final String COLUMN_LOADED = "loaded";




    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TABLE_QUESTIONS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_QUESTION + " TEXT, " +
                COLUMN_OPTION1 + " TEXT, " +
                COLUMN_OPTION2 + " TEXT, " +
                COLUMN_OPTION3 + " TEXT, " +
                COLUMN_OPTION4 + " TEXT, " +
                COLUMN_ANSWER_NR + " INTEGER, " +
                COLUMN_DIFFICULTY + " TEXT, " +
                COLUMN_LOADED + " INTEGER DEFAULT 0" + // Default value is 0 (not loaded)
                ");";
        db.execSQL(createTableStatement);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        onCreate(db);
    }

    // Adding a question to the database
    public void addQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_QUESTION, question.getQuestion());
        cv.put(COLUMN_OPTION1, question.getOption1());
        cv.put(COLUMN_OPTION2, question.getOption2());
        cv.put(COLUMN_OPTION3, question.getOption3());
        cv.put(COLUMN_OPTION4, question.getOption4());
        cv.put(COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(COLUMN_DIFFICULTY, question.getDifficulty());
        db.insert(TABLE_QUESTIONS, null, cv);
        db.close();
    }

    // Fetching all questions from the database
    public List<Question> getAllQuestions() {
        List<Question> questionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_QUESTIONS, null);

        int idIndex = cursor.getColumnIndex(COLUMN_ID);
        int questionIndex = cursor.getColumnIndex(COLUMN_QUESTION);
        int option1Index = cursor.getColumnIndex(COLUMN_OPTION1);
        int option2Index = cursor.getColumnIndex(COLUMN_OPTION2);
        int option3Index = cursor.getColumnIndex(COLUMN_OPTION3);
        int option4Index = cursor.getColumnIndex(COLUMN_OPTION4);
        int answerNrIndex = cursor.getColumnIndex(COLUMN_ANSWER_NR);

        // Checking if any of the indices are -1, which indicates the column name was not found in the result set
        if (idIndex == -1 || questionIndex == -1 || option1Index == -1 ||
                option2Index == -1 || option3Index == -1 || option4Index == -1 ||
                answerNrIndex == -1) {
            Log.e("QuizDbHelper", "One or more columns not found in the database");
            cursor.close();
            db.close();
            return questionList; // Returning empty list as we cannot process further safely
        }

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(cursor.getInt(idIndex));
                question.setQuestion(cursor.getString(questionIndex));
                question.setOption1(cursor.getString(option1Index));
                question.setOption2(cursor.getString(option2Index));
                question.setOption3(cursor.getString(option3Index));
                question.setOption4(cursor.getString(option4Index));
                question.setAnswerNr(cursor.getInt(answerNrIndex));
                questionList.add(question);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return questionList;
    }


    // Fetching questions from the database based on difficulty level
    @SuppressLint("Range")
    public List<Question> getQuestionsByDifficulty(String difficulty) {
        List<Question> questionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {difficulty};
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_QUESTIONS + " WHERE " + COLUMN_DIFFICULTY + " = ?", selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                question.setQuestion(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION)));
                question.setOption1(cursor.getString(cursor.getColumnIndex(COLUMN_OPTION1)));
                question.setOption2(cursor.getString(cursor.getColumnIndex(COLUMN_OPTION2)));
                question.setOption3(cursor.getString(cursor.getColumnIndex(COLUMN_OPTION3)));
                question.setOption4(cursor.getString(cursor.getColumnIndex(COLUMN_OPTION4)));
                question.setAnswerNr(cursor.getInt(cursor.getColumnIndex(COLUMN_ANSWER_NR)));
                question.setDifficulty(cursor.getString(cursor.getColumnIndex(COLUMN_DIFFICULTY)));
                questionList.add(question);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return questionList;
    }

    public boolean areQuestionsLoaded() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {"1"}; // Look for questions marked as loaded
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_QUESTIONS +
                " WHERE " + COLUMN_LOADED + " = ?", selectionArgs);
        boolean loaded = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return loaded;
    }


    public void markQuestionsAsLoaded() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LOADED, 1); // Mark questions as loaded
        db.update(TABLE_QUESTIONS, cv, null, null);
        db.close();
    }


}
