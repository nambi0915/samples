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

public class CatSix {
	static String hosPhone = "";
	static String invNo = "";
	static String invDate = "";
	static String doctorName = "";
	static String petName = "";

	public static void main(String args[]) throws IOException {
		File txtFile = new File(
				"/home/aberami/Documents/Figo/Category6/working/Simon%27s Invoice 3-14-18.txt");
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
		ownerExtract(fileData);
		hospitalExtract(fileData);
		otherExtraction(fileData);
	}

	private static void otherExtraction(String fileData) {
		JSONObject jsonOut = new JSONObject();
		Pattern billAmount = Pattern.compile("(?<=Bill total).+");
		jsonOut.put("invoice_no", invNo);
		jsonOut.put("invoice_date", invDate);
		jsonOut.put("doctor_name", doctorName);
		jsonOut.put("pet_name", petName);

		Matcher match = billAmount.matcher(fileData);
		if (match.find()) {
			jsonOut.put("total_amount", match.group().trim());
		}
		System.out.println(jsonOut);
	}

	// Extraction of Hospital details
	private static void hospitalExtract(String fileData) {
		HashMap<String, String> hosMap = new LinkedHashMap<String, String>();
		Pattern hospital = Pattern.compile(".*?(?=[Ff]ax|ph.|DATE|Phone:)",
				Pattern.DOTALL);
		String matText = "";
		String[] line = new String[10];
		Matcher match = null;
		match = hospital.matcher(fileData);
		if (match.find()) {
			matText = match.group().trim();
			matText = matText.replaceAll("Bill for Services", "");
			// System.err.println(matText);
			line = matText.split("\\n");
			hosMap.put("hospital_name",
					line[0].trim().replaceAll("\\s{2,}", ""));
			StringBuilder hosAddr = new StringBuilder();
			for (int i = 1; i < line.length; i++) {
				hosAddr.append(line[i].trim() + " ");
			}
			// System.err.println(hosAddr);
			String address = hosAddr.toString().replaceAll(
					"%|\\s+\\w\\s+|(?<=\\s)im(?=\\s)|(?<=^|\\s)\\w(?=\\s{2,})",
					"");
			// System.err.println(address);
			hosMap.put("hospital_address", address.replaceAll("\\s{2,}", ""));
		}
		hosMap.put("hospital_phone", hosPhone);
		System.out.println(hosMap);

	}

	private static void ownerExtract(String fileData) {
		String matText = null;
		String[] line = new String[10];
		Pattern accno = Pattern.compile(
				"(?<=Acct\\sno\\.:|Acctno\\.:).+?(?=Qty)", Pattern.DOTALL);
		Pattern owner = Pattern.compile("(?<=Tel:)\\s?.+?(?=Acct\\s?no)",
				Pattern.DOTALL);
		Pattern dateInv = Pattern.compile("(?<=NUM).+?(?=Tel:)",
				Pattern.DOTALL);
		HashMap<String, String> ownerMap = new LinkedHashMap<String, String>();
		String ownerDetail = "";
		Matcher ownMatch = owner.matcher(fileData);
		if (ownMatch.find()) {
			matText = ownMatch.group().trim();
			ownerDetail = matText;
		}
		String ownerPhone = "";
		ownMatch = Pattern
				.compile("(\\s{2,}\\d{3}\\-\\d{3}\\-\\d{4})", Pattern.DOTALL)
				.matcher(ownerDetail);
		if (ownMatch.find()) {
			ownerPhone = ownMatch.group().trim();
			ownerDetail = ownerDetail.replaceAll(ownerPhone, "");
		}
		Matcher match = dateInv.matcher(fileData);
		if (match.find()) {
			matText = match.group().trim();
			if (matText.replaceAll("\\s", "").equals("")) {

				line = ownerDetail.split("\\s{2,}");
				hosPhone = line[0].trim();
				invDate = line[1].trim();
				invNo = line[2].trim();
				ownerMap.put("owner_name", line[3].trim());
				StringBuilder ownerAddr = new StringBuilder();
				for (int i = 4; i < line.length; i++) {
					ownerAddr.append(line[i].trim() + " ");
				}
				ownerMap.put("owner_address", ownerAddr.toString());
				ownerMap.put("owner_phone", ownerPhone);
			} else {
				// System.err.println(matText);
				Pattern date = Pattern.compile("(\\d+\\/\\d+\\/\\d+).*?(\\d+)");
				Matcher inv = date.matcher(matText);
				if (inv.find()) {
					invDate = inv.group(1).trim();
					invNo = inv.group(2).trim();

				}
				// line = matText.split("\\s{2,}");
				// System.err.println(matText);
				line = ownerDetail.split("\\s{2,}");
				hosPhone = line[0].trim();
				ownerMap.put("owner_name", line[1].trim());
				StringBuilder ownerAddr = new StringBuilder();
				for (int i = 2; i < line.length; i++) {
					ownerAddr.append(line[i].trim() + " ");
				}
				ownerMap.put("owner_address", ownerAddr.toString());
				ownerMap.put("owner_phone", ownerPhone);

			}

		}
		Matcher mat = accno.matcher(fileData);
		if (mat.find()) {
			matText = mat.group().trim();
			line = matText.split("\\s{2,}");
			ownerMap.put("owner_account", line[0].trim());
			doctorName = line[1].trim();
		}
		System.out.println(ownerMap);
	}

	// Line Items Extraction
	private static void linItemExtract(String fileData) {
		String matText = "";
		String[] line = new String[10];
		JSONArray lineJarray = new JSONArray();
		JSONObject lineJson = new JSONObject();

		Pattern lineItems = Pattern.compile(
				"^(?=.*(\\d+\\/\\d+\\/\\d+)).+\\$\\d+\\.(\\d+)?.+$",
				Pattern.MULTILINE);
		Matcher match = null;

		match = lineItems.matcher(fileData);
		int lineItemCount = 0;
		while (match.find()) {
			String quantity = "";
			matText = match.group().trim();
			// System.err.println(matText);
			Pattern qty = Pattern
					.compile(".+(?=\\d{1,2}\\/\\d{1,2}\\/\\d{2,5})");
			// System.out.println(test);
			Matcher qtyMatch = qty.matcher(matText);
			if (qtyMatch.find()) {
				quantity = qtyMatch.group().trim();
				matText = matText.substring(qtyMatch.end());
			}
			// System.out.println(matText);
			line = matText.split(
					"(\\s{2,})|(\\s(?=\\d{1,2}\\/\\d{1,2}\\/\\d{4}))|((?<=\\d{2}\\/\\d{2}\\/\\d{4})\\s)|((?<=\\d{1}\\/\\d{2}\\/\\d{4})\\s)|((?<=\\d{1}\\/\\d{1}\\/\\d{4})\\s)|((?<=\\d{2}\\/\\d{1}\\/\\d{4})\\s)|((?<=\\d{1}\\/\\d{2}\\/\\d{5})\\s)|((?<=\\d{1}\\/\\d{1}\\/\\d{3})\\s)");
			if (line.length == 6) {
				if (lineItemCount == 0)
					petName = line[1].trim();
				JSONObject temp = new JSONObject();
				temp.put("quantity", quantity);
				temp.put("date", line[0].trim());
				temp.put("name", line[1].trim());
				temp.put("description", line[2].trim());
				// temp.put("staff", line[3].trim());
				temp.put("price", line[4].trim());
				temp.put("total_price", line[5].trim());
				temp.put("discount", "");
				lineJarray.put(temp);
				lineItemCount++;
			}

			if (line.length == 5) {
				if (lineItemCount == 0)
					petName = line[1].trim();
				JSONObject temp = new JSONObject();
				temp.put("quantity", quantity);
				temp.put("date", line[0].trim());
				temp.put("name", line[1].trim());
				temp.put("description", line[2].trim());
				temp.put("price", line[3].trim());
				temp.put("total_price", line[4].trim());
				temp.put("discount", "");
				lineJarray.put(temp);
				lineItemCount++;
			}
			if (line.length == 4) {
				if (lineItemCount == 0)
					petName = line[1].trim();
				JSONObject temp = new JSONObject();
				temp.put("quantity", quantity);
				temp.put("date", line[0].trim());
				temp.put("name", line[1].trim());
				temp.put("description", line[2].trim());
				temp.put("price", "");
				temp.put("total_price", line[3].trim());
				temp.put("discount", "");
				lineJarray.put(temp);
				lineItemCount++;
			}
		}
		lineJson.put("Line_items", lineJarray);
		System.out.println(lineJson.toString(2));
	}

	// Reminder Extraction
	public static void reminderExtract(String fileData) {
		String matText = null;
		String subString = null;
		String[] line = new String[10];
		JSONArray remJarray = new JSONArray();
		JSONObject remJson = new JSONObject();
		Pattern remind = Pattern.compile("\\s?Reminders:");
		Pattern reminder = Pattern.compile(
				"(\\w+):(\\s*\\d{1,2}\\/\\d{1,2}\\/\\d{2,4}\\s*):(.+)");

		Matcher match = null;
		match = remind.matcher(fileData);
		if (match.find()) {
			subString = fileData.substring(match.start());
		}
		// System.err.println(subString);
		if (subString != null) {
			match = reminder.matcher(subString);
			while (match.find()) {
				matText = match.group().trim();
				line = matText.split(":");
				// System.out.println(line.length);
				if (line.length == 3) {
					JSONObject temp = new JSONObject();
					temp.put("remainder_name", line[0].trim());
					temp.put("remainder_date", line[1].trim());
					temp.put("remainder_description", line[2].trim());
					temp.put("remainder_last_seen_date", "");
					remJarray.put(temp);
				}
				remJson.put("Remainders", remJarray);
			}
			System.out.println(remJson.toString(2));
		} else {
			remJson.put("Remainders", remJarray);
			System.out.println(remJson.toString(2));
		}
	}
}
