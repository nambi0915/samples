package demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rough {

	public static void main(String[] args) throws IOException {
		File file = new File("C:\\Users\\Nambi\\Downloads\\Spark\\WA_Sales_Products_2012-14.txt");
		FileReader fr = new FileReader(file);
		BufferedReader bf = new BufferedReader(fr);
		String test = null;
		StringBuilder str = new StringBuilder();
		while ((test = bf.readLine()) != null) {
			str.append(test);
			str.append("\n");
		}
		String data = str.toString();
		bf.close();
		int count = 0;
		// Matcher mat = Pattern.compile("^United States,.*?,Watches,.*?,2013,.*?$",
		// Pattern.MULTILINE).matcher(data);

		Matcher mat = Pattern.compile("^.*?,2012,.*?$", Pattern.MULTILINE).matcher(data);
		while (mat.find()) {
			count++;
		}
		System.err.println(count);
		count = 0;
		Matcher mat1 = Pattern.compile("^.*?,Fax,.*?,2012,.*?$", Pattern.MULTILINE).matcher(data);
		while (mat1.find()) {
			count++;
		}
		System.err.println(count);
	}
}
