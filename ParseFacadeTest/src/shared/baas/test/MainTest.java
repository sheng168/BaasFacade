package shared.baas.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import shared.baas.DataClassFacade;
import shared.baas.DataQuery;

public class MainTest {
	StackMobFacadeFactory ff = new StackMobFacadeFactory("37c7ebb3-b6c8-43c7-9bfa-79a5ed82fecc", "c19eae0d-993e-49c7-a430-a30643dff36c");
	final DataClassFacade<GameScore> f = ff.get(GameScore.class);
	static String id = "";
	
	public void setUp() throws Exception {
	}
	
	public void testSave() throws InterruptedException, ExecutionException {
//		final CountDownLatch latch = new CountDownLatch(1);
		
		final GameScore gs = f.create();
		gs.name("user");
		gs.score(100);
		
		gs.dataObject().save().get();
	}

	public void testDelete() throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);
		final GameScore gs = f.create();
//		gs.dataObject().setObjectId(id); //TODO
		gs.name("user");
//		gs.dataObject().deleteInBackground(new DoCallback() {
//			@Override
//			public void done() {
//				System.out.println("deleted:"+id);
//				latch.countDown();
//			}
//
//			@Override
//			public void error(Exception e) {
//				e.printStackTrace();
//				latch.countDown();
//			}
//		});
//		latch.await();
	}

	public void testQuery() {
		final DataQuery<GameScore> query = f.newQuery();
		query.equalTo().name("user");
		query.orderAsc().score();
		query.include().name();
//		query.find()
	}

}
