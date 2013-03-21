package shared.baas.keyvalue.test;

import java.util.List;
import java.util.concurrent.ExecutionException;

import shared.baas.DataClassFacade;
import shared.baas.DataQuery;
import shared.baas.impl.BasicDataQuery;
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
		gameScore.score(1339);
		gameScore.playerName("Sheng");
		gameScore.cheatMode(true);
		gameScore.active(true);
		final Game game = factory.get(Game.class).create();
		game.name("My Game");
		gameScore.game(game);
		
		String id = gameScore.dataObject().save().get();
		assertNotNull(id);
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
		List<GameScore> list = ((BasicDataQuery<GameScore>)query).find().get();
		assertNotNull(list);
		
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