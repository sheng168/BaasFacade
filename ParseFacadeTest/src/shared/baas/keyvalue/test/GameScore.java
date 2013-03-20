package shared.baas.keyvalue.test;

import shared.baas.DataInterface;

public interface GameScore extends DataInterface {
	public String playerName();
	public void playerName(String in);

	public Integer score(); // should always return object
	public void score(int in);

	public Boolean cheatMode(); // in case data store has not value or when sorting
	public void cheatMode(boolean in);
	
	public Boolean active(); // in case data store has not value or when sorting
	public void active(boolean in);
//	public static final ParseFacade<GameScore> PF = ParseFacade.get(GameScore.class);
}
