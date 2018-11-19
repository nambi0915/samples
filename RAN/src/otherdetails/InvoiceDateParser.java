package otherdetails;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class InvoiceDateParser {
	public static String invoiceDate = "";
	public static JSONObject otherJson = new JSONObject();

	public String invoiceDateExtract(String data) {
		Matcher invoiceDateMatcher = Pattern.compile(
				"(?:(?:\\|\\s|\\s{2,})date|invoice\\s*date):?.*?( \\d{1,2}[-\\/]\\d{1,2}[-\\/]\\d{2,4})| ([A-Za-z]{3}\\s*\\d{2}[g,]\\s*\\d{4})|([A-Za-z]+\\s*,\\s*[A-Za-z]+\\s*\\d{1,2}[.,]\\s*\\d{4})",
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(data);
		if (invoiceDateMatcher.find()) {

			if (invoiceDateMatcher.group(1) != null)
				invoiceDate = invoiceDateMatcher.group(1).trim();
			else if (invoiceDateMatcher.group(2) != null)
				invoiceDate = invoiceDateMatcher.group(2).trim();
			else if (invoiceDateMatcher.group(3) != null)
				invoiceDate = invoiceDateMatcher.group(3).trim();

		} else {
			invoiceDateMatcher = Pattern.compile("\\d{1,2}\\/\\d{1,2}\\/\\d{4}")
					.matcher(data);
			if (invoiceDateMatcher.find()) {
				invoiceDate = invoiceDateMatcher.group().trim();
			}
		}
		return invoiceDate;
	}
	@SuppressWarnings("unused")
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
		// System.err.println(fileData);
		Matcher address = Pattern.compile(
				"(^(?i)(?=.*?(?:\\bdoctor\\b|\\bveterinary\\b|\\banimal\\b|\\bhospital\\b|\\bcare\\b|\\bpets?\\b|\\bclinic\\b|\\bvet\\b)) *(?:[^ \\n@:]+? +?){1,6}$)(?:(?i)(?!\\bveterinary\\b|\\banimal\\b|\\bhospital\\b|\\bvet\\b|\\bpets?\\b|clinic)[\\s\\S])*?(\\d+[-\\w\\/., \\n$&]*? [A-Z]{1,2}[A-Za-z] ?(?:\\d+-)?\\d+)",
				Pattern.MULTILINE).matcher(fileData);

		if (address.find()) {
			otherJson.put("hospitalName",
					address.group(1).replaceAll("[^A-Za-z-& ]", "")
							.replaceAll("\\s{2,}", "").trim());
			otherJson.put("hospitalAddress",
					address.group(2).replaceAll("\\n|\\|", ",")
							.replaceAll("[^A-Za-z-\\d,. ]", "")
							.replaceAll("\\s{2,}", "").replaceAll(",{2,}", ",")
							.trim());
		}
	}

	@SuppressWarnings("unused")
	private void hospitalPhoneExtract(String data) {
		Matcher phone = Pattern.compile(
				"(?!.*(?:figo|home|call|fax))(?!.*[pP]['\\-.]\\d)^.*?[^\\d-](\\(?\\d{3}[\\/\\);-]? ?\\d{3} ?\\-? ?\\d{4})[^\\d].*?$",
				Pattern.MULTILINE | Pattern.CASE_INSENSITIVE).matcher(data);
		if (phone.find()) {
			otherJson.put("hospitalPhone",
					phone.group(1).trim().replaceAll(";", "\\)"));
		}
	}

}
