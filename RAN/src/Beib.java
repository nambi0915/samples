import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class Beib {
	public static void main(String args[]) throws IOException {
		File file = new File("/home/aberami/Documents/txt/beiber.txt");
		FileReader fr = new FileReader(file);
		BufferedReader bf = new BufferedReader(fr);
		String test = null;
		StringBuilder str = new StringBuilder();
		while ((test = bf.readLine()) != null) {
			str.append(test);
			str.append("\n");
		}
		bf.close();
		String sample = null;
		String data = str.toString();
		String[] line = new String[10];
		JSONObject json = new JSONObject();
		JSONArray jarray = new JSONArray();
		System.out.println(data);
		Pattern remin = Pattern.compile("Reminder");
		Pattern reminder = Pattern.compile("\\d{2}\\/\\d{2}\\/\\d{4}(.|\\n)+?(?=(\\d{2}\\/\\d{2}\\/\\d{4})|(?=Inv))");
		Pattern patientId = Pattern.compile("(?<=Patient ID:).+(?=Species:)");
		Pattern name = Pattern.compile("(?<=Patient Name:).+(?=Breed:)");
		Pattern desc = Pattern.compile("(?=.*(\\s\\d+\\.\\d+)).+(\\$\\d+\\.\\d+)");
		Matcher mat;
		mat = remin.matcher(data);
		while (mat.find()) {
			sample = data.substring(mat.start());
		}
		mat = reminder.matcher(sample);
		while (mat.find()) {
			test = mat.group().trim();
			// System.out.println(test);
			line = test.split("(?<=\\d{2}\\/\\d{2}\\/\\d{4})\\s");
			JSONObject samp = new JSONObject();
			samp.put("date", line[0].trim());
			samp.put("description", line[1].trim().replaceAll("\\s{2,}", "  "));
			jarray.put(samp);

		}
		json.put("reminders", jarray);
		jarray = new JSONArray();

		mat = patientId.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("Id", test);
		}
		mat = name.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("name", test);
		}
		String date = null;
		String staff = null;
		mat = desc.matcher(data);
		while (mat.find()) {

			test = mat.group().trim();
			line = test.split("\\s{2,}");
			if (line.length == 5) {
				date = line[0].trim();
				staff = line[2].trim();
				JSONObject samp = new JSONObject();
				samp.put("date", date);
				samp.put("description", line[1].trim());
				samp.put("staff name", staff);
				samp.put("quantity", line[3].trim());
				samp.put("amount", line[4].trim());

				jarray.put(samp);

			}
			if (line.length == 3) {
				JSONObject samp = new JSONObject();
				samp.put("staff name", staff);
				samp.put("date", date);
				samp.put("description", line[0].trim());
				samp.put("quantity", line[1].trim());
				samp.put("total", line[2].trim());
				jarray.put(samp);
			}

		}
		json.put("description", jarray);
		System.out.println(json);
	}
}