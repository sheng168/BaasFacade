package shared.baas.keyvalue.firebase;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.DataObjectQuery;
import shared.baas.keyvalue.ListenableFuture;
import shared.baas.keyvalue.ListenableFuture.Basic;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

class DataObjectQuery_ extends DataObjectQuery {
	Logger log = LoggerFactory.getLogger(DataObjectQuery_.class);
	
//	ParseQuery query;
	Query query;

	public DataObjectQuery_(DataObjectFactory_ factory, String className) {
		super();
		this.query = factory.root.child(className);
		//new ParseQuery(className);		
	}

	// equality
	@Override
	public DataObjectQuery whereEqualTo(String key, Object value) {
//		query.whereEqualTo(key, value);
		log.warn("not supported ");
		return this;
	}

	@Override
	public DataObjectQuery whereNotEqualTo(String key, Object value) {
//		query.whereNotEqualTo(key, value);
		throw new UnsupportedOperationException("not supported ");
	}

	// inequality
	@Override
	public DataObjectQuery_ whereGreaterThan(String key, Object value) {
		if (DataObject.UPDATED_AT.equals(key)) {
			query = query.startAt(((Number)value).doubleValue());
		} else {
			log.warn("not supported ");
		}
//		query.whereGreaterThan(key, value);

		return this;
	}

	@Override
	public DataObjectQuery whereLessThan(String key, Object value) {
		if (DataObject.UPDATED_AT.equals(key)) {
			if (value instanceof Number)
				query = query.endAt(((Number)value).doubleValue());
			else if (value instanceof String)
				query = query.endAt((String)value);
			else
				log.error("number expected {}", value);
		} else {
			log.warn("not supported ");
		}
//		query.whereGreaterThan(key, value);

		return this;
	}

	// sorting
	@Override
	public DataObjectQuery orderByAsc(String key) {
//		query.addAscendingOrder(key);
		throw new UnsupportedOperationException("not supported ");
	}

	@Override
	public DataObjectQuery orderByDesc(String key) {
//		query.addDescendingOrder(key);
		throw new UnsupportedOperationException("not supported ");
	}

	// paging
	@Override
	public DataObjectQuery isInRange(int newSkip, int newLimit) {
		log.warn("not supported limit skip");
		query = query.limit(newLimit);
		
//		query.setSkip(newSkip);
//		query.setLimit(newLimit);
		return this;
	}
	
	@Override
	public ListenableFuture<List<DataObject>> find() {
		final Basic<List<DataObject>> future = new ListenableFuture.Basic<List<DataObject>>();
		
		query.addListenerForSingleValueEvent(new ValueEventListener() {			
			@Override
			public void onDataChange(DataSnapshot data) {
				Iterable<DataSnapshot> children = data.getChildren();
				
				final ArrayList<DataObject> result = new ArrayList<DataObject>();
				for (DataSnapshot child : children) {
					result.add(new DataObject_(child));
				}
				
				future.set(result, null);
			}
			
			@Override
			public void onCancelled(FirebaseError fe) {
				future.set(null, fe.toException());
			}
		});
		
//		query.findInBackground(new FindCallback() {
//			@Override
//			public void done(List<ParseObject> l, ParseException e) {
//				if (e == null) {
//					future.set(wrap(l), null);
//				} else {
//					future.set(null, e);
//				}
//			}
//
//			private List<DataObject> wrap(List<ParseObject> l) {
//				final ArrayList<DataObject> result = new ArrayList<DataObject>();
//				for (ParseObject parseObject : l) {
//					result.add(new DataObject(parseObject));
//				}
//				return result;
//			}
//		});
		
		return future;
	}
}
