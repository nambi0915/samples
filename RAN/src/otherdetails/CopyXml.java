package otherdetails;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class CopyXml {
	public static void main(String[] args) throws IOException {
		File folder = new File("C:\\Users\\Nambi\\Downloads\\Figo\\Category3");
		File[] listOfXmlFiles = folder.listFiles();
		int count = 0;
		File destination = new File("C:\\Users\\Nambi\\Downloads\\Figo\\Txt\\");
		FileUtils.cleanDirectory(destination);
		for (int i = 0; i < listOfXmlFiles.length; i++) {
			File xmlFile = listOfXmlFiles[i];
			String fileName = xmlFile.getName();
			try {
				if (fileName.substring(fileName.lastIndexOf(".")).equals(".xml")
						&& !(fileName.contains("corrected") | fileName.contains("Optimized"))) {
					System.out.println(fileName);
					FileUtils.copyFileToDirectory(xmlFile, destination);
					count++;
				}
			} catch (Exception e) {
				System.err.println("Exception !!!!!!!!!");
			}
		}
		System.err.println(count);

	}

}
