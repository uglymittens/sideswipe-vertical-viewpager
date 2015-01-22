package com.example.it.vibin.example.drag;

import java.util.HashMap;
import java.util.Iterator;

import com.example.it.vibin.example.drag.VerticalViewPager.OnReadyListener;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Toast;

public class SwipeMenuManager implements OnTouchListener, OnDragListener, OnReadyListener {

	public static final String TAG = "SwipeMenuManager";
	
	private static final int VERTICAL_TRAVEL_LIMIT = 7;
	private static final int HORIZONTAL_TRAVEL_MAX = 60;
	private static final int HORIZONTAL_TRAVEL_MIN = 10;
	
	private Context mContext = null;
	private SwipeMenuEvents mEventListener = null;
	private SwipeMenuGestureDetector mDetector = null;
	
	private VerticalViewPager mViewPager = null;
	private View mCurrentView = null;
	private HashMap<DropZone,View> mDropZones = new HashMap<DropZone,View>();
	
	private int mX = 0;
	private int mY = 0;
	private boolean mIsDragging = false;
	private boolean mLockedOnX = false;
	
	public static enum DropZone {
		Idle, Left, Right
	}
	
	public interface SwipeMenuEvents {
		public void postback_touchEvent(MotionEvent event);
		public void postback_drop(View currentView, DropZone dropZone, View dropZoneView); 
	}
	
	private class SwipeMenuGestureDetector extends GestureDetectorCompat {

		public SwipeMenuGestureDetector(Context context, OnGestureListener listener) {
			super(context, listener);
			Log.d(TAG, "<<<<<<<<<< CONSTRUCT");
		}
	}
	
	private OnGestureListener mGestureListener = new OnGestureListener() {
		
		@Override
		public boolean onDown(MotionEvent e) {
			Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<< ON DOWN");
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			//Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<< ON SCROLL");
			if (e1 == null || e2 == null) return false;
			float x1 = e1.getX();
			float x2 = e2.getX();
			Log.d(TAG, "x1: " + x1 + ", x2: " + x2 + ", distanceX: " + distanceX + ", distanceY: " + distanceY);
			distanceY = Math.abs(distanceY);
			if (distanceY > VERTICAL_TRAVEL_LIMIT) {
				// not a clean horizontal gesture
				if (mViewPager != null) {
					mViewPager.onTouchEvent(e2);
					mLockedOnX = false;
				}
				return false;
			}
			if (Math.abs(distanceX) > HORIZONTAL_TRAVEL_MAX) {
				// it's a bit like a fling, don't drag
				mLockedOnX = false;
				return false;
			}
			if (distanceX > HORIZONTAL_TRAVEL_MIN || distanceX < -HORIZONTAL_TRAVEL_MIN) {
				if (mCurrentView != null && !mIsDragging) {
					mLockedOnX = true;
					mIsDragging = true;
					ClipData data = ClipData.newPlainText("", "");
					//SwipeMenuDragShadowBuilder shadowBuilder = new SwipeMenuDragShadowBuilder(mCurrentView);
					//shadowBuilder.setCoordinates(x2, mY);
			    	DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(mCurrentView);
			    	mCurrentView.startDrag(data, shadowBuilder, mCurrentView, 0);
			    	mCurrentView.setVisibility(View.INVISIBLE);
			    	//mViewPager.requestDisallowInterceptTouchEvent(true);
				}
				return true;
			}
			if (mViewPager != null) {
				mLockedOnX = false;
				mViewPager.onTouchEvent(e2);
			}
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			return false;
		}
	};
	
	public SwipeMenuManager(Context context, SwipeMenuEvents eventListener) {
		mContext = context;
		mEventListener = eventListener;
		mDetector = new SwipeMenuGestureDetector(context, mGestureListener);
	}
	
	@Override
	public void onViewPagerReady(VerticalViewPager viewPager) {
		Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@");
		Log.d(TAG, "@@@@@@@@ READY! @@@@@");
		Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@");
		mViewPager.setCurrentItem(0);
		View v = mViewPager.getChildAt(0);
		mY = (int) v.getY();
		setCurrentCard(mViewPager.getChildAt(0), View.VISIBLE);
	}
	
	/**
	 * Must be a VerticalViewPager.
	 * NOTE: VerticalViewPager should have this snippet of code within
	 * 
	 * <pre>
	 * public OnReadyListener mListener;
	 * public boolean mInit = false;
	 * public interface OnReadyListener {
	 * 	public void onViewPagerReady(VerticalViewPager viewPager);
	 * }
	 * public void setOnReadyListener(OnReadyListener listener) {
	 * 	mListener = listener;
	 * }
	 * @Override
	 * public void onWindowFocusChanged(boolean hasFocus) {
	 * 	super.onWindowFocusChanged(hasFocus);
	 * 	if (!hasFocus) return;
	 * 	if (!mInit && mListener != null) {
	 * 		mInit = true;
	 * 		mListener.onViewPagerReady(this);
	 * 	}
	 * }
	 * </pre>
	 * 
	 * @param viewPager
	 */
	public void setViewPager(VerticalViewPager viewPager) {
		mViewPager = viewPager;
		mViewPager.setOnReadyListener(this);
	}
	
	/**
	 * DropZone.Idle MUST be set (should be the lowest Z-axis view, which is as large as the screen).
	 * 
	 * @param dropZone	{@link DropZone}
	 * @param view		View
	 */
	public void setDropZone(DropZone dropZone, View view) {
		view.setOnDragListener(this);
		mDropZones.put(dropZone, view);
	}
	
	/**
	 * SwipeMenuManager needs to know which card is in the foreground position of VerticalViewPager, so
	 * this method needs to be updated with new card everything the viewpager is updated.
	 * 
	 * @param view			View
	 * @param visibility	int
	 */
	public void setCurrentCard(View view, int visibility) {
		Log.d(TAG, "==========> new foreground card!");
		view.setVisibility(visibility);
		view.setOnTouchListener(this);
		mCurrentView = view;	
	}
	
	/**
	 * Call this function when the view should go back to original position in VerticalViewPager
	 */
	public void recall() {
		mCurrentView.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		int action = event.getAction();
		final View dropView = (View) event.getLocalState();
		ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
		switch (action) {
		case DragEvent.ACTION_DRAG_STARTED:
			mY = (int) dropView.getY() - params.topMargin;
			break;
		case DragEvent.ACTION_DRAG_LOCATION:
			break;
		case DragEvent.ACTION_DROP:
		case DragEvent.ACTION_DRAG_EXITED:
			Iterator<DropZone> iter = mDropZones.keySet().iterator();
			while (iter.hasNext()) {
				DropZone dropZone = iter.next();
				View dropZoneView = mDropZones.get(dropZone);
				if (v.getId() == dropZoneView.getId()) {
					mEventListener.postback_drop(dropView, dropZone, v);
				}
				//mViewPager.requestDisallowInterceptTouchEvent(false);
			}
			mLockedOnX = false;
			mIsDragging = false;
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			Log.d(TAG, "=============> DRAG ENDED!");
			dropView.post(new Runnable() {
				@Override
				public void run() {
					//dropView.setVisibility(View.VISIBLE);
					mLockedOnX = false;
					mIsDragging = false;
					//mViewPager.requestDisallowInterceptTouchEvent(false);
				}
		    });
			break;
		}
		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		//Log.d(TAG, "===========> TOUCH!");
		Log.d(TAG, "locked on X? " + mLockedOnX);
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Log.d(TAG, "==========> UP!");
			mLockedOnX = false;
			mIsDragging = false;
			recall();
//			if (mCurrentView != null) {
//				mCurrentView.setVisibility(View.VISIBLE);
//			}
		}
		return mDetector.onTouchEvent(event);
	}
}
