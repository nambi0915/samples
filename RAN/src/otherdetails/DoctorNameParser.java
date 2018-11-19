package otherdetails;

import java.io.BufferedReader;

import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class DoctorNameParser {

	public static HospitalDetailsParser regex = new HospitalDetailsParser();
	public static List<String> names = new ArrayList<String>();

	public static void main(String args[]) throws IOException {
		File folder = new File("/home/aberami/Documents/Nambi/Figo/Txt/");
		File[] listOfXmlFiles = folder.listFiles();
		Scanner s = new Scanner(System.in);
		System.err.println("Total files: " + listOfXmlFiles.length);

		Map<String, Double> timeDuration = new HashMap<>();
		Double time;

		for (int i = 0, choice = 1; choice == 1
				&& i < listOfXmlFiles.length; i++) {
			long startTime = System.nanoTime();
			File xmlFile = listOfXmlFiles[i];
			FileReader fileReader = new FileReader(listOfXmlFiles[i]);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			System.err.println(xmlFile.getName());
			names.clear();
			// List<String> blocks = regex.blockSeperation(xmlFile);
			String temp = null;
			StringBuilder fileContent = new StringBuilder();

			while ((temp = bufferedReader.readLine()) != null) {
				fileContent.append(temp);
				fileContent.append("\n");
			}
			bufferedReader.close();

			doctorNameExtract(fileContent.toString());
			long endTime = System.nanoTime();
			time = (double) (endTime - startTime) / 1000000000.0;
			System.out.println("Execution Time: " + time + " seconds");
			timeDuration.put(xmlFile.getName(), time);
			choice = s.nextInt();
		}
		s.close();
	}

	private static void doctorNameExtract(String fileContents) {
		List<Pattern> patterns = new ArrayList<Pattern>() {
			{
				add(Pattern.compile(
						"(?:(?i)(?:(?:services|seen) by|(?:veterinarian|doctor):\\ *)([ \\/A-Za-z,.\\u2019]*?)(?=\\s{2,}))",
						Pattern.UNICODE_CHARACTER_CLASS));
				add(Pattern.compile(
						"^ *(?=[A-Za-z,. ]*(?:LVT|D[\\. ]?V[\\. ]?M|Dr[\\. ]))([\\/A-Za-z,. ]+?)(?=\\||\\s{2,})",
						Pattern.MULTILINE));
			}
		};

		for (int i = 0; i < patterns.size() && names.size() == 0; i++) {
			Matcher doctorName = patterns.get(i).matcher(fileContents);
			String name;
			while (doctorName.find()) {
				name = doctorName.group(1).trim();
				if (name.length() != 0) {
					if (!names.contains(name)) {
						isValidName(name);
					}
				}
			}
		}
	}

	private static void isValidName(String name) {
		// Matcher val = Pattern.compile("((?:[\\w.,]+ ){1,5})").matcher(name);
		if (name.matches("((?:[\\/\\w.,\\u2019]+ ?){1,5})")
				&& Pattern.compile("LVT|D[\\. ]?V[\\. ]?M[\\.]?|Dr[ \\.]")
						.matcher(name).find()
				&& !name.matches("LVT|D[\\. ]?V[\\. ]?M[\\.]?|Dr[ \\.]")) {
			names.add(name);
			System.out.println("Doctor name: " + name + "\n");
		}
	}
}
