import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class Han {
	public static void main(String args[]) throws IOException {
		File file = new File("/home/aberami/Documents/txt/bowie1.txt");
		FileReader fr = new FileReader(file);
		BufferedReader bf = new BufferedReader(fr);
		String test = null;
		StringBuilder str = new StringBuilder();
		JSONObject json = new JSONObject();
		JSONArray jarray = new JSONArray();
		String[] line = new String[10];
		while ((test = bf.readLine()) != null) {
			str.append(test);
			str.append("\n");
		}
		bf.close();
		String data = str.toString();
		str.setLength(0);
		System.out.println(data);
		Pattern desc = Pattern.compile(".+(\\$\\s+\\d+\\.\\d+)");
		Pattern species = Pattern.compile("(?<=Species:).+(?=Rabies)");
		Pattern sex = Pattern.compile("(?<=Sex:).+(?=FVRCP)");
		Pattern age = Pattern.compile("(?<=Age:).+(?=Leukemia)");
		Pattern breed = Pattern.compile("(?<=Breed:).+");
		// Matches Description
		Matcher mat = desc.matcher(data);
		String date = null;
		while (mat.find()) {
			test = mat.group().trim();
			line = test.split("\\s{2,}");
			if (line.length == 6) {
				// System.out.println(test);
				JSONObject samp = new JSONObject();
				date = line[0].trim();
				samp.put("date", date);
				samp.put("code", line[1].trim());
				samp.put("Description", line[2].trim());
				samp.put("quantity", line[3].trim());
				str.append(line[4].trim() + line[5].trim());
				samp.put("amount", str.toString());
				str.setLength(0);
				jarray.put(samp);
			}
			if (line.length == 5) {
				// System.out.println(test);
				JSONObject samp = new JSONObject();
				samp.put("date", date);
				samp.put("code", line[0].trim());
				samp.put("Description", line[1].trim());
				samp.put("quantity", line[2].trim());
				str.append(line[3].trim() + line[4].trim());
				samp.put("amount", str.toString());
				str.setLength(0);
				jarray.put(samp);
			}
			json.put("Descriptions", jarray);
		}
		// Matches the details of the Patient
		mat = species.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("species", test);
		}
		mat = sex.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("sex", test);
		}
		mat = age.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("Age", test);
		}
		mat = breed.matcher(data);
		if (mat.find()) {
			test = mat.group().trim();
			json.put("breed", test);
		}
		System.out.println(json);
	}
}