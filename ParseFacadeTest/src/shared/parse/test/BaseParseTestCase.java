package shared.parse.test;

import com.parse.Parse;

import android.content.Context;
import android.test.AndroidTestCase;

public class BaseParseTestCase extends AndroidTestCase {

	public BaseParseTestCase() {
		super();
	}

	@Override
	protected void setUp() throws Exception {
		Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
		Context context = this.getContext();
		assertNotNull(context);

		// parse@jsy.us account demo app
		Parse.initialize(this.getContext(),
				"XLjaPbXGzUGcIvpOKF9dTdTUV9wXpnvuaAHnMvHJ",
				"xjTYhU1sSqtE89w5ucryZnptWdNtppAMI3W6vyBF");
		super.setUp();
	}

}