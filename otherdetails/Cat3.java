package otherdetails;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class Cat3 {
	public static void main(String args[]) throws IOException {
		File txtFile = new File(
				"/home/aberami/Documents/Figo/Category3/working/Harley Edelman Invoice 02202018 II.txt");
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
		new Cat3().hospitalDetailsExtract(fileData);
		new Cat3().patientDetailsExtract(fileData);
		// System.out.println(fileData);
	}

	private void patientDetailsExtract(String data) {
		JSONObject patientJson = new JSONObject();
		Matcher invoice = Pattern.compile(
				"(  invoice\\s*number  |total for)(?=.*(species|age:|breed)).*?(description|qty)",
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(data);
		List<String> invoiceDetails = new ArrayList<String>();
		Matcher date = Pattern.compile("\\w{3}\\s*?\\d{2}[g,]\\s*\\d{4}")
				.matcher(data);
		String invoiceDate = null;
		String invoiceNo = null;
		if (date.find()) {
			invoiceDate = date.group().replaceAll("(?<=\\d{2}).(?=\\d{4})", ",")
					.trim();
		}
		while (invoice.find()) {
			invoiceDetails.add(invoice.group().trim());
		}
		if (invoiceDetails.size() == 0) {
			invoice = Pattern.compile(
					"(  invoice\\s*number  |total for).*?(description|qty)",
					Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(data);
			while (invoice.find()) {
				invoiceDetails.add(invoice.group().trim());
			}
		}
		for (int i = 0; i < invoiceDetails.size(); i++) {
			Map<String, String> patientMap = new HashMap<String, String>();
			// System.err.println(invoiceDetails.get(i));
			String matText = invoiceDetails.get(i);
			Matcher invoiceNumber = Pattern.compile("[^A-Za-z][= ]\\d{5,6}")
					.matcher(matText);
			if (invoiceNumber.find()) {
				invoiceNo = invoiceNumber.group().replaceAll("[^\\d]", "")
						.trim();
				patientMap.put("invoiceNumber", invoiceNo);
			} else
				patientMap.put("invoiceNumber", invoiceNo);

			patientMap.put("invoiceDate", invoiceDate);
			Matcher name = Pattern.compile(
					"^([^\\d\\n]+?)(?=\\(?(#|ft)\\s*[^:][^\\d][A-Z]?\\)?)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE)
					.matcher(matText);
			if (name.find())
				patientMap.put("patientName", name.group(1).trim());
			else {
				name = Pattern
						.compile("[^A-Za-z][= ]\\d{5,6}.*?((?!\\S*\\\\)\\S+)",
								Pattern.DOTALL)
						.matcher(matText);
				if (name.find())
					patientMap.put("name", name.group(1).trim());
			}
			Matcher species = Pattern
					.compile("(species[;:]?\\s*?)(.*?)(?=\\s{2,})",
							Pattern.CASE_INSENSITIVE)
					.matcher(matText);
			if (species.find()) {
				patientMap.put("species", species.group(2).trim());
			}
			Matcher sex = Pattern.compile("(sex[:;]?\\s*?)(.*?)(?=\\s{2,})",
					Pattern.CASE_INSENSITIVE).matcher(matText);
			if (sex.find()) {
				patientMap.put("sex", sex.group(2).trim());
			}
			Matcher age = Pattern.compile("(age:?\\s*?)(.*?)(?=\\s{2,})",
					Pattern.CASE_INSENSITIVE).matcher(matText);
			if (age.find()) {
				patientMap.put("age", age.group(2).trim());
			}
			Matcher breed = Pattern.compile("(breed:?\\s*?)(.*?)(?=\\s{2,})",
					Pattern.CASE_INSENSITIVE).matcher(matText);
			if (breed.find()) {
				patientMap.put("breed", breed.group(2).trim());
			}
			Matcher coatColor = Pattern
					.compile("(coat\\s*colo[nr][.:]?\\s*?)(.*?)(?=\\s{2,})",
							Pattern.CASE_INSENSITIVE)
					.matcher(matText);
			if (coatColor.find()) {
				patientMap.put("coatColor", coatColor.group(2).trim());
			}
			Matcher rabiesTagNumber = Pattern.compile(
					"(rabies\\s*tag\\s*(?:number)?:?\\s*?)(.*?)(?=\\s{2,})",
					Pattern.CASE_INSENSITIVE).matcher(matText);
			if (rabiesTagNumber.find()) {
				patientMap.put("RabiesTagNumber",
						rabiesTagNumber.group(2).trim());
			}
			Matcher rabiesBrandName = Pattern.compile(
					"(rabies\\s*brand\\s*(?:name?)?:?\\s*?)(.*?)(?=\\s{2,})",
					Pattern.CASE_INSENSITIVE).matcher(matText);
			if (rabiesBrandName.find()) {
				patientMap.put("rabiesBrandName",
						rabiesBrandName.group(2).trim());
			}
			Matcher rabiesSerialNumber = Pattern.compile(
					"(rabies\\s*serial\\s*number?:?\\s*?)(.*?)(?=\\s{2,})",
					Pattern.CASE_INSENSITIVE).matcher(matText);
			if (rabiesSerialNumber.find()) {
				patientMap.put("rabiesSerialNumber",
						rabiesSerialNumber.group(2).trim());
			}
			Matcher weight = Pattern.compile(
					"(weight(?: lbs)?:?\\s*?)((?!.*check).*?)(?=\\s{2,})",
					Pattern.CASE_INSENSITIVE).matcher(matText);
			if (weight.find()) {
				patientMap.put("weight", weight.group(2).trim());
			}
			Matcher microChip = Pattern
					.compile("(microchip#:\\s*?)(.*?)(?=\\s{2,})",
							Pattern.CASE_INSENSITIVE)
					.matcher(matText);
			if (microChip.find()) {
				patientMap.put("microChipNumber", microChip.group(2).trim());
			}
			Matcher tattoo = Pattern.compile("(tattoo#:\\s*?)(.*?)(?=\\s{2,})",
					Pattern.CASE_INSENSITIVE).matcher(matText);
			if (tattoo.find()) {
				patientMap.put("tattooNumber", tattoo.group(2).trim());
			}

			patientJson.put("patient" + Integer.toString(i + 1), patientMap);
		}
		System.out.println(patientJson.toString(8));
	}

	private void hospitalDetailsExtract(String data) {
		JSONObject hospitalJson = new JSONObject();
		String hospitalDetails = null;
		Matcher hospital;
		hospital = Pattern.compile(
				"(?<=CamScanner|Police)([\\s\\S]+?)(\\(?\\d{3}\\)? ?\\d{3}\\-?\\d{4})")
				.matcher(data);
		if (hospital.find())
			hospitalDetails = hospital.group().trim();
		else {
			hospital = Pattern.compile(
					"([\\s\\S]+?)(\\(?\\d{3}[-\\)\\/]? ?\\d{3} ?\\-? ?\\d{4})[\\s\\S]*?(?=[{\\(](#|[UV]|ft) ?P?\\d+)")
					.matcher(data);
			if (hospital.find())
				hospitalDetails = hospital.group().trim();
		}
		if (hospitalDetails != null) {
			// System.err.println(hospitalDetails);
			Matcher phone = Pattern
					.compile("(\\(?\\d{3}[\\/\\)-]? ?\\d{3} ?\\-? ?\\d{4})")
					.matcher(hospitalDetails);
			int phoneCount = 0;
			while (phone.find()) {
				hospitalJson.put(
						phoneCount == 0 ? "hospitalPhone" : "hospitalFax",
						phone.group().trim());
				hospitalDetails = hospitalDetails.replace(phone.group(), "");
				phoneCount++;
			}
			// System.err.println(hospitalDetails);
			Matcher web = Pattern
					.compile("(?!.*facebook)www\\.([^.]+?)(.?com|.org)")
					.matcher(hospitalDetails);
			if (web.find()) {
				hospitalJson.put("hospitalLink",
						web.group().replaceAll("\\s", "")
								.replaceAll("(?<!\\.)(?=com)", "."));
				hospitalDetails = hospitalDetails.replaceAll(web.group(), "");
			}
			Matcher email = Pattern
					.compile("(?![A-Za-z ]*\\s{2,})[A-Za-z ]+@.*\\.com")
					.matcher(hospitalDetails);
			if (email.find()) {
				hospitalJson.put("emailId",
						email.group().replaceAll(" ", "").trim());
				hospitalDetails = hospitalDetails.replaceAll(email.group(), "");
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
			hospitalDetails = hospitalDetails.replaceAll("\\.(?=\\s{2,})", "")
					.replaceAll("\\s{2,}\\S+(?=\\s{2,}|$)|^\\s*$", "")
					.replaceAll("[;']", "").trim();

			Matcher address = Pattern.compile(
					"([\\s\\S]*?)(\\d{2,} [\\s\\S]+?([A-Z]{1}[A-Za-z]{1} \\d+(-\\d+)?))([\\s\\S]*)")
					.matcher(hospitalDetails);
			if (address.find()) {
				String hospitalAddress = address.group(2).trim();
				hospitalAddress = hospitalAddress.replaceAll("\\n", ",")
						.replaceAll("\\s{2,}", "").replaceAll(",,", ",");
				hospitalDetails = address.group(1).trim()
						+ address.group(5).trim();
				hospitalJson.put("hospitalAddress", hospitalAddress);
			}
			// System.err.println(hospitalDetails);
		}
		// System.out.println(hospitalJson.toString(2));
	}

}
