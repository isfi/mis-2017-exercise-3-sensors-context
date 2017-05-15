package com.example.pavus.assignment03;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class fft_view extends View {

        private final int paintColor = Color.BLACK;
        private Paint drawWhite;
        private Paint drawRec;

        //public int progress = 0;
        public int samplesize = 50;
        float prevx, currx;
        float prevyW, curryW;
         double max;



        float RecWidth, RecHeight, RecWidthInit, RecHeightInit;

        List<String> Acc_Mag = new ArrayList<String>();
        double[] FFT = new double[512];

        public fft_view(Context context){
            super(context);
        }

        public fft_view(Context context, AttributeSet attrs) {
            super(context, attrs);
            setFocusable(true);
            setFocusableInTouchMode(true);
            setupPaint();
        }

        public fft_view(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        // from https://guides.codepath.com/android/Basic-Painting-with-Views
        private void setupPaint() {


            Acc_Mag.add("0");
            Arrays.fill(FFT,0);

            drawWhite = new Paint();
            drawWhite.setColor(Color.WHITE);
            drawWhite.setAntiAlias(true);
            drawWhite.setStrokeWidth(3);
            drawWhite.setStyle(Paint.Style.STROKE);
            drawWhite.setStrokeJoin(Paint.Join.ROUND);
            drawWhite.setStrokeCap(Paint.Cap.ROUND);

            drawRec = new Paint();
            drawRec.setColor(Color.GRAY);
            drawRec.setAntiAlias(true);
            drawRec.setStrokeWidth(5);
            drawRec.setStrokeJoin(Paint.Join.ROUND);
            drawRec.setStrokeCap(Paint.Cap.ROUND);

            RecWidthInit = 400;
            RecHeightInit = 300;
            RecWidth = 400;
            RecHeight = 300;


        }

        @Override
        protected void onDraw(Canvas canvas) {



            canvas.drawRect(0, 0, RecWidth, RecHeight, drawRec);

            max=FFT[0];

            for (int i = 0; i < FFT.length; i++) {
                if (Math.abs(FFT[i]) > max) {
                    max = FFT[i];
                }
            }


            prevx = 0;
            prevyW = (float)(RecHeight/(2*max)*FFT[0])+(RecHeight/2);

            for (int i =0; i < FFT.length; i++) {
                currx= (RecWidth / FFT.length)*i;

                curryW= (float)(RecHeight/(2*max)*FFT[i])+(RecHeight/2);
                canvas.drawLine(prevx, prevyW, currx, curryW, drawWhite);

                prevx = currx;
                prevyW = curryW;
            }


            invalidate();
        }
    }

