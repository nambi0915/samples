package demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractVariables {
    private static final List<String> KEYWORDS = Arrays.asList("abstract", "assert", "boolean",
	    "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do",
	    "double", "else", "extends", "false", "final", "finally", "float", "for", "goto", "if",
	    "implements", "import", "instanceof", "int", "interface", "long", "native", "new",
	    "null", "package", "private", "protected", "public", "return", "short", "static",
	    "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient",
	    "true", "try", "void", "volatile", "while");

    public static void main(String[] args) {
	if (args.length == 1) {
	    Set<String> variables = new HashSet<String>();
	    List<String> variablesFound = new ArrayList<String>();
	    String fileName = args[0];

	    String code = getFileContentsAsString(fileName);
	    variablesFound = findVariables(code);
	    for (String variable : variablesFound) {
		if (!KEYWORDS.contains(variable)) {
		    variables.add(variable);
		}
	    }
	    for (String variable : variables) {
		System.out.println(variable);
	    }
	} else {
	    System.err.println("Give the file path as argument");
	}
    }

    private static List<String> findVariables(String code) {
	List<String> variablesFound = new ArrayList<String>();
	Matcher variable = Pattern.compile(
		"(?!package.*)(( [_A-Za-z][A-Za-z0-9_]*)(?= ?=))|((?<=[^=]) ([_A-Za-z][A-Za-z0-9_]*(, )?)+(?=;))")
		.matcher(code);
	while (variable.find()) {
	    variablesFound.add(variable.group().trim());
	}
	return variablesFound;
    }

    private static String getFileContentsAsString(String fileName) {
	File javaFile = new File(fileName);
	try {
	    FileReader fileReader = new FileReader(javaFile);
	    BufferedReader bufferedReader = new BufferedReader(fileReader);
	    String temp = null;
	    StringBuilder fileContent = new StringBuilder();

	    while ((temp = bufferedReader.readLine()) != null) {
		fileContent.append(temp);
		fileContent.append("\n");
	    }
	    bufferedReader.close();

	    return fileContent.toString();

	} catch (Exception e) {
	    System.err.println(e);
	}
	return "";
    }
}
