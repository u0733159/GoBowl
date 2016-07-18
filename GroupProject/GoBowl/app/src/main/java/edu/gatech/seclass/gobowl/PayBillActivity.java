package edu.gatech.seclass.gobowl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import edu.gatech.seclass.services.CreditCardService;
import edu.gatech.seclass.services.PaymentService;

public class PayBillActivity extends AppCompatActivity {
    private double totalDue;
    private double remainingDue;
    private double individualPay;
    private int number_of_splits = -1;
    private int splits_remaining = -1;
    private String gameEndTime;
    private Spinner creditCardSpinner;
    private TextView individualDue;
    private EditText individualScore;
    private EditText individualID;
    private Button confirmCreditCards;
    private ScoreTable scores;
    private int laneNumber;
    private int creditCardNumber;
    private Button swipeCreditCard;
    private CreditCardService mCreditCardService;
    private PaymentService mPaymentService;
    private ToastUtil toastUtil;
    private int lane_number = -1;
    private BowlingLaneEvent ble;
    private BowlingLaneEventTable blet;
    private BillingTable billingTable;
    private Manager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bill);
        toastUtil = new ToastUtil(getApplicationContext());
        creditCardSpinner = (Spinner)findViewById(R.id.creditCardNumSpinner);
        individualDue = (TextView)findViewById(R.id.individualDueText);
        confirmCreditCards = (Button)findViewById(R.id.confirmCreditCardNumber);
        scores = ((BowlingApplication)getApplication()).getScore_table();
        swipeCreditCard = (Button)findViewById(R.id.SwipeCardBtn);
        swipeCreditCard.setEnabled(false);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            totalDue = extras.getDouble("Total_Fee_Due");
            lane_number = extras.getInt("Bowling_Lane_ID");
            laneNumber = extras.getInt("Bowling_Lane_ID");
        }
        remainingDue = totalDue;
        manager = ((BowlingApplication)getApplication()).getManager();
        ble = manager.peekBowlingLaneEvent(lane_number);
        blet = ((BowlingApplication)getApplication()).getBowlingEventTable();
        billingTable = ((BowlingApplication)getApplication()).getBillingTable();

        ArrayList<Integer> choices = new ArrayList<Integer>();
        for(int i=0; i< ble.getNonPrimaryCustomers().size() + 1; i++) {
            choices.add(i+1);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, choices);
        if (creditCardSpinner != null) {
            creditCardSpinner.setAdapter(adapter);
        }

        assert creditCardSpinner != null;
        creditCardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                creditCardNumber = Integer.parseInt(parent.getItemAtPosition(pos).toString());

                individualPay = totalDue / creditCardNumber;
                individualDue.setText(Double.toString(individualPay));

                String rounded_str = String.format("$%.2f", individualPay);
                individualDue.setText(rounded_str);
                String view_value = parent.getItemAtPosition(pos).toString();
                number_of_splits = Integer.valueOf(view_value);
                splits_remaining = number_of_splits;
                //creditCardSpinner.setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

    }

    public void onConfirmCreditCardsNumberHandlder(View view){
        creditCardSpinner.setEnabled(false);
        swipeCreditCard.setEnabled(true);

    }

    public void onSwipeCardHandler(View view)
    {
        /* Don't let them press the button too many times */
        String card_ret = CreditCardService.readCreditCard();
        if(card_ret == "ERR") {
            toastUtil.toast_msg("There was problem with reading your card. Please try swipe again.");
            return;
        }
        if(splits_remaining == 0) {
            Log.w("PayBillActivity", "Ignoring extra button presses");
        }
        Log.d("PayBillActivity", "Credit card reader returned:" + card_ret);
        String[] split_vals = card_ret.split("#");
        String firstName = split_vals[0];
        String lastName = split_vals[1];
        String ccNumber = split_vals[2];
        String expDateStr = split_vals[3];
        String securityCode = split_vals[4];

        Date expDate = null;
        DateFormat formatter = new SimpleDateFormat("MMddyyyy", Locale.ENGLISH);
        try {
            expDate = formatter.parse(expDateStr);
        } catch (ParseException pe) {
            Log.e("PaybillActivity", "Fatal: Failed to parse expriry date from card reader!");
        }
        boolean success = PaymentService.processPayment(firstName,
                lastName,
                ccNumber,
                expDate,
                securityCode,
                individualPay);
        if (success) {
            String fullname = firstName + " " + lastName;
            toastUtil.toast_msg("Payment from " + fullname + " successfull", Toast.LENGTH_SHORT);
            remainingDue -= individualPay;
            splits_remaining--;
            if(splits_remaining == 0) {
                blet.deleteBowlingLane(lane_number);
                manager.unregisterBowlingLaneEvent(ble);
                manager.releaseBowlingLane(lane_number);
                Toast t = toastUtil.toast_msg("Payments successfull. Thank you for using GoBowling. Have a nice day!");
                billingTable.addBillingEntry(ble.getPrimaryCustomer().getId(), ble.getEndTime(), totalDue);
                swipeCreditCard.setEnabled(false);
                returnToFrontPageAfterDelay(t.getDuration());
            }
        } else {
            toastUtil.toast_msg("Sorry, your payment failed. Please try again or use another card");
        }
    }

    /*public void onSwipeCardHandler(View view) {
        String readCardResult = mCreditCardService.readCreditCard();
        if (readCardResult != "ERR") {
            String[] creditCardInfors = readCardResult.split("#");
            String fName = creditCardInfors[0];
            String lName = creditCardInfors[1];
            String cardNumber = creditCardInfors[2];
            String expireDate = creditCardInfors[3];
            Date ExpireDate;
            DateFormat formatter = new SimpleDateFormat("MMddyyyy");
            double amount = individualPay;
            String securityCode = creditCardInfors[4];
            try {
                ExpireDate = formatter.parse(expireDate);
                boolean paymentProcessSuccessed = mPaymentService.processPayment(fName, lName, cardNumber, ExpireDate, securityCode, amount);
                while (!paymentProcessSuccessed) {
                    paymentProcessSuccessed = mPaymentService.processPayment(fName, lName, cardNumber, ExpireDate, securityCode, amount);
                }
                Toast.makeText(PayBillActivity.this, "Payment for this credit card was processed successfully!",
                        Toast.LENGTH_SHORT).show();
                swipeSuccessNum++;
                if (swipeSuccessNum == creditCardNumber) {
                    mBowlingLaneEventTable.deleteBowlingLane(laneNumber);
                    Toast.makeText(PayBillActivity.this, "All payments are processed.",
                            Toast.LENGTH_SHORT).show();
                    swipeCreditCard.setEnabled(false);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(PayBillActivity.this, "Reading Credit Card Failed, swipe again or try another one.",
                    Toast.LENGTH_SHORT).show();
        }
    }*/

    public void returnToFrontPageAfterDelay(final int toastDelay)
    {

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    int time_to_sleep;
                    if(toastDelay == 0) {
                        time_to_sleep = 2500;
                    } else {
                        time_to_sleep = 4000;
                    }
                    Thread.sleep(time_to_sleep);
                    retunToFronPage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public void retunToFronPage()
    {
        Intent intent = new Intent(this, FrontScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
