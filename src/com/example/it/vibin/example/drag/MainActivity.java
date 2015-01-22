package com.example.it.vibin.example.drag;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.it.vibin.example.drag.SwipeMenuDropZones.DropZone;
import com.example.it.vibin.example.drag.SwipeMenuDropZones.SwipeMenuDropEvents;
import com.example.it.vibin.example.drag.SwipeMenuGestureListener.ScrollCallback;
import com.example.it.vibin.example.drag.VerticalViewPager.OnPageChangeListener;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements SwipeMenuDropEvents, OnPageChangeListener, ScrollCallback {
	
	public static String TAG = "TAG";
	
	private GestureDetector mDetector = null;
	private SwipeMenuGestureListener mSwipeGestureListener = null;
	private SwipeMenuDragShadow mShadow = null;
	private SwipeMenuDropZones mDropZones = null;
	private SwipeMenuTouchListener mTouchListener = null;
	
	private VerticalViewPager mPager = null;
	private PagerAdapter mAdapter = null;
	
	private View mCurrentView = null;
	
//	private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
//		@Override
//		public boolean onTouch(View v, MotionEvent event) {
//			if (mCurrentView == null) {
//				mCurrentView = mPager.findViewWithTag(mPager.getCurrentItem());
//			}
//			if (mDetector.onTouchEvent(event)) {
//				Log.d(TAG, "SWIPE DETECTED!");
//				return true;
//			}
//			switch (event.getAction()) {
//			case MotionEvent.ACTION_MOVE:
//				Log.d(TAG, "MOVE!");
//				return true;
//			case MotionEvent.ACTION_CANCEL:
//			case MotionEvent.ACTION_UP:
//				mShadow.stopDrag();
//				mDropZones.setExitDragShadow(mShadow);
//			}
//			return false;
//        }
//	};
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		//mPager.setCurrentItem(0);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mPager = (VerticalViewPager) findViewById(R.id.vp);
		
		mSwipeGestureListener = new SwipeMenuGestureListener(this);
		mDetector = new GestureDetector(this, mSwipeGestureListener);
		mShadow = new SwipeMenuDragShadow(this, (ViewGroup) findViewById(R.id.container));
		mDropZones = new SwipeMenuDropZones(this, this);
		mDropZones.setIncreaseHitbox(60);
		mDropZones.setDropZone(DropZone.Left, findViewById(R.id.drop_zone_left));
		mDropZones.setDropZone(DropZone.Right, findViewById(R.id.drop_zone_right));
		mTouchListener = new SwipeMenuTouchListener(mPager, mDetector, mDropZones);
		mTouchListener.setDragShadow(mShadow);
		
		ArrayList<Integer> resIds = new ArrayList<Integer>();
		resIds.add(R.drawable.img_1);
		resIds.add(R.drawable.img_2);
		resIds.add(R.drawable.img_3);
		resIds.add(R.drawable.img_4);
		resIds.add(R.drawable.img_5);
		
//		ImageView testView = (ImageView) findViewById(R.id.iv_image);
//		testView.setImageResource(R.drawable.img_1);
		
		mAdapter = new PagerAdapter() {
			
			@Override
			public View instantiateItem(ViewGroup container, int position) {
				LayoutInflater inflater = getLayoutInflater();
				View v = inflater.inflate(R.layout.vp_page, null);
				Log.d(TAG, "-------------> inflated view!");
				ImageView iv = (ImageView) v.findViewById(R.id.vp_page_image);
				int resId = mResIds.get(position);
				iv.setImageResource(resId);
				Log.d(TAG, "----------> set image view with res id: " + resId);
				float topY = v.getY();
				v.setTag(position);	// XXX: VERY IMPORTANT!!!!!
				container.addView(v);
				return v;
			}
			
			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				View view = (View) object;
		        ((VerticalViewPager) container).removeView(view);
			}
			
			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view == object;
			}
			
			@Override
			public int getCount() {
				int count = mResIds.size();
				//Log.d(TAG, "count: " + count);
				return count;
			}
		};
		mAdapter.setResIds(resIds);
		mAdapter.notifyDataSetChanged();
		mPager.setOffscreenPageLimit(1);
		mPager.setAdapter(mAdapter);
		mPager.setPageTransformer(true, new DepthPageTransformer());
		mPager.setOnPageChangeListener(this);
		mPager.setOnTouchListener(mTouchListener);	// always set ontouch on viewpager, not this child views
		
//		mSwipeManager = new SwipeMenuManager(this, this);
//		mSwipeManager.setViewPager(mPager);
//		mSwipeManager.setDropZone(DropZone.Idle, findViewById(R.id.container));
//		mSwipeManager.
//		mSwipeManager.
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

	@Override
	public void onPageSelected(int position) {
		Log.d(TAG, "onPageSelected! -----> " + position);
		if (position < mAdapter.getCount()) {
			mCurrentView = mPager.getChildAt(position);
			//mSwipeManager.setCurrentCard(mCurrentView, View.VISIBLE);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {}

	@Override
	public SwipeMenuDragShadow postback_drop(DropZone dropZone) {
		if (dropZone == DropZone.Left) {
			Toast.makeText(this, "Left", Toast.LENGTH_SHORT).show();
		}
		else if (dropZone == DropZone.Right) {
			Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show();
		}
		return mShadow;
	}

	@Override
	public void postback_onClick() {
		Toast.makeText(this, "onClick position: " + mPager.getCurrentItem(), Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void postback_onHorizontalScroll(MotionEvent event, boolean consumed) {
		if (consumed) {
			if (mCurrentView == null) {
				mCurrentView = mPager.findViewWithTag(mPager.getCurrentItem());
			}
			mShadow.startDrag(mPager.findViewWithTag(mPager.getCurrentItem()), event, mPager.getLeft(), mPager.getTop());
			mShadow.setMotionEvent(this, event, mCurrentView.getX());
		}
		else {
			if (!mShadow.isDragging()) mPager.onTouchEvent(event);
		}
	}
}
