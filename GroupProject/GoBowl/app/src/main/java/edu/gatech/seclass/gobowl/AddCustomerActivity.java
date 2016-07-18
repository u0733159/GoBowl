package edu.gatech.seclass.gobowl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.gatech.seclass.services.ServicesUtils;

public class AddCustomerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
    }

    public void onAddCustomerHandler(View view)
    {
        EditText first_name = (EditText)findViewById(R.id.fNameText);
        EditText last_name = (EditText)findViewById(R.id.lNameText);
        EditText email_et = (EditText) findViewById(R.id.emailText);
        String fname = first_name.getText().toString();
        String lname = last_name.getText().toString();
        String email_str = email_et.getText().toString();
        //EditText cust_fullname_et = (EditText) findViewById(R.id.fullnameText);


        if(!fname.equals("") && !lname.equals("") && !email_str.equals("")){
            Manager manager= ((BowlingApplication)getApplication()).getManager();
            String ret_id;
            ret_id = manager.addCustomer(fname, lname, email_str);

            if (ret_id != "") {
                first_name.setText("");
                last_name.setText("");
                email_et.setText("");
            } else {
                return;
            }

            ServicesUtils.addCustomer(fname, lname, "678967896789", "12312016", "999", ret_id);
        }


    }
}
