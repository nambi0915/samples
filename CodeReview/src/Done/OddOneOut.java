package Done;

import java.util.Arrays;

public class OddOneOut {
	public static void main(String args[]) {
		int[] A = { 9, 3, 7, 3, 7, 7, 7, 9, 10, 10, 4 };
		Arrays.sort(A);
		System.err.println(Arrays.toString(A));
		for (int i = 0; i < A.length; i += 2) {
			if (i == A.length - 1)
				System.err.println(A[i]);
			else {
				if (A[i] != A[i + 1]) {
					System.err.println(A[i]);
					break;
				}
			}
		}
	}
}