package com.example.lawyerapp;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
/*
public class RecordFragment extends Fragment {
	private Record mRecord; 
	private EditText mEditTitle;
	private Button mDocsBut;
	private Button mContactBut;
	private Button mTimeBut;
	private TextView mTitle;
	private ArrayList<Record> mCat; 
	private Fragment contactfrag; //1
	private Fragment filefrag;    //2
	private Fragment logfrag;     //3
	private int FragSelect=0;
	
	
	
	
	@Override
	public void onCreate(Bundle saved) {
		
		super.onCreate(saved);
		Bundle bundle=getArguments();
		
		mCat = Catalog.get(getActivity()).getRecords();
		mRecord= (Record)mCat.get(bundle.getInt("recordnum"));		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saved) {
		View v = inflater.inflate(R.layout.fragment_record, parent,false); 
		
		mTitle=(TextView) v.findViewById(R.id.Title);
		mTitle.setText(mRecord.getmTitle());
		mEditTitle=(EditText) v.findViewById(R.id.editTitle);
		mEditTitle.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start, int before, int count) {
				mRecord.setmTitle(c.toString());
				mTitle.setText(mRecord.getmTitle());
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			
		}); 
		
		final LinearLayout editLayout = (LinearLayout) v.findViewById(R.id.editLayout);
		final LinearLayout titleLayout = (LinearLayout) v.findViewById(R.id.titleLayout);
		
		if(!(v instanceof EditText)) {

	        v.setOnTouchListener(new OnTouchListener() {

	            public boolean onTouch(View v, MotionEvent event) {
	            	editLayout.setVisibility(View.GONE);
                    titleLayout.setVisibility(View.VISIBLE);
                    mTitle.setVisibility(View.VISIBLE);
	                return false;
	            }

	        });
	    }
		
		mTitle.setOnClickListener(new View.OnClickListener() {

			  @Override
			  public void onClick(View view) {
				  mTitle.setVisibility(View.GONE);
				  if(editLayout.getVisibility() == View.VISIBLE)
				  {
	                    editLayout.setVisibility(View.GONE);
	                    titleLayout.setVisibility(View.VISIBLE);
				  }
	                else
	                {
	                    editLayout.setVisibility(View.VISIBLE);
	                    titleLayout.setVisibility(View.GONE);
	                }
			  }

			});
		
		mDocsBut=(Button) v.findViewById(R.id.recentDocs);
		mContactBut=(Button) v.findViewById(R.id.recentContacts);
		mTimeBut=(Button) v.findViewById(R.id.recentTimeLogs);
		
		contactfrag = new ContactFrag();
		filefrag = new FileFrag();
		logfrag = new LogFrag();
		final FragmentManager fm = getFragmentManager();
		
		mDocsBut.setOnClickListener(new View.OnClickListener() {

			  @Override
			  public void onClick(View view) {
				  mDocsBut.setBackgroundColor(Color.parseColor("#AFDCEC"));
				  mContactBut.setBackgroundColor(Color.parseColor("#157DEC"));
				  mTimeBut.setBackgroundColor(Color.parseColor("#157DEC"));
				  
				  
					if (FragSelect==0)
					{
						FragmentTransaction fta = fm.beginTransaction();
						fta.add(R.id.tabfrag, filefrag);
						fta.commit(); 
						FragSelect=2;
					}
					
					
					if (FragSelect != 2) {
						if(FragSelect==1)
						{
							FragmentTransaction fta = fm.beginTransaction();
							fta.remove(contactfrag).commit();
						}
						else  //3
						{
							FragmentTransaction fta = fm.beginTransaction();
							fta.remove(logfrag).commit();
						}
						FragmentTransaction fta = fm.beginTransaction();
						fta.add(R.id.tabfrag, filefrag);
						fta.commit(); 
						FragSelect=2;
					}
			  }

			});
		mContactBut.setOnClickListener(new View.OnClickListener() {

			  @Override
			  public void onClick(View view) {
				  mDocsBut.setBackgroundColor(Color.parseColor("#157DEC"));
				  mContactBut.setBackgroundColor(Color.parseColor("#AFDCEC"));
				  mTimeBut.setBackgroundColor(Color.parseColor("#157DEC"));
				  
				  if (FragSelect==0)
					{
						FragmentTransaction fta = fm.beginTransaction();
						fta.add(R.id.tabfrag, contactfrag);
						fta.commit(); 
						FragSelect=1;
					}
					
					
					if (FragSelect != 1) {
						if(FragSelect==2)
						{
							FragmentTransaction fta = fm.beginTransaction();
							fta.remove(filefrag).commit();
						}
						else  //3
						{
							FragmentTransaction fta = fm.beginTransaction();
							fta.remove(logfrag).commit();
						}
						FragmentTransaction fta = fm.beginTransaction();
						fta.add(R.id.tabfrag, contactfrag);
						fta.commit(); 
						FragSelect=1;
					}
			  }

			});
		mTimeBut.setOnClickListener(new View.OnClickListener() {

			  @Override
			  public void onClick(View view) {
				  mDocsBut.setBackgroundColor(Color.parseColor("#157DEC"));
				  mContactBut.setBackgroundColor(Color.parseColor("#157DEC"));
				  mTimeBut.setBackgroundColor(Color.parseColor("#AFDCEC"));
				  
					
				  if (FragSelect==0)
					{
						FragmentTransaction fta = fm.beginTransaction();
						fta.add(R.id.tabfrag, logfrag);
						fta.commit(); 
						FragSelect=3;
					}
					
					
					if (FragSelect != 3) {
						if(FragSelect==1)
						{
							FragmentTransaction fta = fm.beginTransaction();
							fta.remove(contactfrag).commit();
						}
						else  //2
						{
							FragmentTransaction fta = fm.beginTransaction();
							fta.remove(filefrag).commit();
						}
						FragmentTransaction fta = fm.beginTransaction();
						fta.add(R.id.tabfrag, logfrag);
						fta.commit(); 
						FragSelect=3;
					}
			  }

			});
		
		
		
		
		
		
		return v; 
	}
}
*/