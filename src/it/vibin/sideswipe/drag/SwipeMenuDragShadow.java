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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

public class SwipeMenuDragShadow {
	
	public static final String TAG = "SwipeMenuDragShadow";
	
	private ViewGroup mLayout = null;
	private Bitmap mBitmap = null;
	private View mCurrentView = null;
	private ImageView mDragView = null;
	
	private float mX = 0;
	private float mTopY = 0;
	
	private float mMidX = 0;
	private float mMidY = 0;
	
	private float mInitialX = 0;
	private float mInitialY = 0;
	private float mDeltaX = 0;
	
	private float mLastX = 0;
	private float mLastY = 0;
	
	private boolean mDragging = false;
	public boolean mDragged = false;
	
	public SwipeMenuDragShadow(Context context, ViewGroup layout) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		mTopY = 0;
		mMidX = metrics.widthPixels / 2;
		mMidY = metrics.heightPixels / 2;
		mLayout = layout;
	}
	
	public boolean isDragging() {
		return mDragging;
	}
	
	public void startDrag(View view, MotionEvent event, float initialX, float initialY) {
		if (view == null) return;
		if (mDragging) return;
		mCurrentView = view;
		mCurrentView.setVisibility(View.INVISIBLE);
		mCurrentView.invalidate();
		mBitmap = getBitmapFromView(view);
		mDragView = new ImageView(view.getContext());
		mDragView.setBackgroundColor(view.getContext().getResources().getColor(android.R.color.transparent));
		mDragView.setImageBitmap(mBitmap);
		mDragView.setX(mInitialX);
		mDragView.setY(mInitialY);
		mDragView.setTag(TAG);
		// check if already has child with tag, and if so, remove it
		View v = mLayout.findViewWithTag(TAG);
		if (v != null) {
			mLayout.removeView(v);
			Log.w(TAG, "@@@@ found artifact drag view and removed it @@@@");
		}
		mLayout.addView(mDragView, 0);
		mLayout.bringChildToFront(mDragView);
		//mLayout.invalidate();
		mDeltaX = event.getX() - initialX;
		mTopY = initialY;
		mInitialX = initialX;
		mInitialY = initialY;
		mDragged = true;
		mDragging = true;
	}
	
	public void stopDrag() {
		if (mCurrentView != null) {
			mCurrentView.setVisibility(View.VISIBLE);
			mCurrentView.invalidate();
		} else {
			Log.e(TAG, "mCurrentView == NULL!");
		}
		if (mDragView != null) {
			mDragView.setX(mInitialX);
			mDragView.setY(mInitialY);
			mDragView.setVisibility(View.GONE);
		} else {
			Log.e(TAG, "mDragView == NULL!");
		}
		if (mLayout != null) {
			mLayout.removeView(mDragView);
			View v = mLayout.findViewWithTag(TAG);
			if (v != null) {
				Log.w(TAG, "removed artifact drag view");
				mLayout.removeView(v);
			}
		}
		if (mBitmap != null && !mBitmap.isRecycled()) {
			mBitmap.recycle();
			mBitmap = null;
		}
		mDragging = false;
	}
	
	public void reset() {
		mLastX = mMidX;
		mLastY = mMidY;
	}
	
	public void setMotionEvent(Context context, MotionEvent event, float initialX) {
		mX = event.getX() - mDeltaX;
		if (mDragView != null && mDragView.isShown()) {
			mDragView.setY(mTopY);
			mDragView.setX(mX);
			mLastX = event.getX();
			mLastY = event.getY();
		}
	}
	
	public float getLastX() {
		return mLastX;
	}
	
	public float getLastY() {
		return mLastY;
	}
	
	public static Bitmap getBitmapFromView(View view) {
        Bitmap out = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(out);
        Drawable drawable = view.getBackground();
        if (drawable != null) drawable.draw(canvas);
        else canvas.drawColor(Color.TRANSPARENT);
        view.draw(canvas);
        return out;
    }
}
