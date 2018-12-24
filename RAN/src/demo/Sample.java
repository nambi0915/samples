package demo;
import java.util.regex.Pattern;

public class Sample {
	public static void main(String args[]) {
		String s=" 2T 3T";
		System.err.println(s.replaceAll("(\\d)", "_$1_"));
		String word="2T";
		System.err.println(word.replaceAll("(.{" + 1 + "})", "_$1"));
	}
}
