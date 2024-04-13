package edu.floridapoly.securesoftware.spring24.triviagame;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AuthManager {
    private DatabaseHelper dbHelper;

    public AuthManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public boolean createAccount(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_PASSWORD, hashPassword(password));

        long result = db.insert(DatabaseHelper.TABLE_USERS, null, values);
        return result != -1;  // return true if account creation is successful
    }

    public boolean login(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {DatabaseHelper.COLUMN_ID};
        String selection = DatabaseHelper.COLUMN_USERNAME + "=? AND " + DatabaseHelper.COLUMN_PASSWORD + "=?";
        String[] selectionArgs = {username, hashPassword(password)};

        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();

        return count > 0;  // return true if login is successful
    }

    private String hashPassword(String password) {
        // Implement password hashing here (use bcrypt or another strong algorithm)
        return password;  // placeholder for hashed password
    }
}
