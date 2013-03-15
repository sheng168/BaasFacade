package shared.baas.keyvalue.stackmob;

import java.util.ArrayList;
import java.util.List;

import shared.baas.ListCallback;
import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.DataObjectQuery;
import shared.baas.keyvalue.ListenableFuture;
import shared.baas.keyvalue.ListenableFuture.Basic;
import shared.baas.keyvalue.parse.DataObjectParse;

import com.parse.ParseObject;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMobDatastore;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.api.StackMobQuery.Ordering;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;
//import shared.baas.DataObject;

public class DataObjectQuerySM implements DataObjectQuery {
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
	public void whereEqualTo(String key, Object value) {
		query.fieldIsEqualTo(key, value.toString());
	}

	@Override
	public void whereNotEqualTo(String key, Object value) {
		query.fieldIsNotEqual(key, value.toString());
	}

	@Override
	public DataObjectQuery whereGreaterThan(String key, Object value) {
		query.fieldIsGreaterThan(key, value.toString());
		return this;
	}

	@Override
	public void orderByAsc(String key) {
		query.fieldIsOrderedBy(key, Ordering.ASCENDING);
	}

	@Override
	public void orderByDesc(String key) {
		query.fieldIsOrderedBy(key, Ordering.DESCENDING);
	}

	@Override
	public void isInRange(int skip, int count) {
		query.isInRange(skip, skip + count - 1);
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
				// TODO parse responseBody
				future.set(new ArrayList<DataObject>(), null);
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