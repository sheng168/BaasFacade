package shared.parse.query;

import com.parse.ParseQuery;

public class ParseQueryEqualHandler extends ParseQueryOneArg {
	@Override
	protected void doNameValue(String name, Object arg) {
		pq.whereEqualTo(name, arg);
	}

	public ParseQueryEqualHandler(ParseQuery pq) {
		super(pq);
	}
	
}