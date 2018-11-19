import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class Civ {
	public static void main(String args[]) throws IOException {
		File file = new File("/home/aberami/Documents/txt/civ1.txt");
		FileReader fr = new FileReader(file);
		BufferedReader bf = new BufferedReader(fr);
		String test = null;
		StringBuilder str = new StringBuilder();
		while ((test = bf.readLine()) != null) {
			str.append(test);
			str.append("\n");
		}
		bf.close();
		JSONObject json = new JSONObject();
		JSONArray jarray = new JSONArray();
		String[] line = new String[10];
		String data = str.toString();
		str.setLength(0);
		System.out.println(data);
		Pattern desc = Pattern.compile("\\d{2}\\/\\d{2}\\/\\d{2}.+");
		Pattern address = Pattern.compile("(?<=Client Information)[\\s\\w\\d-,&\\/\\.#]+(?=Date\\/Time)");
		Matcher mat = null;
		mat = desc.matcher(data);
		while (mat.find()) {
			test = mat.group().trim();
			line = test.split("\\s{2,}");
			if (line.length == 6) {
				JSONObject samp = new JSONObject();
				samp.put("date", line[0]);
				samp.put("description", line[2]);
				samp.put("name", line[1]);
				samp.put("amount", line[5]);
				samp.put("quantity", line[3]);
				jarray.put(samp);
			}
			json.put("Descriptions", jarray);
		}
		mat = address.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();

			test = test.replaceAll("\\s{2,}", "  ");
			System.out.println(test);
			json.put("address", test);
		}

		System.out.println(json);
	}
}
