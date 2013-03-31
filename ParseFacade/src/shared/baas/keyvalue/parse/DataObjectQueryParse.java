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

public class DataObjectQueryParse extends DataObjectQuery {
	ParseQuery query;

	public DataObjectQueryParse(String className) {
		super();
		this.query = new ParseQuery(className);		
	}

	// equality
	@Override
	public DataObjectQuery whereEqualTo(String key, Object value) {
		if (value instanceof DataObjectParse) {
			value = ((DataObjectParse)value).obj;
		}
			
		query.whereEqualTo(key, value);
		return this;
	}

	@Override
	public DataObjectQuery whereNotEqualTo(String key, Object value) {
		query.whereNotEqualTo(key, value);
		return this;
	}

	// inequality
	@Override
	public DataObjectQueryParse whereGreaterThan(String key, Object value) {
		query.whereGreaterThan(key, value);
		return this;
	}

	// sorting
	@Override
	public DataObjectQuery orderByAsc(String key) {
		query.addAscendingOrder(key);
		return this;
	}

	@Override
	public DataObjectQuery orderByDesc(String key) {
		query.addDescendingOrder(key);
		return this;
	}

	// paging
	@Override
	public DataObjectQuery isInRange(int newSkip, int newLimit) {
		query.setSkip(newSkip);
		query.setLimit(newLimit);
		return this;
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

	@Override
	public DataObjectQuery include(String name) {
		query.include(name);
		return super.include(name);
	}
}
