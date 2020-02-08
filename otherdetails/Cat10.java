package otherdetails;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class Cat10 {
	public static void main(String args[]) throws IOException {
		File txtFile = new File("/home/aberami/Documents/Figo/Category10/working/Invoice template-2.txt");
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
		new Cat10().hospitalDetailsExtract(fileData);
		new Cat10().patientDetailsExtract(fileData);
		// System.out.println(fileData);
	}

	private void patientDetailsExtract(String data) {
		Map<String, String> patientMap = new HashMap<String, String>();
		Matcher patient = Pattern.compile("([Ii]NVOICE).*?([Pp]ATIENT|description)", Pattern.DOTALL).matcher(data);
		String patientDetails = null;
		if (patient.find()) {
			patientDetails = patient.group().trim();
		}
		if (patientDetails != null) {
			// System.err.println(patientDetails);
			Matcher invoice = Pattern.compile("([^0-9A-Za-z] \\d{5,6})").matcher(patientDetails);
			if (invoice.find())
				patientMap.put("invoiceNumber", invoice.group().trim());
			int dateCount = 0;
			Matcher date = Pattern.compile("\\d{1,2}[\\/-]\\d{1,2}[\\/-]\\d{4}").matcher(patientDetails);
			while (date.find()) {
				patientMap.put(dateCount == 0 ? "dueDate" : "invoiceDate", date.group().trim());
				dateCount++;
			}
		}
		System.out.println(new JSONObject(patientMap).toString(2));

	}

	private void hospitalDetailsExtract(String data) {
		JSONObject hospitalJson = new JSONObject();
		String hospitalDetails = null;
		Matcher hospital = Pattern.compile(".*?(?=INVOICE #|FOR)", Pattern.DOTALL).matcher(data);
		if (hospital.find()) {
			hospitalDetails = hospital.group().trim().replaceAll("[iI]nvoice|INVOICE", "");
		}
		if (hospitalDetails != null) {
			// System.err.println(hospitalDetails);
			Matcher phone = Pattern.compile("(\\(?\\d{3}[\\/\\)-]? ?\\d{3} ?\\-? ?\\d{4})").matcher(hospitalDetails);
			if (phone.find()) {
				hospitalJson.put("phone", phone.group().trim());
			}
			Matcher name = Pattern.compile("^(?=.*(veterinary|animal[^s]|hospital| care |pet|clinic)).*$",
					Pattern.MULTILINE | Pattern.CASE_INSENSITIVE).matcher(hospitalDetails);
			if (name.find()) {
				hospitalJson.put("hospitalName",
						name.group().replaceAll("[^A-Za-z- ]", "").replaceAll("\\s{2,}", "").trim());
			}
			Matcher web = Pattern.compile("www\\.([^.]+?)(.?com|.org)").matcher(hospitalDetails);
			if (web.find()) {
				hospitalJson.put("hospitalLink", web.group().replaceAll("\\s", "").replaceAll("(?<!\\.)(?=com)", "."));
				hospitalDetails = hospitalDetails.replaceAll(web.group(), "");
			}
			Matcher address = Pattern.compile("\\d{2,}.*?([A-Z]{2} ?\\d{2,})", Pattern.DOTALL).matcher(hospitalDetails);
			if (address.find()) {
				hospitalJson.put("address", address.group().replaceAll("\\n", ",").replaceAll("[^A-Za-z\\d,. ]", "")
						.replaceAll("\\s{2,}", "").replaceAll(",,", "").trim());
			}
		}
		System.out.println(hospitalJson.toString(2));
	}

}
