package shared.sqlite.test;

import shared.parse.test.BaseParseTestCase;
import shared.sqlite.DatabaseHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SqliteTest extends BaseParseTestCase {
	private DatabaseHelper db;

	private void testSqlite() {
		try {
			db = new DatabaseHelper(this.getContext());
			ContentValues values = new ContentValues();
			values.put("name", "test");
			final int size = 1024*64;
			values.put("data", new byte[size]);
			values.put("size", size);
			final SQLiteDatabase wdb = db.getWritableDatabase();
			wdb.insert("file", null, values);
			
			wdb.compileStatement("select * from file");
			
			String table = "file";
			String[] columns = null;
			String selection = null;
			String[] selectionArgs = null;
			String groupBy = null;
			String having = null;
			String orderBy = null;
			String limit = null;
			final Cursor c = wdb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
			System.out.println("rows:"+c.getCount());
			System.out.println("cols:"+c.getColumnCount());
			
//			for (int i = 0; i < c.getCount(); i++) {
//				c.moveToPosition(i);
//				for (int j = 0; j < c.getColumnCount(); j++) {
//					final int type = c.getType(j);
//					
//					String name = c.getColumnName(j);
//					String s;
//					switch (type) {
//					case Cursor.FIELD_TYPE_BLOB:
//						s = ""+ c.getBlob(j).length;
//						break;
//					default:
//						s = c.getString(j);
//						break;
//					}
//	
//					System.out.println(name + ":" + s);
//				}
//			}
			
			final int update = wdb.update("file", values, null, null);
			System.out.println("update:"+update);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

