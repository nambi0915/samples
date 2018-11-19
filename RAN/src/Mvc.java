import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class Mvc {
	public static void main(String args[]) throws IOException {
		File file = new File("/home/aberami/Documents/txt/mvc5.txt");
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
		Pattern remind = Pattern.compile("Reminder");
		Pattern reminder = Pattern
				.compile("(\\d{2}\\/\\d{2}\\/\\d{4})(.|\\n)+?(?=(\\d{2}\\/\\d{2}\\/\\d{4})|(?=Invoice))");
		Pattern addr1 = Pattern.compile(".+(?=Client ID:)");
		Pattern addr2 = Pattern.compile(".+(?=Invoice #)");
		Pattern addr3 = Pattern.compile(".+(?=Date:)");
		Pattern clientId = Pattern.compile("(?<=Client ID:).+");
		Pattern invoiceNumber = Pattern.compile("(?<=Invoice #:).+");
		Pattern description = Pattern.compile(".+(\\$\\d+\\.\\d+)");
		Pattern patientId = Pattern.compile("(?<=Patient ID:).+(?=Species)");
		Pattern species = Pattern.compile("(?<=Species:).+(?=Weight)");
		Pattern patientName = Pattern.compile("(?<=Patient Name:).+(?=Breed:)");
		Pattern breed = Pattern.compile("(?<=Breed:).+(?=Birthday)");
		Matcher mat = null;
		mat = remind.matcher(data);
		String date = null;
		String staff = null;
		String samp = null;
		if (mat.find()) {
			samp = data.substring(mat.start());
			mat = reminder.matcher(samp);
			while (mat.find()) {
				test = mat.group().trim();

				line = test.split("(?<=\\d{2}\\/\\d{2}\\/\\d{4})\\s");
				JSONObject sample = new JSONObject();
				sample.put("date", line[0].trim());
				sample.put("description", line[1].trim().replaceAll("\\s{2,}", " "));
				jarray.put(sample);
			}
			json.put("Reminders", jarray);
			jarray = new JSONArray();
		}
		mat = patientId.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("Patient Id", test);
		}
		mat = species.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("Species", test);
		}
		mat = patientName.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("Patient Name", test);
		}
		mat = breed.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("Breed", test);
		}

		mat = addr1.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			str.append(test + " ");
		}
		mat = addr2.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			str.append(test + " ");
		}
		mat = addr3.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			str.append(test + " ");
			json.put("address", str.toString());
		}
		mat = clientId.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("client_id", test);
		}
		mat = invoiceNumber.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("invoice_no", test);
		}
		mat = description.matcher(data);
		while (mat.find()) {
			test = mat.group().trim();
			line = test.split("\\s{2,}");
			if (line.length == 5) {
				JSONObject sample = new JSONObject();
				date = line[0];
				staff = line[2];
				sample.put("date", date);
				sample.put("description", line[1]);
				sample.put("staff name", staff);
				sample.put("quantity", line[3]);
				sample.put("total", line[4]);
				jarray.put(sample);
			}
			if (line.length == 4) {
				JSONObject sample = new JSONObject();
				sample.put("date", date);
				sample.put("description", line[0]);
				sample.put("staff name", line[1]);
				sample.put("quantity", line[2]);
				sample.put("total", line[3]);
				jarray.put(sample);
			}
			if (line.length == 3) {
				JSONObject sample = new JSONObject();
				sample.put("date", date);
				sample.put("description", line[0]);
				sample.put("staff name", staff);
				sample.put("quantity", line[1]);
				sample.put("total", line[2]);
				jarray.put(sample);
			}
		}
		json.put("Descriptions", jarray);
		System.out.println(json);
	}
}