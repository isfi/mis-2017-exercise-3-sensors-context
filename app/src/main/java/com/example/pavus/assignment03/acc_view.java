package com.example.pavus.assignment03;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class acc_view extends View {
        private final int paintColor = Color.BLACK;
        private Paint drawWhite, drawRed, drawBlue, drawGreen;
        private Paint drawRec;

        //public int progress = 0;
        public int samplesize = 50;
        float prevx, currx;
        float prevyW, curryW, prevyR, curryR, prevyB,  curryB, prevyG,  curryG;

        int RecWidth, RecHeight;
        List<String> Acc_X = new ArrayList<String>();
        List<String> Acc_Y = new ArrayList<String>();
        List<String> Acc_Z = new ArrayList<String>();
        List<String> Acc_Mag = new ArrayList<String>();

        public acc_view(Context context){
            super(context);
        }

        public acc_view(Context context, AttributeSet attrs) {
            super(context, attrs);
            setFocusable(true);
            setFocusableInTouchMode(true);
            setupPaint();
        }

        public acc_view(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        // from https://guides.codepath.com/android/Basic-Painting-with-Views
        private void setupPaint() {
            Acc_X.add("0");
            Acc_Y.add("0");
            Acc_Z.add("0");
            Acc_Mag.add("0");

            drawWhite = new Paint();
            drawWhite.setColor(Color.WHITE);
            drawWhite.setAntiAlias(true);
            drawWhite.setStrokeWidth(3);
            drawWhite.setStyle(Paint.Style.STROKE);
            drawWhite.setStrokeJoin(Paint.Join.ROUND);
            drawWhite.setStrokeCap(Paint.Cap.ROUND);

            drawRed = new Paint();
            drawRed.setColor(Color.RED);
            drawRed.setAntiAlias(true);
            drawRed.setStrokeWidth(3);
            drawRed.setStyle(Paint.Style.STROKE);
            drawRed.setStrokeJoin(Paint.Join.ROUND);
            drawRed.setStrokeCap(Paint.Cap.ROUND);

            drawBlue = new Paint();
            drawBlue.setColor(Color.BLUE);
            drawBlue.setAntiAlias(true);
            drawBlue.setStrokeWidth(3);
            drawBlue.setStyle(Paint.Style.STROKE);
            drawBlue.setStrokeJoin(Paint.Join.ROUND);
            drawBlue.setStrokeCap(Paint.Cap.ROUND);

            drawGreen = new Paint();
            drawGreen.setColor(Color.GREEN);
            drawGreen.setAntiAlias(true);
            drawGreen.setStrokeWidth(3);
            drawGreen.setStyle(Paint.Style.STROKE);
            drawGreen.setStrokeJoin(Paint.Join.ROUND);
            drawGreen.setStrokeCap(Paint.Cap.ROUND);

            drawRec = new Paint();
            drawRec.setColor(Color.GRAY);
            drawRec.setAntiAlias(true);
            drawRec.setStrokeWidth(5);
            drawRec.setStrokeJoin(Paint.Join.ROUND);
            drawRec.setStrokeCap(Paint.Cap.ROUND);

            RecWidth = 500;
            RecHeight = 300;

        }

        @Override
        protected void onDraw(Canvas canvas) {

            canvas.drawRect(0, 0, RecWidth, RecHeight, drawRec);

            if (Acc_X.size() >= samplesize) {
                Acc_X.remove(0);
            }

            if (Acc_Y.size() >= samplesize) {
                Acc_Y.remove(0);
            }

            if (Acc_Z.size() >= samplesize) {
                Acc_Z.remove(0);
            }

            if (Acc_Mag.size() >= samplesize) {
                Acc_Mag.remove(0);
            }

                prevx = 0;
                String temp = Acc_X.get(0);
                prevyR = Float.valueOf(temp); //RED = X
                 temp = Acc_Y.get(0);
                prevyG = Float.valueOf(temp);//GREEN = Y
                temp = Acc_Z.get(0);
                prevyB = Float.valueOf(temp);// BLUE = Z
                temp = Acc_Mag.get(0);
                prevyW = Float.valueOf(temp);// WHITE = MAG


                for (int i =0; i <(Acc_X.size()); i++) {
                    currx= (RecWidth / samplesize)*i;

                    String temp2 = Acc_X.get(i);
                    curryR= (RecHeight/2) - Float.valueOf(temp2);
                     temp2 = Acc_Y.get(i);
                    curryG= (RecHeight/2) - Float.valueOf(temp2);
                     temp2 = Acc_Z.get(i);
                    curryB= (RecHeight/2) - Float.valueOf(temp2);
                     temp2 = Acc_Mag.get(i);
                    curryW= (RecHeight/2) - Float.valueOf(temp2);

                    canvas.drawLine(prevx, prevyR, currx, curryR, drawRed);
                    canvas.drawLine(prevx, prevyG, currx, curryG, drawGreen);
                    canvas.drawLine(prevx, prevyB, currx, curryB, drawBlue);
                    canvas.drawLine(prevx, prevyW, currx, curryW, drawWhite);

                    prevx = currx;
                    prevyR = curryR;
                    prevyG = curryG;
                    prevyB = curryB;
                    prevyW = curryW;
                }


            invalidate();
        }
    }

