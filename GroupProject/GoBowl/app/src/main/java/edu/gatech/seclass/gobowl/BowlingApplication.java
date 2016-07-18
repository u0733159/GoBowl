package edu.gatech.seclass.gobowl;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.seclass.gobowl.DBHelper.DataContract.CustomerEntry;
import edu.gatech.seclass.services.ServicesUtils;

public class BowlingApplication extends Application {

    private Manager manager;

    private CustomerTable cust_table;
    private BowlingLaneEventTable bowling_lane_table;
    private ScoreTable score_table;
    private BillingTable billing_table;
    @Override
    public void onCreate() {
        super.onCreate();


        CreateDBTables();

        /* Check that a customer with the specified email does not already exist */
        String id_string = cust_table.getLastCustIDFromDB();
        int id_val = Integer.parseInt(id_string,16);
        Log.d("id_val", Integer.toString(id_val));
        id_val += 1;

        List<Integer> used_lanes = getUsedLanes();

        //Create the only instance of the manager class
        manager = new Manager(this, id_val, used_lanes);
        restoreBowlingLaneEvents();

    }

    public ScoreTable getScore_table() {
        return score_table;
    }

    private void CreateDBTables()
    {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase sql_db = dbHelper.getWritableDatabase();
        cust_table = new CustomerTable(sql_db);
        bowling_lane_table = new BowlingLaneEventTable(sql_db, cust_table);
        score_table = new ScoreTable(sql_db);
        billing_table = new BillingTable(sql_db);
    }
    public Manager getManager()
    {
        return manager;
    }

    public CustomerTable getCustTable()
    {
        return cust_table;
    }
    public BowlingLaneEventTable getBowlingEventTable()
    {
        return bowling_lane_table;
    }
    public BillingTable getBillingTable() { return billing_table; }
    public void addCustomersToSystem()
    {
        ServicesUtils.resetCustomers();
        Cursor c = cust_table.getAllCustomers();
        boolean moved = c.moveToFirst();
        if (moved) {
            do {
                String cust_id = c.getString(c.getColumnIndexOrThrow(CustomerEntry.KEY_CUSTOMER_ID));
                String cust_first_name = c.getString(c.getColumnIndexOrThrow(CustomerEntry.KEY_FIRSTNAME));
                String cust_last_nane = c.getString(c.getColumnIndexOrThrow(CustomerEntry.KEY_LASTNAME));
                String cust_email = c.getString(c.getColumnIndexOrThrow(CustomerEntry.KEY_EMAIL));

                //Fixme What about this hardcorded junk
                ServicesUtils.addCustomer(cust_first_name, cust_last_nane, "678967896789", "12312016", "999", cust_id);
            } while(c.moveToNext());
        }

    }
    public List<Integer> getUsedLanes()
    {
        return bowling_lane_table.getBowlingLaneIDs();
    }

    public void restoreBowlingLaneEvents()
    {
        List<BowlingLaneEvent> all_events;
        all_events = bowling_lane_table.getAllBowlingLaneEvents();
        for(BowlingLaneEvent ble: all_events) {
            manager.registerBowlingLaneEvent(ble);
        }
    }

}
