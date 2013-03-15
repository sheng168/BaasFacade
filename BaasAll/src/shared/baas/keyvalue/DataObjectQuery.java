package shared.baas.keyvalue;

import java.util.List;



public interface DataObjectQuery {

	void whereEqualTo(String key, Object value);

	void whereNotEqualTo(String key, Object value);

	DataObjectQuery whereGreaterThan(String key, Object value);

	void orderByAsc(String key);

	void orderByDesc(String key);

	void isInRange(int skip, int count);

	ListenableFuture<List<DataObject>> find();

//	void findInBackground(ListCallback<DataObject> callback);

}