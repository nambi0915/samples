package figo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class CatFour {
	public static void main(String args[]) throws IOException {
		File txtFile = new File(
				"/home/aberami/Documents/Figo/Category4/Working/VSC-Bailey-SurgicalCheckup-030918.txt");
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
	}

	private static void lineExtraction(String fileData) {
		String[] line = new String[10];
		String matText = "";
		String subString = "";
		JSONArray lineJarray = new JSONArray();
		JSONObject lineJson = new JSONObject();
		int lineCount = 0;
		Pattern lineItem = Pattern.compile(
				"^.+?((\\s{2,})\\w[\\.,]?\\w*?\\s+(\\d+\\.\\d+%)?\\s+)[\\$S]\\s?\\d.+?$",
				Pattern.MULTILINE);
		Pattern lineLimit = Pattern
				.compile("(Sub\\s?total)|(Payment)|(PAYMENT)");
		Matcher match = lineLimit.matcher(fileData);
		if (match.find()) {
			subString = fileData.substring(0, match.start());
		}
		// System.out.println(samp);
		match = lineItem.matcher(subString);
		while (match.find()) {
			matText = match.group().trim();
			lineCount++;
			System.out.println(matText);
			line = matText.split("\\s{2,}|\\s(?=\\$)");
			if (line.length == 7) {
				JSONObject temp = new JSONObject();
				temp.put("description", line[0].trim());
				temp.put("staff", line[1].trim());
				temp.put("quantity", line[2].trim());
				temp.put("discount", line[4].trim());
				temp.put("total_price", line[6].trim());
				temp.put("price", "");
				temp.put("date", "");
				lineJarray.put(temp);
			}
			if (line.length == 3) {
				JSONObject temp = new JSONObject();
				temp.put("description", line[0].trim());
				temp.put("quantity", line[1].trim());
				temp.put("total_price", line[2].trim());
				temp.put("price", "");
				temp.put("discount", "");
				temp.put("date", "");
				lineJarray.put(temp);

			}
			if (line.length == 4) {
				JSONObject temp = new JSONObject();
				temp.put("description", line[0].trim());
				temp.put("staff", line[1].trim());
				temp.put("quantity", line[2].trim());
				temp.put("total_price", line[3].trim());
				temp.put("price", "");
				temp.put("discount", "");
				temp.put("date", "");
				lineJarray.put(temp);

			}
		}
		lineJson.put("line_items", lineJarray);
		System.out.println(lineJson.toString(2));
		System.err.println(lineCount);
	}

}
