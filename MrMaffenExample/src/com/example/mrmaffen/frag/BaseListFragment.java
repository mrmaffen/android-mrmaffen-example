package com.example.mrmaffen.frag;

import java.util.ArrayList;
import java.util.List;

import com.example.mrmaffen.widget.NoInterceptListView;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;


public abstract class BaseListFragment extends ListFragment
{
	
	public abstract String getFragmentTitle();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		NoInterceptListView listView = new NoInterceptListView(getActivity());
		listView.setLayoutParams(new ViewGroup.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		listView.setId(android.R.id.list);
		listView.setDrawSelectorOnTop(false);
		return listView;//super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(getActivity(), 
			android.R.layout.simple_list_item_1, android.R.id.text1, 
			createDummyData(getFragmentTitle(), 100)));
	}
	
	protected static List<String> createDummyData(String prefix, int count)
	{
		ArrayList<String> data = new ArrayList<String>(count);
		for(int i = 0; i < count; i++){
			data.add(prefix + " " + i);
		}
		return data;
	}
}
