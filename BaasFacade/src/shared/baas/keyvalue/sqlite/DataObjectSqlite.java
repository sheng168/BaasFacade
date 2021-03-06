package shared.baas.keyvalue.sqlite;


import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import shared.baas.DoCallback;
import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.ListenableFuture;
import shared.baas.keyvalue.ListenableFuture.Basic;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

public class DataObjectSqlite extends DataObject {
	final ContentValues values = new ContentValues();
	
	ContentResolver cr;
	Uri baseUri;
	String className;

	protected DataObjectSqlite(ContentResolver cr, Uri baseUri, String className) {
		super();
		this.cr = cr;
		this.baseUri = baseUri;
		this.className = className;
	}

//	private DataObjectSqlite(String className) {
//		super();
//		this.className = className;
//	}

	@Override
	public void put(String key, Object value) {
		if (value == null)
			values.putNull(key);
		else if (value instanceof String)
			values.put(key, (String) value);
		
		else if (value instanceof Boolean)
			values.put(key, (Boolean)value);
		
		else if (value instanceof Long)
			values.put(key, (Long)value);
		else if (value instanceof Integer)
			values.put(key, (Integer)value);
		else if (value instanceof Short)
			values.put(key, (Short)value);
		else if (value instanceof Byte)
			values.put(key, (Byte)value);
		
		else if (value instanceof Double)			
			values.put(key, (Double)value);		
		else if (value instanceof Float)
			values.put(key, (Float)value);
		
		else if (value instanceof byte[])
			values.put(key, (byte[])value);
		else if (value instanceof DataObjectSqlite) {			
			DataObjectSqlite dataObjectSqlite = (DataObjectSqlite)value;
			values.put(key, dataObjectSqlite.getObjectId());
			dataObjectSqlite.save(); // TODO sqlite will save related object first when added
		}
		else
			throw new IllegalArgumentException(key+":"+value + " class:" + value.getClass());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key, Class<T> type) {
		return (T) values.get(key);
	}

	@Override
	public ListenableFuture<String> save() {		
		final Basic<String> future = new ListenableFuture.Basic<String>();
		
		try {
			getObjectId(); // generate id if doesn't exist
			Uri url = Uri.withAppendedPath(baseUri, className);
			Long _id = values.getAsLong(BaseColumns._ID);
			if (_id == null) {
				Uri insert = cr.insert(url, values);
				if (insert == null) {
					future.set(null, new Exception("insert return null Uri"));
				} else {
					List<String> segs = insert.getPathSegments();
					String id = segs.get(segs.size() - 1);
					values.put(BaseColumns._ID, id);
					future.set(id, null);
				}
			} else {
				int update = cr.update(Uri.withAppendedPath(url, _id.toString()), values, null, null);
				if (update == 1) {
					future.set(_id.toString(), null);
				} else {
					future.set(null, new Exception(update + " updated for id: " + _id));
				}
			}
		} catch (Exception e) {
			future.set(null, e);
		}
		
		return future;
	}

//	@Override
	public void deleteInBackground(DoCallback callback) {
		Uri url = Uri.withAppendedPath(baseUri, className);
//		url = Uri.withAppendedPath(url, getObjectId());
		cr.delete(url, null, null);
		
		callback.done(null);
	}
	
//	@Override
	public void refreshInBackground(DoCallback callback) {
		callback.done(new UnsupportedOperationException());
	}

	@Override
	public Set<String> keySet() {
		final HashSet<String> hashSet = new HashSet<String>();
		for (Entry<String, Object> f : values.valueSet()) {
			hashSet.add(f.getKey());
		}
		return hashSet;
	}

	@Override
	public String getObjectId() {		
		String id = get(OBJECT_ID, String.class);
		if (id == null) {
			id = UUID.randomUUID().toString();
			put(OBJECT_ID, id);
		}
		return id;
	}
}