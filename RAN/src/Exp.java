import java.io.IOException;

import org.json.JSONArray;

public class Exp {
	public static void main(String args[]) {
		JSONArray jarray = new JSONArray();

		jarray.put("b");
		jarray.put("a");
		jarray.put(0, "c");
		jarray.put(1, "d");
		System.err.println(jarray.toString());
	}
}
