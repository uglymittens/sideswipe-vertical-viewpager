/*
 * Copyright 2015 www.vibin.it (vic.choy@gmail.com, victor.cui@vibin.it) 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.vibin.sideswipe.drag;

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
