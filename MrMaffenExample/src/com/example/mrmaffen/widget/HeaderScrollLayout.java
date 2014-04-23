package com.example.mrmaffen.widget;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;


public class HeaderScrollLayout extends FrameLayout
{
	private static final String TAG = HeaderScrollLayout.class.getSimpleName();
	
	private static boolean IS_HONEYCOMB = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB);
	
	private HeaderScrollHelper mScrollHelper;
	private int mOffset;
	private int mContentHeight;
	private int mHeaderHeight;
	
	
	public HeaderScrollLayout(Context context)
	{
		super(context);
		initView(context, null);
	}
	
	public HeaderScrollLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context, attrs);
	}
	
	public HeaderScrollLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initView(context, attrs);
	}
	
	protected void initView(Context context, AttributeSet attrs)
	{
		mScrollHelper = new HeaderScrollHelper(context);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int headerHeight = 0, contentHeight = 0, childCount = getChildCount();
		for(int i = 0; i < childCount; i++){
			final View child = getChildAt(i);
			if(child == null || child.getVisibility() == View.GONE)
				continue;
			headerHeight = child.getMeasuredHeight();
			contentHeight += headerHeight;
		}
		mHeaderHeight = headerHeight;
		mContentHeight = contentHeight;
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		super.onLayout(changed, l, t, r, b);
		updateViewPositions();
	}
	
	private void updateViewPositions()
	{
		final int childCount = getChildCount();
		for(int i = 0; i < childCount; i++){
			final View child = getChildAt(i);
			if(child == null || child.getVisibility() == View.GONE)
				continue;
			int offset = ((i == childCount - 1) ? 0 : mHeaderHeight) - mOffset;
			if(IS_HONEYCOMB){
				child.setTranslationY(offset);
			}
			else{
				child.offsetTopAndBottom(offset - child.getTop());
			}
		}
	}
	
	/*@Override
	public void requestDisallowInterceptTouchEvent(boolean disallowIntercept)
	{
		// We don't allow this
	}*/
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event)
	{
		if(mScrollHelper.onInterceptTouchEvent(event)){
			return true;
		}
		return super.onInterceptTouchEvent(event);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(mScrollHelper.onTouchEvent(this, event)){
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	public void computeScroll()
	{
		mScrollHelper.computeScroll(this);
	}
	
	
	private class HeaderScrollHelper extends ScrollHelper
	{
		public HeaderScrollHelper(Context context)
		{
			super(context);
		}
		
		@Override
		public boolean shouldIntercept(float deltaX, float deltaY)
		{
			// Log.e(TAG, "shouldIntercept: " + deltaX + ", " + deltaY + " : " + getScrollY());
			if(Math.abs(deltaX) > Math.abs(deltaY)){
				return false;
			}
			return (deltaY < 0) ? (mOffset <= 0) : (mOffset > 0);
		}
		
		@Override
		public void setScrollTo(int x, int y)
		{
			mOffset = y;
			updateViewPositions();
		}
		
		@Override
		public int getScrollX()
		{
			return 0;
		}
		
		@Override
		public int getScrollY()
		{
			return mOffset;
		}
		
		@Override
		public int getWindowWidth()
		{
			return getWidth() - getPaddingRight() - getPaddingLeft();
		}
		
		@Override
		public int getWindowHeight()
		{
			return getHeight() - getPaddingBottom() - getPaddingTop();
		}
		
		@Override
		public int getContentWidth()
		{
			return getWidth();
		}
		
		@Override
		public int getContentHeight()
		{
			return mContentHeight;
		}
	}
	
	
	@Override
	protected Parcelable onSaveInstanceState()
	{
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.offset = mOffset;
		return ss;
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state)
	{
		SavedState ss = (SavedState)state;
		mOffset = ss.offset;
		super.onRestoreInstanceState(ss.getSuperState());
	}
	
	
	static class SavedState extends BaseSavedState
	{
		public int offset;
		
		
		public SavedState(Parcelable superState)
		{
			super(superState);
		}
		
		public SavedState(Parcel source)
		{
			super(source);
			offset = source.readInt();
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			super.writeToParcel(dest, flags);
			dest.writeInt(offset);
		}
		
		@Override
		public String toString()
		{
			return "HeaderLayout.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " offset="
				+ offset + "}";
		}
		
		
		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>()
		{
			public SavedState createFromParcel(Parcel in)
			{
				return new SavedState(in);
			}
			
			public SavedState[] newArray(int size)
			{
				return new SavedState[size];
			}
		};
	}
}
