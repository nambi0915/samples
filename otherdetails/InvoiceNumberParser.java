package otherdetails;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvoiceNumberParser {
	public static String invoiceNumber = "";

	public String invoiceNumberExtract(String data) {
		Matcher invoice = Pattern.compile(
				"invoice\\s*#?[:i;]?(?:No\\.)?\\s*(\\d{4,9})(?:\\s{2,})?",
				Pattern.CASE_INSENSITIVE).matcher(data);
		if (invoice.find()) {
			invoiceNumber = invoice.group(1).replaceAll("[^\\d]", "").trim();
		} else {
			invoice = Pattern.compile(
					"(?:invoice\\s*number|inv\\.\\s*num|invoice\\s*(?:#|no:)).*?(?:\\s{2,}(\\d{4,7})(?=\\s{2,}))",
					Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(data);
			if (invoice.find()) {
				invoiceNumber = invoice.group(1).replaceAll("[^\\d]", "")
						.trim();
			}
		}
		// System.err.println(invoiceNumber);
		return invoiceNumber;
	}
}
