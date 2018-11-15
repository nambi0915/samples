package otherdetails;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.json.JSONObject;

public class XmlParser {

	JSONObject coordinatesJson = new JSONObject();
	@SuppressWarnings("unchecked")
	List<String> newXmlParserWithLineCoord(File file) {
		List<String> blocks = new ArrayList<String>();
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(file);
			Element rootElement = document.getRootElement();
			Namespace abbyyNs = Namespace.getNamespace(
					"http://www.abbyy.com/FineReader_xml/FineReader10-schema-v1.xml");

			List<Element> pageElements = rootElement.getChildren("page",
					abbyyNs);
			// System.out.println(pageElements);
			for (Element page : pageElements) {

				List<Element> blockElements = page.getChildren();
				for (Element block : blockElements) {

					StringBuilder blockSb = new StringBuilder();
					if (block.getAttributeValue("blockType").equals("Text")) {
						JSONObject blockJson = new JSONObject();
						Element textElement = block.getChild("text", abbyyNs);
						System.out.println(textElement);

						List<Element> parElements = textElement
								.getChildren("par", abbyyNs);
						// System.out.println(parElements);
						int lineCountPerBlock = 0;
						for (Element parElement : parElements) {

							List<Element> lineElements = parElement
									.getChildren("line", abbyyNs);
							// System.out.println(lineElements);

							for (Element lineElement : lineElements) {
								Integer top = Integer.MAX_VALUE,
										left = Integer.MAX_VALUE,
										right = Integer.MIN_VALUE,
										bottom = Integer.MIN_VALUE;
								StringBuilder lineSb = new StringBuilder();
								List<Element> formatting = lineElement
										.getChildren("formatting", abbyyNs);
								for (Element format : formatting) {
									List<Element> charElements = format
											.getChildren();
									for (Element character : charElements) {
										top = character.getAttribute("t")
												.getIntValue() < top
														? character
																.getAttribute(
																		"t")
																.getIntValue()
														: top;
										left = character.getAttribute("l")
												.getIntValue() < left
														? character
																.getAttribute(
																		"l")
																.getIntValue()
														: left;
										right = character.getAttribute("r")
												.getIntValue() > right
														? character
																.getAttribute(
																		"r")
																.getIntValue()
														: right;
										bottom = character.getAttribute("b")
												.getIntValue() > bottom
														? character
																.getAttribute(
																		"b")
																.getIntValue()
														: bottom;
										String charStr = character.getText();
										lineSb.append(charStr);

									}
								}
								// System.err.println(lineSb);
								blockSb.append(lineSb + "\n");
								JSONObject lineJson = new JSONObject();
								lineJson.put("text", lineSb.toString());
								lineJson.put("top", top);
								lineJson.put("left", left);
								lineJson.put("right", right);
								lineJson.put("bottom", bottom);
								blockJson.put("NoOfLines",
										lineCountPerBlock + 1);
								blockJson.put(
										"line" + Integer
												.toString(lineCountPerBlock),
										lineJson);
								lineCountPerBlock++;
								// System.err.println("Line: " + lineSb);
							}
						}
						HospitalDetailsParser.lineCoordinatesJson.put(
								"block" + Integer.toString(blocks.size()),
								blockJson);
						String blockOfText = blockSb.toString();
						blocks.add(blockOfText);
						// System.err.println("Block:\n" + blockOfText);
					}
				}
			}
			System.out.println(
					HospitalDetailsParser.lineCoordinatesJson.toString(4));
			// System.err.println(blocks);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return blocks;
	}
	@SuppressWarnings("unchecked")
	List<String> oldXmlParserWithLineCoord(File file) {
		List<String> blocks = new ArrayList<String>();
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(file);
			Element rootElement = document.getRootElement();
			Namespace abbyyNs = Namespace.getNamespace(
					"http://www.abbyy.com/FineReader_xml/FineReader10-schema-v1.xml");
			List<Element> pageElements = rootElement.getChildren();
			for (Element page : pageElements) {
				List<Element> blockElements = page.getChildren();
				for (Element block : blockElements) {
					StringBuilder blockSb = new StringBuilder();
					if (block.getAttributeValue("blockType").equals("Text")) {
						JSONObject blockJson = new JSONObject();
						Element textElement = block.getChild("text", abbyyNs);

						List<Element> parElements = textElement.getChildren();
						int lineCountPerBlock = 0;
						for (Element parElement : parElements) {
							List<Element> lineElements = parElement
									.getChildren();
							// System.err.println(lineElements.size());
							for (Element lineElement : lineElements) {
								Integer top = Integer.MAX_VALUE,
										left = Integer.MAX_VALUE,
										right = Integer.MIN_VALUE,
										bottom = Integer.MIN_VALUE;
								StringBuilder lineSb = new StringBuilder();
								List<Element> charElements = lineElement
										.getChild("formatting", abbyyNs)
										.getChildren();
								for (Element character : charElements) {
									top = character.getAttribute("t")
											.getIntValue() < top
													? character
															.getAttribute("t")
															.getIntValue()
													: top;
									left = character.getAttribute("l")
											.getIntValue() < left
													? character
															.getAttribute("l")
															.getIntValue()
													: left;
									right = character.getAttribute("r")
											.getIntValue() > right
													? character
															.getAttribute("r")
															.getIntValue()
													: right;
									bottom = character.getAttribute("b")
											.getIntValue() > bottom
													? character
															.getAttribute("b")
															.getIntValue()
													: bottom;
									String charStr = character.getText();
									lineSb.append(charStr);
								}
								// System.err.println(lineSb);
								blockSb.append(lineSb + "\n");
								JSONObject lineJson = new JSONObject();
								lineJson.put("text", lineSb.toString());
								lineJson.put("top", top);
								lineJson.put("left", left);
								lineJson.put("right", right);
								lineJson.put("bottom", bottom);
								blockJson.put("NoOfLines",
										lineCountPerBlock + 1);
								blockJson.put(
										"line" + Integer
												.toString(lineCountPerBlock),
										lineJson);
								lineCountPerBlock++;
								// System.err.println("Line: " + lineSb);
							}
						}
						HospitalDetailsParser.lineCoordinatesJson.put(
								"block" + Integer.toString(blocks.size()),
								blockJson);
						String blockOfText = blockSb.toString();
						blocks.add(blockOfText);
						// System.err.println("Block:\n" + blockOfText);
					}
				}
			}
			System.out.println(
					HospitalDetailsParser.lineCoordinatesJson.toString(4));
			// System.err.println(blocks);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return blocks;
	}

}
