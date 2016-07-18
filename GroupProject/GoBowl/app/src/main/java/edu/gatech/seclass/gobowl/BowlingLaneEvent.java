package edu.gatech.seclass.gobowl;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by R on 7/4/2016.
 */
public class BowlingLaneEvent  {
    public class CustomerAlreadyAdded extends Exception {
        public CustomerAlreadyAdded(String message){
            super(message);
        }
    }
    private Date startTime;
    private String startTimeInfor;
    private Date endTime;
    private int laneNumber;
    private Customer primaryCustomer;
    private int total_cust;
    private List<Customer> nonPrimaryCustomers;
    private List<String> customerIDs = new ArrayList<String>();
    public BowlingLaneEvent(Customer primaryCustomer) {
        this.primaryCustomer = primaryCustomer;
        nonPrimaryCustomers = new ArrayList<Customer>();
        customerIDs.add(primaryCustomer.getId());
    }

    public BowlingLaneEvent(Customer primaryCustomer, List<Customer> nonPrimaryCust, Date startTime)
    {
        this.primaryCustomer = primaryCustomer;
        this.nonPrimaryCustomers = nonPrimaryCust;
        this.startTime = startTime;
    }

    public BowlingLaneEvent(Customer primaryCustomer, List<Customer> nonPrimaryCust,
                            Date startTime, Date endTime)
    {
        this.primaryCustomer = primaryCustomer;
        this.nonPrimaryCustomers = nonPrimaryCust;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public BowlingLaneEvent(Customer primaryCustomer, List<Customer> nonPrimaryCust,
                            Date startTime, Date endTime, int laneNumber)
    {
        this(primaryCustomer, nonPrimaryCust, startTime, endTime);
        this.laneNumber = laneNumber;
    }

    public void resetBowlingLane(){
        startTime = null;
        startTimeInfor = "";
        laneNumber = -1;
        nonPrimaryCustomers.clear();
        primaryCustomer = null;
        customerIDs.clear();
    }
    public boolean addNPCustomer(Customer customer) throws CustomerAlreadyAdded
    {
        if(customerIDs.contains(customer.getId())) {
            throw new CustomerAlreadyAdded("Customer has already been added!");
        }
        else {
            customerIDs.add(customer.getId());
            nonPrimaryCustomers.add(customer);
        }
        return true;
    }

    public Customer getPrimaryCustomer() {
        return primaryCustomer;
    }

    public void setTotalCustomers(int total) {
        total_cust = total;
    }

    public int getLaneNumber()
    {
        return laneNumber;
    }

    public void setLaneNumber(int laneNumber)
    {
        this.laneNumber = laneNumber;
    }

    public String getStartTimeInfor() {
        return startTimeInfor;
    }

    public void startBowlingEvent()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("E MM-dd-yyyy kk:mm:ss");
        startTime = new Date();
        startTimeInfor = formatter.format(startTime);
    }

    public void endBowlingEvent()
    {
        endTime = new Date();
    }

    Date getStartTime()
    {
        return startTime;
    }

    Date getEndTime()
    {
        return endTime;
    }
    public String getPrimaryCustomerID(){
        return primaryCustomer.getNameAndId();
    }


    public String getNonPrimaryCustomerIDs() {
        List<String> cust_ids = new ArrayList<String>();
        for (Customer customer : nonPrimaryCustomers)
            cust_ids.add(customer.getId());
        return TextUtils.join(" ", cust_ids);
    }

    public List<Customer> getNonPrimaryCustomers()
    {
        return nonPrimaryCustomers;
    }
}
