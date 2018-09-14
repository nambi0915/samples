package main;

import java.util.ArrayList;
import java.util.List;

public class SeqinArray {
	public static void main(String args[]) {
		int[] a = { 1, 5, 7, 3, 6, 10, 9, 3, 6, 8, 3, 6, 7, 10, 9, 8 };
		int[] key = { 3, 6 };
		List<Integer> pos = new ArrayList<Integer>();
		for (int i = 0; i <= a.length - key.length; i++) {
			pos = getPosition(i, a, key);
			if (pos != null)
				System.err.println(pos);
		}
	}

	private static List<Integer> getPosition(int i, int[] a, int[] key) {
		int count = 0;
		List<Integer> p = new ArrayList<Integer>();
		for (int j = 0; i < a.length && j < key.length; i++, j++) {
			if (a[i] == key[j]) {
				count++;
				p.add(i);
			}
		}
		return count == key.length ? p : null;
	}
}
