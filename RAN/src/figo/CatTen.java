package figo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class CatTen {
	public static void main(String args[]) throws IOException {
		File txtFile = new File(
				"/home/aberami/Documents/Figo/Cat10/Scan_0023 (3).txt");
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
		int flag = 1;
		int count = 0;
		JSONObject lineJson = new JSONObject();
		while (flag == 1) {
			count++;
			String matText = "";
			String[] line = new String[10];
			JSONArray lineArray = new JSONArray();

			String subString = "";
			Pattern lineItem = Pattern.compile(
					"^.+(\\s\\d+(\\.\\d+\\s\\w+)?\\s).+(\\d+\\.?\\d+)?",
					Pattern.MULTILINE);
			Pattern start = Pattern.compile("PATIENT");
			Pattern end = Pattern.compile("INVOICE TOTAL");
			int endPoint = 0;
			Matcher mat = end.matcher(data);
			if (mat.find()) {
				endPoint = mat.end();
				subString = data.substring(0, mat.start());
			}
			mat = start.matcher(subString);
			if (mat.find()) {
				subString = subString.substring(mat.start());
			}

			// System.err.println(subString);
			mat = lineItem.matcher(subString);
			while (mat.find()) {
				matText = mat.group().trim();
				// System.err.println(matText);

				line = matText.split("\\s{2,}");
				if (line.length == 4) {
					JSONObject temp = new JSONObject();
					temp.put("name", line[0].trim());
					temp.put("description", line[1].trim());
					temp.put("quantity", line[2].trim());
					temp.put("total_price", line[3].trim());
					temp.put("price", "");
					temp.put("date", "");
					temp.put("discount", "");
					lineArray.put(temp);
				}
				if (line.length == 3) {
					if (line[2].trim().matches("\\d+[.\\s]\\d+")) {
						JSONObject temp = new JSONObject();
						temp.put("name", "");
						temp.put("description", line[0].trim());
						temp.put("quantity", line[1].trim());
						temp.put("total_price", line[2].trim());
						temp.put("price", "");
						temp.put("date", "");
						temp.put("discount", "");
						lineArray.put(temp);
					} else {
						JSONObject temp = new JSONObject();
						temp.put("name", line[0].trim());
						temp.put("description", line[1].trim());
						temp.put("quantity", line[2].trim());
						temp.put("total_price", "");
						temp.put("price", "");
						temp.put("date", "");
						temp.put("discount", "");
						lineArray.put(temp);
					}
				}
				if (line.length == 2) {
					JSONObject temp = new JSONObject();
					temp.put("name", "");
					temp.put("description", line[0].trim());
					temp.put("quantity", line[1].trim());
					temp.put("total_price", "");
					temp.put("price", "");
					temp.put("date", "");
					temp.put("discount", "");
					lineArray.put(temp);
				}
			}

			lineJson.put("Line_items " + Integer.toString(count), lineArray);
			// System.err.println(count);
			data = data.substring(endPoint);
			mat = start.matcher(data);
			if (!(mat.find())) {
				flag = 0;
			}
		}
		System.out.println(lineJson.toString(2));
	}

	private static void reminderExtract(String data) {
		int flag = 1;
		int count = 0;
		JSONObject reminderJson = new JSONObject();
		while (flag == 1) {
			count++;
			String matText = "";
			String[] line = new String[10];
			JSONArray reminderArray = new JSONArray();
			String subString = "";
			Pattern start = Pattern.compile("REMINDERS");
			Pattern end = Pattern.compile("APPOINTMENTS");
			Pattern reminder = Pattern.compile("^.+?(\\d+\\/\\d+\\/\\d+).+?$",
					Pattern.MULTILINE);

			int endPoint = 0;
			Matcher mat = end.matcher(data);
			if (mat.find()) {
				endPoint = mat.end();
				subString = data.substring(0, mat.start());
			}
			mat = start.matcher(subString);
			if (mat.find()) {
				subString = subString.substring(mat.start());
			}
			// System.err.println(subString);
			mat = reminder.matcher(subString);
			while (mat.find()) {
				matText = mat.group().trim().replaceAll("^\\w\\s{2,}", "");
				// System.err.println(matText);

				line = matText.split("\\s{2,}");
				if (line.length == 3
						&& line[1].trim().matches("\\d+\\/\\d+\\/\\d+")) {
					JSONObject temp = new JSONObject();
					temp.put("remainder_date", line[1].trim());
					temp.put("remainder_description", line[2].trim());
					temp.put("remainder_last_seen_date", "");
					reminderArray.put(temp);
				}
			}
			reminderJson.put("Remainder " + Integer.toString(count),
					reminderArray);

			data = data.substring(endPoint);
			mat = start.matcher(data);
			if (!(mat.find())) {
				flag = 0;
			}
		}
		System.out.println(reminderJson.toString(2));
	}

}
