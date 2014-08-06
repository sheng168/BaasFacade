package shared.baas.keyvalue;

import java.util.List;



public abstract class DataObjectQuery {

	public abstract DataObjectQuery whereEqualTo(String key, Object value);

	public abstract DataObjectQuery whereNotEqualTo(String key, Object value);

	public abstract DataObjectQuery whereGreaterThan(String key, Object value);

	public abstract DataObjectQuery orderByAsc(String key);

	public abstract DataObjectQuery orderByDesc(String key);

	public abstract DataObjectQuery isInRange(int skip, int count);

	public abstract ListenableFuture<List<DataObject>> find();

	public DataObjectQuery include(String name) {
		return this;
	}

	public DataObjectQuery whereLessThan(String key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

//	void findInBackground(ListCallback<DataObject> callback);

}