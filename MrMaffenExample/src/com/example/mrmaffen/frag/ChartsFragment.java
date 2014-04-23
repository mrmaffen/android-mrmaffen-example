package com.example.mrmaffen.frag;

import com.example.mrmaffen.R;


public class ChartsFragment extends BaseListFragment
{
	public ChartsFragment()
	{
		// required by platform
	}
	
	@Override
	public String getFragmentTitle()
	{
		return getString(R.string.frag_charts);
	}
}
