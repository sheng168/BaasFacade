package shared.baas.keyvalue.test;

import shared.baas.demo.provider.BaasDemo;
import shared.baas.keyvalue.DataObjectFactory;
import shared.baas.keyvalue.sqlite.DataObjectFactorySqlite;

/**
 * Parse.com examples translated to our API.
 * @author sheng
 *
 */
public class KeyValueSqliteTest extends KeyValueAbstractTest {
	@Override
	protected DataObjectFactory getFactory() {
		return new DataObjectFactorySqlite(this.getContext().getContentResolver(), BaasDemo.CONTENT_URI_BASE);
	}

}
