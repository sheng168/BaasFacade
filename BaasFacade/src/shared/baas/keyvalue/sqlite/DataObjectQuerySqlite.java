package shared.baas.keyvalue.sqlite;

import java.util.ArrayList;
import java.util.List;

import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.DataObjectQuery;
import shared.baas.keyvalue.ListenableFuture;
import shared.baas.keyvalue.ListenableFuture.Basic;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DataObjectQuerySqlite extends DataObjectQuery {
//	SqliteFacade<T> facade;
//	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	StringBuilder orderBy = new StringBuilder();
	StringBuilder where = new StringBuilder();
	
	private int skip;
	private int count;
	
	ContentResolver cr;
	private Uri baseUri;
	private final String className;
	private ArrayList<String> whereArgs = new ArrayList<String>();

	public DataObjectQuerySqlite(ContentResolver cr, Uri baseUri,
			String className) {
		super();
		this.cr = cr;
		this.baseUri = baseUri;
		this.className = className;
	}

//	public DataObjectQuerySqlite(String className) {
//		super();
//		this.className = className;
//	}

	@Override
	public ListenableFuture<List<DataObject>> find() {		
		final Basic<List<DataObject>> future = new ListenableFuture.Basic<List<DataObject>>();
		
		try {
			final Cursor c = findCursor();
			
			final ArrayList<DataObject> list = new ArrayList<DataObject>();
			
			int afterLast = Math.max(c.getCount(), skip+count);
			int columnIndex = c.getColumnIndex(BaseColumns._ID);
			for (int i = skip; i < afterLast; i++) {
				c.moveToPosition(i);
				long id = c.getLong(columnIndex);
				DataObjectSqlite obj = new DataObjectWithCursor(cr, baseUri, className, c, i);
				obj.values.put(BaseColumns._ID, id);
				list.add(obj);
			}

			future.set(list, null);
		} catch (Exception e) {
			future.set(null, e);
		}
		
		return future;
	}

	public Cursor findCursor() {
		Uri uri = Uri.withAppendedPath(baseUri, className);
		
		String sortOrder = orderBy.length() == 0 ? null : orderBy.toString();
		String selection = where.toString();
		String[] selectionArgs = whereArgs.toArray(new String[whereArgs.size()]);
		final Cursor c = cr.query(uri, null, selection, selectionArgs, sortOrder);
		return c;
	}

	@Override
	public DataObjectQuery whereEqualTo(String key, Object value) {
		appendWhere(key + " = ?", value);
		return this;
	}

	
	private void appendWhere(String string, Object value) {
		if (where.length() > 0) {
			where.append(" AND ");
		}
		
		where.append(string);
		
		if (value == null) {
			whereArgs.add(null);
		} else if (value instanceof Boolean) {
			whereArgs.add(Boolean.TRUE.equals(value)?"1":"0");
		} else {
			whereArgs.add(value.toString());
		}
	}

	@Override
	public DataObjectQuery whereNotEqualTo(String key, Object value) {
		appendWhere(key + " <> ?", value);
		return this;
	}

//	@Override
//	public void findInBackground(ListCallback<DataObject> callback) {
//		try {
//			callback.done(find());
//		} catch (Exception e) {
//			callback.equals(e);
//		}
//	}

	@Override
	public DataObjectQuery whereGreaterThan(String key, Object value) {
		appendWhere(key + " > ?", value);
		return this;
	}

	@Override
	public DataObjectQuery orderByAsc(String key) {
		if (orderBy.length() > 0) {
			orderBy.append(", ");
		}
		
		orderBy.append(key).append(" asc");
		return this;
	}

	@Override
	public DataObjectQuery orderByDesc(String key) {
		if (orderBy.length() > 0) {
			orderBy.append(", ");
		}
		
		orderBy.append(key).append(" desc");
		return this;
	}

	@Override
	public DataObjectQuery isInRange(int skip, int count) {
		this.skip = skip;
		this.count = count;
		return this;
	}
}

