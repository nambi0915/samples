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

public class Cat8 {
	public static String invoiceNumber = null;
	public static String invoiceDate = null;
	public static void main(String args[]) throws IOException {
		File txtFile = new File(
				"/home/aberami/Documents/Figo/Category8/00 AM (3).txt");
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
		new Cat8().hospitalDetailsExtract(fileData);
		new Cat8().patientDetailsExtract(fileData);
		// System.out.println(fileData);
	}

	private void patientDetailsExtract(String fileData) {
		Map<String, String> patientMap = new HashMap<String, String>();
		patientMap.put("invoiceNumber", invoiceNumber);
		patientMap.put("invoiceDate", invoiceDate);
		System.out.println(patientMap);
	}

	private void hospitalDetailsExtract(String data) {
		JSONObject hospitalJson = new JSONObject();
		Matcher hospital = Pattern
				.compile(".*?(?=client)",
						Pattern.DOTALL | Pattern.CASE_INSENSITIVE)
				.matcher(data);
		String hospitalDetails = null;
		if (hospital.find()) {
			hospitalDetails = hospital.group().trim();
		}
		if (hospitalDetails != null) {
			System.err.println(hospitalDetails);
			Matcher phone = Pattern
					.compile("(\\(?\\d{3}[\\/\\)-]? ?\\d{3} ?\\-? ?\\d{4})")
					.matcher(hospitalDetails);
			if (phone.find()) {
				hospitalJson.put("phone", phone.group().trim());
			}
			Matcher web = Pattern.compile(
					"(?![A-Za-z ]*\\s{2,})[A-Za-z 0-9.]+@.*?\\.c(o|01)[nm]")
					.matcher(data);
			if (web.find()) {
				hospitalJson.put("hospitalLink",
						web.group().replaceAll("\\s", "")
								.replaceAll("(?<!\\.)(?=com)", "."));
				hospitalDetails = hospitalDetails.replaceAll(web.group(), "");
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
					.compile("\\d{2,}[\\S ]+?([A-Z]{2} ?\\d{2,})",
							Pattern.DOTALL)
					.matcher(hospitalDetails);
			if (address.find()) {
				hospitalJson.put("address",
						address.group().replaceAll("\\n|\\|", ",")
								.replaceAll("[^A-Za-z\\d,. ]", "")
								.replaceAll("\\s{2,}", "").replaceAll(",,", "")
								.trim());
			}
			Matcher invoice = Pattern.compile(
					"Date:\\s*(\\d+\\/\\d+\\/\\d+).*?[Ii]nvoice:\\s*(\\d+)")
					.matcher(hospitalDetails);
			if (invoice.find()) {
				invoiceNumber = invoice.group(2).trim();
				invoiceDate = invoice.group(1).trim();
			}
		}

		System.out.println(hospitalJson.toString(2));
	}

}
