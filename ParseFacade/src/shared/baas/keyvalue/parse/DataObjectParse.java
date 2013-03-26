package shared.baas.keyvalue.parse;

import java.util.Set;

import shared.baas.DoCallback;
import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.ListenableFuture;
import shared.baas.keyvalue.ListenableFuture.Basic;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.RefreshCallback;
import com.parse.SaveCallback;

//import shared.baas.DataObject;

public class DataObjectParse extends DataObject {
	/**
	 * if you are using this type directly, you can access this value
	 */
	public final ParseObject obj;

	public DataObjectParse(String className) {
		this(new ParseObject(className));
	}
	public DataObjectParse(ParseObject obj) {
		super();
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key, Class<T> type) {
		// provide uniform access to all keys
		if (OBJECT_ID.equals(key))
			return (T) obj.getObjectId();
		else if (UPDATED_AT.equals(key))
			return obj.getUpdatedAt() == null?null:(T) Long.valueOf(obj.getUpdatedAt().getTime());		
		else if (CREATED_AT.equals(key))
			return obj.getCreatedAt() == null?null:(T) Long.valueOf(obj.getCreatedAt().getTime());
		else {
			Object object = obj.get(key);
			if (object instanceof ParseObject)
				return (T) new DataObjectParse((ParseObject)object);
			else
				return (T) object;
		}
	}

	@Override
	public void put(String key, Object value) {
		if (OBJECT_ID.equals(key))
			obj.setObjectId((String) value); // allow on new objects
		else if (UPDATED_AT.equals(key))
			throw new IllegalArgumentException("field can't be set: " + key);
		else if (CREATED_AT.equals(key))
			throw new IllegalArgumentException("field can't be set: " + key);
		else if (value == null) {
			obj.remove(key);
		} else if (value instanceof DataObjectParse) {
			obj.put(key, ((DataObjectParse)value).obj);
		} else {
			obj.put(key, value);
		}
	}

	@Override
	public Set<String> keySet() {
		return obj.keySet();
	}
	
//	@Override
	public void refreshInBackground(final DoCallback callback) {
		obj.refreshInBackground(new RefreshCallback() {
			@Override
			public void done(ParseObject o, ParseException e) {
				if (e == null) {
					callback.error(e);
				} else {
					callback.done();
				}
			}
		});
	}

	@Override
	public ListenableFuture<String> save() {
		final Basic<String> future = new ListenableFuture.Basic<String>();

		obj.saveInBackground(new SaveCallback() {			
			@Override
			public void done(ParseException e) {
				if (e == null) {
					future.set(obj.getObjectId(), e);
				} else {
					future.set(null, e);
				}
			}
		});
		
		return future;
	}

//	@Override
	public void deleteInBackground(final DoCallback callback) {
		obj.deleteInBackground(new DeleteCallback() {			
			@Override
			public void done(ParseException e) {
				if (e == null) {
					callback.error(e);
				} else {
					callback.done();
				}
			}
		});
	}
	@Override
	public String getObjectId() {
		return obj.getObjectId();
	}
}
