package edu.gatech.seclass.gobowl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.gatech.seclass.services.QRCodeService;

public class EditCustomerActivity extends AppCompatActivity {
    private Button cardScanButton;
    private Button doneButton;
    private Button cancelButton;

    private EditText firstNameText;
    private EditText lastNameText;
    private EditText emailText;
    private EditText manualIDText;
    private Customer editedCustomer;
    private CustomerTable custTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);
        firstNameText = (EditText)findViewById(R.id.firstNameET);
        lastNameText = (EditText)findViewById(R.id.lastNameET);
        emailText = (EditText)findViewById(R.id.emailEditBtn);
        cardScanButton = (Button)findViewById(R.id.scanCardBtn);
        doneButton = (Button)findViewById(R.id.doneBtn);
        cancelButton = (Button)findViewById(R.id.cancelBtn);
        manualIDText = (EditText) findViewById(R.id.manualIDET);
        custTable = ((BowlingApplication)getApplication()).getCustTable();
        //nameText.setText();

    }

    public void onCancelBtnHandler(View view){
        cancelButton.setEnabled(false);
        doneButton.setEnabled(false);
        firstNameText.setText("");
        lastNameText.setText("");
        emailText.setText("");
        cardScanButton.setEnabled(true);
        Toast.makeText(EditCustomerActivity.this, "Updating is Canceled!",
                Toast.LENGTH_SHORT).show();
    }

    public void onDoneBtnHandler(View view){
        CustomerTable customerDB;
        doneButton.setEnabled(false);
        cancelButton.setEnabled(false);
        String first_name = firstNameText.getText().toString();
        String last_name = lastNameText.getText().toString();
        String new_email = emailText.getText().toString();
        //nameText.setText(new_name);
        //emailText.setText(new_email);

        if(!new_email.equals("") && !first_name.equals("")){
            custTable.updateCustomer(editedCustomer.getId(), first_name, last_name, new_email);
        }
        else if(!new_email.equals("") && first_name.equals("")){
            custTable.updateCustomer(editedCustomer.getId(),
                    editedCustomer.getFirstName(),
                    editedCustomer.getLastName(),
                    new_email);
        }
        else if(new_email.equals("") && !first_name.equals("")){
            custTable.updateCustomer(editedCustomer.getId(),
                    editedCustomer.getFirstName(),
                    editedCustomer.getLastName(),
                    editedCustomer.getEmail());
        }
        Toast.makeText(EditCustomerActivity.this, "Successfully Updated Customer Information",
                Toast.LENGTH_SHORT).show();
        firstNameText.setText("");
        lastNameText.setText("");
        emailText.setText("");
        cardScanButton.setEnabled(true);
    }


    public void onScanCardHandler(View view){

        String cust_id = "";
        Log.d("customer ID", cust_id);
        cust_id =  QRCodeService.scanQRCode();
        if(cust_id == "ERR"){
            Toast.makeText(EditCustomerActivity.this, "Machine problem, Try scanning your card again",
                    Toast.LENGTH_SHORT).show();
            //cust_id =  QRCodeService.scanQRCode();
        }
        else{
            doneButton.setEnabled(true);
            cancelButton.setEnabled(true);
            Toast.makeText(EditCustomerActivity.this, "Customer ID is "+cust_id,
                    Toast.LENGTH_SHORT).show();
            //cardScanButton.setEnabled(false);

            editedCustomer =  custTable.getCustomerByID(cust_id);
            /*Log.d("0", editedCustomerInfo[0]);
            Log.d("1", editedCustomerInfo[1]);
            Log.d("2", editedCustomerInfo[2]);*/
            Log.d("returned customer", editedCustomer.printInfo());
            //cardScanButton.setEnabled(false);

            cardScanButton.setEnabled(false);
            fillInCustFields();
        }

    }

    public void onManualCardHandler(View view)
    {
        editedCustomer = custTable.getCustomerByID(manualIDText.getText().toString());
        if(editedCustomer == null) {
            Toast.makeText(EditCustomerActivity.this, "Customer not found!", Toast.LENGTH_SHORT).show();
            return;
        }
        fillInCustFields();
    }

    public void fillInCustFields()
    {
        firstNameText.setText(editedCustomer.getFirstName());
        lastNameText.setText(editedCustomer.getLastName());
        emailText.setText(editedCustomer.getEmail());
    }
}