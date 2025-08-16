package com.example.lostintime;

import android.view.MotionEvent;
import android.view.View;

public abstract class HoldTouchListener implements View.OnTouchListener {
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                onTouchUp();
                return true;
        }
        return false;
    }
    
    public abstract void onTouchDown();
    public abstract void onTouchUp();
}