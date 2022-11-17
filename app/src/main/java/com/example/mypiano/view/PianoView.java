package com.example.mypiano.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.mypiano.R;
import com.example.mypiano.model.Key;
import com.example.mypiano.utils.SoundManager;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PianoView extends View {

    public static final int NUMBER_OF_KEYS = 14;
    private ArrayList<Key> whites;
    private ArrayList<Key> blacks;

    int current = 0;

    private int keyWidth, keyHeight;

    Paint blackPen, whitePen, blackPenDown, whitePenDown;
    private SoundManager soundManager;

    public PianoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        whites = new ArrayList<Key>();
        blacks = new ArrayList<Key>();

        blackPen = new Paint();
        blackPen.setColor(Color.BLACK);
        blackPen.setStyle(Paint.Style.FILL);

        blackPenDown = new Paint();
        blackPenDown.setColor(Color.rgb(43,43,43));
        blackPenDown.setStyle(Paint.Style.FILL);

        whitePen = new Paint();
        whitePen.setColor(Color.WHITE);
        whitePen.setStyle(Paint.Style.FILL);

        whitePenDown = new Paint();
        whitePenDown.setColor(Color.GRAY);
        whitePenDown.setStyle(Paint.Style.FILL);

        soundManager = SoundManager.getInstance();
        soundManager.init(context);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        keyWidth = w / NUMBER_OF_KEYS;
        keyHeight = h;

        int blackCount = 15;
        // create white note array
        for (int i = 0; i < NUMBER_OF_KEYS; i++) {
            int left = i * keyWidth;
            int right = left + keyWidth;

            RectF rect = new RectF(left, 0, right, h);
            whites.add(new Key(i + 1, rect, false));

            // create black note array
            if (i != 0 && i != 3 && i != 7 && i != 10) {
                rect = new RectF(
                        (float)(i - 1) * keyWidth + 0.75f * keyWidth,
                        0,
                        (float)i * keyWidth + 0.25f * keyWidth,
                        0.6f*keyHeight
                        );
                blacks.add(new Key(blackCount, rect, false));
                blackCount++;
            }
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(Key k: whites) {
            canvas.drawRect(k.rect, k.down ? whitePenDown : whitePen);
        }

        for (int i = 1; i < NUMBER_OF_KEYS; i++) {
            canvas.drawLine(i * keyWidth, 0, i*keyWidth, keyHeight, blackPen);
        }

        for(Key k: blacks) {
            canvas.drawRect(k.rect, k.down ? blackPenDown : blackPen);
        }
    }

    void playSound(int index) {
        switch (index) {
            case 1:
                soundManager.playSound(R.raw.c3);
                break;
            case 2:
                soundManager.playSound(R.raw.d3);
                break;
            case 3:
                soundManager.playSound(R.raw.e3);
                break;
            case 4:
                soundManager.playSound(R.raw.f3);
                break;
            case 5:
                soundManager.playSound(R.raw.g3);
                break;
            case 6:
                soundManager.playSound(R.raw.a3);
                break;
            case 7:
                soundManager.playSound(R.raw.b3);
                break;
            case 8:
                soundManager.playSound(R.raw.c4);
                break;
            case 9:
                soundManager.playSound(R.raw.d4);
                break;
            case 10:
                soundManager.playSound(R.raw.e4);
                break;
            case 11:
                soundManager.playSound(R.raw.f4);
                break;
            case 12:
                soundManager.playSound(R.raw.g4);
                break;
            case 13:
                soundManager.playSound(R.raw.a4);
                break;
            case 14:
                soundManager.playSound(R.raw.b4);
                break;
            case 15:
                soundManager.playSound(R.raw.db3);
                break;
            case 16:
                soundManager.playSound(R.raw.eb3);
                break;
            case 17:
                soundManager.playSound(R.raw.gb3);
                break;
            case 18:
                soundManager.playSound(R.raw.ab3);
                break;
            case 19:
                soundManager.playSound(R.raw.bb3);
                break;
            case 20:
                soundManager.playSound(R.raw.db4);
                break;
            case 21:
                soundManager.playSound(R.raw.eb4);
                break;
            case 22:
                soundManager.playSound(R.raw.gb4);
                break;
            case 23:
                soundManager.playSound(R.raw.ab4);
                break;
            case 24:
                soundManager.playSound(R.raw.bb4);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        boolean isDownAction = action == MotionEvent.ACTION_DOWN ||
                               action == MotionEvent.ACTION_MOVE;
        boolean isUpAction = action == MotionEvent.ACTION_UP;


        for(int touchIndex = 0; touchIndex < event.getPointerCount(); touchIndex++) {
            float x = event.getX(touchIndex);
            float y = event.getY(touchIndex);

            for(Key k:whites) {

                boolean flag = false;
                if(k.rect.contains(x, y)) {
                    for(Key h:blacks){
                        if(h.rect.contains(x, y)) {
                            h.down = isDownAction;
                            if(h.down && h.sound != current) {
                                current = h.sound;
                                playSound(h.sound);
                                flag = true;
                                break;
                            }
                        }
                    }

                    if (!flag) {
                        k.down = isDownAction;
                        if(k.down && k.sound != current) {
                            current = k.sound;
                            playSound(k.sound);
                        }
                    }

                }
            }
        }
        if (isUpAction) {
            current = 0;
        }

        invalidate();
        return true;

//        return super.onTouchEvent(event);
    }
}
