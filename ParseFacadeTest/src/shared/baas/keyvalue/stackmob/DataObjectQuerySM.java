package shared.baas.keyvalue.stackmob;

import java.util.ArrayList;
import java.util.List;

import shared.baas.ListCallback;
import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.DataObjectQuery;
import shared.baas.keyvalue.ListenableFuture;
import shared.baas.keyvalue.ListenableFuture.Basic;
import shared.baas.keyvalue.parse.DataObjectParse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.parse.ParseObject;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMobDatastore;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.api.StackMobQuery.Ordering;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;
//import shared.baas.DataObject;

public class DataObjectQuerySM extends DataObjectQuery {
	String className;
//	private String id;
//	Map<String, Object> obj = new HashMap<String, Object>();
	StackMobQuery query;

	public DataObjectQuerySM(String className) {
		super();
		this.className = className;
		query = new StackMobQuery(className);
	}

	protected StackMobDatastore dataStore() {
		return StackMob.getStackMob().getDatastore();
	}

	@Override
	public DataObjectQuery whereEqualTo(String key, Object value) {
		query.fieldIsEqualTo(key, value.toString());
		return this;
	}

	@Override
	public DataObjectQuery whereNotEqualTo(String key, Object value) {
		query.fieldIsNotEqual(key, value.toString());
		return this;
	}

	@Override
	public DataObjectQuery whereGreaterThan(String key, Object value) {
		query.fieldIsGreaterThan(key, value.toString());
		return this;
	}

	@Override
	public DataObjectQuery orderByAsc(String key) {
		query.fieldIsOrderedBy(key, Ordering.ASCENDING);
		return this;
	}

	@Override
	public DataObjectQuery orderByDesc(String key) {
		query.fieldIsOrderedBy(key, Ordering.DESCENDING);
		return this;
	}

	@Override
	public DataObjectQuery isInRange(int skip, int count) {
		query.isInRange(skip, skip + count - 1);
		return this;
	}

//	@Override
	public void findInBackground(final ListCallback<DataObject> callback) {
		dataStore().get(query, new StackMobOptions(), new StackMobCallback() {			
			@Override
			public void success(String responseBody) {
				// TODO Auto-generated method stub
				callback.done(null);
			}
			
			@Override
			public void failure(StackMobException e) {
				callback.error(e);
			}
		});
	}

	@Override
	public ListenableFuture<List<DataObject>> find() {
		final Basic<List<DataObject>> future = new ListenableFuture.Basic<List<DataObject>>();

		dataStore().get(query, new StackMobOptions(), new StackMobCallback() {			
			@Override
			public void success(String responseBody) {
				final ArrayList<DataObject> result = new ArrayList<DataObject>();
				
				final JsonElement json = new JsonParser().parse(responseBody);
				if (json.isJsonArray()) {
					final JsonArray jsonArray = json.getAsJsonArray();
					for (int i = 0; i < jsonArray.size(); i++) {
						result.add(new DataObjectSM(className, jsonArray.get(i).getAsJsonObject()));
					}
				} else {
					future.set(null, new Exception("expecting list"));
				}
				future.set(result, null);
			}
			
			@Override
			public void failure(StackMobException e) {
				future.set(null, e);
			}
			
			private List<DataObject> wrap(List<ParseObject> l) {
				final ArrayList<DataObject> result = new ArrayList<DataObject>();
				for (ParseObject parseObject : l) {
					result.add(new DataObjectParse(parseObject));
				}
				return result;
			}
		});
		
		return future;
	}

}