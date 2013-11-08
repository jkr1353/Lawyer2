package com.example.lawyerapp;

import java.util.ArrayList;
import java.util.UUID;



import android.content.Context;
import android.widget.BaseAdapter;

public class Catalog {
	private static Catalog sCat;
	private Context mAppContext; 
	private ArrayList<Record> mRecords; 
	
	private Catalog(Context cxt) {
		mAppContext = cxt; 
		
		mRecords = new ArrayList<Record>(); 
		for (int i=0; i< 5; i++) {
			Record c = new Record();
			c.setmTitle("Case #" + i);
			mRecords.add(c); 
			
			
		}
	}
	
	public static Catalog get(Context c) {
		if (sCat == null) {
			sCat = new Catalog(c.getApplicationContext());
		}
		
		return sCat; 
	}

	public ArrayList<Record> getRecords() {
		return mRecords; 
	}
	
	public void CreateRecord(String name)
	{
		Record c = new Record();
		c.setmTitle(name);
		mRecords.add(c);		
	}
	
	public Record getOneRec(UUID id) {
		for (Record r: mRecords) {
			if (r.getId().equals(id)) {
				return r; 
			}
		}
		return null; 
	}

	
}
