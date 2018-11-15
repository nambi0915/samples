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

public class Cat4 {
	public static void main(String args[]) throws IOException {
		File txtFile = new File(
				"/home/aberami/Documents/Figo/Category4/NW/Teddy - Bandage Change.txt");
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
		new Cat4().hospitalDetailsExtract(fileData);
		new Cat4().patientDetailsExtract(fileData);
		// System.out.println(fileData);
	}

	private void patientDetailsExtract(String data) {
		Map<String, String> patientMap = new HashMap<String, String>();
		Matcher patient = Pattern
				.compile("(bill to|invoice).*?(description|sub\\s*total)",
						Pattern.DOTALL | Pattern.CASE_INSENSITIVE)
				.matcher(data);
		String patientDetails = null;
		if (patient.find()) {
			patientDetails = patient.group().trim();
		}
		if (patientDetails != null) {
			// System.err.println(patientDetails);
			String regex = "(?:.*?invoice\\s*(\\d{6,7}))?.*?(?:.*?date:?\\s*(\\d{2}-\\d{2}-\\d{4}))?.*?(?:.*?due\\s*date:?\\s*(\\d{2}-\\d{2}-\\d{4}))?.*?(?:.*?patient:?\\s*(\\S+))?.*?(?:.*?customer\\s*id:?\\s*(\\d{6}))?.*?(?:.*?animal:?\\s*(\\S+))?.*?(?:.*?clinical\\s*#:?\\s*(\\d{6,7}))?";
			Matcher details = Pattern
					.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE)
					.matcher(patientDetails);
			if (details.find()) {
				patientMap.put("invoiceNumber",
						details.group(1) != null
								? details.group(1).trim()
								: "");
				patientMap.put("date",
						details.group(2) != null
								? details.group(2).trim()
								: "");
				patientMap.put("dueDate",
						details.group(3) != null
								? details.group(3).trim()
								: "");

				if (details.group(4) != null)
					patientMap.put("patientName", details.group(4).trim());
				else if (details.group(6) != null)
					patientMap.put("patientName", details.group(6).trim());

				patientMap.put("customerId",
						details.group(5) != null
								? details.group(5).trim()
								: "");
				patientMap.put("clinicId",
						details.group(7) != null
								? details.group(7).trim()
								: "");

			}
		}
		System.out.println(new JSONObject(patientMap).toString(2));
	}

	private void hospitalDetailsExtract(String data) {
		JSONObject hospitalJson = new JSONObject();
		Matcher hospital = Pattern
				.compile(
						"(page \\d of \\d)?(?!.*page \\d of \\d).*?(?=bill to)",
						Pattern.DOTALL | Pattern.CASE_INSENSITIVE)
				.matcher(data);
		String hospitalDetails = null;
		if (hospital.find()) {
			hospitalDetails = hospital.group().trim();
		}
		if (hospitalDetails != null) {
			Matcher phoneFax = Pattern.compile(
					"([Pp]h:\\s*(\\(?\\d{3}\\)?[- ]\\d{3}-\\d{4})).*([Ff]ax:\\s*(\\(?\\d{3}\\)?[ -]\\d{3}-\\d{4}))",
					Pattern.DOTALL).matcher(hospitalDetails);
			if (phoneFax.find()) {
				hospitalJson.put("phone", phoneFax.group(2).trim());
				hospitalJson.put("fax", phoneFax.group(4).trim());
			}
			Matcher name = Pattern.compile(
					"^(?=.*(veterinary|animal[^s]|hospital| care |pet|clinic)).*$",
					Pattern.MULTILINE | Pattern.CASE_INSENSITIVE)
					.matcher(hospitalDetails);
			if (name.find()) {
				hospitalJson.put("hospitalName",
						name.group().replaceAll("[^A-Za-z- ]", "")
								.replaceAll("\\s{2,}", "").trim());
			}
			Matcher email = Pattern.compile("\\S+@\\S+\\.com")
					.matcher(hospitalDetails);
			if (email.find()) {
				hospitalJson.put("email", email.group().trim());
			}
			Matcher address = Pattern.compile("(\\d{2,})[\\s\\S]+?(\\d{2,})")
					.matcher(hospitalDetails);
			if (address.find()) {
				hospitalJson.put("address",
						address.group().replaceAll("\\n", ",")
								.replaceAll("[^A-Za-z\\d,. ]", "")
								.replaceAll("\\s{2,}", "").replaceAll(",,", "")
								.trim());
			}
		}
		// System.err.println(hospitalDetails);
		System.out.println(hospitalJson.toString(2));
	}

}
