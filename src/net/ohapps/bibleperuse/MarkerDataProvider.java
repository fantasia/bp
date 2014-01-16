package net.ohapps.bibleperuse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

public class MarkerDataProvider {
	static private MarkerDataProvider instance = new MarkerDataProvider();

	static public MarkerDataProvider getInstance() {
		return instance;
	}

	private String data;
	private JSONArray arj;

	public void init(Activity act) {
		try {
			data = new String(readAll(act.getAssets().open("data")));
			arj = new JSONArray(data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public int size() {
		return arj.length();
	}

	static private int BUFSIZE = 4096;

	static private byte[] readAll(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[BUFSIZE];
		int read = 0;
		while ((read = is.read(buffer, 0, BUFSIZE)) != -1) {
			baos.write(buffer, 0, read);
		}
		is.close();
		baos.flush();
		baos.close();
		return baos.toByteArray();
	}

	public String getDuration(int pos) {
		try {
			JSONObject json = arj.getJSONObject(pos);
			return json.getString("time");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "?";
	}

	public CharSequence getContent(int pos) {
		try {
			JSONObject json = arj.getJSONObject(pos);
			return json.getString("content");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "?";
	}

}
