import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class Avidone {
	public static void main(String args[]) throws IOException {
		File file = new File("/home/aberami/Documents/txt/avid1.txt");
		FileReader fr = new FileReader(file);
		BufferedReader bf = new BufferedReader(fr);
		// Pattern reminder = Pattern.compile("Reminders");
		Pattern reminder = Pattern.compile("(\\d{2}\\/\\d{2}.+)");
		Pattern description = Pattern.compile("(\\d+-\\d+-\\d+)((?=.*(\\s\\d+\\s)).+)(\\s\\d+\\.\\d+)");
		Pattern patientName = Pattern.compile("(?<=FOR:\\s).+(?=Date:)");
		Pattern address1 = Pattern.compile(".+(?=Folder:)");
		Pattern address2 = Pattern.compile(".+(?=Invoice:)");
		Pattern invoice = Pattern.compile("(?<=Invoice:).+");
		String test = null;
		Matcher mat = null;
		StringBuilder str = new StringBuilder();
		// Reads the content into a String
		while ((test = bf.readLine()) != null) {
			str.append(test);
			str.append("\n");
		}
		String data = str.toString();
		str.setLength(0);
		// Matches the reminder
		mat = reminder.matcher(data);
		String[] temp;
		System.out.println(data);
		JSONArray jarray = new JSONArray();
		JSONObject json = new JSONObject();
		while (mat.find()) {
			test = mat.group().trim();
			temp = test.split("\\s{2,}");
			JSONObject hist = new JSONObject();
			if (temp.length == 3) {
				hist.put("date", temp[0]);
				hist.put("description", temp[1]);
				hist.put("last", temp[2]);

			} else {
				hist.put("date", temp[0]);
				hist.put("description", temp[1]);
			}
			jarray.put(hist);
		}
		json.put("Reminders", jarray);
		jarray = new JSONArray();
		// Matches Description
		String name = null;
		mat = description.matcher(data);
		while (mat.find()) {
			test = mat.group().trim();
			System.out.println(test);
			temp = test.split("\\s{2,}");
			JSONObject hist = new JSONObject();
			if (temp.length == 5) {
				name = temp[1];
				hist.put("date", temp[0]);
				hist.put("name", name);
				hist.put("quantity", temp[2]);
				hist.put("description", temp[3]);
				hist.put("price", temp[4]);
				jarray.put(hist);
			}
			if (temp.length == 4) {
				hist.put("date", temp[0]);
				hist.put("name", name);
				hist.put("quantity", temp[1]);
				hist.put("description", temp[2]);
				hist.put("price", temp[3]);
				jarray.put(hist);
			}
			json.put("Description", jarray);
		}
		// Matches Patient name
		mat = patientName.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("name", test);
		}
		// Matches Patient Address
		mat = address1.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			str.append(test + " ");
			mat = address2.matcher(data);
			if (mat.find()) {
				test = mat.group().trim();
				str.append(test + " ");
				json.put("address", str.toString());
			}
		}
		// Matches Invoice number
		mat = invoice.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("invoice number", test);
		}
		bf.close();
		System.out.println(json);
	}
}