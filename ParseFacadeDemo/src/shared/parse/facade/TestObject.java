package shared.parse.facade;

import java.util.Date;

import shared.parse.ParseBase;

import com.parse.ParseGeoPoint;


public interface TestObject extends ParseBase {
	public String stringField();
	public void stringField(String name);

	public double doubleField();
	public void doubleField(double in);
	public float floatField();
	public void floatField(float in);
	
	public long longField();
	public void longField(long in);	
	public int intField();
	public void intField(int in);

	public Date dateField();
	public void dateField(Date in);

	public boolean booleanField();
	public void booleanField(boolean in);

	public byte[] byteField();
	public void byteField(byte[] in);
/*
	public  Field();
	public void Field( in);
*/
	public ParseGeoPoint center();
	public void center(ParseGeoPoint name);
}
