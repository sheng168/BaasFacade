package shared.baas.keyvalue.test;

import java.util.Date;

import shared.baas.keyvalue.DataObjectFactory;
import shared.baas.keyvalue.parse.DataObjectFactoryParse;
import shared.parse.ListCallback;
import shared.parse.ParseFacade.Query;


import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.RefreshCallback;

/**
 * Parse.com examples translated to our API.
 * @author sheng
 *
 */
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
