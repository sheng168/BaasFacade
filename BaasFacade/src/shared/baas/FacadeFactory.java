package shared.baas;


public abstract class FacadeFactory {

	/**
	 * Use this to get instances for related to each class.
	 * 
	 * @param clazz interface that extends ParseBase
	 * @return may be a cache or new instance
	 */
	public abstract <T> DataStoreFacade<T> get(Class<T> clazz);

	private static FacadeFactory instance;
	
	public static FacadeFactory getDefault() {
		return instance;
	}

	public static void setDefault(FacadeFactory f) {
		instance = f;
	}

}