package shared.parse;

import shared.baas.DataClassFacade;
import shared.baas.DataFacadeFactory;

public class ParseFacadeFactory extends DataFacadeFactory {
	/* (non-Javadoc)
	 * @see shared.parse.FacadeFactory#get(java.lang.Class)
	 */
	@Override
	public <T> DataClassFacade<T> get(Class<T> clazz) {
		return (DataClassFacade<T>) ParseFacade.get(clazz);
	}
}
