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

import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.util.Log;
import android.view.View;

public class SwipeMenuDropZones {

	public static final String TAG = "SwipeMenuDropZones";
	
	@SuppressWarnings("unused")
	private Context mContext = null;
	private SwipeMenuDropEvents mSwipeMenuEventListener = null;
	private HashMap<DropZone,View> mDropZones = new HashMap<DropZone,View>();
	
	private float mPadding = 0.0f;
	
	public static enum DropZone {
		Left, Right
	}
	
	public interface SwipeMenuDropEvents {
		public SwipeMenuDragShadow postback_drop(DropZone dropZone); 
	}
	
	public SwipeMenuDropZones(Context context, SwipeMenuDropEvents swipeMenuEventListener) {
		mContext = context;
		mSwipeMenuEventListener = swipeMenuEventListener;
	}
	
	public void setDropZone(DropZone dropZone, View dropZoneView) {
		mDropZones.put(dropZone, dropZoneView);
	}
	
	public void setIncreaseHitbox(float padding) {
		mPadding = padding;
	}
	
	public void setExitDragShadow(SwipeMenuDragShadow dragShadow) {
		Log.d(TAG, "last x: " + dragShadow.getLastX() + ", last y: " + dragShadow.getLastY());
		if (!dragShadow.mDragged) return;
		Iterator<DropZone> iter = mDropZones.keySet().iterator();
		while (iter.hasNext()) {
			DropZone dropZone = iter.next();
			View dropZoneView = mDropZones.get(dropZone);
			if (checkWithinBounds(dropZone, dragShadow, dropZoneView)) {
				SwipeMenuDragShadow shadow = mSwipeMenuEventListener.postback_drop(dropZone);
				shadow.reset();
				break;
			}
		}
	}
	
	private boolean checkWithinBounds(DropZone dropZone, SwipeMenuDragShadow dragShadow, View dropZoneView) {
		float left = dropZoneView.getLeft();
		//float top = dropZoneView.getTop();
		float right = dropZoneView.getRight();
		//float bottom = dropZoneView.getBottom();
		
		if (dropZone == DropZone.Left) {
			if (dragShadow.getLastX() < right + mPadding) {
				Log.d(TAG, "dropped on left");
				return true;
			}
		}
		else if (dropZone == DropZone.Right) {
			if (dragShadow.getLastX() >= left - mPadding) {
				Log.d(TAG, "dropped on right");
				return true;
			}
		}
		Log.d(TAG, "dropped on idle");
		return false;
	}
}
