package shared.baas.test;

import shared.baas.DataFacadeFactory;

import com.stackmob.sdk.api.StackMob;

public class StackMobFacadeFactory extends DataFacadeFactory {
	/* (non-Javadoc)
	 * @see shared.parse.FacadeFactory#get(java.lang.Class)
	 */
	@Override
	public <T> StackMobFacade<T> get(Class<T> clazz) {
		return StackMobFacade.get(clazz);
	}
	
	public StackMobFacadeFactory(String apiKey, String apiSecret) {
        int apiVersion = 0;
        
		StackMob.setStackMob(new StackMob(StackMob.OAuthVersion.One, apiVersion, apiKey, apiSecret));
	}
}
