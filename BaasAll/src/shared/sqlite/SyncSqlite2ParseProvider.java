/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package shared.sqlite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.DataObjectFactory;
import shared.baas.keyvalue.DataObjectQuery;
import shared.baas.keyvalue.parse.DataObjectFactoryParse;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

/**
 * Provides access to a database of notes. Each note has a title, the note
 * itself, a creation date and a modified data.
 */
public abstract class SyncSqlite2ParseProvider extends ContentProvider //implements PipeDataWriter<Cursor> 
{
	
	private static final String FIELD_SYNC_DELETE = "sync_delete";

	private static final String FIELD_SYNC_PENDING = "sync_pending";

	private static final String DONE_FIELD = "done";

	private static final String CONTENT_FIELD = "content";

	private static final String _ID = "_id";

	private static final String FIELD_OBJECT_ID = DataObject.OBJECT_ID;

	private static final String DELETE_STATE = "delete";

	private static final String FIELD_CREATED_AT = "createdAt";

	private static final String FIELD_UPDATED_AT = "updatedAt";

	private static final String SYNC_STATE = "sync";

//	private static final String FIELD_STATUS = "sync_status";

	org.slf4j.Logger log = org.slf4j.LoggerFactory
			.getLogger(SyncSqlite2ParseProvider.class);

	// Used for debugging and logging
	static final String TAG = SyncSqlite2ParseProvider.class
			.getSimpleName();

	/**
	 * A projection map used to select columns from the database
	 */
	private HashMap<String, String> sProjectionMap;

	/**
	 * A projection map used to select columns from the database
	 */
	private HashMap<String, String> sLiveFolderProjectionMap;

	/**
	 * Standard projection for the interesting columns of a normal note.
	 */
	private static final String[] READ_NOTE_PROJECTION = new String[] {
			Generic.Table._ID, // Projection position 0, the note's id
			Generic.Table.COLUMN_NAME_NOTE, // Projection position 1, the note's
											// content
			Generic.Table.COLUMN_NAME_TITLE, // Projection position 2, the
												// note's title
	};
	private static final int READ_NOTE_NOTE_INDEX = 1;
	private static final int READ_NOTE_TITLE_INDEX = 2;

	/*
	 * Constants used by the Uri matcher to choose an action based on the
	 * pattern of the incoming URI
	 */
	// The incoming URI matches the Notes URI pattern
	private static final int TABLE = 1;

	// The incoming URI matches the Note ID URI pattern
	private static final int TABLE_ID = 2;

	// The incoming URI matches the Live Folder URI pattern
	private static final int LIVE_FOLDER = 3;
	private static final int GENERIC = 4;

	private static final int ID_PATH_POSITION = 1;

	/**
	 * A UriMatcher instance
	 */
	private final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	// Handle to a new DatabaseHelper.
	private SQLiteOpenHelper mOpenHelper;

	/**
	 * A block that instantiates and sets static objects
	 */
	void init() {

		/*
		 * Creates and initializes the URI matcher
		 */
		// Create a new instance
		

		// Add a pattern that routes URIs terminated with "notes" to a NOTES
		// operation
		sUriMatcher.addURI(authority(), "*", TABLE);

		// Add a pattern that routes URIs terminated with "notes" plus an
		// integer
		// to a note ID operation
		sUriMatcher.addURI(authority(), "*/#", TABLE_ID);

		// Add a pattern that routes URIs terminated with live_folders/notes to
		// a
		// live folder operation
		sUriMatcher.addURI(authority(), "live_folders/*",
				LIVE_FOLDER);
		// sUriMatcher.addURI(NotePad.AUTHORITY, "*", GENERIC);

		/*
		 * Creates and initializes a projection map that returns all columns
		 */

		// Creates a new projection map instance. The map returns a column name
		// given a string. The two are usually equal.
		sProjectionMap = new HashMap<String, String>();

		// Maps the string "_ID" to the column name "_ID"
		sProjectionMap.put(Generic.Table._ID, Generic.Table._ID);

		// Maps "title" to "title"
		sProjectionMap.put(Generic.Table.COLUMN_NAME_TITLE,
				Generic.Table.COLUMN_NAME_TITLE);

		// Maps "note" to "note"
		sProjectionMap.put(Generic.Table.COLUMN_NAME_NOTE,
				Generic.Table.COLUMN_NAME_NOTE);

		// Maps "created" to "created"
		sProjectionMap.put(Generic.Table.COLUMN_NAME_CREATE_DATE,
				Generic.Table.COLUMN_NAME_CREATE_DATE);

		// Maps "modified" to "modified"
		sProjectionMap.put(Generic.Table.COLUMN_NAME_MODIFICATION_DATE,
				Generic.Table.COLUMN_NAME_MODIFICATION_DATE);

		/*
		 * Creates an initializes a projection map for handling Live Folders
		 */

		// Creates a new projection map instance
		sLiveFolderProjectionMap = new HashMap<String, String>();

//		// Maps "_ID" to "_ID AS _ID" for a live folder
//		sLiveFolderProjectionMap.put(LiveFolders._ID, Generic.Table._ID
//				+ " AS " + LiveFolders._ID);
//
//		// Maps "NAME" to "title AS NAME"
//		sLiveFolderProjectionMap.put(LiveFolders.NAME,
//				Generic.Table.COLUMN_NAME_TITLE + " AS " + LiveFolders.NAME);
	}

	protected abstract SQLiteOpenHelper newHelper();
	protected abstract String authority();

	/**
	 * 
	 * Initializes the provider by creating a new DatabaseHelper. onCreate() is
	 * called automatically when Android creates the provider in response to a
	 * resolver request from a client.
	 */
	@Override
	public boolean onCreate() {
		log.debug("onCreate");

		init();
		// Creates a new helper object. Note that the database itself isn't
		// opened until
		// something tries to access it, and it's only created if it doesn't
		// already exist.
		mOpenHelper = newHelper();
		
		// Assumes that any failures will be reported by a thrown exception.
		return true;
	}

	
	/**
	 * This method is called when a client calls
	 * {@link android.content.ContentResolver#query(Uri, String[], String, String[], String)}
	 * . Queries the database and returns a cursor containing the results.
	 * 
	 * @return A cursor containing the results of the query. The cursor exists
	 *         but is empty if the query returns no results or an exception
	 *         occurs.
	 * @throws IllegalArgumentException
	 *             if the incoming URI pattern is invalid.
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		final String table = uri.getPathSegments().get(0);
		log.debug("table {}", table);

		// Constructs a new query builder and sets its table name
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(table);

		/**
		 * Choose the projection and adjust the "where" clause based on URI
		 * pattern-matching.
		 */
		switch (sUriMatcher.match(uri)) {
		// If the incoming URI is for notes, chooses the Notes projection
		case TABLE:
//			qb.setProjectionMap(sProjectionMap);
			break;

		/*
		 * If the incoming URI is for a single note identified by its ID,
		 * chooses the note ID projection, and appends "_ID = <noteID>" to the
		 * where clause, so that it selects that single note
		 */
		case TABLE_ID:
//			qb.setProjectionMap(sProjectionMap);
			qb.appendWhere(_ID + // the name of the ID column
					"=" +
					// the position of the note ID itself in the incoming URI
					uri.getPathSegments().get(
							ID_PATH_POSITION));
			break;

		case LIVE_FOLDER:
			// If the incoming URI is from a live folder, chooses the live
			// folder projection.
//			qb.setProjectionMap(sLiveFolderProjectionMap);
			break;

		default:
			// If the URI doesn't match any of the known patterns, throw an
			// exception.
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		String orderBy;
		// If no sort order is specified, uses the default
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = Generic.Table.DEFAULT_SORT_ORDER;
		} else {
			// otherwise, uses the incoming sort order
			orderBy = sortOrder;
		}

		// Opens the database object in "read" mode, since no writes need to be
		// done.
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();

		/*
		 * Performs the query. If no problems occur trying to read the database,
		 * then a Cursor object is returned; otherwise, the cursor variable
		 * contains null. If no records were selected, then the Cursor object is
		 * empty, and Cursor.getCount() returns 0.
		 */
		Cursor c = qb.query(db, // The database to query
				projection, // The columns to return from the query
				selection, // The columns for the where clause
				selectionArgs, // The values for the where clause
				null, // don't group the rows
				null, // don't filter by row groups
				orderBy // The sort order
				);

		// Tells the Cursor what URI to watch, so it knows when its source data
		// changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	/**
	 * This is called when a client calls
	 * {@link android.content.ContentResolver#getType(Uri)}. Returns the MIME
	 * data type of the URI given as a parameter.
	 * 
	 * @param uri
	 *            The URI whose MIME type is desired.
	 * @return The MIME type of the URI.
	 * @throws IllegalArgumentException
	 *             if the incoming URI pattern is invalid.
	 */
	@Override
	public String getType(Uri uri) {

		/**
		 * Chooses the MIME type based on the incoming URI pattern
		 */
		switch (sUriMatcher.match(uri)) {

		// If the pattern is for notes or live folders, returns the general
		// content type.
		case TABLE:
		case LIVE_FOLDER:
			return Generic.Table.CONTENT_TYPE;

			// If the pattern is for note IDs, returns the note ID content type.
		case TABLE_ID:
			return Generic.Table.CONTENT_ITEM_TYPE;

			// If the URI pattern doesn't match any permitted patterns, throws
			// an exception.
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	/**
	 * This describes the MIME types that are supported for opening a note URI
	 * as a stream.
	 */
//	static ClipDescription NOTE_STREAM_TYPES = new ClipDescription(null,
//			new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN });

	/**
	 * Returns the types of available data streams. URIs to specific notes are
	 * supported. The application can convert such a note to a plain text
	 * stream.
	 * 
	 * @param uri
	 *            the URI to analyze
	 * @param mimeTypeFilter
	 *            The MIME type to check for. This method only returns a data
	 *            stream type for MIME types that match the filter. Currently,
	 *            only text/plain MIME types match.
	 * @return a data stream MIME type. Currently, only text/plan is returned.
	 * @throws IllegalArgumentException
	 *             if the URI pattern doesn't match any supported patterns.
	 */

//	@Override
//	public String[] getStreamTypes(Uri uri, String mimeTypeFilter) {
//		/**
//		 * Chooses the data stream type based on the incoming URI pattern.
//		 */
//		switch (sUriMatcher.match(uri)) {
//
//		// If the pattern is for notes or live folders, return null. Data
//		// streams are not
//		// supported for this type of URI.
//		case TABLE:
//		case LIVE_FOLDER:
//			return null;
//
//			// If the pattern is for note IDs and the MIME filter is text/plain,
//			// then return
//			// text/plain
////		case TABLE_ID:
////			return NOTE_STREAM_TYPES.filterMimeTypes(mimeTypeFilter);
//
//			// If the URI pattern doesn't match any permitted patterns, throws
//			// an exception.
//		default:
//			throw new IllegalArgumentException("Unknown URI " + uri);
//		}
//	}

	/**
	 * Returns a stream of data for each supported stream type. This method does
	 * a query on the incoming URI, then uses
	 * {@link android.content.ContentProvider#openPipeHelper(Uri, String, Bundle, Object, PipeDataWriter)}
	 * to start another thread in which to convert the data into a stream.
	 * 
	 * @param uri
	 *            The URI pattern that points to the data stream
	 * @param mimeTypeFilter
	 *            A String containing a MIME type. This method tries to get a
	 *            stream of data with this MIME type.
	 * @param opts
	 *            Additional options supplied by the caller. Can be interpreted
	 *            as desired by the content provider.
	 * @return AssetFileDescriptor A handle to the file.
	 * @throws FileNotFoundException
	 *             if there is no file associated with the incoming URI.
	 *//*
	@Override
	public AssetFileDescriptor openTypedAssetFile(Uri uri,
			String mimeTypeFilter, Bundle opts) throws FileNotFoundException {

		// Checks to see if the MIME type filter matches a supported MIME type.
		String[] mimeTypes = getStreamTypes(uri, mimeTypeFilter);

		// If the MIME type is supported
		if (mimeTypes != null) {

			// Retrieves the note for this URI. Uses the query method defined
			// for this provider,
			// rather than using the database query method.
			Cursor c = query(uri, // The URI of a note
					READ_NOTE_PROJECTION, // Gets a projection containing the
											// note's ID, title,
											// and contents
					null, // No WHERE clause, get all matching records
					null, // Since there is no WHERE clause, no selection
							// criteria
					null // Use the default sort order (modification date,
							// descending
			);

			// If the query fails or the cursor is empty, stop
			if (c == null || !c.moveToFirst()) {

				// If the cursor is empty, simply close the cursor and return
				if (c != null) {
					c.close();
				}

				// If the cursor is null, throw an exception
				throw new FileNotFoundException("Unable to query " + uri);
			}

			// Start a new thread that pipes the stream data back to the caller.
			return new AssetFileDescriptor(openPipeHelper(uri, mimeTypes[0],
					opts, c, this), 0, AssetFileDescriptor.UNKNOWN_LENGTH);
		}

		// If the MIME type is not supported, return a read-only handle to the
		// file.
		return super.openTypedAssetFile(uri, mimeTypeFilter, opts);
	}
*/
	/**
	 * Implementation of {@link android.content.ContentProvider.PipeDataWriter}
	 * to perform the actual work of converting the data in one of cursors to a
	 * stream of data for the client to read.
	 *//*
	@Override
	public void writeDataToPipe(ParcelFileDescriptor output, Uri uri,
			String mimeType, Bundle opts, Cursor c) {
		// We currently only support conversion-to-text from a single note
		// entry,
		// so no need for cursor data type checking here.
		FileOutputStream fout = new FileOutputStream(output.getFileDescriptor());
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new OutputStreamWriter(fout, "UTF-8"));
			pw.println(c.getString(READ_NOTE_TITLE_INDEX));
			pw.println("");
			pw.println(c.getString(READ_NOTE_NOTE_INDEX));
		} catch (UnsupportedEncodingException e) {
			Log.w(TAG, "Ooops", e);
		} finally {
			c.close();
			if (pw != null) {
				pw.flush();
			}
			try {
				fout.close();
			} catch (IOException e) {
			}
		}
	}
*/
	/**
	 * This is called when a client calls
	 * {@link android.content.ContentResolver#insert(Uri, ContentValues)}.
	 * Inserts a new row into the database. This method sets up default values
	 * for any columns that are not included in the incoming map. If rows were
	 * inserted, then listeners are notified of the change.
	 * 
	 * @return The row ID of the inserted row.
	 * @throws SQLException
	 *             if the insertion fails.
	 */
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		log.debug("insert {}", uri);

		// Validates the incoming URI. Only the full provider URI is allowed for
		// inserts.
		final int match = sUriMatcher.match(uri);
		if (match == TABLE || match == GENERIC) {
		} else {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		final String table = uri.getPathSegments().get(0);
		log.debug("table {}", table);

		// A map to hold the new record's values.
		final ContentValues values;

		// If the incoming values map is not null, uses it for the new values.
		if (initialValues != null) {
			values = new ContentValues(initialValues);

		} else {
			// Otherwise, create a new value map
			values = new ContentValues();
		}

		values.put(FIELD_SYNC_PENDING, true);
		values.put(FIELD_SYNC_DELETE, false);
		
		// Gets the current system time in milliseconds
		Long now = Long.valueOf(System.currentTimeMillis());

		// If the values map doesn't contain the creation date, sets the value
		// to the current time.
		if (values.containsKey(Generic.Table.COLUMN_NAME_CREATE_DATE) == false) {
			values.put(Generic.Table.COLUMN_NAME_CREATE_DATE, now);
		}

		// If the values map doesn't contain the modification date, sets the
		// value to the current
		// time.
		if (values.containsKey(Generic.Table.COLUMN_NAME_MODIFICATION_DATE) == false) {
			values.put(Generic.Table.COLUMN_NAME_MODIFICATION_DATE, now);
		}

		// // If the values map doesn't contain a title, sets the value to the
		// default title.
		// if (values.containsKey(NotePad.Notes.COLUMN_NAME_TITLE) == false) {
		// Resources r = Resources.getSystem();
		// values.put(NotePad.Notes.COLUMN_NAME_TITLE,
		// r.getString(android.R.string.untitled));
		// }
		//
		// // If the values map doesn't contain note text, sets the value to an
		// empty string.
		// if (values.containsKey(NotePad.Notes.COLUMN_NAME_NOTE) == false) {
		// values.put(NotePad.Notes.COLUMN_NAME_NOTE, "");
		// }

//		values.put(FIELD_STATUS, "insert");
		
		// Opens the database object in "write" mode.
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

		// Performs the insert and returns the ID of the new note.
		final long rowId = db.insert(table, // The table to insert into.
				null, // A hack, SQLite sets this column value to null
						// if values is empty.
				values // A map of column names, and the values to insert
						// into the columns.
				);

		// If the insert succeeded, the row ID exists.
		if (rowId > 0) {
			// Creates a URI with the note ID pattern and the new row ID
			// appended to it.
			final Uri noteUri = ContentUris.withAppendedId(uri, rowId);

			// Notifies observers registered against this provider that the data
			// changed.
			getContext().getContentResolver().notifyChange(noteUri, null);
			
			syncAll(noteUri, table, db);
//			syncInsert(table, values, db, rowId, noteUri);
			
			return noteUri;
		}

		// If the insert didn't succeed, then the rowID is <= 0. Throws an
		// exception.
		throw new SQLException("Failed to insert row into " + uri);
	}

	protected void syncInsert(final String table, final ContentValues values,
			final SQLiteDatabase db, final long rowId, final Uri noteUri) {
		final List<String> filter = Arrays.asList(SyncSqlite2ParseProvider.FIELD_OBJECT_ID, SyncSqlite2ParseProvider.FIELD_CREATED_AT, SyncSqlite2ParseProvider.FIELD_UPDATED_AT);
		final ParseObject po = new ParseObject(table);
		for (Entry<String, Object> entry: values.valueSet()) {
			if (! filter.contains(entry.getKey()))
				po.put(entry.getKey(), entry.getValue());
		}
//			for (String key: values.keySet()) {
//				if (! filter.contains(key))
//					po.put(key, values.get(key));
//			}
		po.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
//						for (String key : po.keySet()) {
//							values.put(key, po.get(key));
//						}
					values.put(SyncSqlite2ParseProvider.FIELD_OBJECT_ID, po.getObjectId());
					values.put(SyncSqlite2ParseProvider.FIELD_CREATED_AT, po.getCreatedAt().getTime());
					values.put(SyncSqlite2ParseProvider.FIELD_UPDATED_AT, po.getUpdatedAt().getTime());
//					values.put(FIELD_STATUS, GenericSqliteProvider.SYNC_STATE);
					
					db.update(table, values, "_id = "+rowId, null);
					getContext().getContentResolver().notifyChange(noteUri, null);

				} else {
					log.error("", e);
				}
			}
		});
	}

	/**
	 * This is called when a client calls
	 * {@link android.content.ContentResolver#delete(Uri, String, String[])}.
	 * Deletes records from the database. If the incoming URI matches the note
	 * ID URI pattern, this method deletes the one record specified by the ID in
	 * the URI. Otherwise, it deletes a a set of records. The record or records
	 * must also match the input selection criteria specified by where and
	 * whereArgs.
	 * 
	 * If rows were deleted, then listeners are notified of the change.
	 * 
	 * @return If a "where" clause is used, the number of rows affected is
	 *         returned, otherwise 0 is returned. To delete all rows and get a
	 *         row count, use "1" as the where clause.
	 * @throws IllegalArgumentException
	 *             if the incoming URI pattern is invalid.
	 */
	@Override
	public int delete(final Uri uri, String where, final String[] whereArgs) {
		final String table = uri.getPathSegments().get(0);
		log.debug("table {}", table);

		// Opens the database object in "write" mode.
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		String finalWhere;

		int count;

		// Does the delete based on the incoming URI pattern.
		switch (sUriMatcher.match(uri)) {

		// If the incoming pattern matches the general pattern for notes, does a
		// delete
		// based on the incoming "where" columns and arguments.
		case TABLE:
			count = db.delete(table, // The database table name
					where, // The incoming where clause column names
					whereArgs // The incoming where clause values
					);
			break;

		// If the incoming URI matches a single note ID, does the delete based
		// on the
		// incoming data, but modifies the where clause to restrict it to the
		// particular note ID.
		case TABLE_ID:
			/*
			 * Starts a final WHERE clause by restricting it to the desired note
			 * ID.
			 */
			finalWhere = _ID + // The ID column name
					" = " + // test for equality
					uri.getPathSegments(). // the incoming note ID
							get(ID_PATH_POSITION);

			// If there were additional selection criteria, append them to the
			// final
			// WHERE clause
			if (where != null) {
				finalWhere = finalWhere + " AND " + where;
			}

			final ContentValues values = new ContentValues();
//			values.put(FIELD_STATUS, GenericSqliteProvider.DELETE_STATE);
			values.put(FIELD_SYNC_PENDING, true);
			values.put(FIELD_SYNC_DELETE, true);
			
			count = db.update(table, values , finalWhere, whereArgs);
			
			// send delete to parse
			final List<String> filter = Arrays.asList(SyncSqlite2ParseProvider.FIELD_OBJECT_ID, SyncSqlite2ParseProvider.FIELD_CREATED_AT, SyncSqlite2ParseProvider.FIELD_UPDATED_AT);
			final ParseObject po = new ParseObject(table);
			String objectId = values.getAsString(SyncSqlite2ParseProvider.FIELD_OBJECT_ID);
			
//			syncDelete(uri, whereArgs, table, db, finalWhere, values, filter,
//					po, objectId);			
			syncAll(uri, table, db);
			break;

		// If the incoming pattern is invalid, throws an exception.
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		/*
		 * Gets a handle to the content resolver object for the current context,
		 * and notifies it that the incoming URI changed. The object passes this
		 * along to the resolver framework, and observers that have registered
		 * themselves for the provider are notified.
		 */
		getContext().getContentResolver().notifyChange(uri, null);

		// Returns the number of rows deleted.
		return count;
	}

	protected void syncDelete(final Uri uri, final String[] whereArgs,
			final String table, final SQLiteDatabase db, String finalWhere,
			final ContentValues values, final List<String> filter,
			final ParseObject po, String objectId) {
		if (objectId == null) {
			Cursor c = query(uri, null, null, null, null);
			if (c != null && c.getCount() == 1) {
				c.moveToPosition(0);
				objectId = c.getString(c.getColumnIndexOrThrow(SyncSqlite2ParseProvider.FIELD_OBJECT_ID));
			}
			c.close();
		}

		if (objectId != null) { 
			po.setObjectId(objectId);
		}
		
		for (Entry<String, Object> entry: values.valueSet()) {
			if (! filter.contains(entry.getKey()))
				po.put(entry.getKey(), entry.getValue());
		}
		
		final String deleteWhere = finalWhere;
		po.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
//						for (String key : po.valueSet()) {
//							values.put(key, po.get(key));
//						}
					values.put(SyncSqlite2ParseProvider.FIELD_OBJECT_ID, po.getObjectId());
					try {
						values.put(SyncSqlite2ParseProvider.FIELD_UPDATED_AT, po.getUpdatedAt().getTime());
//							values.put("createdAt", po.getCreatedAt().getTime()); // null since it's not updated
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					values.put(FIELD_STATUS, GenericSqliteProvider.SYNC_STATE);
					
					// Performs the delete.
					int count = db.delete(table, // The database table name.
							deleteWhere, // The final WHERE clause
							whereArgs // The incoming where clause values.
							);
					getContext().getContentResolver().notifyChange(uri, null);

				} else {
					log.error("", e);
				}
			}
		});
	}

	private void copy(DataObject po, ContentValues v) {
		v.put(SyncSqlite2ParseProvider.FIELD_OBJECT_ID, po.get(DataObject.OBJECT_ID, String.class));
		try {
			v.put(SyncSqlite2ParseProvider.FIELD_UPDATED_AT, po.get(DataObject.UPDATED_AT, Long.class));
			v.put(SyncSqlite2ParseProvider.FIELD_CREATED_AT, po.get(DataObject.CREATED_AT, Long.class)); // null since it's not updated
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		final Set<String> keySet = po.keySet();
		for (String key : keySet) {
			final Object value = po.get(key, Object.class);
			if (value instanceof String) {
				v.put(key, (String)value);
			} else if (value instanceof Boolean) {
				v.put(key, (Boolean)value);
			} else if (value instanceof Long) {
				v.put(key, (Long)value);
			} else if (value instanceof Double) {
				v.put(key, (Double)value);
			} else {
				log.warn("unsupported type {} {}", value, value.getClass());
			}
		}
	}

	/**
	 * This is called when a client calls
	 * {@link android.content.ContentResolver#update(Uri,ContentValues,String,String[])}
	 * Updates records in the database. The column names specified by the keys
	 * in the values map are updated with new data specified by the values in
	 * the map. If the incoming URI matches the note ID URI pattern, then the
	 * method updates the one record specified by the ID in the URI; otherwise,
	 * it updates a set of records. The record or records must match the input
	 * selection criteria specified by where and whereArgs. If rows were
	 * updated, then listeners are notified of the change.
	 * 
	 * @param uri
	 *            The URI pattern to match and update.
	 * @param values
	 *            A map of column names (keys) and new values (values).
	 * @param where
	 *            An SQL "WHERE" clause that selects records based on their
	 *            column values. If this is null, then all records that match
	 *            the URI pattern are selected.
	 * @param whereArgs
	 *            An array of selection criteria. If the "where" param contains
	 *            value placeholders ("?"), then each placeholder is replaced by
	 *            the corresponding element in the array.
	 * @return The number of rows updated.
	 * @throws IllegalArgumentException
	 *             if the incoming URI pattern is invalid.
	 */
	@Override
	public int update(final Uri uri, final ContentValues values, String where,
			String[] whereArgs) {
		final String table = uri.getPathSegments().get(0);
		log.debug("table {}", table);

		// Opens the database object in "write" mode.
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		String finalWhere;

		// Does the update based on the incoming URI pattern
		switch (sUriMatcher.match(uri)) {

		// If the incoming URI matches the general notes pattern, does the
		// update based on
		// the incoming data.
		case TABLE:
			if (values == null) { // use this to trigger sync
//				SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//				qb.setTables(table);
//				qb.appendWhere(inWhere)
//				db.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)
				
				syncAllManual(uri, table, db);
				
				
				count = 0;
				break;
			}
			// Does the update and returns the number of rows updated.
			count = db.update(table, // The database table name.
					values, // A map of column names and new values to use.
					where, // The where clause column names.
					whereArgs // The where clause column values to select on.
					);
			break;

		// If the incoming URI matches a single note ID, does the update based
		// on the incoming
		// data, but modifies the where clause to restrict it to the particular
		// note ID.
		case TABLE_ID:
			// From the incoming URI, get the note ID
			final String rowId = uri.getPathSegments().get(
					ID_PATH_POSITION);

			/*
			 * Starts creating the final WHERE clause by restricting it to the
			 * incoming note ID.
			 */
			finalWhere = _ID + // The ID column name
					" = " + // test for equality
					uri.getPathSegments(). // the incoming note ID
							get(ID_PATH_POSITION);

			// If there were additional selection criteria, append them to the
			// final WHERE
			// clause
			if (where != null) {
				finalWhere = finalWhere + " AND " + where;
			}

//			values.put(FIELD_STATUS, "update");
			values.put(FIELD_SYNC_PENDING, true);
			values.put(FIELD_SYNC_DELETE, false);
			
			// Does the update and returns the number of rows updated.
			count = db.update(table, // The database table name.
					values, // A map of column names and new values to use.
					finalWhere, // The final WHERE clause to use
								// placeholders for whereArgs
					whereArgs // The where clause column values to select on, or
								// null if the values are in the where argument.
					);
			
//			syncUpdate(uri, values, table, db, rowId);
			syncAll(uri, table, db);
			break;
		// If the incoming pattern is invalid, throws an exception.
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		/*
		 * Gets a handle to the content resolver object for the current context,
		 * and notifies it that the incoming URI changed. The object passes this
		 * along to the resolver framework, and observers that have registered
		 * themselves for the provider are notified.
		 */
		getContext().getContentResolver().notifyChange(uri, null);

		// Returns the number of rows updated.
		return count;
	}

	protected void syncUpdate(final Uri uri, final ContentValues values,
			final String table, final SQLiteDatabase db, final String rowId) {
		final List<String> filter = Arrays.asList(SyncSqlite2ParseProvider.FIELD_OBJECT_ID, SyncSqlite2ParseProvider.FIELD_CREATED_AT, SyncSqlite2ParseProvider.FIELD_UPDATED_AT);
		final ParseObject po = new ParseObject(table);
		String objectId = values.getAsString(SyncSqlite2ParseProvider.FIELD_OBJECT_ID);
		
		if (objectId == null) {
			Cursor c = query(uri, null, null, null, null);
			if (c != null && c.getCount() == 1) {
				c.moveToPosition(0);
				objectId = c.getString(c.getColumnIndexOrThrow(SyncSqlite2ParseProvider.FIELD_OBJECT_ID));
			}
			c.close();
		}

		if (objectId != null) { 
			po.setObjectId(objectId);
		}
		
		for (Entry<String, Object> entry: values.valueSet()) {
			if (! filter.contains(entry.getKey()))
				po.put(entry.getKey(), entry.getValue());
		}
		
		po.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
//						for (String key : po.valueSet()) {
//							values.put(key, po.get(key));
//						}
					values.put(SyncSqlite2ParseProvider.FIELD_OBJECT_ID, po.getObjectId());
					try {
						values.put(SyncSqlite2ParseProvider.FIELD_UPDATED_AT, po.getUpdatedAt().getTime());
//							values.put("createdAt", po.getCreatedAt().getTime()); // null since it's not updated
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					values.put(FIELD_STATUS, GenericSqliteProvider.SYNC_STATE);
					
					db.update(table, values, SyncSqlite2ParseProvider._ID +
							" = "+rowId, null);
					getContext().getContentResolver().notifyChange(uri, null);

				} else {
					log.error("", e);
				}
			}
		});
	}

	
	protected synchronized void syncAll(final Uri uri, final String table,
			final SQLiteDatabase db) {
				
		syncAllManual(uri, table, db);
	}

	boolean syncing;
	protected synchronized void syncAllManual(final Uri uri, final String table,
			final SQLiteDatabase db) {
		if (syncing) {
			log.debug("syncing already, abort");
			return;
		}
		syncing = true;
		
		new Thread(new Runnable() {					
			@Override
			public void run() {
				// TODO Auto-generated method stub
				long syncAt = 0;
				
				{ // retrieve last sync time
					String[] selectionArgs = {Boolean.FALSE.toString()};
					String[] columns = {SyncSqlite2ParseProvider.FIELD_UPDATED_AT};
					final Cursor c = db.query(table, columns , FIELD_SYNC_PENDING + " = 0", null , null, null, SyncSqlite2ParseProvider.FIELD_UPDATED_AT + " desc", null);
					try {
						if (c.getCount() > 0 && c.moveToPosition(0)) {
							syncAt = c.getLong(c.getColumnIndexOrThrow(SyncSqlite2ParseProvider.FIELD_UPDATED_AT));
						}
					} finally {
						c.close();
					}
				}
				
				try {
					// get changes from server
//					final List<ParseObject> serverChanges = new ParseQuery(table)
//					.whereGreaterThan(SyncSqlite2ParseProvider.FIELD_UPDATED_AT, new Date(syncAt)).find();

					// TODO decide what to use
					final DataObjectFactory factory = 
//							new DataObjectFactorySM();
							new DataObjectFactoryParse();
					
					final DataObjectQuery query = factory.createDataObjectQuery(table);
					final DataObjectQuery whereGreaterThan = query
						.whereGreaterThan(DataObject.UPDATED_AT, syncAt);
					
					List<DataObject> serverChanges;
					
					try {
						serverChanges = whereGreaterThan.find().get();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						serverChanges = new ArrayList<DataObject>();
					}
					
					log.debug("server: {}", serverChanges.size());
					for (DataObject po : serverChanges) {
						// apply to local
						final Boolean status = po.get(FIELD_SYNC_DELETE, Boolean.class);
						log.debug("remote: {}", status);
						
						String objectId = po.get(DataObject.OBJECT_ID, String.class);
						
						/*if (GenericSqliteProvider.DELETE_STATE.equals(status)) {
							final int delete = db.delete(table, OBJECT_ID_FIELD +
									" = ?", new String[]{objectId}); log.debug("delete {}", delete);
						} else*/ if (true) {
//									String content = po.getString("content");
							
							ContentValues v = new ContentValues();									
//							copy(po, v);
							v.put(SyncSqlite2ParseProvider.FIELD_SYNC_DELETE, po.get(FIELD_SYNC_DELETE, Boolean.class));
							v.put(SyncSqlite2ParseProvider.CONTENT_FIELD, po.get(CONTENT_FIELD, String.class));
							v.put(SyncSqlite2ParseProvider.DONE_FIELD, po.get(DONE_FIELD, Boolean.class));

//							v.put(FIELD_STATUS, GenericSqliteProvider.SYNC_STATE);
							v.put(FIELD_SYNC_PENDING, false);
							
							int update = db.update(table, v, SyncSqlite2ParseProvider.FIELD_OBJECT_ID +
									" = ? AND 0 = " + FIELD_SYNC_PENDING, new String[]{objectId});
							if (update == 0) {
								// there may be a pending local changes
								ContentValues removeId = new ContentValues();
								removeId.putNull(FIELD_OBJECT_ID);
								
								int move = db.update(table, removeId, SyncSqlite2ParseProvider.FIELD_OBJECT_ID +
										" = ? AND 0 <> " + FIELD_SYNC_PENDING, new String[]{objectId});
								log.debug("move {}", move);
								
								long insert = db.insert(table, null, v);
								log.debug("inserted {}", insert);
							} else {
								log.debug("update {}", update);
							}
						}
					}
					
					// get local changes
					{  
						String[] selectionArgs = {Boolean.TRUE.toString()};
						String[] columns = null;
						final Cursor c = db.query(table, columns , FIELD_SYNC_PENDING + " <> 0", 
								null , null, null, 
								FIELD_UPDATED_AT + " desc", null);
						try {
							while (c.moveToNext()) {
								final long rowId = c.getLong(c.getColumnIndex(SyncSqlite2ParseProvider._ID));
//								String status = c.getString(c.getColumnIndexOrThrow(FIELD_STATUS));
//								log.debug("local {} {}", c.getPosition(), status);
								
								final List<String> filter = Arrays.asList(SyncSqlite2ParseProvider.FIELD_OBJECT_ID, SyncSqlite2ParseProvider.FIELD_CREATED_AT, SyncSqlite2ParseProvider.FIELD_UPDATED_AT);
//								final ParseObject po = new ParseObject(table);
								DataObject po = factory.createDataObject(table);
								
								String objectId = c.getString(c.getColumnIndexOrThrow(SyncSqlite2ParseProvider.FIELD_OBJECT_ID));
								if (objectId != null) {
									po.put(DataObject.OBJECT_ID, objectId);
								}
//								if (GenericSqliteProvider.DELETE_STATE.equals(status)) {
//									po.put(FIELD_STATUS, GenericSqliteProvider.DELETE_STATE);
//								} else 
								{
									int n = c.getColumnCount();
//											for (int i = 0; i < n; i++) {
//												c.get
//											}
									po.put(SyncSqlite2ParseProvider.FIELD_SYNC_DELETE, 0 != c.getInt(c.getColumnIndexOrThrow(SyncSqlite2ParseProvider.FIELD_SYNC_DELETE)));
									po.put(SyncSqlite2ParseProvider.CONTENT_FIELD, c.getString(c.getColumnIndexOrThrow(SyncSqlite2ParseProvider.CONTENT_FIELD)));
									po.put(SyncSqlite2ParseProvider.DONE_FIELD, c.getInt(c.getColumnIndexOrThrow(SyncSqlite2ParseProvider.DONE_FIELD)) != 0);
//									po.put(FIELD_STATUS, status);
								}
//										for (Entry<String, Object> entry: values.valueSet()) {
//											if (! filter.contains(entry.getKey()))
//												po.put(entry.getKey(), entry.getValue());
//										}
//										for (String key: values.keySet()) {
//											if (! filter.contains(key))
//												po.put(key, values.get(key));
//										}
								try {
									String id = po.save().get();
									log.debug("id:{}", id);
									
									ContentValues values = new ContentValues();
									values.put(SyncSqlite2ParseProvider.FIELD_OBJECT_ID, po.get(DataObject.OBJECT_ID, String.class));
									
									final Long created = po.get(DataObject.CREATED_AT, Long.class);
									if (created != null)
										values.put(SyncSqlite2ParseProvider.FIELD_CREATED_AT, created);
									final Long updated = po.get(DataObject.UPDATED_AT, Long.class);
									if (updated != null)
										values.put(SyncSqlite2ParseProvider.FIELD_UPDATED_AT, updated);
//									values.put(FIELD_STATUS, GenericSqliteProvider.SYNC_STATE);
									values.put(FIELD_SYNC_PENDING, false);
									final int update = db.update(table, values, SyncSqlite2ParseProvider._ID +
											" = "+rowId, null);
									getContext().getContentResolver().notifyChange(uri, null);

								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
									log.error("", e1);
								}
									

							}
//									if (c.getCount() > 0 && c.moveToPosition(0)) {
//										syncAt = c.getLong(c.getColumnIndexOrThrow("updatedAt"));
//									}
						} finally {
							c.close();
						}
					}

					getContext().getContentResolver().notifyChange(uri, null);
//				} catch (ParseException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (ExecutionException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
				} finally {
					syncing = false;
				}

			}

		}).start();
	}

	/**
	 * A test package can call this to get a handle to the database underlying
	 * NotePadProvider, so it can insert test data into the database. The test
	 * case class is responsible for instantiating the provider in a test
	 * context; {@link android.test.ProviderTestCase2} does this during the call
	 * to setUp()
	 * 
	 * @return a handle to the database helper object for the provider's data.
	 */
	SQLiteOpenHelper getOpenHelperForTest() {
		return mOpenHelper;
	}
}
