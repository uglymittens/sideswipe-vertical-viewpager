package com.example.it.vibin.example.drag;

import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

public class SwipeMenuGestureListener extends SimpleOnGestureListener {

	private ScrollCallback mCallback = null;
	
	public interface ScrollCallback {
		public void postback_onHorizontalScroll(MotionEvent event, boolean consumed);
		public void postback_onClick();
	}
	
	public SwipeMenuGestureListener(ScrollCallback callback) {
		mCallback = callback;
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		mCallback.postback_onClick();
		return true;
	}
	
	@Override
	public void onShowPress(MotionEvent e) {}
	
	/**
	 * Detect the horizontal scrolling here!
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		if (Math.abs(distanceX) > Math.abs(distanceY) * 2) {
			mCallback.postback_onHorizontalScroll(e2, true);
			return true;
		}
		mCallback.postback_onHorizontalScroll(e2, false);
		return false;
	}
	
	@Override
	public void onLongPress(MotionEvent e) {}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}
	@Override
	public boolean onDown(MotionEvent e) { return false; }
}
