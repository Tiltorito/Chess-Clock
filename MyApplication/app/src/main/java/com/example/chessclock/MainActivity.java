package com.example.chessclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, ClockActivity.class);
        intent.putExtra("TIME_PER_PLAYER", 5000);
        intent.putExtra("EXTRA_TIME_PER_MOVE", 5);
        startActivity(intent);
    }
}
