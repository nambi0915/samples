import java.util.regex.Pattern;

public class Sample {
	public static void main(String args[]) {
		Boolean a = true, b = false;
		Boolean c = !a && b;
		Boolean d = a && !b;
		Pattern namePat = Pattern.compile("(?<=[NnAaMmeE]{4}:)\\s*[A-Z.a-z]+(\\s[A-Za-z]+)?\\.*");
		Pattern noPat = Pattern.compile("(?<=[NnOo]{2}:)\\s*[A-Z]{4}-[0-9]{4}-[A-Z]{4}-[0-9]{4}\\s*");
		Pattern declaration = Pattern.compile("[a-zA-Z]+\\s[A-Za-z0-9_]+(?=[;=])");
		Pattern chord = Pattern.compile("[A-G]{1}([5])?([#b]{1})?(mM|Mm|m|M)?([679]|11)?((sus|aug|dim|add)([24]))?");
		Pattern chord2 = Pattern.compile("\\[([A-G][#b]?(maj|m|M)?[27]?(add|aug|dim|sus)?[2-9]?)\\]");
		Pattern date = Pattern.compile("[A-Z][a-z]+\\s[0-9]{1,2},\\s[0-9]{4}");
		Pattern samp = Pattern
				.compile("(?<=[0-9]\\/[0-9]{2}\\/[0-9]{4}\\s)[a-zA-Z\\s0-9-:#&/]+(?=\\s+[0-9]+\\.[0-9]+)");
		if (!(a || b)) {
			System.out.println("both are false");
		}
		if (a && b) {
			System.out.println("both are true");
		}
		if (c) {
			System.out.println("b is true");
		}
		if (d) {
			System.out.println("a is true");
		}
	}
}
