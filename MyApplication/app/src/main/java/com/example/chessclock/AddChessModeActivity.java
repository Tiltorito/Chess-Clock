package com.example.chessclock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class AddChessModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chess_mode);

        Button finalButton = (Button)findViewById(R.id.addEditButton); // this is the button, which when you press it, it should add the new mode in the file/listView

        finalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText titleEditText = (EditText)findViewById(R.id.titleEditText);
                EditText secondsEditText = (EditText)findViewById(R.id.secondsEditText);
                EditText extraEditText = (EditText)findViewById(R.id.extraEditText);

                ChessModeLoader loader = ChessModeLoader.getInstance(); // taking a instance for our loader
                List<ChessMode> list = loader.loadObjects(AddChessModeActivity.this); // taking the list

                long seconds = 0; // init
                long extra = 0; //init
                try {
                    seconds = Long.parseLong(secondsEditText.getText().toString());
                    extra = Long.parseLong(extraEditText.getText().toString());
                }
                catch(NumberFormatException e) { // oups, this should never happen.
                    e.printStackTrace();
                }


                ChessMode mode = new ChessMode(titleEditText.getText().toString(), seconds / 60, seconds % 60, extra); // we create a new chess mode
                list.add(mode); // and then we add it on our list
                loader.saveObjects(AddChessModeActivity.this); // updating the file.
            }
        });
    }


}
