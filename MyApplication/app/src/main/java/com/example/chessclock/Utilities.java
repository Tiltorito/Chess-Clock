package com.example.chessclock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harry on 9/10/17.
 */

public final class Utilities {

    private static final List<ChessMode> list = new ArrayList<>();

    private Utilities() {

    }

    public static List<ChessMode> loadChessModes() {
        return list;
    }

    public static void saveChessModes() {

    }
}
