package com.example.mrmaffen.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;


public class NoInterceptListView extends ListView
{
	public NoInterceptListView(Context context)
	{
		super(context);
		initView(context, null);
	}
	
	public NoInterceptListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context, attrs);
	}
	
	public NoInterceptListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initView(context, attrs);
	}
	
	protected void initView(Context context, AttributeSet attrs)
	{
		//
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		return false;//super.onInterceptTouchEvent(ev);
	}
}
