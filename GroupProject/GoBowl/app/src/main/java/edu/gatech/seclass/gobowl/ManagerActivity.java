package edu.gatech.seclass.gobowl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 *
 */

public class ManagerActivity extends AppCompatActivity {
    private Button add_customer;
    private Button edit_customer;
    private Button run_yearly_event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        add_customer = (Button)findViewById(R.id.add_customer);

    }

    public void onAddCustomerHandler(View view)
    {
        Intent intent = new Intent(this, AddCustomerActivity.class);
        startActivity(intent);
    }

    public void onEditCustomerHandler(View view){
        Intent intent = new Intent(this, EditCustomerActivity.class);
        startActivity(intent);
    }

    public void onShowCustomersHandler(View view){
        Intent intent = new Intent(this, ShowCustomersActivity.class);
        startActivity(intent);
    }

    public void onPrintCard(View view) {
        Intent intent = new Intent(this, PrintCardActivity.class);
        startActivity(intent);
    }

    public void onYearlyTasks(View view)
    {
        Intent intent = new Intent(this, YearlyEventActivity.class);
        startActivity(intent);
    }
}
