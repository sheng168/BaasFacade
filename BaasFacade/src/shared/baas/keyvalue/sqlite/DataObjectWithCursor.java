package shared.baas.keyvalue.sqlite;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class DataObjectWithCursor extends DataObjectSqlite {
	Cursor cursor;
	int position;
	
	public DataObjectWithCursor(ContentResolver cr, Uri baseUri,
			String className, Cursor cursor, int position) {
		super(cr, baseUri, className);
		this.cursor = cursor;
		this.position = position;
	}

	@SuppressWarnings("unchecked")
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
		
		return (T) object;
	}

}
