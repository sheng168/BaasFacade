package shared.baas.keyvalue.test;

import shared.baas.keyvalue.DataObjectFactory;
import shared.baas.keyvalue.parse.DataObjectFactoryParse;

import com.parse.Parse;

public class KeyValueParseTest extends KeyValueAbstractTest {
	@Override
	protected DataObjectFactory getFactory() {
		// parse@jsy.us account demo app
		Parse.initialize(this.getContext(),
				"XLjaPbXGzUGcIvpOKF9dTdTUV9wXpnvuaAHnMvHJ",
				"xjTYhU1sSqtE89w5ucryZnptWdNtppAMI3W6vyBF");
		return new DataObjectFactoryParse();
	}

}
