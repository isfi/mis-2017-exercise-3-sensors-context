package com.example.pavus.assignment03;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class AccView extends View {
        private Paint drawWhite, drawRed, drawBlue, drawGreen;
        private Paint drawRec;

        public int sampleSize = 50;

        public int
                recWidth,
                recHeight;

        List<Double> Acc_X = new ArrayList<Double>();
        List<Double> Acc_Y = new ArrayList<Double>();
        List<Double> Acc_Z = new ArrayList<Double>();
        List<Double> Acc_Mag = new ArrayList<Double>();

        public AccView(Context context){
            super(context);
        }

        public AccView(Context context, AttributeSet attrs) {
            super(context, attrs);
            setFocusable(true);
            setFocusableInTouchMode(true);
            setupPaint();
        }

        public AccView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        // from https://guides.codepath.com/android/Basic-Painting-with-Views
        private void setupPaint() {
            Acc_X.add(0.0);
            Acc_Y.add(0.0);
            Acc_Z.add(0.0);
            Acc_Mag.add(0.0);

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

            recWidth = 500;
            recHeight = 300;

        }

        @Override
        protected void onDraw(Canvas canvas) {

            canvas.drawRect(0, 0, recWidth, recHeight, drawRec);

            while (Acc_X.size() > sampleSize) {
                Acc_X.remove(0);
            }

            while (Acc_Y.size() > sampleSize) {
                Acc_Y.remove(0);
            }

            while (Acc_Z.size() > sampleSize) {
                Acc_Z.remove(0);
            }

            while (Acc_Mag.size() > sampleSize) {
                Acc_Mag.remove(0);
            }

            float prevX = 0,
                prevYR = Acc_X.get(0).floatValue(), //RED = X
                prevYG = Acc_Y.get(0).floatValue(), //GREEN = Y
                prevYB = Acc_Z.get(0).floatValue(), //BLUE = Z
                prevYW = Acc_Mag.get(0).floatValue(), //WHITE = Mag
                halfHeight = (float)recHeight / 2,
                stepSize = (float)recWidth / sampleSize,
                currX = 0,
                currYR = 0,
                currYG = 0,
                currYB = 0,
                currYW = 0;

                for (int i =0; i <(Acc_X.size()); i++) {
                    currX = stepSize * i;

                    currYR = halfHeight - Acc_X.get(i).floatValue();
                    currYG = halfHeight - Acc_Y.get(i).floatValue();
                    currYB = halfHeight - Acc_Z.get(i).floatValue();
                    currYW = halfHeight - Acc_Mag.get(i).floatValue();

                    canvas.drawLine(prevX, prevYR, currX, currYR, drawRed);
                    canvas.drawLine(prevX, prevYG, currX, currYG, drawGreen);
                    canvas.drawLine(prevX, prevYB, currX, currYB, drawBlue);
                    canvas.drawLine(prevX, prevYW, currX, currYW, drawWhite);

                    prevX = currX;
                    prevYR = currYR;
                    prevYG = currYG;
                    prevYB = currYB;
                    prevYW = currYW;
                }


            invalidate();
        }
    }

