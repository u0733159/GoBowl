package edu.gatech.seclass.gobowl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.gatech.seclass.services.QRCodeService;

public class ShowHistoricScoresActivity extends AppCompatActivity {

    private TextView customerInfo;
    private ListView scoreList;
    private ArrayList<String> historicScores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_historic_scores);
        customerInfo = (TextView) findViewById(R.id.customerScores);
        scoreList = (ListView)findViewById(R.id.scoreListView);
    }

    public void onScanCardHandler(View view){
        String customer_id = QRCodeService.scanQRCode();
        if(customer_id=="ERR"){
            Toast.makeText(ShowHistoricScoresActivity.this, "Machine Problem happened in scanning card, scan again!", Toast.LENGTH_SHORT).show();
            return;
        }
        historicScores = ((BowlingApplication)(getApplication())).getScore_table().getScoreEntriesByCustomerID(customer_id);
        //String customerName = ((BowlingApplication)(getApplication())).getCustTable().getCustomerByID(customer_id).getFullName();
        customerInfo.setText("Customer: "+customer_id+"'s Score History(Click to see detail)");
        if(historicScores.size()==0){
            Toast.makeText(ShowHistoricScoresActivity.this, "No Saved Score History Yet!", Toast.LENGTH_LONG).show();
            scoreList.setAdapter(null);
        }
        else{
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, historicScores);


            // Assign adapter to ListView
            scoreList.setAdapter(adapter);

            scoreList.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int row, long id) {

                    // ListView Clicked item value
                    String  itemValue    = (String) scoreList
                            .getItemAtPosition(row);
                    String[] scoreInfor = itemValue.split(" ");

                    Toast.makeText(getApplicationContext(),
                            "Time: "+ scoreInfor[0] + " "+ scoreInfor[1] + " "+ scoreInfor[2]+";\nScore: " + scoreInfor[3] , Toast.LENGTH_LONG)
                            .show();
                }

            });
        }

    }

}
