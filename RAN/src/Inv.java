import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class Inv {
	public static void main(String args[]) throws IOException {
		File file = new File("/home/aberami/Documents/txt/inv1.txt");
		FileReader fr = new FileReader(file);
		BufferedReader bf = new BufferedReader(fr);
		String test = null;
		StringBuilder str = new StringBuilder();
		Matcher mat = null;
		Pattern name = Pattern.compile("(?<=\\/\\d{4}\\s).+?(?=\\s{2})");
		Pattern billAmount = Pattern.compile("(?<=Bill total\\s).+\\n");
		Pattern patientAddress = Pattern.compile("(?<=\\d{7})[\\w\\s\\n(*),]+(?=Acct no)");
		// Pattern address1 = Pattern.compile("\\d+.+(?=Bill)");
		// Pattern address2 = Pattern.compile("(?<=Bill for Services)[\\w\\s,\\d]+\\n");
		Pattern date = Pattern.compile("[\\d]{2}\\/[\\d]{2}\\/[\\d]{2}");
		Pattern invoiceNumber = Pattern.compile("[\\d]{7}");
		Pattern description = Pattern.compile("(?=.*(\\d+\\/\\d+\\/\\d+)).+(\\$\\d+\\.\\d+)");
		JSONObject json = new JSONObject();
		JSONArray jarray = new JSONArray();
		String[] line = new String[10];
		while ((test = bf.readLine()) != null) {
			str.append(test);
			str.append("\n");
		}
		String data = str.toString();
		// System.out.println(data);
		mat = date.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("Date", test);
		}
		mat = invoiceNumber.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("Invoice number", test);
		}
		mat = name.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("name", test);
		}
		mat = patientAddress.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("Address", test);
		}
		mat = billAmount.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("Total amount", test);
		}
		mat = description.matcher(data);
		while (mat.find()) {
			test = mat.group().trim();
			line = test.split("(?<=\\d+\\/\\d+\\/\\d+)\\s|\\s(?=\\d+\\/\\d+\\/\\d+)|\\s{2,}");
			if (line.length == 6) {
				JSONObject samp = new JSONObject();
				samp.put("quantity", line[0]);
				samp.put("date", line[1]);
				samp.put("patient", line[2]);
				samp.put("description", line[3]);
				samp.put("amount", line[5]);
				jarray.put(samp);
			}
			if (line.length == 5) {
				JSONObject samp = new JSONObject();
				samp.put("quantity", line[0]);
				samp.put("date", line[1]);
				samp.put("patient", line[2]);
				samp.put("description", line[3]);
				samp.put("amount", line[4]);
				jarray.put(samp);
			}
		}
		json.put("Description", jarray);
		bf.close();
		System.out.println(json);
	}
}