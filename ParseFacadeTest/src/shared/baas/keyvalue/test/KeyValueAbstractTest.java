package shared.baas.keyvalue.test;

import java.util.concurrent.ExecutionException;

import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.DataObjectFactory;
import android.test.AndroidTestCase;

public abstract class KeyValueAbstractTest extends AndroidTestCase {

	protected abstract DataObjectFactory getFactory(); // testing different implementation

	public void testDataObject() throws InterruptedException, ExecutionException {
		DataObjectFactory factory2 = getFactory();
		
		DataObject object = factory2.createDataObject("GameScore");
		object.put("score", 1338);
		object.save().get();
		
//		DataObjectQuery query = factory2.createDataObjectQuery("GameScore");
//		query.whereEqualTo("score", 1338);
//		List<DataObject> list = query.find().get();
//		assertEquals(1, list.size());
	}

}