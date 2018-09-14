package main;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class Pdf {
	public static void main(String args[]) throws IOException {
		File file = new File("C:\\Users\\lenovo\\Documents\\Nambi\\Pdf files\\invoice.pdf");
		// FileReader fr = new FileReader(file);
		PDDocument pd = PDDocument.load(file);
		PDFTextStripperByArea pdfStripper = new PDFTextStripperByArea();
		// pd.addPage(new PDPage());
		// System.out.println(pd.getNumberOfPages());
		Rectangle shape = new Rectangle(40, 300, 612, 100);
		pdfStripper.addRegion("tab", shape);
		pdfStripper.extractRegions(pd.getPage(0));
		String text = pdfStripper.getTextForRegion("tab");
		// pd.removePage(7);
		// pd.save(file);
		System.out.println(text);
		pd.close();
	}
}