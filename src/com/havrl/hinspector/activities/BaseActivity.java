package com.havrl.hinspector.activities;

import com.havrl.hinspector.db.DatabaseHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends Activity {

    // Make strings for logging
	// TODO: assign the name of the child class
	private final String TAG = this.getClass().getSimpleName();
	
	private DatabaseHelper mDb;
	
	/*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.copy_db:
            	String dbName = getDbHelper().getDbName();
            	if (!TextUtils.isEmpty(dbName)){
            		FileUtils.copyAppFileToSd(this, "databases/" + dbName, "Copied-" + dbName);
            		Toast.makeText(this, "Database was copied to " + "Copied-" + dbName, Toast.LENGTH_LONG).show();
            	}else {
            		Toast.makeText(this, "Error: db name is null", Toast.LENGTH_LONG).show();
            	}
                return true;            
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
	
	/**
	 * Creates a dialog and shows it
	 * 
	 * @param exception
	 *            The exception to show in the dialog
	 * @param title
	 *            The dialog title
	 */
	protected void createAndShowDialog(Exception exception, String title) {
		createAndShowDialog(exception.toString(), title);
	}

	/**
	 * Creates a dialog and shows it
	 * 
	 * @param message
	 *            The dialog message
	 * @param title
	 *            The dialog title
	 */
	protected void createAndShowDialog(String message, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(message);
		builder.setTitle(title);
		builder.create().show();
	}
    
    protected DatabaseHelper getDbHelper(){
		if (mDb == null){ // ensure the database helper is initialized
			mDb = new DatabaseHelper(this);
		}
		
		return mDb;
	}
	
	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);	
		
		// Notification that the activity is creating
		Log.i(TAG, "onCreate");
	}


	@Override
	protected void onRestart() {
		super.onRestart();
		// Notification that the activity will be started
		Log.i(TAG, "onRestart");
	}


	@Override
	protected void onStart() {
		super.onStart();
		// Notification that the activity is starting
		Log.i(TAG, "onStart");
	}


	@Override
	protected void onResume() {
		super.onResume();
		// Notification that the activity will interact with the user
		Log.i(TAG, "onResume");
	}


	protected void onPause() {
		super.onPause();
		// Notification that the activity will stop interacting with the user
		Log.i(TAG, "onPause" + (isFinishing() ? " Finishing" : ""));
	}


	@Override
	protected void onStop() {
		super.onStop();
		// Notification that the activity is no longer visible
		Log.i(TAG, "onStop");
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Notification the activity will be destroyed
		Log.i(TAG,
				"onDestroy "
						// Log which, if any, configuration changed
						+ Integer.toString(getChangingConfigurations(), 16));
		
		if (this.mDb != null){
			this.mDb.close();
			this.mDb = null;
		}
	}


	// ////////////////////////////////////////////////////////////////////////////
	// Called during the lifecycle, when instance state should be saved/restored
	// ////////////////////////////////////////////////////////////////////////////


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// Save instance-specific state			
		Log.i(TAG, "onSaveInstanceState");


	}


	@Override
	public Object onRetainNonConfigurationInstance() {
		Log.i(TAG, "onRetainNonConfigurationInstance");
		return new Integer(0);
	}


	@Override
	protected void onRestoreInstanceState(Bundle savedState) {
		super.onRestoreInstanceState(savedState);
		
		Log.i(TAG, "onRestoreInstanceState");
	}


	// ////////////////////////////////////////////////////////////////////////////
	// These are the minor lifecycle methods, you probably won't need these
	// ////////////////////////////////////////////////////////////////////////////


	@Override
	protected void onPostCreate(Bundle savedState) {
		super.onPostCreate(savedState);
		
		Log.i(TAG, "onPostCreate");
	}


	@Override
	protected void onPostResume() {
		super.onPostResume();
		Log.i(TAG, "onPostResume");
	}


	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		Log.i(TAG, "onUserLeaveHint");
	}


}
