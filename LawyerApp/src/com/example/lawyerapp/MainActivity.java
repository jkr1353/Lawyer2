package com.example.lawyerapp;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

public class MainActivity extends ListActivity{

	private SQLiteDatabase db;

    private DaoInstance daoinstance;
    
    private CasesDao caseDao;
    
    private LogsDao logsDao;
    
    private Cursor cursor;
    
    private Button addNewCase, deleteCase;
    
    private String noteText, caseType;
    
    private String deleteCaseStr, doneCaseStr;
    
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        deleteCaseStr = "Delete Case";
        doneCaseStr = "Done";
        
        daoinstance = DaoInstance.getInstance(this);
        
        setContentView(R.layout.all_cases);
        
        db = daoinstance.getDb();
        caseDao = daoinstance.getCaseDao();
        logsDao = daoinstance.getLogsDao();
        
        addNewCase = (Button) findViewById(R.id.buttonAdd);
        deleteCase = (Button) findViewById(R.id.buttonDelete);
        
        final int buttonColor;
		
		buttonColor = getResources().getColor(R.color.colorScheme2);
		
        addNewCase.setBackgroundColor(buttonColor);
        deleteCase.setBackgroundColor(buttonColor);
        
        deleteCase.setText("Delete Case");

        addNewCase.setOnClickListener(new View.OnClickListener() {
			
			@Override
			  public void onClick(View view) {
				  AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				  final LayoutInflater inflater = getLayoutInflater();

				  final View AlertView = inflater.inflate(R.layout.new_case_dialog, null);
				  builder.setView(AlertView);
				  AlertDialog ad = builder.create();
				  ad.setTitle("Create New Case");
				  ad.setButton(AlertDialog.BUTTON_POSITIVE, "Create Case",
						    new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) {
						        	
						        	EditText eText = (EditText) AlertView.findViewById(R.id.eTextNote);
						            EditText eType = (EditText) AlertView.findViewById(R.id.eTextType);
						            
						            noteText = eText.getText().toString();
						            caseType = eType.getText().toString();
						        	
						            final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
						            String comment = "" + df.format(new Date());
						            
						        	Cases newCase = new Cases(null, noteText, caseType, comment, new Date());

						        	caseDao.insert(newCase);
						            Log.d("DaoExample", "Inserted new note, ID: " + newCase.getId());
						            
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
		});
        
        deleteCase.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				if (deleteCase.getText() == deleteCaseStr)
				{
					deleteCase.setText(doneCaseStr);
				}
				else
				{
					deleteCase.setText(deleteCaseStr);
				}
				
			}
		});
        
        String textColumn = CasesDao.Properties.Name.columnName;
        
        String dateColumn = CasesDao.Properties.Date.columnName;
        String orderBy = dateColumn + " COLLATE LOCALIZED DESC";
        
        cursor = db.query(caseDao.getTablename(), caseDao.getAllColumns(), null, null, null, null, orderBy);
        String[] from = {textColumn, CasesDao.Properties.Casetype.columnName, CasesDao.Properties.CaseDate.columnName};
        int[] to = { R.id.textView1, R.id.textView2, R.id.dateView };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_item2, cursor, from,
                to);
        setListAdapter(adapter);
        
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) 
    {
        if (deleteCase.getText() == deleteCaseStr)
        {
	    	Intent newIntent = new Intent(this, CaseActivity.class);
	    	
	    	Cases newCase = caseDao.queryBuilder().where(CasesDao.Properties.Id.eq(id)).unique();
	    	String newString = newCase.getName();
	    	newIntent.putExtra("name", newString);
	    	newIntent.putExtra("id", id);
	    	
	    	String newType = newCase.getCasetype();
	    	final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
	        String comment = "" + df.format(new Date());
	        
	    	Cases newCase2 = new Cases(id, newString, newType, comment, new Date());
	    	caseDao.insertOrReplace(newCase2);
	    	
	    	startActivity(newIntent);
        }
        else
        {
        	ArrayList<Logs> newLogs = (ArrayList<Logs>) logsDao.queryBuilder().where(LogsDao.Properties.ParentID.eq(id)).list();
        	
        	if (newLogs != null)
        	{
	        	for (Logs newLog : newLogs)
	        	{
	        		logsDao.deleteByKey(newLog.getId());
	        	}
        	}
        	
        	caseDao.deleteByKey(id);
        	
        	cursor.requery();
        }
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	
    	cursor.requery();
    }

}
