package edu.gatech.seclass.gobowl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.gatech.seclass.services.QRCodeService;

public class RequestLaneActivity extends AppCompatActivity {
    private String userinput = "";
    private int cardsToScan=0;
    private CustomerTable custTable;
    private BowlingLaneEventTable bleTable;
    private BowlingLaneEvent bowlingEvent;
    private ToastUtil toastUtil;
    private EditText numPlayersET;
    private Button submitBtn;
    private Button scanNextCustBtn;
    private String primaryCustomerID;
    private Button scanIDCard;
    private Customer pCustomer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_lane);
        custTable = ((BowlingApplication)getApplication()).getCustTable();
        bleTable = ((BowlingApplication)getApplication()).getBowlingEventTable();
        toastUtil = new ToastUtil(getApplicationContext());
        numPlayersET = (EditText) findViewById(R.id.numberOfPlayersET);
        submitBtn = (Button) findViewById(R.id.submitButton);
        scanNextCustBtn = (Button) findViewById(R.id.scanNextCustBtn);
        scanIDCard = (Button)findViewById(R.id.scanIDBtn);
        numPlayersET.addTextChangedListener(new TextWatcher(){
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String playerNumber = numPlayersET.getText().toString();
                if (playerNumber.matches("^0"))
                {
                    // Not allowed
                    Toast.makeText(RequestLaneActivity.this, "Please type number start with non zero!", Toast.LENGTH_SHORT).show();
                    numPlayersET.setText("");
                    //return;
                }
                else{
                    userinput = playerNumber;
                }
            }
            @Override
            public void afterTextChanged(Editable arg0) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });
    }

    public void onScanIDHandler(View view)
    {
        scanIDCard.setEnabled(true);
        String primaryCustomerID = QRCodeService.scanQRCode();
        String text = "";
        if (primaryCustomerID == "ERR") {
            text = "There was an error scanning your card. Please try again.";
            toastUtil.toast_msg(text, Toast.LENGTH_SHORT);
            return;
        }

        scanIDCard.setEnabled(false);

        pCustomer = custTable.getCustomerByID(primaryCustomerID);
        if(pCustomer == null){
            String t_text = "Sorry user not found in database. Speak to manager!";
            toastUtil.toast_msg(t_text);
            scanIDCard.setEnabled(true);
            return;
        }
        String cust_fullname = pCustomer.getFullName();
        if (bleTable.isCustomerBowling(primaryCustomerID)) {
            toastUtil.toast_msg(cust_fullname + ", You are already bowling. First checkout!");
            scanIDCard.setEnabled(true);
            return;
        }

        text = "Welcome " + cust_fullname;
        toastUtil.toast_msg(text, Toast.LENGTH_SHORT);
        submitBtn.setEnabled(true);
        numPlayersET.setEnabled(true);
        bowlingEvent = new BowlingLaneEvent(pCustomer);
    }

    public void onSubmitHandler(View view)
    {
        if(userinput.equals("")){
            Toast.makeText(RequestLaneActivity.this, "Please type the number of players!",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            //Validate the number of players
            String input_num = numPlayersET.getText().toString();

            int num_players = Integer.parseInt(input_num);
            bowlingEvent.setTotalCustomers(num_players);
            if(num_players >= 2){
                scanNextCustBtn.setVisibility(View.VISIBLE);
                String t_text = "Please press the scan next card button for the " +
                        Integer.toString(num_players - 1) + " players";
                Toast.makeText(RequestLaneActivity.this, t_text, Toast.LENGTH_SHORT).show();
                cardsToScan = num_players -1;
            }
            else{
                Manager theManager = ((BowlingApplication)getApplication()).getManager();
                int bowling_lane = theManager.assignBowlingLane();
                bowlingEvent.setLaneNumber(bowling_lane);
                theManager.registerBowlingLaneEvent(bowlingEvent);
                Intent intent = new Intent(this, RequestLaneDone.class);
                intent.putExtra("EXTRA_LANE_NUM", bowling_lane);
                proceed_to_request_done(intent);
            }
        }

    }

    public void onScanNextCust(View view)
    {
        if(cardsToScan > 0)
        {
            String cust_id = QRCodeService.scanQRCode();
            if(cust_id == "ERR") {
                toastUtil.toast_msg("Error scanning card. Please scan again!");
                return;
            }
            Customer tmp_cust = custTable.getCustomerByID(cust_id);
            if(tmp_cust == null ) {
                toastUtil.toast_msg("Customer not found in DB!!");
                return;
            }
            else {
                String full_name = tmp_cust.getFullName();
                boolean added = false;
                try {
                    added = bowlingEvent.addNPCustomer(tmp_cust);
                } catch (BowlingLaneEvent.CustomerAlreadyAdded caa) {
                    toastUtil.toast_msg("The Customer has already been added, please scan another card!", Toast.LENGTH_SHORT);
                    return;
                }
                if(added == false){
                    toastUtil.toast_msg("The Customer has already been added, please scan another card!", Toast.LENGTH_SHORT);
                }
                else{
                    toastUtil.toast_msg("Welcome " + full_name, Toast.LENGTH_SHORT);
                    cardsToScan--;
                    if(cardsToScan == 0) {

                        Manager theManager = ((BowlingApplication)getApplication()).getManager();
                        int bowling_lane = theManager.assignBowlingLane();
                        bowlingEvent.setLaneNumber(bowling_lane);
                        theManager.registerBowlingLaneEvent(bowlingEvent);
                        Intent intent = new Intent(this, RequestLaneDone.class);
                        intent.putExtra("EXTRA_LANE_NUM", bowling_lane);
                        proceed_to_request_done(intent);
                    }
                }
            }
        } else {
            toastUtil.toast_msg("BUG! How did you get here?");
        }
    }

    public void proceed_to_request_done(Intent intent)
    {
        startActivity(intent);
    }
}
