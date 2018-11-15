package figo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class CatSixTeen {
	public static void main(String args[]) throws IOException {
		File txtFile = new File(
				"/home/aberami/Documents/Figo/Category16/Figo Sample Invoices.txt");
		FileReader fileReader = new FileReader(txtFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String test = null;
		StringBuilder str = new StringBuilder();

		while ((test = bufferedReader.readLine()) != null) {
			str.append(test);
			str.append("\n");
		}
		bufferedReader.close();
		// System.out.println("\n\n\n");
		String fileData = str.toString();
		str.setLength(0);
		// System.out.println(fileData);
		reminderExtract(fileData);
		lineItemExtract(fileData);
	}

	private static void lineItemExtract(String fileData) {
		JSONArray lineItemsArray = new JSONArray();
		JSONObject lineItemsJson = new JSONObject();
		Pattern start = Pattern.compile("(Regular\\s?Fee).+?(Subtotal\\s?:)",
				Pattern.DOTALL);
		Pattern lineItem = Pattern.compile("^.+\\d+\\.\\d+(\\s?\\*)?\\s*$",
				Pattern.MULTILINE);
		String samp = "";
		String test = "";
		String[] line = new String[10];
		Matcher mat = start.matcher(fileData);
		if (mat.find()) {
			samp = mat.group().trim();
		}
		// System.out.println(samp);
		mat = lineItem.matcher(samp);
		while (mat.find()) {
			test = mat.group().trim();
			// System.out.println(test);
			line = test.split("\\s{2,}|\\s(?=\\d+\\.\\d+\\s{2})");
			if (line.length == 2) {
				JSONObject temp = new JSONObject();
				temp.put("quantity", line[1].trim());
				temp.put("description", line[0].trim());
				temp.put("date", "");
				temp.put("price", "");
				temp.put("totalPrice", "");
				lineItemsArray.put(temp);
			}
			if (line.length == 3) {
				JSONObject temp = new JSONObject();
				temp.put("quantity", line[1].trim());
				temp.put("description", line[0].trim());
				temp.put("date", "");
				temp.put("price", line[2].trim());
				temp.put("totalPrice", "");
				lineItemsArray.put(temp);

			}
			if (line.length == 4) {
				JSONObject temp = new JSONObject();
				temp.put("quantity", line[1].trim());
				temp.put("description", line[0].trim());
				temp.put("date", "");
				temp.put("price", line[2].trim());
				temp.put("totalPrice", line[3].replaceAll("\\*", "").trim());
				lineItemsArray.put(temp);
			}
		}
		lineItemsJson.put("lineItems", lineItemsArray);
		System.out.println(lineItemsJson.toString(2));
	}

	private static void reminderExtract(String fileData) {
		JSONArray remindersArray = new JSONArray();
		JSONObject remindersJson = new JSONObject();
		Pattern reminder = Pattern.compile(
				"^.+(\\d+\\/\\d+\\/\\d+\\s\\d+:\\d+\\s[AP]M).+$",
				Pattern.MULTILINE);
		Pattern start = Pattern.compile("[Nn]ext [Aa]ppointment");
		String[] line = new String[10];
		String test = "";
		String samp = "";
		Matcher mat = start.matcher(fileData);
		if (mat.find()) {
			samp = fileData.substring(mat.start());
		}
		// System.err.println(samp);
		mat = reminder.matcher(samp);
		while (mat.find()) {
			test = mat.group().trim();
			// System.err.println(test);
			line = test.split("\\s{2,}");
			if (line.length == 5) {
				JSONObject temp = new JSONObject();
				temp.put("remainderDescription", line[1].trim());
				temp.put("remainderLastSeenDate", "");
				temp.put("remainderDate",
						line[2].replaceAll("\\d+:\\d+.+", "").trim());
				remindersArray.put(temp);
			}
		}
		remindersJson.put("remainders", remindersArray);
		System.out.println(remindersJson.toString(2));
	}

}
