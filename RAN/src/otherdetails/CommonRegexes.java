package otherdetails;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class CommonRegexes {
	public static JSONObject hospitalJson = new JSONObject();
	public static void main(String args[]) throws IOException {
		File folder = new File("/home/aberami/Documents/Figo/Cat/");
		File[] listOfFiles = folder.listFiles();
		Scanner s = new Scanner(System.in);
		int choice = 1;
		int i = 0;
		long totalTime;
		System.err.println(listOfFiles.length);
		InvoiceDateParser dateParser = new InvoiceDateParser();
		InvoiceNumberParser noParser = new InvoiceNumberParser();
		int date = 0, number = 0;
		while (choice == 1 && i < listOfFiles.length) {
			long startTime = System.nanoTime();
			FileReader fileReader = new FileReader(listOfFiles[i]);
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
			System.err.println(listOfFiles[i].getName());
			noParser.invoiceNumberExtract(fileData);
			// regex.hospitalPhoneExtract(fileData);
			dateParser.invoiceDateExtract(fileData);
			// regex.hospitalAddressExtract(fileData);
			System.out.println(hospitalJson.toString(2));

			/*
			 * if (!hospitalJson.has("invoiceDate")) date++; if
			 * (!hospitalJson.has("invoiceNumber")) number++;
			 */

			hospitalJson = new JSONObject();
			// System.err.println("Enter Choice 0 or 1");
			long endTime = System.nanoTime();
			totalTime = endTime - startTime;
			System.out.println("Execution Time: "
					+ (double) totalTime / 1000000000.0 + " seconds");
			choice = s.nextInt();
			i++;
		}

		System.err.println("date  " + date + " number  " + number);
		s.close();
		// new CommonRegexes().patientDetailsExtract(fileData);
		// System.out.println(fileData);
	}

	private void hospitalAddressExtract(String fileData) {
		Matcher top = Pattern.compile(
				"(.*?)(description|qty|price)((?!.*?(description|qty|price)).*)",
				Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(fileData);
		if (top.find()) {
			fileData = top.group(1).trim();
		}
		fileData = fileData.replaceAll(
				"[pP]ag[oe]\\ *\\d\\ *\\/\\ *\\d|\\s{3}[-\\w]+(?=\\s{2,})|#",
				"");
		System.err.println(fileData);
		Matcher address = Pattern.compile(
				"(^(?i)(?=.*?(?:\\bdoctor\\b|\\bveterinary\\b|\\banimal\\b|\\bhospital\\b|\\bcare\\b|\\bpets?\\b|\\bclinic\\b|\\bvet\\b)) *(?:[^ \\n@:]+? +?){1,6}$)(?:(?i)(?!\\bveterinary\\b|\\banimal\\b|\\bhospital\\b|\\bvet\\b|\\bpets?\\b|clinic)[\\s\\S])*?(\\d+[-\\w\\/., \\n$&]*? [A-Z]{1,2}[A-Za-z] ?(?:\\d+-)?\\d+)",
				Pattern.MULTILINE).matcher(fileData);

		if (address.find()) {
			hospitalJson.put("hospitalName",
					address.group(1).replaceAll("[^A-Za-z-& ]", "")
							.replaceAll("\\s{2,}", "").trim());
			hospitalJson.put("hospitalAddress",
					address.group(2).replaceAll("\\n|\\|", ",")
							.replaceAll("[^A-Za-z-\\d,. ]", "")
							.replaceAll("\\s{2,}", "").replaceAll(",{2,}", ",")
							.trim());
		}
	}

	private void invoiceDateExtract(String data) {
		Matcher invoiceDate = Pattern.compile(
				"(?:(?:\\|\\s|\\s{2,})date|invoice\\s*date):?.*?( \\d{1,2}[-\\/]\\d{1,2}[-\\/]\\d{2,4})| ([A-Za-z]{3}\\s*\\d{2}[g,]\\s*\\d{4})|([A-Za-z]+\\s*,\\s*[A-Za-z]+\\s*\\d{1,2}[.,]\\s*\\d{4})",
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(data);
		if (invoiceDate.find()) {
			if (invoiceDate.group(1) != null)
				hospitalJson.put("invoiceDate", invoiceDate.group(1).trim());
			else if (invoiceDate.group(2) != null)
				hospitalJson.put("invoiceDate", invoiceDate.group(2).trim());
			else if (invoiceDate.group(3) != null)
				hospitalJson.put("invoiceDate", invoiceDate.group(3).trim());

		} else {
			invoiceDate = Pattern.compile("\\d{1,2}\\/\\d{1,2}\\/\\d{4}")
					.matcher(data);
			if (invoiceDate.find()) {
				hospitalJson.put("invoiceDate", invoiceDate.group().trim());
			}
		}
	}

	private void hospitalPhoneExtract(String data) {
		Matcher phone = Pattern.compile(
				"(?!.*(?:figo|home|call|fax))(?!.*[pP]['\\-.]\\d)^.*?[^\\d-](\\(?\\d{3}[\\/\\);-]? ?\\d{3} ?\\-? ?\\d{4})[^\\d].*?$",
				Pattern.MULTILINE | Pattern.CASE_INSENSITIVE).matcher(data);
		if (phone.find()) {
			hospitalJson.put("hospitalPhone",
					phone.group(1).trim().replaceAll(";", "\\)"));
		}
	}

	private void invoiceNumberExtract(String data) {
		Matcher invoice = Pattern
				.compile("invoice\\s*#?[:i;]?\\s*(\\d{4,9})(?:\\s{2,})?",
						Pattern.CASE_INSENSITIVE)
				.matcher(data);
		if (invoice.find()) {
			hospitalJson.put("invoiceNumber",
					invoice.group(1).replaceAll("[^\\d]", "").trim());
		} else {
			invoice = Pattern.compile(
					"(?:invoice\\s*number|inv\\.\\s*num|invoice\\s*(?:#|no:)).*?(?:\\s{2,}(\\d{4,7})(?=\\s{2,}))",
					Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(data);
			if (invoice.find()) {
				hospitalJson.put("invoiceNumber",
						invoice.group(1).replaceAll("[^\\d]", "").trim());
			}
		}
	}

}
