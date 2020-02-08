package figo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

public class TwoCat {
	public static void main(String args[]) throws IOException {
		File textFile = new File(
				"/home/aberami/Documents/Figo/Category2/WAVC08.06.208.1.txt");
		FileReader fileReader = new FileReader(textFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String test = null;
		StringBuilder str = new StringBuilder();
		while ((test = bufferedReader.readLine()) != null) {
			str.append(test);
			str.append("\n");
		}
		bufferedReader.close();
		String fileData = str.toString();
		str.setLength(0);
		reminderExtraction(fileData);
	}

	public static void reminderExtraction(String fileContents) {
		int multipleInvoiceFlag = 1;
		int pageCount = 0;
		JSONObject remindersJson = new JSONObject();
		while (multipleInvoiceFlag == 1) {
			pageCount++;
			String matchedText = null;
			JSONArray remindersArray = new JSONArray();
			// String[] line = new String[10];
			Pattern reminderEnd = Pattern.compile("(Reminder|RemrQer)\\s+");
			Pattern reminder = Pattern.compile(
					"(\\d{2}\\/\\d{2}\\/\\d{4}|[^\\/]\\d{2}\\/\\d{4})[\\s\\S]+?(?=\\d{2}\\/\\d{2}\\/\\d{4}|Invoice\\s[^#]|Thank\\s|IF\\s|Our\\s|B[eo]cause\\s)|(\\d{2}\\/\\d{2}\\/\\d{4}|[^\\/]\\d{2}\\/\\d{4})[\\s\\S]+?(?=^\\s*$)",
					Pattern.MULTILINE);
			Matcher matcher = null;
			matcher = reminderEnd.matcher(fileContents);
			String subString = null;
			int reminderEndPoint = 0;
			if (matcher.find()) {
				reminderEndPoint = matcher.end();
				subString = fileContents.substring(matcher.start());
			}

			if (subString != null) {
				Pattern invoice = Pattern.compile("[Ii]nvoice\\s[^#]");
				matcher = invoice.matcher(subString);
				if (matcher.find()) {
					subString = subString.substring(0, matcher.end());
					reminderEndPoint = reminderEndPoint + matcher.end();
				}
				System.err.println(subString);
				// int count = 0;
				// System.err.println(subString);
				matcher = reminder.matcher(subString);
				while (matcher.find()) {
					// count++;
					matchedText = matcher.group().trim();
					// System.err.println(test);
					remindersArray = insertToJarray(matchedText,
							remindersArray);
				}
				remindersJson.put("remainders " + Integer.toString(pageCount),
						remindersArray);

				// System.err.println(count);
			} else {
				remindersJson.put("remainders", remindersArray);
			}
			fileContents = fileContents.substring(reminderEndPoint);
			matcher = reminderEnd.matcher(fileContents);
			if (!(matcher.find())) {
				multipleInvoiceFlag = 0;
			}
		}
		System.out.println(remindersJson.toString(2));
	}

	private static JSONArray insertToJarray(String matchedString,
			JSONArray jarray) {
		String[] line = new String[10];
		line = matchedString.split("\\n");
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
			if (inline.length == 2 && inline[0].trim()
					.matches("\\d+\\/\\d+\\/\\d+|\\d+\\/\\d+")) {
				JSONObject tempJson = new JSONObject();
				tempJson.put("remainder_date", inline[0].trim());
				tempJson.put("remainder_description",
						inline[1].trim().replaceAll("\\s{2,}", " "));
				tempJson.put("remainder_last_seen_date", "");
				jarray.put(tempJson);
			}
			if (inline.length == 1 && !(inline[0].equals(""))) {
				JSONObject tempJson = new JSONObject();
				tempJson.put("remainder_date", "");
				tempJson.put("remainder_description",
						inline[0].trim().replaceAll("\\s{2,}", " "));
				tempJson.put("remainder_last_seen_date", "");
				jarray.put(tempJson);
			}
		}
		return jarray;
	}
}