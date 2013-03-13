package shared.baas.impl;

import java.lang.reflect.Proxy;

import shared.baas.DataClassFacade;
import shared.baas.DataObject;

public abstract class AbstractDataStoreFacade<T> implements DataClassFacade<T> {
	protected Class<T> clazz;
	Class<?>[] interfaces;
	
	public Class<T> getClazz() {
		return clazz;
	}

	public Class<?>[] getInterfaces() {
		return interfaces;
	}

	public AbstractDataStoreFacade(Class<T> clazz) {
		super();
		assert clazz != null;
		assert clazz.isInterface();

		this.clazz = clazz;
		Class<?>[] interfaces = { clazz };
		this.interfaces = interfaces;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T wrap(DataObject object) {
		assert object != null;
	
		Object obj = Proxy.newProxyInstance(clazz.getClassLoader(), interfaces,
				new InterfaceHandler(object));
		return (T) obj;
	}

	@Override
	public T create() {		
		return wrap(newObjectData());
	}

	protected abstract DataObject newObjectData();
}