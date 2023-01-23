package ru.kruslan.tuner;

import static android.content.ContentValues.TAG;
import static java.lang.Math.log;
import static java.lang.Math.max;
import static java.lang.Math.sqrt;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class Calculate extends AsyncTask<Void,String,String> {

    double[] a = new double[MainActivity.NUMPTS];
    double[] b = new double[MainActivity.NUMPTS];
    double [] freq=new double[MainActivity.NUMPTS/2];
    double max=0;
    int ind=0;
    @Override
    protected String doInBackground(Void... voids) {
        while(MainActivity.isCalculate){
        Log.d(TAG, "1" );
        b=MainActivity.changeArray(null);
        for (int i=0;i<MainActivity.NUMPTS;i++){

            a[i]=i;
        }

        Fft.fft(a,b);
        max=0;
        ind=0;
        for (int i = 1600; i < 16000;i++) {
            freq[i]= sqrt(a[i]*a[i]+b[i]*b[i])/(double)(MainActivity.NUMPTS)*2.0;
            if (freq[i]>max) {
                max=freq[i];
                ind=i;
            }
        }
        String res=((Double)(ind*(double)MainActivity.sampleRate/MainActivity.NUMPTS)).toString();
        publishProgress(res);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return "end";
    }
    double noteFreq;
    double log;
    String roundStr;
    int round;
    String notes[]={"A","B","H","C","C#","D","D#","E","F","F#","G","G#"};
    String progress;

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        noteFreq=Double.parseDouble(values[0]);
        if (noteFreq>=220 && noteFreq<=880)  {MainActivity.tv.setText(values[0]);
            log=12*log(noteFreq/220)/log(2);
            roundStr=String.format("%.0f",log);
            round=Integer.parseInt(roundStr)%12;
            MainActivity.note.setText(notes[round]);
            progress=String.format("%.0f",(Integer.parseInt(roundStr)-log)*(-100)+50);
            MainActivity.pb.setProgress(Integer.parseInt(progress));
        }

        else {MainActivity.tv.setText("0");MainActivity.note.setText("N");MainActivity.pb.setProgress(0); }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }
}

