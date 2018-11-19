import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class Avid {
	public static void main(String args[]) throws IOException {
		File file = new File("/home/aberami/Documents/txt/form1.txt");
		FileReader fr = new FileReader(file);
		BufferedReader bf = new BufferedReader(fr);
		String test = null;
		StringBuilder str = new StringBuilder();
		while ((test = bf.readLine()) != null) {
			str.append(test);
			str.append("\n");
		}
		String data = str.toString();
		JSONObject json = new JSONObject();
		JSONArray jarray = new JSONArray();
		Pattern reminder = Pattern.compile("Reminders");
		Pattern reminder1 = Pattern.compile(
				"(\\d{1,2}(-|\\/)\\d{2}(-|\\/)\\d{2,4})[\\w\\d\\s-()\\/]+(\\d{1,2}(-|\\/)\\d{2}(-|\\/)\\d{2,4})\n");

		// System.out.println(data);
		Matcher mat = null;
		mat = reminder.matcher(data);
		String rem = null;
		if (mat.find()) {

			rem = data.substring(mat.start());
		}
		String[] remind = new String[20];
		mat = reminder1.matcher(rem);
		if (mat.find()) {
			test = mat.group().trim();
			// System.out.println(test);
			remind = test.split("\\n");
		}

		// System.out.println(remind.length);
		for (int i = 0; i < remind.length; i++) {
			// System.out.println(remind[i]);
			remind[i] = remind[i].trim();
			String[] samp = remind[i].split("\\s\\s+");
			JSONObject sample = new JSONObject();
			if (samp.length == 3) {
				sample.put("date", samp[0]);
				sample.put("description", samp[1]);
				sample.put("lastDone", samp[2]);
			} else {
				sample.put("date", samp[0]);
				sample.put("description", samp[1]);
			}
			jarray.put(sample);
			// System.out.println(jarray);
		}
		json.put("Reminders", jarray);
		System.out.println(json);
		bf.close();
	}
}