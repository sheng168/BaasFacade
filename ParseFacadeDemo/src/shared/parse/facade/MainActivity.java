package shared.parse.facade;

import java.util.Date;
import java.util.List;

import shared.parse.ParseFacade;
import shared.parse.ParseFacade.Query;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final TextView tv = (TextView) findViewById(R.id.textView);

		Parse.initialize(this, "XLjaPbXGzUGcIvpOKF9dTdTUV9wXpnvuaAHnMvHJ",
				"xjTYhU1sSqtE89w5ucryZnptWdNtppAMI3W6vyBF");
		
		final ParseFacade<TestObject> parseFacade = ParseFacade.get(TestObject.class);
		TestObject test = parseFacade.create();
		
		final String name = "HelloWorld!";
		test.stringField(name);
		test.dateField(new Date());
		
		test.parseObject().saveEventually(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				String msg;
				if (e == null) {
					msg = "Test saved";
					
					loadTestObjects(tv, parseFacade, name);
				} else {
					msg = "Error: " + e;
				}
				
				tv.append("\n" + msg);
			}
		});
	}
	
	

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.activity_main, menu);
//		return true;
//	}



	private void loadTestObjects(final TextView tv,
			final ParseFacade<TestObject> parseFacade, final String name) {
		// load
		Query<TestObject> query = parseFacade.query();
		// save query param on template object
		query.equalTo().stringField(name); 
		// indicate sort field by calling read method
		query.orderDesc().dateField(); 
		
		// get back to regular parse.com API
		ParseQuery parseQuery = query.parseQuery();
		
		parseQuery.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> list, ParseException e) {
				String msg;
				if (e == null) {
					final List<TestObject> pojoList = parseFacade.wrap(list);
					msg = String.format("%d found", pojoList.size());
					
					if (pojoList.size() > 0) {
						msg += String.format("\nlatest object dateField: " + pojoList.get(0).dateField());
					}
				} else {
					msg = "Error: " + e;
				}
				tv.append("\n" + msg);
			}
		});
	}
}
