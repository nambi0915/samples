package main;

import java.util.HashSet;
import java.util.Set;

public class Demo {
	public static void main(String args[]) throws InterruptedException {
		Set<Integer> s = new HashSet<Integer>();
		s.add(1);
		s.add(10);
		System.err.println(s);
	}
}