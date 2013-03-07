package shared.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * This class helps open, create, and upgrade the database file. Set to package
 * visibility for testing purposes.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context) {

		// calls the super constructor, requesting the default cursor factory.
		super(context, "local.sqlite", null, 1);
	}

	/**
	 * 
	 * Creates the underlying database with table name and column names taken
	 * from the NotePad class.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("db", "onCreate");
		
		db.execSQL("CREATE TABLE file (_ID INTEGER PRIMARY KEY, objectId TEXT UNIQUE, size INTEGER, name TEXT, data BLOB, createdAt INTEGER, updatedAt INTEGER)");
		
//		db.execSQL("CREATE TABLE " + NotePad.Notes.TABLE_NAME + " ("
//				+ NotePad.Notes._ID + " INTEGER PRIMARY KEY,"
//				+ NotePad.Notes.COLUMN_NAME_TITLE + " TEXT,"
//				+ NotePad.Notes.COLUMN_NAME_NOTE + " TEXT,"
//				+ NotePad.Notes.COLUMN_NAME_CREATE_DATE + " INTEGER,"
//				+ NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE + " INTEGER"
//				+ ");");
	}

	/**
	 * 
	 * Demonstrates that the provider must consider what happens when the
	 * underlying datastore is changed. In this sample, the database is upgraded
	 * the database by destroying the existing data. A real application should
	 * upgrade the database in place.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// Logs that the database is being upgraded
		Log.w("", "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");

		// Kills the table and existing data
		db.execSQL("DROP TABLE IF EXISTS notes");

		// Recreates the database with a new version
		onCreate(db);
	}
}
