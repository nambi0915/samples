package otherdetails;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class OwnerDetailsParser {
	public static JSONObject ownerDetailsJson = new JSONObject();
	public static JSONObject ownerNameJson = new JSONObject();
	public static JSONObject ownerAddressJson = new JSONObject();
	public static JSONObject ownerPhoneJson = new JSONObject();

	public static void main(String args[]) throws Exception {
		File folder = new File("/home/aberami/Documents/Figo/samp/");
		File[] listOfXmlFiles = folder.listFiles();
		Scanner s = new Scanner(System.in);
		System.err.println("Total files: " + listOfXmlFiles.length);
		HospitalDetailsParser regex = new HospitalDetailsParser();
		Map<String, Double> timeDuration = new HashMap<>();
		Double time;
		OwnerDetailsParser ownerParser = new OwnerDetailsParser();

		for (int i = 0, choice = 1; choice == 1
				&& i < listOfXmlFiles.length; i++) {
			long startTime = System.nanoTime();
			File xmlFile = listOfXmlFiles[i];
			System.err.println(xmlFile.getName());
			List<String> blocks = regex.newXmlParserWithLineWordCoord(xmlFile);
			ownerParser.ownerdetailsExtract(blocks);
			System.out.println(ownerDetailsJson.toString(2));
			ownerDetailsJson = new JSONObject();
			ownerNameJson = new JSONObject();
			ownerAddressJson = new JSONObject();
			ownerPhoneJson = new JSONObject();
			// System.err.println("Enter Choice 0 or 1");

			long endTime = System.nanoTime();
			time = (double) (endTime - startTime) / 1000000000.0;
			System.out.println("Execution Time: " + time + " seconds");
			timeDuration.put(xmlFile.getName(), time);
			choice = s.nextInt();
		}
		s.close();
	}

	public void ownerdetailsExtract(List<String> blocks) {
		boolean matchFound = false;
		for (int i = 0; i < blocks.size() && matchFound == false; i++) {
			String block = blocks.get(i);

			Matcher ownerDetails = Pattern.compile(
					"(^(?!.*(?:(?i)\\bdoctor\\b|\\bveterinary\\b|\\banimal\\b|\\bhospital\\b|\\bcare\\b|\\bpet |\\bclinic\\b|vet|\\bpharmacy\\b))[\\\\u2019'\\-\\/A-Z\\.a-z\\d:\\ &{}()#\\\\]*$)\\n([#A-Z]?\\d+[\\/\\.\\-A-Za-z,#:() \\d]+?\\n([\\.\\-A-Za-z,\\/#:() \\d]+?\\n)?[\\/\\.\\-A-Za-z,#() \\d]+?(?:[A-Z]{1,2}|[.A-Za-z]+?),? ?(?:\\d{2,}-)?\\d{2,})",
					Pattern.MULTILINE).matcher(block);
			if (ownerDetails.find()) {

				String ownerName = nameCleansing(ownerDetails.group(1).trim());
				String ownerAddress = addressCleansing(
						ownerDetails.group(2).trim());
				// System.out.println(ownerAddress + "\n");
				if (isAddressValid(ownerAddress)
						&& isNameValid(ownerName, block)) {
					System.err.println(block);
					ownerAddressJson.put("text", ownerAddress);
					ownerNameJson.put("text", ownerName);
					matchFound = true;
				}

				Matcher phone = Pattern.compile(
						"(\\(?\\d{3}[\\/\\);.-]? ?\\d{3} ?[.\\-]? ?\\d{4})")
						.matcher(block);
				if (phone.find()) {
					String ownerPhone = phone.group(1).replaceAll("\\.", "-")
							.trim();;
					ownerPhoneJson.put("text", ownerPhone);
				}
			}
			ownerDetailsJson.put("ownerName", ownerNameJson);
			ownerDetailsJson.put("ownerAddress", ownerAddressJson);
			ownerDetailsJson.put("ownerPhone", ownerPhoneJson);
		}
	}

	private String nameCleansing(String ownerName) {

		ownerName = ownerName.replaceAll("FOR:|[Cc]lient", "")
				.replaceAll("[{(] *(#|ft|[A-Z]) *\\d*?[)}]", "")
				.replaceAll("[^A-Za-z \\/.&()\\u2019']", "").trim();

		return ownerName;
	}
	private String addressCleansing(String ownerAddress) {

		ownerAddress = ownerAddress.replaceAll("\\n|\\|", ",")
				.replaceAll("[^()#\\/A-Za-z-\\d,. ]", "")
				.replaceAll("\\( *\\)", "").replaceAll("[hH]ome|[pP]hone", "")
				.replaceAll("\\s{2,}", "").replaceAll(",{2,}", ",").trim();

		return ownerAddress;
	}
	private boolean isNameValid(String ownerName, String block) {
		System.err.println(ownerName);
		if (Pattern.compile("[A-Za-z]").matcher(ownerName).find()
				&& ownerName.matches("^(\\S+?(?: |$)){1,7}")
				&& !Pattern.compile(
						"((?i)\\bdoctor\\b|\\bveterinary\\b|\\banimal\\b|\\bhospital\\b|\\bcare\\b|\\bpet |\\bclinic\\b|vet|\\bpharmacy\\b)")
						.matcher(block).find()) {
			// System.err.println("name valid");
			return true;
		}
		return false;
	}
	private boolean isAddressValid(String ownerAddress) {
		if (Pattern.compile("[A-Za-z]").matcher(ownerAddress).find()) {
			// System.err.println("addr valid");
			return true;
		}
		return false;
	}
}
