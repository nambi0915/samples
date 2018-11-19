import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;

import org.json.JSONArray;
import org.json.JSONObject;

public class Bowie {
	public static void main(String[] args) throws IOException {
		File file = new File("/home/aberami/Documents/txt/bowie1.txt");
		FileReader fr = new FileReader(file);
		BufferedReader bf = new BufferedReader(fr);
		String test = null;
		String sample = null;
		StringBuilder str = new StringBuilder();
		JSONObject json = new JSONObject();
		JSONArray jarray = new JSONArray();
		String[] line = new String[10];
		Matcher mat = null;
		while ((test = bf.readLine()) != null) {
			str.append(test);
			str.append("\n");
		}
		String data = str.toString();
		str.setLength(0);
		System.out.println(data);
	}
}
