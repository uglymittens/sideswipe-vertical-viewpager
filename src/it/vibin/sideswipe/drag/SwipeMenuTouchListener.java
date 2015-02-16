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
