package ru.kruslan.tuner;

import static android.content.ContentValues.TAG;

import static java.lang.Math.sqrt;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class GetData extends AsyncTask<Void, Void, Void> {

    String res;
    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);

    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (MainActivity.audioRecord == null)
            return null;

        short[] myBuffer = new short[MainActivity.myBufferSize];
        int readCount = 0;
        int totalCount = 0;
        while (MainActivity.isReading) {
            readCount = MainActivity.audioRecord.read(myBuffer, 0,MainActivity. myBufferSize);
            /*Log.d(TAG, "readCount = " + readCount + ", totalCount = "
                    + totalCount);*/
            MainActivity.changeArray(myBuffer);


    }
    return null;
    }


}
