package shared.baas.keyvalue.firebase.test;



public interface Address {
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

//	public ParseGeoPoint location();
//	public void location(ParseGeoPoint value);

//	public static final ParseFacade<Address> facade = ParseFacade.get(Address.class);
//	public static final DataClassFacade<Address> facade = new DataObjectFactory_(new Firebase("")).get(Address.class);
}
