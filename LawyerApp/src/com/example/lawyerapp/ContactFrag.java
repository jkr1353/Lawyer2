package com.example.lawyerapp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ContactFrag extends ListFragment {

	private ArrayList<Contact> Contacts = new ArrayList<Contact>();
	private boolean created=false;
	private Button newButton, pickCurrent, addNewMileage, deleteContact;
	
	@Override
	public void onCreate(Bundle saved) {
		super.onCreate(saved);
		
		newButton = (Button) getActivity().findViewById(R.id.buttonNewHours);
		pickCurrent = (Button) getActivity().findViewById(R.id.buttonNewExpense);
		addNewMileage = (Button) getActivity().findViewById(R.id.buttonNewMileage);
		deleteContact = (Button) getActivity().findViewById(R.id.buttonDelete);
		
		pickCurrent.setVisibility(View.VISIBLE);
		newButton.setVisibility(View.VISIBLE);
		deleteContact.setVisibility(View.VISIBLE);
		
		addNewMileage.setVisibility(View.INVISIBLE);
		
		pickCurrent.setText("Import Existing Contact");
		newButton.setText("New Contact");
		deleteContact.setText("Delete Contact");
		
		
		
		newButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			  public void onClick(View view) 
			{
				/*
				//ContactFrag.addActivityListener(someActivityListener);
				Intent NewCaseIntent = new Intent(Intent.ACTION_INSERT);
				NewCaseIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
				int PICK_CONTACT = 100;
				startActivityForResult(NewCaseIntent,PICK_CONTACT);
				*/
				Intent intent = new Intent(Intent.ACTION_INSERT, 
                        ContactsContract.Contacts.CONTENT_URI);
startActivity(intent);
			}		 
		});
		pickCurrent.setOnClickListener(new View.OnClickListener() {
			
			@Override
			  public void onClick(View view) 
			{
				final int CONTACT_PICKER_RESULT = 1001; 
				Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,  
			            ContactsContract.Contacts.CONTENT_URI);  
			    startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
			}		 
		});
		
		
		
		ContentResolver cr = getActivity().getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

		if (cur.getCount() > 0 && created==false) {
		while (cur.moveToNext()) {
			created=true;
			Contact c=new Contact();
			c.setID(cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID)));
			c.setPicture(loadContactPhoto(cr,Long.valueOf(c.getID())));
		    //String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
			c.setName(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
		    //String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		    if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
		      // This inner cursor is for contacts that have multiple numbers.
		      Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { c.getID() }, null);
		      int PhoneIdx = pCur.getColumnIndex(Phone.DATA);
		      while (pCur.moveToNext()) {
		    	  c.getNumbers().add(pCur.getString(PhoneIdx));
		    	  Contacts.add(c);		        
		      }
		      pCur.close();
		    }
		  }
		}
		
		cur.close();
		
		
		ContactAdapter adapter = new ContactAdapter(Contacts); 
		setListAdapter(adapter);
	}
	
	public static Bitmap loadContactPhoto(ContentResolver cr, long  id) {
	    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
	    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
	    if (input == null) {
	        return null;
	    }
	    return BitmapFactory.decodeStream(input);
	}
	
	
	
	private class ContactAdapter extends ArrayAdapter<Contact> {
		public ContactAdapter(ArrayList<Contact> contacts) {
			super(getActivity(), 0, contacts); 
		}
		
		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.contact_item,  null); 
				
			}
			
			// configure the view
			final Contact c = getItem(pos); 
			
			ImageView pic= (ImageView)convertView.findViewById(R.id.contact_pic);
			pic.setImageBitmap(c.getPicture());
			
			TextView name = (TextView) convertView.findViewById(R.id.contact_display);
			name.setText(c.getName());
			
			TextView number = (TextView) convertView.findViewById(R.id.contact_number);
			number.setText(c.getNumbers().get(0));
			
			
			Button B =(Button)convertView.findViewById(R.id.call_button);
			B.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					String number = "tel:" + c.getNumbers().get(0);
			        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number)); 
			        startActivity(callIntent);
				}
			});
			
			Button B2 =(Button)convertView.findViewById(R.id.edit_button);
			B2.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					 Intent editintent = new Intent(Intent.ACTION_VIEW);
					    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(c.getID()));
					    editintent.setData(uri);
					startActivity(editintent);
				}
			});
			
			
			return convertView; 
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {  
	    if (resultCode == Activity.RESULT_OK) {  
	    	Bundle extras = data.getExtras();  
	    	
	    	   if (resultCode == Activity.RESULT_OK) {

	    	     Uri contactData = data.getData();
	    	     Cursor c =  getActivity().managedQuery(contactData, null, null, null, null);
	    	     if (c.moveToFirst()) {


	    	         String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

	    	         String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

	    	           if (hasPhone.equalsIgnoreCase("1")) {
	    	          Cursor phones = getActivity().getContentResolver().query( 
	    	                       ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
	    	                       ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id, 
	    	                       null, null);
	    	             phones.moveToFirst();
	    	              String cNumber = phones.getString(phones.getColumnIndex("data1"));
	    	             System.out.println("number is:"+cNumber);
	    	           }
	    	         String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

	    	     }
	    	     }
	    	   
	    	/*
	    	Uri result = data.getData();
	    	String id=result.getPath();
	    	ContentResolver cr = getActivity().getContentResolver();
	    	Cursor c;
	    	c = cr.query(  ContactsContract.Contacts.CONTENT_URI
	    			
	    	        new String[]{id}, null);
	    	Cursor c=cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
	    	c.c.getColumnIndex(ContactsContract.Contacts._ID);
	    	c.getColumnIndex(id);
	    	
	    	
	    	/*
	    	
	    	
	    	Set<String> keys = extras.keySet();  
	    	Iterator<String> iterate = keys.iterator();  
	    	
	    	while (iterate.hasNext()) {  
	    	    String key = iterate.next();  
	    	    Log.v("tag1", key + "[" + extras.get(key) + "]");  
	    	}  
	    	
	    	Uri result = data.getData();  
	    	Log.v(DEBUG_TAG, "Got a result: "  
	    	    + result.toString());  
	    	</String></String>
	    	*/

	    }  
	} 
	
	/*
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saved) {
		View v = inflater.inflate(R.layout.contact_frag, parent,false); 
		
		
		ContentResolver cr = getActivity().getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

		if (cur.getCount() > 0) {
		while (cur.moveToNext()) {
		    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
		    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		    if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
		      // This inner cursor is for contacts that have multiple numbers.
		    	//String [] phonenumber=new String[] { id };
		      Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
		      int PhoneIdx = pCur.getColumnIndex(Phone.DATA);
		      while (pCur.moveToNext()) {
		        phoneContactList.add(name);
		        //phoneNumbers.add(pCur.getString(PhoneIdx));
		      }
		      pCur.close();
		    }
		  }
		
		  //Collections.sort(phoneContactList); 
		  //int cnt = phoneContactList.size();

		  
		  ArrayAdapter<String> adapter= new ArrayAdapter<String>(getActivity(), R.layout.contact_item,R.id.contact_display, phoneContactList);
		  lv.setAdapter(adapter);
		  lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		}
		cur.close();
			
			
		
		return v; 
	}
	*/
}
