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

public class Cat20 {
	public static void main(String args[]) throws IOException {
		File txtFile = new File(
				"/home/aberami/Documents/Figo/Category20/working/00 AM (43).txt");
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
		new Cat20().hospitalDetailsExtract(fileData);
		new Cat20().patientDetailsExtract(fileData);

		// System.out.println(fileData);
	}

	private void patientDetailsExtract(String data) {
		Map<String, String> patientMap = new HashMap<String, String>();
		Matcher details = Pattern.compile(
				"(?:client:\\s*([A-Za-z ,]+)).*?(?:patient:\\s*([A-Za-z ,]+)).*?(?:species:\\s*(\\S+)).*?(?:breed:\\s*(\\S+)).*?(?:age:\\s*?(.*?(?=\\s{2,}))).*?(?:sex:\\s*?(.*?(?=\\s{2,}))).*?(?:color:\\s*(\\S+))",
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(data);
		if (details.find()) {
			System.err.println(details.group().trim());
			patientMap.put("patientName",
					details.group(2) != null ? details.group(2).trim() : "");
			patientMap.put("species",
					details.group(3) != null ? details.group(3).trim() : "");
			patientMap.put("breed",
					details.group(4) != null ? details.group(4).trim() : "");
			patientMap.put("age",
					details.group(5) != null ? details.group(5).trim() : "");
			patientMap.put("sex",
					details.group(6) != null ? details.group(6).trim() : "");
			patientMap.put("color",
					details.group(7) != null ? details.group(7).trim() : "");
		}
		System.out.println(new JSONObject(patientMap).toString(2));
	}

	private void hospitalDetailsExtract(String fileData) {

	}

}
