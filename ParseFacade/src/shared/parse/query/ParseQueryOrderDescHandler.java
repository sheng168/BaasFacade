package shared.parse.query;

import com.parse.ParseQuery;

public class ParseQueryOrderDescHandler extends ParseQueryOrderHandler {
	public ParseQueryOrderDescHandler(ParseQuery pq) {
		super(pq);
	}

	@Override
	void orderBy(String name) {
		pq.orderByDescending(name);
	}

}