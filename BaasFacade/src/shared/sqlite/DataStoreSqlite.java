package shared.sqlite;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Nosql style api build on top of sqlite.  Tables and columns will be added on demand.
 * 
 * @author sheng
 *
 */
public class DataStoreSqlite {
	public DataStoreSqlite(SQLiteOpenHelper db) {
		super();
		this.db = db;
	}

	SQLiteOpenHelper db;
	
	public long save(String table, ContentValues values) {
		String nullColumnHack = null;
		int conflictAlgorithm = SQLiteDatabase.CONFLICT_REPLACE;
		final SQLiteDatabase wdb = db.getWritableDatabase();
		
		try {
			return wdb.insertWithOnConflict(table, nullColumnHack, values, conflictAlgorithm);
		} catch (RuntimeException e) {
			e.printStackTrace();
			final String message = e.getMessage();
			if (message.contains("no such table")) {
				wdb.execSQL("CREATE TABLE " +
						table +
						" (_id INTEGER PRIMARY KEY, objectId TEXT UNIQUE )");
				return save(table, values);
			} else {
				final String prefix = "has no column named ";
				if (message.contains(prefix)) {
					final String postfix = "";
					final String reg = ".*" +
							Pattern.quote(prefix) +
							"([a-zA-Z]*)" +
							Pattern.quote(postfix) +
							".*";
					Pattern depArrHours = Pattern.compile(reg);
					Matcher matcher = depArrHours.matcher(message);
					
					String col;
					if (matcher.matches()) {
						col = matcher.group(1);
					} else {
						col = null;
					}
					String type = "DATETIME";
					
					final Object v = values.get(col);
					if (v instanceof Integer)
						type = "INTEGER";
					else if (v instanceof Long)
						type = "INTEGER";
					else if (v instanceof Float)
						type = "DOUBLE";
					else if (v instanceof Double)
						type = "DOUBLE";
					else if (v instanceof String)
						type = "TEXT";
					else if (v instanceof Boolean)
						type = "INTEGER";
					else if (v instanceof byte[])
						type = "BLOB";
					else
						throw new RuntimeException("unsupported type " + v.getClass());
					
					wdb.execSQL("ALTER TABLE \"" +
							table +
							"\" ADD COLUMN \"" +
							col +
							"\" " +
							type);
					return save(table, values);
				} else {
					throw e;
				}
			}
		}
	}
	
	public int delete(String table, String objectId) {
		final SQLiteDatabase wdb = db.getWritableDatabase();
		
		String[] whereArgs = {objectId};
		return wdb.delete(table, "objectId = ?", whereArgs);
	}

	public Cursor query(String table, String selection, String[] selectionArgs, String orderBy) {
		final SQLiteDatabase wdb = db.getWritableDatabase();
		
//		String[] whereArgs = {objectId};
		String[] columns = null;
		String groupBy = null;
		String having = null;
		String limit = null;
		return wdb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}
}
