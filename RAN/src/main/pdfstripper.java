package main;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class pdfstripper {
	public static void main(String args[]) throws IOException {
		File file = new File("C:\\Users\\lenovo\\Documents\\Nambi\\Pdf files\\invoice.pdf");
		// FileReader fr = new FileReader(file);
		PDDocument pd = PDDocument.load(file);
		PDFTextStripper pdfStripper = new PDFTextStripper();

		String text = pdfStripper.getText(pd);
		System.out.println(text);

		pd.close();
	}
}
