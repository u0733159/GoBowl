package edu.gatech.seclass.gobowl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ScoreTable {
    private static SQLiteDatabase scoreDB;
    public ScoreTable(SQLiteDatabase db) { this.scoreDB = db;}

    public void addScoreEntry(Score newScore){
        ContentValues values = new ContentValues();
        values.put(DBHelper.DataContract.ScoreEntry.KEY_CustID, newScore.getCustomerID());
        SimpleDateFormat formatter = new SimpleDateFormat("E MM-dd-yyyy kk:mm:ss");
        String date_str = formatter.format(newScore.getGameEndTime());
        values.put(DBHelper.DataContract.ScoreEntry.KEY_ScoreDate, date_str);
        values.put(DBHelper.DataContract.ScoreEntry.KEY_Score, newScore.getScore());
        long row = scoreDB.insertOrThrow(DBHelper.DataContract.ScoreEntry.TABLE_NAME, null, values);
        if(row == -1) {
            Log.e("ScoreTable", "Failed to insert score!");
        }
    }

    public ArrayList<String> getScoreEntriesByCustomerID(String customerID){
        ArrayList<String> dateScorePairs = new ArrayList<String>();
        String[] projection = {
                DBHelper.DataContract.ScoreEntry.KEY_CustID,
                DBHelper.DataContract.ScoreEntry.KEY_ScoreDate,
                DBHelper.DataContract.ScoreEntry.KEY_Score,
        };
        Cursor cursor = scoreDB.query(DBHelper.DataContract.ScoreEntry.TABLE_NAME, projection, DBHelper.DataContract.ScoreEntry.KEY_CustID +"=?", new String[]{customerID}, null, null, null);

        String cusID = DBHelper.DataContract.ScoreEntry.KEY_CustID;
        String scoDate = DBHelper.DataContract.ScoreEntry.KEY_ScoreDate;
        String score = DBHelper.DataContract.ScoreEntry.KEY_Score;
        if(cursor.moveToFirst()){
            do{
                String  score_date= cursor.getString(cursor.getColumnIndexOrThrow(scoDate));
                int score_num = cursor.getInt(cursor.getColumnIndexOrThrow(score));
                String score_string = String.valueOf(score_num);
                dateScorePairs.add(score_date + " "+score_string);
            }while(cursor.moveToNext());
        }
        return dateScorePairs;
    }
}
