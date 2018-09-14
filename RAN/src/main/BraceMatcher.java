package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BraceMatcher {

	public static void main(String[] args) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new FileReader("C:\\Users\\lenovo\\Documents\\Nambi\\RAN\\src\\main\\Sound.java"));
		String fileLine = null;
		StringBuilder str = new StringBuilder();
		while ((fileLine = bufferedReader.readLine()) != null)
			str.append(fileLine + "\n");
		bufferedReader.close();
		String fileData = str.toString();
		// System.err.println(fileData);
		System.err.println(checkBraces(fileData) ? "Balanced" : "Not balanced");
	}

	private static boolean checkBraces(String fileData) {
		int bracket = 0;
		int curlyBrace = 0;
		int squareBracket = 0;
		Matcher braceMatcher = Pattern.compile("\\(|\\)|\\{|\\}|\\[|\\]").matcher(fileData);
		while (braceMatcher.find()) {
			String brace = braceMatcher.group().trim();
			switch (brace) {
			case "(":
				bracket++;
				break;
			case ")":
				bracket--;
				break;
			case "{":
				curlyBrace++;
				break;
			case "}":
				curlyBrace--;
				break;
			case "[":
				squareBracket++;
				break;
			case "]":
				squareBracket--;
				break;
			default:
				break;
			}
			if (bracket < 0 || curlyBrace < 0 || squareBracket < 0)
				return false;
		}
		return bracket == 0 && curlyBrace == 0 && squareBracket == 0 ? true : false;
	}

}
