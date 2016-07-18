package edu.gatech.seclass.gobowl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import edu.gatech.seclass.gobowl.DBHelper.DataContract.CustomerEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper class around the customer table
 */
public class CustomerTable {

    private SQLiteDatabase customer_db;
    public CustomerTable(SQLiteDatabase customer_db)
    {
        this.customer_db = customer_db;
    }
    public Cursor getAllCustomers()
    {
        String[] projection = {
                CustomerEntry.KEY_CUSTOMER_ID,
                CustomerEntry.KEY_FIRSTNAME,
                CustomerEntry.KEY_LASTNAME,
                CustomerEntry.KEY_EMAIL,
        };

        Cursor c = customer_db.query(
                CustomerEntry.TABLE_NAME,       // The table to query
                projection,                     // The columns to return
                null,                           // The columns for the WHERE clause
                null,                           // The values for the WHERE clause
                null,                           // don't group the rows
                null,                           // don't filter by row groups
                null                           // The sort order
        );
        return c;
    }

    public List<Customer> getCustomersList() {
        List<Customer> customerList = new ArrayList<Customer>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + CustomerEntry.TABLE_NAME;

        Cursor cursor = customer_db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();
                String tempID = cursor.getString(cursor.getColumnIndexOrThrow(CustomerEntry.KEY_CUSTOMER_ID));
                String tempFname = cursor.getString(cursor.getColumnIndexOrThrow(CustomerEntry.KEY_FIRSTNAME));
                String tempLname = cursor.getString(cursor.getColumnIndexOrThrow(CustomerEntry.KEY_LASTNAME));
                String tempEmail = cursor.getString(cursor.getColumnIndexOrThrow(CustomerEntry.KEY_EMAIL));
                customer.setId(tempID);
                customer.setFirstName(tempFname);
                customer.setLastName(tempLname);
                customer.setEmail(tempEmail);
                // Adding customer to list
                customerList.add(customer);
            } while (cursor.moveToNext());
        }
        // return customer list
        return customerList;
    }

    /**
     * Returns a Customer object corresponding to the customer id specified
     * @param customerId The 4 digit ID of the customer
     * @return Customer A customer object corresponding to the user or null if the customer was not found
     */
    public Customer getCustomerByID(String customerId){

        String[] projection = {
                CustomerEntry.KEY_CUSTOMER_ID,
                CustomerEntry.KEY_FIRSTNAME,
                CustomerEntry.KEY_LASTNAME,
                CustomerEntry.KEY_EMAIL,
                CustomerEntry.KEY_IS_VIP,
        };
        String where = CustomerEntry.KEY_CUSTOMER_ID + "=?";
        String[] where_clause = {customerId};
        Cursor cursor = customer_db.query(
                CustomerEntry.TABLE_NAME,
                projection,
                where,
                where_clause,
                null,
                null,
                null);
        boolean moved;
        if (cursor != null) {
            moved = cursor.moveToFirst();
            if(!moved){
                return null;
            }
        }
        String first_name;
        String last_name;
        first_name = cursor.getString(cursor.getColumnIndexOrThrow(CustomerEntry.KEY_FIRSTNAME));
        last_name = cursor.getString(cursor.getColumnIndexOrThrow(CustomerEntry.KEY_LASTNAME));
        String email;
        email = cursor.getString(cursor.getColumnIndexOrThrow(CustomerEntry.KEY_EMAIL));
        int vip_int = cursor.getInt(cursor.getColumnIndexOrThrow(CustomerEntry.KEY_IS_VIP));
        boolean vip = vip_int == 1;
        String id = customerId;
        Customer contact = new Customer(id, first_name, last_name, email, vip);
        return contact;
    }

    public Customer getCustomerByEmail(String email){

        String[] projection = {
                CustomerEntry.KEY_CUSTOMER_ID,
                CustomerEntry.KEY_FIRSTNAME,
                CustomerEntry.KEY_LASTNAME,
                CustomerEntry.KEY_EMAIL,
        };
        String where = CustomerEntry.KEY_EMAIL + "= ?";
        String[] where_clause = { email};
        Cursor cursor = customer_db.query(
                CustomerEntry.TABLE_NAME,
                projection,
                where,
                where_clause,
                null,
                null,
                null);
        boolean moved;
        if (cursor != null) {
            moved = cursor.moveToFirst();
            if(!moved){
                return null;
            }
        }
        String first_name;
        String last_name;
        first_name = cursor.getString(cursor.getColumnIndexOrThrow(CustomerEntry.KEY_FIRSTNAME));
        last_name = cursor.getString(cursor.getColumnIndexOrThrow(CustomerEntry.KEY_LASTNAME));
        String id = cursor.getString(cursor.getColumnIndexOrThrow(CustomerEntry.KEY_CUSTOMER_ID));
        Customer contact = new Customer(id, first_name, last_name, email, false);
        return contact;
    }


    /**
     * Updates a customer in the database with the specified paramaters
     * @param customer_id
     * @param first_name
     * @param last_name
     * @param new_email
     */
    public void updateCustomer(String customer_id, String first_name, String last_name, String new_email){
        Customer tmpCust = getCustomerByID(customer_id);
        ContentValues values = new ContentValues();
        values.put(CustomerEntry.KEY_FIRSTNAME, first_name);
        values.put(CustomerEntry.KEY_LASTNAME, last_name);
        values.put(CustomerEntry.KEY_EMAIL, new_email);

        customer_db.update(CustomerEntry.TABLE_NAME, values, CustomerEntry.KEY_CUSTOMER_ID + " = ?",
                new String[]{tmpCust.getId()});
    }

    public String getLastCustIDFromDB()
    {
        String[] projection = {
                CustomerEntry.KEY_CUSTOMER_ID,
        };

        Cursor c = customer_db.query(
                CustomerEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                CustomerEntry.KEY_CUSTOMER_ID + " Desc", // The sort order
                "1"
        );
        boolean moved = c.moveToFirst();
        String id_string;
        if(moved) {
            id_string = c.getString(
                    c.getColumnIndexOrThrow(CustomerEntry.KEY_CUSTOMER_ID));
        } else {
            id_string = "0";
        }
        return id_string;

    }

    public long insert_customer(String id, String firstName, String lastName, String email)
    {
        ContentValues values = new ContentValues();

        values.put(CustomerEntry.KEY_CUSTOMER_ID, id);
        values.put(CustomerEntry.KEY_FIRSTNAME, firstName);
        values.put(CustomerEntry.KEY_LASTNAME, lastName);
        values.put(CustomerEntry.KEY_IS_VIP, 0);
        values.put(CustomerEntry.KEY_EMAIL, email);
        long newRowID;
        newRowID = customer_db.insert(
                CustomerEntry.TABLE_NAME,
                null,
                values);
        return newRowID;
    }

    public boolean validate_customer_ID(String id)
    {
        String[] projection = {
                CustomerEntry.KEY_CUSTOMER_ID,
        };

        String where_str = CustomerEntry.KEY_CUSTOMER_ID + "= ?";
        String[] where_clause = { id };
        Cursor c = customer_db.query(
                CustomerEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                where_str,                                // The columns for the WHERE clause
                where_clause,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                // don't filter by row groups
                null                                 // The sort order
        );
        return c.moveToFirst();
    }

    public void setVipByID(String customer_id){
        Customer tmpCust = getCustomerByID(customer_id);
        ContentValues values = new ContentValues();
        values.put(CustomerEntry.KEY_FIRSTNAME, tmpCust.getFirstName());
        values.put(CustomerEntry.KEY_LASTNAME, tmpCust.getLastName());
        values.put(CustomerEntry.KEY_IS_VIP, 1);
        values.put(CustomerEntry.KEY_EMAIL, tmpCust.getEmail());

        customer_db.update(CustomerEntry.TABLE_NAME, values, CustomerEntry.KEY_CUSTOMER_ID + " = ?",
                new String[]{tmpCust.getId()});
    }

    public boolean getVipStatusById(String customer_id){
        String[] projection = {
                CustomerEntry.KEY_CUSTOMER_ID,
        };

        String where_str = CustomerEntry.KEY_CUSTOMER_ID + "= ?";
        String[] where_clause = {customer_id};
        Cursor c = customer_db.query(
                CustomerEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                where_str,                                // The columns for the WHERE clause
                where_clause,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                // don't filter by row groups
                null                                 // The sort order
        );
        boolean isVip= false;
        if(c !=null){
         isVip = (1==c.getInt(c.getColumnIndexOrThrow(CustomerEntry.KEY_IS_VIP)));
        }
        return isVip;
    }
}
