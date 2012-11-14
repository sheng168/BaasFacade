package shared.parse.test;

import java.util.Date;
import java.util.List;

import shared.parse.ListCallback;
import shared.parse.ParseFacade.Query;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.RefreshCallback;

/**
 * Parse.com examples translated to our API.
 * @author sheng
 *
 */
public class GameScoreExampleTest extends BaseParseTestCase {
	public void testOverall() throws ParseException {
		// save
		GameScore gameScore = GameScore.PF.create();
		gameScore.score(1337);
		gameScore.playerName("Sean Plott");
		gameScore.cheatMode(true);
		gameScore.parseObject().save();
		String id = gameScore.parseObject().getObjectId();

		// retrieve
		GameScore.PF.query().parseQuery().getInBackground(id, new GetCallback() {
			@Override
			public void done(ParseObject gameScore, ParseException e) {
				if (e == null) {
					// do something with gameScore
					GameScore gameScore2 = GameScore.PF.wrap(gameScore);
				} else {
					// something went wrong
				}
			}
		});

		//
		gameScore = GameScore.PF.create();
		gameScore.parseObject().setObjectId(id);
		gameScore.parseObject().refreshInBackground(new RefreshCallback() {
			@Override
			public void done(ParseObject o, ParseException e) {
				if (e == null) {
					// do something with gameScore
				} else {
					// something went wrong
				}
			}
		});

		//
		Query<GameScore> query = GameScore.PF.query();
		query.equalTo().playerName("Sean Plott");
		query.orderAsc().score();
		query.findInBackground(new ListCallback<GameScore>() {
			@Override
			public void error(ParseException e) {
				// something went wrong
			}

			@Override
			public void done(List<GameScore> list) {
				// do something with gameScore
				if (list.size() > 0) {
					GameScore gameScore2 = list.get(1);
				}
			}
		});

		// read application values
		int score = gameScore.score();
		String playerName = gameScore.playerName();
		boolean cheatMode = gameScore.cheatMode();

		// read special system managed values
		String objectId = gameScore.parseObject().getObjectId();
		Date updatedAt = gameScore.parseObject().getUpdatedAt();
		Date createdAt = gameScore.parseObject().getCreatedAt();
	}
}
