package com.emc2.www.gobang.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.emc2.www.gobang.entity.Record;
import com.emc2.www.gobang.util.FormatDate;

import java.text.Format;
import java.util.ArrayList;
import java.util.List;

public class RecordDao {
    private static final String TAG = "RecordDao";

    private static DatabaseHelper databaseHelper;

    public RecordDao(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
    }


    public boolean createTable() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        try {
            databaseHelper.dropTableDiary(db);
            databaseHelper.createTableDiary(db);
            Log.d(TAG, "Create DB Table");
            return true;
        } catch (SQLException e) {
            Log.d(TAG, e.getMessage());
            return false;
        }
    }
    public static boolean insertRecords(String time,String blackPlayer,String whitePlayer,int chessCount,String winner,Context context){
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseFiled.Record.BLACKPLAYER, blackPlayer);
        cv.put(DatabaseFiled.Record.CHESSCOUNT, chessCount);
        cv.put(DatabaseFiled.Record.TIME, time);
        cv.put(DatabaseFiled.Record.WHITEPAYER, whitePlayer);
        cv.put(DatabaseFiled.Record.WINNER, winner);
        return db.insert(DatabaseFiled.Tables.Record, null, cv) != -1;
    }

    public static List<Record> searchRecord(String blackPlayer, String whitePlayer) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        List<Record> list = new ArrayList<Record>();
        Cursor cursor = db.query(DatabaseFiled.Tables.Record, null, "blackPlayer=? and whitePlayer=?", new String[]{blackPlayer,whitePlayer}, null, null, null);
        if (cursor.getCount() == 0) {
            return null;
        }else{
            while (cursor.moveToNext()) {

                Record record = new Record();
                record.setId(Integer.parseInt(cursor.getString(cursor
                        .getColumnIndex("id"))));
                record.setBlackPlayer(cursor.getString(cursor.getColumnIndex("blackPlayer")));
                record.setWhitePlayer(cursor.getString(cursor.getColumnIndex("whitePlayer")));
                record.setWinner(cursor.getString(cursor.getColumnIndex("winner")));
                record.setTime(cursor.getString(cursor.getColumnIndex("time")));
                record.setChessCount(Integer.parseInt(cursor.getString(cursor.getColumnIndex("chessCount"))));
                list.add(record);
            }
        }
        return list;
    }


    public static List<Record> searchAllRecord() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        List<Record> list = new ArrayList<Record>();
        Cursor cursor = db.query(DatabaseFiled.Tables.Record, null, null, new String[]{}, null, null, "id DESC");
        if (cursor.getCount() == 0) {
            return null;
        }else{
            while (cursor.moveToNext()) {

                Record record = new Record();
                record.setId(Integer.parseInt(cursor.getString(cursor
                        .getColumnIndex("id"))));
                record.setBlackPlayer(cursor.getString(cursor.getColumnIndex("blackPlayer")));
                record.setWhitePlayer(cursor.getString(cursor.getColumnIndex("whitePlayer")));
                record.setWinner(cursor.getString(cursor.getColumnIndex("winner")));
                String time = FormatDate.changeDate(cursor.getString(cursor.getColumnIndex("time")));
                record.setTime(time);
                record.setChessCount(Integer.parseInt(cursor.getString(cursor.getColumnIndex("chessCount"))));
                list.add(record);
            }
        }
        cursor.close();
        return list;
    }

}
