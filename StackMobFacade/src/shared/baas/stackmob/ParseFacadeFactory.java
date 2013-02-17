package shared.baas.stackmob;

import shared.baas.FacadeFactory;

public class ParseFacadeFactory implements FacadeFactory {
	/* (non-Javadoc)
	 * @see shared.parse.FacadeFactory#get(java.lang.Class)
	 */
	@Override
	public <T> ParseFacade<T> get(Class<T> clazz) {
		return ParseFacade.get(clazz);
	}
}
