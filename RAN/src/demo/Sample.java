package demo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

public class Sample {
	public static void main(String args[]) throws IOException {
		File txt = new File("C:\\Users\\Nambi\\Downloads\\Figo\\Category9\\Artie_Adkins_3_16_2018_pdf.txt");
		String contents = FileUtils.readFileToString(txt, StandardCharsets.UTF_8.toString());
		System.err.println(contents);
	}
}
