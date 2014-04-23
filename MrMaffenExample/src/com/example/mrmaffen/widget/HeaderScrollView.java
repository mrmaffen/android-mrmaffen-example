package com.example.mrmaffen.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.OverScroller;


public class HeaderScrollView extends FrameLayout
{
	private static final String TAG = HeaderScrollView.class.getSimpleName();
	
	private OverScroller    mScroller;
	private VelocityTracker mVelocityTracker;
	
	private int     mTouchSlop, mMinimumVelocity, mMaximumVelocity;
	private float   mLastTouchX, mLastTouchY;
	private boolean mDragging = false;
	
	
	public HeaderScrollView(Context context)
	{
		super(context);
		initView(context, null);
	}
	
	public HeaderScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context, attrs);
	}
	
	public HeaderScrollView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initView(context, attrs);
	}
	
	@SuppressLint("Recycle")
	protected void initView(Context context, AttributeSet attrs)
	{
		mScroller        = new OverScroller(context);
		mVelocityTracker = VelocityTracker.obtain();
		mTouchSlop       = ViewConfiguration.get(context).getScaledTouchSlop();
		mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
		mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
	}
	
	@Override
	public boolean canScrollHorizontally(int direction)
	{
		final int scrollX = (getScrollContentWidth() - getScrollWindowWidth()) - (getScrollX() + direction);
		Log.w(TAG, "canScrollHorizontally: " + direction + " : " + scrollX);
		return (direction < 0) ? (scrollX > 0) : (scrollX < 0);
	}
	
	@Override
	public boolean canScrollVertically(int direction)
	{
		final int scrollWH = getScrollWindowHeight();
		final int scrollCH = getScrollContentHeight();
		final int scrollY  = getScrollY();
		final int pos = Math.max(0, (scrollCH - scrollWH) - (getScrollY() - direction));
		Log.w(TAG, "canScrollVertically(" + direction + ") : (" + scrollWH + ", " + scrollCH + ", " + scrollY + ") = " + pos);
		return (direction < 0 && pos > 0) || (direction < 0 && pos == 0);
	}
	
	@Override
	public void requestDisallowInterceptTouchEvent(boolean disallowIntercept)
	{
		// Don't want to allow view's to do this
	}
	
	@Override
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
				final float deltaY = mLastTouchY - y;
				final float deltaX = mLastTouchX - x;
				if(Math.abs(deltaY) > mTouchSlop || Math.abs(deltaX) > mTouchSlop){
					if(canScrollVertically((int)deltaY)){// || canScrollHorizontally((int)deltaX)){
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
		
		return super.onInterceptTouchEvent(event);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		mVelocityTracker.addMovement(event);
		
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				return true;
				
			case MotionEvent.ACTION_MOVE:
				final float x = event.getX();
				final float y = event.getY();
				final float deltaY = mLastTouchY - y;
				final float deltaX = mLastTouchX - x;
				if(!mDragging && (Math.abs(deltaY) > mTouchSlop || Math.abs(deltaX) > mTouchSlop)){
					mDragging = true;
				}
				if(mDragging){
					scrollBy((int)deltaX, (int)deltaY);
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
				final int velocityX = (int)mVelocityTracker.getXVelocity();
				final int velocityY = (int)mVelocityTracker.getYVelocity();
				if(Math.abs(velocityX) > mMinimumVelocity || Math.abs(velocityY) > mMinimumVelocity){
					fling(-velocityX, -velocityY);
				}
				break;
		}
		return super.onTouchEvent(event);
	}
	
	public void fling(int velocityX, int velocityY)
	{
		if(getChildCount() > 0){
			mScroller.fling(getScrollX(), getScrollY(), velocityX, velocityY,
				0, Math.max(0, getScrollContentWidth()  - getScrollWindowWidth()),
				0, Math.max(0, getScrollContentHeight() - getScrollWindowHeight()));
			
			invalidate();
		}
	}
	
	@Override
	public void computeScroll()
	{
		if(mScroller.computeScrollOffset()){
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}
	
	@Override
	public void scrollTo(int x, int y)
	{
		if(getChildCount() > 0){
			x = clamp(x, getScrollWindowWidth(),  getScrollContentWidth());
			y = clamp(y, getScrollWindowHeight(), getScrollContentHeight());
			if(x != getScrollX() || y != getScrollY()){
				super.scrollTo(x, y);
			}
		}
	}
	
	protected int getScrollWindowWidth()
	{
		return getWidth()  - getPaddingRight()  - getPaddingLeft();
	}
	
	protected int getScrollWindowHeight()
	{
		return getHeight() - getPaddingBottom() - getPaddingTop();
	}
	
	protected int getScrollContentWidth()
	{
		return getChildAt(0).getWidth();
	}
	
	protected int getScrollContentHeight()
	{
		return getChildAt(0).getHeight();
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
}
