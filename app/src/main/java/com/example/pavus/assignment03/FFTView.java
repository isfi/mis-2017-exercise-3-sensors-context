package com.example.pavus.assignment03;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;

public class FFTView extends View {
        private Paint
                drawWhite,
                drawRec;

        public float
                recWidth,
                recHeight,
                recWidthInit,
                recHeightInit;

        double[] FFT = new double[128];

        public FFTView(Context context){
            super(context);
        }

        public FFTView(Context context, AttributeSet attrs) {
            super(context, attrs);
            setFocusable(true);
            setFocusableInTouchMode(true);
            setupPaint();
        }

        public FFTView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        // from https://guides.codepath.com/android/Basic-Painting-with-Views
        private void setupPaint() {
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

            recWidthInit = 400;
            recHeightInit = 300;
            recWidth = 400;
            recHeight = 300;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawRect(0, 0, recWidth, recHeight, drawRec);

            float halfHeight = recHeight / 2,
                    stepLength = recWidth / FFT.length,
                    prevX = 0,
                    prevY = (float)(FFT[0] + halfHeight),
                    currX = 0,
                    currY = 0;

            for(int i = 0; i < FFT.length; i++){
                currX = stepLength * i;
                currY = (float)(FFT[i] + halfHeight);

                canvas.drawLine(prevX, prevY, currX, currY, drawWhite);

                prevX = currX;
                prevY = currY;
            }

            invalidate();
        }
    }

