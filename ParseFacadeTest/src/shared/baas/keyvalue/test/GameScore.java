package shared.baas.keyvalue.test;

import shared.baas.DataClassFacade;
import shared.baas.DataFacadeFactory;
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
	
	public Game game();
	public void game(Game in);

	public static final DataClassFacade<GameScore> facade 
		= DataFacadeFactory.getDefault().get(GameScore.class);
}
