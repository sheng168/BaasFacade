package shared.parse.test;

import shared.parse.ParseBase;

import com.parse.ParseGeoPoint;


public interface GcmDevice extends ParseBase {
	public String email();
	public void email(String name);

	public String regId();
	public void regId(String name);

	public float batteryLevel();
	public void batteryLevel(float in);

	public ParseGeoPoint center();
	public void center(ParseGeoPoint name);

	public Integer count();
	public void count(int in);

	public boolean enable();
	public void enable(boolean in);
/*
	public  Field();
	public void Field( in);
*/
}
