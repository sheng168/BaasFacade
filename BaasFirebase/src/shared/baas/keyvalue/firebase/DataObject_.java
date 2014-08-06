package shared.baas.keyvalue.firebase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import shared.baas.DoCallback;
import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.ListenableFuture;
import shared.baas.keyvalue.ListenableFuture.Basic;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.firebase.client.snapshot.EmptyNode;

class DataObject_ implements DataObject {
	private DataSnapshot data;
	HashMap<String, Object> pendingUpdate = new HashMap<String, Object>();

	public DataObject_(DataSnapshot data) {
		this.data = data;
	}

	public DataObject_(Firebase root, String className) {		
		this(new DataSnapshot(root.child(className).push(), EmptyNode.Empty()));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key, Class<T> type) {
		if (OBJECT_ID.equals(key))
			return (T) data.getRef().getName();
//		else if (UPDATED_AT.equals(key))
//			return (T)new Long(((Number)data.getPriority()).longValue()); // obj.getUpdatedAt() == null?null:(T) Long.valueOf(obj.getUpdatedAt().getTime());		
//		else if (CREATED_AT.equals(key))
//			return obj.getCreatedAt() == null?null:(T) Long.valueOf(obj.getCreatedAt().getTime());
		else
			return (T) data.child(key).getValue(type);
	}

	@Override
	public DataObject put(String key, Object value) {
		// TODO
//		if (OBJECT_ID.equals(key))
//			obj.setObjectId((String) value);
//		else
			pendingUpdate.put(key, value); //TODO reject updatedAt here?
			
		return this;
	}

	@Override
	public Set<String> keySet() {
		HashSet<String> hashSet = new HashSet<String>();
		
		for (DataSnapshot d : data.getChildren()) {
			hashSet.add(d.getName());
		}
		
		return hashSet; //java.util.Collections.emptySet();
//		return obj.keySet();
	}
	
	@Override
	public void refreshInBackground(final DoCallback callback) {
		data.getRef().addListenerForSingleValueEvent(new ValueEventListener() {			
			@Override
			public void onDataChange(DataSnapshot data) {
				DataObject_.this.data = data;
				callback.done();
			}
			
			@Override
			public void onCancelled(FirebaseError e) {
				callback.error(e.toException());
			}
		});
		
//		obj.refreshInBackground(new RefreshCallback() {
//			@Override
//			public void done(ParseObject o, ParseException e) {
//				if (e == null) {
//					callback.error(e);
//				} else {
//					callback.done();
//				}
//			}
//		});
	}

	@Override
	public ListenableFuture<String> save() {
		final Basic<String> future = new ListenableFuture.Basic<String>();
		pendingUpdate.put(UPDATED_AT, ServerValue.TIMESTAMP);
		data.getRef().setValue(pendingUpdate, ServerValue.TIMESTAMP, new Firebase.CompletionListener() {			
			@Override
			public void onComplete(FirebaseError e, Firebase fb) {
				if (e == null) {
					future.set(fb.getName(), null);
				} else {
					future.set(null, e.toException());
				}

			}
		});
		
//		obj.saveInBackground(new SaveCallback() {			
//			@Override
//			public void done(ParseException e) {
//				if (e == null) {
//					future.set(obj.getObjectId(), e);
//				} else {
//					future.set(null, e);
//				}
//			}
//		});
		
		return future;
	}

	@Override
	public void deleteInBackground(final DoCallback callback) {
		data.getRef().removeValue(new Firebase.CompletionListener() {			
			@Override
			public void onComplete(FirebaseError e, Firebase fb) {
				if (e == null) {
					callback.done();
				} else {
					callback.error(e.toException());
				}
			}
		});
//		obj.deleteInBackground(new DeleteCallback() {			
//			@Override
//			public void done(ParseException e) {
//				if (e == null) {
//					callback.error(e);
//				} else {
//					callback.done();
//				}
//			}
//		});
	}

	@Override
	public String toString() {
		return "DataObject_ [data=" + data.getValue()
				+ ", pendingUpdate=" + pendingUpdate + "]";
	}
}
