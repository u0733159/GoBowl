package edu.gatech.seclass.gobowl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CustomerActivity extends AppCompatActivity {
    private Button checkout;
    private Button requestlane;
    private Button showscores;
    int bowlingLaneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        requestlane  = (Button)findViewById(R.id.requestLaneBtn);
        checkout = (Button)findViewById(R.id.checkout);
        BowlingLaneEventTable bowlingLanes = ((BowlingApplication)getApplication()).getBowlingEventTable();
        bowlingLaneNumber = bowlingLanes.countRuningBowlingLanes();
        /*
        if(bowlingLaneNumber == 0){
            checkout.setEnabled(false);
        }
        else{
            checkout.setVisibility();
        }
        */

    }

    public void onRequestLaneHandler(View view)
    {
        Intent intent = new Intent(this, RequestLaneActivity.class);
        startActivity(intent);
    }

    public void onCheckoutHandler(View view){
        if(bowlingLaneNumber==0){
            Toast.makeText(CustomerActivity.this, "There is no BowlingLane waiting to be checkouted", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(this, CheckOutActivity.class);
            startActivity(intent);
        }

    }

    public void onShowScoreHandler(View view){
        Intent intent = new Intent(this, ShowHistoricScoresActivity.class);
        startActivity(intent);
    }

}
