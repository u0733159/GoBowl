package edu.gatech.seclass.gobowl;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.Interval;
import org.joda.time.Period;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CheckOutActivity extends AppCompatActivity {
    public int laneNumber;
    private String endTimeInfo;
    private ArrayList<Integer> lane_numbers;
    private Button confirm;
    private Button checkOut;
    private TextView startText;
    private TextView endText;
    private TextView totalDueText;
    private Spinner laneNumberSpinner;
    private BowlingLaneEventTable bowlingLaneTable;
    private CustomerTable mCustomerTable;
    private double totalDue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        laneNumberSpinner = (Spinner)findViewById(R.id.laneIDSpinner);
        startText = (TextView)findViewById(R.id.startText);
        endText = (TextView)findViewById(R.id.endText);
        totalDueText = (TextView)findViewById(R.id.amountText);
        bowlingLaneTable = ((BowlingApplication)(getApplication())).getBowlingEventTable();
        mCustomerTable = ((BowlingApplication)(getApplication())).getCustTable();
        lane_numbers = bowlingLaneTable.getBowlingLaneIDs();
        confirm = (Button)findViewById(R.id.confirmBtn);
        checkOut = (Button)findViewById(R.id.checkOutBtn);
        checkOut.setEnabled(false);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, lane_numbers);
        if (laneNumberSpinner != null) {
            laneNumberSpinner.setAdapter(adapter);
        }

        assert laneNumberSpinner != null;
        laneNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                laneNumber = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });
    }

    /**
    * a. $20/hour from 9am to 5pm, Mon, Tue, Thu, Fri.
     b. $25/hour from 5pm to midnight, Mon, Tue, Thu, Fri.
     c. $30/hour all day on Sat and Sun.
     d. $10/hour all day on Wed.
    */


    /**
     * Once Confirm Button is pressed, the spinner value can not be changed anymore.
     * And the fee summary will be shown
     * @param view
     */
    public void onConfirmHandler(View view)
    {
        confirm.setEnabled(false);
        laneNumberSpinner.setEnabled(false);
        BowlingLaneEventTable bowlingLaneDB = ((BowlingApplication)getApplication()).getBowlingEventTable();
        Manager manager = ((BowlingApplication)getApplication()).getManager();
        BowlingLaneEvent ble = manager.peekBowlingLaneEvent(laneNumber);
        ArrayList<Integer> bowlingLaneIDs = bowlingLaneDB.getBowlingLaneIDs();

        SimpleDateFormat formatter = new SimpleDateFormat("E MM-dd-yyyy kk:mm:ss");
        Date endTime = new Date();
        String starTimeinfor = bowlingLaneDB.getStartTimeInfor(laneNumber);
        String custId= bowlingLaneDB.getPrimaryCusIDtomer(laneNumber);
        Customer pCustomer = mCustomerTable.getCustomerByID(custId);
        boolean isVip = pCustomer.getVip();
        //boolean isVip = mCustomerTable.getVipStatusById(pCustomer.getId());
        endTimeInfo = formatter.format(endTime);
        Date starTimeDate;
        Date endTimeDate;
        try{
            starTimeDate = formatter.parse(starTimeinfor);
            endTimeDate = formatter.parse(endTimeInfo);
            startText.setText(starTimeinfor);
            endText.setText(endTimeInfo);
            String[] start_time = starTimeinfor.split(" ");
            double discount = (isVip == true) ? 0.1 : 0;
            if(start_time[0].equals("Mon") || start_time[0].equals("Tue") || start_time[0].equals("Thu") || start_time[0].equals("Fri"))
            {
                double[] separatedHours = getTimeDifference_MTTF(starTimeDate, endTimeDate);
                double round_first = Math.ceil(separatedHours[0]);
                double round_second = Math.ceil(separatedHours[1]);
                totalDue = (1-discount)*(20* round_first + 25 * round_second);
            }
            else if(start_time[0].equals("Wed")){
                double hour = getTimeDifference_WSS(starTimeDate, endTimeDate);
                double roundUp = Math.ceil(hour);
                totalDue = 10* roundUp * (1-discount);
            }
            else{
                double roundUp = Math.ceil(getTimeDifference_WSS(starTimeDate, endTimeDate));
                totalDue = 30 * roundUp * (1-discount);
            }
            String total_due_str = String.format("$%.2f", totalDue);
            totalDueText.setText(total_due_str);

            bowlingLaneTable.endBowlingEvent(laneNumber);
            ble.endBowlingEvent();
            checkOut.setEnabled(true);
        }
        catch (ParseException e){
            e.printStackTrace();
        }
    }

    public void onCheckOutHandler(View vie){
        Intent nextIntent = new Intent(this, ScoreActivity.class);
        nextIntent.putExtra("Total_Fee_Due", totalDue);
        nextIntent.putExtra("Bowling_Lane_ID", laneNumber);
        nextIntent.putExtra("Game_End_Time", endTimeInfo);
        startActivity(nextIntent);
    }
    /**
     *
     * @param startDate A Certain time of Mon, Tue, Thu or Fri between 9am and Midnight
     * @param endDate A Certain time of Mon, Tue, Thu or Fri between 9am and Midnight
     * @return a double array with two elements, numbers of hours before 5pm and after 5pm
     */
    public static double[] getTimeDifference_MTTF(Date startDate, Date endDate)
    {
        Log.d("MTTF", "Its Friday");
        SimpleDateFormat formatter = new SimpleDateFormat("E MM-dd-yyyy kk:mm:ss");
        String dateStartInfo = formatter.format(startDate);
        String dateEndInfo = formatter.format(endDate);
        String[] startInfo = dateStartInfo.toString().split(" ");
        String[] endInfo = dateEndInfo.toString().split(" ");
        String fivePm = startInfo[0] + " " + startInfo[1] + " "+ startInfo[2] + " "+"17:00:00";
        String afterFive = startInfo[0] + " " + startInfo[1] + " "+endInfo[2];
        Log.d("afterFive", afterFive);
        Date fivePmDate;
        Date afterFiveDate;
        double[] hourCounts = new double[2];
        try{
            fivePmDate = formatter.parse(fivePm);
            afterFiveDate = formatter.parse(afterFive);

            Interval interval_before5 =
                    new Interval(startDate.getTime(), fivePmDate.getTime());
            Interval interval1_after5 =
                    new Interval(fivePmDate.getTime(), afterFiveDate.getTime());

            Period period1 = interval_before5.toPeriod();
            Period period2 = interval1_after5.toPeriod();
            hourCounts[0] = period1.getHours() + (double)period1.getMinutes()/60 + (double)period1.getSeconds()/3600;
            hourCounts[1] = period2.getHours() + (double)period2.getMinutes()/60 + (double)period2.getSeconds()/3600;
            Log.d("Hour", String.valueOf(hourCounts[0]));
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        return hourCounts;
    }

    public double getTimeDifference_WSS(Date startDate, Date endDate){
        Log.d("WSS", "Hour Computation");
        Interval interval =
                new Interval(startDate.getTime(), endDate.getTime());
        Period period = interval.toPeriod();
        double hourAmount;
        hourAmount = 24 * period.getDays() + (double)period.getHours() + (double)period.getMinutes()/60 + (double)period.getSeconds()/3600;
        return hourAmount;
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(CheckOutActivity.this, "You have to press Go Check Out Button To Pay the Fee!", Toast.LENGTH_SHORT).show();
    }

}
