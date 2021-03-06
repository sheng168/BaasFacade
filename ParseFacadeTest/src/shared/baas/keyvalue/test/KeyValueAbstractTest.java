package shared.baas.keyvalue.test;

import java.util.List;
import java.util.concurrent.ExecutionException;

import shared.baas.DataClassFacade;
import shared.baas.DataQuery;
import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.DataObjectFactory;
import shared.baas.keyvalue.DataObjectQuery;
import shared.baas.keyvalue.ListenableFuture;
import android.test.AndroidTestCase;
//import shared.parse.test.GameScore;

public abstract class KeyValueAbstractTest extends AndroidTestCase {

	private static final int ID = 1338;

	protected abstract DataObjectFactory getFactory(); // testing different implementation

	public void testRelated() throws InterruptedException, ExecutionException {
		DataObjectFactory factory = getFactory();
		
		DataClassFacade<GameScore> facade = factory.get(GameScore.class);
		
		GameScore gameScore = facade.create();
		int score = 1369;
		gameScore.score(score);
		gameScore.playerName("Sheng");
		gameScore.cheatMode(true);
		gameScore.active(true);
		final Game game = factory.get(Game.class).create();
		game.name("My Game");
		gameScore.game(game);
		
		String id = gameScore.dataObject().save().get();
		assertNotNull(id);
		
		DataQuery<GameScore> query = facade.newQuery();
		Game inc = query.include().game();
		query.equalTo().score(score);
		List<GameScore> list = query.find().get();
		for (GameScore gs2 : list) {
			assertNotNull(gs2);
			
			Game game2 = gs2.game();
			assertNotNull(game2);
			
			String name = game2.name();
			assertEquals("My Game", name);
		}
		
		assertNotNull("can't chain yet", inc);
	}
	
	public void testDataInterface() throws InterruptedException, ExecutionException {
		DataObjectFactory factory = getFactory();
		
		DataClassFacade<GameScore> facade = factory.get(GameScore.class);
		
		GameScore gameScore = facade.create();
		gameScore.score(1339);
		gameScore.playerName("Sheng");
		gameScore.cheatMode(true);
		gameScore.active(true);
		String id = gameScore.dataObject().save().get();

		assertNotNull(id);
//		assertEquals("id", id);
		
		DataQuery<GameScore> query = facade.newQuery();
		query.equalTo().score(1339);
		query.equalTo().active(true);
		List<GameScore> list = (query).find().get();
		assertNotNull(list);
		
		int score = gameScore.score();
		String playerName = gameScore.playerName();
		boolean cheatMode = gameScore.cheatMode();

		String objectId = gameScore.dataObject().getObjectId();
//		Date updatedAt = gameScore.dataObject().getUpdatedAt();
//		Date createdAt = gameScore.dataObject().getCreatedAt();
		
		for (GameScore gs : list) {
			gs.active(false);
			gs.dataObject().save().get();
		}
		assertEquals(1, list.size());
	}

	public void testDataObject() throws InterruptedException, ExecutionException {
		DataObjectFactory factory = getFactory();
		
		DataObject object = factory.createDataObject("GameScore");
		object.put("score", ID);
		object.put("playerName", "Sean Plott");
		object.put("cheatMode", false);
		object.put("active", true);
		ListenableFuture<String> save = object.save();
		String id = save.get();
		
		assertNotNull(id);
//		assertEquals("id", id);
	}

	public void testQuery() throws InterruptedException, ExecutionException {
		testDataObject();
		
		DataObjectFactory factory = getFactory();
		
		DataObjectQuery query = factory.createDataObjectQuery("GameScore");
		query.whereEqualTo("score", ID);
		query.whereEqualTo("active", true);
		List<DataObject> list = query.find().get();
				
		for (DataObject dataObject : list) {
			dataObject.put("active", false);
			dataObject.save().get();
		}
		
		assertEquals(2, list.size());
	}

	
}