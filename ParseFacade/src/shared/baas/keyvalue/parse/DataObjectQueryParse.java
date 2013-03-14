package shared.baas.keyvalue.parse;

import java.util.List;

import shared.baas.ListCallback;
import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.DataObjectQuery;

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
	public void whereGreaterThan(String key, Object value) {
		query.whereGreaterThan(key, value);
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

	@Override
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
}
