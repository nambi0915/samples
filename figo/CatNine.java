package figo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class CatNine {
	public static void main(String args[]) throws IOException {
		File txtFile = new File(
				"/home/aberami/Documents/Figo/Cat9/VSC-Bailey-SurgicalCheckup-030918.txt");
		FileReader fileReader = new FileReader(txtFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String temp = null;
		StringBuilder fileContent = new StringBuilder();

		while ((temp = bufferedReader.readLine()) != null) {
			fileContent.append(temp);
			fileContent.append("\n");
		}
		bufferedReader.close();
		// System.out.println("\n\n\n");
		String fileData = fileContent.toString();
		fileContent.setLength(0);
		// System.out.println(fileData);
		reminderExtract(fileData);
		linItemExtract(fileData);
	}

	private static void linItemExtract(String data) {
		String matText = "";
		String[] line = new String[10];
		JSONArray lineArray = new JSONArray();
		JSONObject lineJson = new JSONObject();
		Pattern lineItem = Pattern.compile(
				"^.+(\\d{1,2}\\/\\d{1,2}\\/\\d{4}).+?(\\$).+",
				Pattern.MULTILINE);
		// Pattern lineItem = Pattern.compile(
		// "(^\\s*([\\w.]+\\s*){1,3},?\\s*\\n^\\n)?^.+(\\d{1,2}\\/\\d{1,2}\\/\\d{4}).+?(\\$).+?$",
		// Pattern.MULTILINE);
		Matcher match = null;
		match = lineItem.matcher(data);
		while (match.find()) {
			matText = match.group().trim();
			// System.err.println(test);
			line = matText.split(
					"\\s{2,}|\\s(?=\\$)|\\s(?=\\d\\s+\\$)|(?<=DVM|D\\.V\\.M)\\s");
			// System.out.println(line.length);

			if (line.length == 8) {
				JSONObject temp = new JSONObject();
				temp.put("name", line[0].trim());
				temp.put("provider", line[1].trim());
				temp.put("description", line[2].trim());
				temp.put("date", line[3].trim());
				temp.put("quantity", line[4].trim());
				temp.put("price", line[5].trim());
				temp.put("discount", "");
				temp.put("total_price", line[7].trim());
				lineArray.put(temp);
			} else if (line.length == 7) {
				if (line[2].trim().matches("\\d+\\/\\d+\\/\\d+")) {
					// System.err.println(test);
					JSONObject temp = new JSONObject();
					try {
						String patientAndProvider = line[0].trim();
						String patientName = patientAndProvider.substring(0,
								patientAndProvider.indexOf(" "));
						String providerName = patientAndProvider
								.substring(patientAndProvider.indexOf(" "));
						temp.put("name", patientName);
						temp.put("provider", providerName);
						temp.put("description", line[1].trim());
						temp.put("date", line[2].trim());
						temp.put("quantity", line[3].trim());
						temp.put("price", line[4].trim());
						temp.put("discount", "");
						temp.put("total_price", line[6].trim());
						lineArray.put(temp);
					} catch (Exception e) {

					}

				} else {
					JSONObject temp = new JSONObject();
					temp.put("name", line[0].trim());
					temp.put("provider", line[1].trim());
					temp.put("description", line[2].trim());
					temp.put("date", line[3].trim());
					temp.put("quantity", line[4].trim());
					temp.put("price", line[5].trim());
					temp.put("discount", "");
					temp.put("total_price", line[6].trim());
					lineArray.put(temp);
				}
			} else {
				String patientName = matText.substring(0, matText.indexOf(" "));
				matText = matText.substring(matText.indexOf(" ")).trim();
				line = matText.split(
						"(?<=D\\.V\\.M.|DVM)\\s|\\s(?=\\d+\\/\\d+\\/\\d+)|\\s(?=\\$)|\\s(?=\\d\\s+\\$)");
				if (line.length == 7) {
					JSONObject temp = new JSONObject();
					temp.put("name", patientName);
					temp.put("provider",
							line[0].trim().replaceAll("\\s{2,}", "\\s"));
					temp.put("description", line[1].trim());
					temp.put("date", line[2].trim());
					temp.put("quantity", line[3].trim());
					temp.put("price", line[4].trim());
					temp.put("discount", "");
					temp.put("total_price", line[6].trim());
					lineArray.put(temp);
				}

			}
		}
		lineJson.put("Line_items", lineArray);
		System.out.println(lineJson.toString(2));
	}

	private static void reminderExtract(String data) {
		Pattern remind = Pattern.compile("Reminders");
		Pattern reminder = Pattern.compile(".+(\\d+\\/\\d+\\/\\d+)\\s*");
		String matText = "";
		String[] line = new String[10];
		String subString = "";
		JSONArray reminderArray = new JSONArray();
		JSONObject reminderJson = new JSONObject();
		Matcher match = remind.matcher(data);
		if (match.find()) {
			subString = data.substring(match.start());
		}
		// System.err.println(subString);
		match = reminder.matcher(subString);
		while (match.find()) {
			matText = match.group().trim();
			line = matText.split("\\s{2,}");
			if (line.length == 2) {
				JSONObject temp = new JSONObject();
				temp.put("remainder_date", line[1].trim());
				temp.put("remainder_description", line[0].trim());
				temp.put("remainder_last_seen_date", "");
				reminderArray.put(temp);
			}
		}
		reminderJson.put("Remainder", reminderArray);
		System.out.println(reminderJson.toString(2));
	}
}
