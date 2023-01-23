package ru.kruslan.tuner;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Fft {
    static void fft(double [] a, double [] b){
        int n=b.length;
        if(n==1) return;

        double [] a0 =new double[n/2];
        double [] b0 =new double[n/2];
        double [] a1 =new double[n/2];
        double [] b1 =new double[n/2];

        for(int i=0,j=0;i<n;i+=2,j++){
            a0[j]=a[i];
            b0[j]=b[i];
            a1[j]=a[i+1];
            b1[j]=b[i+1];
        }

        fft(a0,b0);
        fft(a1,b1);

        double ang = -2*PI/n  ;
        double wa=1;
        double wb=0;
        double wna=cos(ang);
        double wnb=sin(ang);

        for(int i=0; i<n/2; ++i  ){
            a[i]=a0[i]+wa*a1[i]-wb*b1[i];
            b[i]=b0[i]+wa*b1[i]+wb*a1[i];
            a[i+n/2]= a0[i]-wa*a1[i]+wb*b1[i];
            b[i+n/2]=b0[i]-wa*b1[i]-wb*a1[i];
            wa=wa*wna-wb*wnb;
            wb=wa*wnb+wb*wna;
        }
    }
}
