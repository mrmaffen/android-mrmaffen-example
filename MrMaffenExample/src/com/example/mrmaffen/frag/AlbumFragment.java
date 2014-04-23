package com.example.mrmaffen.frag;

import com.example.mrmaffen.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AlbumFragment extends BaseFragment
{
	private ViewPager mViewPager;
	
	
	public AlbumFragment()
	{
		// required by platform
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_album, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		mViewPager = (ViewPager)view.findViewById(R.id.pager);
		mViewPager.setAdapter(new AlbumPagerAdapter(getFragmentManager()));
	}
	
	
	public class AlbumPagerAdapter extends FragmentPagerAdapter
	{
		private final int[] TITLES = {
			R.string.frag_feed,
			R.string.frag_favorites,
			R.string.frag_charts
		};
		
		
		public AlbumPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}
		
		@Override
		public Fragment getItem(int position)
		{
			final Fragment frag;
			switch(position){
				case 0:  frag = new FeedFragment();      break;
				case 1:  frag = new FavoritesFragment(); break;
				case 2:  frag = new ChartsFragment();    break;
				default: frag = null;                    break;
			}
			return frag;
		}
		
		@Override
		public CharSequence getPageTitle(int position)
		{
			return getString(TITLES[position]);
		}
		
		@Override
		public int getCount()
		{
			return 3;
		}
	}
}
