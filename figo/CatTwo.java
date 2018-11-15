package figo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class CatTwo {
	public static void main(String args[]) throws IOException {
		File file = new File(
				"/home/aberami/Documents/Figo/Cat2/00 AM (44).txt");
		FileReader fr = new FileReader(file);
		BufferedReader bf = new BufferedReader(fr);
		String test = null;
		StringBuilder str = new StringBuilder();
		while ((test = bf.readLine()) != null) {
			str.append(test);
			str.append("\n");

		}
		bf.close();

		String data = str.toString();
		str.setLength(0);
		reminderExtraction(data);

	}

	public static void reminderExtraction(String data) {
		String test = null;
		JSONObject json = new JSONObject();
		JSONArray jarray = new JSONArray();
		// String[] line = new String[10];
		Pattern remind = Pattern.compile("Reminder|RemrQer");
		Pattern reminder = Pattern.compile(
				"(\\d{2}\\/\\d{2}\\/\\d{4}|[^\\/]\\d{2}\\/\\d{4})[\\s\\S]*?((?=\\s*\\d{2}\\/\\d{2}\\/\\d{4})|(?=Invoice\\s[^#])|(?=Our))");
		Matcher mat = null;
		mat = remind.matcher(data);
		String samp = null;
		int start = 0;
		if (mat.find()) {
			start = mat.start();
			samp = data.substring(start);

		}
		System.err.println(samp);
		if (samp != null) {
			Pattern invoice = Pattern.compile("Invoice\\s[^#]");
			mat = invoice.matcher(samp);
			System.err.println(samp);
			if (mat.find()) {
				System.out.println("Invoice");
				mat = reminder.matcher(samp);
				while (mat.find()) {
					test = mat.group().trim();
					// System.err.println(test);
					jarray = insertJarray(test, jarray);
				}
				json.put("Remainders", jarray);
				System.out.println(json.toString(2));
			} else {
				System.out.println("NoInvoice");
				String noInvoice = samp;
				Boolean rowPresent = true;
				while (rowPresent) {
					Pattern singleRem = Pattern.compile(
							"(\\d{2}\\/\\d{2}\\/\\d{4}|\\d{2}\\/\\d{4})[\\s\\S]*?(?=(\\d{2}\\/\\d{2}\\/\\d{4}|\\d{2}\\/\\d{4}))");
					mat = singleRem.matcher(noInvoice);
					if (mat.find()) {
						int end = mat.end();
						test = mat.group().trim();
						// System.out.println(test);
						jarray = insertJarray(test, jarray);
						rowPresent = dateCheck(noInvoice.substring(end + 11));
						noInvoice = noInvoice.substring(end);
					}

				}
				Pattern endRow = Pattern.compile(
						"(\\d{2}\\/\\d{2}\\/\\d{4}|\\d{2}\\/\\d{4})[\\s\\S]*?(?=(^\\s*$))",
						Pattern.MULTILINE);
				mat = endRow.matcher(noInvoice);
				if (mat.find()) {
					test = mat.group().trim();
					// System.out.println(test);
					jarray = insertJarray(test, jarray);
				}
				json.put("Remainders", jarray);
				System.out.println(json.toString(2));
			}
		} else {
			json.put("Remainders", jarray);
			System.out.println(json.toString(2));
		}

	}

	private static JSONArray insertJarray(String test, JSONArray jarray) {

		String[] line = new String[10];
		line = test.split("\\n");
		for (int i = 0; i < line.length; i++) {
			String[] inline = new String[10];
			// System.err.println(line[i]);
			try {
				line[i] = Pattern
						.compile("^\\s*\\w+\\.?\\s*$", Pattern.MULTILINE)
						.matcher(line[i]).replaceAll("");
				line[i] = Pattern.compile("^\\s*$", Pattern.MULTILINE)
						.matcher(line[i]).replaceAll("");

			} catch (Exception e) {

			}
			// System.err.println(line[i] + "***");
			inline = line[i].split(
					"((?<=\\d{2}\\/\\d{2}\\/\\d{4})\\s)|((?<=\\d{2}\\/\\d{4})\\s)");

			if (inline.length == 2) {

				JSONObject sample = new JSONObject();
				sample.put("remainder_date", inline[0].trim());
				sample.put("remainder_description",
						inline[1].trim().replaceAll("\\s{2,}", " "));
				sample.put("remainder_last_seen_date", "");
				jarray.put(sample);

			}
			if (inline.length == 1 && !(inline[0].equals(""))) {

				JSONObject sample = new JSONObject();
				sample.put("remainder_date", "");
				sample.put("remainder_description",
						inline[0].trim().replaceAll("\\s{2,}", " "));
				sample.put("remainder_last_seen_date", "");
				jarray.put(sample);

			}
		}
		return jarray;
	}

	private static boolean dateCheck(String noInvoice) {
		Pattern date = Pattern.compile(
				"^\\s*(\\d{2}\\/\\d{2}\\/\\d{4}|\\d{2}\\/\\d{4})",
				Pattern.MULTILINE);
		Matcher match = date.matcher(noInvoice);
		if (match.find()) {
			return true;
		}
		return false;
	}
}
