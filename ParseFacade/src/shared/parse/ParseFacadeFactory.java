package shared.parse;

import shared.baas.DataStoreFacade;
import shared.baas.FacadeFactory;

public class ParseFacadeFactory implements FacadeFactory {
	/* (non-Javadoc)
	 * @see shared.parse.FacadeFactory#get(java.lang.Class)
	 */
	@Override
	public <T> DataStoreFacade<T> get(Class<T> clazz) {
		return (DataStoreFacade<T>) ParseFacade.get(clazz);
	}
}
