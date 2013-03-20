package shared.baas.demo.provider;

import shared.sqlite.GenericSqliteProvider;
import android.database.sqlite.SQLiteOpenHelper;

public class BaasDemoProvider extends GenericSqliteProvider {

	@Override
	protected SQLiteOpenHelper newHelper() {
		return new DatabaseHelper(getContext());
	}

	@Override
	protected String authority() {
		return BaasDemo.AUTHORITY;
	}

}
