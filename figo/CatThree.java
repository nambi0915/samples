package figo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class CatThree {
	public static void main(String args[]) throws IOException {
		File txtFile = new File(
				"/home/aberami/Documents/Figo/Cat3/Vet Invoice Eco Deco (2).txt");
		FileReader fileReader = new FileReader(txtFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String temp = null;
		StringBuilder fileContent = new StringBuilder();

		while ((temp = bufferedReader.readLine()) != null) {
			fileContent.append(temp);
			fileContent.append("\n");
		}
		bufferedReader.close();
		System.out.println("\n\n\n");
		String fileData = fileContent.toString();
		fileContent.setLength(0);
		// System.out.println(fileData);
		lineExtraction(fileData);
		reminderExtraction(fileData);
	}

	private static void reminderExtraction(String fileData) {
		String matText = "";
		int count = 0;
		JSONArray reminderArray = new JSONArray();
		JSONObject reminderJson = new JSONObject();
		String samp = "";
		Pattern remind = Pattern.compile("Performed");
		Pattern topReminder = Pattern.compile(
				"(\\s{2,})(.(?!\\s\\s))+:\\s*(\\d{2}\\/\\d{2}\\/\\d{4})?(\\s*(\\d{2}\\/\\d{2}\\/\\d{4}))?\\s*$",
				Pattern.MULTILINE);
		Matcher match = null;
		match = remind.matcher(fileData);
		if (match.find()) {
			samp = fileData.substring(match.start());
			// System.err.println(samp);
			Pattern bottomRem = Pattern.compile(
					"^.+\\s*(\\d+\\/\\d+\\/\\d+).+$", Pattern.MULTILINE);
			Matcher rem = bottomRem.matcher(samp);
			while (rem.find()) {
				matText = rem.group().trim();
				reminderArray = insertJarray(matText, reminderArray);
				count++;
			}
			reminderJson.put("Remainders", reminderArray);
			System.out.println(reminderJson.toString(2));
			System.err.println(count);
		} else {
			System.err.println("Top reminder");
			Matcher reminderLimit = Pattern
					.compile("Date\\s*[^:]|Description|Qty").matcher(fileData);
			if (reminderLimit.find()) {
				samp = fileData.substring(0, reminderLimit.start());
			}
			// System.err.println(samp);
			try {
				Matcher start = Pattern.compile("Invoice Number").matcher(samp);
				if (start.find()) {
					samp = samp.substring(start.start());
				}
			} catch (Exception e) {

			}
			// System.err.println(samp);
			match = topReminder.matcher(samp);
			while (match.find()) {
				// System.err.println(matText);
				matText = match.group().trim();
				reminderArray = insertJarray(matText, reminderArray);
				count++;
			}
			reminderJson.put("Remainders", reminderArray);
			System.out.println(reminderJson.toString(2));
			System.err.println(count);
		}
	}
	private static JSONArray insertJarray(String test, JSONArray jarray) {
		// System.err.println(test);
		String[] line = new String[10];
		line = test.split(":?\\s{2,}|(?<=\\d{2}\\/\\d{2}\\/\\d{4})\\s|:\\s|:");
		// System.err.println(line.length);
		// for (int i = 0; i < line.length; i++) {
		// System.out.println(line[i] + "**");
		// }
		if (line.length == 3 && line[2].trim().matches("\\d+\\/\\d+\\/\\d+")
				&& line[1].trim().matches("\\d+\\/\\d+\\/\\d+")) {
			JSONObject temp = new JSONObject();
			temp.put("remainder_date", line[2].trim());
			temp.put("remainder_description", line[0].trim());
			temp.put("remainder_last_seen_date", line[1].trim());
			jarray.put(temp);
		}
		if (line.length == 2 && line[1].trim().matches("\\d+\\/\\d+\\/\\d+")
				&& !(line[0].trim().matches("Rabies Tag Number|ext"))) {

			JSONObject temp = new JSONObject();
			temp.put("remainder_date", line[1].trim());
			temp.put("remainder_description", line[0].trim());
			temp.put("remainder_last_seen_date", "");
			jarray.put(temp);
		}
		if (line.length == 1 && !(line[0].trim().contains("!"))
				&& !line[0].trim().matches("Rabies\\s?Tag\\s?Number|ext")) {
			JSONObject temp = new JSONObject();
			temp.put("remainder_date", "");
			temp.put("remainder_description", line[0].trim());
			temp.put("remainder_last_seen_date", "");
			jarray.put(temp);
		}

		return jarray;
	}
	private static void lineExtraction(String fileData) {
		String matText = "";
		String[] line = new String[10];
		JSONArray lineJarray = new JSONArray();
		JSONObject lineJson = new JSONObject();
		String subString = null;
		int count = 0;
		Pattern lineItem = Pattern.compile(
				"(^\\s*(\\d+\\/\\d+\\/\\d+)?.+\\$.+$)(\\n^\\s*(Discount:|Senior Citizen:).+$)?",
				Pattern.MULTILINE);
		Pattern end = Pattern
				.compile("\\sTotal\\s?([Ili]nvoice|Products?|for)\\s?:?");
		Matcher match = end.matcher(fileData);
		if (match.find()) {
			subString = fileData.substring(0, match.start());
		}
		// System.out.println(subString);
		match = lineItem.matcher(subString);
		while (match.find()) {
			matText = match.group(1).trim();
			// System.err.println(matText);
			String discount = "";
			try {
				discount = match.group(3).trim();
			} catch (Exception e) {
			}
			Matcher discountMatch = Pattern
					.compile("(?<=(Discount:|Senior Citizen:)).+")
					.matcher(discount);
			if (discountMatch.find()) {
				discount = discountMatch.group().replaceAll("\\s{2,}", " ")
						.replaceAll("\\(|\\)", "");
			}
			matText = matText.replaceAll("\\|", "")
					.replaceAll("(?<=\\s{4}|^)[a-zA-Z]\\s{2,}", "");
			System.err.println(matText);
			line = matText.split(
					"\\s{2,}|\\s(?=\\$)|(?<=\\d{2}\\/\\d{2}\\/\\d{4})\\s");
			System.out.println(line.length);
			if (line.length == 6) {
				JSONObject temp = new JSONObject();
				// temp.put("code", line[1].trim());
				temp.put("date", line[0].trim());
				System.err.println(line[2].trim());
				temp.put("description",
						line[2].trim().replaceAll("^(.[^A-Za-z])+", "").trim());
				temp.put("quantity", line[3].trim());
				temp.put("total_price", line[4].trim() + line[5].trim());
				temp.put("price", "");
				if (discount != "")
					temp.put("discount", discount);
				else
					temp.put("discount", "");
				lineJarray.put(temp);
				count++;
			}
			if (line.length == 5) {
				JSONObject temp = new JSONObject();
				String date = line[0].trim().replaceAll("\\|", "")
						.replaceAll("\\s", "");
				if (date.matches("\\d+\\/\\d+\\/\\d+"))
					temp.put("date", date);
				else {
					temp.put("date", "");
					// temp.put("code", line[0].trim());
				}
				System.err.println(line[1].trim());
				temp.put("description",
						line[1].trim().replaceAll("^(.[^A-Za-z])+", "").trim());
				temp.put("quantity", line[2].trim());
				temp.put("total_price", line[3].trim() + line[4].trim());
				temp.put("price", "");
				if (discount != "")
					temp.put("discount", discount);
				else
					temp.put("discount", "");
				lineJarray.put(temp);
				count++;
			}
			if (line.length == 4) {
				JSONObject temp = new JSONObject();
				temp.put("date", "");
				System.err.println(line[0].trim());
				line[0] = line[0].trim().replaceAll("^(.[^A-Za-z])+", "")
						.trim();
				String desc = line[0].substring(0, line[0].indexOf(" "));
				temp.put("description", Pattern.compile(".*[A-Z].*")
						.matcher(desc).find()
						&& Pattern.compile(".*[0-9].*").matcher(desc).find()
								? line[0].substring(line[0].indexOf(" ") + 1)
								: line[0]);
				temp.put("quantity", line[1].trim());
				temp.put("total_price", line[2].trim() + line[3].trim());
				temp.put("price", "");
				if (discount != "")
					temp.put("discount", discount);
				else
					temp.put("discount", "");
				lineJarray.put(temp);
				count++;
			}
		}
		lineJson.put("Line_items", lineJarray);
		System.out.println(lineJson.toString(2));
		System.err.println(count);
	}
}
