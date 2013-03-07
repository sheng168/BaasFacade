package shared.parse;

import java.util.Date;

import shared.baas.DoCallback;
import shared.baas.FacadeFactory;
import shared.baas.ObjectData;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class ParseObjectData implements ObjectData {
	ParseObject parse;
	
	public ParseObjectData(ParseObject parse) {
		this.parse = parse;
	}
//	String objectId();
//	void objectId(String in);
//	
//	Date createdAt();
//	Date updatedAt();
//	
//	void saveAsync(GetCallback<? extends ObjectBase> getCallback);
//	void deleteAsync(GetCallback<? extends ObjectBase> getCallback);

	/* (non-Javadoc)
	 * @see shared.baas.DataObject#deleteInBackground(com.parse.DeleteCallback)
	 */
	@Override
	public void deleteInBackground(final DoCallback callback) {		
		parse.saveInBackground(new SaveCallback() {			
			@Override
			public void done(ParseException e) {
				callback.done(e);
			}
		});
	}

	/* (non-Javadoc)
	 * @see shared.baas.DataObject#getClassName()
	 */
//	@Override
//	public String getClassName() {
//		
//		return parse.getClassName();
//	}

	/* (non-Javadoc)
	 * @see shared.baas.DataObject#getCreatedAt()
	 */
	@Override
	public Date getCreatedAt() {
		
		return parse.getCreatedAt();
	}

	/* (non-Javadoc)
	 * @see shared.baas.DataObject#getObjectId()
	 */
	@Override
	public String getObjectId() {
		
		return parse.getObjectId();
	}

	/* (non-Javadoc)
	 * @see shared.baas.DataObject#getUpdatedAt()
	 */
	@Override
	public Date getUpdatedAt() {
		
		return parse.getUpdatedAt();
	}

	/* (non-Javadoc)
	 * @see shared.baas.DataObject#isDataAvailable()
	 */
//	@Override
//	public boolean isDataAvailable() {
//		
//		return parse.isDataAvailable();
//	}

	/* (non-Javadoc)
	 * @see shared.baas.DataObject#refreshInBackground(com.parse.RefreshCallback)
	 */
	@Override
	public void refreshInBackground(final DoCallback callback) {		
		parse.saveInBackground(new SaveCallback() {			
			@Override
			public void done(ParseException e) {
				callback.done(e);
			}
		});
	}

	/* (non-Javadoc)
	 * @see shared.baas.DataObject#saveInBackground(com.parse.SaveCallback)
	 */
	@Override
	public void saveInBackground(final DoCallback callback) {		
		parse.saveInBackground(new SaveCallback() {			
			@Override
			public void done(ParseException e) {
				callback.done(e);
			}
		});
	}

	@Override
	public void setObjectId(String newObjectId) {		
		parse.setObjectId(newObjectId);
	}

	@Override
	public Object get(String key, Class<?> type) {
		return parse.get(key);
	}

	@Override
	public void put(String key, Object value) {
		parse.put(key, value);
	}

	@Override
	public FacadeFactory getFactory() {
		return new ParseFacadeFactory();
	}

}
