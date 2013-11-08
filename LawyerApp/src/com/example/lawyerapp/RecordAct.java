package com.example.lawyerapp;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordAct extends FragmentActivity {
	
	private Record mRecord; 
	private ArrayList<Record> mCat; 
	
	@Override
	public void onBackPressed()
	{
		mRecord.setDateNow();
		super.onBackPressed();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recordact);
		
		
		
		
		mCat = Catalog.get(this).getRecords();
		mRecord= (Record)mCat.get(getIntExtra("recordnum",0));	
		
		FragmentManager fm = getSupportFragmentManager(); 
		
		Fragment frag = fm.findFragmentById(R.id.fragmentContainer); 
		
		if (frag == null) {
			
			
			
			Bundle args = new Bundle();
			Intent i=getIntent();
            args.putInt("recordnum",i.getIntExtra("recordnum", 0));
   
            
			//frag = new RecordFragment(); // RecordFragment();  
			//frag.setArguments(args);
			FragmentTransaction fta = fm.beginTransaction(); 
			fta.add(R.id.fragmentContainer, frag);
			fta.commit(); 
		}
	}

	private int getIntExtra(String string, int i) {
		// TODO Auto-generated method stub
		return 0;
	}


}