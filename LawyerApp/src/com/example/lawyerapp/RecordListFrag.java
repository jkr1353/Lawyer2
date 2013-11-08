package com.example.lawyerapp;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class RecordListFrag extends ListFragment {

	private ArrayList<Record> mCat; 
	
	@Override 
	public void onCreate(Bundle saved) {
		super.onCreate(saved); 
		
		
		mCat = Catalog.get(getActivity()).getRecords(); 
		Log.d("debug", mCat+""); 
		//ArrayAdapter<Record> adapter = new ArrayAdapter<Record>(getActivity(), 
		//		android.R.layout.simple_list_item_1, mCat);
		
		RecordAdapter adapter = new RecordAdapter(mCat); 
		setListAdapter(adapter);
		
	}
	
	@Override
	public void onResume()
	{
		reset();
		super.onResume();
	}
	
	public void reset()
	{
		((BaseAdapter) getListAdapter()).notifyDataSetChanged();
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int pos, long id){
		Record c = (Record) (getListAdapter()).getItem(pos); 
		c.setDateNow();
		Log.d("debug", "item " + pos + ": " + c.getmTitle() ); 
		
		Intent i = new Intent(getActivity(), RecordAct.class);
		i.putExtra("recordnum", pos);
		startActivity(i); 
	}
	
	private class RecordAdapter extends ArrayAdapter<Record> {
		public RecordAdapter(ArrayList<Record> records) {
			super(getActivity(), 0, records); 
		}
		
		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item,  null); 
				
			}
			
			// configure the view
			Record r = getItem(pos); 
			
			TextView tv = (TextView) convertView.findViewById(R.id.recordTitle);
			tv.setText(r.getmTitle());
			
			TextView dv = (TextView) convertView.findViewById(R.id.recordDate);
			dv.setText(r.getDate().toString());
			
			return convertView; 
		}
	}
}
