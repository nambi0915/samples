package otherdetails;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

public class Coordinate {
	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws JDOMException, IOException {
		File xmlFile = new File("/home/aberami/Documents/Figo/00 AM (44).xml");
		SAXBuilder saxBuilder = new SAXBuilder();
		Document document = saxBuilder.build(xmlFile);
		Element rootElement = document.getRootElement();
		Namespace abbyyNs = rootElement.getNamespace();
		/*
		 * Namespace abbyyNs = Namespace.getNamespace(
		 * "http://www.abbyy.com/FineReader_xml/FineReader10-schema-v1.xml");
		 */
		List<Element> pageElements = rootElement.getChildren();
		for (Element page : pageElements) {
			StringBuilder pageText = new StringBuilder();
			List<Element> lineElements = page.getChildren();
			for (Element line : lineElements) {

				List<Element> phraseElements = line.getChildren("line",
						abbyyNs);
				for (Element phrase : phraseElements) {
					List<Element> formatting = phrase.getChildren("formatting",
							abbyyNs);
					for (Element format : formatting) {
						Element word = format.getChild("word", abbyyNs);
						// System.err.println(word.getText());
					}
					Element phraseTextElement = phrase.getChild("lineText",
							abbyyNs);
					String phraseText = phraseTextElement.getText();
					pageText.append(phraseText + "         ");
					// System.err.println(lineT);
				}
				pageText.append("\n");
			}
			System.out.println("Page:\n" + pageText);
		}
	}
}
