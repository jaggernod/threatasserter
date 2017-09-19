package com.example.threatasserter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ThreatAsserterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Check logcat!");
        setContentView(tv);
    }

}
