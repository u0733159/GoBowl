package edu.gatech.seclass.gobowl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestLaneDone extends AppCompatActivity {

    private static boolean first_run = true;
    private ToastUtil toastUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_lane_done);
        Bundle extras = getIntent().getExtras();
        int lane_num = 0;
        if(extras != null) {
            lane_num = extras.getInt("EXTRA_LANE_NUM");
        }
        toastUtil = new ToastUtil(getApplicationContext());
        TextView bowlingLaneTV = (TextView) findViewById(R.id.bowlingLaneTV);
        bowlingLaneTV.setText(Integer.toString(lane_num));
        Manager manager = ((BowlingApplication)getApplication()).getManager();
        BowlingLaneEvent bowlingLaneEvent = manager.peekBowlingLaneEvent(lane_num);
        String startTimeStr = "";
        String nonP = "";
        if(bowlingLaneEvent != null) {
            bowlingLaneEvent.startBowlingEvent();
            Date startTime = bowlingLaneEvent.getStartTime();
            SimpleDateFormat formatter = new SimpleDateFormat("E MM-dd-yyyy kk:mm:ss");
            startTimeStr =  formatter.format(startTime);
            nonP = bowlingLaneEvent.getNonPrimaryCustomerIDs();
        }
        TextView startTimeTV = (TextView) findViewById(R.id.startTimeTV);
        startTimeTV.setText(startTimeStr);
        BowlingLaneEventTable bowlingDB = ((BowlingApplication)getApplication()).getBowlingEventTable();
        Log.d("Requested Lane ID: ", String.valueOf(bowlingLaneEvent.getLaneNumber()));
        String time = bowlingLaneEvent.getStartTime().toString();
        Log.d("Bowling Lane startTime:", time);
        Log.d("Non Primary Customer: ", nonP);
        if(first_run) {
            if (lane_num != 0)
                bowlingDB.addBowlingLane(bowlingLaneEvent);
        }
        first_run = false;

    }

    public void OnGoBowlingHandler(View view)
    {
        Intent intent = new Intent(this, FrontScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        first_run = true;
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        toastUtil.toast_msg("You can not navigate back from this page.");
    }
}
