package edu.gatech.seclass.gobowl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ShowCustomersActivity extends AppCompatActivity
{
    private ListView showCustomer;
    private List<Customer> allCustomers;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_customers);
        showCustomer = (ListView)findViewById(R.id.listCustomerBtn);
        CustomerTable customer_database = ((BowlingApplication)getApplication()).getCustTable();
        allCustomers =  customer_database.getCustomersList();


        String[] values = new String[allCustomers.size()];
        for(int i = 0; i < values.length; i++){
            values[i] = allCustomers.get(i).getInfoSingleLine();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        showCustomer.setAdapter(adapter);

        showCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int row, long id) {

                // ListView Clicked item value
                String  itemValue    = (String) showCustomer
                        .getItemAtPosition(row);
                String[] customerinfor = itemValue.split(" ");
                // Show Alert
                String fullName = customerinfor[0] + " "+customerinfor[1];
                String email = customerinfor[2];
                String customerId = customerinfor[3];
                Toast.makeText(getApplicationContext(),
                        "ID :"+ customerId+"\nEmail: " + email + "\nFull Name: " + fullName, Toast.LENGTH_LONG)
                        .show();

        }

    });
    }
}

