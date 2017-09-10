package com.example.chessclock;

import android.content.Context;
import android.util.Log;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by harry on 9/10/17.
 */

public class ChessModeLoader implements ObjectLoader<List<ChessMode>> {

    private static final String LOG_TAG = ChessModeLoader.class.getSimpleName(); // TAG

    /**
     * This Class uses the Sigleton pattern, thats means we can only create only one instance of this class
     */
    private static ChessModeLoader instance; // the instance

    /**
     * All chess modes will be there. Even the listView inside the mainActivity uses exactly this list, not even clone.
     */
    private List<ChessMode> list;

    private ChessModeLoader() {
    }

    public static ChessModeLoader getInstance() {
        if(instance == null) { // if it doesnt exists
            instance = new ChessModeLoader();
        }

        return instance;
    }

    /**
     * Returns the list of ChessModes inside the file.
     * @param context is used to obtain the streams.
     */
    @Override
    public List<ChessMode> loadObjects(Context context) {

        if(list == null) { // if it doesnt exists
            list = new ArrayList<>();

            try (FileInputStream fis = context.openFileInput("data.dat");
                 ObjectInputStream inputStream = new ObjectInputStream(fis)) {

                while (true) { // this will throw a EOFException and we will understand that we reach the end of file.
                    list.add((ChessMode) inputStream.readObject());
                }

            } catch (EOFException e) {
                Log.v(LOG_TAG, "All modes have been loaded\n" + e);
            } catch (FileNotFoundException e) { // if the file does not exists

                Log.e(LOG_TAG, "File not found\n" + e); // debug message
                list.add(new ChessMode("Blitz",5, 0 ,0)); // create a new Mode, so it wont be blank.
                saveObjects(context); // Update the file.

            } catch (ClassNotFoundException e) {
                Log.e(LOG_TAG, "Fatal error, class not found\n" + e);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Something went wrong, on Loading\n" + e);
            }

        }

        return list;
    }

    /**
     * Save the contents of the list, in the file. In other words, it's updates the file with the list contents. Does NOT append.
     * @param context is used to obtain the streams.
     */
    @Override
    public void saveObjects(Context context) {
        try(FileOutputStream fos = context.openFileOutput("data.dat", Context.MODE_PRIVATE);
            ObjectOutputStream outputStream = new ObjectOutputStream(fos)) {

            for(ChessMode mode : list) { // for each mode
                outputStream.writeObject(mode); // write it on the file
            }
        }
        catch(IOException e) { // oups
            Log.e(LOG_TAG, "Something went wrong, on Loading\n" + e);
        }
    }
}
