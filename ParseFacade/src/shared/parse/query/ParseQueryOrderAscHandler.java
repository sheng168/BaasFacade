package shared.parse.query;

import com.parse.ParseQuery;

public class ParseQueryOrderAscHandler extends ParseQueryOrderHandler {
	public ParseQueryOrderAscHandler(ParseQuery pq) {
		super(pq);
	}

	@Override
	void orderBy(String name) {
		pq.orderByAscending(name);
	}

}