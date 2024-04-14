package edu.floridapoly.securesoftware.spring24.triviagame;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class ScoresDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quiz_results.db";
    private static final int DATABASE_VERSION = 6;
    private static final String TABLE_RESULTS = "quiz_results";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_TIME_TAKEN = "time_taken";
    private static final String COLUMN_DIFFICULTY = "difficulty";


    public ScoresDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TABLE_RESULTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SCORE + " INTEGER, " +
                COLUMN_TIME_TAKEN + " TEXT, " +
                COLUMN_DIFFICULTY + " TEXT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTS);
        onCreate(db);
    }

    public void addQuizResult(int score, String timeTaken, String difficulty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SCORE, score);
        cv.put(COLUMN_TIME_TAKEN, timeTaken);
        cv.put(COLUMN_DIFFICULTY, difficulty); // Store difficulty level
        db.insert(TABLE_RESULTS, null, cv);
        db.close();
    }

    @SuppressLint("Range")
    public List<String> getAllQuizResults() {
        List<String> quizResults = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RESULTS, null);

        if (cursor.moveToFirst()) {
            do {
                int score = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE));
                String timeTaken = cursor.getString(cursor.getColumnIndex(COLUMN_TIME_TAKEN));
                String difficulty = cursor.getString(cursor.getColumnIndex(COLUMN_DIFFICULTY));
                String result = "Difficulty: " + difficulty + " Score: " + score + ", Time Taken: " + timeTaken;
                quizResults.add(result);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return quizResults;
    }
}
