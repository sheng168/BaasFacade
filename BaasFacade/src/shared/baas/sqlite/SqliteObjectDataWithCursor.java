package shared.baas.sqlite;


import android.database.Cursor;

class SqliteObjectDataWithCursor extends SqliteObjectData {
	Cursor cursor;
	int position;
	
//	public SqliteObjectDataWithCursor(SqliteFacadeFactory factory, String className) {
//		super(factory, className);
//
//	}

	
	public SqliteObjectDataWithCursor(SqliteFacadeFactory factory,
			String className, Cursor cursor, int position) {
		super(factory, className);
		this.cursor = cursor;
		this.position = position;
	}

	
	@Override
	public <T> T get(String key, Class<T> type) {
		Object object = super.get(key, type);
		
		if (object == null) {
			cursor.moveToPosition(position);
			final int columnIndex = cursor.getColumnIndexOrThrow(key);
			
			if (type.isAssignableFrom(String.class))
				object = cursor.getString(columnIndex);
			else if (type.isAssignableFrom(Boolean.class))
				object = cursor.getInt(columnIndex) != 0;
			else if (type.isAssignableFrom(Integer.class))
				object = cursor.getInt(columnIndex) != 0;
			else
				throw new UnsupportedOperationException("don't know how to get value of type: " + type);
		}
		System.out.println(object);
		return (T) object;
	}

	@Override
	public String toString() {
		return super.toString() + values;
	}
}