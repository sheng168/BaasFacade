package shared.baas.keyvalue.sqlite;

import java.util.ArrayList;
import java.util.List;

import shared.baas.ListCallback;
import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.DataObjectQuery;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DataObjectQuerySqlite<T> implements DataObjectQuery {
//	SqliteFacade<T> facade;
	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	StringBuilder orderBy = new StringBuilder();
	private int skip;
	private int count;
	ContentResolver cr;
	private String className;
	private Uri baseUri;


	public List<DataObject> find() throws Exception {
		Uri uri = Uri.withAppendedPath(baseUri, className);
		
		String sortOrder = orderBy.length() == 0 ? null : orderBy.toString();
		String selection = null;
		String[] selectionArgs = null;
		final Cursor c = cr.query(uri, null, selection, selectionArgs, sortOrder);
		
		final ArrayList<DataObject> list = new ArrayList<DataObject>();
		
		int last = Math.max(c.getCount(), skip+count);
		for (int i = skip; i < last; i++) {
			list.add(new DataObjectSqlite());
		}
		return list;
	}

	@Override
	public void whereEqualTo(String key, Object value) {
		qb.appendWhere(key + " = ?");
	}

	@Override
	public void whereNotEqualTo(String key, Object value) {
		qb.appendWhere(key + " <> ?");
	}

	@Override
	public void findInBackground(ListCallback<DataObject> callback) {
		try {
			callback.done(find());
		} catch (Exception e) {
			callback.equals(e);
		}
	}

	@Override
	public void whereGreaterThan(String key, Object value) {
		qb.appendWhere(key + " > ?");
	}

	@Override
	public void orderByAsc(String key) {
		if (orderBy.length() > 0) {
			orderBy.append(", ");
		}
		
		orderBy.append(key).append(" asc");
	}

	@Override
	public void orderByDesc(String key) {
		if (orderBy.length() > 0) {
			orderBy.append(", ");
		}
		
		orderBy.append(key).append(" desc");
	}

	@Override
	public void isInRange(int skip, int count) {
		this.skip = skip;
		this.count = count;
	}
}

