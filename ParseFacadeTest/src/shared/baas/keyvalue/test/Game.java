package shared.baas.keyvalue.test;

import shared.baas.DataInterface;

public interface Game extends DataInterface {
	public String name();
	public void name(String in);
//	public static final ParseFacade<GameScore> PF = ParseFacade.get(GameScore.class);
}
