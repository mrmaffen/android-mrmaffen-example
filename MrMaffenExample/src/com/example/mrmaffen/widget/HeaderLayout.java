package com.example.mrmaffen.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;


/**
 * Positions it's views vertically like LinearLayout but allows for children to have match_parent
 */
public class HeaderLayout extends FrameLayout
{
	public HeaderLayout(Context context)
	{
		super(context);
	}
	
	public HeaderLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public HeaderLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int count = getChildCount();
		int contentHeight = getPaddingTop();
		for(int i = 0; i < count; i++){
			final View child = getChildAt(i);
			if(child.getVisibility() != GONE){
				contentHeight += child.getMeasuredHeight();
			}
		}
		setMeasuredDimension(getMeasuredWidth(), contentHeight);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		final int left = getPaddingLeft();
		final int right = r - l - getPaddingRight();
		int top = getPaddingTop();
		final int count = getChildCount();
		for(int i = 0; i < count; i++){
			final View child = getChildAt(i);
			if(child.getVisibility() != GONE){
				final int width  = child.getMeasuredWidth();
				final int height = child.getMeasuredHeight();
				child.layout(left, top, Math.min(left + width, right), top + height);
				top += height;
			}
		}
	}
}
