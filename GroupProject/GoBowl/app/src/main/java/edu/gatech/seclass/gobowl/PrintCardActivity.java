package edu.gatech.seclass.gobowl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import edu.gatech.seclass.services.PrintingService;

public class PrintCardActivity extends AppCompatActivity {

    private ToastUtil toastUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_card);
        toastUtil = new ToastUtil(getApplicationContext());
    }

    public void onPrintCard(View view)
    {
        CustomerTable customerTable = ((BowlingApplication)getApplication()).getCustTable();
        EditText emailET = (EditText) findViewById(R.id.emailET);
        String emailStr = emailET.getText().toString();

        Customer cust = customerTable.getCustomerByEmail(emailStr);
        if(cust == null) {
            toastUtil.toast_msg("Customer with that email not found");
        } else {
            boolean printed = PrintingService.printCard(cust.getFirstName(), cust.getLastName(), cust.getId());
            if(printed){
                toastUtil.toast_msg("Customers card has been sent to printer");
            } else {
                toastUtil.toast_msg("Problem with printer. Please try again!");
            }

        }
    }
}
