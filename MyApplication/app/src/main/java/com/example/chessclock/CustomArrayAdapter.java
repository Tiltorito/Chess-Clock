package com.example.chessclock;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Μπάμπης Μπιλλίνης on 30/8/2017.
 */

public class CustomArrayAdapter extends ArrayAdapter<ChessMode> {

    public CustomArrayAdapter(Context context, List<ChessMode> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        ChessMode chessMode = getItem(position);

        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView timeTextView = convertView.findViewById(R.id.timeTextView);

        titleTextView.setText(chessMode.getTitle());
        timeTextView.setText(chessMode.getTimeToString());

        return convertView;
    }
}
