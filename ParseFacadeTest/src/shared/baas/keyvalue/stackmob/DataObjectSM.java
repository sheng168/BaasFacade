package shared.baas.keyvalue.stackmob;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import shared.baas.DoCallback;
import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.ListenableFuture;
import shared.baas.keyvalue.ListenableFuture.Basic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMobDatastore;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;
//import shared.baas.DataObject;

public class DataObjectSM extends DataObject {
	String className;
//	private String id;
//	Map<String, Object> obj = new HashMap<String, Object>();
	JsonObject jsonObject = new JsonObject();

	public DataObjectSM(String className) {
		super();
		this.className = className;
	}

	public DataObjectSM(String className, JsonObject jsonObject) {
		this.className = className;
		this.jsonObject = jsonObject;
	}

	protected StackMobDatastore dataStore() {
		return StackMob.getStackMob().getDatastore();
	}

//	@Override
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
				final JsonElement json = new JsonParser().parse(responseBody);
//				GsonBuilder gsonBuilder = new GsonBuilder();
//		        final Gson gson = gsonBuilder.create();
//				Map<String, Object> map = gson.fromJson(json, Map.class);
				jsonObject = json.getAsJsonObject();

				future.set(getId(), null);
			}

			@Override
			public void failure(StackMobException e) {
				future.set(null, e);
			}
		};
		
		final String id = getId();
		if (id == null) {
			dataStore().post(className, jsonObject.toString(), stackMobCallback);
		} else {
			dataStore().put(className, id, jsonObject.toString(), stackMobCallback);
		}
		
		return future;
	}

//	@Override
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
		final String lowerCase = key.toLowerCase();
		return (T) jsonObject.get(lowerCase);
	}

	@Override
	public void put(String key, Object value) {
//		obj.put(key.toLowerCase(), value);
		if (value == null)
			jsonObject.remove(key);
		else if (value instanceof String)
			jsonObject.addProperty(key, (String) value);
		
		else if (value instanceof Boolean)
			jsonObject.addProperty(key, (Boolean)value);
		
		else if (value instanceof Long)
			jsonObject.addProperty(key, (Long)value);
		else if (value instanceof Integer)
			jsonObject.addProperty(key, (Integer)value);
		else if (value instanceof Short)
			jsonObject.addProperty(key, (Short)value);
		else if (value instanceof Byte)
			jsonObject.addProperty(key, (Byte)value);
		
		else if (value instanceof Double)			
			jsonObject.addProperty(key, (Double)value);		
		else if (value instanceof Float)
			jsonObject.addProperty(key, (Float)value);
		
		else if (value instanceof DataObjectSM)
			jsonObject.add(key, ((DataObjectSM)value).jsonObject);
//		else if (value instanceof byte[])
//			obj.addProperty(key, (byte[])value);
		else
			throw new IllegalArgumentException(key+":"+value);
	}

	String getId() {
		final String id_key = id_key();
		JsonElement jsonElement = jsonObject.get(id_key);
		if (jsonElement == null)
			return null;
		else
			return jsonElement.getAsString();
	}

	protected String id_key() {
		return className.toLowerCase() + "_id";
	}

	void setId(String id) {
		put(id_key(), id);
	}

	@Override
	public Set<String> keySet() {
		final HashSet<String> hashSet = new HashSet<String>();
		final Set<Entry<String, JsonElement>> entrySet = jsonObject.entrySet();		
		for (Entry<String, JsonElement> entry : entrySet) {
			hashSet.add(entry.getKey());
		}
		return hashSet;
	}
}