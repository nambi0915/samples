package figo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class CatTwelve {
	public static void main(String args[]) throws IOException {
		File txtFile = new File(
				"/home/aberami/Documents/Figo/Category12/working/Milo.txt");
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
		Pattern lineItem = Pattern.compile(
				"^\\s*(\\d+\\/\\d+\\/\\d+)\\s*(\\w+\\s)(.+?)(\\d+\\.\\d+)\\s*(-?\\d+\\.\\d+)",
				Pattern.MULTILINE);
		String test = "";
		String patientName = "";
		String[] line = new String[10];
		Matcher mat = lineItem.matcher(fileData);
		mat = lineItem.matcher(fileData);
		while (mat.find()) {
			test = mat.group().trim();
			System.out.println(test);
			line = test.split(
					"\\s{2,}|(?<=\\d{3})\\s(?=\\w)|(?<=\\d{1}\\/\\d{2}\\/\\d{4})\\s");
			if (line.length == 5) {
				JSONObject temp = new JSONObject();
				temp.put("quantity", line[3].trim());
				if (line[1].trim().matches("[A-Za-z]+"))
					patientName = line[1].trim();
				else
					temp.put("doctor", line[1].trim());
				temp.put("description", line[2].trim());
				temp.put("date", line[0].trim());
				temp.put("price", "");
				temp.put("totalPrice", line[4].trim());
				lineItemsArray.put(temp);
			}
			if (line.length == 6) {
				JSONObject temp = new JSONObject();
				temp.put("quantity", line[3].trim());
				if (line[1].trim().matches("[A-Za-z]+"))
					patientName = line[1].trim();
				else
					temp.put("doctor", line[2].trim());
				temp.put("description", line[1].trim());
				temp.put("date", line[0].trim());
				temp.put("price", line[4].trim());
				temp.put("totalPrice", line[5].trim());
				lineItemsArray.put(temp);
			}
		}
		lineItemsJson.put("patientName", patientName != "" ? patientName : "");
		lineItemsJson.put("lineItems", lineItemsArray);
		System.out.println(lineItemsJson.toString(2));
	}

}
