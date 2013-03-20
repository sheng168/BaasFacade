package shared.baas.demo.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * This class helps open, create, and upgrade the database file. Set to
 * package visibility for testing purposes.
 */
class DatabaseHelper extends SQLiteOpenHelper {
	static final String TAG = DatabaseHelper.class
			.getSimpleName();
	/**
	 * The database that the provider uses as its underlying data store
	 */
	static final String DATABASE_NAME = "checklist.sqlite";

	/**
	 * The database version
	 */
	static final int DATABASE_VERSION = 5;


	DatabaseHelper(Context context) {

		// calls the super constructor, requesting the default cursor
		// factory.
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * 
	 * Creates the underlying database with table name and column names
	 * taken from the NotePad class.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE \"Todo\" (" +
				"\"_id\" INTEGER PRIMARY KEY  NOT NULL , " +
				"\"objectId\" VARCHAR, " +
				
				"\"content\" VARCHAR, " +
				"\"orderBy\" INTEGER, " +
				"\"done\" BOOL, " +
				
				"\"sync_delete\" BOOL, " +
				"\"sync_pending\" BOOL, " +
				
				"\"createdAt\" DATETIME, " +
				"\"updatedAt\" DATETIME, " +
				"\"status\" VARCHAR)");
	}

	/**
	 * 
	 * Demonstrates that the provider must consider what happens when the
	 * underlying datastore is changed. In this sample, the database is
	 * upgraded the database by destroying the existing data. A real
	 * application should upgrade the database in place.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// Logs that the database is being upgraded
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");

		// Kills the table and existing data
		db.execSQL("DROP TABLE IF EXISTS notes");
		db.execSQL("DROP TABLE IF EXISTS Todo");
		// Recreates the database with a new version
		onCreate(db);
	}
}