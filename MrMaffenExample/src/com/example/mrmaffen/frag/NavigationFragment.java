package com.example.mrmaffen.frag;

import java.util.ArrayList;

import com.example.mrmaffen.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class NavigationFragment extends BaseFragment
{
	private DrawerLayout mDrawerLayout;
	private ListView mListView;
	private NavigationListAdapter mAdapter;
	
	
	public NavigationFragment()
	{
		// required by platform
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mAdapter = new NavigationListAdapter(getActivity());
		mAdapter.addNavigationItem(new NavigationItem(R.string.nav_albums));
		mAdapter.addNavigationItem(new NavigationItem(R.string.nav_artists));
		mAdapter.addNavigationItem(new NavigationItem(R.string.nav_songs));
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_navigation, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		mListView = (ListView)view.findViewById(R.id.list);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mItemClickListener);
		if(savedInstanceState == null){
			mListView.setItemChecked(0, true);
		}
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		if(getView().getParent() instanceof DrawerLayout){
			mDrawerLayout = (DrawerLayout)getView().getParent();
		}
	}
	
	private ListView.OnItemClickListener mItemClickListener = new ListView.OnItemClickListener() 
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			mListView.setItemChecked(position, true);
			if(mDrawerLayout != null){
				mDrawerLayout.closeDrawer(getView());
			}
			// TODO: Navigate to item
		}
	};
	
	
	private static class NavigationListAdapter extends BaseAdapter
	{
		private LayoutInflater mInflater;
		private ArrayList<NavigationItem> mItems = new ArrayList<NavigationItem>();
		
		
		public NavigationListAdapter(Context context)
		{
			mInflater = LayoutInflater.from(new ContextThemeWrapper(context, R.style.AppThemeDark));
		}
		
		public void addNavigationItem(NavigationItem item)
		{
			mItems.add(item);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.item_nav, parent, false);
			}
			TextView itemView = (TextView)convertView.findViewById(android.R.id.text1);
			itemView.setText(mItems.get(position).getTitleStringId());
			return convertView;
		}
		
		@Override
		public NavigationItem getItem(int position)
		{
			return mItems.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		@Override
		public int getCount()
		{
			return mItems.size();
		}
	}
	
	private static class NavigationItem
	{
		private final int mTitleStringId;
		
		
		public NavigationItem(int titleStringId)
		{
			mTitleStringId = titleStringId;
		}
		
		public int getTitleStringId()
		{
			return mTitleStringId;
		}
	}
}
