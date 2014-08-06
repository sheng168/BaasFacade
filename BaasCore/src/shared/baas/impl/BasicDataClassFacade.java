package shared.baas.impl;

import java.lang.reflect.Proxy;

import shared.baas.DataClassFacade;
import shared.baas.DataQuery;
import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.DataObjectFactory;

public class BasicDataClassFacade<T> implements DataClassFacade<T> {
	protected Class<T> clazz;
	Class<?>[] interfaces;
	DataObjectFactory factory;
	
	public Class<T> getClazz() {
		return clazz;
	}

	public Class<?>[] getInterfaces() {
		return interfaces;
	}

	public BasicDataClassFacade(DataObjectFactory factory, Class<T> clazz) {
		assert clazz != null;
		assert clazz.isInterface();

		this.factory = factory;
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
	
	@SuppressWarnings("unchecked")
	public static <T> T wrap(DataObject object, Class<T> clazz) {
		Object obj = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz},
				new InterfaceHandler(object));
		return (T) obj;
	}

	@Override
	public T create() {		
		return wrap(newObjectData());
	}

	protected DataObject newObjectData() {
		return factory.createDataObject(clazz.getSimpleName());
	}

	@Override
	public DataQuery<T> newQuery() {
		return new BasicDataQuery<T>(this, factory.createDataObjectQuery(clazz.getSimpleName()));
	}

	@Override
	public DataQuery<T> newOrQuery(DataQuery<T>... queries) {
		// TODO Auto-generated method stub
		return null;
	}
}