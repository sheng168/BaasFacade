package shared.baas.sqlite;


import java.util.Set;

import shared.baas.DoCallback;
import shared.baas.DataFacadeFactory;
import shared.baas.impl.AbstractDataObject;
import shared.baas.keyvalue.ListenableFuture;
import android.content.ContentValues;
import android.net.Uri;

class SqliteObjectData extends AbstractDataObject {
	final ContentValues values = new ContentValues();
	
//	Context ctx = null;
//	Uri baseUri;
	String className;

	private final SqliteFacadeFactory factory;
	
	public SqliteObjectData(SqliteFacadeFactory factory, String className) {
		super();
//		this.ctx = ctx;
//		this.baseUri = baseUri;
		this.factory = factory;
		this.className = className;
	}

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
	
//	@Override
	public <T> T get(String key, Class<T> type) {
		return (T) values.get(key);
	}

//	@Override
	public void saveInBackground(DoCallback callback) {		
		Uri url = Uri.withAppendedPath(factory.baseUri, className);
		factory.ctx.getContentResolver().insert(url, values);
		
		callback.done(null);
	}

	@Override
	public void deleteInBackground(DoCallback callback) {
//		Uri url = Uri.withAppendedPath(factory.baseUri, className);
//		url = Uri.withAppendedPath(url, getObjectId());
//		factory.ctx.getContentResolver().delete(url, null, null);
		
		callback.done(null);
	}
	
//	@Override
	public void refreshInBackground(DoCallback callback) {
		callback.done(new UnsupportedOperationException());
	}


//	@Override
	public DataFacadeFactory getFactory() {
		return factory;
	}

	@Override
	public String toString() {
		return super.toString() + values;
	}

	@Override
	public Set<String> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListenableFuture<String> save() {
		// TODO Auto-generated method stub
		return null;
	}
}