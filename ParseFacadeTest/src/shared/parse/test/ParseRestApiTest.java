package shared.parse.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.test.AndroidTestCase;

public class ParseRestApiTest extends AndroidTestCase {
	String appId = "XLjaPbXGzUGcIvpOKF9dTdTUV9wXpnvuaAHnMvHJ";
	String key = "NITAgYYzeVxNN6Iso2W4RB9jbwyw4XjVrucqozve";

	public void testGet() throws IOException {
		URL url = new URL("https://api.parse.com/1/batch");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("POST");
		conn.setRequestProperty("X-Parse-Application-Id", appId);
		conn.setRequestProperty("X-Parse-REST-API-Key", key);
		conn.setRequestProperty("Content-Type", "application/json");

		conn.setDoOutput(true);
		conn.setDoInput(true);

		String body = "{\n" + "       \"requests\": [\n" + "         {\n"
				+ "           \"method\": \"POST\",\n"
				+ "           \"path\": \"/1/classes/GameScore\",\n"
				+ "           \"body\": {\n"
				+ "             \"score\": 1337,\n"
				+ "             \"playerName\": \"Sean Plott\"\n"
				+ "           }\n" + "         },\n" + "         {\n"
				+ "           \"method\": \"POST\",\n"
				+ "           \"path\": \"/1/classes/GameScore\",\n"
				+ "           \"body\": {\n"
				+ "             \"score\": 1338,\n"
				+ "             \"playerName\": \"ZeroCool\"\n"
				+ "           }\n" + "         }\n" + "       ]\n" + "     }";

		OutputStream os = conn.getOutputStream();
		os.write(body.getBytes());
		os.close();

		InputStream is = conn.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String s;
		while ((s = reader.readLine()) != null) {
			System.out.println(s);
		}
		is.close();
	}
}
