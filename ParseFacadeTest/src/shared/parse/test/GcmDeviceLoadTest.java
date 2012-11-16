package shared.parse.test;

import java.util.ArrayList;
import java.util.List;

import shared.parse.ListCallback;
import shared.parse.ParseFacade;
import shared.parse.ParseFacade.Query;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

public class GcmDeviceLoadTest extends BaseParseTestCase {
	private static final int COUNT = 1000;
	
	public void _testInsertN() throws ParseException {
		ParseFacade<GcmDevice> pof = ParseFacade.get(GcmDevice.class);
		for (int i = 0; i < COUNT/1000; i++) {
			insert(pof, i);
		}
	}

	public void testInsertBatch() throws ParseException {
		ParseFacade<GcmDevice> pof = ParseFacade.get(GcmDevice.class);
		
		Query<GcmDevice> query = pof.query();
		query.orderDesc().count();
		List<GcmDevice> rs = query.find();
		
		query.findInBackground(new ListCallback<GcmDevice>() {
			@Override
			public void error(ParseException e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void done(List<GcmDevice> list) {
				// TODO Auto-generated method stub
				
			}
		});
		
		int start;
		if (rs.size() > 0) {
			start = rs.get(0).count() + 1;
		} else {
			start = 0;
		}
		
		List<ParseObject> list = new ArrayList<ParseObject>();
		
		int batch = 100;
		for (int i = 0; i < COUNT; i++) {
			GcmDevice g = init(pof, i + start);
			
			list.add(g.parseObject());
			
			if (list.size() >= batch) {
				try {
					ParseObject.saveAll(list);
				} catch (Exception e) {
					e.printStackTrace();
				}
				list.clear();
				System.out.printf("inserted %d at %d\n", batch, i);
			}
		}
		System.out.println("done");
	}

	public void testInsert() throws ParseException {
		ParseFacade<GcmDevice> pof = ParseFacade.get(GcmDevice.class);
		
		
		int i = 0;
		insert(pof, i);
//		String id = po.getObjectId();
//		
//		ParseObject po2 = pof.query().parseQuery().get(id);
//		TestObject to2 = pof.wrap(po2);
//		
//		String n = to2.stringField();
//		assertEquals(name, n);
//		int int1 = to2.intField();
//		float float1 = to2.floatField();
//		Long long1 = to2.longField();
////		Date createdAt = to2.createdAt();
////		Date updatedAt = to2.updatedAt();
//		to2.byteField();
//		
//		assertNotNull(id);
////		to2.objectId(id);
//		
////		System.out.printf("%d %f %d", int1, float1, long1);
	}

	private void insert(ParseFacade<GcmDevice> pof, int i)
			throws ParseException {
		GcmDevice to = init(pof, i);
		
		to.parseObject().save();
		System.out.println(i);
	}

	private GcmDevice init(ParseFacade<GcmDevice> pof, int i) {
		GcmDevice to = pof.create();
		String name = i+"_jinxyu@gmail.com";
		to.email(name);
		to.regId(i+"_APA91bEzGAvf9cgiIrfJNAYZRBWjheMRq0YdDGQlCt3_ZeZVn2_mJoYYBlCKc3RlaX7V5-WkbLw_sfDSmLlBQOBLpMYarvr7lynvZrugzigAjzF7_IKtZUyL_w8sjVspOeOnsofnwUfd");
		to.batteryLevel(i);
		to.count(i);
		to.enable(true);
		
		int l = i % 10;
		to.center(new ParseGeoPoint(l, l));
		return to;
	}
}
