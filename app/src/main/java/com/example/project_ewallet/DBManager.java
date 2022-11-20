package com.example.project_ewallet;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBManager {
    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    // ================================================
    public DBManager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }
    // ================================================

    public void insertIncome( String categories, double amount) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.DATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        contentValue.put(DBHelper.AMOUNT, amount);
        contentValue.put(DBHelper.CATEGORIES, categories);

        database.insert(DBHelper.INCOME_TABLE, null, contentValue);
    }
    public void insertExpense( String categories, double amount) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.DATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        contentValue.put(DBHelper.AMOUNT, amount);
        contentValue.put(DBHelper.CATEGORIES, categories);

        database.insert(DBHelper.EXPENSE_TABLE, null, contentValue);
    }

    public Cursor fetchIncome() {
        String[] columns = new String[] { DBHelper.ID, DBHelper.AMOUNT, DBHelper.DATE, DBHelper.CATEGORIES };
        Cursor cursor = database.query(DBHelper.INCOME_TABLE, columns, null, null, null, null, null);

        return cursor;
    }
    public Cursor fetchExpense() {
        String[] columns = new String[] { DBHelper.ID, DBHelper.AMOUNT, DBHelper.DATE, DBHelper.CATEGORIES };
        Cursor cursor = database.query(DBHelper.EXPENSE_TABLE, columns, null, null, null, null, null);

        return cursor;
    }

    public int updateIncome(long id, String categories, double amount, Date date) {
        ContentValues contentValues = new ContentValues();
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.DATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        contentValue.put(DBHelper.AMOUNT, amount);
        contentValue.put(DBHelper.CATEGORIES, categories);

        int i = database.update(DBHelper.INCOME_TABLE, contentValues, DBHelper.ID + " = " + id, null);
        return i;
    }
    public int updateExpense(long id, String categories, double amount, Date date) {
        ContentValues contentValues = new ContentValues();
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.DATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        contentValue.put(DBHelper.AMOUNT, amount);
        contentValue.put(DBHelper.CATEGORIES, categories);

        int i = database.update(DBHelper.EXPENSE_TABLE, contentValues, DBHelper.ID + " = " + id, null);
        return i;
    }

    // ====================================================
    public void deleteIncome(long id) {
        database.delete(DBHelper.INCOME_TABLE, DBHelper.ID + "=" + id, null);
    }
    public void deleteExpense(long id) {
        database.delete(DBHelper.EXPENSE_TABLE, DBHelper.ID + "=" + id, null);
    }
}