package edu.gatech.seclass.gobowl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import edu.gatech.seclass.gobowl.DBHelper.DataContract.BillingEntry;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BillingTable {
    private static SQLiteDatabase billingDB;

    public BillingTable(SQLiteDatabase db) {
        this.billingDB = db;
    }

    public void addBillingEntry(String cust_id, Date bill_time, double amount) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String date_str = formatter.format(bill_time);
        ContentValues values = new ContentValues();
        values.put(BillingEntry.KEY_CustID, cust_id);
        values.put(BillingEntry.KEY_Date, date_str);
        values.put(BillingEntry.KEY_SpendAmount, amount);
        long rowid = billingDB.insertOrThrow(BillingEntry.TABLE_NAME, null, values);
        if (rowid == -1) {
            Log.e("BillingTable", "Failed to insert Billing row");
        }

    }

    public double getPastYearBillsForCust(String custID)
    {
        String[] projection = {
                BillingEntry.KEY_SpendAmount,
        };
        Date current = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String last_year = Integer.toString(Integer.parseInt(formatter.format(current)) - 1);
        String start_date_str = String.format("%s-01-01", last_year);
        String end_date_str = String.format("%s-12-31", last_year);
        String[] where_clause = {custID, start_date_str, end_date_str};
        String sql_str = "Select " + BillingEntry.KEY_SpendAmount + " from " +
                BillingEntry.TABLE_NAME + " where " + BillingEntry.KEY_CustID + "= ? and " +
                BillingEntry.KEY_Date + " between ? and ?";
        Cursor cursor = billingDB.rawQuery(sql_str, where_clause);
        if(cursor.moveToFirst()) {
            double total = 0.0;
            double value;
            do {
                value = cursor.getDouble(cursor.getColumnIndexOrThrow(BillingEntry.KEY_SpendAmount));
                total += value;
                Log.d("BillingTable", Double.toString(value));
            } while (cursor.moveToNext());
            return total;
        } else {
            return 0.0;
        }
    }
}
