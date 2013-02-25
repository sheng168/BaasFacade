package shared.parse.ui;

import java.util.ArrayList;
import java.util.List;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ParseAdapter extends BaseAdapter {
	ParseQuery query;
	int count;
	List<ParseObject> list = new ArrayList<ParseObject>();
	private Context context;
	
	public ParseQuery getQuery() {
		return query;
	}

	public void setQuery(ParseQuery query) {
		this.query = query;
		query.countInBackground(new CountCallback() {
			@Override
			public void done(int i, ParseException e) {
				if (e == null) {
					count = i;
					list = new ArrayList<ParseObject>(i);
				} else
					count = 0;
			}
		});
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public Object getItem(int i) {
		Log.d("", "getItem" + i);
		return list.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup parent) {
		TextView view;
		if (convertView == null) {
			view = new TextView(context);
		} else {
			view = (TextView) convertView;
		}
		view.setText("Item " + i);
		
		return view;
	}

}
