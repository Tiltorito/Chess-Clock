package com.example.chessclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class EditChessModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chess_mode);

        Intent intent = getIntent();
        /**
         * This is the index which describes the position of the view which was clicked the contextMenuOption.
         */
        int index = intent.getIntExtra("INDEX", -1);

        /**
         * Getting reference to the views. We declare the most Views final, in order to use them inside the listener.
         */
        Button finalButton = (Button)findViewById(R.id.addEditButton);

        final EditText titleEditText = (EditText)findViewById(R.id.titleEditText);
        final EditText secondsEditText = (EditText)findViewById(R.id.secondsEditText);
        final EditText extraEditText = (EditText)findViewById(R.id.extraEditText);

        final ChessModeLoader loader = ChessModeLoader.getInstance(); // obtain the list
        /**
         * Obtains a reference for the mode that we should edit.
         */
        final ChessMode mode  = loader.loadObjects(EditChessModeActivity.this).get(index);

        /**
         * Updates the Texts of the views. Because remember, here we update a current mode.
         * So we want to let user know what was the previous values.
         */
        titleEditText.setText(mode.getTitle());
        secondsEditText.setText(String.valueOf(mode.getMinutes() * 60 + mode.getSeconds()));
        extraEditText.setText(String.valueOf(mode.getExtraTime()));

        finalButton.setText("Update");
        finalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long seconds = 0;
                long extra = 0;
                try {
                    seconds = Long.parseLong(secondsEditText.getText().toString());
                    extra = Long.parseLong(extraEditText.getText().toString());
                }
                catch(NumberFormatException e) {
                    e.printStackTrace();
                }

                /**
                 * Changing the current Mode
                 */
                mode.setTitle(titleEditText.getText().toString());
                mode.setMinutes(seconds / 60);
                mode.setSeconds(seconds % 60);
                mode.setExtraTime(extra);

                loader.saveObjects(EditChessModeActivity.this); // updating the file
            }
        });
    }
}
