package com.example.lawyerapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import android.graphics.Bitmap;
import android.net.Uri;

public class Contact {
	
	private String name;
	private ArrayList<String> numbers ;
	private String ID;
	private Bitmap picture;
	private Uri ContactUri;
	
	public Contact() {
		numbers=new ArrayList<String>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public ArrayList<String> getNumbers() {
		return numbers;
	}
	public void setNumbers(ArrayList<String> numbers) {
		this.numbers = numbers;
	}

	public Bitmap getPicture() {
		return picture;
	}

	public void setPicture(Bitmap picture) {
		this.picture = picture;
	}

	public Uri getContactUri() {
		return ContactUri;
	}

	public void setContactUri(Uri contactUri) {
		ContactUri = contactUri;
	}
	

}
