package com.example.toucheventlib;

import java.util.ArrayList;
import java.util.List;

public class ViewGroup extends View {

  //存放子控件
  List<View> childList = new ArrayList<>();
  private View[] mChildren = new View[0];
  private TouchTarget mFirstTouchTarget;

  public ViewGroup(int left, int top, int right, int bottom) {
    super(left, top, right, bottom);
  }

  //添加子控件
  public void addView(View view) {
    if (view == null) {
      return;
    }
    childList.add(view);
    mChildren = childList.toArray(new View[childList.size()]);
  }

  //事件分发入口
  public boolean dispatchTouchEvent(MotionEvent event) {
    System.out.println(name + "ViewGroup dispatchTouchEvent");
    int actionMasked = event.getActionMasked();
    //判断当前控件是否需要拦截事件
    boolean intercept = onInterceptTouchEvent(event);
    boolean handle = false;
    TouchTarget newTouchTarget;
    if (actionMasked != MotionEvent.ACTION_CANCEL && !intercept) {
      //down
      if (actionMasked == MotionEvent.ACTION_DOWN) {
        final View[] children = mChildren;//确定z轴
        //遍历 倒序 （概率，最有可能获取事件消费）
        for (int i = mChildren.length - 1; i >= 0; i--) {
          //View 不能接收事件
          View child = mChildren[i];
          if (!child.isContain(event.getX(), event.getY())) {
            continue;
          }
          //能够接受事件 分发给子控件
          if (dispatchTransformedTouchEvent(event, child)) {
            handle = true;
            newTouchTarget = addTouchTarget(child);
            break;
          }
        }
      }
    }
    if (mFirstTouchTarget == null) {
      handle = dispatchTransformedTouchEvent(event, null);
    }
    return handle;
  }

  //添加消费事件的View
  private TouchTarget addTouchTarget(View child) {
    TouchTarget target = TouchTarget.obtain(child);
    target.next = mFirstTouchTarget;
    mFirstTouchTarget = target;
    return target;
  }

  private boolean dispatchTransformedTouchEvent(MotionEvent event, View child) {
    boolean handled = false;
    //child 消费了
    if (child != null) {
      handled = child.dispatchTouchEvent(event);
    } else {
      handled = super.dispatchTouchEvent(event);
    }
    return handled;
  }

  private boolean onInterceptTouchEvent(MotionEvent event) {
    return false;
  }

  public static final class TouchTarget {

    private View child;//当前缓存View
    //回收池
    private static TouchTarget sRecyclerBin;
    //size
    private static int sRecycleCount;
    private TouchTarget next;
    private static final Object sRecycleLock = new Object();

    public static TouchTarget obtain(View child) {
      TouchTarget target;
      synchronized (sRecycleLock) {
        if (sRecyclerBin == null) {
          target = new TouchTarget();
        } else {
          target = sRecyclerBin;
        }
        sRecyclerBin = target.next;
        sRecycleCount--;
        target.next = null;
      }
      target.child = child;
      return target;
    }

    public void recycler() {
      if (child == null) {
        throw new IllegalStateException("已经被回收了");
      }
      synchronized (sRecycleLock) {
        if (sRecycleCount < 32) {
          next = sRecyclerBin;
          sRecyclerBin = this;
          sRecycleCount++;
        }
      }
    }

  }
}
