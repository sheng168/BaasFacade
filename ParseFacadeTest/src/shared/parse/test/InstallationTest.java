package shared.parse.test;

import shared.parse.ParseFacade;

import com.parse.ParseException;
import com.parse.ParseInstallation;

public class InstallationTest extends BaseParseTestCase {
	public void testInsert() throws ParseException {
		ParseFacade<GcmDevice> pgd = ParseFacade.get(GcmDevice.class);
		
		ParseInstallation inst = ParseInstallation.getCurrentInstallation();
		GcmDevice wrap = pgd.wrap(inst);
		
		wrap.regId("regId");
		wrap.batteryLevel(.5f);
		
		wrap.parseObject().save();
	}
}
