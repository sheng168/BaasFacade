package shared.baas.keyvalue.parse;

import java.util.ArrayList;
import java.util.List;

import shared.baas.ListCallback;
import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.DataObjectQuery;
import shared.baas.keyvalue.ListenableFuture;
import shared.baas.keyvalue.ListenableFuture.Basic;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class DataObjectQueryParse implements DataObjectQuery {
	ParseQuery query;

	public DataObjectQueryParse(String className) {
		super();
		this.query = new ParseQuery(className);
		
	}

	// equality
	@Override
	public void whereEqualTo(String key, Object value) {
		query.whereEqualTo(key, value);
	}

	@Override
	public void whereNotEqualTo(String key, Object value) {
		query.whereNotEqualTo(key, value);
	}

	// inequality
	@Override
	public DataObjectQueryParse whereGreaterThan(String key, Object value) {
		query.whereGreaterThan(key, value);
		return this;
	}

	// sorting
	@Override
	public void orderByAsc(String key) {
		query.addAscendingOrder(key);
	}

	@Override
	public void orderByDesc(String key) {
		query.addDescendingOrder(key);
	}

	// paging
	@Override
	public void isInRange(int newSkip, int newLimit) {
		query.setSkip(newSkip);
		query.setLimit(newLimit);
	}

//	@Override
	public void findInBackground(final ListCallback<DataObject> callback) {
		query.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> l, ParseException e) {
				if (e == null) {
					callback.done(null);
				} else {
					callback.error(e);
				}
			}
		});
	}
	
	@Override
	public ListenableFuture<List<DataObject>> find() {
		final Basic<List<DataObject>> future = new ListenableFuture.Basic<List<DataObject>>();
		
		query.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> l, ParseException e) {
				if (e == null) {
					future.set(wrap(l), null);
				} else {
					future.set(null, e);
				}
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
