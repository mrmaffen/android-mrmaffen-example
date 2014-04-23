package com.example.mrmaffen.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;


public abstract class ScrollHelper
{
	private OverScroller    mScroller;
	private VelocityTracker mVelocityTracker;
	private int             mMinimumVelocity, mMaximumVelocity;
	private int             mTouchSlop;
	private float           mLastTouchX, mLastTouchY;
	private boolean         mDragging = false;
	
	
	@SuppressLint("Recycle")
	public ScrollHelper(Context context)
	{
		mScroller = new OverScroller(context);
		mVelocityTracker = VelocityTracker.obtain();
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
		mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
	}
	
	public abstract boolean shouldIntercept(float deltaX, float deltaY);
	
	public abstract void setScrollTo(int x, int y);
	public abstract int getScrollX();
	public abstract int getScrollY();
	
	public abstract int getWindowWidth();
	public abstract int getWindowHeight();
	public abstract int getContentWidth();
	public abstract int getContentHeight();
	
	
	public void fling(View v, int velocityX, int velocityY)
	{
		mScroller.fling(getScrollX(), getScrollY(),
			velocityX, velocityY, 0,
			Math.max(0, getContentWidth()  - getWindowWidth()), 0,
			Math.max(0, getContentHeight() - getWindowHeight()));
		v.invalidate();
	}
	
	private int clamp(int n, int my, int child)
	{
		if(my >= child || n < 0){
			return 0;
		}
		if((my + n) > child){
			return child - my;
		}
		return n;
	}
	
	public void computeScroll(View v)
	{
		if(mScroller.computeScrollOffset()){
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			v.postInvalidate();
		}
	}
	
	public void scrollTo(int x, int y)
	{
		int oldX = getScrollX();
		int oldY = getScrollY();
		int newX = clamp(x, getWindowWidth(),  getContentWidth());
		int newY = clamp(y, getWindowHeight(), getContentHeight());
		if(newX != oldX || newY != oldY){
			setScrollTo(newX, newY);
		}
	}
	
	public boolean onInterceptTouchEvent(MotionEvent event)
	{
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				if(!mScroller.isFinished()){
					mScroller.abortAnimation();
				}
				mVelocityTracker.clear();
				mVelocityTracker.addMovement(event);
				mLastTouchX = event.getX();
				mLastTouchY = event.getY();
				break;
			
			case MotionEvent.ACTION_MOVE:
				final float x = event.getX();
				final float y = event.getY();
				final float xDiff = (x - mLastTouchX);
				final float yDiff = (y - mLastTouchY);
				if( (int)Math.abs(xDiff) > mTouchSlop || 
					(int)Math.abs(yDiff) > mTouchSlop)
				{
					if(shouldIntercept(xDiff, yDiff)){
						mDragging = true;
						mVelocityTracker.addMovement(event);
						return true;
					}
				}
				break;
			
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				mDragging = false;
				mVelocityTracker.clear();
				break;
		}
		
		return false;
	}
	
	public boolean onTouchEvent(View v, MotionEvent event)
	{
		mVelocityTracker.addMovement(event);
		
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				return true;
				
			case MotionEvent.ACTION_MOVE:
				final float x = event.getX();
				final float y = event.getY();
				float deltaY = mLastTouchY - y;
				float deltaX = mLastTouchX - x;
				if(!mDragging && (
					Math.abs(deltaY) > mTouchSlop || 
					Math.abs(deltaX) > mTouchSlop) )
				{
					mDragging = true;
				}
				if(mDragging){
					scrollTo(
						getScrollX() + (int)deltaX, 
						getScrollY() + (int)deltaY);
					mLastTouchX = x;
					mLastTouchY = y;
				}
				break;
				
			case MotionEvent.ACTION_CANCEL:
				mDragging = false;
				if(!mScroller.isFinished()){
					mScroller.abortAnimation();
				}
				break;
				
			case MotionEvent.ACTION_UP:
				mDragging = false;
				mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
				int velocityX = (int)mVelocityTracker.getXVelocity();
				int velocityY = (int)mVelocityTracker.getYVelocity();
				if(Math.abs(velocityX) > mMinimumVelocity || Math.abs(velocityY) > mMinimumVelocity){
					fling(v, -velocityX, -velocityY);
				}
				break;
		}
		
		return false;
	}
}
