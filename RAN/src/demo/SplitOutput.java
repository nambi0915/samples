package demo;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class SplitOutput {
	public static void main(String args[]) throws IOException {
		File folder = new File("C:\\Users\\Nambi\\Documents\\Petsbest OP\\");
		File[] listOfXmlFiles = folder.listFiles();
		int count = 0;
		File destination = new File("C:\\Users\\Nambi\\Documents\\POP\\");
		FileUtils.cleanDirectory(destination);
		for (int i = 0; i < listOfXmlFiles.length; i++) {
			File xmlFile = listOfXmlFiles[i];
			String fileName = xmlFile.getName();
			try {
				if (Pattern.compile("_pdf|_jpeg|_jpg").matcher(fileName).find()) {
					System.out.println(fileName);
					FileUtils.copyFileToDirectory(xmlFile, destination);
					count++;
				}
			} catch (Exception e) {
				System.err.println("Exception !!!!!!!!!");
			}

		}
		// URL url = new URL("http://www.google.com");
		// FileUtils.copyURLToFile(url, new
		// File("C:\\Users\\Nambi\\Downloads\\nambi.txt"));
		System.err.println(count);
	}

}
