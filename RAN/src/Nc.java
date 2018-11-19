import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class Nc {
	public static void main(String args[]) throws IOException {
		File file = new File("/home/aberami/Documents/txt/nc1.txt");
		FileReader fr = new FileReader(file);
		BufferedReader bf = new BufferedReader(fr);
		String test = null;
		String[] line = new String[10];
		Pattern name = Pattern.compile("(?<=Patient:\\s).+(?=Invoice Number:)");
		Pattern invoice = Pattern.compile("(?<=Invoice Number:\\s).+");
		Pattern history = Pattern.compile("([\\w]{3}\\s[\\d]{2},\\s[\\d]{4}\\s).+(\\$\\d+\\.\\d+)");
		Pattern address1 = Pattern.compile(".+(?=Date)");
		Pattern address2 = Pattern.compile(".+(?=Printed)");
		Pattern address3 = Pattern.compile("(?<=Printed:)(.|\\n)+?(?=Patient:)");
		StringBuilder str = new StringBuilder();
		while ((test = bf.readLine()) != null) {
			str.append(test);
			str.append("\n");
		}
		System.out.println(str.toString());
		JSONObject json = new JSONObject();
		JSONArray jarray = new JSONArray();
		String data = str.toString();
		str.setLength(0);
		Matcher mat = null;
		mat = name.matcher(data);
		while (mat.find()) {
			test = mat.group().trim();
			json.put("name", test);
		}
		mat = address1.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			str.append(test + " ");
		}
		mat = address2.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			str.append(test + " ");
		}
		mat = address3.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			line = test.split("\\s{2,}");
			for (int i = 1; i < line.length; i++) {
				str.append(line[i] + " ");
			}
			json.put("Address", str);
		}
		mat = invoice.matcher(data);
		while (mat.find()) {
			test = mat.group().trim();
			json.put("invoice", test);
		}
		String[] temp;
		mat = history.matcher(data);
		while (mat.find()) {
			test = mat.group().trim();
			temp = test.split("\\s{2,}");
			JSONObject hist = new JSONObject();
			hist.put("date", temp[0]);
			hist.put("description", temp[1]);
			hist.put("quantity", temp[2]);
			hist.put("amount", temp[3]);
			jarray.put(hist);
		}
		json.put("Description", jarray);
		System.out.println(json);
		bf.close();
	}
}