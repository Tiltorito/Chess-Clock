package com.example.chessclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    private CustomArrayAdapter mAdapter;
    private ChessModeLoader mLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);

        registerForContextMenu(listView); // we register the listView for the contextMenu.

        mLoader = ChessModeLoader.getInstance(); // we are getting a reference for our loader.
        mAdapter = new CustomArrayAdapter(this, mLoader.loadObjects(this)); // creating our custom adapter
        listView.setAdapter(mAdapter); // we say the listView to use the adapter

        // and we add a callback method when a Item inside the listView is clicked.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, ClockActivity.class);
                ChessMode mode = mAdapter.getItem(i);
                intent.putExtra("TIME_PER_PLAYER", mode.getMinutes() * 60 * 1000 + mode.getSeconds() * 1000);
                intent.putExtra("EXTRA_TIME_PER_MOVE", mode.getExtraTime());
                MainActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.context_menu, menu); // inflate the contextMenu

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); // this inflates the app bar menu.
        return true;
    }

    /**
     * This method is called when a menuItem for the float menu is clicked in a view.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        /**
         * We need this, in order to learn the position of the view that is been clicked
         * and pass the index in the EditActivity, if the user want to edit the current mode.
         *
         * Moreover, we use this to learn the position for the deleteAction, so we can remove the object in the specified position.
         */
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();


        switch (item.getItemId()) {
            case R.id.editAction: // if the edit action has been selected
                Intent intent = new Intent(this, EditChessModeActivity.class); // creating a new Intent
                intent.putExtra("INDEX", info.position); // we obtain the position
                startActivity(intent); // starting the editActivity
                return true; // return true, because we handled successfully the event.

            case R.id.deleteAction: // if the delete action has been selected
                mLoader.loadObjects(this).remove(info.position);  // remove from the list the mode in specified position.
                mLoader.saveObjects(this); // update the file. (because we made a change above)
                mAdapter.notifyDataSetChanged(); // notify the adapter, that we changed his contents without his methods.
                return true;  // return true, because we handled successfully the event.

            default:
                return super.onContextItemSelected(item); // no clue what this option is, call super to handle it.
        }

    }

    /**
     * This method is called when a menuItem in the appbar is clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.add_option :
                // start the AddChessModeActivity
                intent = new Intent(this, AddChessModeActivity.class);
                startActivity(intent);
                return true;    // return true, because we handled successfully the event.
            case R.id.aboutUS :
                // start the AboutUSActivity
                intent = new Intent(this , AboutusActivity.class);
                startActivity(intent);
                return true;    // return true, because we handled successfully the event.
            default:
                return super.onOptionsItemSelected(item); // no clue what this option is, call super to handle it.
        }
    }
}
