package edu.gatech.seclass.gobowl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DebugActivity extends AppCompatActivity {

    private ToastUtil toastUtil;
    private BowlingLaneEventTable bleTable;
    private BillingTable billingTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        toastUtil = new ToastUtil(getApplicationContext());
        bleTable = ((BowlingApplication)getApplication()).getBowlingEventTable();
        billingTable = ((BowlingApplication)getApplication()).getBillingTable();
    }

    public void handleClearRequestedLanes(View view)
    {
        bleTable.dropAllEntries();
        toastUtil.toast_msg("Done!");
    }

    public void addToBill(View view)
    {
        EditText custID = (EditText) findViewById(R.id.custIDET);
        EditText dateET = (EditText) findViewById(R.id.dateET);
        EditText amountET = (EditText) findViewById(R.id.amountET);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date addDate = null;
        try {
            addDate = formatter.parse(dateET.getText().toString());
        } catch (ParseException pe) {
            Log.d("debug", "!!!");
        }
        billingTable.addBillingEntry(custID.getText().toString(),
                addDate,
                Double.parseDouble(amountET.getText().toString()));
    }
}
