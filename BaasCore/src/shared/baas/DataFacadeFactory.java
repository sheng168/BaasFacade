package shared.baas;

/**
 * Represents an entire data store.
 * 
 * @author sheng
 *
 */
public abstract class DataFacadeFactory {

	/**
	 * Use this to get instances for related to each class.
	 * 
	 * @param clazz interface that extends ParseBase
	 * @return may be a cache or new instance
	 */
	public abstract <T> DataClassFacade<T> get(Class<T> clazz);

	// ***** factory **************
	private static DataFacadeFactory instance;
	
	public static DataFacadeFactory getDefault() {
		return instance;
	}

	public static void setDefault(DataFacadeFactory f) {
		instance = f;
	}

}