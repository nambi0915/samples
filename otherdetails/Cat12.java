package otherdetails;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class Cat12 {
	public static void main(String args[]) throws IOException {
		File txtFile = new File(
				"/home/aberami/Documents/Figo/Category12/working/00 PM (40).txt");
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
		new Cat12().hospitalDetailsExtract(fileData);
		new Cat12().otherDetailsExtract(fileData);
		// System.out.println(fileData);
	}

	private void otherDetailsExtract(String data) {
		JSONObject invoiceJson = new JSONObject();
		Matcher number = Pattern.compile("invoice number:\\s*(\\d{5,6})",
				Pattern.CASE_INSENSITIVE).matcher(data);
		if (number.find()) {
			invoiceJson.put("invoiceNumber", number.group(1).trim());
		}
		Matcher date = Pattern.compile("date:\\s*(\\d+\\/\\d+\\/\\d+)",
				Pattern.CASE_INSENSITIVE).matcher(data);
		if (date.find()) {
			invoiceJson.put("inovoiceDate", date.group(1).trim());
		}
		System.out.println(invoiceJson.toString(2));
	}

	private void hospitalDetailsExtract(String data) {
		JSONObject hospitalJson = new JSONObject();
		Matcher hospital = Pattern
				.compile(".*(?=client\\s*information)",
						Pattern.CASE_INSENSITIVE | Pattern.DOTALL)
				.matcher(data);
		String hospitalDetails = null;
		if (hospital.find()) {
			hospitalDetails = hospital.group()
					.replaceAll("INVOICE|[iI]nvoice", "").trim();
		}
		if (hospitalDetails != null) {
			System.err.println(hospitalDetails);
			Matcher phone = Pattern
					.compile("(\\(?\\d{3}[\\/\\)-]? ?\\d{3} ?\\-? ?\\d{4})")
					.matcher(hospitalDetails);
			if (phone.find()) {
				hospitalJson.put("phone", phone.group().trim());
			}
			Matcher name = Pattern.compile(
					"^(?=.*(veterinary|animal|hospital|vet|pet|clinic)).*$",
					Pattern.MULTILINE | Pattern.CASE_INSENSITIVE)
					.matcher(hospitalDetails);
			if (name.find()) {
				hospitalJson.put("hospitalName",
						name.group().replaceAll("[^A-Za-z-& ]", "")
								.replaceAll("\\s{2,}", "").trim());
			}
			Matcher address = Pattern
					.compile("\\d{2,}.*?([A-Z]{2} ?\\d{2,})", Pattern.DOTALL)
					.matcher(hospitalDetails);
			if (address.find()) {
				hospitalJson.put("address",
						address.group().replaceAll("\\n", ",")
								.replaceAll("[^A-Za-z\\d,. ]", "")
								.replaceAll("\\s{2,}", "").replaceAll(",,", "")
								.trim());
			}
			Matcher web = Pattern
					.compile("(?!.*@)\\s(www\\.)?(\\S+)(.?com|.org)")
					.matcher(hospitalDetails);
			if (web.find()) {
				hospitalJson.put("hospitalLink",
						web.group().replaceAll("\\s", "")
								.replaceAll("(?<!\\.)(?=com)", "."));
				hospitalDetails = hospitalDetails.replaceAll(web.group(), "");
			}
			Matcher email = Pattern
					.compile("(?![A-Za-z ]*\\s{2,})[A-Za-z ]+@.*?\\.com")
					.matcher(hospitalDetails);
			if (email.find()) {
				hospitalJson.put("emailId",
						email.group().replaceAll(" ", "").trim());
				hospitalDetails = hospitalDetails.replaceAll(email.group(), "");
			}
		}
		System.out.println(hospitalJson.toString(2));
	}

}
