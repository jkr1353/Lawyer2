package com.example.lawyerapp;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.support.v4.app.ListFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class LogFrag extends ListFragment {
	
	private DaoInstance daoinstance;
	private SQLiteDatabase db;
	private LogsDao logsDao;
	private Cursor cursor;
	private EditText eText, eHours, eNotes;
	private Button addNewHours, addNewExpense, addNewMileage, deleteLog;
	private Long parentID;
	private String deleteLogStr, doneLogStr;
	private float tempFloat = 0.0f;
	private TextView totalHours, totalExpense, totalMileage;
	private float num_of_hours = 0.0f;
	
	@Override
	public void onCreate(Bundle saved) 
	{
		super.onCreate(saved);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saved) {
		View v = inflater.inflate(R.layout.main, parent,false); 
		
		deleteLogStr = "Delete Log";
        doneLogStr = "Done";
		
		daoinstance = DaoInstance.getInstance(getActivity());
		
		db = daoinstance.getDb();
		
		totalHours = (TextView) v.findViewById(R.id.totalHours);
		totalExpense = (TextView) v.findViewById(R.id.totalExpenses);
		totalMileage = (TextView) v.findViewById(R.id.totalMileage);
		
		logsDao = daoinstance.getLogsDao();
		
		parentID = getActivity().getIntent().getExtras().getLong("id");
		
		
		totalHours.setText("Total Hours: " + calcTotalHours("Hours"));
		totalExpense.setText("Total Expenses: " + calcTotalHours("Expenses"));
		totalMileage.setText("Total Mileage: " + calcTotalHours("Mileage"));
		
		
		addNewHours = (Button) getActivity().findViewById(R.id.buttonNewHours);
		addNewExpense = (Button) getActivity().findViewById(R.id.buttonNewExpense);
		addNewMileage = (Button) getActivity().findViewById(R.id.buttonNewMileage);
		deleteLog = (Button) getActivity().findViewById(R.id.buttonDelete);
		
		addNewHours.setVisibility(View.VISIBLE);
		deleteLog.setVisibility(View.VISIBLE);
		
		
		deleteLog.setText(deleteLogStr);
		addNewHours.setText("New Hours");
		
		
		
		addNewExpense.setVisibility(View.VISIBLE);
		addNewMileage.setVisibility(View.VISIBLE);
		
		
		
		addNewExpense.setText("New Expense");
		addNewMileage.setText("New Mileage");
		
		final LayoutInflater lInflater = inflater;

        addNewHours.setOnClickListener(new View.OnClickListener() {
			
			@Override
			  public void onClick(View view) 
			{
				onClickMethod(0, lInflater);
			}		 
		});
        
        addNewExpense.setOnClickListener(new View.OnClickListener() {
			
			@Override
			  public void onClick(View view) 
			{
				onClickMethod(1, lInflater);
			}		 
		});
        
        addNewMileage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			  public void onClick(View view) 
			{
				onClickMethod(2, lInflater);
			}		 
		});
		
        deleteLog.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				if (deleteLog.getText() == deleteLogStr)
				{
					deleteLog.setText(doneLogStr);
					
					addNewHours.setVisibility(View.INVISIBLE);
					addNewExpense.setVisibility(View.INVISIBLE);
					addNewMileage.setVisibility(View.INVISIBLE);
				}
				else
				{
					deleteLog.setText(deleteLogStr);
					
					addNewHours.setVisibility(View.VISIBLE);
					addNewExpense.setVisibility(View.VISIBLE);
					addNewMileage.setVisibility(View.VISIBLE);
				}
			}
		});
        
		String textColumn = LogsDao.Properties.Name.columnName;
		
		String dateColumn = LogsDao.Properties.Date.columnName;
        String orderBy = dateColumn + " COLLATE LOCALIZED DESC";
		
        cursor = db.query(logsDao.getTablename(), null, "PARENT_ID IN " +
        		"(SELECT PARENT_ID FROM LOGS WHERE PARENT_ID = " + parentID.toString() + ")"
        		, null, null, null, orderBy);
        
        String[] from = {textColumn, LogsDao.Properties.LogType.columnName, LogsDao.Properties.LogDate.columnName};
        int[] to = { R.id.textView1, R.id.textView2, R.id.dateView };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item2, cursor, from,
                to);
        setListAdapter(adapter);
		
		return v; 
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) 
	{
		final Logs newlog = logsDao.queryBuilder().where(LogsDao.Properties.Id.eq(id)).unique();
		
		if (deleteLog.getText() == deleteLogStr)
		{
			// PUT THE ACTIVITY/FRAG FOR EACH TIME LOG HERE
			
			final long tempID = id;
			
			int tempType = 0;
			
			Log.i("CS499", newlog.getLogType());
			
			if (newlog.getLogType().equals("Hours"))
			{
				tempType = 0;
				
				Log.i("CS499", "This is an Hour Type");
			}
			else if (newlog.getLogType().equals("Expenses"))
			{
				tempType = 1;
				
				Log.i("CS499", "This is an Expense Type");
			}
			else if (newlog.getLogType().equals("Mileage"))
			{
				tempType = 2;
				
				Log.i("CS499", "This is an Mileage Type");
			}
			
			//Log.i("CS499", tempType + "");
			
			final int logType = tempType;
			
			//Log.i("CS499", logType + "");
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			  final View AlertView = View.inflate(getActivity(), R.layout.new_log_dialog, null);
					  
			  eText = (EditText) AlertView.findViewById(R.id.editTextName);
				eHours = (EditText) AlertView.findViewById(R.id.editTextValue);
				eNotes = (EditText) AlertView.findViewById(R.id.editTextNote);
				
				eText.setText(newlog.getName());
				eNotes.setText(newlog.getNotes());
				
				TextView tempTextView = (TextView) AlertView.findViewById(R.id.textViewName);
				
				switch (logType)
				{
				case 0:
					eHours.setText(newlog.getHours()+"");
					tempTextView.setText("Hours: ");
					break;
					
				case 1:
					eHours.setText(newlog.getExpenses()+"");
					tempTextView.setText("Expenses: ");
					break;
					
				case 2:
					eHours.setText(newlog.getMileage()+"");
					tempTextView.setText("Mileage: ");
					break;
				}
				
				if (eNotes.getText().toString().isEmpty())
				{
					eNotes.setHint("Enter Note");
				}
				
			  builder.setView(AlertView);
			  AlertDialog ad = builder.create();
			  ad.setTitle(newlog.getName());
			  ad.setButton(AlertDialog.BUTTON_POSITIVE, "Done",
					    new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) {
					        	
					        	float tempLogFloat;
					        	
					        	String noteText = eText.getText().toString();
						        eText.setText("");
						        
						        checkForNull(eHours);
						        tempLogFloat = tempFloat;
						        
						        String tempNotes = eNotes.getText().toString();
						        
						        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
						        String comment = "" + df.format(new Date());
						        
						        String newType = "";
						        
						        switch (logType)
						        {
						        case 0:
						        	newType = "Hours";
						        	
							        Logs log = new Logs(tempID, noteText, parentID, comment, new Date(), tempNotes, newType, tempLogFloat, null, null);
							        logsDao.insertOrReplace(log);
		
							        totalHours.setText("Total Hours: " + calcTotalHours(newType));
							        break;
							        
						        case 1:
						        	newType = "Expenses";
						        	
						        	Logs log1 = new Logs(tempID, noteText, parentID, comment, new Date(), tempNotes, newType, null, null, tempLogFloat);
							        logsDao.insertOrReplace(log1);
		
							        totalExpense.setText("Total Expenses: " + calcTotalHours(newType));
							        break;
						        	
						        case 2:
						        	newType = "Mileage";
						        	
						        	Logs log2 = new Logs(tempID, noteText, parentID, comment, new Date(), tempNotes, newType, null, tempLogFloat, null);
							        logsDao.insertOrReplace(log2);
							        
							        totalMileage.setText("Total Mileage: " + calcTotalHours(newType));
						        	break;
						        }
						        
						        cursor.requery();
					        }
					    });
			  ad.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
					    new DialogInterface.OnClickListener() 
			  			{
					        public void onClick(DialogInterface dialog, int which) 
					        {
					        	
					        }
					    });
			  
					ad.show();
		}
		else
		{
			logsDao.deleteByKey(id);
			
			if (newlog.getLogType().equals("Hours"))
			{
				totalHours.setText("Total Hours: " + calcTotalHours("Hours"));
			}
			else if (newlog.getLogType().equals("Expenses"))
			{
				totalExpense.setText("Total Expenses: " + calcTotalHours("Expenses"));
			}
			else if (newlog.getLogType().equals("Mileage"))
			{
				totalMileage.setText("Total Mileage: " + calcTotalHours("Mileage"));
			}
		}
		
		cursor.requery();
	}
	
	public void checkForNull(EditText tempText)
	{
		if (tempText.getText().toString().isEmpty() && !tempText.getText().equals(null))
        {
        	tempFloat = 0.0f;
        }
        else
        {
	        tempFloat = Float.parseFloat(tempText.getText().toString());
	        tempText.setText("");
        }
	}
	
	public float calcTotalHours(String inputString)
	{
		ArrayList<Logs> newList = (ArrayList<Logs>) logsDao.queryBuilder().where(LogsDao.Properties.ParentID.eq(parentID), LogsDao.Properties.LogType.eq(inputString)).list();
		
		num_of_hours = 0.0f;
		
		int type = 0;
		
		if (inputString == "Hours")
		{
			type = 0;
		}
		else if (inputString == "Expenses")
		{
			type = 1;
		}
		else if (inputString == "Mileage")
		{
			type = 2;
		}
		
		if (!newList.isEmpty())
		{	
			Log.i("CS499", "New List");
			
			switch (type)
			{
			case 0:
				for (Logs arrayLogs: newList)
				{
					if (arrayLogs.getHours() != null)
					{
						num_of_hours += arrayLogs.getHours();
					}
				}
				break;
				
			case 1:
				for (Logs arrayLogs: newList)
				{
					if (arrayLogs.getExpenses() != null)
					{
						num_of_hours += arrayLogs.getExpenses();
					}
				}
				break;
				
			case 2:
				for (Logs arrayLogs: newList)
				{
					if (arrayLogs.getMileage() != null)
					{
						num_of_hours += arrayLogs.getMileage();
					}
				}
				break;
			}
		}
		
		return num_of_hours;
	}
	
	private void onClickMethod(int logType, LayoutInflater lInflater)
	{
		  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		  
		  final int type = logType;
		  
		  final View AlertView = lInflater.inflate(R.layout.new_log_dialog, null);
		  
		  	eText = (EditText) AlertView.findViewById(R.id.editTextName);
		  	eNotes = (EditText) AlertView.findViewById(R.id.editTextNote);
		  	eHours = (EditText) AlertView.findViewById(R.id.editTextValue);
		
			TextView tempTextView = (TextView) AlertView.findViewById(R.id.textViewName);
			
			tempTextView.setText("");
			
			eText.setHint("Name");
			eNotes.setHint("Notes");
			
			switch (type)
			{
			case 0:
				eHours.setHint("Hours");
				break;
				
			case 1:
				eHours.setHint("Expenses");
				break;
				
			case 2:
				eHours.setHint("Mileage");
				break;
			}
			
		  builder.setView(AlertView);
		  AlertDialog ad = builder.create();
		  ad.setTitle("Create New Log");
		  ad.setButton(AlertDialog.BUTTON_POSITIVE, "Create Log",
				    new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) {
					         
					        float tempLogFloat;
				        	
				        	String noteText = eText.getText().toString();
					        eText.setText("");
					        
					        checkForNull(eHours);
					        tempLogFloat = tempFloat;
					        
					        String tempNotes = eNotes.getText().toString();
					        
					        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
					        String comment = "" + df.format(new Date());
					        
					        String newType = "";
					        
					        switch (type)
					        {
					        case 0:
					        	newType = "Hours";
					        	
						        Logs log = new Logs(null, noteText, parentID, comment, new Date(), tempNotes, newType, tempLogFloat, null, null);
						        logsDao.insert(log);
	
						        totalHours.setText("Total Hours: " + calcTotalHours(newType));
						        break;
						        
					        case 1:
					        	newType = "Expenses";
					        	
					        	Logs log1 = new Logs(null, noteText, parentID, comment, new Date(), tempNotes, newType, null, null, tempLogFloat);
						        logsDao.insert(log1);
	
						        totalExpense.setText("Total Expenses: " + calcTotalHours(newType));
						        break;
					        	
					        case 2:
					        	newType = "Mileage";
					        	
					        	Logs log2 = new Logs(null, noteText, parentID, comment, new Date(), tempNotes, newType, null, tempLogFloat, null);
						        logsDao.insert(log2);
						        
						        totalMileage.setText("Total Mileage: " + calcTotalHours(newType));
					        	break;
					        }
					        
					        cursor.requery();
				        }
				    });
		  ad.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
				    new DialogInterface.OnClickListener() 
		  			{
				        public void onClick(DialogInterface dialog, int which) 
				        {
				        	
				        }
				    });
		  
				ad.show();
	  }
	}