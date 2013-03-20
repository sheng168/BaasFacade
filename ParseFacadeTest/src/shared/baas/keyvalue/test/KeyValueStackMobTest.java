package shared.baas.keyvalue.test;

import shared.baas.keyvalue.DataObjectFactory;
import shared.baas.keyvalue.parse.DataObjectFactoryParse;
import shared.baas.keyvalue.stackmob.DataObjectFactorySM;

import com.parse.Parse;
import com.stackmob.android.sdk.common.StackMobAndroid;

public class KeyValueStackMobTest extends KeyValueAbstractTest {
	@Override
	protected DataObjectFactory getFactory() {
		StackMobAndroid.init(getContext(), 0, "3ccfcd0d-a56c-4a5d-b4cb-812c7b606929"); // tasks

		return new DataObjectFactorySM();
	}

}
