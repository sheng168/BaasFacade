package shared.baas.test;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import junit.framework.TestCase;
import shared.baas.DataClassFacade;

import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;

public class StackMobTest extends TestCase {
	StackMobFacadeFactory ff = new StackMobFacadeFactory("37c7ebb3-b6c8-43c7-9bfa-79a5ed82fecc", "c19eae0d-993e-49c7-a430-a30643dff36c");
	final DataClassFacade<GameScore> f = ff.get(GameScore.class);
	CountDownLatch latch;
	
	@Override
	public void setUp() throws Exception {
		latch = new CountDownLatch(1);
	}
	
	public void testQuery() throws InterruptedException, ExecutionException {
		StackMob.getStackMob().getDatastore().get(new StackMobQuery("gamescore").isInRange(0, 1).fieldIsEqualTo("gamescore_id", "0ba61abb1b9144dc84bfe5511f2a3286"), new StackMobCallback() {
			@Override
			public void failure(StackMobException e) {
				e.printStackTrace();
			}

			@Override
			public void success(String s) {
				System.out.println("query:"+s);
			}
		});
		
		Thread.sleep(1000);
	}

	public void testDelete() throws InterruptedException, ExecutionException {
		StackMob.getStackMob().getDatastore().delete("gamescore", "0ba61abb1b9144dc84bfe5511f2a3286", new StackMobCallback() {
			@Override
			public void failure(StackMobException e) {
				e.printStackTrace();
			}

			@Override
			public void success(String s) {
				System.out.println("delete:"+s);
			}
		});
		
		Thread.sleep(1000);
	}

	public void testGet() throws InterruptedException, ExecutionException {
		StackMob.getStackMob().getDatastore().get("gamescore", new StackMobCallback() {
			@Override
			public void failure(StackMobException e) {
				e.printStackTrace();
			}

			@Override
			public void success(String s) {
				System.out.println(s);
			}
		});
		
		Thread.sleep(1000);
	}

	public void testMap() throws InterruptedException, ExecutionException {
		
		
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", "jin");
		map.put("radius", 1.2);
		map.put("active", false);
		
		StackMob.getStackMob().getDatastore().post("gamescore", map, new StackMobCallback() {
			@Override
			public void failure(StackMobException e) {
				e.printStackTrace();
			}

			@Override
			public void success(String s) {
				System.out.println(s);
			}
		});
		
		Thread.sleep(1000);
	}
	
	public void testJsonString() throws InterruptedException, ExecutionException {
		StackMob.getStackMob().getDatastore().post("gamescore", "{\n" + 
				"  \"name\": \"sheng\",\n" + 
				"  \"score\": 100\n" + 
				"}", new StackMobCallback() {
			@Override
			public void failure(StackMobException e) {
				e.printStackTrace();
			}

			@Override
			public void success(String s) {
				System.out.println(s);
			}
		});
		
		Thread.sleep(1000);
		
	}
}
