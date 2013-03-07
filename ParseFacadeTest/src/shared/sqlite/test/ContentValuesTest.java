package shared.sqlite.test;

import java.util.Map.Entry;
import java.util.Set;

import com.parse.ParseObject;

import android.content.ContentValues;
import junit.framework.TestCase;

public class ContentValuesTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testTypes() {
		ContentValues v = new ContentValues();
		
		v.put("bool", true);
		v.put("byte", Byte.MAX_VALUE);
		v.put("short", Short.MAX_VALUE);
		v.put("int", Integer.MAX_VALUE);
		v.put("long", Long.MAX_VALUE);
		v.put("float", Float.MAX_VALUE);
		v.put("double", Double.MAX_VALUE);
		v.put("string", "helloworld");
		v.put("byte[]", new byte[8]);
		
//		v.putNull("empty");
//		assertTrue(v.containsKey("empty"));
//		assertNull(v.get("empty"));
		
		final ParseObject po = new ParseObject("ok");
		
		StringBuffer sb = new StringBuffer();
		final Set<Entry<String, Object>> valueSet = v.valueSet();
		for (Entry<String, Object> entry : valueSet) {
			final String key = entry.getKey();
			final Object value = entry.getValue();
			
			sb.append(key + ":" + value).append('\n');
			po.put(key, value);
		}
		sb.append("po:").append('\n');
		final Set<String> keySet = po.keySet();
		for (String key : keySet) {
//			final String key = entry.getKey();
			final Object value = po.get(key);
			
			final Object value2 = v.get(key);
			
			assertEquals(value, value2);
			assertEquals(value.getClass(), value2.getClass());
			sb.append(key + ":" + value).append('\n');

		}
		System.err.println(sb);
		fail(sb.toString());
	}
}
