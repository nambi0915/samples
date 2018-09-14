package main;

import java.util.concurrent.ThreadLocalRandom;

public class Rough {

	public static void main(String[] args) {
		int a;
		System.err.println(a = ThreadLocalRandom.current().nextInt(0, 100));
	}

}
