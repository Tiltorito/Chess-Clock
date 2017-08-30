package com.example.chessclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<ChessMode> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.listView);

        list.add(new ChessMode("Fischer rapid", 10, 0 ,5));
        list.add(new ChessMode("Blitz",5,0,0));
        list.add(new ChessMode("Fischer", 5, 0, 5));
        list.add(new ChessMode("Bullet",2,0,1));
        list.add(new ChessMode("Delay Bullet",1,0,2));

        listView.setAdapter(new CustomArrayAdapter(this,list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, ClockActivity.class);
                ChessMode mode = list.get(i);
                intent.putExtra("TIME_PER_PLAYER", mode.getMinutes() * 60 * 1000 + mode.getSeconds() * 1000);
                intent.putExtra("EXTRA_TIME_PER_MOVE", mode.getExtraTime());
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
