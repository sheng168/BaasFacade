package shared.baas.sqlite;


import shared.baas.DoCallback;
import shared.baas.FacadeFactory;
import shared.baas.impl.AbstractObjectData;
import android.content.ContentValues;
import android.net.Uri;

class SqliteObjectData<T> extends AbstractObjectData<T> {
	final ContentValues values = new ContentValues();
	
//	Context ctx = null;
//	Uri baseUri;
	String className;

	private SqliteFacadeFactory factory;
	
	public SqliteObjectData(SqliteFacadeFactory factory, String className) {
		super();
//		this.ctx = ctx;
//		this.baseUri = baseUri;
		this.factory = factory;
		this.className = className;
	}

	@Override
	public void put(String key, Object value) {
		if (value instanceof String)
			values.put(key, (String) value);
		else if (value instanceof Boolean)
			values.put(key, (Boolean)value);
		else if (value instanceof Long)
			values.put(key, (Long)value);
		else
			throw new IllegalArgumentException(key+":"+value);
	}
	
	@Override
	public Object get(String key, Class<?> type) {
		return values.get(key);
	}

	@Override
	public void saveInBackground(DoCallback callback) {		
		Uri url = Uri.withAppendedPath(factory.baseUri, className);
		factory.ctx.getContentResolver().insert(url, values);
		
		callback.done(null);
	}

	@Override
	public void deleteInBackground(DoCallback callback) {
		Uri url = Uri.withAppendedPath(factory.baseUri, className);
		url = Uri.withAppendedPath(url, getObjectId());
		factory.ctx.getContentResolver().delete(url, null, null);
		
		callback.done(null);
	}
	
	@Override
	public void refreshInBackground(DoCallback callback) {
		callback.done(new UnsupportedOperationException());
	}


	@Override
	public FacadeFactory getFactory() {
		return factory;
	}

	@Override
	public String toString() {
		return super.toString() + values;
	}
}