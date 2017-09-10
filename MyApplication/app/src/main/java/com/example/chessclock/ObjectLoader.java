package com.example.chessclock;

import android.content.Context;

/**
 * Created by harry on 9/10/17.
 */

public interface ObjectLoader<E> {
    E loadObjects(Context context);
    void saveObjects(Context context);
}
