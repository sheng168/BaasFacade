package shared.baas.keyvalue.firebase.test;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutionException;

import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.DataObjectFactory;
import shared.baas.keyvalue.firebase.DataObjectFactory_;

import com.firebase.client.Firebase;

public class TestDataObjectFactory_ {
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		DataObjectFactory factory = new DataObjectFactory_(
				new Firebase("https://abeona-dev.firebaseio.com/syncedValue/class"));
		
		for (int i = 0; i < 3; i++) {
			DataObject createDataObject = factory.createDataObject("Test");
			createDataObject.put("dateString", ""+new Timestamp(System.currentTimeMillis()));
			String s = createDataObject.save().get();
			System.out.println(s);
		}
		
		List<DataObject> list = factory.createDataObjectQuery("Test")
				.whereGreaterThan(DataObject.UPDATED_AT, System.currentTimeMillis() - 10000)
				.isInRange(0, 5)
				.find().get();
		
		System.out.println(list);
		System.out.println(list.size());
		
		Address a = Address.facade.create();
		a.address("Test");
		a.active(true);
		a.radius(234);
		System.out.println(a.dataObject().save().get());
		
		Thread.sleep(1000);
	}
}