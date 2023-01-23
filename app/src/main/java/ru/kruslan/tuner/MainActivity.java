package ru.kruslan.tuner;

import static android.content.ContentValues.TAG;
import static java.lang.Math.PI;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;


import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
    static TextView tv;
    static TextView note;
    static TextView tv2;
    static ProgressBar pb;
    static int myBufferSize = 10000;
    static AudioRecord audioRecord;
    static boolean isReading = false;
    static double p1;
    static int NUMPTS = 65536;
    static int sampleRate = 8000;
    static double[] b = new double[NUMPTS];
    static double[] a = new double[NUMPTS];

    static double p0;

    static boolean isCalculate=true;
    static String res;

    static synchronized double[] changeArray(short [] srs){
        if (srs!=null){
            for (int i=0; i<srs.length;i++){
                b[i]=srs[i];
            }
           // Log.d(TAG, "record");
            return null;
        }
        else{
            double[] res=new double[NUMPTS];
            for (int i=0; i<NUMPTS;i++){
                res[i]=b[i];
            }
           // Log.d(TAG, "reading" );
            return res;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb=(ProgressBar)findViewById(R.id.progressBar);
        note=(TextView) findViewById(R.id.note);
        tv = (TextView) findViewById(R.id.hz);
        for (int i=0;i<NUMPTS;i++){
            a[i]=i;
            b[i]=0;
        }
        Button button1 = (Button) findViewById(R.id.start_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "record start");
                audioRecord.startRecording();
                int recordingState = audioRecord.getRecordingState();
                Log.d(TAG, "recordingState = " + recordingState);
                Log.d(TAG, "read start");
                isReading = true;
                if (audioRecord == null)
                    return;
                isCalculate=true;
                new GetData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new Calculate().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        createAudioRecorder();



    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        isReading = false;
        if (audioRecord != null) {
            audioRecord.release();
        }
    }


    void createAudioRecorder() {
        //int sampleRate = 8000;
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

        int minInternalBufferSize = AudioRecord.getMinBufferSize(sampleRate,
                channelConfig, audioFormat);
        int internalBufferSize = 5000;
        Log.d(TAG, "minInternalBufferSize = " + minInternalBufferSize
                + ", internalBufferSize = " + internalBufferSize
                + ", myBufferSize = " + myBufferSize);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                sampleRate, channelConfig, audioFormat, internalBufferSize);
    }






        /*
        for(int i = 0; i < NUMPTS; i++) {
            a[i]=i;
            b[i]=4000*sin(440*PI*i*1/sampleRate);
        }
        p0=System.currentTimeMillis();
        Fft.fft(a,b);
        p1=System.currentTimeMillis()-p0;

        double max=0;
        int ind=0;
        double [] freq=new double[NUMPTS/2];
        for (int i = 1600; i < 16000;i++) {
            freq[i]= sqrt(a[i]*a[i]+b[i]*b[i])/(double)NUMPTS*2.0;
            if (freq[i]>max) {
                max=freq[i];
                ind=i;
            }

        }
        String res=((Double)(ind*(double)sampleRate/NUMPTS*2.0)).toString();
        tv.setText(res);
        tv2.setText(((Double)(p1)).toString());*/

/*
    public class Calculate extends AsyncTask<Void,Void,String> {


        @Override
        protected String doInBackground(Void... voids) {
            while (MainActivity.isCalculate) {
                Log.d(TAG, "1");
                Fft.fft(MainActivity.a, MainActivity.b);
                MainActivity.max = 0;
                MainActivity.ind = 0;
                for (int i = 1600; i < 16000; i++) {
                    MainActivity.freq[i] = sqrt(MainActivity.a[i] * MainActivity.a[i] + MainActivity.b[i] * MainActivity.b[i]) / (double) MainActivity.NUMPTS * 2.0;
                    if (MainActivity.freq[i] > MainActivity.max) {
                        MainActivity.max = MainActivity.freq[i];
                        MainActivity.ind = i;
                    }

                }
                MainActivity.res = ((Double) (MainActivity.ind * (double) MainActivity.sampleRate / MainActivity.NUMPTS * 2.0)).toString();
                Log.d(TAG, "2");
                MainActivity.this.sendBroadcast(new Intent("abcd"));
            }
            return "end";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //MainActivity.tv.setText(s);
        }
    }*/
}