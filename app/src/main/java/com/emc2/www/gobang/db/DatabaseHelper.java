package com.emc2.www.gobang.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.support.constraint.Constraints.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DatabaseFiled.DATABASE_NAME, null, DatabaseFiled.DATABASE_VERSION);
        Log.d(TAG, "Create DatabaseHelper Object Version=" + DatabaseFiled.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + DatabaseFiled.Tables.Record + " ("
                + DatabaseFiled.Record.ID + " INTEGER  PRIMARY KEY AUTOINCREMENT, "
                + DatabaseFiled.Record.WINNER + " TEXT NOT NULL,"
                + DatabaseFiled.Record.CHESSCOUNT + " INTEGER NOT NULL,"
                + DatabaseFiled.Record.TIME + " TEXT NOT NULL,"
                + DatabaseFiled.Record.WHITEPAYER + " TEXT NOT NULL,"
                + DatabaseFiled.Record.BLACKPLAYER + " TEXT NOT NULL"
                + ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade=" + newVersion);
        dropTableDiary(db);
        createTableDiary(db);
        Log.d(TAG, "onUpgrade success");
    }

    void createTableDiary(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + DatabaseFiled.Tables.Record + " ("
                + DatabaseFiled.Record.ID + " INTEGER  PRIMARY KEY AUTOINCREMENT, "
                + DatabaseFiled.Record.WINNER + " TEXT NOT NULL,"
                + DatabaseFiled.Record.CHESSCOUNT + " INTEGER NOT NULL,"
                + DatabaseFiled.Record.TIME + " TEXT NOT NULL,"
                + DatabaseFiled.Record.WHITEPAYER + " TEXT NOT NULL,"
                + DatabaseFiled.Record.BLACKPLAYER + " TEXT NOT NULL"
                + ");";
        db.execSQL(sql);
    }

    void dropTableDiary(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseFiled.DATABASE_NAME);
    }
}

