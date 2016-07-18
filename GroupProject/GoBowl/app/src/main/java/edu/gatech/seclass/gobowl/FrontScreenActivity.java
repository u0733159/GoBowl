package edu.gatech.seclass.gobowl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FrontScreenActivity extends AppCompatActivity {

    private int debug_pressed_count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_screen);
    }

    public void onCustomerHandler(View view)
    {
        Intent intent = new Intent(this, CustomerActivity.class);
        startActivity(intent);
    }

    public void onManagerHandler(View view)
    {
        Intent intent = new Intent(this, ManagerActivity.class);
        startActivity(intent);

    }
    public void onExitHandler(View view)
    {
        this.finish();
        //System.exit(0);
    }

    public void onDebugUtils(View view)
    {
        debug_pressed_count++;
        if(debug_pressed_count >= 7) {
            debug_pressed_count = 0;
            Intent intent = new Intent(this, DebugActivity.class);
            startActivity(intent);
        }
    }

    public void onHelp(View view)
    {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        //Don't allow jumping back to previous activity
    }


}
