package com.example.threatasserter;

import com.jaggernod.threatasserter.annotations.AssertMainThread;
import com.jaggernod.threatasserter.annotations.AssertWorkerThread;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickWithWorkerThread(View view) {
        AsyncTask.execute(new Runnable() {

            @AssertMainThread
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,
                               "Won't be displayed as will crash before.",
                               Toast.LENGTH_LONG).show();
            }

        });
    }

    @AssertWorkerThread
    public void onClickWithMainThread(View view) {
        // Looong task. But will crash before getting here.
        SystemClock.sleep(1000);
    }
}
