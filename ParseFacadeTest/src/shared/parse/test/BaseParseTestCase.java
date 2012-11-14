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

		// nymsg account
		Parse.initialize(this.getContext(),
				"mDrOLT1hvbwEyL8E3QQjpAXaqA2hgEVISIodh4zD",
				"mKMDvVnmEaqIdxUTDm9sm3IFkswbLP1RfjzDj7pP");
		super.setUp();
	}

}