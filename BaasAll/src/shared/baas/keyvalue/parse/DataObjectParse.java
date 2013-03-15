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

public class DataObjectParse implements DataObject {
	ParseObject obj;

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
		if ("objectId".equals(key))
			return (T) obj.getObjectId();
		else if ("updatedAt".equals(key))
			return (T) obj.getUpdatedAt();		
		else if ("createdAt".equals(key))
			return (T) obj.getCreatedAt();
		else
			return (T) obj.get(key);
	}

	@Override
	public void put(String key, Object value) {
		if ("objectId".equals(key))
			obj.setObjectId((String) value);
		else
			obj.put(key, value); //TODO
	}

	@Override
	public Set<String> keySet() {
		return obj.keySet();
	}
	
	@Override
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

	@Override
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
}
