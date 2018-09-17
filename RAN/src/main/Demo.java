package main;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Iterators;

public class Demo {
	public static void main(String args[]) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("name", "nambi");
		json.put("no", "1234");
		Iterator<?> keys = json.keys();

		while (keys.hasNext()) {
			String key = (String) keys.next();
			System.err.println(json.get(key));
		}
		System.err.println(Iterators.size(json.keys()));
	}
}