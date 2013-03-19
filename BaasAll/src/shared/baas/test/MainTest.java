package shared.baas.test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import shared.baas.DataClassFacade;
import shared.baas.DataQuery;
import shared.baas.DoCallback;
import shared.baas.ListCallback;

public class MainTest {
	StackMobFacadeFactory ff = new StackMobFacadeFactory("37c7ebb3-b6c8-43c7-9bfa-79a5ed82fecc", "c19eae0d-993e-49c7-a430-a30643dff36c");
	final DataClassFacade<GameScore> f = ff.get(GameScore.class);
	static String id = "";
	
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testSave() throws InterruptedException, ExecutionException {
		final CountDownLatch latch = new CountDownLatch(1);
		
		final GameScore gs = f.create();
		gs.name("user");
		gs.score(100);
		
		gs.dataObject().saveInBackground(new DoCallback() {
			@Override
			public void done() {
				try {
					id = gs.dataObject().getObjectId();
					System.out.println("saved:"+id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				latch.countDown();
			}

			@Override
			public void error(Exception e) {
				e.printStackTrace();
				latch.countDown();
			}
		});
		
		latch.await();
	}

	@Test
	public void testDelete() throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);
		final GameScore gs = f.create();
		gs.dataObject().setObjectId(id);
		gs.name("user");
		gs.dataObject().deleteInBackground(new DoCallback() {
			@Override
			public void done() {
				System.out.println("deleted:"+id);
				latch.countDown();
			}

			@Override
			public void error(Exception e) {
				e.printStackTrace();
				latch.countDown();
			}
		});
		latch.await();
	}

	@Ignore
	@Test
	public void testQuery() {
		final DataQuery<GameScore> query = f.newQuery();
		query.equalTo().name("user");
		query.orderAsc().score();
		query.include().name();
		query.findInBackground(new ListCallback<GameScore>() {
			
			@Override
			public void error(Exception e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void done(List<GameScore> list) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
