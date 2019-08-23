package com.example.toucheventlib.listener;

import com.example.toucheventlib.MotionEvent;
import com.example.toucheventlib.View;

public interface OnTouchListener {

    boolean onTouch(View view, MotionEvent event);
}
