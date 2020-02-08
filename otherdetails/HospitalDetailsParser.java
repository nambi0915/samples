package otherdetails;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.json.JSONObject;

import com.google.common.collect.Iterators;

public class HospitalDetailsParser {
	public static String outputTextFilePath = "";
	public static String outputXmlFilePath = "";
	public static JSONObject hospitalJson = new JSONObject();
	public static JSONObject lineCoordinatesJson = new JSONObject();
	public static JSONObject wordCoordinatesJson = new JSONObject();
	public static List<String> blocksOfText;
	public static Integer totalWordsInDocument = 0;
	public static HospitalDetailsParser regex = new HospitalDetailsParser();

	public static void main(String args[]) throws Exception {
		File folder = new File("/home/aberami/Documents/Figo/samp/");
		File[] listOfXmlFiles = folder.listFiles();
		Scanner s = new Scanner(System.in);
		System.err.println("Total files: " + listOfXmlFiles.length);

		/*
		 * Vector<String> fileVector = new Vector(); AbbyyOcr abbyyocr = new AbbyyOcr();
		 * for (int i = 0; i < listOfXmlFiles.length; i++) { File file =
		 * listOfXmlFiles[i]; fileVector.add("recognize");
		 * fileVector.add(file.getAbsolutePath()); abbyyocr.doOcr(fileVector);
		 * fileVector.clear(); }
		 */
		Map<String, Double> timeDuration = new HashMap<>();
		Double time;
		StringBuilder hosNames = new StringBuilder();

		for (int i = 0, choice = 1; choice == 1 && i < listOfXmlFiles.length; i++) {
			long startTime = System.nanoTime();
			File xmlFile = listOfXmlFiles[i];
			System.err.println(xmlFile.getName());
			List<String> blocks = regex.oldXmlParserWithLineWordCoord(xmlFile);
			blocksOfText = new ArrayList<String>(blocks);
			regex.hospitalDetailsExtract(blocks);
			System.out.println(hospitalJson.toString(2));
			try {
				hosNames.append(hospitalJson.getJSONObject("hospitalName").getString("text") + "\n");
			} catch (Exception e) {

			}

			hospitalJson = new JSONObject();
			// System.err.println("Enter Choice 0 or 1");

			long endTime = System.nanoTime();
			time = (double) (endTime - startTime) / 1000000000.0;
			System.out.println("Execution Time: " + time + " seconds");
			timeDuration.put(xmlFile.getName(), time);
			// choice = s.nextInt();
		}
		System.err.println(hosNames);

		// System.err.println(new JSONObject(timeDuration).toString(2));
		/*
		 * Double sum = timeDuration.values().stream().reduce(0.0, Double::sum);
		 * System.out.println( "\n\nAverage execution time " + sum /
		 * listOfXmlFiles.length); String maxKey = Collections
		 * .max(timeDuration.entrySet(),
		 * Comparator.comparingDouble(Map.Entry::getValue)) .getKey(); String minKey =
		 * Collections .min(timeDuration.entrySet(),
		 * Comparator.comparingDouble(Map.Entry::getValue)) .getKey();
		 * System.out.println("Total Execution time: " + sum);
		 * System.out.println("Max: " + maxKey + "   " + timeDuration.get(maxKey));
		 * System.out.println("Min: " + minKey + "   " + timeDuration.get(minKey));
		 */ s.close();

	}

	@SuppressWarnings("unchecked")

	List<String> newXmlParserWithLineWordCoord(File file) {

		List<String> blocks = new ArrayList<String>();
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(file);
			Element rootElement = document.getRootElement();
			Namespace abbyyNs = Namespace
					.getNamespace("http://www.abbyy.com/FineReader_xml/FineReader10-schema-v1.xml");
			List<Element> pageElements = rootElement.getChildren();
			for (Element page : pageElements) {
				List<Element> blockElements = page.getChildren("block", abbyyNs);

				for (Element block : blockElements) {
					Integer wordCount = 0;
					StringBuilder blockSb = new StringBuilder();
					if (block.getAttributeValue("blockType").equals("Text")) {
						JSONObject blockJson = new JSONObject();
						Element textElement = block.getChild("text", abbyyNs);

						List<Element> parElements = textElement.getChildren("par", abbyyNs);
						int lineCountPerBlock = 0;
						for (Element parElement : parElements) {
							List<Element> lineElements = parElement.getChildren("line", abbyyNs);
							// System.err.println(lineElements.size());
							for (Element lineElement : lineElements) {

								StringBuilder wordSb = new StringBuilder();

								Integer lineTop = Integer.MAX_VALUE, lineLeft = Integer.MAX_VALUE,
										lineRight = Integer.MIN_VALUE, lineBottom = Integer.MIN_VALUE;

								Integer wordTop = Integer.MAX_VALUE, wordLeft = Integer.MAX_VALUE,
										wordRight = Integer.MIN_VALUE, wordBottom = Integer.MIN_VALUE;

								StringBuilder lineSb = new StringBuilder();
								List<Element> formatting = lineElement.getChildren("formatting", abbyyNs);
								for (Element format : formatting) {
									List<Element> charElements = format.getChildren("charParams", abbyyNs);
									for (Element character : charElements) {
										lineTop = character.getAttribute("t").getIntValue() < lineTop
												? character.getAttribute("t").getIntValue()
												: lineTop;
										lineLeft = character.getAttribute("l").getIntValue() < lineLeft
												? character.getAttribute("l").getIntValue()
												: lineLeft;
										lineRight = character.getAttribute("r").getIntValue() > lineRight
												? character.getAttribute("r").getIntValue()
												: lineRight;
										lineBottom = character.getAttribute("b").getIntValue() > lineBottom
												? character.getAttribute("b").getIntValue()
												: lineBottom;

										wordTop = character.getAttribute("t").getIntValue() < wordTop
												? character.getAttribute("t").getIntValue()
												: wordTop;
										wordLeft = character.getAttribute("l").getIntValue() < wordLeft
												? character.getAttribute("l").getIntValue()
												: wordLeft;
										wordRight = character.getAttribute("r").getIntValue() > wordRight
												? character.getAttribute("r").getIntValue()
												: wordRight;
										wordBottom = character.getAttribute("b").getIntValue() > wordBottom
												? character.getAttribute("b").getIntValue()
												: wordBottom;

										String charStr = character.getText();
										wordSb.append(charStr);
										if (charStr.matches(" ")) {
											addWordToJson(wordSb, wordTop, wordLeft, wordRight, wordBottom,
													blocks.size(), wordCount);
											// System.err.println(wordSb.toString());
											wordCount++;
											wordSb.setLength(0);
											wordTop = Integer.MAX_VALUE;
											wordLeft = Integer.MAX_VALUE;
											wordRight = Integer.MIN_VALUE;
											wordBottom = Integer.MIN_VALUE;
										}
										lineSb.append(charStr);
									}
								}

								addWordToJson(wordSb, wordTop, wordLeft, wordRight, wordBottom, blocks.size(),
										wordCount);
								// System.err.println(wordSb.toString());
								wordCount++;
								// System.err.println(wordCount);
								// System.err.println(lineSb);

								blockSb.append(lineSb + "\n");
								JSONObject lineJson = new JSONObject();
								lineJson.put("text", lineSb.toString());
								lineJson.put("top", lineTop);
								lineJson.put("left", lineLeft);
								lineJson.put("right", lineRight);
								lineJson.put("bottom", lineBottom);
								blockJson.put("NoOfLines", lineCountPerBlock + 1);
								blockJson.put("line" + Integer.toString(lineCountPerBlock), lineJson);
								lineCountPerBlock++;
								// System.err.println("Line: " + lineSb);
							}
						}
						lineCoordinatesJson.put("block" + Integer.toString(blocks.size()), blockJson);
						String blockOfText = blockSb.toString();
						blocks.add(blockOfText);
						// System.err.println("Block:\n" + blockOfText);
					}

					totalWordsInDocument += wordCount;
				}
			}
			// System.err.println(totalWords);
			// Iterator<String> keys = wordCoordinatesJson.keys();
			// System.err.println(Iterators.size(keys));
			// System.out.println(wordCoordinatesJson.toString(2));
			// System.out.println(lineCoordinatesJson.toString(4));
			// System.err.println(blocks);
		} catch (Exception e) {
			regex.oldXmlParserWithLineWordCoord(file);
		}
		return blocks;

	}

	private void addWordToJson(StringBuilder wordSb, Integer wordTop, Integer wordLeft, Integer wordRight,
			Integer wordBottom, Integer blockSize, Integer wordCount) {
		JSONObject wordJson = new JSONObject();
		wordJson.put("text", wordSb.toString().trim());
		wordJson.put("top", wordTop);
		wordJson.put("left", wordLeft);
		wordJson.put("right", wordRight);
		wordJson.put("bottom", wordBottom);
		// System.err.println("word" + Integer.toString(blockSize) + "b"
		// + Integer.toString(wordCount) + "w");
		wordCoordinatesJson.put("word" + Integer.toString(blockSize) + "b" + Integer.toString(wordCount) + "w",
				wordJson);

	}

	@SuppressWarnings("unchecked")
	List<String> oldXmlParserWithLineWordCoord(File file) {

		List<String> blocks = new ArrayList<String>();
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(file);
			Element rootElement = document.getRootElement();
			Namespace abbyyNs = Namespace
					.getNamespace("http://www.abbyy.com/FineReader_xml/FineReader10-schema-v1.xml");
			List<Element> pageElements = rootElement.getChildren();
			for (Element page : pageElements) {
				List<Element> blockElements = page.getChildren("block", abbyyNs);

				for (Element block : blockElements) {
					StringBuilder blockSb = new StringBuilder();
					if (block.getAttributeValue("blockType").equals("Text")) {
						Integer wordCount = 0;
						JSONObject blockJson = new JSONObject();
						Element textElement = block.getChild("text", abbyyNs);

						List<Element> parElements = textElement.getChildren();
						int lineCountPerBlock = 0;
						for (Element parElement : parElements) {
							List<Element> lineElements = parElement.getChildren();
							// System.err.println(lineElements.size());
							for (Element lineElement : lineElements) {
								StringBuilder wordSb = new StringBuilder();
								Integer lineTop = Integer.MAX_VALUE, lineLeft = Integer.MAX_VALUE,
										lineRight = Integer.MIN_VALUE, lineBottom = Integer.MIN_VALUE;

								Integer wordTop = Integer.MAX_VALUE, wordLeft = Integer.MAX_VALUE,
										wordRight = Integer.MIN_VALUE, wordBottom = Integer.MIN_VALUE;

								StringBuilder lineSb = new StringBuilder();
								List<Element> charElements = lineElement.getChild("formatting", abbyyNs).getChildren();

								for (Element character : charElements) {
									lineTop = character.getAttribute("t").getIntValue() < lineTop
											? character.getAttribute("t").getIntValue()
											: lineTop;
									lineLeft = character.getAttribute("l").getIntValue() < lineLeft
											? character.getAttribute("l").getIntValue()
											: lineLeft;
									lineRight = character.getAttribute("r").getIntValue() > lineRight
											? character.getAttribute("r").getIntValue()
											: lineRight;
									lineBottom = character.getAttribute("b").getIntValue() > lineBottom
											? character.getAttribute("b").getIntValue()
											: lineBottom;

									wordTop = character.getAttribute("t").getIntValue() < wordTop
											? character.getAttribute("t").getIntValue()
											: wordTop;
									wordLeft = character.getAttribute("l").getIntValue() < wordLeft
											? character.getAttribute("l").getIntValue()
											: wordLeft;
									wordRight = character.getAttribute("r").getIntValue() > wordRight
											? character.getAttribute("r").getIntValue()
											: wordRight;
									wordBottom = character.getAttribute("b").getIntValue() > wordBottom
											? character.getAttribute("b").getIntValue()
											: wordBottom;

									String charStr = character.getText();
									wordSb.append(charStr);
									if (charStr.matches(" ")) {
										addWordToJson(wordSb, wordTop, wordLeft, wordRight, wordBottom, blocks.size(),
												wordCount);

										wordCount++;
										wordSb.setLength(0);
										wordTop = Integer.MAX_VALUE;
										wordLeft = Integer.MAX_VALUE;
										wordRight = Integer.MIN_VALUE;
										wordBottom = Integer.MIN_VALUE;
									}
									lineSb.append(charStr);

								}
								addWordToJson(wordSb, wordTop, wordLeft, wordRight, wordBottom, blocks.size(),
										wordCount);
								wordCount++;
								// System.err.println(lineSb);
								blockSb.append(lineSb + "\n");
								JSONObject lineJson = new JSONObject();
								lineJson.put("text", lineSb.toString());
								lineJson.put("top", lineTop);
								lineJson.put("left", lineLeft);
								lineJson.put("right", lineRight);
								lineJson.put("bottom", lineBottom);
								blockJson.put("NoOfLines", lineCountPerBlock + 1);
								blockJson.put("line" + Integer.toString(lineCountPerBlock), lineJson);
								lineCountPerBlock++;
								// System.err.println("Line: " + lineSb);
							}
						}
						lineCoordinatesJson.put("block" + Integer.toString(blocks.size()), blockJson);
						String blockOfText = blockSb.toString();
						blocks.add(blockOfText);
						System.err.println("Block:\n" + blockOfText);
					}
				}
			}
			// System.out.println(wordCoordinatesJson.toString(2));
			// System.out.println(coordinatesJson.toString(4));
			// System.err.println(blocks);
		} catch (Exception e) {
			regex.newXmlParserWithLineWordCoord(file);
		}
		return blocks;
	}

	void hospitalDetailsExtract(List<String> blocks) {
		int matchFound = 0;
		for (int i = 0; i < blocks.size() && matchFound == 0; i++) {
			Matcher nameAndAddress = Pattern.compile(
					"(^(?i)(?!.*?f[li]go)(?=.*?(?:\\bdoctor\\b|\\bveterinary\\b|\\banimal\\b|\\bhospital\\b|\\bcare\\b|\\bpet |\\bclinic\\b|\\bvet\\b|\\bpharmacy\\b))(?:(?:^| )(?!\\S+(?:\\:))\\S+ ?){2,7}$)(?:(?i)(?!\\bveterinary\\b|\\banimal\\b|\\bhospital\\b|\\bvet\\b|\\bpets?\\b|clinic)[\\s\\S])*?(\\d+[-\\w()\\/^.,* #|\\n$&]* (?:[A-Z.]{1,2}|[.A-Za-z]+?),? ?(?:\\d{2,}-)?\\d{2,})",
					Pattern.MULTILINE).matcher(blocks.get(i));
			if (nameAndAddress.find()) {
				matchFound = 1;
				// System.err.println(blockOfText);
				JSONObject nameJson = getNameCoordinates(nameAndAddress.group(1).trim(), i);
				JSONObject addressJson = getAddressCoordinates(nameAndAddress.group(2).trim(), i);
				hospitalJson.put("hospitalName", nameJson);
				hospitalJson.put("hospitalAddress", addressJson);
				Matcher phone = Pattern.compile("(\\(?\\d{3}[\\/\\);.-]? ?\\d{3} ?[.\\-]? ?\\d{4})")
						.matcher(blocks.get(i));
				if (phone.find()) {
					JSONObject phoneJson = getPhoneCoordinates(phone.group(1).trim(), i);
					hospitalJson.put("hospitalPhone", phoneJson);
				} else {
					System.err.println("phone not found");
					seperateRegexes(new ArrayList<String>(blocks.subList(i, blocks.size() - 1)), true, true, false,
							blocks.size());
				}
			}
		}
		if (matchFound == 0) {
			seperateRegexes(blocks, false, false, false, blocks.size());
		}

	}

	private JSONObject getPhoneCoordinates(String extPhoneNumber, int blockNumber) {
		// System.err.println(blockNumber);
		String[] word = extPhoneNumber.split("\\s+");
		JSONObject phoneJson = new JSONObject();

		JSONObject phoneCoord = new JSONObject();
		Integer top = Integer.MAX_VALUE, left = Integer.MAX_VALUE, right = Integer.MIN_VALUE,
				bottom = Integer.MIN_VALUE;
		for (int i = 0, j = 0; j != word.length; i++) {
			JSONObject wordCoord = (JSONObject) wordCoordinatesJson
					.get("word" + Integer.toString(blockNumber) + "b" + Integer.toString(i) + "w");
			// System.err.println(wordCoord.getString("text"));
			if (wordCoord.getString("text").contains(word[j])) {
				j++;
				top = wordCoord.getInt("top") < top ? wordCoord.getInt("top") : top;
				left = wordCoord.getInt("left") < left ? wordCoord.getInt("left") : left;
				right = wordCoord.getInt("right") > right ? wordCoord.getInt("right") : right;
				bottom = wordCoord.getInt("bottom") > bottom ? wordCoord.getInt("bottom") : bottom;
			} else {
				j = 0;
				top = Integer.MAX_VALUE;
				left = Integer.MAX_VALUE;
				right = Integer.MIN_VALUE;
				bottom = Integer.MIN_VALUE;
			}
		}
		String phoneNumber = extPhoneNumber.replaceAll("\\.", " ").trim();
		phoneCoord.put("top", top);
		phoneCoord.put("left", left);
		phoneCoord.put("right", right);
		phoneCoord.put("bottom", bottom);
		phoneJson.put("text", phoneNumber);
		phoneJson.put("coordinates", phoneCoord);

		return phoneJson;

	}

	private JSONObject getAddressCoordinates(String extAddress, int blockNumber) {
		JSONObject addressJson = new JSONObject();
		JSONObject addressCoord = new JSONObject();
		String[] addr = extAddress.split("\\n");
		JSONObject blockJson = (JSONObject) lineCoordinatesJson.get("block" + Integer.toString(blockNumber));
		Integer lines = blockJson.getInt("NoOfLines");
		Integer top = Integer.MAX_VALUE, left = Integer.MAX_VALUE, right = Integer.MIN_VALUE,
				bottom = Integer.MIN_VALUE;
		for (int i = 0; i < addr.length; i++) {
			for (int j = 0; j < lines; j++) {
				JSONObject lineCoord = blockJson.getJSONObject("line" + Integer.toString(j));
				if (lineCoord.getString("text").contains(addr[i])) {
					top = lineCoord.getInt("top") < top ? lineCoord.getInt("top") : top;
					left = lineCoord.getInt("left") < left ? lineCoord.getInt("left") : left;
					right = lineCoord.getInt("right") > right ? lineCoord.getInt("right") : right;
					bottom = lineCoord.getInt("bottom") > bottom ? lineCoord.getInt("bottom") : bottom;
				}
			}
		}
		String hospitalAddress = extAddress.replaceAll("\\n|\\|", ",").replaceAll("[^()\\/A-Za-z-\\d,. ]", "")
				.replaceAll("\\s{2,}", "").replaceAll(",{2,}", ",").trim();
		addressCoord.put("top", top);
		addressCoord.put("left", left);
		addressCoord.put("right", right);
		addressCoord.put("bottom", bottom);
		addressJson.put("text", hospitalAddress);
		addressJson.put("coordinates", addressCoord);
		return addressJson;

	}

	private JSONObject getNameCoordinates(String extName, int blockNumber) {
		JSONObject nameJson = new JSONObject();
		JSONObject nameCoord = new JSONObject();
		JSONObject blockJson = (JSONObject) lineCoordinatesJson.get("block" + Integer.toString(blockNumber));
		Integer lines = blockJson.getInt("NoOfLines");
		for (int i = 0; i < lines; i++) {
			JSONObject lineCoord = blockJson.getJSONObject("line" + Integer.toString(i));
			if (lineCoord.getString("text").contains(extName)) {
				nameCoord.put("top", lineCoord.get("top"));
				nameCoord.put("left", lineCoord.get("left"));
				nameCoord.put("right", lineCoord.get("right"));
				nameCoord.put("bottom", lineCoord.get("bottom"));
			}
			String hospitalName = extName.replaceAll("[^A-Za-z-& ]", "").replaceAll("\\s{2,}", "").trim();
			nameJson.put("coordinates", nameCoord);
			nameJson.put("text", hospitalName);
		}
		// System.err.println(blockCoord.toString(2));
		return nameJson;
	}

	void seperateRegexes(List<String> blocks, boolean nameFound, boolean addressFound, boolean phoneFound,
			int wholeBlockSize) {

		System.err.println("Separate regex...");
		Integer add = blocks.size() == wholeBlockSize ? 0 : wholeBlockSize - blocks.size() - 1;
		// System.err.println(wholeBlockSize + " " + blocks.size() + " " + add);
		for (int i = 0; i < blocks.size()
				&& (phoneFound == false || addressFound == false || nameFound == false); i++) {
			String block = blocks.get(i);
			if (nameFound == false)
				nameFound = findHospitalName(block, i + add);

			else if (addressFound == false) {
				addressFound = findHospitalAddress(block, i + add);
				if (addressFound == false)
					findHospitalName(block, i + add);
				phoneFound = findHospitalPhone(block, i + add);
			} else
				phoneFound = findHospitalPhone(block, i + add);
		}
	}

	boolean findHospitalName(String block, int blockNumber) {
		Matcher hospitalName = Pattern.compile(
				"(^(?i)(?!.*?f[li]go)(?=.*?(?:\\bdoctor\\b|\\bpharmacy\\b|\\bveterinary\\b|\\banimal\\b|\\bhospital\\b|\\bcare\\b|\\bpet |\\bclinic\\b|\\bvet\\b))(?:(?:^| )(?!\\S+(?:\\:))\\S+ ?){2,7}$)",
				Pattern.MULTILINE | Pattern.CASE_INSENSITIVE).matcher(block);
		if (hospitalName.find()) {
			JSONObject nameJson = getNameCoordinates(hospitalName.group(1).trim(), blockNumber);
			hospitalJson.put("hospitalName", nameJson);
			return true;
		}
		return false;
	}

	boolean findHospitalAddress(String block, int blockNumber) {
		Matcher address = Pattern
				.compile("(\\d{2,}[-\\w()#\\/^.,* |\\n$&]*[, ](?:[A-Z]{1,2}|[A-Za-z]+?),? ?(?:\\d+-)?\\d+)")
				.matcher(block);

		if (address.find()) {
			JSONObject addressJson = getAddressCoordinates(address.group(1), blockNumber);
			hospitalJson.put("hospitalAddress", addressJson);
			return true;
		}
		return false;
	}

	boolean findHospitalPhone(String block, int blockNumber) {

		Matcher phone = Pattern.compile("(\\(?\\d{3}[\\/\\);.-]? ?\\d{3} ?[.\\-]? ?\\d{4})").matcher(block);
		if (phone.find()) {
			if (Pattern.compile("[^\\d]").matcher(phone.group(1).trim()).find()) {
				System.err.println(blockNumber);
				JSONObject phoneJson = getPhoneCoordinates(phone.group(1), blockNumber);
				hospitalJson.put("hospitalPhone", phoneJson);
				return true;
			}
		}
		return false;
	}
}