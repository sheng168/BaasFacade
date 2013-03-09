package shared.baas.impl;

import java.util.Date;

import shared.baas.DataObject;

public abstract class AbstractObjectData<T> implements DataObject {

//	public AbstractObjectData() {
//		super();
//	}

	@Override
	public void setObjectId(String newObjectId) {
		put("objectId", newObjectId);
	}

	@Override
	public String getObjectId() {
		return (String) get("objectId", String.class);
	}

	@Override
	public Date getUpdatedAt() {
		return (Date) get("updatedAt", Date.class);
	}

	@Override
	public Date getCreatedAt() {
		return (Date) get("createdAt", Date.class);
	}

}