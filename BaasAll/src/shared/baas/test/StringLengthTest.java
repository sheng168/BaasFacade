package shared.baas.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;

import shared.baas.DataClassFacade;
import shared.baas.DoCallback;

public class StringLengthTest {
	StackMobFacadeFactory ff = new StackMobFacadeFactory("37c7ebb3-b6c8-43c7-9bfa-79a5ed82fecc", "c19eae0d-993e-49c7-a430-a30643dff36c");
	final DataClassFacade<GameScore> f = ff.get(GameScore.class);
	static String id = "";
	
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testSave() throws InterruptedException, ExecutionException {
		final CountDownLatch latch = new CountDownLatch(0);

		String name = "user";
		
		for (int i = 0; i < 24; i++) {
			name = name+name;
			
			final GameScore gs = f.create();
			gs.name(name);
			final int length = name.length();
			gs.score(length);
			
			gs.dataObject().save().get();

//			Thread.sleep(1000);
		}
		
		latch.await();
	}
}
