package edu.gatech.seclass.gobowl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {

    private ToastUtil toastUtil;
    private int lane_number = 0;
    private List<CheckBox> checkBoxes = new ArrayList<CheckBox>();
    private List<Customer> wantScoresSaved = new ArrayList<Customer>();
    private Customer primary_customer;
    private List<Customer> np_customers;
    private BowlingLaneEvent ble;
    private ScoreTable score_tbl;
    private int have_scores = 0;
    private double total_due = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        toastUtil = new ToastUtil(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            lane_number = extras.getInt("Bowling_Lane_ID");
            total_due = extras.getDouble("Total_Fee_Due");
        }
        score_tbl = ((BowlingApplication) getApplication()).getScore_table();
        /*Cleanup from any previous calls*/
        wantScoresSaved.clear();
        checkBoxes.clear();
        primary_customer = null;
    }

    public void onYesHandler(View view)
    {
        Log.d("ScoreActivity", "onYesHandler");
        Button saveScoreBtn = (Button) findViewById(R.id.saveScoresBtn);
        saveScoreBtn.setVisibility(View.VISIBLE);
        BowlingLaneEventTable blet = ((BowlingApplication) getApplication()).getBowlingEventTable();
        Manager manager = ((BowlingApplication) getApplication()).getManager();


        ble = blet.getBowlingEventFromLane(lane_number);
        primary_customer = ble.getPrimaryCustomer();
        np_customers = ble.getNonPrimaryCustomers();
        LinearLayout inLayout = (LinearLayout) findViewById(R.id.innerVerticalLayout);
        CheckBox nCheckBox = new CheckBox(this);
        checkBoxes.clear();
        nCheckBox.setId(0);
        nCheckBox.setText(primary_customer.getFullName());
        nCheckBox.setOnClickListener(onUserSelected(nCheckBox));
        checkBoxes.add(nCheckBox);
        inLayout.addView(nCheckBox);
        for(int i=0; i < np_customers.size(); i++ ) {
            Customer cust = np_customers.get(i);
            nCheckBox = new CheckBox(this);
            nCheckBox.setId(i+1);
            nCheckBox.setText(cust.getFullName());
            nCheckBox.setOnClickListener(onUserSelected(nCheckBox));
            checkBoxes.add(nCheckBox);
            inLayout.addView(nCheckBox);
        }
    }

    public void onNoHandler(View view)
    {
        startPayment();
    }

    public View.OnClickListener onUserSelected(final Button button)
    {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d("ScoreActivity", "ID:" + Integer.toString(v.getId()));
            }
        };
    }

    public void OnSaveScores(View view)
    {
        for(CheckBox c: checkBoxes) {
           if(c.isChecked()) {
               int id = c.getId();
               if(id == 0) {
                   //Primary customer was checked
                   wantScoresSaved.add(primary_customer);
               } else {
                   int ind = id -1;
                   wantScoresSaved.add(np_customers.get(ind));
               }
           }
        }
        final int needed_scores = wantScoresSaved.size();
        have_scores = 0;
        for(final Customer c: wantScoresSaved) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String custFullName;
            custFullName = c.getFullName();
            builder.setTitle("Enter the score for " + custFullName);

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String m_score = input.getText().toString();
                    Score score = new Score(Integer.parseInt(m_score), ble.getEndTime(), c.getId());
                    score_tbl.addScoreEntry(score);
                    have_scores++;
                    if(have_scores == needed_scores) {
                        startPayment();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }

    }

    public void startPayment()
    {
        Intent intent = new Intent(ScoreActivity.this, PayBillActivity.class);
        intent.putExtra("Total_Fee_Due", total_due);
        intent.putExtra("Bowling_Lane_ID", lane_number);
        startActivity(intent);
    }
}
