package com.havrl.hinspector.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;


/**
 * Base class for database helper that allows creating and upgrading SQLite database from SQL scripts 
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	/** The name of the database file on the file system */
    private static final String DATABASE_NAME = "inspdb.sqlite";
    /** The version of the database that this class understands. */
    private static final int DATABASE_VERSION = 1;
    /** Keep track of context so that we can load SQL from string resources */
    private final Context mContext;

	private final List<String> mScripts;
	private final static String TAG = "SqlFileDatabaseHelper";
	
	public DatabaseHelper(Context context) {		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);		
		
		mContext = context;
		
		mScripts = new ArrayList<String>();
		mScripts.add("inspdb-v1.sql");		
	}
	
	/** Called when it is time to create the database */
	@Override
	public void onCreate(SQLiteDatabase db) {
		List<String> scripts = getCreateScripts();
		executeDatabaseScripts(db, scripts);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		List<String> scripts = getUpgradeScripts(oldVersion, newVersion);
		executeDatabaseScripts(db, scripts);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
	    super.onOpen(db);
	    if (!db.isReadOnly()) {
	        // Enable foreign key constraints
	        db.execSQL("PRAGMA foreign_keys=ON;");
	    }
	}
	
	/** Returns the name of the created database */
	public String getDbName() {
		return DATABASE_NAME;
	}

	private void executeDatabaseScripts(SQLiteDatabase db, List<String> scripts) {
		if (scripts != null && scripts.size() > 0) {
			for (String script : scripts) {
				db.execSQL(script);
			}
		}
	}

	protected List<String> getCreateScripts() {

		List<String> scripts = new ArrayList<String>();
		for (String scriptPath : mScripts) {
			try {
				String script = getScriptFileContent(scriptPath);
				scripts.addAll(tokenizeSql(script));
			} catch (IOException e) {
				Log.e(TAG, "Create script is not found: " + mScripts.get(0));
				e.printStackTrace();
			}
		}

		return scripts;
	}

	protected List<String> getUpgradeScripts(int oldVersion, int newVersion) {

		List<String> scripts = new ArrayList<String>();
		for (int i = oldVersion, j = newVersion; i < j; ++i) {
			String script;
			try {
				script = getScriptFileContent(mScripts.get(i));
				scripts.addAll(tokenizeSql(script));				
			} catch (IOException e) {
				Log.e(TAG, "Upgrade script is not found: " + mScripts.get(i));
				e.printStackTrace();
			}
		}

		return scripts;
	}
	
	/**
	 * Breaks the SQL script into executable queries 
	 * @param sql script
	 * @return list of strings
	 */
	private List<String> tokenizeSql(String sql) {
		List<String> scripts = new ArrayList<String>();
		String[] sqlQueries = TextUtils.split(sql, ";\n");
		for (String sqlQuery : sqlQueries) {
			sqlQuery = sqlQuery.trim();			
			if (!TextUtils.isEmpty(sqlQuery)) {
				scripts.add(sqlQuery);
			}
		}
		
		return scripts;
	}

	private String getScriptFileContent(String scriptFile) throws IOException {
		String content = "";
		InputStream is = mContext.getAssets().open(scriptFile);

		if (is != null) {
			try {
				Writer writer = new StringWriter();

				char[] buffer = new char[1024];
				try {
					Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8096);
					int n;
					while ((n = reader.read(buffer)) != -1) {
						writer.write(buffer, 0, n);
					}
				} finally {
					is.close();
				}
				content = writer.toString();
			} finally {
				is.close();
			}
		}

		return content;
	}
}
