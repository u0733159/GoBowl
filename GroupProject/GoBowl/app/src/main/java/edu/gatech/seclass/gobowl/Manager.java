package edu.gatech.seclass.gobowl;

import android.app.Application;
import android.content.Context;
import android.support.v4.util.CircularIntArray;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by R on 7/4/2016.
 */
public class Manager {
    static int customerNumber = 1;
    private BowlingApplication theApp;
    private Queue<Integer> availableLanes;
    private static int BowlingLaneCapacity = 1000;
    private List<BowlingLaneEvent> bowlingLaneEvents;
    public Manager(Application app, int initial_id, List<Integer> used_lanes)
    {
        theApp = (BowlingApplication) app;
        customerNumber = initial_id;
        availableLanes =  new LinkedList<Integer>();;
        for(int i=1; i< 100; i++) {
            if(!used_lanes.contains(i)){
                availableLanes.add(i);
            }
        }


        bowlingLaneEvents = new ArrayList<BowlingLaneEvent>();
    }


    public String addCustomer(String firstName, String lastName, String email ){

        String id = generateRandomUniqueID();
        CustomerTable cust_db = theApp.getCustTable();
        Customer new_customer = new Customer(id, firstName,lastName, email, false);
        new_customer.setID(id);

        String email_to_insert = email.toLowerCase();
        long newRowID = cust_db.insert_customer(id, firstName, lastName, email_to_insert);
        Context context = theApp.getApplicationContext();
        CharSequence text;
        int duration = Toast.LENGTH_SHORT;
        boolean ret_val;
        if (newRowID == -1) {
            text = "The customer already exists in the database!";
            ret_val = false;
        } else {
            text = "Customer added successfully. Add another customer or press back to return.";
            ret_val = true;
        }
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        if (ret_val == false) {
            return "";
        }
        return id;
    }
    public String generateRandomUniqueID(){
        String s = String.format("%04x", customerNumber);
        customerNumber++;
        return s;
    }
    public void editCustomerByName(int customerID, String new_name){

    }
    public void editCustomerByEmail(int customerID, String new_email){

    }
    public void editCustomerByNameEmail(int customerID, String new_name, String new_email){

    }
    /**Assign BowlingLane Should have a look at the bowling lane database table first to
     * verify if a lane is already running in the data base"
    */
    public int assignBowlingLane() {
        //return availableLanes.remove();
        BowlingLaneEventTable bowlingDB = theApp.getBowlingEventTable();
        int currentLaneNumber = bowlingDB.countRuningBowlingLanes();
        ArrayList<Integer> bowlingLaneIds = bowlingDB.getBowlingLaneIDs();

        if(currentLaneNumber < BowlingLaneCapacity){
            currentLaneNumber++;
            while(bowlingLaneIds.contains(currentLaneNumber)){
                currentLaneNumber++;
            }
        }
        else{
            currentLaneNumber = 1;
            while(currentLaneNumber< BowlingLaneCapacity)
            {
                if(!bowlingLaneIds.contains(currentLaneNumber)){
                    break;
                }
                else{
                    currentLaneNumber++;
                }
            }
        }
        return currentLaneNumber;
    }

    public void releaseBowlingLane(int lane)
    {
        availableLanes.add(lane);
    }

    public void registerBowlingLaneEvent(BowlingLaneEvent bowlingLaneEvent)
    {
        bowlingLaneEvents.add(bowlingLaneEvent);
    }

    public void unregisterBowlingLaneEvent(BowlingLaneEvent bowlingLaneEvent) {
        bowlingLaneEvents.remove(bowlingLaneEvent);
    }

    public BowlingLaneEvent peekBowlingLaneEvent(int laneNumber)
    {
        for(BowlingLaneEvent b: bowlingLaneEvents){
            if (b.getLaneNumber() == laneNumber) {
                return b;
            }
        }
        return null;
    }



}
