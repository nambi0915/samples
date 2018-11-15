package figo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class CatTwenty {
	public static void main(String args[]) throws IOException {
		File txtFile = new File(
				"/home/aberami/Documents/Figo/Category20/00 PM (65).txt");
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
		lineItemExtract(fileData);
	}

	private static void lineItemExtract(String fileData) {
		JSONArray lineItemsArray = new JSONArray();
		JSONObject lineItemsJson = new JSONObject();
		String samp = "";
		String[] line = new String[10];
		String test = "";
		Pattern lineItem = Pattern.compile(
				"^\\s*?(Performed)\\s*(\\d+\\/\\d+\\/\\d+).+?$",
				Pattern.MULTILINE);
		Matcher mat = Pattern.compile("Invoice Items").matcher(fileData);
		if (mat.find()) {
			samp = fileData.substring(mat.start());
		}
		mat = lineItem.matcher(samp);
		while (mat.find()) {
			test = mat.group().trim();
			System.err.println(test);
			line = test.split("\\s{2,}");
			if (line.length == 5) {
				JSONObject temp = new JSONObject();
				temp.put("quantity", line[3].trim());
				temp.put("description", line[2].trim());
				temp.put("date", line[1].trim());
				temp.put("price", "");
				temp.put("totalPrice", line[4].trim());
				lineItemsArray.put(temp);
			}
			if (line.length == 6) {
				JSONObject temp = new JSONObject();
				temp.put("quantity", line[4].trim());
				temp.put("description", line[2].trim() + line[3].trim());
				temp.put("date", line[1].trim());
				temp.put("price", "");
				temp.put("totalPrice", line[5].trim());
				lineItemsArray.put(temp);
			}
		}
		lineItemsJson.put("lineItems", lineItemsArray);
		System.out.println(lineItemsJson.toString(2));
	}
}
