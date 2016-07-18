package edu.gatech.seclass.gobowl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HelpActivity extends AppCompatActivity {


    private ToastUtil toastUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        toastUtil = new ToastUtil(getApplicationContext());
    }

    public void onLoadUtilities(View view)
    {
        ((BowlingApplication)getApplication()).addCustomersToSystem();
        toastUtil.toast_msg("Customers from DB have been loaded");
    }
}
