package com.example.lawyerapp;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.lawyerapp.DaoMaster.DevOpenHelper;

public class DaoInstance extends Application 
{
    private static DaoInstance instance;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CasesDao caseDao;
    private LogsDao logsDao;
    private FilesDao filesDao;
    private CaseContactsDao caseContactsDao;
    private SQLiteDatabase db;
    
    private DaoInstance(Context c) {
    	
    	DevOpenHelper helper = new DaoMaster.DevOpenHelper(c, "lawyers9-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        caseDao = daoSession.getCasesDao();
        logsDao = daoSession.getLogsDao();
        filesDao = daoSession.getFilesDao();
        setCaseContactsDao(daoSession.getCaseContactsDao());
        
    }

    public static DaoInstance getInstance(Context c) 
    {
    	if (instance == null)
    	{
    		instance = new DaoInstance(c.getApplicationContext());
    	}
    	
        return instance;
    }

	public DaoMaster getDaoMaster() {
		return daoMaster;
	}

	public void setDaoMaster(DaoMaster daoMaster) {
		this.daoMaster = daoMaster;
	}

	public DaoSession getDaoSession() {
		return daoSession;
	}

	public void setDaoSession(DaoSession daoSession) {
		this.daoSession = daoSession;
	}

	public CasesDao getCaseDao() {
		return caseDao;
	}

	public void setCaseDao(CasesDao caseDao) {
		this.caseDao = caseDao;
	}

	public LogsDao getLogsDao() {
		return logsDao;
	}

	public void setLogsDao(LogsDao logsDao) {
		this.logsDao = logsDao;
	}

	public SQLiteDatabase getDb() {
		return db;
	}

	public void setDb(SQLiteDatabase db) {
		this.db = db;
	}

	public FilesDao getFilesDao() {
		return filesDao;
	}

	public void setFilesDao(FilesDao filesDao) {
		this.filesDao = filesDao;
	}

	public CaseContactsDao getCaseContactsDao() {
		return caseContactsDao;
	}

	public void setCaseContactsDao(CaseContactsDao caseContactsDao) {
		this.caseContactsDao = caseContactsDao;
	}

}
