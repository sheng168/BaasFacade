package shared.baas.stackmob;

import shared.baas.FacadeFactory;

public class StackMobFacadeFactory implements FacadeFactory {
	/* (non-Javadoc)
	 * @see shared.parse.FacadeFactory#get(java.lang.Class)
	 */
	@Override
	public <T> StackMobFacade<T> get(Class<T> clazz) {
		return StackMobFacade.get(clazz);
	}
}
