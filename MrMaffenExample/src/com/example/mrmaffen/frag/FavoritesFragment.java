package com.example.mrmaffen.frag;

import com.example.mrmaffen.R;


public class FavoritesFragment extends BaseListFragment
{
	public FavoritesFragment()
	{
		// required by platform
	}
	
	@Override
	public String getFragmentTitle()
	{
		return getString(R.string.frag_favorites);
	}
}
