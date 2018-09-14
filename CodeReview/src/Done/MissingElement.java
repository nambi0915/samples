package Done;

import java.util.Arrays;

public class MissingElement {
	public static void main(String args[]) {
		int[] A = { 1, 2, 3 };
		Arrays.sort(A);
		if (A.length == 0)
			System.err.println("1");
		for (int i = 1; i <= A.length + 1; i++) {
			if (Arrays.binarySearch(A, i) < 0)
				System.err.println(i);
		}
	}
}
