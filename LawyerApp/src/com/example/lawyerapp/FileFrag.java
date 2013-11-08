package com.example.lawyerapp;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class FileFrag extends ListFragment {

	private Button photoButton;
	private Button addNewExpense, addNewMileage, deleteFile;
	
	private Cursor cursor;
	
	private DaoInstance daoinstance;
	private FilesDao filesDao;
	private SQLiteDatabase db;
	
	private Long parentID;
	
	private String deleteFileStr, doneFileStr;
	
	@Override
	public void onCreate(Bundle saved) {
		super.onCreate(saved);
			
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saved) {
		View v = inflater.inflate(R.layout.file_frag, parent,false); 
		
		deleteFileStr = "Delete Photo";
        doneFileStr = "Done";
		
		daoinstance = DaoInstance.getInstance(getActivity());
		
		db = daoinstance.getDb();
		
		filesDao = daoinstance.getFilesDao();
		
		parentID = getActivity().getIntent().getExtras().getLong("id");
		
		Log.i("CS499", parentID.toString());
		
		photoButton = (Button) getActivity().findViewById(R.id.buttonNewHours);
		addNewExpense = (Button) getActivity().findViewById(R.id.buttonNewExpense);
		addNewMileage = (Button) getActivity().findViewById(R.id.buttonNewMileage);
		deleteFile = (Button) getActivity().findViewById(R.id.buttonDelete);
		
		addNewExpense.setVisibility(View.INVISIBLE);
		addNewMileage.setVisibility(View.INVISIBLE);
		
		photoButton.setText("New Photo");
		deleteFile.setText("Delete Photo");
		
		photoButton.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				Intent takePhoto = new Intent(getActivity(), PhotoIntentActivity.class);
				
				startActivityForResult(takePhoto, 1);
			}
			
		});
		
		deleteFile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				if (deleteFile.getText() == deleteFileStr)
				{
					deleteFile.setText(doneFileStr);
					
					photoButton.setVisibility(View.INVISIBLE);
				}
				else
				{
					deleteFile.setText(deleteFileStr);
					
					photoButton.setVisibility(View.VISIBLE);
				}
			}
		});
		
		String textColumn = FilesDao.Properties.Name.columnName;
		
		//String dateColumn = FilesDao.Properties.Date.columnName;
        String orderBy = textColumn + " COLLATE LOCALIZED DESC";
		
        cursor = db.query(filesDao.getTablename(), null, "PARENT_ID IN " +
        		"(SELECT PARENT_ID FROM FILES WHERE PARENT_ID = " + parentID.toString() + ")"
        		, null, null, null, orderBy);
        
        //cursor = db.query(filesDao.getTablename(), null, null, null, null, null, null, null);
        
        String[] from = {textColumn};
        int[] to = { R.id.textView1};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item2, cursor, from,
                to);
        setListAdapter(adapter);
		
		return v; 
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		addNewExpense.setVisibility(View.INVISIBLE);
		addNewMileage.setVisibility(View.INVISIBLE);
		photoButton.setVisibility(View.VISIBLE);
		deleteFile.setVisibility(View.VISIBLE);
		
		photoButton.setText("New Photo");
		deleteFile.setText("Delete Photo");
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (resultCode == Activity.RESULT_OK)
		{
			String name = "";
			name = data.getExtras().getString("name");
			
			String path = "";
			path = data.getExtras().getString("path");
			
			Files file = new Files(null, name, parentID, "photo", path);
	        filesDao.insertOrReplace(file);
			
	        cursor.requery();
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) 
	{
		final Files newFile = filesDao.queryBuilder().where(FilesDao.Properties.Id.eq(id)).unique();
		
		if (deleteFile.getText() == deleteFileStr)
		{	
			Intent newInt = new Intent(getActivity(), PhotoView.class);
			newInt.putExtra("path", newFile.getPath());
			newInt.putExtra("name", newFile.getName());
			
			startActivity(newInt);
		}
		else
		{
			File file = new File(newFile.getPath());
			boolean deleted = file.delete();
			
			filesDao.deleteByKey(id);
		}
		
		cursor.requery();
	}
}
