package shared.baas.keyvalue.sqlite;


import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import shared.baas.DoCallback;
import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.ListenableFuture;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

public class DataObjectSqlite implements DataObject {
	final ContentValues values = new ContentValues();
	
	ContentResolver cr;
	Uri baseUri;
	String className;



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
		else
			throw new IllegalArgumentException(key+":"+value);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key, Class<T> type) {
		return (T) values.get(key);
	}

	@Override
	public ListenableFuture<String> save() {		
		Uri url = Uri.withAppendedPath(baseUri, className);
		cr.insert(url, values);
		
		return null;
	}

	@Override
	public void deleteInBackground(DoCallback callback) {
		Uri url = Uri.withAppendedPath(baseUri, className);
//		url = Uri.withAppendedPath(url, getObjectId());
		cr.delete(url, null, null);
		
		callback.done(null);
	}
	
	@Override
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
}