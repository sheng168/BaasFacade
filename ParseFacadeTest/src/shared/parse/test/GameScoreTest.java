package shared.parse.test;

import shared.parse.ParseFacade.Query;

import com.parse.ParseException;
import com.parse.ParseObject;

public class GameScoreTest extends BaseParseTestCase {
	public void testNeverSet() throws ParseException {
		// create object but haven't set or save
		GameScore gameScore = GameScore.PF.create();

		// read application values
		assertNull(gameScore.score()) ;
		assertNull(gameScore.playerName());
		assertNull(gameScore.cheatMode());

		// read special system managed values
		ParseObject po = gameScore.parseObject();
		assertNull(po.getObjectId());
		assertNull(po.getUpdatedAt());
		assertNull(po.getCreatedAt());
		
		try {
			int score = gameScore.score();
			fail("should have thrown exception, instead of returning: " + score);
		} catch (Exception e) {
			assertEquals(NullPointerException.class, e.getClass());
		}
	}
	
	public void testOverall() throws ParseException {
		// save
		GameScore gameScore = GameScore.PF.create();
		gameScore.score(1337);
		gameScore.playerName("Sean Plott");
		gameScore.cheatMode(true);
		gameScore.parseObject().save();
		
		// system manage field set after save
		ParseObject po = gameScore.parseObject();
		assertNotNull(po.getObjectId());
		assertNotNull(po.getUpdatedAt());
		assertNotNull(po.getCreatedAt());

		String id = po.getObjectId();

		// retrieve by id
		GameScore gameScore2 = GameScore.PF.create();
		gameScore2.parseObject().setObjectId(id);
		gameScore2.parseObject().refresh();
		
		assertEquals(gameScore.score(), gameScore2.score());
		assertEquals(gameScore.playerName(), gameScore2.playerName());
		assertEquals(gameScore.cheatMode(), gameScore2.cheatMode());

		// query
		Query<GameScore> query = GameScore.PF.query();
		query.equalTo().playerName("Sean Plott");
		query.orderDesc().score();
		query.orderAsc().playerName();
		gameScore2 = query.find().get(0);
		
		assertEquals(gameScore.score(), gameScore2.score());
		assertEquals(gameScore.playerName(), gameScore2.playerName());
		assertEquals(gameScore.cheatMode(), gameScore2.cheatMode());
		
	}
}
