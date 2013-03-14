package shared.baas.keyvalue.parse;

import shared.baas.DoCallback;
import shared.baas.keyvalue.DataObject;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.RefreshCallback;
import com.parse.SaveCallback;

//import shared.baas.DataObject;

public class DataObjectParse implements DataObject {
	ParseObject obj;

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
	public void saveInBackground(final DoCallback callback) {
		obj.saveInBackground(new SaveCallback() {			
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
	public Object get(String key, Class<?> type) {
		return obj.get(key);
	}

	@Override
	public void put(String key, Object value) {
		obj.put(key, value);
	}
}
