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

public class Cat2 {
	public static void main(String args[]) throws IOException {
		File txtFile = new File(
				"/home/aberami/Documents/Figo/Category2/working/00 AM (44).txt");
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
		new Cat2().hospitalDetailsExtract(fileData);
		// new Cat2().patientDetailsExtract(fileData);
		// System.out.println(fileData);
	}
	void patientDetailsExtract(String data) {

		Map<String, String> patientMap = new HashMap<String, String>();

		Matcher invoiceNumber = Pattern.compile("[iI]nvoice ?#[i:]?\\s*(.+)")
				.matcher(data);
		if (invoiceNumber.find()) {
			patientMap.put("invoiceNumber",
					invoiceNumber.group(1).replaceAll("[^\\d]", "").trim());
		}
		Matcher invoiceDate = Pattern
				.compile("[Dd]ate:\\s*(\\d+\\/\\d+\\/\\d+)").matcher(data);
		if (invoiceDate.find()) {
			patientMap.put("invoiceDate", invoiceDate.group(1).trim());
		}
		Matcher clientId = Pattern.compile("[Cc]lient\\s*[iI]D:?\\s*(\\d+)")
				.matcher(data);
		if (clientId.find()) {
			patientMap.put("clientId",
					clientId.group(1).replaceAll("[^\\d]", "").trim());
		}
		Matcher patientName = Pattern
				.compile("([Pp]atien[lt]\\s*Name:?\\s*)(.+?)(?=\\s{2,})")
				.matcher(data);
		if (patientName.find()) {
			patientMap.put("patientName", patientName.group(2).trim());
		}
		Matcher patientId = Pattern
				.compile("([Pp]atien[lt]\\s*ID[i:]?\\s*)(\\S+)").matcher(data);
		if (patientId.find()) {
			patientMap.put("patientId", patientId.group(2).trim());
		}
		Matcher species = Pattern.compile("([sS]peci?es:?\\s*)(.+?)(?=\\s{2,})")
				.matcher(data);
		if (species.find()) {
			patientMap.put("species", species.group(2).trim());
		}
		Matcher breed = Pattern.compile("([Bb]reed:?\\s*)(.+?)(?=\\s{2,})")
				.matcher(data);
		if (breed.find()) {
			patientMap.put("breed", breed.group(2).trim());
		}
		Matcher sex = Pattern.compile("([Ss]ex:?\\s*)(.+?)(?=\\s{2,})")
				.matcher(data);
		if (sex.find()) {
			patientMap.put("sex", sex.group(2).trim());
		}
		Matcher weight = Pattern
				.compile("([Ww]eight:?\\s*)(.*?)(?=\\s{2,}|Patient)")
				.matcher(data);
		if (weight.find()) {
			patientMap.put("weight",
					weight.group(2).replaceAll(",", ".").trim());
		}
		Matcher dob = Pattern
				.compile("Birthday[:.]?\\s*(\\d+[\\/,]\\d+[\\/,]\\d+)")
				.matcher(data);
		if (dob.find()) {
			patientMap.put("dateOfBirth",
					dob.group(1).replaceAll(",", "/").trim());
		}
		System.out.println(new JSONObject(patientMap).toString(2));

	}
	void hospitalDetailsExtract(String data) {
		String text = null;
		JSONObject hospitalJson = new JSONObject();
		String hospitalDetails = null;
		/*
		 * int pageCount = 1; Matcher multiPage =
		 * Pattern.compile("[Pp][-'\\.]\\d").matcher(data); while
		 * (multiPage.find()) { pageCount++; }
		 */

		Matcher hospital = null;
		hospital = Pattern.compile(
				"(?<=1 of 1|[pP]age \\d  |benefits\\.|[Pp][-'\\.]\\d)(?![\\s\\S]*[Pp][-'\\.]\\d)(?![\\s\\S]*[pP]age \\d  )[\\s\\S]*?(?=^.+Client ?ID:?)",
				Pattern.MULTILINE).matcher(data);
		if (hospital.find()) {
			text = hospital.group().trim();
			System.err.println(text);
		} else {

			hospital = Pattern.compile(
					"[\\s\\S]*?(?=  Payment On Account)|[\\s\\S]*?(?=^.+Client ?ID:?)",
					Pattern.MULTILINE).matcher(data);
			if (hospital.find()) {
				text = hospital.group().trim();
				System.err.println(text);
			}
		}
		if (text != null) {
			hospitalDetails = text.replaceAll("\\d+\\/\\d+\\/\\d+", "");
			Matcher phone = Pattern
					.compile("\\(?\\d{3}(\\)|;|-) ?\\d{3}[- *]\\d{4}")
					.matcher(hospitalDetails);

			while (phone.find()) {
				hospitalJson.put("hospitalPhone", phone.group().trim());
				hospitalDetails = hospitalDetails.replace(phone.group(), "");
			}
			/*
			 * hospitalDetails = hospitalDetails
			 * .replaceAll("\\(\\d+(\\)|;) ?\\d{3}[- *]\\d{4}", "").trim();
			 */
			Matcher email = Pattern.compile("(\\S+@\\S+)")
					.matcher(hospitalDetails);
			if (email.find()) {
				hospitalJson.put("hospitalEmail", email.group().trim());
				hospitalDetails = hospitalDetails.replaceAll(email.group(), "");
			}
			Matcher web = Pattern.compile("(www.)? ?\\S+\\.? ?com")
					.matcher(hospitalDetails);
			if (web.find()) {
				hospitalJson.put("hospitalLink",
						web.group().trim().replaceAll("\\s", ""));
				hospitalDetails = hospitalDetails.replaceAll(web.group(), "");
			}
			hospitalDetails = hospitalDetails.replaceAll("[Pp]age.+?(?= {2,})",
					"");
			Matcher name = Pattern.compile(
					"^(?=.*(veterinary|animal|hospital|cat|dog|pet|clinic|vet)).*$",
					Pattern.MULTILINE | Pattern.CASE_INSENSITIVE)
					.matcher(hospitalDetails);
			if (name.find()) {
				hospitalJson.put("hospitalName",
						name.group().replaceAll("[^A-Za-z &]", "")
								.replaceAll("\\s{2,}", " ").trim());
			}

			hospitalDetails = hospitalDetails.replaceAll("\\.(?=\\s{2,})", "")
					.replaceAll("\\s{2,}\\S+(?=\\s{2,}|$)|^\\s*$", "")
					.replaceAll("[;']", "").trim();
			// System.err.println(hospitalDetails);

			// System.err.println(hospitalDetails);

			Matcher address = Pattern.compile(
					"(1|\\d{2,})[\\s\\S]+?([A-Z]{1}[A-Za-z]{1} ?(\\d+-)?\\d+)")
					.matcher(hospitalDetails);
			if (address.find()) {
				// System.err.println(address.group());
				hospitalJson.put("hospitalAddress",
						address.group().replaceAll("\\n", ",")
								.replaceAll("\\s{2,}", "").replaceAll(",,", ",")
								.trim());
				hospitalDetails = hospitalDetails.replaceAll(address.group(),
						"");
			}
			// System.err.println(hospitalDetails);
			/*
			 * hospitalDetails = hospitalDetails
			 * .replaceAll("\\d+[\\s\\S]+?([A-Z]{2} ?(\\d+-)?\\d+)", "")
			 * .trim();
			 */

			/*
			 * String hospitalName = hospitalDetails.replaceAll("\\n", "<>")
			 * .replaceAll("\\s{2,}", "").replaceAll("<>", " ").trim();
			 * hospitalName = hospitalName.replaceAll("([^A-Za-z &]+)", "")
			 * .replaceAll("\\s{2,}", " ");
			 */

		}

		// System.err.println(phoneNumber);
		// System.err.println(hospitalDetails);
		System.out.println(hospitalJson.toString(2));
	}

}
