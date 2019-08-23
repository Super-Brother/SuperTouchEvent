package com.example.toucheventlib;

import com.example.toucheventlib.listener.OnClickListener;
import com.example.toucheventlib.listener.OnTouchListener;

public class Activity {

  public static void main(String[] args) {
    ViewGroup viewGroup = new ViewGroup(0, 0, 1080, 1920);
    viewGroup.setName("顶层容器");
    ViewGroup viewGroup2 = new ViewGroup(0, 0, 600, 600);
    viewGroup2.setName("第二级容器");
    View view = new View(0, 0, 300, 300);
    view.setName("子控件");
    View view2 = new View(0, 0, 300, 300);
    view2.setName("子控件2号");

    viewGroup2.addView(view);
    viewGroup2.addView(view2);
    viewGroup.addView(viewGroup2);
    viewGroup.setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent event) {
        System.out.println("顶层容器的OnTouch事件");
        return false;
      }
    });
    viewGroup2.setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent event) {
        System.out.println("第二层容器的OnTouch事件");
        return false;
      }
    });
    view.setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent event) {
        System.out.println("view的OnTouch事件");
        return true;
      }
    });
    view.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        System.out.println("view的OnClick事件");
      }
    });
    view2.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        System.out.println("view2的OnClick事件");
      }
    });

    MotionEvent motionEvent = new MotionEvent(100, 100);
    motionEvent.setActionMasked(MotionEvent.ACTION_DOWN);
    //顶层容器传递事件
    viewGroup.dispatchTouchEvent(motionEvent);
  }
}
