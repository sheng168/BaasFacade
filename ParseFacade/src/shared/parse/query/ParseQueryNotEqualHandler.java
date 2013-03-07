package shared.parse.query;

import com.parse.ParseQuery;

public class ParseQueryNotEqualHandler extends ParseQueryOneArg {
	@Override
	protected void doNameValue(String name, Object arg) {
		pq.whereNotEqualTo(name, arg);
	}

	public ParseQueryNotEqualHandler(ParseQuery pq) {
		super(pq);
	}
	
}