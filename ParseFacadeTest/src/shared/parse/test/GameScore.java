package shared.parse.test;

import shared.parse.ParseBase;
import shared.parse.ParseFacade;

public interface GameScore extends ParseBase {
	public String playerName();
	public void playerName(String in);

	public Integer score(); // should always return object
	public void score(int in);

	public Boolean cheatMode(); // in case data store has not value or when sorting
	public void cheatMode(boolean in);
	
	public static final ParseFacade<GameScore> PF = ParseFacade.get(GameScore.class);
}
