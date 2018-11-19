import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class West {
	public static void main(String[] args) throws IOException {
		File file = new File("/home/aberami/Documents/txt/west2.txt");
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
		bf.close();
		String data = str.toString();
		str.setLength(0);
		System.out.println(data);
		Pattern description = Pattern.compile("(?!.*\\$).+?(\\s\\d+\\.\\d+)");
		Pattern address1 = Pattern.compile("(?<=FOR)(.|\\n)+?(?=DUE)");
		Pattern address2 = Pattern.compile("(?<=TERMS).+");
		Pattern invoiceNumber = Pattern.compile("\\s+\\d{6}\\s+(?=\\d+\\/\\d+\\/\\d+)");
		Pattern remind = Pattern.compile("REMINDERS");
		Pattern reminder = Pattern.compile(".+?(\\s\\d+\\/\\d+\\/\\d+\\s\\s).+");
		Pattern totalAmount = Pattern.compile("(?<=INVOICE TOTAL).+");
		mat = description.matcher(data);
		while (mat.find()) {
			test = mat.group().trim();
			System.out.println("\n\n\n");
			line = test.split("\\s{2,}");
			if (line.length == 4) {
				JSONObject samp = new JSONObject();
				samp.put("patient", line[0]);
				samp.put("description", line[1]);
				samp.put("quantity", line[2]);
				samp.put("amount", line[3]);
				jarray.put(samp);
			}
			if (line.length == 6) {
				JSONObject samp = new JSONObject();
				samp.put("patient name", line[0]);
				samp.put("description", line[1]);
				samp.put("date", line[2]);
				samp.put("doctor", line[3]);
				samp.put("quantity", line[4]);
				samp.put("amount", line[5]);
				jarray.put(samp);
			}
		}
		json.put("Description", jarray);
		jarray = new JSONArray();
		line = null;
		mat = address1.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			str.append(test + " ");
			mat = address2.matcher(data);
			if (mat.find()) {
				test = mat.group().trim();
				str.append(test);
			}
			json.put("Address", str.toString().replaceAll("\\s{2,}", " "));
		}
		mat = invoiceNumber.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("Invoice number", test);
		}
		mat = remind.matcher(data);
		if (mat.find()) {
			sample = data.substring(mat.start());
			mat = reminder.matcher(sample);
			while (mat.find()) {
				test = mat.group().trim();
				line = test.split("\\s{2,}");
				if (line.length == 3) {
					JSONObject samp = new JSONObject();
					samp.put("patient name", line[0]);
					samp.put("date", line[1]);
					samp.put("description", line[2]);
					jarray.put(samp);
				}
				json.put("Reminders", jarray);
			}
		}
		mat = totalAmount.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("Total amount", test);
		}
		System.out.println(json);
	}
}