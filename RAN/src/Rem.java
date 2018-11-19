import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class Rem {
	public static void main(String args[]) throws IOException {
		File file = new File("/home/aberami/Documents/rem/ext/op.txt");
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
		// System.out.println(data);
	}

	public static void reminderExtraction(String data) {
		String test = null;
		JSONObject json = new JSONObject();
		JSONArray jarray = new JSONArray();
		String[] line = new String[10];
		Pattern remind = Pattern.compile("Reminder|RemrQer");
		// Stack over flow error
		// Pattern reminder = Pattern.compile(
		// "(\\d{2}\\/\\d{2}\\/\\d{4})(.|\\n)+?(?=(\\d{2}\\/\\d{2}\\/\\d{4})|(?=Invoice)|(?=Advances)|(?=Bocouao))");

		Pattern reminder = Pattern.compile(
				"\\d{2}\\/\\d{2}\\/\\d{4}(.)*?((?=\\s+\\d{2}\\/\\d{2}\\/\\d{4})|(?=\\s+Invoice)|(?=\\s+Advances)|(?=\\s+Bocouao)|(?=\\s+IF))",
				Pattern.DOTALL);
		Pattern reminder3 = Pattern
				.compile("\\d{2}\\/\\d{2}\\/\\d{4}(.*?\\n)*?((?=\\d{2}\\/\\d{2}\\/\\d{4})|(?=\\s+Invoice))");
		Pattern reminder4 = Pattern.compile(
				"\\s*(\\d{2}\\/\\d{2}\\/\\d{4}|\\d{2}\\-\\d{2}\\-\\d{4}|[^\\/]\\d{2}\\/\\d{4})(.*?|\\n)*?((?=\\s*\\d{2}\\/\\d{2}\\/\\d{4})|(?=\\s*Invoice)|(?=^\\s*$))",
				Pattern.MULTILINE);
		Pattern reminder1 = Pattern.compile(
				"\\d{2}\\/\\d{2}\\/\\d{4}(.*?\\n)*?((?=\\s+\\d{2}\\/\\d{2}\\/\\d{4})|(?=Invoice)|(?=\\s+Advances)|(?=\\s+Prevent))");
		Pattern reminder2 = Pattern.compile(
				"\\d{2}\\/\\d{2}\\/\\d{4}[\\S\\s]*?((?=\\s+\\d{2}\\/\\d{2}\\/\\d{4})|(?=Invoice)|(?=\\s+Advances)|(?=\\s+Prevent))");
		Matcher mat = null;
		mat = remind.matcher(data);
		String samp = null;
		int start = 0, ends = 0;
		if (mat.find()) {
			start = mat.start();
			samp = data.substring(start);

		}
		// System.out.println(start);
		System.out.println(samp);
		if (samp != null) {
			Pattern end = Pattern.compile("Invoice\\s");
			mat = end.matcher(samp);
			if (mat.find()) {
				ends = mat.start();
				// System.out.println(ends);
				ends += 10;
				samp = samp.substring(0, ends);
			}

			// System.out.println(samp);
			// samp = data.substring(start, ends + start);
			// System.out.println(samp);
			mat = reminder4.matcher(samp);
			while (mat.find()) {
				test = mat.group().trim();
				System.err.println(test);
				line = test.split("\\n");
				for (int i = 0; i < line.length; i++) {
					String[] inline = new String[10];
					try {
						line[i] = Pattern.compile("^\\w+\\.?\\s{2,}", Pattern.MULTILINE).matcher(line[i])
								.replaceAll("");
						line[i] = Pattern.compile("^\\s*$", Pattern.MULTILINE).matcher(line[i]).replaceAll(" ");
					} catch (Exception e) {

					}
					// System.err.println(line[i]);
					inline = line[i].split("((?<=\\d{2}\\/\\d{2}\\/\\d{4})\\s)|((?<=\\d{2}\\/\\d{4})\\s)");
					if (inline.length == 2) {
						JSONObject sample = new JSONObject();
						sample.put("reminder_date", inline[0].trim());
						sample.put("reminder_description", inline[1].trim().replaceAll("\\s{2,}", " "));
						sample.put("remainder_last_seen_date", "");
						jarray.put(sample);
					}
					if (inline.length == 1 && !(inline.equals(""))) {
						JSONObject sample = new JSONObject();

						if (!(inline[0].equals(""))) {
							sample.put("reminder_date", "");
							sample.put("reminder_description", inline[0].trim().replaceAll("\\s{2,}", " "));
							sample.put("remainder_last_seen_date", "");
							jarray.put(sample);
						}

					}
				}
			}
		}
		json.put("Reminders", jarray);
		jarray = new JSONArray();
		System.out.println(json.toString(2));

	}
}
