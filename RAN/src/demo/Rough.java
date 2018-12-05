package demo;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.regex.Pattern;

import otherdetails.OwnerDetailsParser;

public class Rough {
	public int o = 0;

	public static void main(String[] args) {
		String s = "aaaaaaaaabbbbbbbbbb";
		s = Pattern.compile("a").matcher(s).replaceAll("c");
		System.err.println(s);
	}
}
