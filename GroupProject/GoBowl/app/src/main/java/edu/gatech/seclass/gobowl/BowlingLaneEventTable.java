package edu.gatech.seclass.gobowl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import edu.gatech.seclass.gobowl.DBHelper.DataContract.BowlingLaneEventEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by R on 7/10/2016.
 */
public class BowlingLaneEventTable {
    private static SQLiteDatabase bowlingLaneEventDB;
    private CustomerTable customerTable;
    private SimpleDateFormat formatter;
    public BowlingLaneEventTable(SQLiteDatabase db, CustomerTable customerTable)
    {
        this.bowlingLaneEventDB = db;
        this.customerTable = customerTable;
        formatter = new SimpleDateFormat("E MM-dd-yyyy kk:mm:ss");
    }

    public void addBowlingLane(BowlingLaneEvent bowling_lane){
        ContentValues values = new ContentValues();
        values.put(BowlingLaneEventEntry.KEY_LaneID, bowling_lane.getLaneNumber());
        values.put(BowlingLaneEventEntry.KEY_StartTime, bowling_lane.getStartTimeInfor());
        values.put(BowlingLaneEventEntry.KEY_PrimaryCustomerID, bowling_lane.getPrimaryCustomer().getId());
        values.put(BowlingLaneEventEntry.KEY_NonPrimaryCustomerIDs, bowling_lane.getNonPrimaryCustomerIDs());
        bowlingLaneEventDB.insertOrThrow(BowlingLaneEventEntry.TABLE_NAME, null, values);
    }


    public void deleteBowlingLane(int laneNumber){
        bowlingLaneEventDB.delete(BowlingLaneEventEntry.TABLE_NAME, BowlingLaneEventEntry.KEY_LaneID +" =?", new String[]{String.valueOf(laneNumber)});
    }


    public String getPrimaryCusIDtomer(int laneNumber){
        String[] projection = {
            BowlingLaneEventEntry.KEY_LaneID,
                BowlingLaneEventEntry.KEY_StartTime,
                BowlingLaneEventEntry.KEY_PrimaryCustomerID,
                BowlingLaneEventEntry.KEY_NonPrimaryCustomerIDs
        };
        Cursor cursor = bowlingLaneEventDB.query(BowlingLaneEventEntry.TABLE_NAME, projection, BowlingLaneEventEntry.KEY_LaneID +"=?", new String[]{String.valueOf(laneNumber)}, null, null, null);

        boolean moved;
        if (cursor != null) {
            moved = cursor.moveToFirst();
            if(!moved){
                return null;
            }
        }
        int blEventID = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(BowlingLaneEventEntry.KEY_LaneID)));
        String primaryID = cursor.getString(cursor.getColumnIndexOrThrow(BowlingLaneEventEntry.KEY_PrimaryCustomerID));
        return primaryID;
    }

    public String getStartTimeInfor(int laneNumber){
        String[] projection = {
                BowlingLaneEventEntry.KEY_LaneID,
                BowlingLaneEventEntry.KEY_StartTime,
                BowlingLaneEventEntry.KEY_PrimaryCustomerID,
                BowlingLaneEventEntry.KEY_NonPrimaryCustomerIDs
        };
        Cursor cursor = bowlingLaneEventDB.query(BowlingLaneEventEntry.TABLE_NAME, projection, BowlingLaneEventEntry.KEY_LaneID +"=?", new String[]{String.valueOf(laneNumber)}, null, null, null);

        boolean moved;
        if (cursor != null) {
            moved = cursor.moveToFirst();
            if(!moved){
                return null;
            }
        }
        String startTimeInfo = cursor.getString(cursor.getColumnIndexOrThrow(BowlingLaneEventEntry.KEY_StartTime));
        return startTimeInfo;
    }

    /**
     * Gets a lits of used bowling lanes.
     * @return A list of used bowling lanes
     */
    public ArrayList<Integer> getBowlingLaneIDs(){
        ArrayList<Integer> laneIds = new ArrayList<Integer>();
        String selectQuery = "SELECT * FROM " + BowlingLaneEventEntry.TABLE_NAME;
        Cursor cursor = bowlingLaneEventDB.rawQuery(selectQuery, null);

        String[] projection = {
                BowlingLaneEventEntry.KEY_LaneID,
                BowlingLaneEventEntry.KEY_StartTime,
                BowlingLaneEventEntry.KEY_PrimaryCustomerID,
                BowlingLaneEventEntry.KEY_NonPrimaryCustomerIDs
        };

        if(cursor.moveToFirst()){
            do{
                int tempID = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(BowlingLaneEventEntry.KEY_LaneID)));
                laneIds.add(tempID);

            }while(cursor.moveToNext());
        }
        return laneIds;
    }

    public static int countRuningBowlingLanes(){
        int runninigLanes = 0;
        String countQuery = "SELECT * FROM " + BowlingLaneEventEntry.TABLE_NAME;

        Cursor cursor = bowlingLaneEventDB.rawQuery(countQuery, null);

        runninigLanes = cursor.getCount();
// return count
        cursor.close();
        return runninigLanes;
    }
    
    /**
     * Checks if a customer is involved in a bowling lane event
     * @param custID
     * @return true if the customer is in an event. False otherwise.
     */
    public boolean isCustomerBowling(String custID)
    {
        String[] projection = {
                BowlingLaneEventEntry.KEY_PrimaryCustomerID
        };

        String where_str = BowlingLaneEventEntry.KEY_PrimaryCustomerID + "= ?";
        String[] where_clause = { custID };
        Cursor cursor = bowlingLaneEventDB.query(
                BowlingLaneEventEntry.TABLE_NAME,
                projection,                               // The columns to return
                where_str,                                // The columns for the WHERE clause
                where_clause,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        return cursor.moveToFirst();
    }

    /**
     * Only used for debugging to clear the table
     * @return
     */
    public int dropAllEntries()
    {
        //String query = "Delete from " + BowlingLaneEventEntry.TABLE_NAME;
        return bowlingLaneEventDB.delete(BowlingLaneEventEntry.TABLE_NAME, null, null);
    }

    public BowlingLaneEvent getBowlingEventFromLane(int lane_number)
    {
        String[] projection = {
                BowlingLaneEventEntry.KEY_LaneID,
                BowlingLaneEventEntry.KEY_PrimaryCustomerID,
                BowlingLaneEventEntry.KEY_NonPrimaryCustomerIDs,
                BowlingLaneEventEntry.KEY_StartTime,
                BowlingLaneEventEntry.KEY_EndTime
        };

        String where_str = BowlingLaneEventEntry.KEY_LaneID + "= ?";
        String[] where_clause = { Integer.toString(lane_number) };
        Cursor cursor = bowlingLaneEventDB.query(
                BowlingLaneEventEntry.TABLE_NAME,
                projection,                               // The columns to return
                where_str,                                // The columns for the WHERE clause
                where_clause,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        boolean moved = cursor.moveToFirst();
        if(moved) {
            Customer primaryCust = customerTable.getCustomerByID(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    BowlingLaneEventEntry.KEY_PrimaryCustomerID)));
            String nonPrimaryCustStr = cursor.getString(
                    cursor.getColumnIndexOrThrow(BowlingLaneEventEntry.KEY_NonPrimaryCustomerIDs));
            String startTimeStr = cursor.getString(
                    cursor.getColumnIndexOrThrow(BowlingLaneEventEntry.KEY_StartTime));
            String endTimeStr = cursor.getString(
                    cursor.getColumnIndexOrThrow(BowlingLaneEventEntry.KEY_EndTime));

            Date startTime = null;
            Date endTime = null;
            try {
                startTime = formatter.parse(startTimeStr);
                endTime = formatter.parse(endTimeStr);
            } catch (ParseException pe) {
                //This shouldn't happen
            }
            ArrayList<Customer> nonPrimaryCust = new ArrayList<Customer>();
            for(String s : nonPrimaryCustStr.split(" ")) {
                if(s.length() == 0) {
                    continue;
                }
                Customer npc = customerTable.getCustomerByID(s);
                nonPrimaryCust.add(npc);
            }
            BowlingLaneEvent ble = new BowlingLaneEvent(primaryCust, nonPrimaryCust, startTime, endTime);
            return ble;
        } else {
            return null;
        }
    }

    public void endBowlingEvent(int lane_number)
    {
        Date endDate = new Date();
        String dateStr = formatter.format(endDate);
        ContentValues values = new ContentValues();
        values.put(BowlingLaneEventEntry.KEY_EndTime, dateStr );
        String whereStr = BowlingLaneEventEntry.KEY_LaneID + "= ?";
        String[] whereClause = { Integer.toString(lane_number)};
        int rows = bowlingLaneEventDB.update(
                BowlingLaneEventEntry.TABLE_NAME,
                values,
                whereStr,
                whereClause);
        if(rows != 1) {
            Log.w("BowlingLaneEventTable", "endBowlingEvent did not update a row");
        }
    }

    public List<BowlingLaneEvent> getAllBowlingLaneEvents()
    {
        List<BowlingLaneEvent> return_list = new ArrayList<BowlingLaneEvent>();
        String[] projection = {
                BowlingLaneEventEntry.KEY_LaneID,
                BowlingLaneEventEntry.KEY_PrimaryCustomerID,
                BowlingLaneEventEntry.KEY_NonPrimaryCustomerIDs,
                BowlingLaneEventEntry.KEY_StartTime,
                BowlingLaneEventEntry.KEY_EndTime
        };

        Cursor cursor = bowlingLaneEventDB.query(
                BowlingLaneEventEntry.TABLE_NAME,
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        boolean moved = cursor.moveToFirst();
        if(moved == false){
            return return_list;
        } else {
            do {
                List<Customer> non_primary_cust = new ArrayList<Customer>();
                int lane = cursor.getInt(
                        cursor.getColumnIndexOrThrow(BowlingLaneEventEntry.KEY_LaneID));
                String pCustID = cursor.getString(
                        cursor.getColumnIndexOrThrow(BowlingLaneEventEntry.KEY_PrimaryCustomerID));
                String npCustIDs = cursor.getString(
                        cursor.getColumnIndexOrThrow(BowlingLaneEventEntry.KEY_NonPrimaryCustomerIDs));
                String startDateStr = cursor.getString(
                        cursor.getColumnIndexOrThrow(BowlingLaneEventEntry.KEY_StartTime));
                String endDateStr = cursor.getString(
                        cursor.getColumnIndexOrThrow(BowlingLaneEventEntry.KEY_StartTime));
                for(String s: npCustIDs.split(" ")) {
                    if(s.length() == 0){
                        continue;
                    }
                    Customer npc = customerTable.getCustomerByID(s);
                    non_primary_cust.add(npc);
                }
                Customer primary_cust = customerTable.getCustomerByID(pCustID);
                Date start_date = null;
                Date end_date = null;
                try {
                    start_date = formatter.parse(startDateStr);
                    end_date = formatter.parse(endDateStr);
                } catch (ParseException pe) {
                    Log.e("BowlingLaneEventTable", "Date parse error");
                }
                BowlingLaneEvent ble = new BowlingLaneEvent(primary_cust, non_primary_cust,
                        start_date, end_date,lane);
                return_list.add(ble);
            } while (cursor.moveToNext());
        }
        return return_list;
    }
}

