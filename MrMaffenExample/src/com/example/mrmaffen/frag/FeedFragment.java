package com.example.mrmaffen.frag;

import com.example.mrmaffen.R;


public class FeedFragment extends BaseListFragment
{
	public FeedFragment()
	{
		// required by platform
	}
	
	@Override
	public String getFragmentTitle()
	{
		return getString(R.string.frag_feed);
	}
}
