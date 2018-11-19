import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class Bouli {
	public static void main(String args[]) throws IOException {
		File file = new File("/home/aberami/Documents/txt/boul1.txt");
		FileReader fr = new FileReader(file);
		BufferedReader bf = new BufferedReader(fr);
		String test = null;
		StringBuilder str = new StringBuilder();
		JSONObject json = new JSONObject();
		JSONArray jarray = new JSONArray();
		String[] line = new String[10];

		while ((test = bf.readLine()) != null) {
			str.append(test);
			str.append("\n");
		}
		bf.close();
		String data = str.toString();
		System.out.println(data);
		Matcher mat = null;
		Pattern name = Pattern.compile("(?<=Client:\\s).+(?=Invoice No:)");
		Pattern invoiceNo = Pattern.compile("(?<=Invoice No:).+");
		Pattern invoiceDate = Pattern.compile("(?<=Invoice Date:).+");
		Pattern address1 = Pattern.compile("(?<=Address:).+(?=Invoice Date:)");
		Pattern address2 = Pattern.compile(".+(?=Terms:)");
		Pattern total = Pattern.compile("(?<=Total:).+");
		Pattern balance = Pattern.compile("(?<=Balance Due:).+");
		Pattern desc = Pattern.compile("(?=.*(\\w{3}\\s\\d{2},\\s\\d{4})).+?(\\$\\d+\\.\\d+)");
		// Matches the client Name
		mat = name.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			System.out.println(test);
			json.put("name", test);
		}
		// Matches invoice Number
		mat = invoiceNo.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			System.out.println(test);
			json.put("invoice_no", test);
		}
		// Matches the invoice Date
		mat = invoiceDate.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			System.out.println(test);
			json.put("invoice_date", test);
		}
		// Matches the client Address
		mat = address1.matcher(data);
		while (mat.find()) {
			test = mat.group().trim();
			StringBuilder addr = new StringBuilder();
			addr.append(test + " ");
			System.out.println(test);
			mat = address2.matcher(data);
			if (mat.find()) {
				test = mat.group().trim();
				System.out.println(test);
				addr.append(test);
				json.put("address", addr.toString());
			}

		}
		// Match the total amount
		mat = total.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			System.out.println(test);
			json.put("total", test);
		}
		// Matches the balance amount
		mat = balance.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			System.out.println(test);
			json.put("balance", test);
		}
		// Matches the description
		mat = desc.matcher(data);
		while (mat.find()) {
			test = mat.group().trim();
			System.out.println(test);
			line = test.split("\\s{2,}");
			if (line.length == 5) {
				JSONObject samp = new JSONObject();
				samp.put("name", line[0]);
				samp.put("date", line[1]);
				samp.put("description", line[2]);
				samp.put("quantity", line[3]);
				samp.put("amount", line[4]);
				jarray.put(samp);
			}

			// System.out.println(test);
		}
		json.put("Description", jarray);
		System.out.println(json);
	}
}
