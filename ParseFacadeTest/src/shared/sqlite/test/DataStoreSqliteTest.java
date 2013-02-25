package shared.sqlite.test;

import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import shared.parse.test.BaseParseTestCase;
import shared.sqlite.DataStoreSqlite;
import shared.sqlite.DatabaseHelper;
import junit.framework.TestCase;

public class DataStoreSqliteTest extends BaseParseTestCase {
	DataStoreSqlite dss;
	final String table = "GameScore";

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		
		final Context context = this.getContext();
		assertNotNull(context);
		
		dss = new DataStoreSqlite(new DatabaseHelper(context));
	}

	public void testSave() throws ParseException {
		ParseQuery q = new ParseQuery(table);
		final List<ParseObject> list = q.find();
		
		for (ParseObject parseObject : list) {
			final ContentValues values = convert(parseObject);
			dss.save(table, values);
		}
	}

	private ContentValues convert(ParseObject parseObject) {
		final ContentValues values = new ContentValues();
		values.put("objectId", parseObject.getObjectId());
		values.put("updatedAt", parseObject.getUpdatedAt().getTime());
		values.put("createdAt", parseObject.getCreatedAt().getTime());

		final Set<String> keySet = parseObject.keySet();
		
		for (String key : keySet) {
			Object v = parseObject.get(key);
			
			if (v instanceof String)
				values.put(key, (String)v);
			else if (v instanceof Integer)
				values.put(key, (Integer)v);
			else if (v instanceof Long)
				values.put(key, (Long)v);
			else if (v instanceof byte[])
				values.put(key, (byte[])v);
			else if (v instanceof Float)
				values.put(key, (Float)v);
			else if (v instanceof Double)
				values.put(key, (Double)v);
			else
				fail("unknown type:" + v.getClass());
		}
		
		return values;
	}

	public void testDelete() {
		final Cursor c = dss.query(table, null, null, null);
		final int i = c.getColumnIndexOrThrow("objectId");
		
		for (int j = 0; j < c.getCount(); j++) {
			c.moveToPosition(j);
			
			if (c.isNull(i)) {
				
			} else {
				final String id = c.getString(i);
				assertNotNull(id);
				
				dss.delete(table, id);
			}

		}
	}

	public void testQuery() {
		final Cursor c = dss.query(table, null, null, null);
		
		final int i = c.getColumnIndexOrThrow("objectId");
		
		for (int j = 0; j < c.getCount(); j++) {
			c.moveToPosition(j);
			
			if (c.isNull(i)) {
				
			} else {
				final String id = c.getString(i);
				assertNotNull(id);
			}

		}
	}

}
