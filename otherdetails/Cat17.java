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

public class Cat17 {
	public static Map<String, String> patientMap = new HashMap<String, String>();
	public static void main(String args[]) throws IOException {
		File txtFile = new File(
				"/home/aberami/Documents/Figo/Category17/working/Vet_12-21-17.txt");
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
		new Cat17().hospitalDetailsExtract(fileData);
		new Cat17().patientDetailsExtract(fileData);

		// System.out.println(fileData);
	}

	private void patientDetailsExtract(String data) {
		Matcher patient = Pattern
				.compile(".*(?=Service\\/Item)", Pattern.DOTALL).matcher(data);
		String patientDetails = null;
		if (patient.find()) {
			patientDetails = patient.group().trim();
		}
		if (patientDetails != null) {
			// System.err.println(patientDetails);
			Matcher nameAndAge = Pattern.compile("(\\S+)\\s*(?:Age:\\s*(\\S+))")
					.matcher(patientDetails);
			if (nameAndAge.find()) {
				patientMap.put("name", nameAndAge.group(1).trim());
				patientMap.put("age", nameAndAge.group(2).trim());
			}
			Matcher sexAndSpecies = Pattern
					.compile("(\\S+)\\s*(?:Sex:\\s*(\\S+))")
					.matcher(patientDetails);
			if (sexAndSpecies.find()) {
				patientMap.put("species", sexAndSpecies.group(1).trim());
				patientMap.put("sex", sexAndSpecies.group(2).trim());
			}
			Matcher tagWeight = Pattern.compile(
					"(?:Tag:\\s*(\\S+)).*?(?:Weight:\\s*(\\d+\\.\\d+)).*?(?:Doctor:\\s*(.*?D\\.?V\\.?M))",
					Pattern.DOTALL).matcher(patientDetails);
			if (tagWeight.find()) {
				patientMap.put("tag", tagWeight.group(1).trim());
				patientMap.put("weight", tagWeight.group(2).trim());
				patientMap.put("doctorName", tagWeight.group(3).trim());
			}

		}
		System.out.println(new JSONObject(patientMap).toString(2));
	}

	private void hospitalDetailsExtract(String data) {
		JSONObject hospitalJson = new JSONObject();
		Matcher hospital = Pattern.compile(".*(?=Age:|Sex:)", Pattern.DOTALL)
				.matcher(data);
		String hospitalDetails = null;
		if (hospital.find()) {
			hospitalDetails = hospital.group().trim();
		}
		if (hospitalDetails != null) {
			// System.err.println(hospitalDetails);
			Matcher details = Pattern.compile(
					"(?:.*?(account:\\s*(\\d+)))?.*?(invoice:\\s*(\\d+)).*?(date:\\s*(\\d+\\/\\d+\\/\\d+)).*?(?:.*?(time:\\s*(\\d+:\\d+ [AP]M)))?.*?(page:\\s*(\\d+))",
					Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(data);
			if (details.find()) {
				patientMap.put("accountNumber",
						details.group(2) != null
								? details.group(2).trim()
								: "");
				patientMap.put("invoiceNumber",
						details.group(4) != null
								? details.group(4).trim()
								: "");
				patientMap.put("invoiceDate",
						details.group(6) != null
								? details.group(6).trim()
								: "");
				patientMap.put("invoiceTime",
						details.group(8) != null
								? details.group(8).trim()
								: "");
				for (int i = 1; i < details.groupCount(); i++) {
					if (details.group(i) != null)
						hospitalDetails = hospitalDetails
								.replace(details.group(i), "");
				}
			}
			Matcher phone = Pattern
					.compile("(\\(?\\d{2,3}[\\)-] ?\\d{3} ?\\-? ?\\d{4})")
					.matcher(hospitalDetails);
			if (phone.find()) {
				hospitalJson.put("phone", phone.group().trim());
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
			// System.err.println(hospitalDetails);
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
					.compile("\\d{2,}.*?([A-Z][A-Za-z] \\d+-\\d+)",
							Pattern.DOTALL)
					.matcher(hospitalDetails);
			if (address.find()) {
				hospitalJson.put("address",
						address.group().replaceAll("\\n", ",")
								.replaceAll("[^A-Za-z-\\d,. ]", "")
								.replaceAll("\\s{2,}", "").replaceAll(",,", "")
								.trim());
			}
			System.out.println(hospitalJson.toString(2));
		}
	}

}
