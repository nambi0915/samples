
package figo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class CatFive {
	public static String doctorName = null;

	public static void main(String args[]) throws IOException {
		File txtFile = new File(
				"/home/aberami/Documents/Figo/Category5/working/SCAN0022.txt");
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
		// System.out.println(data);
		reminderExtraction(fileData);
		addrExtraction(fileData);
		petExtraction(fileData);
		ownerExtraction(fileData);
		lineExtraction(fileData);
		otherExtraction(fileData);

	}

	// Reminder Extraction
	public static void reminderExtraction(String fileData) {
		String matText = null;
		String subString = null;
		JSONObject jsonOut = new JSONObject();
		JSONArray reminderJarray = new JSONArray();
		String[] line = new String[10];

		Pattern rem = Pattern.compile("Reminders");
		Pattern reminder = Pattern.compile(".*?(\\d{2}\\/\\d{2}\\/\\d{4})");
		Matcher match = null;
		// Reminder Extraction
		match = rem.matcher(fileData);
		if (match.find()) {
			subString = fileData.substring(match.start());
		}
		// System.out.println(subString);
		if (subString != null) {
			match = reminder.matcher(subString);
			while (match.find()) {
				matText = match.group().trim();
				// System.err.println(matText);
				matText = matText.replaceAll("[Oo]verdue", "");
				line = matText.split("\\s{2,}");
				// System.out.println(line.length);
				if (line.length == 2
						&& line[1].trim().matches("\\d+\\/\\d+\\/\\d+")) {
					JSONObject temp = new JSONObject();
					temp.put("remainder_date", line[1].trim());
					temp.put("remainder_description",
							line[0].trim().replaceAll("\\s{2,}", " "));
					temp.put("remainder_last_seen_date", "");
					reminderJarray.put(temp);
				}
				if (line.length == 3 && !line[1].trim().contains(";")
						&& !line[1].trim().contains(":")) {
					JSONObject temp = new JSONObject();
					temp.put("remainder_date", line[2].trim());
					temp.put("remainder_description",
							line[1].trim().replaceAll("\\s{2,}", " "));
					temp.put("remainder_last_seen_date", "");
					reminderJarray.put(temp);

				}
				if (line.length == 4) {
					JSONObject temp = new JSONObject();
					temp.put("remainder_date", line[3].trim());
					temp.put("remainder_description",
							line[1].trim().replaceAll("\\s{2,}", " "));
					temp.put("remainder_last_seen_date", "");
					reminderJarray.put(temp);

				}
			}
			jsonOut.put("Reminders", reminderJarray);
			reminderJarray = new JSONArray();
		} else
			jsonOut.put("Reminders", "");
		System.out.println(jsonOut.toString(2));

	}

	// Address Extraction
	public static void addrExtraction(String fileData) {
		String matText = null;
		String subString = null;
		HashMap<String, String> addressMap = new LinkedHashMap<String, String>();
		String[] line = new String[10];
		Pattern addrEnd = Pattern.compile("Phone:");
		Pattern addr1 = Pattern
				.compile("(?<=\\d{2}\\/\\d{2}\\/\\d{4})(.*?|\\n)*?(?=Invoice)");
		// Pattern addr1 = Pattern.compile("(.|\\n)*?(?=Invoice)");
		Pattern addr2 = Pattern
				.compile("(?<=\\s\\d{5})[\\S\\s]*?(?=\\(?\\d{3}\\)?-?\\d{3}-)");
		Pattern hosPhone = Pattern
				.compile("\\(?\\d{3}\\)?-?\\d{3}-[\\w\\d]{4}");
		Matcher match = null;
		match = addrEnd.matcher(fileData);
		if (match.find()) {
			subString = fileData.substring(0, match.start());
		}
		// System.err.println(subString);
		if (subString != null) {
			match = addr1.matcher(subString);
			if (match.find()) {
				StringBuilder address = new StringBuilder();
				String hospitalName = "";
				matText = match.group().trim();
				// System.out.println(test);
				line = matText.split("\\s{2,}");
				// System.out.println(line.length);
				if (line.length == 2) {
					hospitalName = line[0].trim();
					address.append(line[1].trim() + " ");
				}
				if (line.length == 1)
					address.append(line[0].trim() + " ");

				Pattern addAddress = Pattern
						.compile(".*(?=\\d{2}\\/\\d{2}\\/\\d{4})");

				match = addAddress.matcher(fileData);
				if (match.find()) {
					if (match.group().trim().length() != 1)
						addressMap.put("hospital_name", match.group().trim());
					else
						addressMap.put("hospital_name", hospitalName);
				} else
					addressMap.put("hospital_name", hospitalName);

				match = addr2.matcher(subString);
				if (match.find()) {
					// System.err.println(matText);
					matText = match.group().trim();
					address.append(matText);
					addressMap.put("hospital_address", address.toString());
					// System.out.println(address);
				}
				match = hosPhone.matcher(subString);
				if (match.find()) {
					matText = match.group();
					// System.out.println(test);
					addressMap.put("hospital_phone", matText);
				}

			}
			System.out.println(addressMap);

		}
	}

	// Pet Details Extraction
	public static void petExtraction(String fileData) {
		String test = null;

		HashMap<String, String> petMap = new LinkedHashMap<String, String>();

		Pattern petName = Pattern.compile("(?<=Patient:).*?(?=DOB:)");
		Pattern breed = Pattern.compile("(?<=Breed:).*?(?=Sex)");
		Pattern weight = Pattern.compile("(?<=Weight:).*");
		Pattern gender = Pattern.compile("(?<=Sex:).*");
		Pattern species = Pattern.compile("(?<=Species:).*?(?=Age:)");
		Matcher match = null;

		match = petName.matcher(fileData);
		if (match.find()) {
			test = match.group().trim();
			// System.out.println(test);
			petMap.put("pet_name", test);
		}
		match = weight.matcher(fileData);
		if (match.find()) {
			test = match.group().trim();
			// System.out.println(test);
			petMap.put("pet_weight", test);
		}
		match = breed.matcher(fileData);
		if (match.find()) {
			test = match.group().trim();
			petMap.put("pet_breed", test);
		}
		match = gender.matcher(fileData);
		if (match.find()) {
			test = match.group().trim();
			petMap.put("pet_gender", test);
		}
		match = species.matcher(fileData);
		if (match.find()) {
			test = match.group().trim();
			petMap.put("pet_species", test);
		}

		System.out.println(petMap);

	}

	public static void ownerExtraction(String fileData) {
		String matText = null;
		StringBuilder address = new StringBuilder();
		HashMap<String, String> ownerMap = new LinkedHashMap<String, String>();
		Pattern trim = Pattern.compile("(?<=Phone:)[\\s\\S]*?(?=Client:)");
		Pattern name = Pattern.compile("(?<=Client:).*");
		Pattern addr1 = Pattern.compile(
				"\\d{3}\\s\\w+.*?((?=Color:)|(?=Breed:)|(?=Weight:)|(?=Species))",
				Pattern.DOTALL);
		Pattern addr2 = Pattern.compile(
				"^(?=.*(,)).*?((?=Color:)|(?=Breed:)|(?=Weight:))",
				Pattern.MULTILINE);
		Pattern phone = Pattern.compile("(?<=Phone:).*");
		String subString = null;
		Matcher match = null;
		match = trim.matcher(fileData);
		if (match.find()) {
			subString = match.group().trim();
		}
		// System.err.println(subString);
		match = name.matcher(fileData);
		if (match.find()) {
			matText = match.group().trim();
			ownerMap.put("owner_name", matText);
		}

		match = addr1.matcher(subString);
		if (match.find()) {
			matText = match.group().trim();
			address.append(matText + " ");
			// System.err.println(address);
			match = addr2.matcher(subString);
			if (match.find()) {
				matText = match.group().trim();
				address.append(matText);
				// System.err.println(test);
				// System.out.println(address);
			}
			ownerMap.put("owner_address", address.toString());
		}
		match = phone.matcher(fileData);
		if (match.find()) {
			matText = match.group().trim();
			ownerMap.put("owner_phone", matText);
		}
		System.out.println(ownerMap);
	}

	public static void lineExtraction(String fileData) {
		String matText = null;

		JSONObject jsonOut = new JSONObject();
		JSONArray lineJarray = new JSONArray();
		String[] line = new String[10];
		String providerName = null;
		Matcher match = null;
		Pattern lineitem = Pattern.compile(
				"(.*)(\\$\\d{1,}\\.\\d{2}\\s*)\\n(^\\s{2,}([\\w\\d]+\\s){1,4}\\s{2,}$)?",
				Pattern.MULTILINE);
		match = lineitem.matcher(fileData);
		int lineItemCount = 0;
		while (match.find()) {
			// test = mat.group(2).trim();
			String extendedLine = null;
			matText = match.group(1).trim() + "    " + match.group(2).trim();
			// System.out.println(test);
			try {
				extendedLine = match.group(3).trim();
			} catch (Exception e) {
				// No need to perform anything
			}
			// System.err.println(test);
			line = matText.split("(\\s{2,}|\\s(?=\\d{2}\\/\\d{2}\\/\\d{4}))");
			if (line.length == 5) {
				JSONObject temp = new JSONObject();
				temp.put("provider", line[0].trim());
				if (lineItemCount == 0)
					providerName = line[0].trim();
				if (extendedLine != null)
					temp.put("item", line[1].trim() + " " + extendedLine);
				else
					temp.put("item", line[1].trim());
				temp.put("date", line[2].trim());
				temp.put("quantity", line[3].trim());
				temp.put("total_price", line[4].trim());
				temp.put("discount", "");
				lineJarray.put(temp);
			}
			if (line.length == 6) {
				JSONObject temp = new JSONObject();
				temp.put("provider", line[0].trim());
				if (lineItemCount == 0)
					providerName = line[0].trim();
				if (extendedLine != null)
					temp.put("item", line[1].trim() + " " + extendedLine);
				else
					temp.put("item", line[1].trim());
				temp.put("discount", "");
				temp.put("date", line[2].trim());
				temp.put("quantity", line[3].trim());
				temp.put("price", line[4].trim());
				temp.put("total_price", line[5].trim());
				lineJarray.put(temp);

			}
			lineItemCount++;
		}
		doctorName = providerName;
		jsonOut.put("line_items", lineJarray);
		System.out.println(jsonOut.toString(2));
	}

	public static void otherExtraction(String fileData) {
		String matText = null;
		JSONObject jsonOut = new JSONObject();
		Pattern invoiceNo = Pattern.compile("(?<=Invoice:).*");
		Pattern invoiceDate = Pattern
				.compile("\\s+\\d{2}\\/\\d{2}\\/\\d{4}\\s+");
		Pattern total = Pattern.compile("(?<=Net Invoice).*(\\$.+?\\..+)");
		Matcher match = null;
		match = invoiceNo.matcher(fileData);
		if (match.find()) {
			matText = match.group().trim();
			jsonOut.put("invoice_no", matText);
		}
		match = invoiceDate.matcher(fileData);
		if (match.find()) {
			matText = match.group().trim();
			jsonOut.put("invoice_date", matText);
		}
		jsonOut.put("doctor_name", doctorName);
		match = total.matcher(fileData);
		if (match.find()) {
			matText = match.group(1).trim();
			jsonOut.put("total", matText);
		}
		System.out.println(jsonOut);

	}
}