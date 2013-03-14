package shared.baas.keyvalue;


import shared.baas.DoCallback;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

public class SqliteDataObject implements DataObject {
	final ContentValues values = new ContentValues();
	
	ContentResolver cr;
	Uri baseUri;
	String className;

//	private final SqliteFacadeFactory factory;
	
//	public SqliteObjectData(SqliteFacadeFactory factory, String className) {
//		super();
////		this.ctx = ctx;
////		this.baseUri = baseUri;
//		this.factory = factory;
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
		else
			throw new IllegalArgumentException(key+":"+value);
	}
	
	@Override
	public Object get(String key, Class<?> type) {
		return values.get(key);
	}

	@Override
	public void saveInBackground(DoCallback callback) {		
		Uri url = Uri.withAppendedPath(baseUri, className);
		cr.insert(url, values);
		
		callback.done(null);
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
}