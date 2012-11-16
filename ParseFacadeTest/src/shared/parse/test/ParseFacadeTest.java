package shared.parse.test;

import java.util.ArrayList;
import java.util.Date;

import shared.parse.ParseFacade;
import android.content.Context;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ParseFacadeTest extends BaseParseTestCase {
	public void testParseNative() throws ParseException {
		ParseObject to = new ParseObject("TestObject");
		to.put("stringField", "name");
		to.put("intField", 1987);
		to.put("longField", 123456789012345L);
		to.put("floatField", 1987.1f);
		to.put("booleanField", true);
		to.put("dateField", new Date());
		to.put("byteField", new byte[4]);
		to.put("listField", new ArrayList<Object>());
		
		to.save();

		String id = to.getObjectId();
		to.put("objectId", id);

		ParseQuery q = new ParseQuery("TestObject");
		ParseObject po = q.get(id);
		int int1 = po.getInt("longField");
		long long1 = po.getLong("longField_");
		byte[] bytes = po.getBytes("byteField");
		
		System.out.printf("%d %d %d", int1, long1, bytes.length);
	}
	
	public void testFacade() throws ParseException {
		ParseFacade<TestObject> pof = ParseFacade.get(TestObject.class);
		TestObject to = pof.create();
		
		String name = "type-safe";
		to.stringField(name);
		to.floatField(1987.1015f);
		to.intField(1987);
		to.longField(1234567890123456L);
		to.byteField(new byte[9]);
		
		ParseObject po = to.parseObject();
		po.save();
		String id = po.getObjectId();
		
		ParseObject po2 = pof.query().parseQuery().get(id);
		TestObject to2 = pof.wrap(po2);
		
		String n = to2.stringField();
		assertEquals(name, n);
		int int1 = to2.intField();
		float float1 = to2.floatField();
		Long long1 = to2.longField();
//		Date createdAt = to2.createdAt();
//		Date updatedAt = to2.updatedAt();
		to2.byteField();
		
		assertNotNull(id);
//		to2.objectId(id);
		
//		System.out.printf("%d %f %d", int1, float1, long1);
	}
}
