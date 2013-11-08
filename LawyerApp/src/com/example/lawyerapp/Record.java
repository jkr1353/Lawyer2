package com.example.lawyerapp;

import java.util.Date;
import java.util.UUID;
import java.io.Serializable;

@SuppressWarnings("serial") //with this annotation we are going to hide compiler warning
public class Record implements Serializable {

	private UUID mId;
	private String mTitle;
	private Date mDate;
	
	
	public Record() {
		mId = UUID.randomUUID(); 
		mDate = new Date(); 
	}

	public UUID getId() {
		return mId;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	
	public Date getDate() {
		return mDate;
	}

	public void setDate(Date mDate) {
		this.mDate = mDate;
	}
	
	public void setDateNow() {
		this.mDate = new Date();
	}	
	
	@Override
	public String toString() {
		return mTitle; 
	}
	
}
