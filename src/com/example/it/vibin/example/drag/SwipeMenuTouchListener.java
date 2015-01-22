package com.example.it.vibin.example.drag;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SwipeMenuTouchListener implements OnTouchListener {

	public static final String TAG = "SwipeMenuTouchListener";
	
	private VerticalViewPager mViewPager = null;
	private GestureDetector mDetector = null;
	private SwipeMenuDropZones mDropZones = null;
	private SwipeMenuDragShadow mShadow = null;
	
	public SwipeMenuTouchListener(VerticalViewPager viewPager, GestureDetector detector, SwipeMenuDropZones dropZones) {
		mViewPager = viewPager;
		mDetector = detector;
		mDropZones = dropZones;
	}
	
	public void setDragShadow(SwipeMenuDragShadow dragShadow) {
		mShadow = dragShadow;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (mDetector.onTouchEvent(event)) {
			Log.d(TAG, "SWIPE DETECTED!");
			return true;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			Log.d(TAG, "MOVE!");
			return true;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mShadow.stopDrag();
			mDropZones.setExitDragShadow(mShadow);
		}
		return false;
	}

}
