package edu.gatech.seclass.gobowl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class YearlyEventActivity extends AppCompatActivity {

    private BillingTable billingTable;
    private CustomerTable mCustomerTable;
    private Button promoteVips;
    private Button showVips;
    private ListView vipCustomerList;
    private List<Customer> customers;
    private ArrayList<String> vipCustomers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yearly_event);
        billingTable = ((BowlingApplication)getApplication()).getBillingTable();
        mCustomerTable = ((BowlingApplication)getApplication()).getCustTable();
        showVips = (Button)findViewById(R.id.showVipBtn);
        promoteVips = (Button)findViewById(R.id.promoteVipBtn);
        vipCustomerList = (ListView)findViewById(R.id.vipListView);
        customers = mCustomerTable.getCustomersList();
        vipCustomers = new ArrayList<String>();
        for(Customer cust:customers){
            String id = cust.getId();
            Log.d("Id is", id);
            double cost = billingTable.getPastYearBillsForCust(cust.getId());
            Log.d("cost is", String.valueOf(cost));
            if(cost >= 500){

                String vipEntry = cust.getFullName() + " " + cust.getId()+ " "+ String.valueOf(cost);
                vipCustomers.add(vipEntry);
            }
        }
    }

    public void onPromoteVipsHandler(View view){
        if(vipCustomers.size()!=0){
            for(Customer cus:customers){
                mCustomerTable.setVipByID(cus.getId());
            }
            Toast.makeText(getApplicationContext(),
                    "Finished changing status for all vip candidates for next year!" , Toast.LENGTH_SHORT)
                    .show();
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "No Vip Customers yet!" , Toast.LENGTH_SHORT)
                    .show();
        }

    }

    public void onShowVipHandler(View view)
    {
        if(vipCustomers.size()!=0){
            Log.d("show VIp", "VIPPPP");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, vipCustomers);


            // Assign adapter to ListView
            vipCustomerList.setAdapter(adapter);

            vipCustomerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int row, long id) {

                    // ListView Clicked item value
                    String  itemValue    = (String) vipCustomerList
                            .getItemAtPosition(row);
                    String[] scoreInfor = itemValue.split(" ");

                    Toast.makeText(getApplicationContext(),
                            "Full Name: "+ scoreInfor[0] + " "+ scoreInfor[1] + "\nID: "+ scoreInfor[2]+";\nSpending of Past year: " + scoreInfor[3] , Toast.LENGTH_LONG)
                            .show();
                }

            });
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "No Vip Customers yet!" , Toast.LENGTH_SHORT)
                    .show();
        }

    }
}

