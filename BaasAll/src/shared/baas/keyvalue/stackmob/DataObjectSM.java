package shared.baas.keyvalue.stackmob;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import shared.baas.DoCallback;
import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.ListenableFuture;
import shared.baas.keyvalue.ListenableFuture.Basic;

import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMobDatastore;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;
//import shared.baas.DataObject;

public class DataObjectSM implements DataObject {
	String className;
//	private String id;
	Map<String, Object> obj = new HashMap<String, Object>();

	public DataObjectSM(String className) {
		super();
		this.className = className;
	}

	protected StackMobDatastore dataStore() {
		return StackMob.getStackMob().getDatastore();
	}

	@Override
	public void refreshInBackground(final DoCallback callback) {
		dataStore().get(className+"/"+getId(), new StackMobCallback() {
			@Override
			public void success(String responseBody) {
				callback.done();
			}

			@Override
			public void failure(StackMobException e) {
				callback.error(e);
			}
		});
	}

	@Override
	public ListenableFuture<String> save() {		
		final Basic<String> future = new ListenableFuture.Basic<String>();

		final StackMobCallback stackMobCallback = new StackMobCallback() {
			@Override
			public void success(String responseBody) {
				future.set(responseBody, null);
			}

			@Override
			public void failure(StackMobException e) {
				future.set(null, e);
			}
		};
		
		final String id = getId();
		if (id == null) {
			dataStore().post(className, obj, stackMobCallback);
		} else {
			dataStore().put(className, id, obj, stackMobCallback);
		}
		
		return future;
	}

	@Override
	public void deleteInBackground(final DoCallback callback) {
		dataStore().delete(className, getId(), new StackMobCallback() {
			@Override
			public void success(String responseBody) {
				callback.done();
			}

			@Override
			public void failure(StackMobException e) {
				callback.error(e);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key, Class<T> type) {
		return (T) obj.get(key);
	}

	@Override
	public void put(String key, Object value) {
		obj.put(key, value);
	}

	String getId() {
		final String id_key = id_key();
		return (String) obj.get(id_key);
	}

	protected String id_key() {
		return className + "_id";
	}

	void setId(String id) {
		obj.put(id_key(), id);
	}

	@Override
	public Set<String> keySet() {
		return obj.keySet();
	}
}