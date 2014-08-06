package shared.baas.keyvalue.firebase.test;

import com.firebase.client.Firebase;

import shared.baas.DataClassFacade;
import shared.baas.DataInterface;
import shared.baas.keyvalue.firebase.DataObjectFactory_;



public interface Address extends DataInterface {
	public String address();
	public void address(String value);

	public String address_2();
	public void address_2(String value);
	
	public String city();
	public void city(String value);

	public String state();
	public void state(String value);

	public String zip_code();
	public void zip_code(String value);
	
	public void active(boolean value);
	public void radius(int value);

//	public ParseGeoPoint location();
//	public void location(ParseGeoPoint value);

//	public static final ParseFacade<Address> facade = ParseFacade.get(Address.class);
	public static final DataClassFacade<Address> facade = new DataObjectFactory_(new Firebase("https://abeona-dev.firebaseio.com/syncedValue/class")).get(Address.class);
}
